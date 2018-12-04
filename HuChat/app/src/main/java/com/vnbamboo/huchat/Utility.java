package com.vnbamboo.huchat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.vnbamboo.huchat.object.User;

import java.io.Serializable;

public class Utility {
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
}
