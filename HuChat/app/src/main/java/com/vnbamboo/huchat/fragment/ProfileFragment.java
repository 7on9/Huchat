package com.vnbamboo.huchat.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.vnbamboo.huchat.R;
import com.vnbamboo.huchat.Utility;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.vnbamboo.huchat.ServiceConnection.mSocket;
import static com.vnbamboo.huchat.ServiceConnection.thisUser;
import static com.vnbamboo.huchat.Utility.LOGOUT;

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
        imgAvatar = (CircleImageView) v.findViewById(R.id.imgViewAvatar);
        if(thisUser.getAvatar() != null)
            imgAvatar.setImageBitmap(thisUser.getAvatar());
        else imgAvatar.setImageResource(R.mipmap.squareiconhuchat);
    }
    private void addEvent(){
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                mSocket.emit(LOGOUT, thisUser.getUserName());
                Utility.startLoginActivity(v.getContext());
            }
        });


        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Utility.startEditProfileActivity(v.getContext(), thisUser);
            }
        });
    }
}

