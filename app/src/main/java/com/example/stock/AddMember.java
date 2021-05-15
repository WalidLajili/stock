package com.example.stock;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.example.stock.DB.Member;
import com.example.stock.DB.Stock;
import com.example.stock.DB.StockDbHelper;
import com.google.android.material.snackbar.Snackbar;

public class AddMember extends Add {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    AddMember self = this;
    setContentView(R.layout.activity_add_member);
    findViewById(R.id.button_cancel)
      .setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            self.goBack("member");
          }
        }
      );
    EditText firstNameInput = findViewById(R.id.first_name);
    EditText lastNameInput = findViewById(R.id.last_name);
    EditText phoneInput = findViewById(R.id.phone);
    findViewById(R.id.button_add)
      .setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            String firstName = firstNameInput.getText().toString();
            String lastName = lastNameInput.getText().toString();
            String phone = phoneInput.getText().toString();

            if (
              !(firstName.isEmpty() || lastName.isEmpty() || phone.isEmpty())
            ) {
              hideSoftKeyBoard();
              Member.add(db, firstName, lastName, phone);
              self.goBack("member");
            } else {
              Snackbar
                .make(
                  view,
                  "Il faut remplir tous les champs",
                  Snackbar.LENGTH_LONG
                )
                .show();
            }
          }
        }
      );
  }
}
