package com.example.doan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {

    EditText edtSignupUsername,edtSignupPassword,edtSignupCfmPass,editSignupEmail;
    FirebaseAuth auth;
    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtSignupUsername = findViewById(R.id.edt_signup_username) ;
        edtSignupPassword = findViewById(R.id.edt_signup_password);
        edtSignupCfmPass = findViewById(R.id.edt_signup_cfmpass);
        editSignupEmail = findViewById(R.id.edt_signup_email);

        auth = FirebaseAuth.getInstance();
    }

    public void SignUp(View view) {
        String username = edtSignupUsername.getText().toString();
        String pass = edtSignupPassword.getText().toString();
        String cfmpass = edtSignupCfmPass.getText().toString();
        String email = editSignupEmail.getText().toString();

        if (username.equals("") || pass.equals("") || cfmpass.equals("") || email.equals(""))
//            Log.d("testdetrong","thua");
           Toast.makeText(SignUpActivity.this,"Bạn cần phải điển đầy đủ tất cả thông tin", Toast.LENGTH_LONG).show();
        else if(!pass.equals(cfmpass))
            Toast.makeText(SignUpActivity.this, "Mật khẩu xác nhận không khớp",Toast.LENGTH_LONG).show();


        else {

            auth.createUserWithEmailAndPassword(email,pass)
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
                                hashMap.put("imageURL","default");
                                hashMap.put("status","offline");

                                reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
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
}