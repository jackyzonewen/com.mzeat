package com.mzeat.db;

import java.util.ArrayList;

import com.mzeat.MzeatApplication;
import com.mzeat.model.PrivilegeItem;
import com.mzeat.model.User;
import com.mzeat.util.CheckTable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MycartDb {
	private DatabaseHelper databaseHelper;
	private SQLiteDatabase db;
	private Context context;

	public MycartDb(Context context) {
		this.context = context;
		databaseHelper = DatabaseHelper.getInstance(context);
		db = databaseHelper.getWritableDatabase();

	}

	/**
	 * 添加到购物车
	 * @param columns
	 */
	public void add(PrivilegeItem columns) {
		db.beginTransaction(); // 开始事务
		/** 数据库SQL语句 添加一个表 **/
		String tablename = "Mycart";
		String mycart = "create table "+ tablename + "("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ "email TEXT,"
				+ "goods_id TEXT," 
				+ "title TEXT," 
				+ "goods_desc TEXT,"
				+ "goods_brief TEXT," 
				+ "image TEXT," 
				+ "buy_count TEXT,"
				+ "ori_price_format TEXT," 
				+ "cur_price_format TEXT,"
				+ "discount TEXT," 
				+ "address TEXT," 
				+ "sp_detail TEXT,"
				+ "sp_tel TEXT," 
				+ "saving_format TEXT," 
				+ "less_time TEXT,"
				+ "xpoint TEXT," 
				+ "ypoint TEXT," 
				+ "distance TEXT,"
				+ "time_status TEXT," 
				+ "buy_status TEXT," 
				+ "buy_type TEXT,"
				+ "count_image TEXT," 
				+ "start_times TEXT,"
				+ "sp_open_times TEXT," 
				+ "cur_price TEXT," 
				+ "num TEXT," 
				+ "count TEXT" 
				+ ");";
		// db.execSQL("DROP TABLE IF EXISTS Mycart");
		if (!CheckTable.tabbleIsExist(context, tablename)) {
			db.execSQL(mycart);
		}

		try {

			db.execSQL(
					"INSERT INTO Mycart VALUES(null,?,?,?,?,?,?,?, ?, ?,?,?,?,?, ?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
					new Object[] {
							MzeatApplication.getInstance()
									.getpPreferencesConfig()
									.getString("email", ""),
							columns.getGoods_id(), columns.getTitle(),
							columns.getGoods_desc(), columns.getGoods_brief(),
							columns.getImage(), columns.getBuy_count(),
							columns.getOri_price_format(),
							columns.getCur_price_format(),
							columns.getDiscount(), columns.getAddress(),
							columns.getSp_detail(), columns.getSp_tel(),
							columns.getSaving_format(), columns.getLess_time(),
							columns.getXpoint(), columns.getYpoint(),
							columns.getDistance(), columns.getTime_status(),
							columns.getBuy_status(), columns.getBuy_type(),
							columns.getCount_image(), columns.getStart_times(),
							columns.getSp_open_times(), columns.getCur_price(),
							columns.getNum(),
							columns.getCount()
							});

			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}

	/**
	 * 删除该账号购物车里的所有商品
	 * @param email
	 */
	public void deleteAll(String email) {
		String whereClause = "email = ?";
		String[] whereArgs = { email };

		db.delete("Mycart", whereClause, whereArgs);

	}

	/**
	 * 删除该账号购物车里的单件商品
	 * @param goods_id
	 * @param email
	 */
	public void delete(String goods_id, String email) {
		String whereClause = "goods_id = ? AND email = ?";
		String[] whereArgs = { goods_id, email };

		db.delete("Mycart", whereClause, whereArgs);

	}
	/**
	 * 取得该账号购物车里面的所有商品
	 * @param goods_id
	 * @param email
	 */
	public ArrayList<PrivilegeItem> getItem(String email) {
		ArrayList<PrivilegeItem> items = new ArrayList<PrivilegeItem>();
		// HomeUser home_user = new HomeUser();
		String table = "Mycart";
		String[] columns = { "*" };
		String selection = "email = ?";
		String[] selectionArgs = { email };
		String groupBy = null;
		String having = null;
		String orderBy = null;
		if (CheckTable.tabbleIsExist(context, table)) {
			Cursor c = db.query(table, columns, selection, selectionArgs,
					groupBy, having, orderBy);

			while (c.moveToNext()) {
				PrivilegeItem item = new PrivilegeItem();
				item.setGoods_id(c.getString(c.getColumnIndex("goods_id")));
				item.setTitle(c.getString(c.getColumnIndex("title")));
				item.setImage(c.getString(c.getColumnIndex("image")));
				item.setBuy_count(c.getString(c.getColumnIndex("buy_count")));
				item.setOri_price_format(c.getString(c
						.getColumnIndex("ori_price_format")));
				item.setCur_price_format(c.getString(c
						.getColumnIndex("cur_price_format")));
				item.setDiscount(c.getString(c.getColumnIndex("discount")));
				item.setAddress(c.getString(c.getColumnIndex("address")));
				item.setSp_detail(c.getString(c.getColumnIndex("sp_detail")));
				item.setSp_tel(c.getString(c.getColumnIndex("sp_tel")));
				item.setSaving_format(c.getString(c
						.getColumnIndex("saving_format")));
				item.setLess_time(c.getString(c.getColumnIndex("less_time")));
				item.setXpoint(c.getString(c.getColumnIndex("xpoint")));
				item.setYpoint(c.getString(c.getColumnIndex("ypoint")));
				item.setDistance(c.getString(c.getColumnIndex("distance")));
				item.setGoods_brief(c.getString(c.getColumnIndex("goods_brief")));
				item.setGoods_desc(c.getString(c.getColumnIndex("goods_desc")));
				item.setTime_status(c.getString(c.getColumnIndex("time_status")));
				item.setBuy_status(c.getString(c.getColumnIndex("buy_status")));
				item.setBuy_type(c.getString(c.getColumnIndex("buy_type")));
				item.setCount_image(c.getString(c.getColumnIndex("count_image")));
				item.setStart_times(c.getString(c.getColumnIndex("start_times")));
				item.setSp_open_times(c.getString(c
						.getColumnIndex("sp_open_times")));
				item.setCur_price(c.getString(c.getColumnIndex("cur_price")));
				item.setNum(c.getString(c.getColumnIndex("num")));
				items.add(item);
			}
			c.close();
		}
		return items;
	}
	
	/**
	 * 取得该账号购物车里面的特定商品
	 * @param goods_id
	 * @param email
	 */
	public PrivilegeItem getSelectItem(String email,String goods_id ) {
		// HomeUser home_user = new HomeUser();
		PrivilegeItem item = new PrivilegeItem();

		String table = "Mycart";
		String[] columns = { "*" };
		String selection = "email = ? AND goods_id = ? ";
		String[] selectionArgs = { email,goods_id };
		String groupBy = null;
		String having = null;
		String orderBy = null;
		if (CheckTable.tabbleIsExist(context, table)) {
			Cursor c = db.query(table, columns, selection, selectionArgs,
					groupBy, having, orderBy);
			while (c.moveToNext()) {
				
				item.setGoods_id(c.getString(c.getColumnIndex("goods_id")));
				item.setTitle(c.getString(c.getColumnIndex("title")));
				item.setImage(c.getString(c.getColumnIndex("image")));
				item.setBuy_count(c.getString(c.getColumnIndex("buy_count")));
				item.setOri_price_format(c.getString(c
						.getColumnIndex("ori_price_format")));
				item.setCur_price_format(c.getString(c
						.getColumnIndex("cur_price_format")));
				item.setDiscount(c.getString(c.getColumnIndex("discount")));
				item.setAddress(c.getString(c.getColumnIndex("address")));
				item.setSp_detail(c.getString(c.getColumnIndex("sp_detail")));
				item.setSp_tel(c.getString(c.getColumnIndex("sp_tel")));
				item.setSaving_format(c.getString(c
						.getColumnIndex("saving_format")));
				item.setLess_time(c.getString(c.getColumnIndex("less_time")));
				item.setXpoint(c.getString(c.getColumnIndex("xpoint")));
				item.setYpoint(c.getString(c.getColumnIndex("ypoint")));
				item.setDistance(c.getString(c.getColumnIndex("distance")));
				item.setGoods_brief(c.getString(c.getColumnIndex("goods_brief")));
				item.setGoods_desc(c.getString(c.getColumnIndex("goods_desc")));
				item.setTime_status(c.getString(c.getColumnIndex("time_status")));
				item.setBuy_status(c.getString(c.getColumnIndex("buy_status")));
				item.setBuy_type(c.getString(c.getColumnIndex("buy_type")));
				item.setCount_image(c.getString(c.getColumnIndex("count_image")));
				item.setStart_times(c.getString(c.getColumnIndex("start_times")));
				item.setSp_open_times(c.getString(c
						.getColumnIndex("sp_open_times")));
				item.setCur_price(c.getString(c.getColumnIndex("cur_price")));
				item.setNum(c.getString(c.getColumnIndex("num")));
			}
			c.close();
		}
		return item;
	}

	
	public void updateCart(PrivilegeItem item) {
		ContentValues values = new ContentValues();
		
		values.put("num", item.getNum());
		values.put("count", item.getCount());
		
		db.update("Mycart", values, "email = ? AND goods_id = ? ",
				new String[] {
				MzeatApplication.getInstance().getpPreferencesConfig().getString("email", ""),
				item.getGoods_id()});
		db.close();
	}
	/**
	 * close database
	 */
	public void closeDB() {
		db.close();
	}

}
