package com.example.arunavigator.Activities.Activities.GetterSetter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Vertex {
    private String vertex_id,vertex_name,vertex_lat,vertex_long,vertex_number,vertex_type,vertex_building_name,vertex_path_type;
    public Vertex(){}

    public Vertex(String vertex_id, String vertex_name, String vertex_lat, String vertex_long, String vertex_number, String vertex_type, String vertex_building_name, String vertex_path_type) {
        this.vertex_id = vertex_id;
        this.vertex_name = vertex_name;
        this.vertex_lat = vertex_lat;
        this.vertex_long = vertex_long;
        this.vertex_number = vertex_number;
        this.vertex_type = vertex_type;
        this.vertex_building_name = vertex_building_name;
        this.vertex_path_type = vertex_path_type;
    }

    public String getVertex_id() {
        return vertex_id;
    }

    public void setVertex_id(String vertex_id) {
        this.vertex_id = vertex_id;
    }

    public String getVertex_name() {
        return vertex_name;
    }

    public void setVertex_name(String vertex_name) {
        this.vertex_name = vertex_name;
    }

    public String getVertex_lat() {
        return vertex_lat;
    }

    public void setVertex_lat(String vertex_lat) {
        this.vertex_lat = vertex_lat;
    }

    public String getVertex_long() {
        return vertex_long;
    }

    public void setVertex_long(String vertex_long) {
        this.vertex_long = vertex_long;
    }

    public String getVertex_number() {
        return vertex_number;
    }

    public void setVertex_number(String vertex_number) {
        this.vertex_number = vertex_number;
    }

    public String getVertex_type() {
        return vertex_type;
    }

    public void setVertex_type(String vertex_type) {
        this.vertex_type = vertex_type;
    }

    public String getVertex_building_name() {
        return vertex_building_name;
    }

    public void setVertex_building_name(String vertex_building_name) {
        this.vertex_building_name = vertex_building_name;
    }

    public String getVertex_path_type() {
        return vertex_path_type;
    }

    public void setVertex_path_type(String vertex_path_type) {
        this.vertex_path_type = vertex_path_type;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        return super.equals(obj);
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
