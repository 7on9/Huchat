package com.vnbamboo.huchat.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.vnbamboo.huchat.R;
import com.vnbamboo.huchat.adapter.GroupGridViewAdapter;

public class GroupFragment extends Fragment {
    GridView grdViewGroup;
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
        return v;
    }
}
