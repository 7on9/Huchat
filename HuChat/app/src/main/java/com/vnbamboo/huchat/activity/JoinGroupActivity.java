package com.vnbamboo.huchat.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;

import com.vnbamboo.huchat.R;
import com.vnbamboo.huchat.helper.RegexInputFilter;
import com.vnbamboo.huchat.helper.Utility;
import com.vnbamboo.huchat.object.Room;

import static com.vnbamboo.huchat.helper.ServiceConnection.mSocket;
import static com.vnbamboo.huchat.helper.ServiceConnection.resultFromServer;
import static com.vnbamboo.huchat.helper.ServiceConnection.thisUser;
import static com.vnbamboo.huchat.helper.Utility.CLIENT_REQUEST_IMAGE_ROOM;
import static com.vnbamboo.huchat.helper.Utility.JOIN_EXIST_ROOM;
import static com.vnbamboo.huchat.helper.Utility.MAP_ROOM_OF_THIS_USER;
import static com.vnbamboo.huchat.helper.Utility.TIME_WAIT_LONG;

public class JoinGroupActivity extends AppCompatActivity {
    Room room = null;
    Button btnBack, btnJoin;
    TextInputEditText txtPassword, txtRoomCode;
    Context mContext = this;
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
        txtRoomCode.setFilters(new InputFilter[] {
                new RegexInputFilter("^[a-zA-Z0-9]+"),
                new InputFilter.LengthFilter(41)
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                onBackPressed();
            }
        });

        btnJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                room = MAP_ROOM_OF_THIS_USER.get(txtRoomCode.getText().toString().toLowerCase());
                if(room == null){
                    mSocket.emit(JOIN_EXIST_ROOM, txtRoomCode.getText().toString().toLowerCase(), thisUser.getUserName(), txtPassword.getText());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(resultFromServer.event.equals(JOIN_EXIST_ROOM)){
                                if(resultFromServer.success){
                                    room = MAP_ROOM_OF_THIS_USER.get(txtRoomCode.getText().toString().toLowerCase());
                                    mSocket.emit(CLIENT_REQUEST_IMAGE_ROOM, txtRoomCode.getText().toString());
                                    Utility.startChatActivity(mContext, room.getName(), room.getRoomCode());
                                }
                                else {
                                    txtRoomCode.setError("Phòng không tồn tại");
                                }
                            }
                        }
                    }, TIME_WAIT_LONG);
                }
                else {
                    Utility.startChatActivity(v.getContext(), room.getName(), room.getRoomCode());
                }
            }
        });
    }
}
