package com.vnbamboo.huchat.object;

import java.io.Serializable;

public class User implements Serializable {
    private String userName;
    private String fullName;
    private String email;
    private String phone;
    private String avatarPath;
    private Long dob;
    private Boolean gender;
    public User(){
        userName = email = phone = avatarPath = "";
        gender = null;
        dob = null;
    }
    public User(User a){
        this.userName = a.userName;
        this.fullName = a.fullName;
        this.email = a.email;
        this.phone = a.phone;
        this.avatarPath = a.avatarPath;
        this.dob = a.dob;
        this.gender = a.gender;
    }
    public User(String userName, String fullName, Long dob, Boolean gender, String email, String phone, String avatarPath){
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.avatarPath = avatarPath;
        this.dob = dob;
        this.gender = gender;
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

    public String getAvatarPath() {
        return avatarPath;
    }

    public void setAvatarPath( String avatarPath ) {
        this.avatarPath = avatarPath;
    }

    public Boolean getGender() {
        return gender;
    }

    public Long getDob() {
        return dob;
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
}
