package com.vnbamboo.huchat.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.vnbamboo.huchat.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateNewGroupActivity extends AppCompatActivity {
    Button btnBack, btnCreate;
    CircleImageView imgAvatar;
    TextInputEditText txtPassword, txtGroupName, txtRetypePassword;
    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_create_new_group);
    }
    void addControl(){
        btnBack = (Button) findViewById(R.id.btnBack);
        btnCreate = (Button) findViewById(R.id.btnCreate);
        txtPassword = findViewById(R.id.txtPassword);
        txtRetypePassword = findViewById(R.id.txtReTypePassword);
        txtGroupName = findViewById(R.id.txtGroupName);
        imgAvatar = findViewById(R.id.imgAvatar);

    }
}
