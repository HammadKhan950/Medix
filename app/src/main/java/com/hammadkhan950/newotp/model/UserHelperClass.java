package com.hammadkhan950.newotp.model;

public class UserHelperClass {


    String name, number, gender, age;

    public UserHelperClass() {
    }

    public UserHelperClass(String name, String number, String gender, String age) {
        this.name = name;
        this.number = number;
        this.gender = gender;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
