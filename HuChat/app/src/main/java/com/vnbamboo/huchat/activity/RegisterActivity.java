package com.vnbamboo.huchat.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.InputFilter;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.vnbamboo.huchat.R;
import com.vnbamboo.huchat.helper.RegexInputFilter;

import static com.vnbamboo.huchat.helper.ServiceConnection.mSocket;
import static com.vnbamboo.huchat.helper.ServiceConnection.resultFromServer;
import static com.vnbamboo.huchat.helper.Utility.REGISTER;
import static com.vnbamboo.huchat.helper.Utility.TIME_WAIT_LONG;
import static com.vnbamboo.huchat.helper.Utility.TIME_WAIT_MEDIUM;
import static com.vnbamboo.huchat.helper.Utility.startLoginActivity;
import static com.vnbamboo.huchat.helper.Utility.toSHA256;

public class RegisterActivity extends AppCompatActivity {

    Button btnRegister, btnCancel;
    TextView txtUserName, txtPassword, txtRetypePassword, txtEmail;
    CheckBox cbxAcceptPriacy;
    Context thisContext = this;
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
        btnCancel = findViewById(R.id.btnCancel);
        btnRegister = findViewById(R.id.btnRegister);
        txtUserName = findViewById(R.id.txtUserName);
        txtPassword = findViewById(R.id.txtPassword);
        txtRetypePassword = findViewById(R.id.txtReTypePassword);
        txtEmail = findViewById(R.id.txtEmail);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addEvent() {

        txtUserName.setFilters(new InputFilter[] {
                new RegexInputFilter("^[a-zA-Z0-9_]+"),
                new InputFilter.LengthFilter(20)
        });

        txtUserName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction( TextView v, int actionId, KeyEvent event ) {
                if (txtUserName.getText().length() <= 5) {
                    txtUserName.setError("Tên tài khoản phải từ 6 - 20 ký tự");
                }
                return false;
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                if(txtPassword.length()*txtEmail.length()*txtUserName.length()*txtRetypePassword.length() == 0){
                    Toast.makeText(thisContext, "Hãy điền hết tất cả các trường!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!txtPassword.getText().toString().equals(txtRetypePassword.getText().toString())){
                    Toast.makeText(thisContext, "Mật khẩu gõ lại không trùng khớp!", Toast.LENGTH_SHORT).show();
                    return;
                }
                mSocket.emit(REGISTER, txtUserName.getText().toString(), toSHA256(txtPassword.getText().toString()), txtEmail.getText().toString());
                final ProgressDialog dialog = new ProgressDialog(thisContext);
                dialog.setTitle("Đang tiến hành...");
                dialog.setContentView(R.layout.loading_layout);
                dialog.show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (resultFromServer.event.equals(REGISTER) && resultFromServer.success) {
                            resetText();
                            Toast.makeText(thisContext, "Đăng ký thành công! Sẽ chuyển đến màn hình đăng nhập", Toast.LENGTH_SHORT).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startLoginActivity(thisContext);
                                }
                            }, TIME_WAIT_LONG);
                        } else
                            Toast.makeText(thisContext, "Có lỗi khi đăng ký! Xin hãy thử lại!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }, TIME_WAIT_MEDIUM);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                onBackPressed();
            }
        });

        txtPassword.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch( View v, MotionEvent event ) {
                switch ( event.getAction() ) {
                    case MotionEvent.ACTION_UP:
                        txtPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                        txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        break;
                    case MotionEvent.ACTION_DOWN:
                        txtPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_black_24dp, 0);
                        txtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                return false;
            }
        });
        txtRetypePassword.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch( View v, MotionEvent event ) {
                switch ( event.getAction() ) {
                    case MotionEvent.ACTION_UP:
                        txtRetypePassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                        txtRetypePassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        break;
                    case MotionEvent.ACTION_DOWN:
                        txtRetypePassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_black_24dp, 0);
                        txtRetypePassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                return false;
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

