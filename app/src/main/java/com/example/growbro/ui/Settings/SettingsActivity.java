package com.example.growbro.ui.Settings;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.growbro.MainActivity;
import com.example.growbro.R;
import com.example.growbro.ui.signin.SignInActivity;

public class SettingsActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    private Button btnSignout;

    public static final String KEY_PREF_FAHRENHEIT_SWITCH = "fahrenheit_switch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);

        btnSignout = findViewById(R.id.btn_sign_out_activity_settings);

        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().putBoolean("signed_in", false).apply();
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(intent);
            }
        });

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_settings_container, new MainSettingsFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}
