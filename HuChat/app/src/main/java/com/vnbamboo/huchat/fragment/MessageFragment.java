package com.vnbamboo.huchat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vnbamboo.huchat.Utility;
import com.vnbamboo.huchat.ViewAdapter.MessageRecyclerViewAdapter;
import com.vnbamboo.huchat.OnLoadMoreListener;
import com.vnbamboo.huchat.R;
import com.vnbamboo.huchat.object.Room;
import com.vnbamboo.huchat.object.User;

import java.util.ArrayList;
import java.util.List;

import static com.vnbamboo.huchat.ServiceConnection.mSocket;
import static com.vnbamboo.huchat.ServiceConnection.thisUser;

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
//    private List<User> listTempData = new ArrayList<>();
//    private List<User> listData = new ArrayList<>();
    private List<Room> listData = new ArrayList<>();
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private User tempUser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_message, container, false);

        recyclerView = (RecyclerView) v.findViewById(R.id.rclViewCardMessage);

        FloatingActionButton btnCreateNewMessage = (FloatingActionButton) v.findViewById(R.id.btnCreateNewMessage);
        btnCreateNewMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                Utility.startCreateNewMessageActivity(v.getContext(),"s", tempUser);
             }
        });

        mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);

        bounData();

        return v;
    }

    public void bounData(){
        listData.removeAll(listData);
        listData.addAll(thisUser.getRoomList());

        final MessageRecyclerViewAdapter recyclerViewAdapter = new MessageRecyclerViewAdapter(recyclerView,this, listData);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                size = thisUser.getRoomList().size() + 7;
                if(listData.size() <= size){
                    listData.add(null); //if listData[i] == null -> loading view
                    recyclerViewAdapter.notifyItemInserted(listData.size()-1);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            listData.remove(listData.size()-1);
                            recyclerViewAdapter.notifyItemRemoved(listData.size());

                            //random data
//                            createRandom10User();
                            recyclerViewAdapter.notifyDataSetChanged();
                            recyclerViewAdapter.setLoaded();
                        }
                    },1000);
                }else{
                    Toast.makeText(getContext(), "Đã tải hết các liên hệ gần đây!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

//    void createRandom10User(){
//        for(int i = 1; i <= 10; i++){
//            int x = (int) (Math.random()*10)%4;
//            tempUser = new User((User) listTempData.get(x));
//            listData.add(tempUser);
//        }
//    }
}
