package com.vnbamboo.huchat.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.vnbamboo.huchat.R;
import com.vnbamboo.huchat.helper.RegexInputFilter;
import com.vnbamboo.huchat.helper.ServiceConnection;
import com.vnbamboo.huchat.object.ResultFromServer;
import com.vnbamboo.huchat.object.Room;

import java.math.RoundingMode;

import static com.vnbamboo.huchat.helper.ServiceConnection.logined;
import static com.vnbamboo.huchat.helper.ServiceConnection.mSocket;
import static com.vnbamboo.huchat.helper.ServiceConnection.resultFromServer;
import static com.vnbamboo.huchat.helper.ServiceConnection.statusConnection;
import static com.vnbamboo.huchat.helper.ServiceConnection.thisUser;
import static com.vnbamboo.huchat.helper.Utility.CLIENT_REQUEST_IMAGE_ROOM;
import static com.vnbamboo.huchat.helper.Utility.CLIENT_REQUEST_LIST_ROOM;
import static com.vnbamboo.huchat.helper.Utility.CLIENT_REQUEST_PUBLIC_INFO_USER;
import static com.vnbamboo.huchat.helper.Utility.LIST_ALL_PUBLIC_ROOM;
import static com.vnbamboo.huchat.helper.Utility.LOGIN;
import static com.vnbamboo.huchat.helper.Utility.TIME_WAIT_LONG;
import static com.vnbamboo.huchat.helper.Utility.TIME_WAIT_MEDIUM;
import static com.vnbamboo.huchat.helper.Utility.TIME_WAIT_SHORT;
import static com.vnbamboo.huchat.helper.Utility.toSHA256;

public class LoginActivity extends AppCompatActivity {

    boolean doubleBackToExitPressedOnce = false;
    Button btnLogin, btnRegister;
    TextView btnForgot;
    TextView txtUserName;
    TextView txtPassword, txtConnectionState;
    CheckBox cbxRememberPass;
    Context thisContext = this;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().hide();

        getWindow().setStatusBarColor(getColor(R.color.lightGreenColor));

        final Intent intent = new Intent(LoginActivity.this, ServiceConnection.class);

//        if(ServiceConnection.isConnected)
//            this.stopService(intent);
        new Thread(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!ServiceConnection.isConnected)
                            thisContext.startService(intent);
                        if(resultFromServer == null)
                            resultFromServer = new ResultFromServer();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        mSocket.emit(CLIENT_REQUEST_LIST_ROOM);
                                        mSocket.emit(CLIENT_REQUEST_PUBLIC_INFO_USER);
                                    }
                                });
                            }
                        }).start();
                    }
                });
            }
        }).start();


        setControl();
        addEvent();

//        Room room = null;
//        LIST_ALL_PUBLIC_ROOM.add(null);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(resultFromServer.success)
                    txtConnectionState.setCompoundDrawablesWithIntrinsicBounds(0,0, R.mipmap.bullet_green, 0);
                else
                    txtConnectionState.setCompoundDrawablesWithIntrinsicBounds(0,0, R.mipmap.bullet_red, 0);
            }
        }, TIME_WAIT_SHORT);
    }

    private void setControl(){
        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        btnForgot = (TextView) findViewById(R.id.btnForgot);
        txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtPassword = (TextView) findViewById(R.id.txtPassword);
        txtConnectionState = (TextView) findViewById(R.id.txtConnectionState);
        cbxRememberPass = (CheckBox) findViewById(R.id.cbxRememberPass);
    }

    private void addEvent(){
//        if(resultFromServer.success)
//            txtConnectionState.setCompoundDrawablesWithIntrinsicBounds(0,0, R.mipmap.bullet_green, 0);
//        else
//            txtConnectionState.setCompoundDrawablesWithIntrinsicBounds(0,0, R.mipmap.bullet_red, 0);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                if(!ServiceConnection.isConnected) {
                    Intent intent = new Intent(LoginActivity.this, ServiceConnection.class);
                    LoginActivity.super.startService(intent);
                }
                if (statusConnection)
                    txtConnectionState.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.bullet_green, 0);
                else
                    txtConnectionState.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.mipmap.bullet_red, 0);

                if(txtUserName.length()*txtPassword.length() == 0){
                    Toast.makeText(view.getContext(), "Các ô không được để trống!", Toast.LENGTH_SHORT).show();
                    return;
                }
                for (int i = 0; i < LIST_ALL_PUBLIC_ROOM.size(); i++) {
                    final int a = i;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mSocket.emit(CLIENT_REQUEST_IMAGE_ROOM, LIST_ALL_PUBLIC_ROOM.get(a).getRoomCode());
//                            mSocket.emit(CLIENT_REQUEST_LIST_MEMBER_OF_ROOM, LIST_ALL_PUBLIC_ROOM.get(a).getRoomCode());
//                            mSocket.emit(CLIENT_REQUEST_HISTORY_CHAT_ROOM, LIST_ALL_PUBLIC_ROOM.get(a).getRoomCode());
                        }
                    }).start();
                }
                if (!statusConnection) {
                    Toast.makeText(view.getContext(), "Không thể kết nối đến server! Hãy kiểm tra lại kết nối mạng!", Toast.LENGTH_SHORT).show();
                    return;
                }
                resultFromServer.event = "";
                mSocket.emit(LOGIN, txtUserName.getText().toString(), toSHA256(txtPassword.getText().toString()));
                final ProgressDialog dialog = new ProgressDialog(thisContext);
                dialog.setTitle("Đang đăng nhập...");
                dialog.setContentView(R.layout.loading_layout);
                dialog.show();
