package com.vnbamboo.huchat.ViewAdapter;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.LinearLayout;

import com.vnbamboo.huchat.ChatActivity;
import com.vnbamboo.huchat.OnLoadMoreListener;
import com.vnbamboo.huchat.R;
import com.vnbamboo.huchat.Utility;
import com.vnbamboo.huchat.fragment.MessageFragment;
import com.vnbamboo.huchat.object.Room;
import com.vnbamboo.huchat.object.User;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private MessageFragment mContext;

    public static final int TYPE_LOAD = 0;
    public static final int TYPE_CARD = 1;

    private List<Room> data = new ArrayList<>();
    OnLoadMoreListener loadMore;
    boolean isLoading;
    int visibleThreshold = 5;
    int lastVisibleItem, totalItemCount;

    public MessageRecyclerViewAdapter( RecyclerView recyclerView, MessageFragment mContext, List<Room> data) {
        this.mContext = mContext;
        this.data = data;
        final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalItemCount = linearLayoutManager.getItemCount();
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (loadMore != null) {
                        loadMore.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }


    @Override
    public int getItemViewType(int position) {
        if (data.get(position) == null)
            return TYPE_LOAD;
        return data.get(position) instanceof Room ? TYPE_CARD : TYPE_LOAD;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        final RecyclerView.ViewHolder holder;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view;
        // switch case -> connect to layout
        switch (viewType) {
            case TYPE_CARD:
                view = inflater.inflate(R.layout.card_message_layout, parent, false);
                return new CardMessageViewHolder(view);
            default:
                view = inflater.inflate(R.layout.loading_layout, parent, false);
                holder = new LoadingViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CardMessageViewHolder) {
            CardMessageViewHolder temp = (CardMessageViewHolder) holder;
            Room room = (Room) data.get(position);
            temp.bindData(room);
        } else {
            LoadingViewHolder temp = (LoadingViewHolder) holder;
            temp.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void setOnLoadMoreListener(OnLoadMoreListener mOnLoadMoreListener) {
        this.loadMore = mOnLoadMoreListener;
    }

    public class CardMessageViewHolder extends RecyclerView.ViewHolder {
        TextView lastMessage, userName;
        CircleImageView imgAvatar;
        LinearLayout line;

        public CardMessageViewHolder(View itemView) {
            super(itemView);

            imgAvatar = itemView.findViewById(R.id.imgViewAvatar);
            userName = itemView.findViewById(R.id.txtCardName);
            lastMessage = itemView.findViewById(R.id.txtLastMessage);
            line = itemView.findViewById(R.id.line);

            line.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Utility.startChatActivity(v.getContext(),(String) userName.getText());
                    // line.setBackgroundColor(R.color.colorAccent);
                }
            });
        }

        void bindData(Room room) {
            this.userName.setText(room.getName());
            this.lastMessage.setText(room.getRoomCode());
            this.imgAvatar.setImageBitmap(room.getAvatar());
        }

    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

    public interface OnItemClickedListener {
        void onItemClick(String username);
    }

    private OnItemClickedListener onItemClickedListener;

    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
        this.onItemClickedListener = onItemClickedListener;
    }

}