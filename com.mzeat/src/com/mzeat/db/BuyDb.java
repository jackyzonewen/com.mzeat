package com.mzeat.db;

import java.util.ArrayList;
import java.util.List;

import com.mzeat.model.Shopping;
import com.mzeat.util.CheckTable;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class BuyDb {
	private DatabaseHelper databaseHelper;
	private SQLiteDatabase db;
	private Context context;
	public BuyDb(Context context) {
		this.context = context;
		databaseHelper = DatabaseHelper.getInstance(context);
		db = databaseHelper.getWritableDatabase();

	}

	public void add(List<Shopping> columns) {
		db.beginTransaction(); // 开始事务
		/** 数据库SQL语句 添加一个表 **/
		String Buy = "create table Buy ("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ "id TEXT,"
				+ "name TEXT," 
				+ "logo TEXT," 
				+ "xpoint TEXT," 
				+ "ypoint TEXT,"
				+ "api_address TEXT," 
				+ "mzeatvip TEXT," 
				+ "avg_point TEXT,"
				+ "tel TEXT," 
				+ "Characteristic TEXT," 
				+ "open_time TEXT,"
				+ "comment_count TEXT," 
				+ "brand_id TEXT," 
				+ "distance TEXT,"
				+ "mobile_brief TEXT" + ");";
		db.execSQL("DROP TABLE IF EXISTS Buy");
		db.execSQL(Buy);
		try {
			for (Shopping column : columns) {
				db.execSQL(
						"INSERT INTO Buy VALUES(null, ?, ?, ?,?,?,?,?,?,?,?,?,?,?,?,?)",
						new Object[] { 
								column.getId(), 
								column.getName(),
								column.getLogo(), 
								column.getXpoint(),
								column.getYpoint(), 
								column.getApi_address(),
								column.getMzeatvip(), 
								column.getAvg_point(),
								column.getTel(), 
								column.getCharacteristic(),
								column.getOpen_time(),
								column.getComment_count(),
								column.getBrand_id(), 
								column.getDistance(),
								column.getMobile_brief()
						});
			}
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}

	public void deleteAll() {
		db.delete("Buy", null, null);

	}

	public void delete(String id) {
		String whereClause = "id = ?";
		String[] whereArgs = { id };

		db.delete("Buy", whereClause, whereArgs);

	}

	public ArrayList<Shopping> getShoppings() {

		ArrayList<Shopping> mShoppings = new ArrayList<Shopping>();

		String table = "Buy";
		String[] columns = { "*" };
		String selection = null;
		String[] selectionArgs = null;
		String groupBy = null;
		String having = null;
		String orderBy = "id";
		if (CheckTable.tabbleIsExist(context, table)) {
		Cursor c = db.query(table, columns, selection, selectionArgs, groupBy,
				having, orderBy);

		while (c.moveToNext()) {
			Shopping shopping = new Shopping();
			shopping.setId(c.getString(c.getColumnIndex("id")));
			shopping.setName(c.getString(c.getColumnIndex("name")));
			shopping.setLogo(c.getString(c.getColumnIndex("logo")));
			shopping.setXpoint(c.getString(c.getColumnIndex("xpoint")));
			shopping.setYpoint(c.getString(c.getColumnIndex("ypoint")));
			shopping.setApi_address(c.getString(c.getColumnIndex("api_address")));
			shopping.setMzeatvip(c.getString(c.getColumnIndex("mzeatvip")));
			shopping.setAvg_point(c.getString(c.getColumnIndex("avg_point")));
			shopping.setTel(c.getString(c.getColumnIndex("tel")));
			shopping.setCharacteristic(c.getString(c.getColumnIndex("Characteristic")));
			shopping.setOpen_time(c.getString(c.getColumnIndex("open_time")));
			shopping.setComment_count(c.getString(c
					.getColumnIndex("comment_count")));
			shopping.setBrand_id(c.getString(c.getColumnIndex("brand_id")));
			shopping.setDistance(c.getString(c.getColumnIndex("distance")));
			shopping.setMobile_brief(c.getString(c.getColumnIndex("mobile_brief")));
			mShoppings.add(shopping);
		}
		c.close();}
		return mShoppings;
	}

	/**
	 * close database
	 */
	public void closeDB() {
		db.close();
	}
}
