package com.vnbamboo.huchat.object;

public class ChatMessage{
    private String from;
    private String value;
    private String time;
    ChatMessage()
    {
        from = "";
        value = "";
    }
    public ChatMessage( String from, String value ){
        this.from = from;
        this.value = value;
    }

    public String getFrom() {
        return from;
    }

    public String getTime() {
        return time;
    }

    public String getValue() {
        return value;
    }

    public void setFrom( String from ) {
        this.from = from;
    }

    public void setTime( String time ) {
        this.time = time;
    }

    public void setValue( String value ) {
        this.value = value;
    }
}