//                dialog.cancel();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (logined) {
                            thisUser.setPassword( toSHA256(txtPassword.getText().toString()));
                            savingPreferences();
                            dialog.dismiss();
                            startMainActivity();
                        } else {
                            dialog.dismiss();
                            Toast.makeText(thisContext, "Sai tên đăng nhập hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, TIME_WAIT_LONG);
            }
        });

        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                startMainActivity();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                startRegisterActivity();
            }
        });

        txtUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged( CharSequence s, int start, int count, int after ) {

            }

            @Override
            public void onTextChanged( CharSequence s, int start, int before, int count ) {
                restoringPreferences();
            }

            @Override
            public void afterTextChanged( Editable s ) {

            }
        });

        txtUserName.setFilters(new InputFilter[] {
                new RegexInputFilter("^[a-zA-Z0-9_]+"),
                new InputFilter.LengthFilter(20)
        });

        txtPassword.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch( View v, MotionEvent event ) {
                switch ( event.getAction() ) {
                    case MotionEvent.ACTION_UP:
                        txtPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_black_24dp, 0, R.drawable.ic_visibility_off_black_24dp, 0);
                        txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        break;
                    case MotionEvent.ACTION_DOWN:
                        txtPassword.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_lock_black_24dp, 0, R.drawable.ic_visibility_black_24dp, 0);
                        txtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                return false;
            }
        });
    }

    public void restoringPreferences()
    {
        SharedPreferences pre = getSharedPreferences(txtUserName.getText().toString().toLowerCase(), MODE_PRIVATE);
        //lấy giá trị checked ra, nếu không thấy thì giá trị mặc định là false
        boolean bchk = pre.getBoolean("remembered", false);
        String pwd = pre.getString("password", "");
        txtPassword.setText(pwd);
        cbxRememberPass.setChecked(bchk);
    }

    public void savingPreferences()
    {
        //tạo đối tượng getSharedPreferences
        SharedPreferences pre=getSharedPreferences(txtUserName.getText().toString().toLowerCase(), MODE_PRIVATE);
        //tạo đối tượng Editor để lưu thay đổi
        SharedPreferences.Editor editor=pre.edit();
        String user = txtUserName.getText().toString();
        String pwd = txtPassword.getText().toString();
        boolean bchk = cbxRememberPass.isChecked();

        if(!bchk)
        {
            //xóa mọi lưu trữ trước đó
            editor.clear();
        }
        else
        {
            //lưu vào editor
            editor.putString("userName", user);
            editor.putString("password", pwd);
            editor.putBoolean("remembered", bchk);
        }
        //chấp nhận lưu xuống file
        editor.commit();
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
        }, TIME_WAIT_LONG);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(LoginActivity.this, ServiceConnection.class);
        if(ServiceConnection.isConnected) {
//            mSocket.off();
            this.stopService(intent);
        }
        if(!ServiceConnection.isConnected)
            this.startService(intent);
        restoringPreferences();
    }

    @Override
    protected void onDestroy() {
        Intent intent = new Intent(LoginActivity.this, ServiceConnection.class);
        //this.stopService(intent);
        super.onDestroy();
    }
}
