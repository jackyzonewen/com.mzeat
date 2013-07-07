package com.mzeat.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static DatabaseHelper mInstance = null;
	private Context context;
	private SQLiteDatabase mDb = null;
	private String TABLE_NAME = "";

	/** 数据库名称 **/
	public static final String DATABASE_NAME = "com.mzeat.db";

	/** 数据库版本号 **/
	private static final int DATABASE_VERSION = 1;

	DatabaseHelper(Context context) {
		// CursorFactory设置为null,使用默认值
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	/** 单例模式 **/
	public static synchronized DatabaseHelper getInstance(Context context) {
		if (mInstance == null) {
			mInstance = new DatabaseHelper(context);
		}
		return mInstance;
	}

/** 数据库SQL语句 添加一个表 **/

	private static final String Poster = 
			"create table Poster("
			+ "_id INTEGER PRIMARY KEY AUTOINCREMENT,"
			+ "id TEXT,"
			+ "name TEXT,"
			+ "img TEXT" 
			+ ");";
	/**
	private static String food = 
			"create table Food("
			+ "_id INTEGER PRIMARY KEY AUTOINCREMENT," 
			+ "id TEXT,"
			+ "name TEXT," 
			+ "logo TEXT" 
			+ "xpoint TEXT" 
			+ "ypoint TEXT"
			+ "api_address TEXT" 
			+ "mzeatvip TEXT" 
			+ "address TEXT"
			+ "tel TEXT" 
			+ "is_dy TEXT" 
			+ "city_name TEXT"
			+ "comment_count TEXT" 
			+ "brand_id TEXT" 
			+ "distance TEXT"
			+ "brief TEXT" + ");";
	 
**/
	@Override
	public void onCreate(SQLiteDatabase db) {
		/** 向数据中添加表 **/
		db.execSQL("DROP TABLE IF EXISTS Poster");
		db.execSQL(Poster);
		//db.execSQL("DROP TABLE IF EXISTS Food");
		//db.execSQL(food);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		/** 可以拿到当前数据库的版本信息 与之前数据库的版本信息 用来更新数据库 **/
	}

	/**
	 * 删除数据库
	 * 
	 * @param context
	 * @return
	 */
	public boolean deleteDatabase(Context context) {

		return context.deleteDatabase(DATABASE_NAME);
	}

	public void creatTable(String sql) {
		/** 创建一张表的SQL语句 **/
		mDb = this.getReadableDatabase();
		String NAME_TABLE_CREATE = sql;
		try {
			mDb.execSQL(NAME_TABLE_CREATE);
		} catch (SQLiteException e) {
		}
	}

	public void deleteTable(String table) {
		/** 删除一张表的SQL语句 **/
		mDb = this.getReadableDatabase();
		String NAME_TABLE_DELETE = table;
		try {
			mDb.execSQL(NAME_TABLE_DELETE);
		} catch (SQLiteException e) {
		}
	}

	// 设置数据库表名称
	public void setTableName(String tableName) {
		this.TABLE_NAME = tableName;
	}

}