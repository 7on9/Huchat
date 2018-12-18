package com.vnbamboo.huchat;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;

import com.vnbamboo.huchat.object.ChatMessage;
import com.vnbamboo.huchat.object.ResultFromServer;
import com.vnbamboo.huchat.object.Room;
import com.vnbamboo.huchat.object.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

import static com.vnbamboo.huchat.Utility.CONNECTION;
import static com.vnbamboo.huchat.Utility.LOGIN;
import static com.vnbamboo.huchat.Utility.LOGOUT;
import static com.vnbamboo.huchat.Utility.RESULT;
import static com.vnbamboo.huchat.Utility.SERVER_SEND_IMAGE_ROOM;
import static com.vnbamboo.huchat.Utility.SERVER_SEND_IMAGE_USER;
import static com.vnbamboo.huchat.Utility.SERVER_SEND_HISTORY_CHAT_ROOM;
import static com.vnbamboo.huchat.Utility.SERVER_SEND_LIST_ROOM;
import static com.vnbamboo.huchat.Utility.SERVER_SEND_LIST_USER;
import static com.vnbamboo.huchat.Utility.byteArrayToBimap;
import static com.vnbamboo.huchat.Utility.objectToJSONArray;
import static com.vnbamboo.huchat.Utility.objectToJSONObject;

public class ServiceConnection extends Service {

    public static Bitmap tempImage = null;
    public static Boolean isConnected = false;
    public static Boolean statusConnecttion = false;
    public static Emitter.Listener onResultFromServer, onListUserFromServer;
    public static List<ChatMessage> tmpListChat = new ArrayList<>();
    public static ResultFromServer resultFromServer;
    public static Socket mSocket;
    public static User thisUser = new User();
    public static List<User> userList = new ArrayList<>();
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
        isConnected = statusConnecttion  =  true;
        try
        {
            mSocket = IO.socket(Utility.getLocalHost());
        }catch (Exception e)
        {

        }
        onResultFromServer = new Emitter.Listener() {
            @Override
            public void call( Object... args ) {
                resultFromServer = new ResultFromServer((String) args[0], (Boolean) args[1]);
                switch (resultFromServer.event) {
                    case CONNECTION:
                        statusConnecttion = resultFromServer.success;
                        break;
                    case LOGIN:
                        if (!resultFromServer.success.booleanValue()) break;
                        JSONObject jsonUser = objectToJSONObject(args[2]);
                        try {
                            String tmp = (String) jsonUser.get("USER_NAME");
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
                        break;

                    case SERVER_SEND_IMAGE_USER:
                        if (resultFromServer.success)
                            thisUser.setAvatar(byteArrayToBimap((byte[]) args[2]));
                        break;

                    case SERVER_SEND_IMAGE_ROOM:
                        if (resultFromServer.success)
                            thisUser.getRoomAt(thisUser.getIndexRoomCode((String) args[3])).setAvatar(byteArrayToBimap((byte[]) args[2]));
                        break;

                    case SERVER_SEND_LIST_ROOM:
                        if (resultFromServer.success) {
                            JSONArray jsonRoomArr = objectToJSONArray(args[2]);
                            try {
                                for (int i = 0; i < jsonRoomArr.length(); i++) {
                                    Room room = new Room();
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

                    case SERVER_SEND_HISTORY_CHAT_ROOM :
                        if(resultFromServer.success){
                            JSONArray jsonRoomArr = objectToJSONArray(args[2]);
                            try {
                                tmpListChat = new ArrayList<>();
                                for (int i = 0; i < jsonRoomArr.length(); i++) {
                                    ChatMessage chatMessage = new ChatMessage();
                                    JSONObject jsonobject = null;
                                    jsonobject = jsonRoomArr.getJSONObject(i);
                                    chatMessage.setContent(jsonobject.getString("CONTENT"));
                                    chatMessage.setUserNameSender(jsonobject.getString("USER_NAME"));
                                    chatMessage.setTime(jsonobject.getLong("TIME"));
                                    tmpListChat.add(chatMessage);
                                }
                                thisUser.getRoomAt(thisUser.getIndexRoomCode((String) args[3])).setChatHistory(tmpListChat);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                }
            }
        };
        onListUserFromServer = new Emitter.Listener() {
            @Override
            public void call( Object... args ) {
                try {
                    JSONArray jsonArray = objectToJSONArray(args[0]);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        User tmpUser = new User();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        tmpUser.setUserName(jsonObject.getString("USER_NAME"));
                        tmpUser.setFullName(jsonObject.getString("FULL_NAME"));
                        userList.add(tmpUser);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        mSocket.connect();
        //Add listen event
        mSocket.on(RESULT, onResultFromServer);
        mSocket.on(SERVER_SEND_LIST_USER, onListUserFromServer);
        return START_STICKY;
    }

//    public void reconnect(){
////        mSocket.io().reconnection();
//    }
    @Override
    public void onDestroy() {
        isConnected = false;
        statusConnecttion = false;
        if(thisUser.getUserName().length() > 0) {
            mSocket.emit(LOGOUT, thisUser.getUserName());
        }
        mSocket.disconnect();
        super.onDestroy();
    }
}

