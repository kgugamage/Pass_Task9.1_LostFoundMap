package com.example.lostfoundapp;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ItemListActivity extends AppCompatActivity {

    Spinner spinnerFilter;
    ListView listViewItems;
    Button btnBackList;
    DatabaseHelper databaseHelper;
    ArrayList<LostFoundItem> itemList;

    String[] filterCategories = {
            "All", "Electronics", "Pets", "Wallets", "Keys", "Bags", "Documents", "Other"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        spinnerFilter = findViewById(R.id.spinnerFilter);
        listViewItems = findViewById(R.id.listViewItems);
        btnBackList = findViewById(R.id.btnBackList);
        databaseHelper = new DatabaseHelper(this);

        btnBackList.setOnClickListener(v -> finish());

        ArrayAdapter<String> filterAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                filterCategories
        );

        spinnerFilter.setAdapter(filterAdapter);

        loadItems("All");

        spinnerFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                loadItems(filterCategories[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void loadItems(String category) {
        itemList = new ArrayList<>();
        Cursor cursor;

        if (category.equals("All")) {
            cursor = databaseHelper.getAllItems();
        } else {
            cursor = databaseHelper.getItemsByCategory(category);
        }

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_ID));
                String type = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TYPE));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME));
                String itemCategory = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_CATEGORY));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DESCRIPTION));
                String location = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_LOCATION));
                String image = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_IMAGE));
                String dateTime = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_DATE));

                itemList.add(new LostFoundItem(
                        id,
                        type,
                        name,
                        itemCategory,
                        description,
                        location,
                        image,
                        dateTime
                ));

            } while (cursor.moveToNext());
        }

        cursor.close();

        ItemAdapter adapter = new ItemAdapter(
                this,
                itemList,
                () -> loadItems(spinnerFilter.getSelectedItem().toString())
        );

        listViewItems.setAdapter(adapter);
    }
}
