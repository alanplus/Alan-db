package com.alan.db;

import com.alan.db.base.DbModel;
import com.alan.db.table.TableFactory;

import java.util.HashMap;
import java.util.List;

/**
 * @author Alan
 * 时 间：2019-12-26
 * 简 述：<功能简述>
 */
public class DBBuilder<T extends DbModel> {

    private HashMap<String, String> hashMap;
    private boolean isList;
    private Class<T> tClass;

    private String order;
    private String group;
    private String having;
    private String limit;

    private DBExecutor.OnSqlExecutorCallback onSqlExecutorCallback;

    private DBBuilder(Class<T> tClass) {
        this.tClass = tClass;
        this.hashMap = new HashMap<>();
    }


    public static <T extends DbModel> DBBuilder<T> get(Class<T> tClass) {
        return new DBBuilder<>(tClass);
    }


    public DBBuilder<T> addParams(String key, int value) {
        hashMap.put(key, String.valueOf(value));
        return this;
    }

    public DBBuilder<T> addParams(String key, long value) {
        hashMap.put(key, String.valueOf(value));
        return this;
    }

    public DBBuilder<T> addParams(String key, String value) {
        hashMap.put(key, value);
        return this;
    }

    public DBBuilder<T> addParams(String key, boolean value) {
        hashMap.put(key, String.valueOf(value));
        return this;
    }

    public DBBuilder<T> addParams(String key, char value) {
        hashMap.put(key, String.valueOf(value));
        return this;
    }


    public DBBuilder<T> addHashMap(HashMap<String, String> hashMap) {
        this.hashMap.putAll(hashMap);
        return this;
    }

    public DBBuilder<T> order(String order) {
        this.order = order;
        return this;
    }

    public DBBuilder<T> group(String group) {
        this.group = group;
        return this;
    }

    public DBBuilder<T> having(String having) {
        this.having = having;
        return this;
    }

    public DBBuilder<T> limit(String limit) {
        this.limit = limit;
        return this;
    }

    public DBBuilder<T> setOnSqlExecutorCallback(DBExecutor.OnSqlExecutorCallback onSqlExecutorCallback) {
        this.onSqlExecutorCallback = onSqlExecutorCallback;
        return this;
    }

    public T executor() {
        return DBExecutor.find(this.tClass, hashMap);
    }

    public List<T> executorList() {
        return DBExecutor.findList(this.tClass, hashMap, group, having, order, limit);
    }

    public void executor(DBExecutor.OnSqlExecutorCallback onSqlExecutorCallback) {
        DBExecutor.findList(this.tClass, hashMap, group, having, order, limit, onSqlExecutorCallback);
    }

    public void delete() {
        String tableName = TableFactory.getTableName(tClass);
        if(hashMap==null||hashMap.size()==0){
            DBExecutor.deleteAll(tClass);
            return;
        }
        StringBuilder stringBuilder = new StringBuilder();
        String[] params = DBExecutor.getQueryParams(stringBuilder, hashMap);
        DBExecutor.getSQLiteDatabase().delete(tableName, stringBuilder.toString(), params);

    }
}
