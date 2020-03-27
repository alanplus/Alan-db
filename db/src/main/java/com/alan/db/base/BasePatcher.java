package com.alan.db.base;


import com.alan.db.table.PatcherHelper;

import net.sqlcipher.database.SQLiteDatabase;

/**
 * @author Alan
 * 时 间：2019-11-27
 * 简 述：<功能简述>
 */
public abstract class BasePatcher<T extends DbModel> implements IPatcher {


    /**
     * 添加列
     *
     * @param database
     * @param tClass
     * @param columns
     */
    public void update(SQLiteDatabase database, Class<T> tClass, String[] columns) {
        PatcherHelper.alertTableForAdd(columns, database, tClass);
    }

    /**
     * 创建表
     * @param database
     * @param tClass
     */
    public void create(SQLiteDatabase database, Class<T> tClass) {
        PatcherHelper.createTable(tClass, database);

    }
}
