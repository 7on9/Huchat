package com.vnbamboo.huchat.ViewAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vnbamboo.huchat.R;
import com.vnbamboo.huchat.object.ChatMessage;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.vnbamboo.huchat.ServiceConnection.thisUser;

public class ChatMessageListViewAdapter extends BaseAdapter{

    private List<ChatMessage> listData = new ArrayList<ChatMessage>();
    private Context context;
    private LayoutInflater layoutInflater;

    public ChatMessageListViewAdapter(Context aContext) {
        this.context = aContext;
        layoutInflater = LayoutInflater.from(aContext);
    }

    public ChatMessageListViewAdapter(Context mConText, List<ChatMessage> chatMessageList){
        this.context = mConText;
        layoutInflater = LayoutInflater.from(mConText);
        listData = chatMessageList;
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
        if (chatMessage.getUserNameSender().equals(thisUser.getUserName())) {
            convertView = layoutInflater.inflate(R.layout.my_message, null);
            messageListViewHolder.message = (TextView) convertView.findViewById(R.id.txtMyMessage);

            convertView.setTag(messageListViewHolder);
            messageListViewHolder.message.setText(chatMessage.getContent());
        } else {
            convertView = layoutInflater.inflate(R.layout.their_message, null);
            messageListViewHolder.message = (TextView) convertView.findViewById(R.id.txtTheirMessage);
            messageListViewHolder.user = (TextView) convertView.findViewById(R.id.txtTheirName);
            messageListViewHolder.avatar = (CircleImageView) convertView.findViewById(R.id.imgViewAvatar);

            messageListViewHolder.avatar.setImageBitmap(thisUser.getAvatar());
            messageListViewHolder.message.setText(chatMessage.getContent());
            messageListViewHolder.user.setText(chatMessage.getUserNameSender());
            convertView.setTag(messageListViewHolder);

        }
        return convertView;
    }
}

class MessageViewHolder{
    TextView message;
    TextView user;
    CircleImageView avatar;
}
