package com.alan.db.base;

import android.database.sqlite.SQLiteDatabase;

import com.alan.db.table.PatcherHelper;

/**
 * @author Alan
 * 时 间：2019-11-27
 * 简 述：<功能简述>
 */
public abstract class BasePatcher implements IPatcher {


    public <T extends DbModel> void execute(SQLiteDatabase database, Class<T> tClass, String[] columns) {
        PatcherHelper.alertTableForAdd(columns, database, tClass);
    }
}
