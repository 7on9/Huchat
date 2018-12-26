package com.vnbamboo.huchat;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import static android.widget.Toast.LENGTH_SHORT;
import static com.vnbamboo.huchat.ServiceConnection.mSocket;
import static com.vnbamboo.huchat.ServiceConnection.resultFromServer;
import static com.vnbamboo.huchat.ServiceConnection.thisUser;
import static com.vnbamboo.huchat.Utility.CHANGE_DOB;
import static com.vnbamboo.huchat.Utility.CHANGE_FULL_NAME;
import static com.vnbamboo.huchat.Utility.CHANGE_GENDER;
import static com.vnbamboo.huchat.Utility.CHANGE_MAIL;
import static com.vnbamboo.huchat.Utility.CHANGE_PASSWORD;
import static com.vnbamboo.huchat.Utility.CHANGE_PHONE;
import static com.vnbamboo.huchat.Utility.TIME_WAIT_MEDIUM;
import static com.vnbamboo.huchat.Utility.toSHA256;

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
        txtPassword = findViewById(R.id.txtPassword);
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

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                if(txtPassword.length() == 0){
                    txtPassword.setError("Ô không được để trống!");
                    return;
                }
                if(txtChangeTo.length() == 0){
                    txtPassword.setError("Ô không được để trống!");
                    return;
                }
                if(txtRetypeChange.length() == 0){
                    txtPassword.setError("Ô không được để trống!");
                    return;
                }
                if(!txtRetypeChange.getText().toString().equals(txtChangeTo.getText().toString())){
                    txtRetypeChange.setError("Bạn cần nhập 2 ô thay đổi khớp với nhau ");
                }
                if(toSHA256(txtPassword.getText().toString()).equals(thisUser.getPassword())){
                    switch (editProperty){
                        case "Full name":
                            mSocket.emit(CHANGE_FULL_NAME, thisUser.getUserName(), txtChangeTo.getText().toString());
                            break;
                        case "Mail":
                            mSocket.emit(CHANGE_MAIL, thisUser.getUserName(), txtChangeTo.getText().toString());
                            break;
                        case "Phone":
                            mSocket.emit(CHANGE_PHONE, thisUser.getUserName(), txtChangeTo.getText().toString());
                            break;
                        case "Password":
                            mSocket.emit(CHANGE_PASSWORD, thisUser.getUserName(), toSHA256(txtChangeTo.getText().toString()));
                            break;
                    }
                    try {
                        new Thread().sleep(TIME_WAIT_MEDIUM);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    switch (resultFromServer.event){
                        case CHANGE_MAIL:
                            if(resultFromServer.success){
                                thisUser.setEmail(txtChangeTo.getText().toString());
                                Toast.makeText(v.getContext(),"Thay đổi thành công!", LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(v.getContext(),"Thay đổi thất bại!", LENGTH_SHORT).show();
                            }
                            break;
                        case CHANGE_PASSWORD:
                            if(resultFromServer.success){
                                thisUser.setPassword(toSHA256(txtChangeTo.getText().toString()));
                                Toast.makeText(v.getContext(),"Thay đổi thành công!", LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(v.getContext(),"Thay đổi thất bại!", LENGTH_SHORT).show();
                            }
                            break;
                        case CHANGE_FULL_NAME:
                            if(resultFromServer.success){
                                thisUser.setFullName(txtChangeTo.getText().toString());
                                Toast.makeText(v.getContext(),"Thay đổi thành công!", LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(v.getContext(),"Thay đổi thất bại!", LENGTH_SHORT).show();
                            }
                            break;
                        case CHANGE_PHONE:
                            if(resultFromServer.success){
                                thisUser.setPhone(txtChangeTo.getText().toString());
                                Toast.makeText(v.getContext(),"Thay đổi thành công!", LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(v.getContext(),"Thay đổi thất bại!", LENGTH_SHORT).show();
                            }
                            break;
                        case CHANGE_GENDER:
                        if(resultFromServer.success){
                            }
                            else {

                            }
                        break;
                        case CHANGE_DOB:
                            if(resultFromServer.success){

                            }
                            else {

                            }
                            break;
                    }
                }
            }
        });
    }
}
