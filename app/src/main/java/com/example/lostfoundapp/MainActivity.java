package com.example.lostfoundapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnAddItem, btnViewItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnAddItem = findViewById(R.id.btnAddItem);
        btnViewItems = findViewById(R.id.btnViewItems);

        btnAddItem.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, AddItemActivity.class);
            startActivity(intent);
        });

        btnViewItems.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ItemListActivity.class);
            startActivity(intent);
        });
    }
}
