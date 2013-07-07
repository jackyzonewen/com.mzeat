package com.mzeat.db;

import java.util.ArrayList;
import java.util.List;

import com.mzeat.model.Change;
import com.mzeat.model.Invite;
import com.mzeat.model.Sale;
import com.mzeat.model.Shopping;
import com.mzeat.util.CheckTable;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ChangeDb {
	private DatabaseHelper databaseHelper;
	private SQLiteDatabase db;
	private Context context;
	public ChangeDb(Context context) {
		this.context = context;
		databaseHelper = DatabaseHelper.getInstance(context);
		db = databaseHelper.getWritableDatabase();

	}

	public void add(List<Change> columns) {
		db.beginTransaction(); // 开始事务
		/** 数据库SQL语句 添加一个表 **/
		String Change = "create table Change ("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ "goods_id TEXT,"
				+ "title TEXT," 
				+ "score TEXT," 
				+ "bought TEXT," 
				+ "image TEXT," 
				+ "count_image TEXT," 
				+ "content TEXT" 
				
 + ");";
		db.execSQL("DROP TABLE IF EXISTS Change");
		db.execSQL(Change);
		try {
			for (Change column : columns) {
				db.execSQL(
						"INSERT INTO Change VALUES(null, ?, ?, ?,?,?,?,?)",
						new Object[] { 
								column.getGoods_id(), 
								column.getTitle(),
								column.getScore(), 
								column.getBought(),
								column.getImage(),
								column.getCount_image(),
								column.getContent(),
					
							
						});
			}
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}

	public void deleteAll() {
		db.delete("Change", null, null);

	}

	public void delete(String id) {
		String whereClause = "goods_id = ?";
		String[] whereArgs = { id };

		db.delete("Change", whereClause, whereArgs);

	}

	public ArrayList<Change> getChange() {

		ArrayList<Change> mChange = new ArrayList<Change>();

		String table = "Change";
		String[] columns = { "*" };
		String selection = null;
		String[] selectionArgs = null;
		String groupBy = null;
		String having = null;
		String orderBy = null;
		if (CheckTable.tabbleIsExist(context, table)) {
		Cursor c = db.query(table, columns, selection, selectionArgs, groupBy,
				having, orderBy);

		while (c.moveToNext()) {
			Change change = new Change();
		
			change.setGoods_id(c.getString(c.getColumnIndex("goods_id")));
			change.setTitle(c.getString(c.getColumnIndex("title")));
			
			change.setScore(c.getString(c.getColumnIndex("score")));

			change.setBought(c.getString(c.getColumnIndex("bought")));
			change.setImage(c.getString(c.getColumnIndex("image")));
			change.setCount_image(c.getString(c.getColumnIndex("count_image")));
			
			change.setContent(c.getString(c.getColumnIndex("content")));
			
			
			mChange.add(change);
		}
		c.close();}
		return mChange;
	}

	/**
	 * close database
	 */
	public void closeDB() {
		db.close();
	}
}
