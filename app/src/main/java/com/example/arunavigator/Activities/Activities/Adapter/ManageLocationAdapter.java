package com.example.arunavigator.Activities.Activities.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.arunavigator.Activities.Activities.GetterSetter.Location;
import com.example.arunavigator.R;

import java.util.ArrayList;

public class ManageLocationAdapter extends RecyclerView.Adapter<ManageLocationAdapter.ManageLocationViewHolder> {
    private Context mContext;
    private ArrayList<Location> mLocationList;

    public ManageLocationAdapter(Context mContext, ArrayList<Location> mLocationList) {
        this.mContext = mContext;
        this.mLocationList = mLocationList;
    }

    @NonNull
    @Override
    public ManageLocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_manage_location,parent,false);
        return new ManageLocationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageLocationViewHolder holder, int position) {
        final Location lt = mLocationList.get(position);
        holder.manage_location_name_txt.setText(lt.getLocation_name());
        Glide.with(mContext)
                .load(lt.getImage_path())
                .fitCenter()
                .into(holder.manage_location_img);
        holder.manage_location_relative.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(mContext, lt.getLocation_name(), Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mLocationList.size();
    }

    public class ManageLocationViewHolder extends RecyclerView.ViewHolder{
        public ImageView manage_location_img;
        public TextView manage_location_name_txt;
        public RelativeLayout manage_location_relative;
        public ManageLocationViewHolder(@NonNull View itemView) {
            super(itemView);
            manage_location_relative = itemView.findViewById(R.id.relative_recyclerview_manage_location);
            manage_location_img = itemView.findViewById(R.id.recycler_img_manage_location);
            manage_location_name_txt = itemView.findViewById(R.id.recycler_txt_manage_location_name);
        }
    }
}
