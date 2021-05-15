package com.example.stock.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.stock.DB.Stock;

public class StockDbHelper extends SQLiteOpenHelper {

  public static final int DATABASE_VERSION = 1;
  public static final String DATABASE_NAME = "Stock.db";

  public StockDbHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
  }

  public void onCreate(SQLiteDatabase db) {
    db.execSQL(Stock.StockCategory.SQL_CREATE_CATEGORY);
    db.execSQL(Stock.StockComponent.SQL_CREATE_COMPONENT);
    db.execSQL(Stock.StockMember.SQL_CREATE_MEMBER);
    db.execSQL(Stock.StockBorrow.SQL_CREATE_BORROW);
  }

  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL(Stock.StockCategory.SQL_DELETE_CATEGORY);
    db.execSQL(Stock.StockComponent.SQL_DELETE_COMPONENT);
    db.execSQL(Stock.StockMember.SQL_DELETE_MEMBER);
    db.execSQL(Stock.StockBorrow.SQL_DELETE_BORROW);
    onCreate(db);
  }

  public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    onUpgrade(db, oldVersion, newVersion);
  }
}
