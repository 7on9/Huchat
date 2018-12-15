package com.vnbamboo.huchat.object;

public class ResultFromSever{
    public String event;
    public Boolean success;
    public ResultFromSever(String event, Boolean success){
        this.event = event;
        this.success = success;
    }
    public ResultFromSever(){
        event = "";
        success = true;
    }
}
