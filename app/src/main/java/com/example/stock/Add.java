package com.example.stock;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import androidx.appcompat.app.AppCompatActivity;
import com.example.stock.DB.StockDbHelper;

public class Add extends AppCompatActivity {

  StockDbHelper dbHelper;
  SQLiteDatabase db;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    dbHelper = new StockDbHelper(this);
    db = dbHelper.getWritableDatabase();
  }

  public void goBack(String from) {
    Intent intent = new Intent(this, Home.class);
    intent.putExtra("from", from);
    startActivity(intent);
  }

  public void hideSoftKeyBoard() {
    InputMethodManager imm = (InputMethodManager) getSystemService(
      INPUT_METHOD_SERVICE
    );

    if (imm.isAcceptingText()) { // verify if the soft keyboard is open
      imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
  }
}
