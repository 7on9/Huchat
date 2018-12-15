package com.vnbamboo.huchat.object;

public class ChatMessage{
    private String userNameSender;
    private String content;
    private long time;
    private String roomCodeReceive;
    public ChatMessage()
    {
        userNameSender = "";
        content = "";
        roomCodeReceive = "";
        time = 0;
    }
    public ChatMessage( String userNameSender, String content ){
        this.userNameSender = userNameSender;
        this.content = content;
    }

    public String getUserNameSender() {
        return userNameSender;
    }

    public String getRoomCodeReceive() {
        return roomCodeReceive;
    }

    public long getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }

    public void setRoomCodeReceive( String roomCodeReceive ) {
        this.roomCodeReceive = roomCodeReceive;
    }

    public void setUserNameSender( String userNameSender ) {
        this.userNameSender = userNameSender;
    }

    public void setContent( String content ) {
        this.content = content;
    }

    public void setTime( long time ) {
        this.time = time;
    }
}
