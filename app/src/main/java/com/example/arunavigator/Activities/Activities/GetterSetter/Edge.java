package com.example.arunavigator.Activities.Activities.GetterSetter;

import androidx.annotation.NonNull;

public class Edge {
    private String edge_id,edge_source,edge_destination;
    private int distance;

    public Edge(String edge_id, String edge_source, String edge_destination, int distance) {
        this.edge_id = edge_id;
        this.edge_source = edge_source;
        this.edge_destination = edge_destination;
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

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
