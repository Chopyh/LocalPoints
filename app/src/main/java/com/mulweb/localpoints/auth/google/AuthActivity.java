package com.mulweb.localpoints.auth.google;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.mulweb.localpoints.R;
import com.mulweb.localpoints.auth.google.login.LoginFragment;

public class AuthActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_auth);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.auth), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            finish();
        }

        Button backButton = findViewById(R.id.btn_back);
        backButton.setOnClickListener(v -> {
            setBackButtonVisibility(false);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.auth_layout, new LoginFragment(this))
                    .commit();
        });

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.auth_layout, new LoginFragment(this))
                .commit();
    }


    public void setBackButtonVisibility(boolean visible) {
        Button backButton = findViewById(R.id.btn_back);
        backButton.setVisibility(visible ? View.VISIBLE : View.GONE);
        backButton.setEnabled(visible);
    }
}