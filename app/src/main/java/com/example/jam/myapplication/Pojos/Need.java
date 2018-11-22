package com.example.jam.myapplication.Pojos;

public class Need {

    String userID;
    String item_name;
    String desc;
    String lati;
    String longi;
    String need_have;

    public Need(String userID, String item_name, String desc, String lati, String longi, String need_have) {
        this.userID = userID;
        this.item_name = item_name;
        this.desc = desc;
        this.lati = lati;
        this.longi = longi;
        this.need_have = need_have;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLati() {
        return lati;
    }

    public void setLati(String lati) {
        this.lati = lati;
    }

    public String getLongi() {
        return longi;
    }

    public void setLongi(String longi) {
        this.longi = longi;
    }

    public String getNeed_have() {
        return need_have;
    }

    public void setNeed_have(String need_have) {
        this.need_have = need_have;
    }
}
