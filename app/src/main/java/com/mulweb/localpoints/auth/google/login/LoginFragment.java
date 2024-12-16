package com.mulweb.localpoints.auth.google.login;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.mulweb.localpoints.MainActivity;
import com.mulweb.localpoints.R;
import com.mulweb.localpoints.auth.google.AuthActivity;

import java.util.concurrent.Executor;


public class LoginFragment extends Fragment {

    private FirebaseAuth mAuth;
    private final AuthActivity authActivity;

    public LoginFragment(AuthActivity authActivity) {
        this.authActivity = authActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        Button signInButton = view.findViewById(R.id.btn_login);
        Button signUpButton = view.findViewById(R.id.btn_register);

        signInButton.setOnClickListener(v -> {
            Log.println(Log.INFO, "LoginFragment", "Sign in");
            String email = ((EditText) view.findViewById(R.id.ed_Email)).getText().toString();
            String password = ((EditText) view.findViewById(R.id.et_password)).getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(getContext(), "Por favor rellena todos los campos", Toast.LENGTH_SHORT).show();
                return;
            }

            signInEmail(email, password);
            if (mAuth.getCurrentUser() != null) {
                Toast.makeText(getContext(), "Inicio de sesión satisfactorio", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(authActivity, MainActivity.class));
                authActivity.finish();
            }
        });

        signUpButton.setOnClickListener(v -> {
            authActivity.setBackButtonVisibility(true);
            authActivity.getSupportFragmentManager().beginTransaction()
                .replace(R.id.auth_layout, new RegisterFragment(authActivity))
                .commit();
        });

        // Inflate the layout for this fragment
        return view;
    }

    public void signInEmail(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "signInWithEmail:success");
                    } else {
                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                        Toast.makeText(getContext(), "Authentication failed.", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}