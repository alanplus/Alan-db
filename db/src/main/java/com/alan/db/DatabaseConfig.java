package com.alan.db;

import android.content.Context;

/**
 * @author Alan
 * 时 间：2019-11-26
 * 简 述：<功能简述>
 */
public class DatabaseConfig {


    public static Context context;
    public static IDatabaseConfig iDatabaseConfig;

    public static void register(Context context, IDatabaseConfig iDatabaseConfig) {
        DatabaseConfig.context = context;
        DatabaseConfig.iDatabaseConfig = iDatabaseConfig;
    }

}
