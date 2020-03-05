package com.alan.db.model;

import com.alan.db.annotations.PrimaryKey;
import com.alan.db.annotations.Table;
import com.alan.db.base.DbModel;

/**
 * @author Alan
 * 时 间：2019-11-27
 * 简 述：<功能简述>
 */
@Table(name = "config")
public class Config extends DbModel {

    @PrimaryKey
    private String key;
    private String value;

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
