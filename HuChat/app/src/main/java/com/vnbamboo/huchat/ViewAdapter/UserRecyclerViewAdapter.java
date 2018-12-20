package com.vnbamboo.huchat.ViewAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vnbamboo.huchat.R;
import com.vnbamboo.huchat.object.User;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.vnbamboo.huchat.Utility.LIST_ALL_USER;

public class UserRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

//    private List<User> listData = new ArrayList<User>();
    private Context context;
    private LayoutInflater layoutInflater;

    public UserRecyclerViewAdapter(Context aContext) {
        this.context = aContext;
        layoutInflater = LayoutInflater.from(aContext);
    }

    public void add(User user) {
        LIST_ALL_USER.add(user);
        notifyDataSetChanged(); // to render the list we need to notify
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder( @NonNull ViewGroup viewGroup, int i ) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view;

        view = inflater.inflate(R.layout.card_user_layout, viewGroup, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder( @NonNull RecyclerView.ViewHolder viewHolder, int i ) {
        UserViewHolder userViewHolder = (UserViewHolder) viewHolder;
        User user = LIST_ALL_USER.get(i);
        if(user.getAvatar() != null)
            userViewHolder.imgAvatar.setImageBitmap(user.getAvatar());
        else {
            userViewHolder.imgAvatar.setImageResource(R.mipmap.squareiconhuchat);
        }
        userViewHolder.txtUserName.setText(user.getUserName());
    }

    @Override
    public long getItemId( int position ) {
        return position;
    }

    @Override
    public int getItemCount() {
        return LIST_ALL_USER.size();
    }
    public class UserViewHolder extends RecyclerView.ViewHolder{
        TextView txtUserName;
        CircleImageView imgAvatar;

        public UserViewHolder( @NonNull View itemView ) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            imgAvatar = itemView.findViewById(R.id.imgViewAvatar);
        }
    }
}

