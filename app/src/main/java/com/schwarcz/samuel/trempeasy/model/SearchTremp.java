package com.schwarcz.samuel.trempeasy.model;

import java.io.Serializable;

public class SearchTremp implements Serializable {
    String uid, from , to , hour_start, hour_end , places;


    public SearchTremp(String uid, String from, String to, String hour_start, String hour_end, String places) {
        this.uid = uid;
        this.from = from;
        this.to = to;
        this.hour_start = hour_start;
        this.hour_end = hour_end;
        this.places = places;
    }

    public String getUid() {
        return uid;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getHour_start() {
        return hour_start;
    }

    public String getHour_end() {
        return hour_end;
    }

    public String getPlaces() {
        return places;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setHour_start(String hour_start) {
        this.hour_start = hour_start;
    }

    public void setHour_end(String hour_end) {
        this.hour_end = hour_end;
    }

    public void setPlaces(String places) {
        this.places = places;
    }
}
