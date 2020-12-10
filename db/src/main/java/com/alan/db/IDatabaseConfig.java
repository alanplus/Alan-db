package com.alan.db;

import android.content.Context;

import com.alan.db.base.DbModel;
import com.alan.db.base.temp.BaseDAO;

import android.database.sqlite.SQLiteDatabase;

import java.util.List;

/**
 * @author Alan
 * 时 间：2019-11-26
 * 简 述：<功能简述>
 */
public interface IDatabaseConfig {

    /**
     * 获取数据库名
     *
     * @return
     * @author dushengjun
     */
    String getDatabaseName();

    /**
     * 获取数据库版本号
     *
     * @return
     * @author dushengjun
     */
    int getDatabaseVersion();

    /**
     * 获取数据库库中所有表的类
     *
     * @return
     * @author dushengjun
     */
    List<Class<? extends DbModel>> getTables(Context context);

    List<Class<? extends BaseDAO>> getTempTables(Context context);

    void attatch(SQLiteDatabase sqLiteDatabase);

    String getKey();

    boolean isDebug();

}
