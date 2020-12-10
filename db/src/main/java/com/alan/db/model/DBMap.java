package com.alan.db.model;

import com.alan.db.LogUtil;

import android.database.Cursor;

import java.util.HashMap;

/**
 * @author Alan
 * 时 间：2020-05-06
 * 简 述：<功能简述>
 */
public class DBMap {

    private HashMap<String, DBItem> map;

    public DBMap() {
        map = new HashMap<>();
    }

    public DBMap(Cursor cursor) {
        map = new HashMap<>();
        if (null == cursor) {
            return;
        }

        String[] columnNames = cursor.getColumnNames();
        if (null == columnNames || columnNames.length == 0) {
            return;
        }

        for (String name : columnNames) {
            int index = cursor.getColumnIndex(name);
            int type = cursor.getType(cursor.getColumnIndex(name));
            String value = cursor.getString(index);
            map.put(name, new DBItem(type, value));
        }

    }

    public int getInt(String key, int defaultValue) {
        if (!map.containsKey(key)) {
            return defaultValue;
        }

        DBItem dbItem = map.get(key);
        if (dbItem == null) {
            return defaultValue;
        }
        try {
            return (int) dbItem.value;
        } catch (Exception e) {
            LogUtil.error(e);
        }
        return defaultValue;
    }

    public float getFloat(String key, float defaultValue) {
        if (!map.containsKey(key)) {
            return defaultValue;
        }

        DBItem dbItem = map.get(key);
        if (dbItem == null) {
            return defaultValue;
        }
        try {
            return (float) dbItem.value;
        } catch (Exception e) {
            LogUtil.error(e);
        }
        return defaultValue;
    }

    public String getString(String key, String defaultValue) {
        if (!map.containsKey(key)) {
            return defaultValue;
        }

        DBItem dbItem = map.get(key);
        if (dbItem == null) {
            return defaultValue;
        }
        try {
            return (String) dbItem.value;
        } catch (Exception e) {
            LogUtil.error(e);
        }
        return defaultValue;
    }

    public boolean getBool(String key, boolean defaultValue) {
        if (!map.containsKey(key)) {
            return defaultValue;
        }

        DBItem dbItem = map.get(key);
        if (dbItem == null) {
            return defaultValue;
        }
        try {
            return (boolean) dbItem.value;
        } catch (Exception e) {
            LogUtil.error(e);
        }
        return defaultValue;
    }

    public long getLong(String key, long defaultValue) {
        if (!map.containsKey(key)) {
            return defaultValue;
        }

        DBItem dbItem = map.get(key);
        if (dbItem == null) {
            return defaultValue;
        }
        try {
            return (long) dbItem.value;
        } catch (Exception e) {
            LogUtil.error(e);
        }
        return defaultValue;
    }

    public void put(String key, Object o) {
        map.put(key, new DBItem(5, o));
    }

    public void remove(String key) {
        map.remove(key);
    }
}
