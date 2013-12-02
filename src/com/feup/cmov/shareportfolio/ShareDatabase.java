package com.feup.cmov.shareportfolio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ShareDatabase extends SQLiteOpenHelper{
	public ShareDatabase(Context context) {
		super(context, "Share.db", null, 1);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE Stock(companyID VARCHAR(4), shareNumber INT(4));");
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS Ticket");
		onCreate(db);		
	}
	public Cursor getAllShares(){
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.rawQuery("SELECT * from Stock", null);
		return cursor;
	}
	
	public void addShare(int shareNumber, String companyName){
		SQLiteDatabase db = getWritableDatabase();
		ContentValues content = new ContentValues();
		/*for(int i = 0; i < nTickets; i++){
			content.put("idticket", uuids[i]);
			content.put("type", types[i]);
			content.put("ischecked", 0);
			content.put("isvalidated", 0);
			content.put("timeofvalidation", "");
			content.put("idbus", "");
			db.insert("Ticket", null, content);
		}*/
	}

}
