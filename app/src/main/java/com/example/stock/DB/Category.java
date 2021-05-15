package com.example.stock.DB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

public class Category {

  private long id;
  private String title;

  public Category(long id, String title) {
    this.id = id;
    this.title = title;
  }

  public long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public static ArrayList<Category> find(SQLiteDatabase db) {
    ArrayList<Category> categories = new ArrayList();
    String[] projection = {
      Stock.StockCategory._ID,
      Stock.StockCategory.COLUMN_NAME_TITLE,
    };

    Cursor cursor = db.query(
      Stock.StockCategory.TABLE_NAME, // The table to query
      projection, // The array of columns to return (pass null to get all)
      null, // The columns for the WHERE clause
      null, // The values for the WHERE clause
      null, // don't group the rows
      null, // don't filter by row groups
      null // The sort order
    );

    while (cursor.moveToNext()) {
      long id = cursor.getLong(
        cursor.getColumnIndexOrThrow(Stock.StockCategory._ID)
      );
      String title = cursor.getString(
        cursor.getColumnIndexOrThrow(Stock.StockCategory.COLUMN_NAME_TITLE)
      );
      Category category = new Category(id, title);

      categories.add(category);
    }
    cursor.close();
    return categories;
  }

  public static Category findById(SQLiteDatabase db, long categoryId) {
    Category category = null;

    String[] projection = {
      Stock.StockCategory._ID,
      Stock.StockCategory.COLUMN_NAME_TITLE,
    };

    Cursor cursor = db.query(
      Stock.StockCategory.TABLE_NAME, // The table to query
      projection, // The array of columns to return (pass null to get all)
      Stock.StockCategory._ID + " = ?", // The columns for the WHERE clause
      new String[] { Long.toString(categoryId) }, // The values for the WHERE clause
      null, // don't group the rows
      null, // don't filter by row groups
      null // The sort order
    );

    if (cursor.moveToNext()) {
      long id = cursor.getLong(
        cursor.getColumnIndexOrThrow(Stock.StockCategory._ID)
      );
      String title = cursor.getString(
        cursor.getColumnIndexOrThrow(Stock.StockCategory.COLUMN_NAME_TITLE)
      );

      category = new Category(id, title);
    }

    cursor.close();

    return category;
  }

  public static void add(SQLiteDatabase db, String title) {
    ContentValues values = new ContentValues();

    values.put(Stock.StockCategory.COLUMN_NAME_TITLE, title);

    db.insert(Stock.StockCategory.TABLE_NAME, null, values);
  }
}
