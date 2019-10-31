package com.example.arunavigator.Activities.Activities.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.arunavigator.Activities.Activities.EditLocationActivity;
import com.example.arunavigator.Activities.Activities.GetterSetter.Location;
import com.example.arunavigator.Activities.Activities.MainActivity;
import com.example.arunavigator.R;
import com.google.android.material.textfield.TextInputEditText;

import org.w3c.dom.Text;

import java.io.Serializable;
import java.util.ArrayList;

import static com.example.arunavigator.R.layout.activity_new_location;
import static com.example.arunavigator.R.layout.dialog_main_show_activity;

public class ManageLocationAdapter extends RecyclerView.Adapter<ManageLocationAdapter.ManageLocationViewHolder> {
    private Context mContext;
    private ArrayList<Location> mLocationList;
    private Uri uri;

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
    public void onBindViewHolder(@NonNull final ManageLocationViewHolder holder, int position) {
        final Location lt = mLocationList.get(position);
        holder.manage_location_name_txt.setText(lt.getLocation_name());
        Glide.with(mContext)
                .load(lt.getImage_path())
                .fitCenter()
                .into(holder.manage_location_img);
        holder.manage_location_relative.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(mContext, EditLocationActivity.class);
                intent.putExtra("id",lt.getLocation_id());
                intent.putExtra("name",lt.getLocation_name());
                intent.putExtra("des",lt.getLocation_des());
                intent.putExtra("pic",lt.getImage_path());
                intent.putExtra("lat",lt.getLat());
                intent.putExtra("long",lt.getLongti());
                intent.putExtra("type",lt.getType());
                mContext.startActivity(intent);
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
