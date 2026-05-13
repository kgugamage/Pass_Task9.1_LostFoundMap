package com.example.lostfoundapp;

import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MapsActivity extends AppCompatActivity {

    WebView mapWebView;
    EditText editRadius;
    Button btnApplyRadius, btnBackMap;
    DatabaseHelper databaseHelper;

    double userLat = -38.1499;
    double userLng = 144.3617;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mapWebView = findViewById(R.id.mapWebView);
        editRadius = findViewById(R.id.editRadius);
        btnApplyRadius = findViewById(R.id.btnApplyRadius);
        btnBackMap = findViewById(R.id.btnBackMap);

        btnBackMap.setOnClickListener(v -> finish());

        databaseHelper = new DatabaseHelper(this);

        WebSettings settings = mapWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        showMap(0);

        btnApplyRadius.setOnClickListener(v -> {
            String radiusText = editRadius.getText().toString().trim();

            if (radiusText.isEmpty()) {
                showMap(0);
                return;
            }

            try {
                double radius = Double.parseDouble(radiusText);
                showMap(radius);
            } catch (Exception e) {
                Toast.makeText(this, "Enter valid radius", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showMap(double radiusKm) {
        Cursor cursor = databaseHelper.getAllItems();
        StringBuilder markers = new StringBuilder();

        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_NAME));
            String type = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_TYPE));
            String itemLocation = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_LOCATION));

            double lat = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_LATITUDE));
            double lng = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COL_LONGITUDE));

            if (lat == 0 || lng == 0) {
                continue;
            }

            if (radiusKm > 0) {
                float[] results = new float[1];
                Location.distanceBetween(userLat, userLng, lat, lng, results);
                double distanceKm = results[0] / 1000.0;

                if (distanceKm > radiusKm) {
                    continue;
                }
            }

            String popupText = type + ": " + name + "<br>" + itemLocation;
            popupText = popupText.replace("'", "");

            markers.append("L.marker([")
                    .append(lat)
                    .append(",")
                    .append(lng)
                    .append("]).addTo(map).bindPopup('")
                    .append(popupText)
                    .append("');");
        }

        cursor.close();

        String html =
                "<html>" +
                        "<head>" +
                        "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                        "<link rel='stylesheet' href='https://unpkg.com/leaflet@1.9.4/dist/leaflet.css' />" +
                        "<script src='https://unpkg.com/leaflet@1.9.4/dist/leaflet.js'></script>" +
                        "</head>" +
                        "<body style='margin:0'>" +
                        "<div id='map' style='width:100%; height:100%;'></div>" +
                        "<script>" +
                        "var map = L.map('map').setView([" + userLat + "," + userLng + "], 13);" +
                        "L.tileLayer('https://{s}.basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}{r}.png', {maxZoom: 19}).addTo(map);" +
                        "L.marker([" + userLat + "," + userLng + "]).addTo(map).bindPopup('My area');" +
                        markers +
                        "</script>" +
                        "</body>" +
                        "</html>";

        mapWebView.loadDataWithBaseURL(
                "https://carto.com",
                html,
                "text/html",
                "UTF-8",
                null
        );
    }
}
