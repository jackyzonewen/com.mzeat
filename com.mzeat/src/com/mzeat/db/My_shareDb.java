package com.mzeat.db;

import java.util.ArrayList;
import java.util.List;

import com.mzeat.model.My_share;
import com.mzeat.model.Shopping;
import com.mzeat.util.CheckTable;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class My_shareDb {
	private DatabaseHelper databaseHelper;
	private SQLiteDatabase db;
	private Context context;
	public My_shareDb(Context context) {
		this.context = context;
		databaseHelper = DatabaseHelper.getInstance(context);
		db = databaseHelper.getWritableDatabase();

	}

	public void add(List<My_share> columns) {
		db.beginTransaction(); // 开始事务
		/** 数据库SQL语句 添加一个表 **/
		String my_share = "create table my_share ("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ "share_id TEXT,"
				+ "content TEXT," 
				+ "is_read TEXT" 
				+ ");";
		db.execSQL("DROP TABLE IF EXISTS my_share");
		db.execSQL(my_share);
		try {
			for (My_share column : columns) {
				db.execSQL(
						"INSERT INTO my_share VALUES(null, ?, ?, ?)",
						new Object[] { 
								column.getShare_id(), 
								column.getContent(),
								column.getIs_read(), 

						});
			}
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}

	public void deleteAll() {
		db.delete("my_share", null, null);

	}

	public void delete(String id) {
		String whereClause = "share_id = ?";
		String[] whereArgs = { id };
		if (CheckTable.tabbleIsExist(context, "my_share")) {
		db.delete("my_share", whereClause, whereArgs);
		}
	}

	public ArrayList<My_share> getMy_share() {

		ArrayList<My_share> mMy_share = new ArrayList<My_share>();

		String table = "my_share";
		String[] columns = { "*" };
		String selection = null;
		String[] selectionArgs = null;
		String groupBy = null;
		String having = null;
		String orderBy = "share_id "+ " DESC";
		if (CheckTable.tabbleIsExist(context, table)) {
		Cursor c = db.query(table, columns, selection, selectionArgs, groupBy,
				having, orderBy);

		while (c.moveToNext()) {
			My_share my_share = new My_share();
			my_share.setShare_id(c.getString(c.getColumnIndex("share_id")));
			my_share.setContent(c.getString(c.getColumnIndex("content")));
			my_share.setIs_read(c.getString(c.getColumnIndex("is_read")));
			mMy_share.add(my_share);
		}
		c.close();}
		return mMy_share;
	}

	/**
	 * close database
	 */
	public void closeDB() {
		db.close();
	}
}
