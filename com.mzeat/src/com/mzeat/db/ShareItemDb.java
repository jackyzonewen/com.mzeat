package com.mzeat.db;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.mzeat.model.ShareItem;
import com.mzeat.util.CheckTable;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ShareItemDb {
	private DatabaseHelper databaseHelper;
	private SQLiteDatabase db;
	private Context context;

	public ShareItemDb(Context context) {
		this.context = context;
		databaseHelper = DatabaseHelper.getInstance(context);
		db = databaseHelper.getWritableDatabase();

	}

	public void add(List<ShareItem> column) {
		db.beginTransaction(); // 开始事务
		/** 数据库SQL语句 添加一个表 **/
		String ShareItem = "create table ShareItem ("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ "share_id TEXT,"
				+ "title TEXT," 
				+ "content TEXT,"
				+ "reply_count TEXT,"
				+ "user_avatar TEXT," 
				+ "user_name TEXT,"
				+ "create_time TEXT,"
				+ "img TEXT," 
				+ "small_img TEXT" 
				+ ");";
		db.execSQL("DROP TABLE IF EXISTS ShareItem");
		db.execSQL(ShareItem);
		try {
			for (ShareItem columns : column) {
			db.execSQL(
					"INSERT INTO ShareItem VALUES(null,?,?,?,?, ?, ?,?,?,?)",
					new Object[] { 
							columns.getShare_id(), 
							columns.getTitle(),
							columns.getContent(),
							columns.getReply_count(),
							columns.getUser_avatar(),
							columns.getUser_name(),
							columns.getCreate_time(),
							columns.getImg(), 
							columns.getSmall_img(), 
							
							});
			}
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}

	public void deleteAll() {
		db.delete("ShareItem", null, null);

	}

	public void delete(String share_id) {
		String whereClause = "share_id = ?";
		String[] whereArgs = { share_id };

		db.delete("ShareItem", whereClause, whereArgs);

	}

	public LinkedList<ShareItem> getItem() {
		LinkedList<ShareItem>  items = new LinkedList<ShareItem>();
		// HomeUser home_user = new HomeUser();
		String table = "ShareItem";
		String[] columns = { "*" };
		String selection = null;
		String[] selectionArgs = null;
		String groupBy = null;
		String having = null;
		String orderBy = null;
		if (CheckTable.tabbleIsExist(context, table)) {
			Cursor c = db.query(table, columns, selection, selectionArgs,
					groupBy, having, orderBy);

			while (c.moveToNext()) {
				ShareItem item = new ShareItem();
				item.setShare_id(c.getString(c.getColumnIndex("share_id")));
				item.setTitle(c.getString(c.getColumnIndex("title")));
				item.setContent(c.getString(c.getColumnIndex("content")));
				item.setReply_count(c.getString(c.getColumnIndex("reply_count")));
				item.setUser_avatar(c.getString(c.getColumnIndex("user_avatar")));
				item.setUser_name(c.getString(c.getColumnIndex("user_name")));
				item.setCreate_time(c.getString(c.getColumnIndex("create_time")));
				item.setImg((c.getString(c.getColumnIndex("img"))));
				item.setSmall_img((c.getString(c.getColumnIndex("small_img"))));

				items.add(item);
			}
			c.close();
		}
		return items;
	}

	/**
	 * close database
	 */
	public void closeDB() {
		db.close();
	}

}
