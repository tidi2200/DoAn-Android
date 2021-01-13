package com.example.doan;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.LinkedList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    public static final int MSG_LEFT = 0;
    public static final int MSG_RIGHT = 1;

    private Context context;
    private List<Chat> lstChat;
    private String imgURL;

    FirebaseUser fuser;

    public MessageAdapter(Context context, List<Chat> mChat, String avatar) {
        this.context = context;
        lstChat = mChat;
        imgURL = avatar;
    }
//    public UserAdapter(List<User> mUser) {
//
//        lstUser = mUser;
//    }

    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_RIGHT) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.MessageViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.MessageViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) {
        Chat chat = lstChat.get(position);

        holder.show_message.setText(chat.getMessage());
        if (!imgURL.equals("default"))
            holder.profile_img.setImageResource(R.mipmap.ic_launcher);
        else holder.profile_img.setImageResource(R.mipmap.ic_launcher);
    }

    @Override
    public int getItemCount() {
        return lstChat.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder {
        public TextView show_message;
        public ImageView profile_img;


        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.txt_msg);
            profile_img = itemView.findViewById(R.id.profile_image);
        }
    }

    //Ktr tin nhắn là của sender hay receiver rồi thực hiện trên onCreate
    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(lstChat.get(position).getSender().equals(fuser.getUid()))
        {
            return MSG_RIGHT;
        } else return MSG_LEFT;
    }
}