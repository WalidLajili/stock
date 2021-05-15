package com.example.stock.DB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Borrow {

  private long id;
  private Member member;
  private Component component;
  private int quantity;
  private Date createdAt;
  private Date returnedAt;
  private int state;

  private boolean returned;

  public Borrow(
    long id,
    Member member,
    Component component,
    int quantity,
    String createdAt
  ) {
    this.id = id;
    this.member = member;
    this.component = component;
    this.quantity = quantity;
    try {
      SimpleDateFormat format = new SimpleDateFormat(
        "EEE MMM dd HH:mm:ss zzz yyyy"
      );
      this.createdAt = format.parse(createdAt);
    } catch (Exception pe) {}

    this.returned = false;
  }

  public Borrow(
    long id,
    Member member,
    Component component,
    int quantity,
    String createdAt,
    String returnedAt,
    int state
  ) {
    this.id = id;
    this.member = member;
    this.component = component;
    this.state = state;
    this.quantity = quantity;
    try {
      SimpleDateFormat format = new SimpleDateFormat(
        "EEE MMM dd HH:mm:ss zzz yyyy"
      );
      this.createdAt = format.parse(createdAt);
      this.returnedAt = format.parse(returnedAt);
    } catch (Exception pe) {}
    this.returned = true;
  }

  public long getId() {
    return id;
  }

  public Member getMember() {
    return member;
  }

  public void setMember(Member member) {
    this.member = member;
  }

  public Component getComponent() {
    return component;
  }

  public Date getCreatedAt() {
    return createdAt;
  }

  public Date getReturnedAt() {
    return returnedAt;
  }

  public int getState() {
    return state;
  }

  public boolean isReturned() {
    return returned;
  }

  public int getQuantity() {
    return quantity;
  }

  public static Borrow initializeBorrowFromCursor(Cursor cursor) {
    // borrow rows
    long id = cursor.getLong(
      cursor.getColumnIndex(
        Stock.StockBorrow.TABLE_NAME + Stock.StockBorrow._ID
      )
    );
    long memberId = cursor.getLong(
      cursor.getColumnIndex(Stock.StockBorrow.COLUMN_NAME_MEMBER)
    );
    long componentId = cursor.getLong(
      cursor.getColumnIndex(Stock.StockBorrow.COLUMN_NAME_COMPONENT)
    );
    int quantity = cursor.getInt(
      cursor.getColumnIndex(Stock.StockBorrow.COLUMN_NAME_QUANTITY)
    );
    String createdAt = cursor.getString(
      cursor.getColumnIndex(Stock.StockBorrow.COLUMN_NAME_CREATE_DATE)
    );

    // member rows
    String memberFirstName = cursor.getString(
      cursor.getColumnIndex(Stock.StockMember.COLUMN_NAME_FIRST_NAME)
    );
    String memberLastName = cursor.getString(
      cursor.getColumnIndex(Stock.StockMember.COLUMN_NAME_LAST_NAME)
    );
    String memberPhone = cursor.getString(
      cursor.getColumnIndex(Stock.StockMember.COLUMN_NAME_PHONE_NUMBER)
    );

    Member member = new Member(
      memberId,
      memberFirstName,
      memberLastName,
      memberPhone
    );

    // component rows
    String componentTitle = cursor.getString(
      cursor.getColumnIndex(Stock.StockComponent.COLUMN_NAME_TITLE)
    );
    int componentQuantity = cursor.getInt(
      cursor.getColumnIndex(Stock.StockComponent.COLUMN_NAME_QUANTITY)
    );
    String componentCreatedAt = cursor.getString(
      cursor.getColumnIndex(Stock.StockComponent.COLUMN_NAME_CREATE_DATE)
    );

    Component component = new Component(
      componentId,
      componentTitle,
      componentQuantity,
      componentCreatedAt
    );

    String returnedDate = cursor.getString(
      cursor.getColumnIndexOrThrow(Stock.StockBorrow.COLUMN_NAME_RETURN_DATE)
    );
    int state = cursor.getInt(
      cursor.getColumnIndexOrThrow(Stock.StockBorrow.COLUMN_NAME_STATE)
    );
    if (returnedDate != null && !returnedDate.isEmpty()) {
      return new Borrow(
        id,
        member,
        component,
        quantity,
        createdAt,
        returnedDate,
        state
      );
    }
    return new Borrow(id, member, component, quantity, createdAt);
  }

  public static ArrayList<Borrow> find(SQLiteDatabase db) {
    ArrayList<Borrow> borrows = new ArrayList();
    String query =
      "SELECT " +
      Stock.StockBorrow.TABLE_NAME +
      "." +
      Stock.StockBorrow._ID +
      " AS " +
      Stock.StockBorrow.TABLE_NAME +
      Stock.StockBorrow._ID +
      ",* FROM " +
      Stock.StockBorrow.TABLE_NAME +
      " INNER JOIN " +
      Stock.StockMember.TABLE_NAME +
      " ON " +
      Stock.StockBorrow.TABLE_NAME +
      "." +
      Stock.StockBorrow.COLUMN_NAME_MEMBER +
      " = " +
      Stock.StockMember.TABLE_NAME +
      "." +
      Stock.StockMember._ID +
      " INNER JOIN " +
      Stock.StockComponent.TABLE_NAME +
      " ON " +
      Stock.StockBorrow.TABLE_NAME +
      "." +
      Stock.StockBorrow.COLUMN_NAME_COMPONENT +
      " = " +
      Stock.StockComponent.TABLE_NAME +
      "." +
      Stock.StockComponent._ID;

    Cursor cursor = db.rawQuery(query, null);

    while (cursor.moveToNext()) {
      borrows.add(initializeBorrowFromCursor(cursor));
    }
    cursor.close();

    return borrows;
  }

  public static Borrow findById(SQLiteDatabase db, long borrowId) {
    Borrow borrow = null;
    String query =
      "SELECT " +
      Stock.StockBorrow.TABLE_NAME +
      "." +
      Stock.StockBorrow._ID +
      " AS " +
      Stock.StockBorrow.TABLE_NAME +
      Stock.StockBorrow._ID +
      ",* FROM " +
      Stock.StockBorrow.TABLE_NAME +
      " INNER JOIN " +
      Stock.StockMember.TABLE_NAME +
      " ON " +
      Stock.StockBorrow.TABLE_NAME +
      "." +
      Stock.StockBorrow.COLUMN_NAME_MEMBER +
      " = " +
      Stock.StockMember.TABLE_NAME +
      "." +
      Stock.StockMember._ID +
      " INNER JOIN " +
      Stock.StockComponent.TABLE_NAME +
      " ON " +
      Stock.StockBorrow.TABLE_NAME +
      "." +
      Stock.StockBorrow.COLUMN_NAME_COMPONENT +
      " = " +
      Stock.StockComponent.TABLE_NAME +
      "." +
      Stock.StockComponent._ID +
      " WHERE " +
      Stock.StockBorrow.TABLE_NAME +
      "." +
      Stock.StockBorrow._ID +
      " = ?";

    Cursor cursor = db.rawQuery(
      query,
      new String[] { Long.toString(borrowId) }
    );

    if (cursor.moveToNext()) {
      borrow = initializeBorrowFromCursor(cursor);
    }
    cursor.close();

    return borrow;
  }

  public static void add(
    SQLiteDatabase db,
    long component,
    long member,
    int quantity
  ) {
    ContentValues values = new ContentValues();

    values.put(Stock.StockBorrow.COLUMN_NAME_COMPONENT, component);
    values.put(Stock.StockBorrow.COLUMN_NAME_MEMBER, member);
    values.put(Stock.StockBorrow.COLUMN_NAME_QUANTITY, quantity);
    values.put(
      Stock.StockBorrow.COLUMN_NAME_CREATE_DATE,
      new Date().toString()
    );

    db.insert(Stock.StockBorrow.TABLE_NAME, null, values);
  }

  public static void update(SQLiteDatabase db, long borrowId, int state) {
    ContentValues values = new ContentValues();
    values.put(Stock.StockBorrow.COLUMN_NAME_STATE, state);
    values.put(
      Stock.StockBorrow.COLUMN_NAME_RETURN_DATE,
      new Date().toString()
    );
    System.out.println(borrowId + " " + state);
    int count = db.update(
      Stock.StockBorrow.TABLE_NAME,
      values,
      Stock.StockBorrow._ID + " = ?",
      new String[] { Long.toString(borrowId) }
    );
    if (count > 0 && state > 0) {
      Borrow borrow = Borrow.findById(db, borrowId);
      Component.update(
        db,
        borrow.component.getId(),
        borrow.component.getQuantity() - borrow.quantity
      );
    }
  }
}
