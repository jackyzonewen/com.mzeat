package com.mzeat.db;

import java.util.ArrayList;
import java.util.List;

import com.mzeat.model.Invite;
import com.mzeat.model.Shopping;
import com.mzeat.util.CheckTable;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class InviteDb {
	private DatabaseHelper databaseHelper;
	private SQLiteDatabase db;
	private Context context;
	public InviteDb(Context context) {
		this.context = context;
		databaseHelper = DatabaseHelper.getInstance(context);
		db = databaseHelper.getWritableDatabase();

	}

	public void add(List<Invite> columns) {
		db.beginTransaction(); // 开始事务
		/** 数据库SQL语句 添加一个表 **/
		String Invite = "create table Invite ("
				+ "_id INTEGER PRIMARY KEY AUTOINCREMENT," 
				+ "yczp_id TEXT,"
				+ "Post TEXT," 
				+ "sex TEXT," 
				+ "Degree TEXT," 
				+ "number TEXT,"
				+ "Treatment TEXT," 
				+ "Phone TEXT," 
				+ "contact TEXT,"
				+ "Address TEXT," 
				+ "Claim TEXT," 
				+ "Business TEXT,"
				+ "create_time TEXT" 
 + ");";
		db.execSQL("DROP TABLE IF EXISTS Invite");
		db.execSQL(Invite);
		try {
			for (Invite column : columns) {
				db.execSQL(
						"INSERT INTO Invite VALUES(null, ?, ?, ?,?,?,?,?,?,?,?,?,?)",
						new Object[] { 
								column.getYczp_id(), 
								column.getPost(),
								column.getSex(), 
								column.getDegree(),
								column.getNumber(), 
								column.getTreatment(),
								column.getPhone(), 
								column.getContact(),
								column.getAddress(), 
								column.getClaim(),
								column.getBusiness(),
								column.getCreate_time()
							
						});
			}
			db.setTransactionSuccessful(); // 设置事务成功完成
		} finally {
			db.endTransaction(); // 结束事务
		}
	}

	public void deleteAll() {
		db.delete("Invite", null, null);

	}

	public void delete(String id) {
		String whereClause = "id = ?";
		String[] whereArgs = { id };

		db.delete("Invite", whereClause, whereArgs);

	}

	public ArrayList<Invite> getInvite() {

		ArrayList<Invite> mInvite = new ArrayList<Invite>();

		String table = "Invite";
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
			Invite invite = new Invite();
			invite.setYczp_id(c.getString(c.getColumnIndex("yczp_id")));
			invite.setPost(c.getString(c.getColumnIndex("Post")));
			invite.setSex(c.getString(c.getColumnIndex("sex")));
			
			invite.setDegree(c.getString(c.getColumnIndex("Degree")));
			invite.setNumber(c.getString(c.getColumnIndex("number")));
			invite.setTreatment(c.getString(c.getColumnIndex("Treatment")));
			invite.setPhone(c.getString(c.getColumnIndex("Phone")));
			invite.setContact(c.getString(c.getColumnIndex("contact")));
			
			invite.setAddress(c.getString(c.getColumnIndex("Address")));
			invite.setClaim(c.getString(c.getColumnIndex("Claim")));
			invite.setBusiness(c.getString(c.getColumnIndex("Business")));
			invite.setCreate_time(c.getString(c.getColumnIndex("create_time")));
			
			mInvite.add(invite);
		}
		c.close();}
		return mInvite;
	}

	/**
	 * close database
	 */
	public void closeDB() {
		db.close();
	}
}
