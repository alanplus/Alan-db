package com.alan.db.base;

import android.content.Context;

import com.alan.db.IDatabaseConfig;
import com.alan.db.LogUtil;
import com.alan.db.annotations.Patcher;
import com.alan.db.base.temp.BaseDAO;
import com.alan.db.table.Table;
import com.alan.db.table.TableFactory;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.List;

/**
 * sqlite数据库管理类，负责表的初始化和表的更
 *
 * @author Alan
 */
public class SQLiteManager extends SQLiteOpenHelper {
    protected static volatile SQLiteManager instance;
    protected Context mContext;

    protected static IDatabaseConfig mConfig;
    protected static volatile SQLiteDatabase db;

    protected SQLiteManager(Context context, String name, int version) {
        super(context, name, null, version);
        mContext = context;
    }

    public static synchronized SQLiteDatabase getDB(Context context,
                                                    IDatabaseConfig config) {
        if (db == null) {
            mConfig = config;
            if (instance == null) {
                instance = new SQLiteManager(context, config.getDatabaseName(),
                        config.getDatabaseVersion());
            }
            db = instance.getWritableDatabase(config.getKey());

            config.attatch(db);
//
//            try {
//                String attachDatabaseName = config.getAttachDatabaseName();
//
//                if (!TextUtils.isEmpty(attachDatabaseName)) {
//                    db.execSQL(String.format("attach DATABASE '/data/data/%s/databases/%s' AS 'c'", context.getPackageName(), attachDatabaseName));
//                }
//            } catch (Exception e) {
//                Logger.e(Log.getStackTraceString(e));
//            }
        }
        return db;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        LogUtil.d("表：" + "开始创建");
        List<Class<? extends DbModel>> classes = mConfig.getTables(mContext);
        for (Class<? extends DbModel> clazz : classes) {
            try {
                Table table = TableFactory.getTable(clazz);
                String sql = table.getCreateSqlStr();
                LogUtil.d(sql);
                db.execSQL(sql);
                LogUtil.d("表" + table.getTableName());
            } catch (Exception e) {
                LogUtil.error(e);
            }
        }
        createTempTable(db);
    }

    public void createTempTable(SQLiteDatabase sqLiteDatabase) {

        List<Class<? extends BaseDAO>> classes = mConfig.getTempTables(mContext);
        for (Class<? extends BaseDAO> clazz : classes) {
            try {
                Constructor<? extends BaseDAO> con = clazz
                        .getConstructor(Context.class);
                BaseDAO table = con.newInstance(mContext);
                table.onCreate(sqLiteDatabase);
            } catch (Exception e) {
            }
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        List<Class<? extends DbModel>> classes = mConfig.getTables(mContext);
        LogUtil.d("onUpgrade");
        for (Class<? extends DbModel> clazz : classes) {
            try {
                Patcher annotation = clazz.getAnnotation(Patcher.class);
                Class<? extends IPatcher>[] name = annotation.name();
                if (null != name && name.length > 0) {
                    for (Class<? extends IPatcher> patcher : name) {
                        try {
                            LogUtil.d("onUpgrade step find");
                            IPatcher inst = patcher.newInstance();
                            int max = inst.getSupportMaxVersion();
                            if (oldVersion < max) {
                                LogUtil.d("onUpgrade step execute");
                                inst.execute(db, clazz);
                            }
                        } catch (Exception e) {
                            LogUtil.error(e);
                        }
                    }
                }
            } catch (Exception e) {
                LogUtil.error(e);
            }
        }
    }


    public static void destroy() {
        if (null != db) {
            db.close();
            db = null;
            instance = null;
        }
    }

    public static void encryption(Context context, File file) {
        if (mConfig == null) {
            return;
        }
        try {
            File tempFile = File.createTempFile("sqlcipherutils", "tmp", context.getCacheDir());
            SQLiteDatabase db = SQLiteDatabase.openDatabase(file.getAbsolutePath(), "", null, SQLiteDatabase.OPEN_READWRITE);
            db.rawExecSQL(String.format("ATTACH DATABASE '%s' AS encrypted KEY '%s';", tempFile.getAbsolutePath(), mConfig.getKey()));
            db.rawExecSQL("SELECT sqlcipher_export('encrypted')");
            db.rawExecSQL("DETACH DATABASE encrypted;");
            int version = db.getVersion();
            db.close();

            db = SQLiteDatabase.openDatabase(tempFile.getAbsolutePath(), mConfig.getKey(), null, SQLiteDatabase.OPEN_READWRITE);
            db.setVersion(version);
            db.close();

            file.delete();
            tempFile.renameTo(file);
        } catch (IOException e) {
            LogUtil.error(e);
        }
    }

    public static void attach(File file, SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("attach DATABASE '" + file.getAbsolutePath() + "' AS 'c' KEY '" + mConfig.getKey() + "'");
    }
}
