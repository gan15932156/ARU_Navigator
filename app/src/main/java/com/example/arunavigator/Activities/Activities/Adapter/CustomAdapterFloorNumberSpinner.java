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

import com.example.arunavigator.Activities.Activities.GetterSetter.Floor;
import com.example.arunavigator.R;

import java.util.List;

public class CustomAdapterFloorNumberSpinner extends ArrayAdapter<Floor> {
    private Activity context;
    private List<Floor> floors;

    public CustomAdapterFloorNumberSpinner(@NonNull Context context, List<Floor> floors) {
        super(context, R.layout.custom_spn_floor_number,floors);
        this.context = (Activity) context;
        this.floors = floors;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.custom_spn_floor_number,null,true);
        TextView name = listViewItem.findViewById(R.id.cus_txt_floor_number);
        Floor floor = floors.get(position);
        name.setText(floor.getFloor_number());
        return  listViewItem;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.custom_spn_floor_number,null,true);
        TextView name = listViewItem.findViewById(R.id.cus_txt_floor_number);
        Floor floor = floors.get(position);
        name.setText(floor.getFloor_number());
        return  listViewItem;
    }
}
