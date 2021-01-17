package com.example.doan;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private Context context;
    private List<User> lstUser;
    private boolean ischat;
    String theLastMess;

    public UserAdapter(Context context, List<User> mUser, boolean ischat) {
        this.context = context;
        this.lstUser = mUser;
        this.ischat = ischat;
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
        Log.d("useradapter", user.getImageURL());
        if (user.getImageURL().equals("default"))
            holder.profile_img.setImageResource(R.mipmap.ic_launcher);
        else
            Glide.with(context).load(user.getImageURL()).into(holder.profile_img);

        if (ischat) {
            lastMessage(user.getId(), holder.lastmsg);
        } else {
            holder.lastmsg.setVisibility(View.GONE);
        }

        if (ischat) {
            if (user.getStatus().equals("online")) {
                holder.img_on.setVisibility(View.VISIBLE);
                holder.img_off.setVisibility(View.GONE);
            } else {
                holder.img_on.setVisibility(View.GONE);
                holder.img_off.setVisibility(View.VISIBLE);
            }
        } else {
            holder.img_on.setVisibility(View.GONE);
            holder.img_off.setVisibility(View.GONE);
        }
        //Dùng để thêm event chuyển sang conversation của mỗi user khi user đó được click vào
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MessageActivity.class);
                intent.putExtra("userid", user.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lstUser.size();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView username;
        public TextView lastmsg;
        public ImageView profile_img;
        public ImageView img_on;
        public ImageView img_off;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.txt_username_messageitem);
            lastmsg = itemView.findViewById(R.id.txt_lastmsgitem);
            profile_img = itemView.findViewById(R.id.imageView_user);
            img_on = itemView.findViewById(R.id.img_on);
            img_off = itemView.findViewById(R.id.img_off);
        }
    }

    private void lastMessage(final String userid, final TextView last_msg) {
        theLastMess = "default";
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("chat");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Chat chat = snapshot.getValue(Chat.class);
                    if (chat.getReceiver().equals(firebaseUser.getUid()) && chat.getSender().equals(userid) || chat.getReceiver().equals(userid) &&
                            chat.getSender().equals(firebaseUser.getUid())) {
                        theLastMess = chat.getMessage();
                    }
                }
                switch (theLastMess) {
                    case "default":
                        last_msg.setText("No Message");
                        break;

                    default:
                        last_msg.setText(theLastMess);
                        break;
                }
                theLastMess = "default";
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
