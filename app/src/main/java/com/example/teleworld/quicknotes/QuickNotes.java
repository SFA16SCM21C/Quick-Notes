package com.example.teleworld.quicknotes;

/**
 * Created by teleworld on 2/11/2017.
 */

public class QuickNotes {
    public QuickNotes(){}

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    //varibles to hold the data
    String Date = "";
    String Data = "";

    public String toString(){
        return Date + ": " + Data;
    }
}
