package com.mzeat.db;

import java.util.ArrayList;
import java.util.List;

import com.mzeat.model.Advs;
import com.mzeat.util.CheckTable;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class PosterDb {
	private DatabaseHelper databaseHelper;
	private SQLiteDatabase db;
	private Context context;

	public PosterDb(Context context) {
		this.context = context;
		databaseHelper = DatabaseHelper.getInstance(context);
		db = databaseHelper.getWritableDatabase();

	}

	public void add(List<Advs> columns) {
		db.beginTransaction(); // 开始事务
		/** 数据库SQL语句 添加一个表 **/
		String poster = "create table Poster("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT," + "id TEXT,"
				+ "name TEXT," + "img TEXT" + ");";
		db.execSQL("DROP TABLE IF EXISTS Poster");
		db.execSQL(poster);
		try {
			for (Advs column : columns) {
				db.execSQL(
						"INSERT INTO Poster VALUES(null, ?, ?, ?)",
						new Object[] { column.getId(), column.getName(),
								column.getImg(),

						});
			}
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}

	public void deleteAll() {
		db.delete("Poster", null, null);

	}

	public void delete(String id) {
		String whereClause = "id = ?";
		String[] whereArgs = { id };

		db.delete("Poster", whereClause, whereArgs);

	}

	public ArrayList<String> getImgs() {

		ArrayList<String> imgs = new ArrayList<String>();

		String table = "Poster";
		String[] columns = { "img" };
		String selection = null;
		String[] selectionArgs = null;
		String groupBy = null;
		String having = null;
		String orderBy = "id";

		if (CheckTable.tabbleIsExist(context, table)) {
			Cursor c = db.query(table, columns, selection, selectionArgs,
					groupBy, having, orderBy);

			while (c.moveToNext()) {

				imgs.add(c.getString(c.getColumnIndex("img")));
			}
			c.close();
		}

		return imgs;
	}

	public ArrayList<Advs> getAdvs() {

		ArrayList<Advs> advs = new ArrayList<Advs>();

		String table = "Poster";
		String[] columns = { "*" };
		String selection = null;
		String[] selectionArgs = null;
		String groupBy = null;
		String having = null;
		String orderBy = "id";
		if (CheckTable.tabbleIsExist(context, table)) {
			Cursor c = db.query(table, columns, selection, selectionArgs,
					groupBy, having, orderBy);

			while (c.moveToNext()) {
				Advs imgs = new Advs();

				imgs.setId(c.getString(c.getColumnIndex("id")));
				imgs.setName(c.getString(c.getColumnIndex("name")));
				imgs.setImg(c.getString(c.getColumnIndex("img")));

				advs.add(imgs);
			}
			c.close();
		}
		return advs;
	}

	/**
	 * close database
	 */
	public void closeDB() {
		db.close();
	}
}
