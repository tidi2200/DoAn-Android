package com.example.doan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
//    private Context context;
//    private List<User> lstUser;
    LayoutInflater mInflater;
    final LinkedList<User> mDataSet;

    public UserAdapter(Context context, LinkedList<User> mDataSet) {
        this.mDataSet = mDataSet;
        this.mInflater = LayoutInflater.from(context);
    }
//    public UserAdapter(List<User> mUser) {
//
//        lstUser = mUser;
//    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = this.mInflater.inflate(R.layout.user_item, parent, false);
        return new UserViewHolder(view, this);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = this.mDataSet.get(position);
        holder.username.setText(user.getUsername());
        holder.profile_img.setImageResource(R.mipmap.ic_launcher);
//        if (user.getImageURL().equals("default")) {
//            holder.profile_img.setImageResource(R.mipmap.ic_launcher);
//        } else {
//            holder.profile_img.setImageResource(R.mipmap.ic_launcher);
//        }

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
        return this.mDataSet.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        public final TextView username;
        public final ImageView profile_img;
        final UserAdapter mAdapter;
        public UserViewHolder(@NonNull View itemView, final UserAdapter mAdapter) {
            super(itemView);
            this.mAdapter = mAdapter;
            this.username = itemView.findViewById(R.id.txt_usernameitem);
            this.profile_img = itemView.findViewById(R.id.imageView_user);
        }
    }
}
