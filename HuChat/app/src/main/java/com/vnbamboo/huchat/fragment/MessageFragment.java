package com.vnbamboo.huchat.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vnbamboo.huchat.MessageRecyclerViewAdapter;
import com.vnbamboo.huchat.OnLoadMoreListener;
import com.vnbamboo.huchat.R;
import com.vnbamboo.huchat.User;

import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment {

    public MessageFragment() {
        // Required empty public constructor
    }

    public static MessageFragment newInstance( String param1, String param2) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }


    public int size = 40;
    private List<User> listTempData = new ArrayList<>();
    private List<User> listData = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private User tempUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_message, container, false);
        recyclerView = (RecyclerView) v.findViewById(R.id.rclViewCardMessage);
        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        bounData();
        return v;
    }

    public void bounData(){
        tempUser = new User("CR7", "Rô", "6996", "Bồ@gmail.com", "Xào rau muống");
        listTempData.add(tempUser);
        tempUser = new User("M10", "Si", "9699", "Arg","Luộc trứng");
        listTempData.add(tempUser);
        tempUser = new User("N10", "Ney", "9669966", "Bra","Trứng vịt lộn");
        listTempData.add(tempUser);
        tempUser = new User("spc", "Mou", "000010", "MU","Đậu chiên");

        listTempData.add(tempUser);
        listTempData.add(null);

        createRandom10User();

        final MessageRecyclerViewAdapter recyclerViewAdapter = new MessageRecyclerViewAdapter(recyclerView,this, listData);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if(listData.size() <= size){
                    listData.add(null); //if listData[i] == null -> loading view
                    recyclerViewAdapter.notifyItemInserted(listData.size()-1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            listData.remove(listData.size()-1);
                            recyclerViewAdapter.notifyItemRemoved(listData.size());

                            //random data
                            createRandom10User();
                            recyclerViewAdapter.notifyDataSetChanged();
                            recyclerViewAdapter.setLoaded();
                        }
                    },1000);
                }else{
                    Toast.makeText(getContext(), "Load data completed !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void createRandom10User(){
        for(int i = 1; i <= 10; i++){
            int x = (int) (Math.random()*10)%4;
            tempUser = new User((User) listTempData.get(x));
            listData.add(tempUser);
        }
    }
}
