package com.mulweb.localpoints;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mulweb.localpoints.auth.google.AuthActivity;
import com.mulweb.localpoints.fragment.main.ForumFragment;
import com.mulweb.localpoints.fragment.main.ListFragment;
import com.mulweb.localpoints.fragment.main.MapFragment;
import com.mulweb.localpoints.fragment.main.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        BottomNavigationView nav = findViewById(R.id.bottomNavigationView);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            startActivity(new Intent(this, AuthActivity.class));
            finish();
        }

        loadFragment(new MapFragment());
        nav.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            if (item.getItemId() == R.id.nav_item_map) {
                fragment = new MapFragment();
            } else if (item.getItemId() == R.id.nav_item_list) {
                fragment = new ListFragment();
            } else if (item.getItemId() == R.id.nav_item_forum) {
                fragment = new ForumFragment();
            } else if (item.getItemId() == R.id.nav_item_settings) {
                fragment = new SettingsFragment();
            }

            return loadFragment(fragment);
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}