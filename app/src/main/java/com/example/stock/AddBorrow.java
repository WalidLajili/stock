package com.example.stock;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import com.example.stock.DB.Borrow;
import com.example.stock.DB.Component;
import com.example.stock.DB.Member;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;

public class AddBorrow extends Add {

  private ArrayList<Component> components = new ArrayList();
  private ArrayList<Member> members = new ArrayList();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    components = Component.find(db, null, null);
    members = Member.find(db);
    AddBorrow self = this;
    setContentView(R.layout.activity_add_borrow);

    EditText quantityInput = findViewById(R.id.quantity);
    Spinner membersInput = findViewById(R.id.member_select);

    ArrayList<String> membersOptions = new ArrayList();

    for (int i = 0; i < members.size(); i++) {
      Member member = members.get(i);
      membersOptions.add(member.getFirstName() + " " + member.getLastName());
    }

    membersInput.setAdapter(
      new ArrayAdapter(
        this,
        android.R.layout.simple_spinner_dropdown_item,
        membersOptions
      )
    );

    ArrayList<String> componentsOptions = new ArrayList();

    for (int i = 0; i < components.size(); i++) {
      Component component = components.get(i);
      componentsOptions.add(component.getTitle());
    }

    AutoCompleteTextView componentsInput = findViewById(R.id.component_select);

    componentsInput.setAdapter(
      new ArrayAdapter(
        this,
        android.R.layout.simple_spinner_dropdown_item,
        componentsOptions
      )
    );

    componentsInput.setOnTouchListener(
      new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
          componentsInput.showDropDown();
          return false;
        }
      }
    );

    findViewById(R.id.button_add)
      .setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View view) {
            String quantityText = quantityInput.getText().toString();
            int quantity = 0;
            if (!quantityText.isEmpty()) {
              quantity = Integer.parseInt(quantityText, 10);
            }
            int memberPosition = membersInput.getSelectedItemPosition();
            int componentPosition = -1;
            String componentText = componentsInput.getText().toString();
            for (int i = 0; i < componentsOptions.size(); i++) {
              if (componentText.equals(componentsOptions.get(i))) {
                componentPosition = i;
                break;
              }
            }

            if (quantity > 0 && memberPosition > -1 && componentPosition > -1) {
              hideSoftKeyBoard();
              Component component = components.get(componentPosition);
              int availableQuantity = component.findAvailableQuantity(db);
              if (quantity > availableQuantity) {
                Snackbar
                  .make(
                    view,
                    "La quantité demandée n'est pas disponible pour le moment (la quantité au stock est" +
                    availableQuantity +
                    " )",
                    Snackbar.LENGTH_LONG
                  )
                  .show();
              } else {
                Borrow.add(
                  db,
                  components.get(componentPosition).getId(),
                  members.get(memberPosition).getId(),
                  quantity
                );
                self.goBack("borrow");
              }
            } else if (quantity == 0 || memberPosition == -1) {
              Snackbar
                .make(
                  view,
                  "Il faut remplir tous les champs",
                  Snackbar.LENGTH_LONG
                )
                .show();
            } else {
              Snackbar
                .make(view, "Composant introuvable", Snackbar.LENGTH_LONG)
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
            self.goBack("borrow");
          }
        }
      );
  }
}
