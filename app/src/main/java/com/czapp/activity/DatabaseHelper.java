package com.czapp.activity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DB_NAME = "myMap.db"; // 数据库名称
	private static final int VERSION = 1;
	private static final String SWORD = "SWORD";

	// 三个不同参数的构造函数
	// 带全部参数的构造函数，此构造函数必不可少
	public DatabaseHelper(Context context, CursorFactory factory,
						  int version) {
		super(context, DB_NAME, factory, version);

	}

	// 带两个参数的构造函数，调用的其实是带三个参数的构造函数
	public DatabaseHelper(Context context) {
		this(context, VERSION);
	}

	// 带三个参数的构造函数，调用的是带所有参数的构造函数
	public DatabaseHelper(Context context, int version) {
		this(context,null, version);
	}

	// 创建数据库
	public void onCreate(SQLiteDatabase db) {
		Log.i(SWORD, "create a MyMap");
		// 创建数据库sql语句 (id,经度,纬度,时间)
		String sql = "create table MyLocus(id INTEGER PRIMARY KEY,Longitude float,Latitude float,Time datetime)";
		// 执行创建数据库操作
		db.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// 创建成功，日志输出提示
		Log.i(SWORD, "update a MyMap");
	}
	public void DeleteDatabase(SQLiteDatabase db,String table)
	{
	    db.execSQL("DELETE FROM " + table);
	    db.execSQL("update MyLocus SET seq = 0 where name = '"+table+"'");
//	    db.execSQL("update sqlite_sequence SET seq = 0 where name = '"+table+"'");
	   db.close();
	}

}