package com.vnbamboo.huchat;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import static com.vnbamboo.huchat.ServiceConnection.mSocket;
import static com.vnbamboo.huchat.Utility.REGISTER;
import static com.vnbamboo.huchat.Utility.toSHA256;

public class RegisterActivity extends AppCompatActivity {

    Button btnRegister, btnCancel;
    TextView txtUserName, txtPassword, txtRetypePassword, txtEmail;
    CheckBox cbxAcceptPriacy;
    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(getColor(R.color.lightGreenColor));

        setControl();
        addEvent();

    }
    private void setControl(){
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtPassword = (TextView) findViewById(R.id.txtPassword);
        txtRetypePassword = (TextView) findViewById(R.id.txtReTypePassword);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
    }

    private void addEvent(){
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                mSocket.emit(REGISTER,  txtUserName.getText().toString(), toSHA256(txtPassword.getText().toString()), txtEmail.getText().toString());
                resetText();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                onBackPressed();
            }
        });
    }

    private void resetText(){
        txtUserName.setText("");
        txtPassword.setText("");
        txtRetypePassword.setText("");
        txtEmail.setText("");
    }
}

