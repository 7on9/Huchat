package com.vnbamboo.huchat.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.vnbamboo.huchat.R;
import com.vnbamboo.huchat.helper.Utility;
import com.vnbamboo.huchat.object.Room;

import static com.vnbamboo.huchat.helper.ServiceConnection.mSocket;
import static com.vnbamboo.huchat.helper.ServiceConnection.resultFromServer;
import static com.vnbamboo.huchat.helper.ServiceConnection.thisUser;
import static com.vnbamboo.huchat.helper.Utility.JOIN_EXIST_ROOM;
import static com.vnbamboo.huchat.helper.Utility.MAP_ROOM_OF_THIS_USER;
import static com.vnbamboo.huchat.helper.Utility.TIME_WAIT_LONG;

public class JoinGroupActivity extends AppCompatActivity {
    Button btnBack, btnJoin;
    TextInputEditText txtPassword, txtRoomCode;

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_join_group);
        setControl();
        addEvent();
    }

    void setControl(){
        btnBack = findViewById(R.id.btnBack);
        btnJoin = findViewById(R.id.btnJoin);
        txtPassword = findViewById(R.id.txtPassword);
        txtRoomCode = findViewById(R.id.txtRoomCode);
    }
    void addEvent(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                onBackPressed();
            }
        });

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Room room = MAP_ROOM_OF_THIS_USER.get(thisUser.getUserName().toLowerCase());
                if(room == null)
                    mSocket.emit(JOIN_EXIST_ROOM, thisUser.getUserName(), txtRoomCode.getText(), txtPassword.getText());
                else {
                    Utility.startChatActivity(v.getContext(), room.getName(), room.getRoomCode());

                }
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(resultFromServer.event.equals(JOIN_EXIST_ROOM)){
                    if(resultFromServer.success){
                        Utility.startChatActivity(v.getContext(), room.getName(), room.getRoomCode());
                    }
                }
            }
        }, TIME_WAIT_LONG);
    }
}
