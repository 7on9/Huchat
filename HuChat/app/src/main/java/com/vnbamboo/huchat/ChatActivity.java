package com.vnbamboo.huchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.vnbamboo.huchat.object.ChatMessage;
import com.vnbamboo.huchat.RecyclerViewAdapter.ChatMessageListViewAdapter;

public class ChatActivity extends AppCompatActivity{
    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportActionBar().hide();
//        final List<ChatMessage> messageList = new ArrayList<>();
//        ChatMessage a = new ChatMessage("", "");
//        messageList.add(a);

        Button btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                ChatActivity.super.onBackPressed();
            }
        });

        final ChatMessageListViewAdapter chatMessageListViewAdapter = new ChatMessageListViewAdapter(this);

        final ListView lstChatMessage = (ListView) findViewById(R.id.lstChatMessage);
        lstChatMessage.setAdapter(chatMessageListViewAdapter);

        Intent intent = getIntent();

        String userName = intent.getStringExtra("UserName");
        TextView txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtUserName.setText(userName);

        final EditText txtMessage = (EditText) findViewById(R.id.txtMessage);

        ImageButton btnSendMessage = (ImageButton) findViewById(R.id.btnSendMessage);
        btnSendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                ChatMessage a = new ChatMessage("", txtMessage.getText().toString());
                chatMessageListViewAdapter.add(a);
                lstChatMessage.setSelection(lstChatMessage.getCount()-1);
                a = new ChatMessage("Rem","Welcome home! Master!");
                chatMessageListViewAdapter.add(a);
                lstChatMessage.setSelection(lstChatMessage.getCount()-1);
            }
        });
    }
}
