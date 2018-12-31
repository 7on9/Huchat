package com.vnbamboo.huchat.activity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.vnbamboo.huchat.R;
import com.vnbamboo.huchat.adapter.UserRecyclerViewAdapter;
import com.vnbamboo.huchat.helper.Utility;
import com.vnbamboo.huchat.object.User;

import static com.vnbamboo.huchat.helper.ServiceConnection.mSocket;
import static com.vnbamboo.huchat.helper.ServiceConnection.thisUser;
import static com.vnbamboo.huchat.helper.Utility.JOIN_DUAL_ROOM;
import static com.vnbamboo.huchat.helper.Utility.LIST_NAME_USER;
import static com.vnbamboo.huchat.helper.Utility.MAP_ALL_USER;
import static com.vnbamboo.huchat.helper.Utility.TIME_WAIT_LONG;

public class CreateNewMessageActivity extends AppCompatActivity {
    RecyclerView rclViewCardUser;
    Button btnBack;
    TextView btnStartChat;
    AutoCompleteTextView txtFind;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_create_new_message);

        setControl();
        addEvent();
        mLayoutManager = new LinearLayoutManager(this);
        rclViewCardUser.setLayoutManager(mLayoutManager);
        UserRecyclerViewAdapter adapter = new UserRecyclerViewAdapter(this);
        rclViewCardUser.setAdapter(adapter);

    }
    private void setControl(){
        rclViewCardUser = findViewById(R.id.rclViewCardUser);
        btnBack = findViewById(R.id.btnBack);
        txtFind = findViewById(R.id.txtFind);
        btnStartChat = findViewById(R.id.btnStartChat);
    }
    private void addEvent(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                CreateNewMessageActivity.super.onBackPressed();
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, LIST_NAME_USER);
        txtFind.setThreshold(1);
        txtFind.setAdapter(adapter);

        btnStartChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( final View v ) {
                if(txtFind.getText().length() == 0){
                    txtFind.setError("Hãy nhập tên người dùng");
                    return;
                }

                final User user = MAP_ALL_USER.get(txtFind.getText().toString().toLowerCase());

                if (user == null){
                    txtFind.setError("Không tìm thấy người dùng");
                    return;
                }

                final String roomCode = (thisUser.getUserName().compareToIgnoreCase(user.getUserName()) < 0) ?
                        thisUser.getUserName().toLowerCase().concat("#" + user.getUserName().toLowerCase()) :
                        user.getUserName().toLowerCase().concat("#" + thisUser.getUserName().toLowerCase());
                mSocket.emit(JOIN_DUAL_ROOM, thisUser.getUserName(), user.getUserName());

                final ProgressDialog dialog = new ProgressDialog(v.getContext());
                dialog.setTitle("Đợi 1 chút nhé...");
                dialog.setContentView(R.layout.loading_layout);
                dialog.show();

                try {
                    new Thread().sleep(TIME_WAIT_LONG);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        Utility.startChatActivity(v.getContext(), user.getFullName(), roomCode);
                    }
                }).start();
            }
        });
    }
}
