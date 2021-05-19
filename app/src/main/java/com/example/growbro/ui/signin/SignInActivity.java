package com.example.growbro.ui.signin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.growbro.MainActivity;
import com.example.growbro.R;

public class SignInActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword;
    private ImageView btnRegister;

    private SignInViewModel signInViewModel;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        editTextUsername = findViewById(R.id.edtUsername);
        editTextPassword = findViewById(R.id.edtextPassword);
        btnRegister = findViewById(R.id.btnSignUp);

        signInViewModel = new ViewModelProvider(this).get(SignInViewModel.class);

        btnRegister.setOnClickListener(view -> {
            String userName = editTextUsername.getText().toString();
            String password = editTextPassword.getText().toString();

            if (TextUtils.isEmpty(userName)) {
                editTextUsername.setError("Username is required.");
                return;
            }

            if (TextUtils.isEmpty(password)) {
                editTextPassword.setError("Password is required.");
                return;
            }

            if (password.length() < 6) {
                editTextPassword.setError("Password must be more than 6 Characters");
                return;
            }

            signInViewModel.login(userName, password);
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Check if user is signed in. Null if not
        if (signInViewModel.getCurrentUser() != null) { //TODO This is for when user is already signed in. Not working yet.
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
        }
    }
}
