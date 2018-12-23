package com.vnbamboo.huchat;

import com.vnbamboo.huchat.fragment.GroupFragment;
import com.vnbamboo.huchat.fragment.MessageFragment;
import com.vnbamboo.huchat.fragment.ProfileFragment;
import com.vnbamboo.huchat.helper.BottomNavigationBehavior;
import com.vnbamboo.huchat.object.User;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;


import static com.vnbamboo.huchat.ServiceConnection.mSocket;
import static com.vnbamboo.huchat.ServiceConnection.thisUser;
import static com.vnbamboo.huchat.Utility.CLIENT_REQUEST_LIST_MEMBER_OF_ROOM;
import static com.vnbamboo.huchat.Utility.LIST_ALL_USER;
import static com.vnbamboo.huchat.Utility.CLIENT_REQUEST_IMAGE_ROOM;
import static com.vnbamboo.huchat.Utility.CLIENT_REQUEST_IMAGE_USER;
import static com.vnbamboo.huchat.Utility.CLIENT_REQUEST_LIST_ROOM;
import static com.vnbamboo.huchat.Utility.LIST_ROOM;
import static com.vnbamboo.huchat.Utility.TIME_WAIT_MEDIUM;
import static com.vnbamboo.huchat.Utility.TIME_WAIT_SHORT;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

//        mSocket.emit(CLIENT_REQUEST_IMAGE_USER, thisUser.getUserName());
        if(LIST_ALL_USER.get(0).getAvatar() == null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < LIST_ALL_USER.size(); i++) {
                        final int a = i;
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                mSocket.emit(CLIENT_REQUEST_IMAGE_USER, LIST_ALL_USER.get(a).getUserName());
                            }
                        }).start();
                    }
                }
            }).start();
        }
        else {
            for (User u : LIST_ALL_USER){
                if(u.getUserName().equals(thisUser.getUserName())){
                    thisUser.setAvatar(u.getAvatar());
                    break;
                }
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                mSocket.emit(CLIENT_REQUEST_LIST_ROOM, thisUser.getUserName());
            }
        }).start();

        try {
            new Thread().sleep(TIME_WAIT_MEDIUM);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < LIST_ROOM.size(); i++) {
                    final int a = i;
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            mSocket.emit(CLIENT_REQUEST_IMAGE_ROOM, LIST_ROOM.get(a).getRoomCode());
                            mSocket.emit(CLIENT_REQUEST_LIST_MEMBER_OF_ROOM, LIST_ROOM.get(a).getRoomCode());
                        }
                    }).start();
                }
            }
        }).start();

        try {
            new Thread().sleep(TIME_WAIT_SHORT);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        try {
//            new Thread().sleep(TIME_WAIT_MEDIUM);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        // attaching bottom sheet behaviour - hide / show on scroll
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) navigation.getLayoutParams();
        layoutParams.setBehavior(new BottomNavigationBehavior());

        // load the store fragment by default
        loadFragment(new MessageFragment());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.navigation_gifts:
                    fragment = new MessageFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_cart:
                    fragment = new GroupFragment();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_profile:
                    fragment = new ProfileFragment();
                    loadFragment(fragment);
                    return true;
            }

            return false;
        }
    };

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onDestroy() {
//        Intent intent = new Intent(MainActivity.this, ServiceConnection.class);
//        this.stopService(intent);
        super.onDestroy();
    }
}


