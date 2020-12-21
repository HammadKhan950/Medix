package com.hammadkhan950.newotp.model;

public class AdminData {
    private String Category;
    private String Date;
    private String Name;
    private String Time;

    public AdminData(String category, String date, String name, String time) {
        Category = category;
        Date = date;
        Name = name;
        Time = time;
    }

    public AdminData() {
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }
}
