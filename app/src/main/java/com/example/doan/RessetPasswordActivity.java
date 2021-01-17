package com.example.doan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class RessetPasswordActivity extends AppCompatActivity {

    EditText send_email;
    Button btn_resset;

    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resset_password);

        send_email = findViewById(R.id.send_email);
        btn_resset = findViewById(R.id.btn_resset);

        firebaseAuth  = firebaseAuth.getInstance();
        btn_resset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = send_email.getText().toString();

                if(email.equals(""))
                {
                    Toast.makeText(RessetPasswordActivity.this, "Bạn chưa nhập tài khoản muốn đổi mật khẩu !!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RessetPasswordActivity.this, "Pleas check your Email", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RessetPasswordActivity.this,LoginActivity.class));
                            }
                            else {
                                String erro = task.getException().getMessage();
                                Toast.makeText(RessetPasswordActivity.this, erro, Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}