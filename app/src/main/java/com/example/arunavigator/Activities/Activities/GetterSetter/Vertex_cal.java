package com.example.arunavigator.Activities.Activities.GetterSetter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class Vertex_cal {
    private String vertex_id;
    private String vertex_name;
    private String vertex_lat;
    private String vertex_long;
    private String vertex_number;
    private String vertex_type;
    private String vertex_building_name;
    private String vertex_path_type;

    public Vertex_cal(String vertex_id, String vertex_name, String vertex_lat, String vertex_long, String vertex_number, String vertex_type, String vertex_building_name, String vertex_path_type) {
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((vertex_id == null) ? 0 : vertex_id.hashCode());
        return result;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Vertex_cal other = (Vertex_cal) obj;
        if (vertex_id == null) {
            if (other.vertex_id != null)
                return false;
        } else if (!vertex_id.equals(other.vertex_id))
            return false;
        return true;
    }

    @NonNull
    @Override
    public String toString() {
        return vertex_name;
    }
}
