package com.schwarcz.samuel.trempeasy.model;

public class Tremp {

    String uid, from ,to , hour, places , remainPlaces;




    public Tremp(){}

    public Tremp(String uid,String mfrom, String to, String hour, String places) {
        this.uid = uid;
        this.from = mfrom;
        this.to = to;
        this.hour = hour;
        this.places = places;
        this.remainPlaces=places;


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

    public String getHour() {
        return hour;
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

    public void setHour(String hour) {
        this.hour = hour;
    }


    public void setPlaces(String places) {
        this.places = places;
    }

    public String getRemainPlaces() {
        return remainPlaces;
    }

    public void setRemainPlaces(String remainPlaces) {
        this.remainPlaces = remainPlaces;
    }

//    public String[] getTravellers() {
//        return travellers;
//    }

//    public void setTravellers(String[] travellers) {
//        this.travellers = travellers;
//    }
}
