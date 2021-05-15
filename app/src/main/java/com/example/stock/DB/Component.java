package com.example.stock.DB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Component {

  private long id;
  private String title;
  private int quantity;
  private Date createdAt;

  public Component(long id, String title, int quantity, String createdAt) {
    this.id = id;
    this.title = title;
    this.quantity = quantity;
    try {
      SimpleDateFormat format = new SimpleDateFormat(
        "EEE MMM dd HH:mm:ss zzz yyyy"
      );
      this.createdAt = format.parse(createdAt);
    } catch (Exception pe) {
      // do nothing
    }
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

  public int getQuantity() {
    return quantity;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public int findAvailableQuantity(SQLiteDatabase db) {
    int availableQuantity = this.quantity - this.findBorrowedQuantity(db);
    return availableQuantity;
  }

  public int findBorrowedQuantity(SQLiteDatabase db) {
    int borrowedQuantity = 0;
    String query =
      "SELECT SUM(" +
      Stock.StockBorrow.COLUMN_NAME_QUANTITY +
      ") FROM " +
      Stock.StockBorrow.TABLE_NAME +
      " WHERE " +
      Stock.StockBorrow.COLUMN_NAME_COMPONENT +
      " = " +
      this.id +
      " AND " +
      Stock.StockBorrow.COLUMN_NAME_RETURN_DATE +
      " IS NULL";
    Cursor cursor = db.rawQuery(query, null);
    if (cursor.moveToFirst()) {
      borrowedQuantity = cursor.getInt(0);
    }
    cursor.close();
    return borrowedQuantity;
  }

  public static ArrayList<Component> find(
    SQLiteDatabase db,
    String selection,
    String[] selectionArgs
  ) {
    ArrayList<Component> components = new ArrayList();
    String[] projection = {
      Stock.StockComponent._ID,
      Stock.StockComponent.COLUMN_NAME_TITLE,
      Stock.StockComponent.COLUMN_NAME_QUANTITY,
      Stock.StockComponent.COLUMN_NAME_CREATE_DATE,
    };

    Cursor cursor = db.query(
      Stock.StockComponent.TABLE_NAME, // The table to query
      projection, // The array of columns to return (pass null to get all)
      selection, // The columns for the WHERE clause
      selectionArgs, // The values for the WHERE clause
      null, // don't group the rows
      null, // don't filter by row groups
      null // The sort order
    );

    while (cursor.moveToNext()) {
      long id = cursor.getLong(
        cursor.getColumnIndexOrThrow(Stock.StockComponent._ID)
      );
      String title = cursor.getString(
        cursor.getColumnIndexOrThrow(Stock.StockComponent.COLUMN_NAME_TITLE)
      );

      int quantity = cursor.getInt(
        cursor.getColumnIndexOrThrow(Stock.StockComponent.COLUMN_NAME_QUANTITY)
      );
      String createdAt = cursor.getString(
        cursor.getColumnIndexOrThrow(
          Stock.StockComponent.COLUMN_NAME_CREATE_DATE
        )
      );

      Component component = new Component(id, title, quantity, createdAt);

      components.add(component);
    }

    cursor.close();
    return components;
  }

  public static Component findById(SQLiteDatabase db, long componentId) {
    Component component = null;

    String[] projection = {
      Stock.StockComponent._ID,
      Stock.StockComponent.COLUMN_NAME_TITLE,
      Stock.StockComponent.COLUMN_NAME_QUANTITY,
      Stock.StockComponent.COLUMN_NAME_CREATE_DATE,
    };

    Cursor cursor = db.query(
      Stock.StockComponent.TABLE_NAME, // The table to query
      projection, // The array of columns to return (pass null to get all)
      Stock.StockCategory._ID + " = ?", // The columns for the WHERE clause
      new String[] { Long.toString(componentId) }, // The values for the WHERE clause
      null, // don't group the rows
      null, // don't filter by row groups
      null // The sort order
    );

    if (cursor.moveToNext()) {
      long id = cursor.getLong(
        cursor.getColumnIndexOrThrow(Stock.StockComponent._ID)
      );
      String title = cursor.getString(
        cursor.getColumnIndexOrThrow(Stock.StockComponent.COLUMN_NAME_TITLE)
      );

      int quantity = cursor.getInt(
        cursor.getColumnIndexOrThrow(Stock.StockComponent.COLUMN_NAME_QUANTITY)
      );

      String createdAt = cursor.getString(
        cursor.getColumnIndexOrThrow(
          Stock.StockComponent.COLUMN_NAME_CREATE_DATE
        )
      );

      component = new Component(id, title, quantity, createdAt);
    }

    cursor.close();

    return component;
  }

  public static void add(
    SQLiteDatabase db,
    String title,
    int quantity,
    long category
  ) {
    ContentValues values = new ContentValues();

    values.put(Stock.StockComponent.COLUMN_NAME_TITLE, title);
    values.put(Stock.StockComponent.COLUMN_NAME_QUANTITY, quantity);
    values.put(Stock.StockComponent.COLUMN_NAME_CATEGORY, category);
    values.put(
      Stock.StockComponent.COLUMN_NAME_CREATE_DATE,
      new Date().toString()
    );

    db.insert(Stock.StockComponent.TABLE_NAME, null, values);
  }

  public static void update(SQLiteDatabase db, long id, int quantity) {
    ContentValues values = new ContentValues();
    values.put(Stock.StockComponent.COLUMN_NAME_QUANTITY, quantity);
    db.update(
      Stock.StockComponent.TABLE_NAME,
      values,
      Stock.StockComponent._ID + " = ?",
      new String[] { Long.toString(id) }
    );
  }
}
