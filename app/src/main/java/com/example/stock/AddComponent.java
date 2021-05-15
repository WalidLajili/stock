package com.example.stock;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.example.stock.DB.Component;
import com.example.stock.DB.Stock;
import com.example.stock.DB.StockDbHelper;
import com.google.android.material.snackbar.Snackbar;
import java.util.Date;

public class AddComponent extends Add {

  long category = -1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_component);
    category = this.checkCategory();

    AddComponent self = this;

    findViewById(R.id.button_cancel)
      .setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            self.goBack();
          }
        }
      );
    EditText titleInput = findViewById(R.id.component_title);
    EditText quantityInput = findViewById(R.id.quantity);
    findViewById(R.id.button_add)
      .setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            String title = titleInput.getText().toString();
            String quantityText = quantityInput.getText().toString();
            int quantity = 0;
            if (!quantityText.isEmpty()) {
              quantity = Integer.parseInt(quantityText, 10);
            }

            if (!(title.isEmpty() || quantity < 0)) {
              hideSoftKeyBoard();
              Component.add(db, title, quantity, category);
              self.goBack();
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

  public long checkCategory() {
    long categoryId = getIntent().getLongExtra("category", -1);
    if (categoryId == -1) {
      Intent intent = new Intent(this, Home.class);
      startActivity((intent));
    }
    return categoryId;
  }

  public void goBack() {
    Intent intent = new Intent(this, ComponentActivity.class);
    intent.putExtra("category", category);
    startActivity(intent);
  }

  @Override
  public void goBack(String name) {
    throw new UnsupportedOperationException();
  }
}
