package com.affc.ait.models;

import android.graphics.Picture;
import android.graphics.drawable.PictureDrawable;

public class Student {

    private int id;
    private String name;
    private String email;
    private String login_code;
    private String address;
    private String city;
    private String phone;
    private String gender;
    private String DOB;

    private PictureDrawable profilePicture;

    public PictureDrawable getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(PictureDrawable profilePicture) {
        this.profilePicture = profilePicture;
    }

    public Student(int id, String name, String email, String login_code, String address, String city, String phone, String gender, String DOB, PictureDrawable profilePic) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.login_code = generateLoginCode();
        this.address = address;
        this.city = city;
        this.phone = phone;
        this.gender = gender;
        this.DOB = DOB;
        this.profilePicture = profilePic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin_code() {
        return login_code;
    }

    public void setLogin_code(String login_code) {
        this.login_code = login_code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", login_code='" + login_code + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", phone='" + phone + '\'' +
                ", gender='" + gender + '\'' +
                ", DOB='" + DOB + '\'' +
                '}';
    }

    public String generateLoginCode(){
        //TODO generate login code. sending it is handled by another class
        return " ";
    }


}
