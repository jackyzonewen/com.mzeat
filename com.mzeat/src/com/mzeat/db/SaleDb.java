package com.mzeat.db;

import java.util.ArrayList;
import java.util.List;

import com.mzeat.model.Invite;
import com.mzeat.model.Sale;
import com.mzeat.model.Shopping;
import com.mzeat.util.CheckTable;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class SaleDb {
	private DatabaseHelper databaseHelper;
	private SQLiteDatabase db;
	private Context context;
	public SaleDb(Context context) {
		this.context = context;
		databaseHelper = DatabaseHelper.getInstance(context);
		db = databaseHelper.getWritableDatabase();

	}

	public void add(List<Sale> columns) {
		db.beginTransaction(); // 开始事务
		/** 数据库SQL语句 添加一个表 **/
		String Sale = "create table Sale ("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ "notice_id TEXT,"
				+ "title TEXT," 
				+ "create_time TEXT," 
				+ "content TEXT" 
				
 + ");";
		db.execSQL("DROP TABLE IF EXISTS Sale");
		db.execSQL(Sale);
		try {
			for (Sale column : columns) {
				db.execSQL(
						"INSERT INTO Sale VALUES(null, ?, ?, ?,?)",
						new Object[] { 
								column.getNotice_id(), 
								column.getTitle(),
								column.getCreate_time(), 
								column.getContent(),
					
							
						});
			}
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}

	public void deleteAll() {
		db.delete("Sale", null, null);

	}

	public void delete(String id) {
		String whereClause = "notice_id = ?";
		String[] whereArgs = { id };

		db.delete("Sale", whereClause, whereArgs);

	}

	public ArrayList<Sale> getSale() {

		ArrayList<Sale> mSale = new ArrayList<Sale>();

		String table = "Sale";
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
			Sale sale = new Sale();
		
			sale.setNotice_id(c.getString(c.getColumnIndex("notice_id")));
			sale.setTitle(c.getString(c.getColumnIndex("title")));
			
			sale.setCreate_time(c.getString(c.getColumnIndex("create_time")));
			sale.setContent(c.getString(c.getColumnIndex("content")));
			
			
			mSale.add(sale);
		}
		c.close();}
		return mSale;
	}

	/**
	 * close database
	 */
	public void closeDB() {
		db.close();
	}
}
