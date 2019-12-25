package com.example.arunavigator.Activities.Activities.GetterSetter;

import androidx.annotation.NonNull;

public class Edge {
    private String edge_id,edge_source,edge_destination,vertex_path_type,distance;

    public Edge(String edge_id, String edge_source, String edge_destination, String vertex_path_type, String distance) {
        this.edge_id = edge_id;
        this.edge_source = edge_source;
        this.edge_destination = edge_destination;
        this.vertex_path_type = vertex_path_type;
        this.distance = distance;
    }

    public String getEdge_id() {
        return edge_id;
    }

    public void setEdge_id(String edge_id) {
        this.edge_id = edge_id;
    }

    public String getEdge_source() {
        return edge_source;
    }

    public void setEdge_source(String edge_source) {
        this.edge_source = edge_source;
    }

    public String getEdge_destination() {
        return edge_destination;
    }

    public void setEdge_destination(String edge_destination) {
        this.edge_destination = edge_destination;
    }

    public String getVertex_path_type() {
        return vertex_path_type;
    }

    public void setVertex_path_type(String vertex_path_type) {
        this.vertex_path_type = vertex_path_type;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
