package com.vnbamboo.huchat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.vnbamboo.huchat.object.User;

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

public class Utility {

    public static final String CONNECTION = "connection";
    public static final String REGISTER = "register";
    public static final String LOGIN = "login";
    public static final String RESULT = "result";
    public static final String LOGOUT = "logout";
    public static final String SERVER_SEND_IMAGE = "severSendImage";
    public static final String CLIENT_SEND_IMAGE = "clientSendImage";
    public static final String CLIENT_REQUEST_IMAGE_USER = "clientRequestImageUser";
    public static final String CLIENT_GET_HISTORY_CHAT_ROOM = "clientGetHistoryChatRoom";
    public static final String SEVER_RETURN_HISTORY_CHAT_ROOM = "severReturnHistoryChatRoom";
    public static final String JOINROOM = "joinRoom";
    public static final String LEAVEROOM = "leaveRoom";
    public static final String MESSAGE_FROM_CLIENT = "messageFromClient";
    public static final String MESSAGE_FROM_SEVER = "messageFromSever";
    public static final byte REQUEST_TAKE_PHOTO = 24;
    public static final byte REQUEST_CHOOSE_PHOTO = 9;

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

    public static void startChatActivity(Context mContext,String userName){
        Intent intent = new Intent(mContext, ChatActivity.class);
        intent.putExtra("UserName", userName);
        mContext.startActivity(intent);
    }

    public static void startEditProfileActivity(Context mContext, User user){
        Intent intent = new Intent(mContext, EditProfileActivity.class);
        intent.putExtra("User", (Serializable) user);
        mContext.startActivity(intent);
    }

    public static String getLocalHost(){
        //set match sever ip
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

    public byte[] getByteArrayFromLocalFile(String path){
        File file = new File(path);
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        try {
            BufferedInputStream buf = new BufferedInputStream(new FileInputStream(file));
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bytes;
    }

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

}
