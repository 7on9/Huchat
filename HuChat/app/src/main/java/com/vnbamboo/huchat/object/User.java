package com.vnbamboo.huchat.object;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class User implements Serializable {
    private String userName;
    private String fullName;
    private String email;
    private String phone;
    private Long dob;
    private Boolean gender;
    private String password;
    private transient Bitmap avatar;
    private Map<String, Room> mapRoom;

    public User(){
        userName = email = phone = "";
        gender = null;
        dob = null;
        mapRoom = new HashMap<>();
    }

    public User(User a){
        this.userName = a.userName;
        this.fullName = a.fullName;
        this.email = a.email;
        this.phone = a.phone;
        this.dob = a.dob;
        this.gender = a.gender;
        this.avatar = a.avatar;
        this.mapRoom.clear();
        this.mapRoom.putAll(a.mapRoom);
    }

    public User(String userName, String fullName, Long dob, Boolean gender, String email, String phone){
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.dob = dob;
        this.gender = gender;
    }

    public User(String userName, String fullName, boolean gender, Bitmap avatar){
        this.userName = userName;
        this.fullName = fullName;
        this.gender = gender;
        this.avatar = avatar;
    }

    public void setUserName( String userName ) {
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName( String fullName ) {
        this.fullName = fullName;
    }

    public Boolean getGender() {
        return gender;
    }

    public Long getDob() {
        return dob;
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public void setDob( Long dob ) {
        this.dob = dob;
    }

    public void setEmail( String email ) {
        this.email = email;
    }

    public void setGender( Boolean gender ) {
        this.gender = gender;
    }

    public void setPhone( String phone ) {
        this.phone = phone;
    }

    public void setAvatar( Bitmap avatar ) {
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword( String password ) {
        this.password = password;
    }
}
