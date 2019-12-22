package com.example.arunavigator.Activities.Activities.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.arunavigator.Activities.Activities.EditSpDesActivity;
import com.example.arunavigator.Activities.Activities.GetterSetter.SpacificDes;
import com.example.arunavigator.R;

import java.util.ArrayList;

public class ManageSpDesAdapter extends RecyclerView.Adapter<ManageSpDesAdapter.ManageSpDesViewHolder>{
    private Context mContext;
    private ArrayList<SpacificDes> mSpDesList;

    public ManageSpDesAdapter(Context mContext, ArrayList<SpacificDes> mSpDesList) {
        this.mContext = mContext;
        this.mSpDesList = mSpDesList;
    }

    @NonNull
    @Override
    public ManageSpDesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.recyclerview_manage_sp_des,parent,false);
        return new ManageSpDesAdapter.ManageSpDesViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ManageSpDesViewHolder holder, int position) {
        final SpacificDes spacificDes = mSpDesList.get(position);
        holder.manage_sp_des_txt.setText(spacificDes.getSp_des());
        holder.manage_sp_des_relative.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(mContext, EditSpDesActivity.class);
                intent.putExtra("id",spacificDes.getSp_id());
                intent.putExtra("des",spacificDes.getSp_des());
                intent.putExtra("location_id",spacificDes.getLocation_id());
                mContext.startActivity(intent);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSpDesList.size();
    }

    public class ManageSpDesViewHolder extends RecyclerView.ViewHolder{
        public TextView manage_sp_des_txt;
        public RelativeLayout manage_sp_des_relative;
        public ManageSpDesViewHolder(@NonNull View itemView) {
            super(itemView);
            manage_sp_des_relative = itemView.findViewById(R.id.relative_recyclerview_manage_sp_des);
            manage_sp_des_txt = itemView.findViewById(R.id.recycler_txt_manage_sp_des);
        }
    }
}
