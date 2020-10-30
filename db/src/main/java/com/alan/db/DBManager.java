package com.alan.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.HashMap;

/**
 * @author Alan
 * 时 间：2020/10/30
 * 简 述：<功能简述>
 */
public class DBManager {

    private static HashMap<String, SQLiteDatabase> dbMap = new HashMap<>();

    public static void registerDefaultDB(Context context, String dbName) {
        File file = context.getDatabasePath(dbName);
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(file, null);
        dbMap.put("default_db", sqLiteDatabase);
        dbMap.put(dbName, sqLiteDatabase);
    }

    public static SQLiteDatabase getDefaultDatabase() {
        return dbMap.get("default_db");
    }

    public static SQLiteDatabase getDatabase(Context context, String name) {
        if (dbMap.containsKey(name)) {
            return dbMap.get(name);
        }
        File file = context.getDatabasePath(name);
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(file, null);
        dbMap.put(name, sqLiteDatabase);
        return sqLiteDatabase;
    }

    public static void destroy() {
        for (String s : dbMap.keySet()) {
            SQLiteDatabase sqLiteDatabase = dbMap.get(s);
            if (null != sqLiteDatabase && sqLiteDatabase.isOpen()) {
                sqLiteDatabase.close();
            }
        }
        dbMap.clear();
    }
}
