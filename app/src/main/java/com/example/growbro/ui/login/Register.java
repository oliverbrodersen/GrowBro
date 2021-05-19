package com.example.growbro.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.growbro.MainActivity;
import com.example.growbro.R;

public class Register extends AppCompatActivity {

    private EditText editTextUsername, editTextEmail, editTextPassword;
    private ImageView btnRegister;
    private TextView alreadyRegistered;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editTextUsername = findViewById(R.id.edtUsername);
        editTextEmail = findViewById(R.id.edtEmail);
        editTextPassword = findViewById(R.id.edtextPassword);
        btnRegister = findViewById(R.id.btnSignUp);
        alreadyRegistered = findViewById(R.id.txtalreadyRegistered);



        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });
        alreadyRegistered.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), Login.class)));
    }
}
