package com.example.stock.DB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;

public class Member {

  private long id;
  private String firstName;
  private String lastName;
  private String phone;

  public Member(long id, String firstName, String lastName, String phone) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.phone = phone;
  }

  public long getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public String getPhone() {
    return phone;
  }

  public static ArrayList<Member> find(SQLiteDatabase db) {
    ArrayList<Member> members = new ArrayList();
    String[] projection = {
      Stock.StockMember._ID,
      Stock.StockMember.COLUMN_NAME_FIRST_NAME,
      Stock.StockMember.COLUMN_NAME_LAST_NAME,
      Stock.StockMember.COLUMN_NAME_PHONE_NUMBER,
    };

    Cursor cursor = db.query(
      Stock.StockMember.TABLE_NAME, // The table to query
      projection, // The array of columns to return (pass null to get all)
      null, // The columns for the WHERE clause
      null, // The values for the WHERE clause
      null, // don't group the rows
      null, // don't filter by row groups
      null // The sort order
    );

    while (cursor.moveToNext()) {
      long id = cursor.getLong(
        cursor.getColumnIndexOrThrow(Stock.StockMember._ID)
      );
      String firstName = cursor.getString(
        cursor.getColumnIndexOrThrow(Stock.StockMember.COLUMN_NAME_FIRST_NAME)
      );

      String lastName = cursor.getString(
        cursor.getColumnIndexOrThrow(Stock.StockMember.COLUMN_NAME_LAST_NAME)
      );

      String phoneNumber = cursor.getString(
        cursor.getColumnIndexOrThrow(Stock.StockMember.COLUMN_NAME_PHONE_NUMBER)
      );
      members.add(new Member(id, firstName, lastName, phoneNumber));
    }

    cursor.close();
    return members;
  }

  public static void add(
    SQLiteDatabase db,
    String firstName,
    String lastName,
    String phone
  ) {
    ContentValues values = new ContentValues();

    values.put(Stock.StockMember.COLUMN_NAME_FIRST_NAME, firstName);
    values.put(Stock.StockMember.COLUMN_NAME_LAST_NAME, lastName);
    values.put(Stock.StockMember.COLUMN_NAME_PHONE_NUMBER, phone);

    db.insert(Stock.StockMember.TABLE_NAME, null, values);
  }
}
