package com.example.stock.ui.category;

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

import com.example.stock.AddCategory;
import com.example.stock.ComponentActivity;
import com.example.stock.DB.Category;
import com.example.stock.DB.Stock;
import com.example.stock.DB.StockDbHelper;
import com.example.stock.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;
import java.util.List;

public class CategoryFragment extends Fragment {

    ArrayList<Category> categories = new ArrayList<Category>();

    private class CategoryAdapter extends ArrayAdapter<Category> {
        private int resourceLayout;
        private Context mContext;

        public CategoryAdapter(Context context, int resource, List<Category> items) {
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

            Category category = getItem(position);

            if (category != null) {
                TextView title = v.findViewById(R.id.category_title);

                if (title != null) {
                    title.setText(category.getTitle());
                }

            }

            return v;
        }


    }


    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        StockDbHelper helper = new StockDbHelper(getContext());
        SQLiteDatabase db = helper.getReadableDatabase();
        categories = Category.find(db);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_category, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FloatingActionButton fab = view.findViewById(R.id.add);
        fab.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getActivity(), AddCategory.class);
                        startActivity(intent);
                    }
                }
        );
        CategoryAdapter adapter = new CategoryAdapter(getContext(),
                R.layout.category_item, categories);
        ListView categoriesListView = view.findViewById(R.id.categories);
        categoriesListView.setClickable(false);
        categoriesListView.setDivider(null);
        categoriesListView.setAdapter(adapter);

        if(categories.size() == 0) {
            TextView empty = view.findViewById(R.id.empty);
            empty.setText("Aucun category trouvé, cliquez sur le bouton Ajouter pour créer votre premier!");
        }

        categoriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                Intent intent = new Intent(getActivity(), ComponentActivity.class);
                Category category = categories.get(position);
                intent.putExtra("category", category.getId());
                startActivity(intent);
            }
        });
    }
}
