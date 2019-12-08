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
import com.example.arunavigator.Activities.Activities.EditLocationActivity;
import com.example.arunavigator.Activities.Activities.EditToiletActivity;
import com.example.arunavigator.Activities.Activities.GetterSetter.Location;
import com.example.arunavigator.Activities.Activities.GetterSetter.Toilet;
import com.example.arunavigator.R;

import java.util.ArrayList;

public class ManageToiletAdapter extends RecyclerView.Adapter<ManageToiletAdapter.ManageToiletViewHolder> {
    private Context mContext;
    private ArrayList<Toilet> mToiletList;

    public ManageToiletAdapter(Context mContext, ArrayList<Toilet> mToiletList) {
        this.mContext = mContext;
        this.mToiletList = mToiletList;
    }


    @NonNull
    @Override
    public ManageToiletViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_manage_toilet,parent,false);
        return new ManageToiletAdapter.ManageToiletViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageToiletViewHolder holder, int position) {
        final Toilet t = mToiletList.get(position);
        holder.manage_toilet_name_txt.setText(t.getToilet_name());
        holder.manage_toilet_relative.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(mContext, EditToiletActivity.class);
                intent.putExtra("toilet_id",t.getToilet_id());
                intent.putExtra("floor_id",t.getFloor_id());
                intent.putExtra("toilet_name",t.getToilet_name());
                intent.putExtra("type",t.getToilet_type());
                intent.putExtra("pic",t.getToilet_map_pic());
                mContext.startActivity(intent);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mToiletList.size();
    }

    public class ManageToiletViewHolder extends RecyclerView.ViewHolder {
        public TextView manage_toilet_name_txt;
        public RelativeLayout manage_toilet_relative;
        public ManageToiletViewHolder(@NonNull View itemView) {
            super(itemView);
            manage_toilet_relative = itemView.findViewById(R.id.relative_recyclerview_manage_toilet);
            manage_toilet_name_txt = itemView.findViewById(R.id.recycler_txt_manage_toilet_name);
        }
    }
}
