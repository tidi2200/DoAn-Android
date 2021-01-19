package com.example.doan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ChangeUsername extends AppCompatActivity {
    Button btn_submitUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_username);
        btn_submitUsername = findViewById(R.id.btn_submitUsername);
        btn_submitUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText values = findViewById(R.id.edt_newUsername);
                String newUserName = values.getText().toString();

                if (newUserName != "") {
                    FirebaseUser fuser = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("user").child(fuser.getUid());
                    ref.child("username").setValue(newUserName);
                    startActivity(new Intent(ChangeUsername.this,Contact.class));
                    finish();

                }else Toast.makeText(ChangeUsername.this,"Không được để trống",Toast.LENGTH_LONG);
            }
        });
    }
}