package com.example.stock;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.example.stock.DB.Category;
import com.google.android.material.snackbar.Snackbar;

public class AddCategory extends Add {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    AddCategory self = this;
    setContentView(R.layout.activity_add_category);
    EditText editTitle = findViewById(R.id.title);

    findViewById(R.id.button_add)
      .setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            String title = editTitle.getText().toString();

            if (!title.isEmpty()) {
              hideSoftKeyBoard();
              Category.add(db, title);
              self.goBack("family");
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

    findViewById(R.id.button_cancel)
      .setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            self.goBack("family");
          }
        }
      );
  }
}
