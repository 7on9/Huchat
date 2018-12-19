package com.vnbamboo.huchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class EditProfileSubActivity extends AppCompatActivity {
    Button btnChange, btnBack;
    TextInputEditText txtPassword, txtChangeTo, txtRetypeChange;
    TextView txtEditProperty;
    Intent intent;
    String editProperty;
    String property = "";
    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sub_activity_edit_profile);
        getSupportActionBar().hide();
        intent = getIntent();
        editProperty = intent.getStringExtra("Edit property");
        setControl();
        addEvent();
    }

    void setControl(){
        switch (editProperty){
            case "Full name":
                property = "tên hiển thị";
            break;
            case "Mail":
                property = "mail";
                break;
            case "Phone":
                property = "số điện thoại";
                break;
            case "Password":
                property = "mật khẩu";
                break;
        }

        btnChange = findViewById(R.id.btnChange);
        btnBack = findViewById(R.id.btnBack);
        txtEditProperty = findViewById(R.id.txtEditProperty);
        txtChangeTo = findViewById(R.id.txtChangeTo);
        txtRetypeChange = findViewById(R.id.txtReType);

        //set basic view
        txtEditProperty.setText(property);

    }
    void addEvent(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                onBackPressed();
            }
        });
        txtChangeTo.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange( View v, boolean hasFocus ) {
                if(txtChangeTo.hasFocus()){
                    txtChangeTo.setHint("Nhập "+ property + " mới");
                }
                else {
                    txtChangeTo.setHint("");
                }
            }
        });
        txtRetypeChange.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange( View v, boolean hasFocus ) {
                if(txtRetypeChange.hasFocus()){
                    txtRetypeChange.setHint("Nhập lại "+ property);
                }
                else {
                    txtRetypeChange.setHint("");
                }
            }
        });
    }
}
