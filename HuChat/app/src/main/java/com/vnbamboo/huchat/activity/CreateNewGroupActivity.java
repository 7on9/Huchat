package com.vnbamboo.huchat.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;

import com.vnbamboo.huchat.R;
import com.vnbamboo.huchat.object.Room;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.InputStream;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.vnbamboo.huchat.helper.ServiceConnection.mSocket;
import static com.vnbamboo.huchat.helper.ServiceConnection.resultFromServer;
import static com.vnbamboo.huchat.helper.ServiceConnection.thisUser;
import static com.vnbamboo.huchat.helper.Utility.CREATE_ROOM;
import static com.vnbamboo.huchat.helper.Utility.LIST_ROOM_OF_THIS_USER;
import static com.vnbamboo.huchat.helper.Utility.MAP_ROOM_OF_THIS_USER;
import static com.vnbamboo.huchat.helper.Utility.REQUEST_CHOOSE_PHOTO;
import static com.vnbamboo.huchat.helper.Utility.REQUEST_TAKE_PHOTO;
import static com.vnbamboo.huchat.helper.Utility.TIME_WAIT_MEDIUM;
import static com.vnbamboo.huchat.helper.Utility.TIME_WAIT_SHORT;
import static com.vnbamboo.huchat.helper.Utility.getByteArrayFromBitmap;
import static com.vnbamboo.huchat.helper.Utility.resize;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class CreateNewGroupActivity extends AppCompatActivity {
    Button btnBack, btnCreate;
    CircleImageView imgAvatar;
    Bitmap img = null;
    ImageView imgView;
    byte[] imgTemp;
    Switch stwIsPrivate;
    TextInputEditText txtPassword, txtGroupName, txtRetypePassword;
    LayoutInflater inflater;
    AlertDialog.Builder dialogBuilder;
    AlertDialog alertDialog;

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_create_new_group);
        setControl();
        addEvent();
    }
    void setControl(){
        btnBack = findViewById(R.id.btnBack);
        btnCreate = findViewById(R.id.btnCreate);
        txtPassword = findViewById(R.id.txtPassword);
        txtRetypePassword = findViewById(R.id.txtReTypePassword);
        txtGroupName = findViewById(R.id.txtGroupName);
        imgAvatar = findViewById(R.id.imgAvatar);
        stwIsPrivate = findViewById(R.id.stwIsPrivate);
        inflater = this.getLayoutInflater();
    }
    void addEvent(){
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                onBackPressed();
            }
        });

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                dialogBuilder = new AlertDialog.Builder(v.getContext());
                @SuppressLint("ResourceType") View dialogView = inflater.inflate(R.layout.dialog_edit_profile_image_layout, (ViewGroup) findViewById(R.layout.activity_edit_profile));

                imgView = dialogView.findViewById(R.id.imgAvatar);
                imgView.setImageResource(R.mipmap.squareiconhuchat);

                Button btnChooseImage = dialogView.findViewById(R.id.btnChooseImage);
                Button btnTakeImage = dialogView.findViewById(R.id.btnTakeImage);
                Button btnSave = dialogView.findViewById(R.id.btnSave);

                btnChooseImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick( View v ) {
                        choosePicture();
                    }
                });

                btnTakeImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick( View v ) {
                        takePicture();
                    }
                });

                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick( final View v ) {
                        if (img != null) {
                            imgTemp = getByteArrayFromBitmap(img);
                            imgAvatar.setImageBitmap(img);
                        }
                    }
                });
                dialogBuilder.setView(dialogView);
//                dialogBuilder.setTitle("Chỉnh ảnh đại diện");
                alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        });
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                alertDialog.dismiss();
                if(txtGroupName.length() == 0){
                    txtGroupName.setError("Cần đặt tên cho nhóm");
                    return;
                }
                if(!txtPassword.getText().toString().equals(txtRetypePassword.getText().toString())){
                    txtRetypePassword.setError("Mật khẩu nhập lại phải trùng nhau");
                    return;
                }
                else {
                    JSONObject packCreateGroup = new JSONObject();
                    try {
                        packCreateGroup.put("userName", thisUser.getUserName());
                        packCreateGroup.put("roomName", txtGroupName.getText().toString());
                        packCreateGroup.put("password", txtPassword.getText().toString());
                        packCreateGroup.put("isPrivate", stwIsPrivate.isChecked() ? TRUE : FALSE);
                        packCreateGroup.put("imgRoom", imgTemp);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    mSocket.emit(CREATE_ROOM, packCreateGroup);
                    final Dialog dialog = new ProgressDialog(v.getContext());
                    dialog.show();
                    dialog.setTitle("Đang tạo nhóm chat...");
                    dialog.setContentView(R.layout.loading_layout);

                    while (!resultFromServer.event.equals(CREATE_ROOM)){
                    //nothing
                    }
                    if(!resultFromServer.success) {
                        dialog.setContentView(R.layout.fail_layout);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                            }
                        },TIME_WAIT_MEDIUM);
                    }
                    try {
                        new Thread().sleep(TIME_WAIT_SHORT);
                    } catch (InterruptedException e) {
                        dialog.dismiss();
                        e.printStackTrace();
                    }
                    dialog.setTitle("Tạo nhóm thành công!");
                    dialog.setContentView(R.layout.success_layout);

                    Room room = new Room(resultFromServer.args.toString(), txtGroupName.getText().toString());
                    room.getListMember().put(thisUser.getUserName(), thisUser);
                    room.setDual(false);
                    room.setAvatar(img);

                    LIST_ROOM_OF_THIS_USER.add(room);
                    MAP_ROOM_OF_THIS_USER.put(room.getRoomCode(), room);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            dialog.dismiss();
                            onBackPressed();
                        }
                    },TIME_WAIT_MEDIUM);
                }
            }
        });
    }
    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {
        Boolean success = false;
        if (requestCode == REQUEST_CHOOSE_PHOTO && resultCode == RESULT_OK) {
            try {
                Uri imageURI = data.getData();
                InputStream is = getContentResolver().openInputStream(imageURI);
                Bitmap bm = BitmapFactory.decodeStream(is);
                bm = resize(bm, 300, 300);
                img = bm;
                success = true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            bitmap = resize(bitmap, 300, 300);
            img = bitmap;
            success = true;
        }
        if (success)
            imgView.setImageBitmap(img);
    }
    private void takePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_TAKE_PHOTO);
    }

    private void choosePicture() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CHOOSE_PHOTO);
    }
}
