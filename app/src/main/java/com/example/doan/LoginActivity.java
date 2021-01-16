package com.example.doan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class LoginActivity extends AppCompatActivity {
    EditText editLoginUsername,editLoginPassword;
    Button btnlogin,btnsignup;
    String user,pass;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;

    DatabaseReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        editLoginUsername = (EditText)findViewById(R.id.edt_username);
        editLoginPassword = (EditText)findViewById(R.id.edt_password);

        auth = FirebaseAuth.getInstance();

        btnlogin = (Button)findViewById(R.id.btn_login);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 user = editLoginUsername.getText().toString();
                 pass = editLoginPassword.getText().toString();
                auth.signInWithEmailAndPassword(user, pass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Intent intent = new Intent(LoginActivity.this, Contact.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
            }
        });
        btnsignup = (Button)findViewById(R.id.btn_signup);
        //Giu trang thai dang nhap
        auth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        if(firebaseUser!=null){
            Intent intent = new Intent(LoginActivity.this, Contact.class);
            startActivity(intent);
            finish();
        }

    }

   /* public void Login(View view) {*/
//        if(editLoginUsername.getText().length() != 0 && editLoginPassword.getText().length() != 0) {
//            if(editLoginUsername.getText().toString().equals("quang") && editLoginPassword.getText().toString().equals("1")) {
//                Toast.makeText(LoginActivity.this,"Bạn đã đăng nhập thành công ",Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
//                startActivity(intent);
//            } else{
//                Toast.makeText(LoginActivity.this,"Bạn đã đăng nhập thất bại !!! ",Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            Toast.makeText(LoginActivity.this,"Yêu cầu bạn nhập đủ thông tin ",Toast.LENGTH_SHORT).show();
//        }

        /*String username = editLoginUsername.getText().toString();
        String password = editLoginPassword.getText().toString();
        auth.signInWithEmailAndPassword(username, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Intent intent = new Intent(LoginActivity.this, ListUser.class);
                            startActivity(intent);
                        }
                    }
                });*/
/*    }*/


    public void OppenSignUp(View view) {
        Intent intent = new Intent(LoginActivity.this,SignUpActivity.class);
        startActivity(intent);
    }

}