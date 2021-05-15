package com.example.stock;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.stock.DB.Category;
import com.example.stock.DB.Component;
import com.example.stock.DB.Stock;
import com.example.stock.DB.StockDbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ComponentActivity extends AppCompatActivity {

  private Category category;

  private ArrayList<Component> components = new ArrayList<Component>();
  private StockDbHelper helper;
  private SQLiteDatabase db;

  private class ComponentAdapter extends ArrayAdapter<Component> {

    private int resourceLayout;
    private Context mContext;

    public ComponentAdapter(
      Context context,
      int resource,
      List<Component> items
    ) {
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

      Component component = getItem(position);
      if (component != null) {
        TextView title = v.findViewById(R.id.component_title);

        if (title != null) {
          title.setText(component.getTitle());
        }
        TextView quantity = v.findViewById(R.id.component_quantity);

        if (quantity != null) {
          quantity.setText(Integer.toString(component.getQuantity()));
        }

        TextView createdAt = v.findViewById(R.id.component_date);

        if (createdAt != null) {
          DateFormat dateFormat = new SimpleDateFormat("dd MMMM YYYY hh:mm");
          String date = dateFormat.format(component.getCreatedAt());
          createdAt.setText(date);
        }
      }

      return v;
    }
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    helper = new StockDbHelper(this);
    db = helper.getReadableDatabase();
    ComponentActivity self = this;
    long categoryId = getIntent().getLongExtra("category", -1);
    category = Category.findById(db, categoryId);
    components =
      Component.find(
        db,
        Stock.StockCategory._ID + " = ?",
        new String[] { Long.toString(category.getId()) }
      );
    setContentView(R.layout.activity_component);
    setTitle(category.getTitle());

    FloatingActionButton fab = findViewById(R.id.add);
    fab.setOnClickListener(
      new View.OnClickListener() {
        @Override
        public void onClick(View view) {
          Intent intent = new Intent(self, AddComponent.class);
          intent.putExtra("category", category.getId());
          startActivity(intent);
        }
      }
    );

    ComponentActivity.ComponentAdapter adapter = new ComponentActivity.ComponentAdapter(
      this,
      R.layout.component_item,
      components
    );
    ListView componentsListView = findViewById(R.id.components);
    componentsListView.setClickable(false);
    componentsListView.setDivider(null);
    componentsListView.setAdapter(adapter);

    if(components.size() == 0) {
      TextView empty = findViewById(R.id.empty);
      empty.setText("Aucun composant trouvé dans cette catégorie, cliquez sur le bouton Ajouter pour créer le premier!");
    }

    componentsListView.setOnItemClickListener(
      new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(
          AdapterView<?> parent,
          View view,
          int position,
          long id
        ) {
          Intent intent = new Intent(self, UpdateComponent.class);
          Component component = components.get(position);
          intent.putExtra("component", component.getId());
          intent.putExtra("category", category.getId());
          startActivity(intent);
        }
      }
    );
  }
}
