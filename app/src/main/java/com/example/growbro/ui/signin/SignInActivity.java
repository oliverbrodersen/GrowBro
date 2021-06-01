package com.example.growbro.ui.signin;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.growbro.MainActivity;
import com.example.growbro.Models.User;
import com.example.growbro.R;
import com.google.android.material.textfield.TextInputLayout;

public class SignInActivity extends AppCompatActivity {

    private TextInputLayout editTextUsername, editTextPassword, editTextPassword2;
    private LinearLayout loading;
    private Button btnLogin;
    private TextView textView;
    private SignInViewModel signInViewModel;
    SharedPreferences sharedPreferences;
    private boolean newUser;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        editTextUsername = findViewById(R.id.edtEmailLogin);
        editTextPassword = findViewById(R.id.edtextPasswordLogin);
        editTextPassword2 = findViewById(R.id.edtextPasswordLogin2);

        btnLogin = findViewById(R.id.btnLogin);
        textView = findViewById(R.id.txtNewUser);
        newUser = false;
        loading = findViewById(R.id.loadingOverlay);
        editTextPassword2.setVisibility(View.GONE);

        signInViewModel = new ViewModelProvider(this).get(SignInViewModel.class);

        loading.setVisibility(View.GONE);

        btnLogin.setOnClickListener(view -> {
            Boolean noError = true;
            String userName = editTextUsername.getEditText().getText().toString();
            String password = editTextPassword.getEditText().getText().toString();
            String password2 = editTextPassword2.getEditText().getText().toString();

            if (TextUtils.isEmpty(userName)) {
                editTextUsername.setError("Username is required.");
                noError = false;
            }

            if (TextUtils.isEmpty(password)) {
                editTextPassword.setError("Password is required.");
                noError = false;
            }

            if (newUser && !editTextPassword2.getEditText().getText().toString().equals(password)) {
                editTextPassword2.setError("Passwords must be identical");
                noError = false;
            }

            if (noError) {
                loading.setVisibility(View.VISIBLE);
                signInViewModel.getCurrentUser().observe(this, new Observer<User>() {
                    @Override
                    public void onChanged(User user) {
                        if (user.getUsername() != null)
                            goToActivity();
                        else{
                            loading.setVisibility(View.GONE);
                            editTextPassword.getEditText().setText("");
                            editTextPassword.setError("Incorrect login information");
                        }
                    }
                });

                if (newUser) {
                    signInViewModel.addUser(userName, password);
                } else {
                    signInViewModel.signIn(userName, password);
                }
            }
        });

        textView.setOnClickListener(view -> {
            if(!newUser) {
                btnLogin.setText(R.string.sign_up);
                editTextPassword2.setVisibility(View.VISIBLE);
                textView.setText(R.string.already_have_an_account);
                newUser = true;
            } else{
                btnLogin.setText(R.string.sign_in);
                editTextPassword2.setVisibility(View.GONE);
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
