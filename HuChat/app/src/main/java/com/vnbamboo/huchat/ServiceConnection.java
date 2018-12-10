package com.vnbamboo.huchat;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.vnbamboo.huchat.Utility.CONNECTION;
import static com.vnbamboo.huchat.Utility.RESULT;
import static com.vnbamboo.huchat.Utility.SERVER_SEND_IMAGE;

public class ServiceConnection extends Service {

    public static Socket mSocket;
    public static ResultFromSever resultFromSever;
    public static Boolean isConnected = false;
    public static Emitter.Listener onNewImage, onResultFromSever;
    public static Boolean statusConnecttion = false;

    public ServiceConnection() {
    }

    @Override
    public IBinder onBind( Intent intent ) {


        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        { }
    }

    @Override
    public int onStartCommand( Intent intent, int flags, int startId ) {
        if(isConnected) return START_STICKY;
        isConnected = true;
        try
        {
            mSocket = IO.socket(Utility.getLocalHost());

        }catch (Exception e)
        {

        }
        onResultFromSever = new Emitter.Listener(){
            @Override
            public void call( Object... args ) {
                resultFromSever = new ResultFromSever((String) args[0], (Boolean) args[1]);
                if(resultFromSever.event.equals(CONNECTION)){
                    statusConnecttion = resultFromSever.success;
                }
            }
        };
        onNewImage = new Emitter.Listener() {
            @Override
            public void call(Object... args) {

            }
        };

        mSocket.connect();
        //Add listen event
        mSocket.on(SERVER_SEND_IMAGE, onNewImage);
        mSocket.on(RESULT, onResultFromSever);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        isConnected = false;
        mSocket.disconnect();
        super.onDestroy();
    }

    public void disConnect(){
        mSocket.disconnect();
    }
}
class ResultFromSever{
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
