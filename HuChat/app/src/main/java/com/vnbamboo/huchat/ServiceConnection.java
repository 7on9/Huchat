package com.vnbamboo.huchat;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.vnbamboo.huchat.Utility.SERVER_SEND_IMAGE;

public class ServiceConnection extends Service {

    public static Socket mSocket;
    public static Boolean isConnected = false;
    public static Emitter.Listener onNewImage;

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
        onNewImage = new Emitter.Listener() {
            @Override
            public void call(Object... args) {

            }
        };
        mSocket.connect();
        mSocket.on(SERVER_SEND_IMAGE, onNewImage);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        isConnected = false;
        mSocket.disconnect();
        super.onDestroy();
    }
}
