package com.vnbamboo.huchat.RecyclerViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vnbamboo.huchat.R;
import com.vnbamboo.huchat.object.ChatMessage;

import java.util.ArrayList;
import java.util.List;

public class ChatMessageListViewAdapter extends BaseAdapter{

    private List<ChatMessage> listData = new ArrayList<ChatMessage>();
    private Context context;
    private LayoutInflater layoutInflater;

    public ChatMessageListViewAdapter(Context aContext) {
        this.context = aContext;
        layoutInflater = LayoutInflater.from(aContext);
    }

    public void add(ChatMessage message) {
        this.listData.add(message);
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
        ChatMessage chatMessage = listData.get(position);
        MessageViewHolder messageListViewHolder = new MessageViewHolder();
        if (chatMessage.getFrom() == "") {
            convertView = layoutInflater.inflate(R.layout.my_message, null);
            messageListViewHolder.message = (TextView) convertView.findViewById(R.id.txtMyMessage);
            convertView.setTag(messageListViewHolder);
            messageListViewHolder.message.setText(chatMessage.getValue());
        } else {
            convertView = layoutInflater.inflate(R.layout.their_message, null);
            messageListViewHolder.message = (TextView) convertView.findViewById(R.id.txtTheirMessage);
            messageListViewHolder.user = (TextView) convertView.findViewById(R.id.txtTheirName);

            messageListViewHolder.message.setText(chatMessage.getValue());
            messageListViewHolder.user.setText(chatMessage.getFrom());
            convertView.setTag(messageListViewHolder);

        }
        return convertView;
    }
}

class MessageViewHolder{
    TextView message;
    TextView user;
    ImageView avatar;
}
