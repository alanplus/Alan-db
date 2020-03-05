package com.alan.db.converters;

import java.sql.Date;

/**
 * @author 试着飞 Date: 13-11-18
 */
abstract class BaseConverter implements IColumnConverter {

	// 不进行转化
	@Override
	public Object toSqlValue(Object value) {
		return value;
	}

	// 不进行转化
	@Override
	public Object toJavaValue(Object value) {
		return value;
	}
}

class BooleanConverter implements IColumnConverter {
	// boolean ->> int
	@Override
	public Integer toSqlValue(Object value) {
		return (Boolean) value ? 1 : 0;
	}

	// int ->> boolean
	@Override
	public Boolean toJavaValue(Object value) {
		return (Integer) value == 1 ? true : false;
	}

	@Override
	public DBType getDBType() {
		return DBType.INTEGER;
	}

	@Override
	public String getDefaultValue() {
		return "false";
	}
}

class CharConverter implements IColumnConverter {
	// char ->> int
	@Override
	public Integer toSqlValue(Object value) {
		return (int) ((Character) value).charValue();
	}

	// char ->> int
	@Override
	public Character toJavaValue(Object value) {
		return (char) ((Integer) value).intValue();

	}

	@Override
	public DBType getDBType() {
		return DBType.INTEGER;
	}

	@Override
	public String getDefaultValue() {
		return "";
	}

}

class DateConverter implements IColumnConverter {

	// date ->> long
	@Override
	public Long toSqlValue(Object value) {
		Date date = (Date) value;
		return date.getTime();
	}

	// long ->> date
	@Override
	public Date toJavaValue(Object value) {
		Long time = (Long) value;
		return new Date(time);
	}

	@Override
	public DBType getDBType() {
		return DBType.BIGINT;
	}

	@Override
	public String getDefaultValue() {
		return "0";
	}

}

class IntegerConverter extends BaseConverter {
	// int ->> int
	@Override
	public DBType getDBType() {
		return DBType.INTEGER;
	}

	@Override
	public String getDefaultValue() {
		return "0";
	}

}

class DoubleConverter extends BaseConverter {
	// double ->> double
	@Override
	public DBType getDBType() {
		return DBType.REAL;
	}

	@Override
	public String getDefaultValue() {
		return "0";
	}
}

class FlaotConverter extends BaseConverter {

	// float ->> double
	@Override
	public Object toSqlValue(Object value) {
		return ((Number) value).doubleValue();
	}

	// double ->> float
	@Override
	public Float toJavaValue(Object value) {
		return ((Number) value).floatValue();
	}

	@Override
	public DBType getDBType() {
		return DBType.REAL;
	}

	@Override
	public String getDefaultValue() {
		return "0";
	}
}

class StringConverter extends BaseConverter {
	// String ->> String
	@Override
	public DBType getDBType() {
		return DBType.TEXT;
	}

	@Override
	public String getDefaultValue() {
		return "";
	}
}

class LongConverter extends BaseConverter {
	// long ->> long
	@Override
	public DBType getDBType() {
		return DBType.BIGINT;
	}

	@Override
	public String getDefaultValue() {
		return "0";
	}

}

class ShortConverter extends BaseConverter {

	// short ->> int
	@Override
	public Integer toSqlValue(Object value) {
		return ((Number) value).intValue();
	}

	// int ->> short
	@Override
	public Short toJavaValue(Object value) {
		return ((Number) value).shortValue();
	}

	@Override
	public DBType getDBType() {
		return DBType.INTEGER;
	}

	@Override
	public String getDefaultValue() {
		return "";
	}

}

class ByteConverter extends BaseConverter {
	// byte ->> int
	@Override
	public Integer toSqlValue(Object value) {
		return ((Number) value).intValue();
	}

	// int ->> byte
	@Override
	public Byte toJavaValue(Object value) {
		return ((Number) value).byteValue();
	}

	// byte
	@Override
	public DBType getDBType() {
		return DBType.INTEGER;
	}

	@Override
	public String getDefaultValue() {
		return "0";
	}

}

class ByteArrayConverter extends BaseConverter {
	@Override
	public DBType getDBType() {
		return DBType.BLOB;
	}

	@Override
	public String getDefaultValue() {
		return "";
	}

}
