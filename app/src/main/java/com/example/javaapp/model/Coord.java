package com.example.javaapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coord {

@SerializedName("lon")
@Expose
private Double lon;
@SerializedName("lat")
@Expose
private Double lat;

    public Coord(double v, double v1) {
        this.lat = v;
        this.lon = v1;
    }

    public Double getLon() {
    return lon;
    }

    public void setLon(Double lon) {
    this.lon = lon;
    }

    public Double getLat() {
    return lat;
    }

    public void setLat(Double lat) {
    this.lat = lat;
    }

}