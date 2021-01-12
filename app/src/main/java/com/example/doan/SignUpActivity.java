package com.example.doan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    EditText editLoginUsername,editLoginPassword;
    String user,pass;
    EditText edtSignupUsername,edtSignupPassword,editSignupEmail;
    FirebaseAuth auth;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        editLoginUsername = (EditText)findViewById(R.id.edt_username);
        editLoginPassword = (EditText)findViewById(R.id.edt_password);
        edtSignupUsername = (EditText)findViewById(R.id.edt_signup_username) ;
        edtSignupPassword = (EditText)findViewById(R.id.edt_signup_password);
        editSignupEmail = (EditText)findViewById(R.id.edt_signup_email);

        auth = FirebaseAuth.getInstance();
    }

    public void SignUp(View view) {
        String username = edtSignupUsername.getText().toString().trim();
        String pass = edtSignupPassword.getText().toString().trim();
        String email = editSignupEmail.getText().toString().trim();
//        editLoginUsername.setText(user);
//        editLoginPassword.setText(pass);
//        if(edtSignupUsername.getText().length() != 0 && edtSignupPassword.getText().length() != 0)
//        {
//            Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
//            startActivity(intent);
//        }
//        else {
//            Toast.makeText(SignUpActivity.this,"Đăng ký thất bại !!!",Toast.LENGTH_SHORT).show();
//        }


        auth.createUserWithEmailAndPassword(email, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("user").child(userid);

                            HashMap<String, String> hashMap= new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", username);
                            hashMap.put("ImageURL","default");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    }
                });
    }
}