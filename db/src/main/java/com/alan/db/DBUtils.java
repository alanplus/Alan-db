package com.alan.db;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;

/**
 * @author Alan
 * 时 间：2020-03-10
 * 简 述：<功能简述>
 */
public class DBUtils {

    public static void encrypt(Context context, String dbName, String passphrase) throws IOException {
        File originalFile = context.getDatabasePath(dbName);
        if (originalFile.exists()) {
            File newFile = File.createTempFile("sqlcipherutils", "tmp", context.getCacheDir());
            SQLiteDatabase db = SQLiteDatabase.openDatabase(originalFile.getAbsolutePath(), "", null, SQLiteDatabase.OPEN_READWRITE);
            db.rawExecSQL(String.format("ATTACH DATABASE '%s' AS encrypted KEY '%s';", newFile.getAbsolutePath(), passphrase));
            db.rawExecSQL("SELECT sqlcipher_export('encrypted')");
            db.rawExecSQL("DETACH DATABASE encrypted;");
            int version = db.getVersion();
            db.close();
            db = SQLiteDatabase.openDatabase(newFile.getAbsolutePath(), passphrase, null, SQLiteDatabase.OPEN_READWRITE);
            db.setVersion(version);
            db.close();
            originalFile.delete();
            newFile.renameTo(originalFile);
        }
    }

    public static void copyDataBase(@NonNull Context context, String name) throws Exception {
        copyDataBase(context, name, name);
    }

    public static void copyDataBase(@NonNull Context context, String name, String destName) throws Exception {
        int n = 1;
        Exception exception = null;
        while (n < 4) {
            try {
                copyDataBase2(context, name, destName);
            } catch (Exception e) {
                n++;
                exception = e;
                continue;
            }
            exception = null;
            break;
        }
        if (null != exception) {
            throw exception;
        }
    }

    private static void copyDataBase2(@NonNull Context context, String name, String destName) throws Exception {
        // 打开 文件
        InputStream myInput = context.getAssets().open(name);
        // 获取目标文件
        File file = context.getDatabasePath(destName);
        //获取目标文件的文件夹
        File parentFile = file.getParentFile();
        // 创建文件夹
        if (!parentFile.exists()) {
            boolean mkdirs = parentFile.mkdirs();
            if (!mkdirs) {
                throw new Exception("文件夹创建失败");
            }
        }

        if (file.exists()) {
            boolean delete = file.delete();
            if (!delete) {
                throw new Exception("文件删除失败");
            }
        }

        if (!file.exists()) {
            boolean newFile = file.createNewFile();
            if (!newFile) {
                throw new Exception("文件创建失败");
            }
        }

        OutputStream myOutput = new FileOutputStream(file);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public static boolean checkAndroidDataBase(Context context, String name) {
        android.database.sqlite.SQLiteDatabase sqLiteDatabase = null;
        try {
            File file = context.getDatabasePath(name);
            sqLiteDatabase = android.database.sqlite.SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, 0, null);
            return sqLiteDatabase.isOpen();
        } catch (Exception e) {
            return false;
        } finally {
            if (null != sqLiteDatabase) {
                sqLiteDatabase.close();
            }
        }
    }

    public static boolean checkSqlCipherDataBase(Context context, String name, String password) {
        SQLiteDatabase sqLiteDatabase = null;
        try {
            File file = context.getDatabasePath(name);
            sqLiteDatabase = SQLiteDatabase.openDatabase(file.getAbsolutePath(), password, null, 0);
            return sqLiteDatabase.isOpen();
        } catch (Exception e) {
            return false;
        } finally {
            if (null != sqLiteDatabase) {
                sqLiteDatabase.close();
            }
        }
    }
}
