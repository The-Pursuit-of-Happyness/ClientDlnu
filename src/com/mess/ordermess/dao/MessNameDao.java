package com.mess.ordermess.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mess.ordermess.db.MessMessageDB;

public class MessNameDao {
	
	private MessMessageDB dpHelper;
	private Context context;
	
	public MessNameDao(Context context){
		this.context = context;
		dpHelper = new MessMessageDB(context);
	}
	
	/**
	 * 插入数据
	 * @param mess
	 */
	public void insertMessMsg(final List<MessMenu> mess){
		final SQLiteDatabase db = dpHelper.getReadableDatabase();		
		new Thread(){
			public void run(){
				for(MessMenu object : mess){
					ContentValues values = new ContentValues();
					values.put("mess_id", object.getMess_Id());
					values.put("mess_Name", object.getMess_Name());
					db.insert("messMenu", null, values);
				}
				db.close();
			}
		}.start();
	}
	
	/**
	 * 取得菜品名称
	 * @return
	 */
	public List<String> getMessMsg(){
		List<String> result = new ArrayList<String>();
		SQLiteDatabase db = dpHelper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select mess_Name from messMenu",new String[]{});
		while(cursor.moveToNext()){
			result.add(cursor.getString(0));
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result;
	}
}
