package com.vnbamboo.huchat.object;

public class ResultFromServer{
    public String event;
    public Boolean success;
    public ResultFromServer(String event, Boolean success){
        this.event = event;
        this.success = success;
    }
    public ResultFromServer(){
        event = "";
        success = true;
    }
}
