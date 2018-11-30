package com.vnbamboo.huchat.RecyclerViewAdapter;

public class ChatMessage{
    String from;
    String value;
    ChatMessage()
    {
        from = "";
        value = "";
    }
    public ChatMessage( String from, String value ){
        this.from = from;
        this.value = value;
    }
}
