package com.vnbamboo.huchat;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.util.Log;

import com.vnbamboo.huchat.object.Room;
import com.vnbamboo.huchat.object.User;

import org.json.JSONArray;
import org.json.JSONObject;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.vnbamboo.huchat.Utility.CLIENT_REQUEST_LIST_ROOM;
import static com.vnbamboo.huchat.Utility.CONNECTION;
import static com.vnbamboo.huchat.Utility.LOGIN;
import static com.vnbamboo.huchat.Utility.LOGOUT;
import static com.vnbamboo.huchat.Utility.RESULT;
import static com.vnbamboo.huchat.Utility.SERVER_SEND_IMAGE_ROOM;
import static com.vnbamboo.huchat.Utility.SERVER_SEND_IMAGE_USER;
import static com.vnbamboo.huchat.Utility.byteArrayToBimap;
import static com.vnbamboo.huchat.Utility.objectToJSONArray;
import static com.vnbamboo.huchat.Utility.objectToJSONObject;

public class ServiceConnection extends Service {

    public static Socket mSocket;
    public static ResultFromSever resultFromSever;
    public static Boolean isConnected = false;
    public static Emitter.Listener onNewImage, onResultFromSever;
    public static Boolean statusConnecttion = false;
    public static User thisUser = new User();
    public static Bitmap tempImage = null;
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
                switch (resultFromSever.event) {
                    case CONNECTION : statusConnecttion = resultFromSever.success;
                    break;
                    case LOGIN : {
                        if(!resultFromSever.success.booleanValue()) break;
                        JSONObject jsonUser = objectToJSONObject(args[2]);
                        try {
                            String tmp = (String)jsonUser.get("USER_NAME");
                            thisUser.setUserName(tmp);
                            tmp = (String) jsonUser.get("FULL_NAME");
                            thisUser.setFullName(tmp);
                            tmp = (String) jsonUser.get("EMAIL");
                            thisUser.setEmail(tmp);
//                            Long t = (Long) jsonUser.get("DOB");
//                            thisUser.setDob(t);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                    case SERVER_SEND_IMAGE_USER :
                        if(resultFromSever.success)
                            thisUser.setAvatar(byteArrayToBimap((byte[]) args[2]));
                    break;
                    case SERVER_SEND_IMAGE_ROOM :
                        if(resultFromSever.success)
                            tempImage = byteArrayToBimap((byte[]) args[2]);
                        break;
                    case CLIENT_REQUEST_LIST_ROOM :
                        if(resultFromSever.success){
                            JSONArray jsonRoomArr = objectToJSONArray(args[2]);
                            try{
                                Room room = new Room();
                                for (int i = 0; i < jsonRoomArr.length(); i++) {
                                    JSONObject jsonobject = null;
                                    jsonobject = jsonRoomArr.getJSONObject(i);
                                    room.setRoomCode(jsonobject.getString("ROOM_CODE"));
                                    room.setName(jsonobject.getString("ROOM_NAME"));
                                    thisUser.addRoom(room);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
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
        mSocket.on(RESULT, onResultFromSever);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        isConnected = false;
        mSocket.emit(LOGOUT, thisUser.getUserName());
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

