package com.example.arunavigator.Activities.Activities.GetterSetter;

import androidx.annotation.NonNull;

public class Edge_cal {
    private final String edge_cal_id;
    private final Vertex_cal edge_cal_source;
    private final Vertex_cal edge_cal_destination;
    private final int weight;

    public Edge_cal(String edge_cal_id, Vertex_cal edge_cal_source, Vertex_cal edge_cal_destination, int weight) {
        this.edge_cal_id = edge_cal_id;
        this.edge_cal_source = edge_cal_source;
        this.edge_cal_destination = edge_cal_destination;
        this.weight = weight;
    }

    public String getEdge_cal_id() {
        return edge_cal_id;
    }

    public Vertex_cal getEdge_cal_source() {
        return edge_cal_source;
    }

    public Vertex_cal getEdge_cal_destination() {
        return edge_cal_destination;
    }

    public int getWeight() {
        return weight;
    }

    @NonNull
    @Override
    public String toString() {
        return edge_cal_source + " " + edge_cal_destination;
    }
}
