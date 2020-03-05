package com.alan.db.model;

import com.alan.db.DBExecutor;
import com.alan.db.LogUtil;

import java.util.HashMap;

/**
 * @author Alan
 * 时 间：2019-11-27
 * 简 述：<功能简述>
 */
public class BaseConfigManger {


    protected int getInt(String key, int defaultValue) {
        Config config = getConfig(key);
        try {
            return null != config ? Integer.valueOf(config.getValue()) : defaultValue;
        } catch (Exception e) {
            LogUtil.error(e);
        }
        return defaultValue;
    }

    protected boolean getBool(String key, boolean defaultValue) {
        Config config = getConfig(key);
        try {
            return null != config ? Boolean.valueOf(config.getValue()) : defaultValue;
        } catch (Exception e) {
            LogUtil.error(e);
        }
        return defaultValue;
    }

    protected long getLong(String key, long defaultValue) {
        Config config = getConfig(key);
        try {
            return null != config ? Long.valueOf(config.getValue()) : defaultValue;
        } catch (Exception e) {
            LogUtil.error(e);
        }
        return defaultValue;
    }

    protected String getString(String key, String defaultValue) {
        Config config = getConfig(key);
        return null != config ? config.getValue() : defaultValue;
    }

    protected void setValue(String key, String value) {
        Config config = new Config();
        config.setKey(key);
        config.setValue(value);
        DBExecutor.replace(config);
    }


    protected Config getConfig(String keyValue) {
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("key", keyValue);
        return DBExecutor.find(Config.class, hashMap);
    }


}
