package com.alan.db.converters;

import android.database.sqlite.SQLiteProgram;

import net.sqlcipher.Cursor;

/**
 * sql的类型
 * 
 * @author 试着飞 </br> Date: 13-11-18
 */
public enum DBType {
	/**
	 * integer
	 */
	INTEGER {
		@Override
		public void bindObjectToProgram(SQLiteProgram prog, int index,
				Object value) {
			prog.bindLong(index, (Integer) value);
		}

		@Override
		public Object getValueFromCursor(Cursor cursor, int index) {
			return cursor.getInt(index);
		}

		@Override
		public Object getValueFromCursor(android.database.Cursor cursor, int index) {
			return cursor.getInt(index);
		}
	},
	/**
	 * 字符串String类型
	 */
	TEXT {
		@Override
		public void bindObjectToProgram(SQLiteProgram prog, int index,
				Object value) {
			prog.bindString(index, String.valueOf(value));
		}

		@Override
		public Object getValueFromCursor(Cursor cursor, int index) {
			return cursor.getString(index);
		}

		@Override
		public Object getValueFromCursor(android.database.Cursor cursor, int index) {
			return cursor.getString(index);
		}
	},
	/**
	 * long型
	 */
	BIGINT {
		@Override
		public void bindObjectToProgram(SQLiteProgram prog, int index,
				Object value) {
			prog.bindLong(index, ((Number) value).longValue());
		}

		@Override
		public Object getValueFromCursor(Cursor cursor, int index) {
			return cursor.getLong(index);
		}

		@Override
		public Object getValueFromCursor(android.database.Cursor cursor, int index) {
			return cursor.getLong(index);
		}
	},
	/**
	 * double等类型的存储
	 */
	REAL {
		@Override
		public void bindObjectToProgram(SQLiteProgram prog, int index,
				Object value) {
			prog.bindDouble(index, ((Number) value).doubleValue());
		}

		@Override
		public Object getValueFromCursor(Cursor cursor, int index) {
			return cursor.getDouble(index);
		}

		@Override
		public Object getValueFromCursor(android.database.Cursor cursor, int index) {
			return cursor.getDouble(index);
		}
	},
	/**
	 * byte数组的数据的存储
	 */
	BLOB {
		@Override
		public void bindObjectToProgram(SQLiteProgram prog, int index,
				Object value) {
			prog.bindBlob(index, (byte[]) value);
		}

		@Override
		public Object getValueFromCursor(Cursor cursor, int index) {
			return cursor.getBlob(index);
		}

		@Override
		public Object getValueFromCursor(android.database.Cursor cursor, int index) {
			return cursor.getBlob(index);
		}
	},

	NULL {
		@Override
		public void bindObjectToProgram(SQLiteProgram prog, int index,
				Object value) {
			prog.bindNull(index);
		}

		@Override
		public Object getValueFromCursor(Cursor cursor, int index) {
			return null;
		}

		@Override
		public Object getValueFromCursor(android.database.Cursor cursor, int index) {
			return null;
		}
	};
	/**
	 * 实现绑定数据到SQLiteProgram中
	 * 
	 * @param prog
	 * @param index
	 * @param value
	 */
	public abstract void bindObjectToProgram(SQLiteProgram prog, int index,
			Object value);

	/**
	 * 从cursor 获取游标里面的数据
	 * 
	 * @param cursor
	 * @param index
	 * @return 游标里面的数据
	 */
	public abstract Object getValueFromCursor(Cursor cursor, int index);
	public abstract Object getValueFromCursor(android.database.Cursor cursor, int index);

}
