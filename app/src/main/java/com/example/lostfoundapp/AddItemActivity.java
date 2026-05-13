package com.example.lostfoundapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class AddItemActivity extends AppCompatActivity {

    EditText editItemName, editDescription, editLocation;
    Spinner spinnerCategory;
    Button btnChooseImage, btnSaveItem, btnBack, btnGetLocation;
    ImageView imagePreview;
    RadioButton radioLost, radioFound;

    DatabaseHelper databaseHelper;
    String imageUriString = "";

    double latitude = 0;
    double longitude = 0;

    String[] categories = {"Electronics", "Pets", "Wallets", "Keys", "Bags", "Documents", "Other"};

    ActivityResultLauncher<String> imagePickerLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        databaseHelper = new DatabaseHelper(this);

        editItemName = findViewById(R.id.editItemName);
        editDescription = findViewById(R.id.editDescription);
        editLocation = findViewById(R.id.editLocation);
        spinnerCategory = findViewById(R.id.spinnerCategory);
        btnChooseImage = findViewById(R.id.btnChooseImage);
        btnSaveItem = findViewById(R.id.btnSaveItem);
        btnBack = findViewById(R.id.btnBack);
        btnGetLocation = findViewById(R.id.btnGetLocation);
        imagePreview = findViewById(R.id.imagePreview);
        radioLost = findViewById(R.id.radioLost);
        radioFound = findViewById(R.id.radioFound);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                categories
        );
        spinnerCategory.setAdapter(adapter);

        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.GetContent(),
                uri -> {
                    if (uri != null) {
                        imageUriString = uri.toString();
                        imagePreview.setImageURI(uri);
                    }
                }
        );

        btnChooseImage.setOnClickListener(v -> imagePickerLauncher.launch("image/*"));

        btnBack.setOnClickListener(v -> finish());

        btnGetLocation.setOnClickListener(v -> {
            latitude = -38.1499;
            longitude = 144.3617;
            editLocation.setText("Waurn Ponds, Geelong");
            Toast.makeText(this, "Current location added", Toast.LENGTH_SHORT).show();
        });

        btnSaveItem.setOnClickListener(v -> saveItem());
    }

    private void saveItem() {
        String type = radioLost.isChecked() ? "Lost" : "Found";
        String name = editItemName.getText().toString().trim();
        String category = spinnerCategory.getSelectedItem().toString();
        String description = editDescription.getText().toString().trim();
        String location = editLocation.getText().toString().trim();

        if (name.isEmpty() || description.isEmpty() || location.isEmpty()) {
            Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show();
            return;
        }

        if (imageUriString.isEmpty()) {
            Toast.makeText(this, "Please upload an image", Toast.LENGTH_SHORT).show();
            return;
        }

        if (latitude == 0 || longitude == 0) {
            latitude = -38.1499;
            longitude = 144.3617;
        }

        boolean inserted = databaseHelper.insertItem(
                type,
                name,
                category,
                description,
                location,
                latitude,
                longitude,
                imageUriString
        );

        if (inserted) {
            Toast.makeText(this, "Advert saved", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, ItemListActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
        }
    }
}
