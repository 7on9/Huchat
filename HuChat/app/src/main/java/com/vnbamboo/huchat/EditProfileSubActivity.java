package com.vnbamboo.huchat;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.NumberFormat;

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

        btnChange = findViewById(R.id.btnChange);
        btnBack = findViewById(R.id.btnBack);
        txtEditProperty = findViewById(R.id.txtEditProperty);
        txtChangeTo = findViewById(R.id.txtChangeTo);
        txtRetypeChange = findViewById(R.id.txtReType);

        switch (editProperty){
            case "Full name":
                property = "tên hiển thị";
                txtChangeTo.setInputType(InputType.TYPE_CLASS_TEXT);
                txtChangeTo.setFilters(new InputFilter[] {
                        new InputFilter.LengthFilter(20)
                });
                txtRetypeChange.setInputType(InputType.TYPE_CLASS_TEXT);
                txtRetypeChange.setFilters(new InputFilter[] {
                        new InputFilter.LengthFilter(20)
                });
            break;
            case "Mail":
                property = "mail";
                txtChangeTo.setInputType(InputType.TYPE_CLASS_TEXT);
                txtChangeTo.setFilters(new InputFilter[] {
                        new InputFilter.LengthFilter(50)
                });
                txtRetypeChange.setInputType(InputType.TYPE_CLASS_TEXT);
                txtRetypeChange.setFilters(new InputFilter[] {
                        new InputFilter.LengthFilter(50)
                });
                break;
            case "Phone":
                property = "số điện thoại";

                txtChangeTo.setInputType(InputType.TYPE_CLASS_NUMBER);
                txtChangeTo.setFilters(new InputFilter[] {
                        new RegexInputFilter("^[0-9]+"),
                        new InputFilter.LengthFilter(10)
                });

                txtRetypeChange.setInputType(InputType.TYPE_CLASS_NUMBER);
                txtRetypeChange.setFilters(new InputFilter[] {
                        new RegexInputFilter("^[0-9]+"),
                        new InputFilter.LengthFilter(10)
                });
                break;
            case "Password":
                property = "mật khẩu";
                txtChangeTo.setTransformationMethod(PasswordTransformationMethod.getInstance());
                txtChangeTo.setFilters(new InputFilter[] {
                        new InputFilter.LengthFilter(32)
                });
                txtRetypeChange.setTransformationMethod(PasswordTransformationMethod.getInstance());
                txtRetypeChange.setFilters(new InputFilter[] {
                        new InputFilter.LengthFilter(32)
                });
                break;
        }



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
