package com.mess.ordermess.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MessMessageDB extends SQLiteOpenHelper {

	public MessMessageDB(Context context) {
		super(context, "OrderMess.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table messMenu(mess_id varchar(20)," +
				"mess_Name varchar)");		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
