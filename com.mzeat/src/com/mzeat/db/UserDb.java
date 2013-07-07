package com.mzeat.db;

import java.util.ArrayList;
import java.util.List;

import com.mzeat.model.Shopping;
import com.mzeat.model.User;
import com.mzeat.util.CheckTable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class UserDb {
	private DatabaseHelper databaseHelper;
	private SQLiteDatabase db;
	private Context context;

	public UserDb(Context context) {
		this.context = context;
		databaseHelper = DatabaseHelper.getInstance(context);
		db = databaseHelper.getWritableDatabase();

	}

	public void add(User columns) {
		db.beginTransaction(); // 开始事务
		/** 数据库SQL语句 添加一个表 **/
		String User = "create table User ("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT," + 
				"uid TEXT,"
				+ "user_name TEXT," 
				+ "user_email TEXT," 
				+ "user_money TEXT,"
				+ "user_money_format TEXT," 
				+ "group_id TEXT,"
				+ "mzeatno TEXT," 
				+ "score TEXT," 
				+ "t_sign_info TEXT,"
				+ "user_avatar TEXT," 
				+ "mobile TEXT," 
				+ "b_day TEXT,"
				+ "sex TEXT" 
				+ ");";
		db.execSQL("DROP TABLE IF EXISTS User");
		db.execSQL(User);
		try {

			db.execSQL(
					"INSERT INTO User VALUES(null, ?, ?, ?,?,?,?,?,?,?,?,?,?,?)",
					new Object[] { columns.getUid(), columns.getUser_name(),
							columns.getUser_email(), columns.getUser_money(),
							columns.getUser_money_format(),
							columns.getGroup_id(), columns.getMzeatno(),
							columns.getScore(), columns.getT_sign_info(),
							columns.getUser_avatar(), columns.getMobile(),
							columns.getB_day(), columns.getSex() });

			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}

	public void deleteAll() {
		db.delete("User", null, null);

	}

	public void delete(String uid) {
		String whereClause = "uid = ?";
		String[] whereArgs = { uid };

		db.delete("User", whereClause, whereArgs);

	}

	public User getUser() {
		User user = new User();
		// HomeUser home_user = new HomeUser();
		String table = "User";
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
				user.setUid(c.getString(c.getColumnIndex("uid")));
				user.setUser_name(c.getString(c.getColumnIndex("user_name")));
				user.setUser_email(c.getString(c.getColumnIndex("user_email")));
				user.setUser_money(c.getString(c.getColumnIndex("user_money")));
				user.setUser_money_format(c.getString(c
						.getColumnIndex("user_money_format")));
				user.setScore(c.getString(c.getColumnIndex("score")));
				user.setGroup_id(c.getString(c.getColumnIndex("group_id")));
				user.setMzeatno(c.getString(c.getColumnIndex("mzeatno")));
				user.setT_sign_info(c.getString(c.getColumnIndex("t_sign_info")));
				user.setMobile(c.getString(c.getColumnIndex("mobile")));
				user.setB_day(c.getString(c.getColumnIndex("b_day")));
				user.setUser_avatar(c.getString(c.getColumnIndex("user_avatar")));
				user.setSex(c.getString(c.getColumnIndex("sex")));
				// home_user.setFans(c.getString(c.getColumnIndex("fans")));
				// home_user.setPhotos(c.getString(c.getColumnIndex("photos")));
				// home_user.setGoods(c.getString(c.getColumnIndex("goods")));
				// home_user.setFollows(c.getString(c.getColumnIndex("follows")));
				// home_user.setFavs(c.getString(c.getColumnIndex("favs")));
				// home_user.setUser_avatar(c.getString(c.getColumnIndex("user_avatar")));
				// user.setHome_user(home_user);
			}
			c.close();
		}
		return user;
	}

	/**
	 * close database
	 */
	public void closeDB() {
		db.close();
	}

	public void updataUser(User user) {
		ContentValues values = new ContentValues();
		
		values.put("mobile", user.getMobile());
		values.put("b_day", user.getB_day());
		values.put("sex", user.getSex());
		
		db.update("User", values, "user_name = ?", new String[] { user.getUser_name()});
		db.close();
	}
}
