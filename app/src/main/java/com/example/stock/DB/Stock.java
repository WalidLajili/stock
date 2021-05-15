package com.example.stock.DB;

import android.provider.BaseColumns;

public final class Stock {

  private Stock() {}

  public static class StockCategory implements BaseColumns {

    public static final String TABLE_NAME = "categories";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String SQL_CREATE_CATEGORY =
      "CREATE TABLE " +
      StockCategory.TABLE_NAME +
      " (" +
      StockCategory._ID +
      " INTEGER PRIMARY KEY," +
      StockCategory.COLUMN_NAME_TITLE +
      " TEXT)";
    public static final String SQL_DELETE_CATEGORY =
      "DROP TABLE IF EXISTS " + StockCategory.TABLE_NAME;
  }

  public static class StockComponent implements BaseColumns {

    public static final String TABLE_NAME = "components";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_CREATE_DATE = "created_at";
    public static final String COLUMN_NAME_QUANTITY = "quantity";
    public static final String COLUMN_NAME_CATEGORY = "category_id";
    public static final String SQL_CREATE_COMPONENT =
      "CREATE TABLE " +
      StockComponent.TABLE_NAME +
      " (" +
      StockComponent._ID +
      " INTEGER PRIMARY KEY," +
      StockComponent.COLUMN_NAME_TITLE +
      " TEXT," +
      StockComponent.COLUMN_NAME_QUANTITY +
      " INTEGER," +
      StockComponent.COLUMN_NAME_CREATE_DATE +
      " TEXT," +
      StockComponent.COLUMN_NAME_CATEGORY +
      " INTEGER, FOREIGN KEY (" +
      StockComponent.COLUMN_NAME_CATEGORY +
      ") REFERENCES " +
      StockCategory.TABLE_NAME +
      "(" +
      StockCategory._ID +
      "))";
    public static final String SQL_DELETE_COMPONENT =
      "DROP TABLE IF EXISTS " + StockComponent.TABLE_NAME;
  }

  public static class StockMember implements BaseColumns {

    public static final String TABLE_NAME = "members";
    public static final String COLUMN_NAME_FIRST_NAME = "first_name";
    public static final String COLUMN_NAME_LAST_NAME = "last_name";
    public static final String COLUMN_NAME_PHONE_NUMBER = "phone_number";
    public static final String SQL_CREATE_MEMBER =
      "CREATE TABLE " +
      StockMember.TABLE_NAME +
      " (" +
      StockMember._ID +
      " INTEGER PRIMARY KEY," +
      StockMember.COLUMN_NAME_FIRST_NAME +
      " TEXT," +
      StockMember.COLUMN_NAME_LAST_NAME +
      " TEXT," +
      StockMember.COLUMN_NAME_PHONE_NUMBER +
      " TEXT)";
    public static final String SQL_DELETE_MEMBER =
      "DROP TABLE IF EXISTS " + StockMember.TABLE_NAME;
  }

  public static class StockBorrow implements BaseColumns {

    public static final String TABLE_NAME = "borrows";
    public static final String COLUMN_NAME_COMPONENT = "component_id";
    public static final String COLUMN_NAME_MEMBER = "member_id";
    public static final String COLUMN_NAME_QUANTITY = "borrow_quantity";
    public static final String COLUMN_NAME_CREATE_DATE = "created_at";
    public static final String COLUMN_NAME_RETURN_DATE = "returned_at";
    public static final String COLUMN_NAME_STATE = "state";

    public static final String SQL_CREATE_BORROW =
      "CREATE TABLE " +
      StockBorrow.TABLE_NAME +
      " (" +
      StockBorrow._ID +
      " INTEGER PRIMARY KEY," +
      StockBorrow.COLUMN_NAME_MEMBER +
      " INTEGER, " +
      COLUMN_NAME_QUANTITY +
      " INTEGER," +
      StockBorrow.COLUMN_NAME_CREATE_DATE +
      " TEXT, " +
      StockBorrow.COLUMN_NAME_RETURN_DATE +
      " TEXT, " +
      StockBorrow.COLUMN_NAME_STATE +
      " INTEGER DEFAULT -1, " +
      StockBorrow.COLUMN_NAME_COMPONENT +
      " INTEGER, FOREIGN KEY (" +
      StockBorrow.COLUMN_NAME_COMPONENT +
      ") REFERENCES " +
      StockComponent.TABLE_NAME +
      "(" +
      StockComponent._ID +
      "), " +
      "FOREIGN KEY (" +
      StockBorrow.COLUMN_NAME_MEMBER +
      ") REFERENCES " +
      StockMember.TABLE_NAME +
      "(" +
      StockMember._ID +
      ")) ";
    public static final String SQL_DELETE_BORROW =
      "DROP TABLE IF EXISTS " + StockBorrow.TABLE_NAME;
  }
}
