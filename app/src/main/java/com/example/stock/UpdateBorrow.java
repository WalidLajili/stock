package com.example.stock;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Spinner;
import com.example.stock.DB.Borrow;
import com.example.stock.DB.StockDbHelper;

public class UpdateBorrow extends Add {

  private Borrow borrow;
  private StockDbHelper helper;
  private SQLiteDatabase db;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    UpdateBorrow self = this;
    helper = new StockDbHelper(this);
    db = helper.getReadableDatabase();
    borrow = Borrow.findById(db, getIntent().getLongExtra("borrow", -1));
    setTitle("Retourner " + borrow.getComponent().getTitle());
    setContentView(R.layout.activity_update_borrow);

    Spinner stateSelect = findViewById(R.id.state_select);

    findViewById(R.id.button_add)
      .setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            Borrow.update(
              db,
              borrow.getId(),
              stateSelect.getSelectedItemPosition()
            );
            self.goBack("borrow");
          }
        }
      );

    findViewById(R.id.button_cancel)
      .setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            self.goBack("borrow");
          }
        }
      );
  }
}
