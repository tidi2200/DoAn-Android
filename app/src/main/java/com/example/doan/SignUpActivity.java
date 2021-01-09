package com.example.doan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {
    EditText editLoginUsername,editLoginPassword;
    String user,pass;
    EditText edtSignupUsername,edtSignupPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        editLoginUsername = (EditText)findViewById(R.id.edt_username);
        editLoginPassword = (EditText)findViewById(R.id.edt_password);
        edtSignupUsername = (EditText)findViewById(R.id.edt_signup_username) ;
        edtSignupPassword = (EditText)findViewById(R.id.edt_signup_password) ;
    }

    public void SignUp(View view) {
        user = edtSignupUsername.getText().toString().trim();
        pass = edtSignupPassword.getText().toString().trim();
        editLoginUsername.setText(user);
        editLoginPassword.setText(pass);
        if(edtSignupUsername.getText().length() != 0 && edtSignupPassword.getText().length() != 0)
        {
            Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
            startActivity(intent);
        }
        else {
            Toast.makeText(SignUpActivity.this,"Đăng ký thất bại !!!",Toast.LENGTH_SHORT).show();
        }

    }
}