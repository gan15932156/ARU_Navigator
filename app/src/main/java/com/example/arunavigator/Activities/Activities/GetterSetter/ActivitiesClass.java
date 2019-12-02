package com.example.arunavigator.Activities.Activities.GetterSetter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class ActivitiesClass {
    private String ac_des,ad_id,ac_image,ac_name,ac_place_name,ac_lat,ac_long;
    public ActivitiesClass(){}

    public ActivitiesClass(String ac_des, String ad_id, String ac_image, String ac_name, String ac_place_name, String ac_lat, String ac_long) {
        this.ac_des = ac_des;
        this.ad_id = ad_id;
        this.ac_image = ac_image;
        this.ac_name = ac_name;
        this.ac_place_name = ac_place_name;
        this.ac_lat = ac_lat;
        this.ac_long = ac_long;
    }

    public String getAc_des() {
        return ac_des;
    }

    public void setAc_des(String ac_des) {
        this.ac_des = ac_des;
    }

    public String getAd_id() {
        return ad_id;
    }

    public void setAd_id(String ad_id) {
        this.ad_id = ad_id;
    }

    public String getAc_image() {
        return ac_image;
    }

    public void setAc_image(String ac_image) {
        this.ac_image = ac_image;
    }

    public String getAc_name() {
        return ac_name;
    }

    public void setAc_name(String ac_name) {
        this.ac_name = ac_name;
    }

    public String getAc_place_name() {
        return ac_place_name;
    }

    public void setAc_place_name(String ac_place_name) {
        this.ac_place_name = ac_place_name;
    }

    public String getAc_lat() {
        return ac_lat;
    }

    public void setAc_lat(String ac_lat) {
        this.ac_lat = ac_lat;
    }

    public String getAc_long() {
        return ac_long;
    }

    public void setAc_long(String ac_long) {
        this.ac_long = ac_long;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof ActivitiesClass){ ActivitiesClass l = (ActivitiesClass ) obj;
            if(l.getAc_name().equals(ac_name) && l.getAd_id()==ad_id ) return true;
        }
        return false;
    }
}
