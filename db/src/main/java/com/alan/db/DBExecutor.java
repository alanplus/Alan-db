package com.alan.db;


import android.content.ContentValues;
import android.text.TextUtils;
import android.util.SparseArray;

import com.alan.db.base.DbModel;
import com.alan.db.base.SQLiteManager;
import com.alan.db.converters.ColumnConverterFactory;
import com.alan.db.converters.IColumnConverter;
import com.alan.db.model.DBMap;
import com.alan.db.reflect.ObjectFactory;
import com.alan.db.table.Column;
import com.alan.db.table.Table;
import com.alan.db.table.TableFactory;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * @author Alan
 * 时 间：2019-11-26
 * 简 述：数据库执行类
 */
public class DBExecutor {

    private static final String[] ALL_COLS = new String[]{"*"};

    /**
     * 插入数据库对象
     *
     * @param t
     * @param <T>
     * @return
     */
    public static synchronized <T extends DbModel> boolean insert(T t) {
        if (t == null) {
            return true;
        }
        ContentValues contentValues = getContentValues(t);
        Table table = t.getTable();
        return getSQLiteDatabase().insert(table.getTableName(), null, contentValues) > 0;
    }


    /**
     * 批量插入
     *
     * @param list
     * @param <T>
     * @return
     */
    public static synchronized <T extends DbModel> boolean insert(List<T> list) {
        if (null == list) {
            return true;
        }
        SQLiteDatabase sqLiteDatabase = getSQLiteDatabase();
        sqLiteDatabase.beginTransaction();
        for (T t : list) {
            if (!insert(t)) {
                return false;
            }
        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        return true;
    }

    /**
     * 根据主键替换数据库对象
     *
     * @param t
     * @param <T>
     * @return
     */
    public static synchronized <T extends DbModel> boolean replace(T t) {
        if (null == t) {
            return true;
        }
        ContentValues contentValues = getContentValues(t);
        Table table = t.getTable();
        return getSQLiteDatabase().replace(table.getTableName(), null, contentValues) > 0;
    }

    /**
     * 批量替换
     *
     * @param list
     * @param <T>
     */
    public static synchronized <T extends DbModel> void replace(List<T> list) {
        if (null == list) {
            return;
        }
        SQLiteDatabase sqLiteDatabase = getSQLiteDatabase();
        sqLiteDatabase.beginTransaction();
        for (T t : list) {
            replace(t);
        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    /**
     * 根据主键替换数据库对象
     *
     * @param t
     * @param columns 根据对象的列名
     * @return
     */
    public static synchronized <T extends DbModel> boolean update(T t, String... columns) {
        if (null == t) {
            return true;
        }
        ContentValues contentValues = getContentValues(t);
        Table table = t.getTable();
        StringBuilder stringBuilder = new StringBuilder();
        String[] strings = whereArgs(t, stringBuilder, columns);
        return getSQLiteDatabase().update(table.getTableName(), contentValues, stringBuilder.toString(), strings) > 0;
    }

    /**
     * 根据主键 删除对象
     *
     * @param t
     * @param <T>
     * @return
     */
    public static synchronized <T extends DbModel> boolean delete(T t, String... columns) {
        if (null == t) {
            return true;
        }
        Table table = t.getTable();
        StringBuilder stringBuilder = new StringBuilder();
        String[] strings = whereArgs(t, stringBuilder, columns);
        return getSQLiteDatabase().delete(table.getTableName(), stringBuilder.toString(), strings) > 0;
    }

    /**
     * 批量删除
     *
     * @param columns
     * @param list
     * @param <T>
     * @return
     */
    public static synchronized <T extends DbModel> boolean delete(String[] columns, T... list) {
        if (list == null) {
            return true;
        }
        SQLiteDatabase sqLiteDatabase = getSQLiteDatabase();
        sqLiteDatabase.beginTransaction();
        for (T t : list) {
            if (!delete(t, columns)) {
                return false;
            }
        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        return true;
    }

    public static synchronized void delete(String sql, Object... objects) {
        getSQLiteDatabase().execSQL(sql, objects);
    }

    public static synchronized void handlerTransaction(OnHandlerTransactionListener onHandlerTransactionListener) {
        if (null == onHandlerTransactionListener) {
            return;
        }
        SQLiteDatabase sqLiteDatabase = getSQLiteDatabase();
        sqLiteDatabase.beginTransaction();
        onHandlerTransactionListener.onHandlerTransactisonListener();
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }

    /**
     * 删除表中的所有数据
     *
     * @param tClass
     */
    @SafeVarargs
    public static synchronized void deleteAll(Class<? extends DbModel>... tClass) {
        if (null == tClass || tClass.length == 0) {
            return;
        }
        SQLiteDatabase sqLiteDatabase = getSQLiteDatabase();
        sqLiteDatabase.beginTransaction();
        for (Class<? extends DbModel> t : tClass) {
            String tableName = TableFactory.getTableName(t);
            getSQLiteDatabase().delete(tableName, null, null);
        }
        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
    }


    public static synchronized <T extends DbModel> List<T> findAll(Class<T> tClass) {
        Table table = TableFactory.getTable(tClass);
        String sql = "select * from " + table.getTableName();
        return findList(tClass, sql);
    }

    public static synchronized <T extends DbModel> T find(Class<T> tClass, HashMap<String, String> map) {
        return find(tClass, map, null, null, null, null);
    }

    public static synchronized <T extends DbModel> List<T> findList(Class<T> tClass, HashMap<String, String> map) {
        return findList(tClass, map, null, null, null, null);
    }

    /**
     * 根据列表查询对象
     *
     * @param tClass
     * @param map
     * @param <T>
     * @return
     */
    public static synchronized <T extends DbModel> T find(Class<T> tClass, HashMap<String, String> map, String groupBy, String having, String orderBy, String limit) {
        Table table = TableFactory.getTable(tClass);
        StringBuilder stringBuilder = new StringBuilder();
        String[] objects = getQueryParams(stringBuilder, map);
        Cursor cursor = getSQLiteDatabase().query(table.getTableName(), ALL_COLS, stringBuilder.toString(), objects, groupBy, having, orderBy, limit);
        return getItem(tClass, cursor);
    }


    public static synchronized <T extends DbModel> List<T> findList(Class<T> tClass, HashMap<String, String> map, String groupBy, String having, String orderBy, String limit) {
        Table table = TableFactory.getTable(tClass);
        StringBuilder stringBuilder = new StringBuilder();
        String[] objects = getQueryParams(stringBuilder, map);
        Cursor cursor = getSQLiteDatabase().query(table.getTableName(), ALL_COLS, stringBuilder.toString(), objects, groupBy, having, orderBy, limit);
        return getList(tClass, cursor);
    }

    public static synchronized <T extends DbModel> void findList(Class<T> tClass, HashMap<String, String> map, String groupBy, String having, String orderBy, String limit, OnSqlExecutorCallback onSqlExecutorCallback) {
        Table table = TableFactory.getTable(tClass);
        StringBuilder stringBuilder = new StringBuilder();
        String[] objects = getQueryParams(stringBuilder, map);
        Cursor cursor = getSQLiteDatabase().query(table.getTableName(), ALL_COLS, stringBuilder.toString(), objects, groupBy, having, orderBy, limit);
        executor(cursor, onSqlExecutorCallback);
    }

    public static synchronized String[] getQueryParams(StringBuilder stringBuilder, HashMap<String, String> map) {
        String[] objects = new String[map.size()];
        int i = 0;
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(" and ");
            }
            stringBuilder.append(entry.getKey()).append("=?");
            objects[i++] = entry.getValue();
        }
        return objects;
    }

    private static synchronized <T extends DbModel> T getItem(Class<T> tClass, Cursor cursor) {

        ObjectFactory<T> factory = new ObjectFactory<>(tClass);
        Table table = TableFactory.getTable(tClass);
        try {
            if (null != cursor && cursor.getCount() > 0 && cursor.moveToFirst()) {
                T t = getModelByCursor(table, cursor, factory);
                return t;
            }
            return null;
        } catch (Exception e) {
            LogUtil.error(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    private static synchronized <T extends DbModel> List<T> getList(Class<T> tClass, Cursor cursor) {
        List<T> list = new ArrayList<>();
        try {
            if (null != cursor && cursor.getCount() > 0) {
                ObjectFactory<T> factory = new ObjectFactory<>(tClass);
                Table table = TableFactory.getTable(tClass);
                while (cursor.moveToNext()) {
                    T t = getModelByCursor(table, cursor, factory);
                    if (t != null) {
                        list.add(t);
                    }
                }
            }
        } catch (Exception e) {
            LogUtil.error(e);
        } finally {
            cursor.close();
        }
        return list;
    }

    /**
     * 查找一个对象
     *
     * @param tClass
     * @param sql
     * @param <T>
     * @return
     */
    public static synchronized <T extends DbModel> T find(Class<T> tClass, String sql) {
        return find(tClass, sql, new Object[0]);
    }

    public static synchronized <T extends DbModel> T find(Class<T> tClass, String sql, Object... args) {
        Cursor cursor = getSQLiteDatabase().rawQuery(sql, getArgs(args));
        return getItem(tClass, cursor);
    }

    public static synchronized <T extends DbModel> List<T> query(Class<T> tClass, String where, Object... args) {
        return query(tClass, where, null, null, null, args);
    }

    public static synchronized <T extends DbModel> List<T> query(Class<T> tClass, String where, String group, String having, String order, Object... args) {
        Table table = TableFactory.getTable(tClass);
        String[] strArgs = new String[null == args ? 0 : args.length];
        for (int i = 0; i < args.length; i++) {
            strArgs[i] = String.valueOf(args[i]);
        }
        String[] ALL_COLS = new String[]{"*"};
        Cursor cursor = getSQLiteDatabase().query(table.getTableName(), ALL_COLS, where, strArgs, group, having, order);
        return getList(tClass, cursor);
    }

    /**
     * 查找多个对象
     *
     * @param tClass
     * @param sql
     * @param <T>
     * @return
     */
    public static synchronized <T extends DbModel> List<T> findList(Class<T> tClass, String sql) {
        return findList(tClass, sql, new Object[0]);
    }

    public static synchronized <T extends DbModel> List<T> findList(Class<T> tClass, String sql, Object... args) {
        Cursor cursor = getSQLiteDatabase().rawQuery(sql, getArgs(args));
        return getList(tClass, cursor);
    }

    private static synchronized <T extends DbModel> T getModelByCursor(Table table, Cursor cursor, ObjectFactory<T> objectFactory) throws Exception {
        T t = objectFactory.newInstance();
        Map<String, Column> map = table.getColumns();
        for (int columnIndex = 0, count = cursor.getColumnCount(); columnIndex < count; columnIndex++) {
            String columnName = cursor.getColumnName(columnIndex);
            // 通过字段名字得到对应的java类中的字段
            Column column = map.get(columnName);
            if (column != null) {
                // 得到字段的转化器
                IColumnConverter converter = column.getColumnConverter();
                // 从cursor中得到值
                Object value = converter.getDBType().getValueFromCursor(cursor, columnIndex);
                // 从sql类型的数据转化成java类保存的数据类型
                value = converter.toJavaValue(value);
                // 把value值设置在result实例里
                column.getColumnField().set(t, value);
            }
        }
        return t;
    }

    private static String[] getArgs(Object... args) {
        String[] array = null;
        if (null != args || args.length == 0) {
            array = new String[args.length];
            for (int i = 0; i < args.length; i++) {
                array[i] = String.valueOf(args[i]);
            }
        }
        return array;
    }

    public static synchronized SQLiteDatabase getSQLiteDatabase() {
        return SQLiteManager.getDB(DatabaseConfig.context, DatabaseConfig.iDatabaseConfig);
    }

    public static synchronized <T extends DbModel> String[] whereArgs(T t, StringBuilder stringBuilder, String... columns) {
        List<String> list = new ArrayList<>();
        if (columns == null) {
            Collection<Column> values = t.getTable().getColumns().values();
            for (Column column : values) {
                if (column.isPrimaryKey()) {
                    if (stringBuilder.length() > 0) {
                        stringBuilder.append(" and ");
                    }
                    stringBuilder.append(column.getName()).append("=?");
                    list.add(getValue(t, column));
                }
            }

        } else {
            Map<String, Column> map = t.getTable().getColumns();
            for (String columnStr : columns) {
                Column column = map.get(columnStr);
                if (stringBuilder.length() > 0) {
                    stringBuilder.append(" and ");
                }
                stringBuilder.append(columnStr).append("=?");
                list.add(getValue(t, column));
            }
        }
        String[] array = new String[list.size()];
        list.toArray(array);
        return array;
    }

    private static synchronized <T extends DbModel> String getValue(T t, Column column) {
        try {
            return convertToStringValues(column.getColumnField().get(t));
        } catch (IllegalAccessException e) {
            LogUtil.error(e);
        }
        return "";
    }

    private static synchronized <T extends DbModel> ContentValues getContentValues(T t) {
        ContentValues contentValues = new ContentValues();
        Table table = t.getTable();
        Collection<Column> values = table.getColumns().values();
        for (Column column : values) {
            String value = getValue(t, column);
            if (column.isAutoIncrement() && (TextUtils.isEmpty(value) || "0".equals(value))) {
                continue;
            }
            contentValues.put(column.getName(), value);
        }
        return contentValues;
    }

    private static String convertToStringValues(Object value) {
        /**
         * 把绑定的值转化为 字符串形式的绑定的值，但是必须先进行转化器转化一下
         */
        // 得到对应类型的转化器
        IColumnConverter converter = ColumnConverterFactory.getColumnConverter(value);
        // 从java类型的数据转化成sql保存的数据类�?
        value = converter.toSqlValue(value);
        // 转化为字符串
        return String.valueOf(value);
    }

    public synchronized static void executor(String sql, OnSqlExecutorCallback onSqlExecutorCallback) {
        executor(sql, onSqlExecutorCallback, new Object[0]);
    }

    public synchronized static <T extends DbModel> T executor(String sql, final Class<T> clazz) {
        final List<T> list = new ArrayList<>();
        executor(sql, new OnSqlExecutorCallback() {
            @Override
            public void onSqlExecutorCallback(Cursor cursor) {
                T t = getObjectByCursor(clazz, cursor);
                if (t != null) {
                    list.add(t);
                }
            }
        }, new Object[0]);
        return list.size() == 0 ? null : list.get(0);
    }

    public synchronized static <T extends DbModel> List<T> executorList(String sql, final Class<T> clazz) {
        final List<T> list = new ArrayList<>();
        executor(sql, new OnSqlExecutorCallback() {
            @Override
            public void onSqlExecutorCallback(Cursor cursor) {
                T t = getObjectByCursor(clazz, cursor);
                if (t != null) {
                    list.add(t);
                }
            }
        }, new Object[0]);
        return list;
    }


    public synchronized static void executorSQL(String sql) {
        getSQLiteDatabase().execSQL(sql);
    }

//    public synchronized static <T> T executor(String sql, OnSqlCallback<T> onSqlExecutorCallback) {
//        executor(sql, onSqlExecutorCallback, new Object[0]);
//    }

    public synchronized static void executor(Cursor cursor, OnSqlExecutorCallback onSqlExecutorCallback) {
        try {
            if (null != cursor && cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    if (null != onSqlExecutorCallback) {
                        onSqlExecutorCallback.onSqlExecutorCallback(cursor);
                    }
                }
            }
        } catch (Exception e) {

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public synchronized static void executor(String sql, OnSqlExecutorCallback onSqlExecutorCallback, Object... args) {
        Cursor cursor = getSQLiteDatabase().rawQuery(sql, getArgs(args));
        executor(cursor, onSqlExecutorCallback);
    }

//    public synchronized static <T>  T execSql(String sql, OnSqlCallback<T> onSqlCallback, Object... args) {
//        Cursor cursor = getSQLiteDatabase().rawQuery(sql, getArgs(args));
//        executor(cursor, onSqlExecutorCallback);
//    }

    public static <T extends DbModel> T getObjectByCursor(Class<T> tClass, Cursor cursor) {
        if (null == cursor) {
            return null;
        }
        ObjectFactory<T> factory = new ObjectFactory<>(tClass);
        Table table = TableFactory.getTable(tClass);
        try {
            return getModelByCursor(table, cursor, factory);
        } catch (Exception ignore) {

        }
        return null;
    }

    public interface OnSqlExecutorCallback {
        void onSqlExecutorCallback(Cursor cursor);
    }


    public interface OnSqlCallback<T> {
        T onSqlExecutorCallback(Cursor cursor);
    }

    public interface OnHandlerTransactionListener {
        void onHandlerTransactisonListener();
    }

    public List<DBMap> findList(String sql) {
        Cursor cursor = getSQLiteDatabase().rawQuery(sql, null);
        List<DBMap> list = new ArrayList<>();
        if (null == cursor) {
            return list;
        }
        while (cursor.moveToNext()) {
            DBMap map = new DBMap(cursor);
            list.add(map);
        }
        cursor.close();
        return list;
    }

    public DBMap find(String sql) {
        Cursor cursor = getSQLiteDatabase().rawQuery(sql, null);
        if (null == cursor) {
            return null;
        }
        try {
            if (cursor.moveToFirst()) {
                return new DBMap(cursor);
            }
        } catch (Exception e) {
            LogUtil.error(e);
        } finally {
            cursor.close();
        }
        return null;
    }
}
