package com.alan.db.table;

import android.text.TextUtils;

import com.alan.db.annotations.Check;
import com.alan.db.annotations.Column;
import com.alan.db.annotations.Id;
import com.alan.db.annotations.NotNull;
import com.alan.db.annotations.PrimaryKey;
import com.alan.db.annotations.Transient;
import com.alan.db.annotations.Unique;
import com.alan.db.converters.IColumnConverter;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * @author 试着飞 </br> Date: 13-11-18
 */
public class ColumnUtils {
    /**
     * 是否是不需要的字段
     *
     * @param field
     * @return 是否是不需要的字段
     */
    public static boolean isTransient(Field field) {
        int mark = field.getModifiers();
        return Modifier.isStatic(mark) || Modifier.isTransient(mark)
                || field.getAnnotation(Transient.class) != null;
    }

    /**
     * 得到字段名
     *
     * @param field
     * @return 在数据库表中字段的名字
     */
    public static String getColumnName(Field field) {
        Column column = field.getAnnotation(com.alan.db.annotations.Column.class);
        if (column != null) {
            return column.column();
        }
        return field.getName();
    }

    /**
     * 获取字段是否是主键
     *
     * @param field
     * @return
     */
    public static boolean isPrimaryKey(Field field) {
        PrimaryKey annotation = field.getAnnotation(PrimaryKey.class);
        return annotation != null;
    }

    /**
     * 得到该字段的默认值
     *
     * @param column
     * @return 默认值
     */
    public static String getDefaultValue(com.alan.db.table.Column column) {
        return getDefaultValue(column.getColumnField(), column.getColumnConverter());
    }

    /**
     * 得到该字段的默认值
     *
     * @param field
     * @return 默认值
     */
    public static String getDefaultValue(Field field, IColumnConverter iColumnConverter) {
        Column column = field.getAnnotation(Column.class);
        if (column != null && !TextUtils.isEmpty(column.defaultValue()))
            return column.defaultValue();
        return iColumnConverter.getDefaultValue();
    }

    /**
     * 该字段是否是唯一
     *
     * @param field
     * @return 是否是唯一
     */
    public static boolean isUnique(Field field) {
        return field.getAnnotation(Unique.class) != null;
    }

    /**
     * 该字段是否是不为空
     *
     * @param field
     * @return 是否是不为空
     */
    public static boolean isNotNull(Field field) {
        return field.getAnnotation(NotNull.class) != null;
    }

    public static String getCheck(Field field) {
        Check check = field.getAnnotation(Check.class);
        if (check != null)
            return check.value();
        return null;
    }


    public static boolean isId(Field field) {
        return field.getAnnotation(Id.class) != null;
    }

}
