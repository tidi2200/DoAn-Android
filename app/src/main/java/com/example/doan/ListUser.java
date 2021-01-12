package com.example.doan;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ListUser extends AppCompatActivity {

    RecyclerView recyclerView;

    UserAdapter userAdapter;
    LinkedList<User> lstUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reccyclerview_userlist);
        lstUser = new LinkedList<User>();
        recyclerView = findViewById(R.id.rev_listuser);
//        userAdapter = new UserAdapter(this,lstUser);
//        recyclerView.setAdapter(userAdapter);
        lstUser = getAllUser();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public LinkedList<User> getAllUser(){
        LinkedList<User> lstUser = new LinkedList<>();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        DatabaseReference usersdRef = rootRef.child("user");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String username = ds.child("username").getValue(String.class);
                    String id = ds.child("id").getValue(String.class);
                    String img = ds.child("ImageURL").getValue(String.class);

                    User user = new User(id,username,img);
                    String usera = user.getUsername();
                    Log.d("TAG", username);
                    Log.d("Tag_user", usera);
                    lstUser.addLast(user);
                }
                userAdapter = new UserAdapter(ListUser.this,lstUser);
                recyclerView.setAdapter(userAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        usersdRef.addListenerForSingleValueEvent(eventListener);
        return lstUser;
    }
}