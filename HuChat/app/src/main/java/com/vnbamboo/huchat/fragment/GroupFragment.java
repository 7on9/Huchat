package com.vnbamboo.huchat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.vnbamboo.huchat.R;
import com.vnbamboo.huchat.activity.JoinGroupActivity;
import com.vnbamboo.huchat.adapter.GroupGridViewAdapter;
import com.vnbamboo.huchat.helper.Utility;
import com.vnbamboo.huchat.object.Room;
import com.vnbamboo.huchat.object.User;

import io.github.yavski.fabspeeddial.FabSpeedDial;
import io.github.yavski.fabspeeddial.SimpleMenuListenerAdapter;

import static com.vnbamboo.huchat.helper.ServiceConnection.thisUser;
import static com.vnbamboo.huchat.helper.Utility.LIST_ALL_PUBLIC_ROOM;

public class GroupFragment extends Fragment {
    static GridView grdViewGroup;
    public GroupFragment() {
        // Required empty public constructor
    }

    public static GroupFragment newInstance(String param1, String param2) {
        GroupFragment fragment = new GroupFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_group, container, false);

        grdViewGroup = v.findViewById(R.id.grdViewGroup);

        GroupGridViewAdapter groupGridViewAdapter = new GroupGridViewAdapter(v.getContext());

        grdViewGroup.setAdapter(groupGridViewAdapter);

        FabSpeedDial fabSpeedDial = v.findViewById(R.id.btnMenu);

        fabSpeedDial.setMenuListener(new SimpleMenuListenerAdapter() {
            @Override
            public boolean onMenuItemSelected(MenuItem menuItem) {
                if(menuItem.getTitle().equals("Tạo nhóm")){
                    Utility.startCreateNewGroupActivity(getContext(),"s", new User());
                }else {
                    Utility.startCreateNewMessageActivity(getContext(),"s", new User());
                    Intent intent = new Intent(getContext(), JoinGroupActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
        return v;
    }
}
