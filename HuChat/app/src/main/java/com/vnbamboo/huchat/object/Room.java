package com.vnbamboo.huchat.object;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

public class Room {
    private String roomCode;
    private String name;
    private List<User> listMember;
    private transient Bitmap avatar;

    public List<User> getListMember() {
        return listMember;
    }

    public User getUserAt(int i){
        return listMember.get(i);
    }

    public String getRoomCode() {
        return roomCode;
    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public void setListMember( List<User> listMember ) {
        this.listMember = listMember;
    }

    public void setRoomCode( String roomCode ) {
        this.roomCode = roomCode;
    }

    public Room( String roomCode, String name){
        this.roomCode = roomCode;
        this.name = name;
    }
    public Room(){
        roomCode = name = null;
        listMember = new ArrayList<>();
    }

    public Bitmap getAvatar() {
        return avatar;
    }

    public void setAvatar( Bitmap avatar ) {
        this.avatar = avatar;
    }
}

