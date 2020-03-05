package com.alan.db.table;


import com.alan.db.LogUtil;
import com.alan.db.base.DbModel;

import net.sqlcipher.database.SQLiteDatabase;

import java.util.Map;

/**
 * @author Alan
 * 时 间：2019-11-27
 * 简 述：<功能简述>
 */
public class PatcherHelper {

    public static <T extends DbModel> void alertTableForAdd(String[] columns, SQLiteDatabase sqLiteDatabase, Class<T> tClass) {
        Table table = TableFactory.getTable(tClass);
        Map<String, Column> columnsMap = table.getColumns();
        for (String column : columns) {
            Column c = columnsMap.get(column);
            if (c != null) {
                addColumn(table.getTableName(), c, sqLiteDatabase);
            }
        }
    }


    private static void addColumn(String tableName, Column column, SQLiteDatabase sqLiteDatabase) {
        String sqlFormat = "ALTER TABLE %s ADD COLUMN %s %s";
        String sql = String.format(sqlFormat, tableName, column.getName(), column.getColumnConverter().getDBType().name());
        LogUtil.d(sql);
        sqLiteDatabase.execSQL(sql);
    }
}
