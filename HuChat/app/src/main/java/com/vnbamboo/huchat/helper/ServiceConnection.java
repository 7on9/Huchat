package com.vnbamboo.huchat.helper;

import android.app.Service;
import android.content.Intent;
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

import static com.vnbamboo.huchat.helper.Utility.CHANGE_DOB;
import static com.vnbamboo.huchat.helper.Utility.CHANGE_FULL_NAME;
import static com.vnbamboo.huchat.helper.Utility.CHANGE_GENDER;
import static com.vnbamboo.huchat.helper.Utility.CHANGE_MAIL;
import static com.vnbamboo.huchat.helper.Utility.CHANGE_PASSWORD;
import static com.vnbamboo.huchat.helper.Utility.CHANGE_PHONE;
import static com.vnbamboo.huchat.helper.Utility.LIST_ALL_PUBLIC_ROOM;
import static com.vnbamboo.huchat.helper.Utility.LIST_ALL_USER;
import static com.vnbamboo.huchat.helper.Utility.LIST_NAME_USER;
import static com.vnbamboo.huchat.helper.Utility.LIST_ROOM_OF_THIS_USER;
import static com.vnbamboo.huchat.helper.Utility.MAP_ALL_PUBLIC_ROOM;
import static com.vnbamboo.huchat.helper.Utility.MAP_ALL_USER;
import static com.vnbamboo.huchat.helper.Utility.MAP_ROOM_OF_THIS_USER;
import static com.vnbamboo.huchat.helper.Utility.CONNECTION;
import static com.vnbamboo.huchat.helper.Utility.LOGIN;
import static com.vnbamboo.huchat.helper.Utility.LOGOUT;
import static com.vnbamboo.huchat.helper.Utility.NEW_ROOM;
import static com.vnbamboo.huchat.helper.Utility.RESULT;
import static com.vnbamboo.huchat.helper.Utility.SERVER_SEND_IMAGE_ROOM;
import static com.vnbamboo.huchat.helper.Utility.SERVER_SEND_IMAGE_USER;
import static com.vnbamboo.huchat.helper.Utility.SERVER_SEND_HISTORY_CHAT_ROOM;
import static com.vnbamboo.huchat.helper.Utility.SERVER_SEND_LIST_MEMBER_OF_ROOM;
import static com.vnbamboo.huchat.helper.Utility.SERVER_SEND_LIST_ROOM;
import static com.vnbamboo.huchat.helper.Utility.SERVER_SEND_LIST_ROOM_OF_THIS_USER;
import static com.vnbamboo.huchat.helper.Utility.SERVER_SEND_MAP_ALL_USER;
import static com.vnbamboo.huchat.helper.Utility.byteArrayToBimap;
import static com.vnbamboo.huchat.helper.Utility.objectToJSONArray;
import static com.vnbamboo.huchat.helper.Utility.objectToJSONObject;

public class ServiceConnection extends Service {

