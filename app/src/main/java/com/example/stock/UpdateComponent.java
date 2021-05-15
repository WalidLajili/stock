package com.example.stock;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.example.stock.DB.Component;
import com.google.android.material.snackbar.Snackbar;

public class UpdateComponent extends Add {

  private Component component;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    component =
      Component.findById(db, getIntent().getLongExtra("component", -1));
    setContentView(R.layout.activity_update_component);
    UpdateComponent self = this;

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
    titleInput.setText(component.getTitle());
    titleInput.setInputType(InputType.TYPE_NULL);
    EditText quantityInput = findViewById(R.id.quantity);
    quantityInput.setText(Integer.toString(component.getQuantity()));
    findViewById(R.id.button_add)
      .setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            hideSoftKeyBoard();
            int quantity = 0;
            String quantityText = quantityInput.getText().toString();
            if (!quantityText.isEmpty()) {
              quantity = Integer.parseInt(quantityText, 10);
            }

            int borrowedQuantity = component.findBorrowedQuantity(db);

            if (quantity >= 0 && quantity >= borrowedQuantity) {
              Component.update(db, component.getId(), quantity);
              self.goBack();
            } else if (quantity < borrowedQuantity) {
              Snackbar
                .make(
                  view,
                  "La quantité est inférieure à la quantité empruntée",
                  Snackbar.LENGTH_LONG
                )
                .show();
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

  public void goBack() {
    Intent intent = new Intent(this, ComponentActivity.class);
    intent.putExtra("category", getIntent().getLongExtra("category", -1));
    startActivity(intent);
  }

  @Override
  public void goBack(String name) {
    throw new UnsupportedOperationException();
  }
}
