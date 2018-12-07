package com.vnbamboo.huchat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import com.vnbamboo.huchat.object.User;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;

public class Utility {

    public static final String SERVER_SEND_IMAGE = "severSendImage";
    public static final String CLIENT_SEND_IMAGE = "clientSendImage";
    public static final String REGISTER = "register";
    public static final String LOGIN = "login";
    public static final String LOGOUT = "register";
    public static final String JOINROOM = "register";
    public static final String LEAVEROOM = "register";
    public static final String CLIENT_GET_HISTORY_CHAT_ROOM = "clientGetHistoryChatRoom";
    public static final String SEVER_RETURN_HISTORY_CHAT_ROOM = "severReturnHistoryChatRoom";
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
        return "http://192.168.1.98:2409/";
    }
    public static byte[] getByteArrayFromBitmap(Bitmap bm){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
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
}