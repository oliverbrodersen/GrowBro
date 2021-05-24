package com.example.growbro.ui.signin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.growbro.MainActivity;
import com.example.growbro.R;

public class SignInActivity extends AppCompatActivity {

    private EditText editTextUsername, editTextPassword, editTextPassword2;
    private Button btnLogin;
    private TextView textView;

    private SignInViewModel signInViewModel;

    SharedPreferences sharedPreferences;
    private boolean newUser;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);

        //Check if user is signed in
        if (sharedPreferences.getBoolean("signed_in",false)) {
            goToActivity();
        }

        editTextUsername = findViewById(R.id.edtUsername);
        editTextPassword = findViewById(R.id.edtextPassword);
        editTextPassword2 = findViewById(R.id.edtextPassword2);
        btnLogin = findViewById(R.id.btnLogin);
        textView = findViewById(R.id.txtNewUser);
        newUser = false;

        signInViewModel = new ViewModelProvider(this).get(SignInViewModel.class);

        btnLogin.setOnClickListener(view -> {
            String userName = editTextUsername.getText().toString();
            String password = editTextPassword.getText().toString();
            String password2 = editTextPassword2.getText().toString();

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

            if (newUser && !editTextPassword2.getText().toString().equals(password)) {
                editTextPassword2.setError("Passwords must be identical");
                return;
            }

            if(newUser)
                signInViewModel.addUser(userName, password);
            else
                signInViewModel.signIn(userName, password);

            goToActivity();
        });

        textView.setOnClickListener(view -> {
            if(!newUser) {
                btnLogin.setText(R.string.sign_up);
                editTextPassword2.setVisibility(View.VISIBLE);
                textView.setText(R.string.already_have_an_account);
                newUser = true;
            } else{
                btnLogin.setText(R.string.sign_in);
                editTextPassword2.setVisibility(View.INVISIBLE);
                textView.setText(R.string.new_user);
                newUser = false;
            }
        });

    }
    public void goToActivity(){
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_TASK_ON_HOME);
        startActivity(intent);
        this.finishAfterTransition();
    }
}
