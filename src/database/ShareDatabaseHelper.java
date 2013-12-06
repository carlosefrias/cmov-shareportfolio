package database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ShareDatabaseHelper extends SQLiteOpenHelper{
	public static String TABLE_STOCK = "Stock";
	public static String TABLE_USER = "User";
	public static String[] stock_table_fields = new String[]{"companyID", "shareNumber", "username", "correntValue"};
	public static String[] user_table_fields = new String[]{"username", "password"};
	
	public ShareDatabaseHelper(Context context) {
		super(context, "Shares.db", null, 1);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE Stock(companyID VARCHAR(5), shareNumber INT(4), username VARCHAR(20), correntValue DOUBLE(6));");
		db.execSQL("CREATE TABLE User(username VARCHAR(20), password VARCHAR(20));");
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		db.execSQL("DROP TABLE IF EXISTS Stock");
		db.execSQL("DROP TABLE IF EXISTS User");
		onCreate(db);		
	}
}
