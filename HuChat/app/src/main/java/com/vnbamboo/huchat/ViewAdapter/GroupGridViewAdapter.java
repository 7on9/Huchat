package com.vnbamboo.huchat.ViewAdapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vnbamboo.huchat.R;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.vnbamboo.huchat.Utility.LIST_ROOM;

public class GroupGridViewAdapter extends BaseAdapter {
    private LayoutInflater mLayoutInflater;
    public GroupGridViewAdapter( Context context ){
        mLayoutInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return LIST_ROOM == null ? 0 : LIST_ROOM.size();
    }

    @Override
    public Object getItem( int position ) {
        return LIST_ROOM.get(position);
    }

    @Override
    public long getItemId( int position ) {
        return 0;
    }

    @Override
    public View getView( int position, View convertView, ViewGroup parent ) {
        GroupViewItem groupViewItem;
        if(convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.card_group_layout, parent, false);
            groupViewItem = new GroupViewItem(convertView);

            Bitmap  img = LIST_ROOM.get(position).getAvatar();
            if (img != null)
                groupViewItem.imgAvatar.setImageBitmap(img);
            groupViewItem.txtRoomCode.setText(LIST_ROOM.get(position).getRoomCode());
            groupViewItem.txtRoomName.setText(LIST_ROOM.get(position).getName());
            convertView.setTag(groupViewItem);
        }
        else {
            groupViewItem = (GroupViewItem) convertView.getTag();
        }
        return convertView;
    }
    private class GroupViewItem{
        TextView txtRoomName, txtRoomCode;
        CircleImageView imgAvatar;

        GroupViewItem (View convertView){
            txtRoomName = convertView.findViewById(R.id.txtRoomName);
            txtRoomCode = convertView.findViewById(R.id.txtRoomCode);
            imgAvatar = convertView.findViewById(R.id.imgAvatar);
        }

    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }
}
