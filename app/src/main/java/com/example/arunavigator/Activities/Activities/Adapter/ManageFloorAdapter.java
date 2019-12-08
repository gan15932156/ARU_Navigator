package com.example.arunavigator.Activities.Activities.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.arunavigator.Activities.Activities.EditFloorActivity;
import com.example.arunavigator.Activities.Activities.EditLocationActivity;
import com.example.arunavigator.Activities.Activities.GetterSetter.Floor;
import com.example.arunavigator.Activities.Activities.GetterSetter.Location;
import com.example.arunavigator.R;

import java.util.ArrayList;

public class ManageFloorAdapter extends RecyclerView.Adapter<ManageFloorAdapter.ManageFloorHolder> {
    private Context mContext;
    private ArrayList<Floor> mFloorList;

    public ManageFloorAdapter(Context mContext, ArrayList<Floor> mFloorList) {
        this.mContext = mContext;
        this.mFloorList = mFloorList;
    }

    @NonNull
    @Override
    public ManageFloorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_manage_floor,parent,false);
        return new ManageFloorAdapter.ManageFloorHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageFloorHolder holder, int position) {
        final Floor ft = mFloorList.get(position);
        holder.manage_floor_name_txt.setText(ft.getFloor_number());
        holder.manage_floor_relative.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(mContext, EditFloorActivity.class);
                intent.putExtra("floor_id",ft.getFloor_id());
                intent.putExtra("location_id",ft.getLocation_id());
                intent.putExtra("floor_number",ft.getFloor_number());
                mContext.startActivity(intent);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() { return mFloorList.size(); }

    public class ManageFloorHolder extends RecyclerView.ViewHolder{
        public TextView manage_floor_name_txt;
        public RelativeLayout manage_floor_relative;
        public ManageFloorHolder(@NonNull View itemView) {
            super(itemView);
            manage_floor_name_txt = itemView.findViewById(R.id.recycler_txt_manage_floor_name);
            manage_floor_relative = itemView.findViewById(R.id.relative_recyclerview_manage_floor);
        }
    }
}
