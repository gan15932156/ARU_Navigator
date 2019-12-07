package com.example.arunavigator.Activities.Activities.GetterSetter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Floor {
    private String floor_id,location_id,floor_number;

    public Floor(){}

    public Floor(String floor_id, String location_id, String floor_number) {
        this.floor_id = floor_id;
        this.location_id = location_id;
        this.floor_number = floor_number;
    }

    public String getFloor_id() {
        return floor_id;
    }

    public void setFloor_id(String floor_id) {
        this.floor_id = floor_id;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getFloor_number() {
        return floor_number;
    }

    public void setFloor_number(String floor_number) {
        this.floor_number = floor_number;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof Floor){ Floor l = (Floor ) obj;
            if(l.getFloor_number().equals(floor_number) && l.getFloor_id()==floor_id ) return true;
        }
        return false;
    }
}
