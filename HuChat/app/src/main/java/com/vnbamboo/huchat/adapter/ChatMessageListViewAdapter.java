package com.vnbamboo.huchat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vnbamboo.huchat.R;
import com.vnbamboo.huchat.object.ChatMessage;
import com.vnbamboo.huchat.object.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.vnbamboo.huchat.helper.ServiceConnection.thisUser;
import static com.vnbamboo.huchat.helper.Utility.LIST_ALL_USER;

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

    public void add(ChatMessage txtMessage) {
        this.listData.add(txtMessage);
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
        final MessageViewHolder messageListViewHolder = new MessageViewHolder();

        Date date = new Date(chatMessage.getTime() * 1000); // convert seconds to milliseconds
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd/MM "); // the format of your date
        String formattedDate = dateFormat.format(date);


        if (chatMessage.getUserNameSender().equals(thisUser.getUserName())) {
            convertView = layoutInflater.inflate(R.layout.my_message, null);
            messageListViewHolder.txtMessage = (TextView) convertView.findViewById(R.id.txtMyMessage);
            messageListViewHolder.txtTime = convertView.findViewById(R.id.txtTime);
            messageListViewHolder.line = convertView.findViewById(R.id.line);
            convertView.setTag(messageListViewHolder);
            messageListViewHolder.txtMessage.setText(chatMessage.getContent());


            messageListViewHolder.txtTime.setText(formattedDate);
        } else {
            convertView = layoutInflater.inflate(R.layout.their_message, null);
            messageListViewHolder.txtMessage = (TextView) convertView.findViewById(R.id.txtTheirMessage);
            messageListViewHolder.txtUser = (TextView) convertView.findViewById(R.id.txtTheirName);
            messageListViewHolder.imgAvatar = (CircleImageView) convertView.findViewById(R.id.imgAvatar);
            messageListViewHolder.txtTime = convertView.findViewById(R.id.txtTime);
            messageListViewHolder.line = convertView.findViewById(R.id.line);

            for(User u : LIST_ALL_USER){
                if(u.getUserName().equals(chatMessage.getUserNameSender()))
                    messageListViewHolder.imgAvatar.setImageBitmap(u.getAvatar());
            }
            messageListViewHolder.txtMessage.setText(chatMessage.getContent());
            messageListViewHolder.txtUser.setText(chatMessage.getUserNameSender());
            messageListViewHolder.txtTime.setText(formattedDate);
            convertView.setTag(messageListViewHolder);
        }

        messageListViewHolder.txtMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                if(messageListViewHolder.txtTime.getVisibility() == View.INVISIBLE)
                    messageListViewHolder.txtTime.setVisibility(View.VISIBLE);
                else messageListViewHolder.txtTime.setVisibility(View.INVISIBLE);
            }
        });

        messageListViewHolder.line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                if(messageListViewHolder.txtTime.getVisibility() == View.INVISIBLE)
                    messageListViewHolder.txtTime.setVisibility(View.VISIBLE);
                else messageListViewHolder.txtTime.setVisibility(View.INVISIBLE);
            }
        });
        return convertView;
    }
    class MessageViewHolder{
        TextView txtMessage;
        TextView txtUser;
        TextView txtTime;
        CircleImageView imgAvatar;
        RelativeLayout line;
    }
}


