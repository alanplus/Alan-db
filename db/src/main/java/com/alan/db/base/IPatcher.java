package com.alan.db.base;


import net.sqlcipher.database.SQLiteDatabase;

public interface IPatcher<T> {
    /**
     * 原有数据库版本 小于等于这个值都将被执行
     *
     * @return
     */
    int getSupportMaxVersion();

    /**
     * 执行数据库补丁
     *
     * @param database
     */
    void execute(SQLiteDatabase database, Class<T> tClass);
}
