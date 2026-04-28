package com.example.lostfoundapp;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ItemAdapter extends BaseAdapter {

    Context context;
    ArrayList<LostFoundItem> itemList;
    DatabaseHelper databaseHelper;
    Runnable refreshList;

    public ItemAdapter(Context context, ArrayList<LostFoundItem> itemList, Runnable refreshList) {
        this.context = context;
        this.itemList = itemList;
        this.refreshList = refreshList;
        this.databaseHelper = new DatabaseHelper(context);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return itemList.get(position).id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_lost_found, parent, false);
        }

        ImageView itemImage = convertView.findViewById(R.id.itemImage);
        TextView itemName = convertView.findViewById(R.id.itemName);
        TextView itemDetails = convertView.findViewById(R.id.itemDetails);
        TextView itemDate = convertView.findViewById(R.id.itemDate);
        Button btnDelete = convertView.findViewById(R.id.btnDelete);

        LostFoundItem item = itemList.get(position);

        itemName.setText(item.type + ": " + item.name);
        itemDetails.setText(item.category + " | " + item.location + "\n" + item.description);
        itemDate.setText("Posted: " + item.dateTime);

        try {
            itemImage.setImageURI(Uri.parse(item.image));
        } catch (Exception e) {
            itemImage.setImageResource(android.R.drawable.ic_menu_gallery);
        }

        btnDelete.setOnClickListener(v -> {
            boolean deleted = databaseHelper.deleteItem(item.id);

            if (deleted) {
                Toast.makeText(context, "Advert removed", Toast.LENGTH_SHORT).show();
                refreshList.run();
            } else {
                Toast.makeText(context, "Could not remove advert", Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}
