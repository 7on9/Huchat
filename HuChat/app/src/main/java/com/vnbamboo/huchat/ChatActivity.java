package com.vnbamboo.huchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.vnbamboo.huchat.RecyclerViewAdapter.ChatMessage;
import com.vnbamboo.huchat.RecyclerViewAdapter.ChatMessageListViewAdapter;

public class ChatActivity extends AppCompatActivity{
    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

//        final List<ChatMessage> messageList = new ArrayList<>();
//        ChatMessage a = new ChatMessage("", "");
//        messageList.add(a);


        final ChatMessageListViewAdapter chatMessageListViewAdapter = new ChatMessageListViewAdapter(this);

        final ListView lstChatMessage = (ListView) findViewById(R.id.lstChatMessage);
        lstChatMessage.setAdapter(chatMessageListViewAdapter);
        Intent intent = getIntent();
        String userName = intent.getStringExtra("UserName");
        getSupportActionBar().setTitle(userName);
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
            }
        });

    }
}
