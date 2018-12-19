package com.vnbamboo.huchat.ViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vnbamboo.huchat.R;
import com.vnbamboo.huchat.object.User;

import java.util.ArrayList;
import java.util.List;

public class FriendListViewAdapter extends BaseAdapter {

    private List<User> listData = new ArrayList<User>();
    private Context context;
    private LayoutInflater layoutInflater;

    public FriendListViewAdapter(Context aContext) {
        this.context = aContext;
        layoutInflater = LayoutInflater.from(aContext);
    }

    public void add(User user) {
        this.listData.add(user);
        notifyDataSetChanged(); // to render the list we need to notify
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem( int position ) {
        return listData.get(position);
    }

    @Override
    public long getItemId( int position ) {
        return position;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {
        User user = listData.get(position);
        FriendViewHolder LIST_ALL_USERViewHolder = new FriendViewHolder();
        LIST_ALL_USERViewHolder.avatar = convertView.findViewById(R.id.imgViewAvatar);
        LIST_ALL_USERViewHolder.user = convertView.findViewById(R.id.txtUserName);
        return convertView;
    }
}

class FriendViewHolder{
    TextView user;
    ImageView avatar;
}
