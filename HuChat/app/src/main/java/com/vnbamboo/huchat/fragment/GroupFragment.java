package com.vnbamboo.huchat.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vnbamboo.huchat.R;
import com.vnbamboo.huchat.object.User;

import java.util.ArrayList;
import java.util.List;

import static com.vnbamboo.huchat.Utility.LIST_ALL_USER;

public class GroupFragment extends Fragment {
    List<String> listNameUser = new ArrayList<>();
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
        for(User user: LIST_ALL_USER){
             listNameUser.add(user.getUserName());
        }

        return inflater.inflate(R.layout.fragment_group, container, false);
    }
}
