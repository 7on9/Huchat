package com.vnbamboo.huchat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        getWindow().setStatusBarColor(getColor(R.color.lightGreenColor));
        Button btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                startMainActivity();
            }
        });

        Button btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                startChatActivity();
            }
        });

    }

    public void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }

    public void startChatActivity(){
        Intent intent = new Intent(this, ChatActivity.class);
        this.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Bấm BACK lần nữa để thoát! ", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}
