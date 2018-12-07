package com.vnbamboo.huchat;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

        Intent intent = new Intent(LoginActivity.this, ServiceConnection.class);
        if(!ServiceConnection.isConnected)
            this.stopService(intent);
        this.startService(intent);

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
                startRegisterActivity();
            }
        });

    }

    public void startMainActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }

    public void startRegisterActivity(){
        Intent intent = new Intent(this, RegisterActivity.class);
        this.startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Bấm BACK lần nữa để thoát! ", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(LoginActivity.this, ServiceConnection.class);
        this.stopService(intent);
        super.onDestroy();
    }
}
