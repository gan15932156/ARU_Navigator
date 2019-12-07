package com.example.arunavigator.Activities.Activities.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.arunavigator.Activities.Activities.GetterSetter.Location;
import com.example.arunavigator.R;

import java.util.List;

public class CustomAdapterFloorSpinner extends ArrayAdapter<Location> {
    private Activity context;
    private List<Location> locations;

    public CustomAdapterFloorSpinner(@NonNull Context context,List<Location> locations) {
        super(context, R.layout.custom_spn_floor,locations);
        this.context = (Activity) context;
        this.locations = locations;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.custom_spn_floor,null,true);
        TextView name = listViewItem.findViewById(R.id.cus_txt_floor_name);
        Location location = locations.get(position);
        name.setText(location.getLocation_name());
        return  listViewItem;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.custom_spn_floor,null,true);
        TextView name = listViewItem.findViewById(R.id.cus_txt_floor_name);
        Location location = locations.get(position);
        name.setText(location.getLocation_name());
        return  listViewItem;
    }
}
