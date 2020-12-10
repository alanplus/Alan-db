package com.alan.db.base;


import com.alan.db.table.PatcherHelper;

import android.database.sqlite.SQLiteDatabase;

/**
 * @author Alan
 * 时 间：2019-11-27
 * 简 述：<功能简述>
 */
public abstract class BasePatcher implements IPatcher {

    /**
     * 添加列
     *
     * @param database
     * @param tClass
     * @param columns
     */
    public <T extends DbModel> void update(SQLiteDatabase database, Class<T> tClass, String[] columns) {
        PatcherHelper.alertTableForAdd(columns, database, tClass);
    }

    /**
     * 创建表
     * @param database
     * @param tClass
     */
    public <T extends DbModel> void create(SQLiteDatabase database, Class<T> tClass) {
        PatcherHelper.createTable(tClass, database);

    }
}
