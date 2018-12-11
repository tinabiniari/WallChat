package com.unipi.tinampiniari.wallchat;

public class Messages {

    private String date;
    private String message;
    private String name;
    private String time;
    private String id;


    public Messages(String date, String message, String name, String time, String id) {  //Constructor class for Messages
        this.date = date;
        this.message = message;
        this.name = name;
        this.time = time;
        this.id = id;
    }

    public Messages(){

    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
