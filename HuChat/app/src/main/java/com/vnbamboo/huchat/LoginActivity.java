package com.vnbamboo.huchat;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import static com.vnbamboo.huchat.ServiceConnection.mSocket;
import static com.vnbamboo.huchat.Utility.LOGIN;
import static com.vnbamboo.huchat.Utility.toSHA256;

public class LoginActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    Button btnLogin, btnRegister;
    TextView txtUserName, txtPassword;
    CheckBox cbxRememberPass;

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

        setControl();
        addEvent();
    }

    private void setControl(){
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtPassword = (TextView) findViewById(R.id.txtPassword);
        cbxRememberPass = (CheckBox) findViewById(R.id.cbxRememberPass);
    }

    private void addEvent(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                mSocket.emit(LOGIN, txtUserName.getText().toString(), toSHA256(txtPassword.getText().toString()));
                startMainActivity();
            }
        });

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
        Toast.makeText(this, "Bấm lần nữa để thoát!", Toast.LENGTH_SHORT).show();

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
