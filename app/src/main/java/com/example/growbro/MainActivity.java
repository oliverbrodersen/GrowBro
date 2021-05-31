package com.example.growbro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.growbro.ui.signin.SignInActivity;
import com.example.growbro.ui.signin.SignInViewModel;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    SharedPreferences sharedPreferences;
    SignInViewModel signInViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signInViewModel = new ViewModelProvider(this).get(SignInViewModel.class);
        sharedPreferences = getSharedPreferences("login",MODE_PRIVATE);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home)
                .setDrawerLayout(drawer)
                .build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(item -> {
            boolean handled = NavigationUI.onNavDestinationSelected(item, navController);

            if(!handled) {
                int id = item.getItemId();
                if (id == R.id.nav_signout) {
                    signInViewModel.signOut();
                    sharedPreferences.edit().putBoolean("signed_in", false).apply();

                    Intent intent = new Intent(this, SignInActivity.class);
                    startActivity(intent);

                    return true;
                }
            }
            drawer.closeDrawer(GravityCompat.START);


            return handled;
        });


        if(signInViewModel.getCurrentUser() == null && !sharedPreferences.getBoolean("signed_in",false)){
            //If the database did not return a user AND the user is not signed in from a previous session
            System.out.println("User is not signed in");
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
        } else {
            //Let user stay signed in after closing app
            //sharedPreferences.edit().putBoolean("signed_in", true).apply();

            //Save userid for use after app restart
            //if(signInViewModel.getCurrentUser() != null)
            //    sharedPreferences.edit().putInt("current_user_id", signInViewModel.getCurrentUser().getValue().getId()).apply();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}