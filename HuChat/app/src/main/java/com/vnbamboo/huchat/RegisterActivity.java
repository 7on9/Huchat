package com.vnbamboo.huchat;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import static com.vnbamboo.huchat.ServiceConnection.mSocket;
import static com.vnbamboo.huchat.ServiceConnection.resultFromSever;
import static com.vnbamboo.huchat.Utility.REGISTER;
import static com.vnbamboo.huchat.Utility.startLoginActivity;
import static com.vnbamboo.huchat.Utility.toSHA256;

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
        btnCancel = (Button) findViewById(R.id.btnCancel);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtPassword = (TextView) findViewById(R.id.txtPassword);
        txtRetypePassword = (TextView) findViewById(R.id.txtReTypePassword);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
    }

    private void addEvent() {
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                if(!txtPassword.getText().toString().equals(txtRetypePassword.getText().toString())){
                    Toast.makeText(thisContext, "Mật khẩu gõ lại không trùng khớp!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(txtPassword.length()*txtEmail.length()*txtUserName.length()*txtRetypePassword.length() == 0){
                    Toast.makeText(thisContext, "Hãy điền hết tất cả các trường!", Toast.LENGTH_SHORT).show();
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
                        if (resultFromSever.event.equals(REGISTER) && resultFromSever.success) {
                            resetText();
                            Toast.makeText(thisContext, "Đăng ký thành công! Sẽ chuyển đến màn hình đăng nhập", Toast.LENGTH_SHORT).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startLoginActivity(thisContext);
                                }
                            }, 2000);
                        } else
                            Toast.makeText(thisContext, "Có lỗi khi đăng ký! Xin hãy thử lại!", Toast.LENGTH_SHORT).show();
                        dialog.cancel();
                    }
                }, 1000);
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

