package com.example.arunavigator.Activities.Activities.GetterSetter;

import androidx.annotation.NonNull;

public class Toilet {
    private String toilet_id,floor_id,toilet_name,toilet_map_pic,toilet_type;
    public Toilet(){}

    public Toilet(String toilet_id, String floor_id, String toilet_name, String toilet_map_pic, String toilet_type) {
        this.toilet_id = toilet_id;
        this.floor_id = floor_id;
        this.toilet_name = toilet_name;
        this.toilet_map_pic = toilet_map_pic;
        this.toilet_type = toilet_type;
    }

    public String getToilet_id() {
        return toilet_id;
    }

    public void setToilet_id(String toilet_id) {
        this.toilet_id = toilet_id;
    }

    public String getFloor_id() {
        return floor_id;
    }

    public void setFloor_id(String floor_id) {
        this.floor_id = floor_id;
    }

    public String getToilet_name() {
        return toilet_name;
    }

    public void setToilet_name(String toilet_name) {
        this.toilet_name = toilet_name;
    }

    public String getToilet_map_pic() {
        return toilet_map_pic;
    }

    public void setToilet_map_pic(String toilet_map_pic) {
        this.toilet_map_pic = toilet_map_pic;
    }

    public String getToilet_type() {
        return toilet_type;
    }

    public void setToilet_type(String toilet_type) {
        this.toilet_type = toilet_type;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Toilet){ Toilet l = (Toilet ) obj;
            if(l.getToilet_name().equals(toilet_name) && l.getToilet_id()==toilet_id ) return true;
        }
        return false;
    }
}
