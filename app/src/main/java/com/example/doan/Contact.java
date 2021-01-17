package com.example.doan;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
import java.util.HashMap;
import java.util.List;

//Hiện danh sách user
public class Contact extends AppCompatActivity {

    com.mikhaellopez.circularimageview.CircularImageView profileAvatar;
    RecyclerView recyclerView;
    TextView txtUserCurrent;
    UserAdapter userAdapter;
    List<User> lstUser;
    Context mContext;

    FirebaseUser firebaseUser;

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.Contact);

        //Hiển thị username đang đăng nhập
        txtUserCurrent = findViewById(R.id.usernamecurrentdisplay);
        profileAvatar = findViewById(R.id.profileavatarcontact);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("user").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                txtUserCurrent.setText(user.getUsername());
                if(user.getImageURL().equals("default")){
                    profileAvatar.setImageResource(R.mipmap.ic_launcher);
                }
                else{
                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profileAvatar);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
        CircularImageView circularImageView = findViewById(R.id.profileavatarcontact);
        circularImageView.setCircleColorDirection(CircularImageView.GradientDirection.TOP_TO_BOTTOM);
        circularImageView.setBorderColorStart(Color.BLACK);
        circularImageView.setBorderColorEnd(Color.MAGENTA);
        circularImageView.setShadowRadius(7f);
        circularImageView.setShadowGravity(CircularImageView.ShadowGravity.CENTER);

        lstUser= new ArrayList<>();

        mContext = Contact.this;

        recyclerView = findViewById(R.id.rev_userlist);

       //recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DisplayData();
    }

    //Hiện danh sách user lên recycler view (sự kiện conversation cho mỗi user được viết bên UserAdapter)
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
                    String status = ds.child("status").getValue(String.class);
                    User user = new User(id,username,img,status);
                    String usera = user.getUsername();
                    lstUser.add(user);
                }
                userAdapter = new UserAdapter(mContext,lstUser,true);
                recyclerView.setAdapter(userAdapter); }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("Failed to read value.", error.toException());
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