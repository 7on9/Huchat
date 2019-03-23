package com.vnbamboo.huchat.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vnbamboo.huchat.R;
import com.vnbamboo.huchat.activity.JoinGroupActivity;
import com.vnbamboo.huchat.fragment.GroupFragment;
import com.vnbamboo.huchat.helper.Utility;
import com.vnbamboo.huchat.object.Room;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.vnbamboo.huchat.helper.ServiceConnection.mSocket;
import static com.vnbamboo.huchat.helper.ServiceConnection.resultFromServer;
import static com.vnbamboo.huchat.helper.ServiceConnection.thisUser;
import static com.vnbamboo.huchat.helper.Utility.JOIN_EXIST_ROOM;
import static com.vnbamboo.huchat.helper.Utility.LIST_ALL_PUBLIC_ROOM;
import static com.vnbamboo.huchat.helper.Utility.LIST_ROOM_OF_THIS_USER;
import static com.vnbamboo.huchat.helper.Utility.MAP_ROOM_OF_THIS_USER;
import static com.vnbamboo.huchat.helper.Utility.TIME_WAIT_LONG;

public class GroupGridViewAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;

    public GroupGridViewAdapter( Context context ) {
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return LIST_ALL_PUBLIC_ROOM == null ? 0 : LIST_ALL_PUBLIC_ROOM.size();
    }

    @Override
    public Room getItem( int position ) {
        return LIST_ALL_PUBLIC_ROOM.get(position);
    }

    @Override
    public long getItemId( int position ) {
        return position;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {
        GroupViewItem groupViewItem;
        Room room = getItem(position);
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.card_group_layout, parent, false);
            groupViewItem = new GroupViewItem(convertView);
            convertView.setTag(groupViewItem);
        } else {
            groupViewItem = (GroupViewItem) convertView.getTag();
        }
        groupViewItem.bindData(room);
        return convertView;
    }
    private class GroupViewItem {
        TextView txtRoomName, txtRoomCode;
        CircleImageView imgAvatar;
        LinearLayout item;

        GroupViewItem( View convertView ) {
            txtRoomName = convertView.findViewById(R.id.txtRoomName);
            txtRoomCode = convertView.findViewById(R.id.txtRoomCode);
            imgAvatar = convertView.findViewById(R.id.imgAvatar);
            item = convertView.findViewById(R.id.group);
        }

        void bindData(final Room room){
            Bitmap img = room.getAvatar();
            if (img != null) {
                this.imgAvatar.setImageBitmap(img);
            }

            this.txtRoomCode.setText(room.getRoomCode());
            this.txtRoomName.setText(room.getName());

            this.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick( final View v ) {
                    if(MAP_ROOM_OF_THIS_USER.get(room.getRoomCode()) == null) {
                        mSocket.emit(JOIN_EXIST_ROOM, room.getRoomCode(), thisUser.getUserName(), "");
                        final ProgressDialog dialog = new ProgressDialog(v.getContext());
                        dialog.show();
                        dialog.setTitle("Đợi 1 chút nhé...");
                        dialog.setContentView(R.layout.loading_layout);

                        try {
                            new Thread().sleep(TIME_WAIT_LONG);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                dialog.dismiss();
                                if(resultFromServer.event.equals(JOIN_EXIST_ROOM) && resultFromServer.success){
                                    MAP_ROOM_OF_THIS_USER.put(room.getRoomCode(), room);
                                    LIST_ROOM_OF_THIS_USER.add(room);
                                    Utility.startChatActivity(v.getContext(), room.getName(), room.getRoomCode());
                                }
                                else {

                                }
                            }
                        }).start();
                    }else {
                        Utility.startChatActivity(v.getContext(), room.getName(), room.getRoomCode());
                    }
                }
            });
        }
    }
}
