package com.vnbamboo.huchat.object;

public class ChatMessage{
    private String fromUser;
    private String content;
    private long time;
    private String toUser;
    ChatMessage()
    {
        fromUser = "";
        content = "";
        toUser = "";
        time = 0;
    }
    public ChatMessage( String fromUser, String content ){
        this.fromUser = fromUser;
        this.content = content;
    }

    public String getFromUser() {
        return fromUser;
    }

    public String getToUser() {
        return toUser;
    }

    public long getTime() {
        return time;
    }

    public String getContent() {
        return content;
    }

    public void setToUser( String toUser ) {
        this.toUser = toUser;
    }

    public void setFromUser( String fromUser ) {
        this.fromUser = fromUser;
    }

    public void setContent( String content ) {
        this.content = content;
    }

    public void setTime( long time ) {
        this.time = time;
    }
}
