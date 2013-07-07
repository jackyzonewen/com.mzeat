package com.mzeat.db;

import java.util.ArrayList;
import java.util.List;

import com.mzeat.model.My_share;
import com.mzeat.model.Shopping;
import com.mzeat.model.U_commentlist_item;
import com.mzeat.util.CheckTable;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class U_commentlist_itemDb {
	private DatabaseHelper databaseHelper;
	private SQLiteDatabase db;
	private Context context;
	public U_commentlist_itemDb(Context context) {
		this.context = context;
		databaseHelper = DatabaseHelper.getInstance(context);
		db = databaseHelper.getWritableDatabase();

	}

	public void add(List<U_commentlist_item> columns) {
		db.beginTransaction(); // 开始事务
		/** 数据库SQL语句 添加一个表 **/
		String U_commentlist_item = "create table U_commentlist_item ("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ "share_id TEXT,"
				+ "comment_id TEXT,"
				+ "uid TEXT," 
				+ "parent_id TEXT," 
				+ "content TEXT," 
				+ "create_time TEXT," 
				+ "scontent TEXT," 
				+ "user_name TEXT," 
				+ "time TEXT" 
				+ ");";
		db.execSQL("DROP TABLE IF EXISTS U_commentlist_item");
		db.execSQL(U_commentlist_item);
		try {
			for (U_commentlist_item column : columns) {
				db.execSQL(
						"INSERT INTO U_commentlist_item VALUES(null, ?, ?, ?,?,?,?,?,?,?)",
						new Object[] { 
								column.getShare_id(), 
								column.getComment_id(),
								column.getUid(), 
								column.getParent_id(), 
								column.getContent(), 
								column.getCreate_time(), 
								column.getScontent(), 
								column.getUser_name(), 
								column.getTime()

						});
			}
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}

	public void deleteAll() {
		
		db.delete("U_commentlist_item", null, null);

	}

	public void delete(String id) {
		String whereClause = "comment_id = ?";
		String[] whereArgs = { id };
		if (CheckTable.tabbleIsExist(context, "U_commentlist_item")) {
		db.delete("U_commentlist_item", whereClause, whereArgs);
		}
	}

	public ArrayList<U_commentlist_item> getItems() {

		ArrayList<U_commentlist_item> mU_commentlist_item = new ArrayList<U_commentlist_item>();

		String table = "U_commentlist_item";
		String[] columns = { "*" };
		String selection = null;
		String[] selectionArgs = null;
		String groupBy = null;
		String having = null;
		String orderBy = "comment_id" + " DESC";
		if (CheckTable.tabbleIsExist(context, table)) {
		Cursor c = db.query(table, columns, selection, selectionArgs, groupBy,
				having, orderBy);

		while (c.moveToNext()) {
			U_commentlist_item u_commentlist_item = new U_commentlist_item();
			
			u_commentlist_item.setShare_id(c.getString(c.getColumnIndex("share_id")));
			u_commentlist_item.setContent(c.getString(c.getColumnIndex("content")));
			u_commentlist_item.setComment_id(c.getString(c.getColumnIndex("comment_id")));
			u_commentlist_item.setUid(c.getString(c.getColumnIndex("uid")));
			u_commentlist_item.setParent_id(c.getString(c.getColumnIndex("parent_id")));
			u_commentlist_item.setCreate_time(c.getString(c.getColumnIndex("create_time")));
			u_commentlist_item.setScontent(c.getString(c.getColumnIndex("scontent")));
			u_commentlist_item.setUser_name(c.getString(c.getColumnIndex("user_name")));
			u_commentlist_item.setTime(c.getString(c.getColumnIndex("time")));
			
			mU_commentlist_item.add(u_commentlist_item);
		}
		c.close();}
		return mU_commentlist_item;
	}

	/**
	 * close database
	 */
	public void closeDB() {
		db.close();
	}
}
