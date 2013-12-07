package database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class StockDataSource {
	// Database fields
	private SQLiteDatabase database;
	private ShareDBHelper dbHelper;
	private String[] allColumns = ShareDBHelper.stock_table_fields;

	public StockDataSource(Context context) {
		dbHelper = new ShareDBHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public long createShare(SimpleQuoteDB sq, String username) {
		ContentValues values = new ContentValues();
		values.put(allColumns[0], sq.getCompanyName());
		values.put(allColumns[1], sq.getShareNumber());
		values.put(allColumns[2], username);
		values.put(allColumns[3], sq.getCurrentValue());
		return database.insert(ShareDBHelper.TABLE_STOCK, null, values);
	}

	public long addUser(String username, String password) {
		ContentValues values = new ContentValues();
		values.put("username", username);
		values.put("password", password);
		return database.insert(ShareDBHelper.TABLE_USER, null, values);
	}

	public long updateShare(SimpleQuoteDB sq, String username) {
		ContentValues values = new ContentValues();
		values.put(allColumns[1], sq.getShareNumber());
		values.put(allColumns[3], sq.getCurrentValue());
		return database.update(ShareDBHelper.TABLE_STOCK, values, allColumns[0]
				+ "=? AND " + allColumns[2] + "=?",
				new String[] { sq.getCompanyName(), username });
	}

	public void deleteShare(String companyID, String username) {
		database.delete(ShareDBHelper.TABLE_STOCK, allColumns[0] + "=? AND "
				+ allColumns[2] + "=?", new String[] { companyID, username });
	}

	public boolean validateLogin(String username, String password) {
		Cursor cursor = database.query(ShareDBHelper.TABLE_USER, new String[] {
				"username", "password" }, "username=?",
				new String[] { username }, null, null, null);
		cursor.moveToFirst();
		if(!cursor.isAfterLast())
			return cursor.getString(1).equals(password);
		else 
			return false;
	}

	public ArrayList<SimpleQuoteDB> getAllShares(String username) {
		ArrayList<SimpleQuoteDB> quotes = new ArrayList<SimpleQuoteDB>();
		Cursor cursor = database.query(ShareDBHelper.TABLE_STOCK, allColumns,
				null, null, null, null, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			if (cursor.getString(2).equals(username)) {
				SimpleQuoteDB sq = cursorToSimpleQuote(cursor);
				quotes.add(sq);
			}
			cursor.moveToNext();
		}
		cursor.close();
		return quotes;
	}

	private SimpleQuoteDB cursorToSimpleQuote(Cursor cursor) {
		SimpleQuoteDB sq = new SimpleQuoteDB();
		sq.setCompanyName(cursor.getString(0));
		sq.setShareNumber(cursor.getInt(1));
		sq.setCurrentValue(cursor.getDouble(3));
		return sq;
	}
}
