package com.vnbamboo.huchat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.vnbamboo.huchat.object.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class Utility {

    public static final String CONNECTION = "connection";
    public static final String REGISTER = "register";
    public static final String LOGIN = "login";
    public static final String RESULT = "result";
    public static final String LOGOUT = "logout";
    public static final String SERVER_SEND_IMAGE_USER = "serverSendImageUser";
    public static final String SERVER_SEND_IMAGE_ROOM = "serverSendImageRoom";
    public static final String CLIENT_SEND_IMAGE_USER = "clientSendImageUser";
    public static final String CLIENT_SEND_IMAGE_ROOM = "clientSendImageRoom";
    public static final String CLIENT_REQUEST_IMAGE_USER = "clientRequestImageUser";
    public static final String CLIENT_REQUEST_IMAGE_ROOM = "clientRequestImageRoom";
    public static final String CLIENT_REQUEST_LIST_ROOM = "clientRequestListRoom";
    public static final String SERVER_SEND_LIST_ROOM = "serverSendListRoom";
    public static final String NEW_ROOM = "newRoom";
    public static final String JOIN_DUAL_ROOM = "joinDualRoom";
    public static final String CLIENT_REQUEST_HISTORY_CHAT_ROOM = "clientRequestHistoryChatRoom";
    public static final String SERVER_SEND_HISTORY_CHAT_ROOM = "serverSendHistoryChatRoom";
    public static final String CLIENT_REQUEST_PUBLIC_INFO_USER = "clientRequestPublicInfoUser";
    public static final String SERVER_SEND_LIST_USER = "serverSendListPublicInfoUser";
    public static final String JOIN_ROOM = "joinRoom";
    public static final String LEAVE_ROOM = "leaveRoom";
    public static final String CLIENT_SEND_MESSAGE = "clientSendMessage";
    public static final String SERVER_SEND_MESSAGE = "serverSendMessage";
    public static final byte REQUEST_TAKE_PHOTO = 24;
    public static final byte REQUEST_CHOOSE_PHOTO = 9;
    public static final int TIME_WAIT_SHORT = 750;
    public static final int TIME_WAIT_MEDIUM = 1000;
    public static final int TIME_WAIT_LONG = 2000;


    public static List<User> LIST_ALL_USER = new ArrayList<>();

    public static void startCreateNewMessageActivity( Context context, String userName, User user ){
        Intent intent = new Intent(context, CreateNewMessageActivity.class);
        intent.putExtra("UserName", userName);
        intent.putExtra("User", (Serializable) user);
        context.startActivity(intent);
    }

    public static void startLoginActivity(Context context){
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static void startChatActivity(Context mContext,String roomName, String roomCode){
        Intent intent = new Intent(mContext, ChatActivity.class);
        intent.putExtra("RoomName", roomName);
        intent.putExtra("RoomCode", roomCode);
        mContext.startActivity(intent);
    }

    public static void startEditProfileActivity(Context mContext){
        Intent intent = new Intent(mContext, EditProfileActivity.class);
        mContext.startActivity(intent);
    }

    public static String getLocalHost(){
        //set match server ip
        return "http://192.168.1.73:2409/";
    }

    public static String toSHA256(String input)
    {
        try {

            // Static getInstance method is called with hashing SHA
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // digest() method called
            // to calculate message digest of an input
            // and return array of byte
            byte[] messageDigest = md.digest(input.getBytes());

            // Convert byte array into signum representation
            BigInteger no = new BigInteger(1, messageDigest);

            // Convert message digest into hex value
            String hashtext = no.toString(16);

            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }

            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            System.out.println("Exception thrown"
                    + " for incorrect algorithm: " + e);

            return null;
        }
    }


    public static byte[] getByteArrayFromBitmap(Bitmap bm){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public static Bitmap byteArrayToBimap(byte[] img) {
        byte[] imageByteArray = (byte[]) img;
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.length);
        return bitmap;
    }

    public static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

//    Using to save sound file // use for later
//    public byte[] getByteArrayFromLocalFile(String path){
//        File file = new File(path);
//        int size = (int) file.length();
//        byte[] bytes = new byte[size];
//        try {
//            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
//            buf.read(bytes, 0, bytes.length);
//            buf.close();
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//        return bytes;
//    } .

    public static JSONObject objectToJSONObject( Object object){
        Object json = null;
        JSONObject jsonObject = null;
        try {
            json = new JSONTokener(object.toString()).nextValue();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (json instanceof JSONObject) {
            jsonObject = (JSONObject) json;
        }
        return jsonObject;
    }

    public static JSONArray objectToJSONArray(Object object){
        Object json = null;
        JSONArray jsonArray = null;
        try {
            json = new JSONTokener(object.toString()).nextValue();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (json instanceof JSONArray) {
            jsonArray = (JSONArray) json;
        }
        return jsonArray;
    }

}
