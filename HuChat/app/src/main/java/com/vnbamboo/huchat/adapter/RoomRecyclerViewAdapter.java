package com.vnbamboo.huchat.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.LinearLayout;

import com.vnbamboo.huchat.helper.OnLoadMoreListener;
import com.vnbamboo.huchat.R;
import com.vnbamboo.huchat.helper.Utility;
import com.vnbamboo.huchat.fragment.MessageFragment;
import com.vnbamboo.huchat.object.Room;
import com.vnbamboo.huchat.object.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.vnbamboo.huchat.helper.ServiceConnection.mSocket;
import static com.vnbamboo.huchat.helper.ServiceConnection.thisUser;
import static com.vnbamboo.huchat.helper.Utility.JOIN_ROOM;

public class RoomRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private MessageFragment mContext;

    public static final int TYPE_LOAD = 0;
    public static final int TYPE_CARD = 1;

    private List<Room> data = new ArrayList<>();
    OnLoadMoreListener loadMore;
    boolean isLoading;
    int visibleThreshold = 5;
    int lastVisibleItem, totalItemCount;

    public RoomRecyclerViewAdapter( RecyclerView recyclerView, MessageFragment mContext, List<Room> data) {
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
                view = inflater.inflate(R.layout.card_room_layout, parent, false);
                return new CardRoomViewHolder(view);
            default:
                view = inflater.inflate(R.layout.loading_layout, parent, false);
                holder = new LoadingViewHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CardRoomViewHolder) {
            CardRoomViewHolder temp = (CardRoomViewHolder) holder;
            Room room = data.get(position);
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

    public class CardRoomViewHolder extends RecyclerView.ViewHolder {
        TextView txtRoomCode, roomName, txtRoomType;
        CircleImageView imgAvatar;
        LinearLayout line;
        String roomCode;

        public CardRoomViewHolder( View itemView ) {
            super(itemView);

            imgAvatar = itemView.findViewById(R.id.imgAvatar);
            roomName = itemView.findViewById(R.id.txtCardName);
            txtRoomCode = itemView.findViewById(R.id.txtRoomCode);
            txtRoomType = itemView.findViewById(R.id.txtType);
            line = itemView.findViewById(R.id.line);

            line.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick( View v ) {
                    mSocket.emit(JOIN_ROOM, roomCode);
                    Utility.startChatActivity(v.getContext(), (String) roomName.getText(), roomCode);
                    // line.setBackgroundColor(R.color.colorAccent);
                }
            });
        }

        void bindData( Room room ) {
            this.txtRoomCode.setText("Mã phòng : " + room.getRoomCode());
            this.roomCode = room.getRoomCode();
            if (!room.isDual()) {
                this.roomName.setText(room.getName());
                this.txtRoomType.setText("Chat nhóm");
                if (room.getAvatar() != null)
                    this.imgAvatar.setImageBitmap(room.getAvatar());
                else
                    this.imgAvatar.setImageResource(R.mipmap.squareiconhuchat);
            } else {
                for (Map.Entry<String, User> entry : room.getListMember().entrySet()) {
                    if (!entry.getValue().getUserName().equals(thisUser.getUserName())) {
                        this.roomName.setText(entry.getValue().getFullName());
                        this.imgAvatar.setImageBitmap(entry.getValue().getAvatar());
                    }
                }
                this.txtRoomType.setText("Chat đôi");
            }
            if(room.getRoomCode().equals((thisUser.getUserName().toLowerCase().concat("#"+ thisUser.getUserName().toLowerCase())))){
                this.roomName.setText(thisUser.getFullName());
                this.imgAvatar.setImageBitmap(thisUser.getAvatar());
            }
        }
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        public ProgressBar progressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressBar);
        }
    }

//    public interface OnItemClickedListener {
//        void onItemClick(String username);
//    }
//
//    private OnItemClickedListener onItemClickedListener;
//
//    public void setOnItemClickedListener(OnItemClickedListener onItemClickedListener) {
//        this.onItemClickedListener = onItemClickedListener;
//    }

}