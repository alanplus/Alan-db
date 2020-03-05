package com.alan.db.table;

import com.alan.db.converters.DBType;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alan
 * 时 间：2019-11-26
 * 简 述：<功能简述>
 */
public class Table {

    /**
     * id字段
     */
    private Column id;
    /**
     * 所有的字段，包括id
     */
    private Map<String, Column> columns;
    /**
     * 表的名字
     */
    private String tableName;
    private Class<?> tableClass;
    /**
     * id是否自增长
     **/
    private boolean isAutoIncrement;

    Table(Class<?> tableClass, String tableName, Map<String, Column> columns, Column id, boolean isAutoIncrement) {
        this.tableClass = tableClass;
        this.tableName = tableName;
        this.columns = Collections.unmodifiableMap(columns);
        this.isAutoIncrement = isAutoIncrement;
        this.id = id;
    }

    /**
     * 获取表对应的class
     *
     * @return
     */
    public Class<?> getTableClass() {
        return tableClass;
    }

    /**
     * 获取表的名字
     *
     * @return
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * 获取id字段
     *
     * @return
     */
    public Column getId() {
        return id;
    }

    /**
     * 返回所有的字段，包括id字段，key为字段的名字<br/>
     * 特别注意：返回的是不可修改的map，一旦remove，put操作就会触发异常
     *
     * @return 表中所有字段
     */
    public Map<String, Column> getColumns() {
        return columns;
    }

    public Map<String, Column> getWhitoutIdColumns() {
        HashMap<String, Column> hashMap = new HashMap<String, Column>(columns);
        hashMap.remove(id.getName());
        return hashMap;
    }

    /**
     * id是否是自增长
     *
     * @return
     */
    public boolean isAutoIncrement() {
        return isAutoIncrement;
    }

    public String getCreateSqlStr() {
        Column id = getId();
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS ");
        sql.append(tableName);
        sql.append(" ( ");
        if (isAutoIncrement()) {
            sql.append("\"").append(id.getName()).append("\"  ").append("INTEGER PRIMARY KEY AUTOINCREMENT,");
        }

        Collection<Column> columns = getColumns().values();
        StringBuilder primaryKey = new StringBuilder();
        for (Column column : columns) {
            if (column == id) {
                continue;
            }
            DBType dbType = column.getColumnConverter().getDBType();
            sql.append("\"").append(column.getName()).append("\"  ");
            sql.append(dbType.name());
            if (column.getDefaultValue() != null) {
                sql.append(" default ");
                if (DBType.TEXT.equals(dbType)) {
                    sql.append('\'').append(column.getDefaultValue()).append('\'');
                } else {
                    sql.append(column.getDefaultValue());
                }
                sql.append(" ");
            }
            if (ColumnUtils.isUnique(column.getColumnField())) {
                sql.append(" UNIQUE");
            }
            if (ColumnUtils.isNotNull(column.getColumnField())) {
                sql.append(" NOT NULL");
            }
            String check = ColumnUtils.getCheck(column.getColumnField());
            if (check != null) {
                sql.append(" CHECK(").append(check).append(")");
            }
            sql.append(",");
            if (column.isPrimaryKey() && !column.isAutoIncrement()) {
                primaryKey.append(column.getName()).append(",");
            }

        }
        if (primaryKey.length() > 0) {
            primaryKey.deleteCharAt(primaryKey.length() - 1);
            sql.append("PRIMARY KEY (").append(primaryKey.toString()).append(")");
        } else {
            sql.deleteCharAt(sql.length() - 1);
        }

        sql.append(" )");
        return sql.toString();
    }

    public String[] getColumnsStr() {
        Collection<Column> values = getColumns().values();
        String[] array = new String[values.size()];
        int i = 0;
        for (Column column : values) {
            array[i++] = column.getName();
        }
        return array;
    }

}
