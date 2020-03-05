package com.alan.db.table;

import com.alan.db.converters.ColumnConverterFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Alan
 * 时 间：2019-11-26
 * 简 述：<功能简述>
 */
public class TableFactory {

    private static HashMap<Class<?>, Table> tables = new HashMap<>();


    /**
     * 获取table
     *
     * @param tableClass
     * @return
     */
    public synchronized static Table getTable(Class<?> tableClass) {
        Table table = tables.get(tableClass);
        if (table == null) {
            table = createTable(tableClass);
            tables.put(tableClass, table);
        }
        return table;
    }

    /**
     * 创建table
     *
     * @param tableClass
     * @return
     */
    private static Table createTable(Class<?> tableClass) {
        String tableName = getTableName(tableClass);
        Column id = null;
        boolean autoIncrement = false;
        Class<?> entityType = tableClass;
        Map<String, Column> columns = new HashMap<String, Column>();
        while (!Object.class.equals(entityType)) {
            Field[] fields = entityType.getDeclaredFields();
            for (Field field : fields) {
                // 如果是 不需要的字段
                if (ColumnUtils.isTransient(field))
                    continue;
                // 如果字段的类型是不支持的数据数据类型
                if (!ColumnConverterFactory.isSuport(field.getType()))
                    continue;
                String columnName = ColumnUtils.getColumnName(field);
                // 如果已经有相同的字段，也就是子类和父类重复的情况，优先选择子类的字段
                if (columns.containsKey(columnName))
                    continue;
                // 创建字段
                Column column = new Column(field, columnName);
                column.setAutoIncrement(false);
                // 如果表id还没有找到，那么就查看该字段是否是id，是id就设置id
                if (id == null && ColumnUtils.isId(field)) {
                    autoIncrement = true;
                    id = column;
                    column.setAutoIncrement(true);
                }
                column.setPrimaryKey(ColumnUtils.isPrimaryKey(field));
                field.setAccessible(true);
                // 将字段都放入表中
                columns.put(columnName, column);
            }
            // 继续向父类递归查找
            entityType = entityType.getSuperclass();
        }

        return new Table(tableClass, tableName, columns, id, autoIncrement);

    }

    /**
     * 得到表的名字
     *
     * @param tableClass
     * @return
     */
    public static String getTableName(Class<?> tableClass) {
        com.alan.db.annotations.Table table = tableClass
                .getAnnotation(com.alan.db.annotations.Table.class);
        if (table != null) {
            return table.name();
        }
        return tableClass.getCanonicalName().replace('.', '_');
    }


}
