package com.mzeat.util;

import com.mzeat.db.DatabaseHelper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CheckTable {

	/**
	 * 判断某张表是否存在
	 * 
	 * @param tabName
	 *            表名
	 * @return
	 */

	private static DatabaseHelper databaseHelper;
	private static SQLiteDatabase db;

	public static boolean tabbleIsExist(Context context, String tableName) {
		boolean result = false;
		if (tableName == null) {
			return false;
		}
		databaseHelper = DatabaseHelper.getInstance(context);
		db = databaseHelper.getWritableDatabase();
		Cursor cursor = null;
		try {

			String sql = "select count(*) as c from Sqlite_master  where type ='table' and name ='"
					+ tableName.trim() + "' ";
			cursor = db.rawQuery(sql, null);
			if (cursor.moveToNext()) {
				int count = cursor.getInt(0);
				if (count > 0) {
					result = true;
				}
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		return result;
	}
}
