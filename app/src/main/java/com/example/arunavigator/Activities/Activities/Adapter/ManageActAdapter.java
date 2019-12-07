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
import com.example.arunavigator.Activities.Activities.EditActActivity;
import com.example.arunavigator.Activities.Activities.GetterSetter.ActivitiesClass;
import com.example.arunavigator.R;

import java.util.ArrayList;

public class ManageActAdapter extends RecyclerView.Adapter<ManageActAdapter.ManageActViewHolder>{
    private Context mContext;
    private ArrayList<ActivitiesClass> mActList;

    public ManageActAdapter(Context mContext, ArrayList<ActivitiesClass> mActList) {
        this.mContext = mContext;
        this.mActList = mActList;
    }

    @NonNull
    @Override
    public ManageActViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_manage_act,parent,false);
        return new ManageActAdapter.ManageActViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageActViewHolder holder, int position) {
        final ActivitiesClass activitiesClass = mActList.get(position);
        holder.manage_act_name_txt.setText(activitiesClass.getAc_name());
        Glide.with(mContext)
                .load(activitiesClass.getAc_image())
                .fitCenter()
                .into(holder.manage_act_img);
        holder.manage_act_relative.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(mContext, EditActActivity.class);
                intent.putExtra("id",activitiesClass.getAd_id());
                intent.putExtra("name",activitiesClass.getAc_name());
                intent.putExtra("des",activitiesClass.getAc_des());
                intent.putExtra("pic",activitiesClass.getAc_image());
                intent.putExtra("lat",activitiesClass.getLat());
                intent.putExtra("long",activitiesClass.getMlong());
                intent.putExtra("place_name",activitiesClass.getAc_place_name());
                mContext.startActivity(intent);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mActList.size();
    }

    public class ManageActViewHolder extends RecyclerView.ViewHolder{
        public ImageView manage_act_img;
        public TextView manage_act_name_txt;
        public RelativeLayout manage_act_relative;
        public ManageActViewHolder(@NonNull View itemView) {
            super(itemView);
            manage_act_relative = itemView.findViewById(R.id.relative_recyclerview_manage_act);
            manage_act_img = itemView.findViewById(R.id.recycler_img_manage_act);
            manage_act_name_txt = itemView.findViewById(R.id.recycler_txt_manage_act_name);
        }
    }
}
