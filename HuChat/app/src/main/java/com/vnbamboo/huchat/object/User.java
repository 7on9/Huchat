package com.vnbamboo.huchat.object;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {
    private String userName;
    private String fullName;
    private String email;
    private String phone;
    private Long dob;
    private Boolean gender;
    private transient Bitmap avatar;
    private List<Room> roomList;

    public User(){
        userName = email = phone = "";
        gender = null;
        dob = null;
        roomList = new ArrayList<>();
    }

    public User(User a){
        this.userName = a.userName;
        this.fullName = a.fullName;
        this.email = a.email;
        this.phone = a.phone;
        this.dob = a.dob;
        this.gender = a.gender;
        this.avatar = a.avatar;
        this.roomList = a.roomList;
    }

    public User(String userName, String fullName, Long dob, Boolean gender, String email, String phone){
        this.userName = userName;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
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

    public void setRoomList( List<Room> roomList ) {
        this.roomList = roomList;
    }

    public List<Room> getRoomList() {
        return roomList;
    }

    public void addRoom(Room room){
        this.roomList.add(room);
    }

    public Room getRoomAt(int i){
        return this.roomList.get(i);
    }

    public int getIndexRoomCode(String code){
        for (int i = 0;i < roomList.size(); i++) {
            if(roomList.get(i).getRoomCode().toLowerCase().equals(code.toLowerCase())){
                return i;
            }
        }
        return -1;
    }
}