    public static Boolean isConnected = false;
    public static Boolean statusConnection = false;
    public static Emitter.Listener onResultFromServer, onListUserFromServer, onListRoom;
    public static List<ChatMessage> tmpListChat = new ArrayList<>();
    public static ResultFromServer resultFromServer;
    public static Socket mSocket;
    public static User thisUser = new User();

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
        {

        }
    }

    @Override
    public int onStartCommand( Intent intent, int flags, int startId ) {
        if(isConnected) return START_STICKY;

        statusConnection  =  false;
        //mSocket = null;
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
                        statusConnection = resultFromServer.success;
                        break;
                    case LOGIN:
                        if (!resultFromServer.success.booleanValue()) break;

                        JSONObject jsonUser = objectToJSONObject(args[2]);

                        try {
                            String tmp = (String) jsonUser.get("USER_NAME");
                            thisUser.setUserName(tmp.toLowerCase());
                            tmp = (String) jsonUser.get("FULL_NAME");
                            thisUser.setFullName(tmp);
                            tmp = (String) jsonUser.get("MAIL");
                            thisUser.setEmail(tmp.toLowerCase());
//                            Long t = (Long) jsonUser.get("DOB");
//                            thisUser.setDob(t);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;

                    case SERVER_SEND_IMAGE_USER:
                        if (resultFromServer.success) {
                            for (User user: LIST_ALL_USER){
                                if(user.getUserName().equals((String) args[3])){
                                    user.setAvatar(byteArrayToBimap((byte[]) args[2]));
                                }
                                if(user.getUserName().equals(thisUser.getUserName())){
                                    thisUser.setAvatar(user.getAvatar());
                                }
                            }
                        }
                        break;

                    case SERVER_SEND_IMAGE_ROOM:
                        if (resultFromServer.success)
                            MAP_ROOM_OF_THIS_USER.get((String) args[3]).setAvatar(byteArrayToBimap((byte[]) args[2]));
                        break;

                    case SERVER_SEND_LIST_ROOM_OF_THIS_USER:
                        if (resultFromServer.success) {
                            JSONArray jsonRoomArr = objectToJSONArray(args[2]);
                            try {
                                for (int i = 0; i < jsonRoomArr.length(); i++) {
                                    Room room = new Room();
                                    JSONObject jsonobject = null;
                                    jsonobject = jsonRoomArr.getJSONObject(i);
                                    room.setRoomCode(jsonobject.getString("ROOM_CODE"));
                                    room.setName(jsonobject.getString("ROOM_NAME"));
                                    room.setDual(jsonobject.getInt("IS_DUAL") == 1 ? true : false);
                                    LIST_ROOM_OF_THIS_USER.add(room);
                                    MAP_ROOM_OF_THIS_USER.put(room.getRoomCode(), room);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case NEW_ROOM :
                        Room room = new Room();
                        try {
                            JSONObject jsonObject = objectToJSONObject(args[2]);
                            room.setRoomCode(jsonObject.getString("ROOM_CODE"));
                            room.setName(jsonObject.getString("ROOM_NAME"));
                            room.setDual(jsonObject.getInt("IS_DUAL") == 1 ? true : false);
                            String[] listUserName = room.getRoomCode().split("#");
                            for (String userName : listUserName){
                                room.getListMember().put(userName, MAP_ALL_USER.get(userName));
                            }
                            MAP_ROOM_OF_THIS_USER.put(room.getRoomCode(), room);
                            LIST_ROOM_OF_THIS_USER.add(room);
                        }
                        catch(Exception e){
                            e.printStackTrace();
                        };
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
                                    chatMessage.setUserNameSender(jsonobject.getString("USER_NAME").toLowerCase());
                                    chatMessage.setTime(jsonobject.getLong("TIME"));
                                    tmpListChat.add(chatMessage);
                                }
                                MAP_ROOM_OF_THIS_USER.get((String) args[3]).setChatHistory(tmpListChat);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case SERVER_SEND_LIST_MEMBER_OF_ROOM :
                        if(resultFromServer.success){
                            JSONArray jsonRoomArr = objectToJSONArray(args[3]);
                            try {
                                tmpListChat = new ArrayList<>();
                                for (int i = 0; i < jsonRoomArr.length(); i++) {
                                    try {
                                        JSONObject jsonobject = null;
                                        jsonobject = jsonRoomArr.getJSONObject(i);
                                        String userName = jsonobject.getString("USER_NAME_MEMBER").toLowerCase();
                                        MAP_ROOM_OF_THIS_USER.get((String) args[2]).getListMember().put(userName, MAP_ALL_USER.get(userName));
                                    } catch (Exception ex){
                                        ex.printStackTrace();
                                    }
                                }
//                                MAP_ROOM_OF_THIS_USER.get((String) args[3]).setChatHistory(tmpListChat);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        break;
                    case CHANGE_MAIL:
                        break;
                    case CHANGE_PASSWORD:
                        break;
                    case CHANGE_FULL_NAME:
                        break;
                    case CHANGE_GENDER:
                        break;
                    case CHANGE_PHONE:
                        break;
                    case CHANGE_DOB:
                        break;

                }
            }
        };
        onListUserFromServer = new Emitter.Listener() {
            @Override
            public void call( Object... args ) {
                try {
                    JSONArray jsonArray = objectToJSONArray(args[0]);
                    LIST_ALL_USER.removeAll(LIST_ALL_USER);
                    LIST_NAME_USER.removeAll(LIST_NAME_USER);
                    MAP_ALL_USER.clear();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        User tmpUser = new User();
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        tmpUser.setUserName(jsonObject.getString("USER_NAME").toLowerCase());
                        tmpUser.setFullName(jsonObject.getString("FULL_NAME"));
                        LIST_ALL_USER.add(tmpUser);
                        LIST_NAME_USER.add(tmpUser.getUserName());
                        MAP_ALL_USER.put(tmpUser.getUserName(), tmpUser);
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        onListRoom = new Emitter.Listener() {
            @Override
            public void call( Object... args ) {
                JSONArray jsonRoomArr = objectToJSONArray(args[0]);
                try {
                    for (int i = 0; i < jsonRoomArr.length(); i++) {
                        Room room = new Room();
                        JSONObject jsonobject = null;
                        jsonobject = jsonRoomArr.getJSONObject(i);
                        room.setRoomCode(jsonobject.getString("ROOM_CODE"));
                        room.setName(jsonobject.getString("ROOM_NAME"));
                        room.setUserNameOwner(jsonobject.getString("USER_NAME_OWNER"));
                        LIST_ALL_PUBLIC_ROOM.add(room);
                        MAP_ALL_PUBLIC_ROOM.put(room.getRoomCode(), room);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        mSocket.connect();
        //Add listen event
        isConnected = true;
        mSocket.on(RESULT, onResultFromServer);
        mSocket.on(SERVER_SEND_LIST_ROOM, onListRoom);
        mSocket.on(SERVER_SEND_MAP_ALL_USER, onListUserFromServer);
        return START_STICKY;
    }

//    public void reconnect(){
////        mSocket.io().reconnection();
//    }
    @Override
    public void onDestroy() {
        isConnected = false;
        statusConnection = false;
        mSocket.off(RESULT, onResultFromServer);
//        mSocket.on(RESULT, onResultFromServer);
        mSocket.off(SERVER_SEND_MAP_ALL_USER, onListUserFromServer);
        if(thisUser.getUserName().length() > 0) {
            mSocket.emit(LOGOUT, thisUser.getUserName());
        }
        mSocket.disconnect();
        try
        {
            mSocket = IO.socket(Utility.getLocalHost());
        }catch (Exception e)
        {

        }
        mSocket.on(RESULT, onResultFromServer);
        super.onDestroy();
    }
}

