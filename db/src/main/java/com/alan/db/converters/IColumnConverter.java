package com.alan.db.converters;

/**
 * sql类型与java类型 互相转化的转化器
 *
 * @author 试着飞 </br> Date: 13-11-18
 */
public interface IColumnConverter {

	Object toSqlValue(Object value);

	Object toJavaValue(Object value);

	DBType getDBType();

	String getDefaultValue();
}
