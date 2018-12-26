package com.vnbamboo.huchat.ViewAdapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vnbamboo.huchat.ChatActivity;
import com.vnbamboo.huchat.R;
import com.vnbamboo.huchat.Utility;
import com.vnbamboo.huchat.object.User;

import java.util.logging.Handler;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.vnbamboo.huchat.ServiceConnection.mSocket;
import static com.vnbamboo.huchat.ServiceConnection.thisUser;
import static com.vnbamboo.huchat.Utility.JOIN_DUAL_ROOM;
import static com.vnbamboo.huchat.Utility.LIST_ALL_USER;
import static com.vnbamboo.huchat.Utility.TIME_WAIT_LONG;

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
    public void onBindViewHolder( @NonNull RecyclerView.ViewHolder viewHolder, final int i ) {
        UserViewHolder userViewHolder = (UserViewHolder) viewHolder;
        final User user = LIST_ALL_USER.get(i);
        if(user.getAvatar() != null)
            userViewHolder.imgAvatar.setImageBitmap(user.getAvatar());
        else {
            userViewHolder.imgAvatar.setImageResource(R.mipmap.squareiconhuchat);
        }
        userViewHolder.txtUserName.setText(user.getUserName());
        userViewHolder.txtFullName.setText(user.getFullName());
        userViewHolder.line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( final View v ) {
                final String roomCode = (thisUser.getUserName().compareToIgnoreCase(user.getUserName()) < 0) ?
                        thisUser.getUserName().toLowerCase().concat("#" + user.getUserName().toLowerCase()) :
                        user.getUserName().toLowerCase().concat("#" + thisUser.getUserName().toLowerCase());
                mSocket.emit(JOIN_DUAL_ROOM, thisUser.getUserName(), user.getUserName());

                final ProgressDialog dialog = new ProgressDialog(v.getContext());
                dialog.setTitle("Đợi 1 chút nhé...");
                dialog.setContentView(R.layout.loading_layout);
                dialog.show();

                try {
                    new Thread().sleep(TIME_WAIT_LONG);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        Utility.startChatActivity(v.getContext(), user.getFullName(), roomCode);
                    }
                }).start();
            }
        });
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
        TextView txtUserName, txtFullName;
        CircleImageView imgAvatar;
        LinearLayout line;
        public UserViewHolder( @NonNull View itemView ) {
            super(itemView);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtFullName = itemView.findViewById(R.id.txtFullName);
            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            line = itemView.findViewById(R.id.friendLine);
        }

    }
}

