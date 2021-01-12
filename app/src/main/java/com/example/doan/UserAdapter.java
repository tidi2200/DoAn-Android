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

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private Context context;
    private List<User> lstUser;

    public UserAdapter(Context context, List<User> mUser) {
        this.context = context;
        lstUser = mUser;
    }
//    public UserAdapter(List<User> mUser) {
//
//        lstUser = mUser;
//    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserAdapter.UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = lstUser.get(position);
        holder.username.setText(user.getUsername());
        if (user.getImageURL().equals("default")) {
            holder.profile_img.setImageResource(R.mipmap.ic_launcher);
        } else {
            holder.profile_img.setImageResource(R.mipmap.ic_launcher);
        }

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(context, MessageActivity.class);
//                intent.putExtra("userid", user.getId());
//                context.startActivity(intent);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return lstUser.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public ImageView profile_img;


        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.txt_username);
            profile_img = itemView.findViewById(R.id.imageView_user);
        }
    }
}
