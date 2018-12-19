package com.vnbamboo.huchat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.vnbamboo.huchat.ViewAdapter.UserRecyclerViewAdapter;

public class CreateNewMessageActivity extends AppCompatActivity {
    RecyclerView rclViewCardUser;
    Button btnBack;
    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_create_new_message);

        setControl();
        addEvent();
        UserRecyclerViewAdapter adapter = new UserRecyclerViewAdapter(this);
        rclViewCardUser.setAdapter(adapter);
    }
    private void setControl(){
        rclViewCardUser = findViewById(R.id.rclViewCardUser);
        btnBack = (Button) findViewById(R.id.btnBack);
    }
    private void addEvent(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                CreateNewMessageActivity.super.onBackPressed();
            }
        });
    }
}
