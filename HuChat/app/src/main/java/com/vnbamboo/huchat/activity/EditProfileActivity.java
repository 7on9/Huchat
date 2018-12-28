package com.vnbamboo.huchat.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vnbamboo.huchat.R;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static com.vnbamboo.huchat.helper.ServiceConnection.mSocket;
import static com.vnbamboo.huchat.helper.ServiceConnection.resultFromServer;
import static com.vnbamboo.huchat.helper.ServiceConnection.thisUser;
import static com.vnbamboo.huchat.helper.Utility.CLIENT_SEND_IMAGE_USER;
import static com.vnbamboo.huchat.helper.Utility.REQUEST_CHOOSE_PHOTO;
import static com.vnbamboo.huchat.helper.Utility.REQUEST_TAKE_PHOTO;
import static com.vnbamboo.huchat.helper.Utility.TIME_WAIT_MEDIUM;
import static com.vnbamboo.huchat.helper.Utility.getByteArrayFromBitmap;
import static com.vnbamboo.huchat.helper.Utility.resize;

public class EditProfileActivity extends AppCompatActivity {
    Button btnBack, btnChangeFullName,
            btnChangeMail, btnChangeGender,
            btnChangeDob, btnChangePassword,
            btnChangePhone, btnChange;
    ImageView imgAvatar;
    Bitmap img = null;
    LayoutInflater inflater;
    ImageView imgAvatemp;
    Context thisContex = this;
    TextView txtFullName;

    @Override
    protected void onCreate( @Nullable Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().hide();
        getWindow().setStatusBarColor(getColor(R.color.lightestGreenColor));
        inflater = this.getLayoutInflater();
        addControl();
        addEvent();
    }

    private void addControl() {
        btnBack = (Button) findViewById(R.id.btnBack);
        imgAvatar = (ImageView) findViewById(R.id.imgAvatar);
        txtFullName = (TextView) findViewById(R.id.txtFullName);
        btnChangeFullName = findViewById(R.id.btnChangeFullName);
        btnChangeDob = findViewById(R.id.btnChangeDob);
        btnChangeGender = findViewById(R.id.btnChangeGender);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnChangeMail = findViewById(R.id.btnChangeMail);
        btnChangePhone = findViewById(R.id.btnChangePhone);

        imgAvatar.setImageBitmap(thisUser.getAvatar());
        txtFullName.setText(thisUser.getFullName());
    }

    private void addEvent() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                onBackPressed();
            }
        });

        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(v.getContext());
                @SuppressLint("ResourceType") View dialogView = inflater.inflate(R.layout.dialog_edit_profile_image_layout, (ViewGroup) findViewById(R.layout.activity_edit_profile));

                imgAvatemp = dialogView.findViewById(R.id.imgAvatar);
                imgAvatemp.setImageBitmap(thisUser.getAvatar());

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
                    public void onClick( View v ) {
                        if (img != null) {
                            byte[] bytes = getByteArrayFromBitmap(img);
                            mSocket.emit(CLIENT_SEND_IMAGE_USER, bytes);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (resultFromServer.event.equals(CLIENT_SEND_IMAGE_USER)) {
                                        if (resultFromServer.success) {
                                            thisUser.setAvatar(img);
                                            imgAvatar.setImageBitmap(img);
                                            Toast.makeText(thisContex, "Cập nhật ảnh đại diện thành công!", Toast.LENGTH_SHORT).show();
                                        } else
                                            Toast.makeText(thisContex, "Có lỗi khi cập nhật ảnh đại diện!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }, TIME_WAIT_MEDIUM);
                        }
                    }
                });
                dialogBuilder.setView(dialogView);
//                dialogBuilder.setTitle("Chỉnh ảnh đại diện");
                AlertDialog b = dialogBuilder.create();
                b.show();
//                choosePicture();
            }
        });

        btnChangeFullName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                gotoEditProfile("Full name");
            }
        });

        btnChangePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                gotoEditProfile("Phone");
            }
        });

        btnChangeMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                gotoEditProfile("Mail");
            }
        });

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                gotoEditProfile("Password");
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
            imgAvatemp.setImageBitmap(img);
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

    private void gotoEditProfile( String property ) {
        Intent intent = new Intent(thisContex, EditProfileSubActivity.class);
        intent.putExtra("Edit property", property);
        EditProfileActivity.super.startActivity(intent);
    }
}
