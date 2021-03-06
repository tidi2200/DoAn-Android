package com.example.doan;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Message extends AppCompatActivity {

    RecyclerView recyclerview;
    UserAdapter userAdapter;
    List<User> lstUser;

    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;

    List<ChatList> userList;
    DatabaseReference reference;
    Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.Message);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Contact:
                        startActivity(new Intent(getApplicationContext(), Contact.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Message:
                        return true;
                    case R.id.Setting:
                        startActivity(new Intent(getApplicationContext(), Setting.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        recyclerview = findViewById(R.id.revMessage);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        Log.d("getUid",firebaseUser.getUid());

        userList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("ChatList").child(firebaseUser.getUid());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    ChatList chatList = ds.getValue(ChatList.class);
                    userList.add(chatList);
                }

                chatList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void chatList() {
        lstUser = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("user");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                lstUser.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    User user = ds.getValue(User.class);
                    for(ChatList chatList : userList){
                        if(user.getId().equals(chatList.getId())){
                            lstUser.add(user);
                        }
                    }
                    userAdapter = new UserAdapter(mContext, lstUser,true);
                    recyclerview.setAdapter(userAdapter);
                }
                userAdapter = new UserAdapter(getApplicationContext(), lstUser, true);
                recyclerview.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("user").child(firebaseUser.getUid());
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
    }

}