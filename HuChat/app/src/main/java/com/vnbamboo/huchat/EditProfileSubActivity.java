package com.vnbamboo.huchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class EditProfileSubActivity extends AppCompatActivity {
    Button btnChange, btnBack;
    TextInputEditText txtPassword, txtChange, txtRetypeChange;
    Intent intent = getIntent();
    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_activity_edit_profile);

    }

    void setControl(){
        btnChange = findViewById(R.id.btnChange);
        btnBack = findViewById(R.id.btnBack);
    }
    void addEvent(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                onBackPressed();
            }
        });
    }
}
