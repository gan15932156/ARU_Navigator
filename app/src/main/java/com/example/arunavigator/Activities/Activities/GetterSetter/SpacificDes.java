package com.example.arunavigator.Activities.Activities.GetterSetter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SpacificDes {
    private String sp_id,location_id,sp_des;

    public SpacificDes(){}

    public SpacificDes(String sp_id, String location_id, String sp_des) {
        this.sp_id = sp_id;
        this.location_id = location_id;
        this.sp_des = sp_des;
    }

    public String getSp_id() {
        return sp_id;
    }

    public void setSp_id(String sp_id) {
        this.sp_id = sp_id;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getSp_des() {
        return sp_des;
    }

    public void setSp_des(String sp_des) {
        this.sp_des = sp_des;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof SpacificDes){ SpacificDes l = (SpacificDes ) obj;
            if(l.getSp_des().equals(sp_des) && l.getSp_id()==sp_id ) return true;
        }
        return false;
    }
}
