package com.vnbamboo.huchat.fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.vnbamboo.huchat.R;
import com.vnbamboo.huchat.helper.ServiceConnection;
import com.vnbamboo.huchat.object.ResultFromServer;
import com.vnbamboo.huchat.helper.Utility;
import com.vnbamboo.huchat.object.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.vnbamboo.huchat.helper.ServiceConnection.mSocket;
import static com.vnbamboo.huchat.helper.ServiceConnection.resultFromServer;
import static com.vnbamboo.huchat.helper.ServiceConnection.thisUser;
import static com.vnbamboo.huchat.helper.ServiceConnection.tmpListChat;
import static com.vnbamboo.huchat.helper.Utility.LIST_ALL_PUBLIC_ROOM;
import static com.vnbamboo.huchat.helper.Utility.LIST_ALL_USER;
import static com.vnbamboo.huchat.helper.Utility.LIST_NAME_USER;
import static com.vnbamboo.huchat.helper.Utility.LIST_ROOM_OF_THIS_USER;
import static com.vnbamboo.huchat.helper.Utility.LOGOUT;
import static com.vnbamboo.huchat.helper.Utility.MAP_ALL_PUBLIC_ROOM;
import static com.vnbamboo.huchat.helper.Utility.MAP_ALL_USER;
import static com.vnbamboo.huchat.helper.Utility.MAP_ROOM_OF_THIS_USER;

public class ProfileFragment extends Fragment {

    Button btnLogout, btnEditProfile;
    TextView txtUserName, txtFullName, txtEmail;
    CircleImageView imgAvatar;
    View v;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(thisUser.getAvatar() != null)
            imgAvatar.setImageBitmap(thisUser.getAvatar());
        else imgAvatar.setImageResource(R.mipmap.squareiconhuchat);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profile, container, false);

        setControl();
        addEvent();

        return v;
    }

    private void setControl(){
        btnLogout = (Button) v.findViewById(R.id.btnLogout);
        btnEditProfile = (Button) v.findViewById(R.id.btnEditProfile);
        txtFullName = (TextView) v.findViewById(R.id.txtFullName);
        txtFullName.setText(thisUser.getUserName());
        txtEmail = (TextView) v.findViewById(R.id.txtEmail);
        txtEmail.setText(thisUser.getEmail());

        imgAvatar = (CircleImageView) v.findViewById(R.id.imgAvatar);

        if(thisUser.getAvatar() != null)
            imgAvatar.setImageBitmap(thisUser.getAvatar());
        else imgAvatar.setImageResource(R.mipmap.squareiconhuchat);
    }
    private void addEvent(){
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                showAlertDialog();
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Utility.startEditProfileActivity(v.getContext());
            }
        });
    }
    public void showAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Huchat");
        builder.setMessage("Bạn có muốn đăng xuất không?");
        builder.setCancelable(false);
        builder.setPositiveButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton("Đăng xuất", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mSocket.emit(LOGOUT, thisUser.getUserName());

                thisUser = new User();

                MAP_ROOM_OF_THIS_USER.clear();
                MAP_ALL_USER.clear();
                MAP_ALL_PUBLIC_ROOM.clear();
                LIST_ROOM_OF_THIS_USER.clear();
                LIST_ALL_PUBLIC_ROOM.clear();
                LIST_NAME_USER.clear();
                LIST_ALL_USER.clear();

                LIST_ALL_PUBLIC_ROOM.add(null);
                resultFromServer = new ResultFromServer();
                tmpListChat = new ArrayList<>();
                Intent intent = new Intent(ProfileFragment.super.getContext(), ServiceConnection.class);
//                mSocket.close();
                ProfileFragment.super.getActivity().stopService(intent);
//                mSocket.disconnect();
//                mSocket.io().reconnection();
                Utility.startLoginActivity(v.getContext());
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }
}

