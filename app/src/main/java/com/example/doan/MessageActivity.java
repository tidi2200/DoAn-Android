package com.example.doan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doan.Notification.Client;
import com.example.doan.Notification.Data;
import com.example.doan.Notification.MyResponse;
import com.example.doan.Notification.Sender;
import com.example.doan.Notification.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//Conversation của mỗi user
public class MessageActivity extends AppCompatActivity {
    CircleImageView profile_image;
    TextView username;

    FirebaseUser fuser;
    DatabaseReference reference;

    ImageButton btn_send;
    EditText edt_message;

    MessageAdapter messageAdapter;
    List<Chat> lstChat;

    RecyclerView recyclerView;

    Intent intent;
    String userid;

    ValueEventListener seenListener;

    APIService apiService;
    boolean notify = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usermessage);
        Toolbar myChildToolbar =
                (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myChildToolbar);
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });

        recyclerView = findViewById(R.id.rev_messagelist);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        profile_image = findViewById(R.id.profile_image);
        username = findViewById(R.id.username);
        btn_send = findViewById(R.id.btn_send);
        edt_message = findViewById(R.id.text_send);

        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        intent= getIntent();
        userid = intent.getStringExtra("userid");

        fuser = FirebaseAuth.getInstance().getCurrentUser(); //Lấy thông tin của user hiện hành

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                notify = true;
                String msg = edt_message.getText().toString();
                if (!msg.equals("")) {
                    sendMessage(fuser.getUid(),userid,msg);
                } else Toast.makeText(MessageActivity.this,"Khong the gui",Toast.LENGTH_LONG).show();
                edt_message.setText("");

            }
        });

        //Lấy thông tin của receiver và hiển thị message list khi dữ liệu thay đổi sẽ được cập nhật

        reference = FirebaseDatabase.getInstance().getReference("user").child(userid);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                username.setText(user.getUsername());
                if(user.getImageURL().equals("default")){
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                }
                else{
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image);
                }

                DisplayMessage(fuser.getUid(), userid, user.getImageURL());
//                messageAdapter.notifyDataSetChanged(); //Làm mới lại recyclerview để realtime theo database
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        seenMessage(userid);
    }

    private void seenMessage(final String userid) {
        reference = FirebaseDatabase.getInstance().getReference("chat");
        seenListener = reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    Chat chat;
                    chat = snapshot.getValue(Chat.class);

                    if (chat.getReceiver().equals(fuser.getUid()) && chat.getSender().equals(userid)) {

                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isseen", true);
                        snapshot.getRef().updateChildren(hashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(String sender, String receiver, String message) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("sender",sender);
        hashMap.put("receiver",receiver);
        hashMap.put("message",message);
        hashMap.put("isseen", false);

        reference.child("chat").push().setValue(hashMap);


        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference("ChatList")
                .child(fuser.getUid())
                .child(userid);

        chatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(!snapshot.exists()){
                    chatRef.child("id").setValue(userid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final String msg = message;

        reference = FirebaseDatabase.getInstance().getReference("user").child(fuser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if(notify){
                    sendNotification(receiver, user.getUsername(), msg);
                }else
                    notify = false;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void sendNotification(String receiver, final String username, final String message){
        DatabaseReference tokens = FirebaseDatabase.getInstance().getReference("Tokens");
        Query query = tokens.orderByKey().equalTo(receiver);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    Token token = ds.getValue(Token.class);
                    Data data = new Data(fuser.getUid(), R.drawable.logo, username+": "+message, "Tin nhắn mới",userid);

                    Sender sender = new Sender(data, token.getToken());

                    apiService.sendNotification(sender)
                            .enqueue(new Callback<MyResponse>() {
                                @Override
                                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                                    if(response.code() == 200){
                                        if(response.body().getSuccess() != 1){
                                            Toast.makeText(MessageActivity.this,"Failed",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }
                                @Override
                                public void onFailure(Call<MyResponse> call, Throwable t) {
                                }
                            });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void DisplayMessage(String myid, String userid, String imageurl) {
        lstChat = new ArrayList<>();

        reference = FirebaseDatabase.getInstance().getReference("chat");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstChat.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {

//                    String sender = ds.child("sender").getValue(String.class);
//                    String receiver = ds.child("receiver").getValue(String.class);
//                    String message = ds.child("message").getValue(String.class);

                    Chat chat = ds.getValue(Chat.class);
                    if (chat.getReceiver().equals(myid) && chat.getSender().equals(userid) ||
                            chat.getReceiver().equals(userid) && chat.getSender().equals(myid)) {
                        lstChat.add(chat);
                    }
                }
                messageAdapter = new MessageAdapter(MessageActivity.this,lstChat,imageurl);
                recyclerView.setAdapter(messageAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("user").child(fuser.getUid());
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("status",status);
        reference.updateChildren(hashMap);
    }

    @Override
    protected void onResume() {
        super.onResume();
        status("online");
    }

    @Override
    protected void onPause() {
        super.onPause();
        status("offline");
        reference.removeEventListener(seenListener);
    }
}
