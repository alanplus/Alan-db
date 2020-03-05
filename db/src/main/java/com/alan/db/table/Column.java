package com.alan.db.table;

import com.alan.db.converters.ColumnConverterFactory;
import com.alan.db.converters.IColumnConverter;

import java.lang.reflect.Field;

/**
 * @author Alan
 * 时 间：2019-11-26
 * 简 述：<功能简述>
 */
public class Column {

    /**
     * 字段名
     */
    private String name;
    /**
     * 字段的反射
     */
    private Field columnfield;
    /**
     * 类型转化
     */
    private IColumnConverter columnConverter;
    /**
     * 默认值
     */
    private String defaultValue;

    private boolean isPrimaryKey;

    private boolean autoIncrement;

    public Column(Field columnfield) {
        this(columnfield, ColumnUtils.getColumnName(columnfield));
    }

    public Column(Field columnfield, String name) {
        super();
        this.name = name;
        this.columnfield = columnfield;
        columnConverter = ColumnConverterFactory.getColumnConverter(columnfield
                .getType());
        defaultValue = ColumnUtils.getDefaultValue(columnfield,columnConverter);
    }

    public String getName() {
        return name;
    }

    public Field getColumnField() {
        return columnfield;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public IColumnConverter getColumnConverter() {
        return columnConverter;
    }

    public boolean isPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

    public void setAutoIncrement(boolean autoIncrement) {
        this.autoIncrement = autoIncrement;
        this.isPrimaryKey = true;
    }

    public boolean isAutoIncrement() {
        return autoIncrement;
    }
}
