package com.vnbamboo.huchat;

public class User {

    private String userName;
    private String name;
    private boolean gender;
    private String avatarPath;
    private String phoneNumber;
    private String mail;

    User(){
        userName = "";
        name = phoneNumber = mail = "";
    }
    
    public User( String userName, String name, String phoneNumber, String mail, String avatarPath ){
        this.userName = userName;
        this.name = name;
        this.mail = mail;
        this.phoneNumber = phoneNumber;
        this.avatarPath = avatarPath;
    }
    
    public User( User a ){
        this.userName = a.userName;
        this.mail = a.mail;
        this.name = a.name;
        this.phoneNumber = a.phoneNumber;
    }
    
    public String getName(){
        return name;
    }
    
    public String getPhoneNumber(){
        return phoneNumber;
    }
    
    public String getAddress(){
        return mail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName( String userName ) {
        this.userName = userName;
    }

    public void setGender( String userName ) {
        this.userName = userName;
    }

    public void setMail( String mail ) {
        this.mail = mail;
    }

    public void setAvatarPath( String avatarPath ) {
        this.avatarPath = avatarPath;
    }

    public boolean isGender() {
        return gender;
    }

    public String getAvatarPath() {
        return avatarPath;
    }

    public String getMail() {
        return mail;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public void setPhoneNumber( String phoneNumber ) {
        this.phoneNumber = phoneNumber;
    }
}
