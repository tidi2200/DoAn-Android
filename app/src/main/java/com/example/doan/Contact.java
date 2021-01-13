package com.example.doan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.ArrayList;
import java.util.List;


public class Contact extends AppCompatActivity {

    RecyclerView recyclerView;

    UserAdapter userAdapter;
    List<User> lstUser;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.Contact);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.Contact:
                        return true;
                    case R.id.Message:
                        startActivity(new Intent(getApplicationContext(),Message.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.Setting:
                        startActivity(new Intent(getApplicationContext(), Setting.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
        CircularImageView circularImageView = findViewById(R.id.circularImageView);
        circularImageView.setCircleColorDirection(CircularImageView.GradientDirection.TOP_TO_BOTTOM);
        circularImageView.setBorderColorStart(Color.BLACK);
        circularImageView.setBorderColorEnd(Color.MAGENTA);
        circularImageView.setShadowRadius(7f);
        circularImageView.setShadowGravity(CircularImageView.ShadowGravity.CENTER);




        lstUser= new ArrayList<>();

        mContext = Contact.this;

        recyclerView = findViewById(R.id.rev_userlist);

//        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DisplayData();
    }

    public void DisplayData(){
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

        DatabaseReference usersdRef = rootRef.child("user");

        usersdRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                lstUser.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    String username = ds.child("username").getValue(String.class);
                    String id = ds.child("id").getValue(String.class);
                    String img = ds.child("ImageURL").getValue(String.class);

                    User user = new User(id,username,img);
                    String usera = user.getUsername();
                    Log.d("TAG", username);
                    Log.d("Tag_user", usera);
                    lstUser.add(user);
                }
                userAdapter = new UserAdapter(mContext,lstUser);
                recyclerView.setAdapter(userAdapter); }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}