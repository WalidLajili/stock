package com.example.stock.ui.borrows;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.stock.AddBorrow;
import com.example.stock.AddCategory;
import com.example.stock.ComponentActivity;
import com.example.stock.DB.Borrow;
import com.example.stock.DB.Category;
import com.example.stock.DB.Stock;
import com.example.stock.DB.StockDbHelper;
import com.example.stock.R;
import com.example.stock.UpdateBorrow;
import com.example.stock.ui.category.CategoryFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class BorrowFragment extends Fragment {

    ArrayList<Borrow> borrows;

    private class BorrowAdapter extends ArrayAdapter<Borrow> {
        private int resourceLayout;
        private Context mContext;

        public BorrowAdapter(Context context, int resource, List<Borrow> items) {
            super(context, resource, items);
            this.resourceLayout = resource;
            this.mContext = context;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                LayoutInflater vi;
                vi = LayoutInflater.from(mContext);
                v = vi.inflate(resourceLayout, null);
            }

            Borrow borrow = getItem(position);
            if (borrow != null) {
                TextView component = v.findViewById(R.id.borrow_component);
                if (component != null) {
                    component.setText("Component: "  + borrow.getComponent().getTitle());
                }
                TextView member = v.findViewById(R.id.borrow_member);
                if(member != null) {
                    member.setText("Member: "  + borrow.getMember().getFirstName() + " " + borrow.getMember().getLastName());
                }

                TextView quantity = v.findViewById(R.id.borrow_quantity);
                if(quantity != null) {
                    quantity.setText("Quantité: "  + borrow.getQuantity());
                }

                TextView date = v.findViewById(R.id.borrow_date);
                if(date != null) {
                    DateFormat dateFormat = new SimpleDateFormat("dd MMMM YYYY hh:mm");
                  String returnText = "Date: " + dateFormat.format(borrow.getCreatedAt());
                  if(borrow.isReturned()) {
                      returnText += " - " + dateFormat.format(borrow.getReturnedAt());
                  }
                  date.setText(returnText);
                }
                TextView status = v.findViewById(R.id.borrow_status);
                if(status != null){
                    if(borrow.isReturned()) {
                        status.setText("Etat: " + getResources().getStringArray(R.array.borrow_state)[borrow.getState()]);
                    } else {
                        status.setVisibility(View.GONE) ;

                    }
                }


            }

            return v;
        }


    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        StockDbHelper helper = new StockDbHelper(getContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        borrows = Borrow.find(db);
        View root = inflater.inflate(R.layout.fragment_borrows, container, false);
        return root;
    }


    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton fab = view.findViewById(R.id.add);
        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), AddBorrow.class);
                        startActivity(intent);
                    }
                }
        );

        BorrowFragment.BorrowAdapter adapter = new BorrowFragment.BorrowAdapter (getContext(),
                R.layout.borrow_item, borrows);
        ListView borrowsListView = view.findViewById(R.id.borrows);
        borrowsListView.setClickable(false);
        borrowsListView.setDivider(null);
        borrowsListView.setAdapter(adapter);

        if(borrows.size() == 0) {
            TextView empty = view.findViewById(R.id.empty);
            empty.setText("Aucun emprunt trouvé, cliquez sur le bouton Ajouter pour créer votre premier!");
        }

        borrowsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Borrow borrow = borrows.get(position);
                if(!borrow.isReturned()) {
                    Intent intent = new Intent(getActivity(), UpdateBorrow.class);
                    intent.putExtra("borrow", borrow.getId());
                    startActivity(intent);
                }
            }
        });

    }
}