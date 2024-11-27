package com.mulweb.localpoints.auth.google.login;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.mulweb.localpoints.MainActivity;
import com.mulweb.localpoints.R;
import com.mulweb.localpoints.auth.google.AuthActivity;


public class RegisterFragment extends Fragment {
    private AuthActivity authActivity;
    private FirebaseAuth mauth;

    public RegisterFragment(AuthActivity authActivity) {
        this.authActivity = authActivity;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mauth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        Button signUpButton = view.findViewById(R.id.btn_register);
        signUpButton.setOnClickListener(v -> {
            CheckBox checkBox = view.findViewById(R.id.chb_terms);
            CheckBox checkBox2 = view.findViewById(R.id.chb_privacy);

            EditText emailEditText = view.findViewById(R.id.ed_Email);
            EditText passwordEditText = view.findViewById(R.id.ed_password);

            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (!checkBox.isChecked() || !checkBox2.isChecked()) {
                Toast.makeText(authActivity, "Por favor acepta los términos y condiciones", Toast.LENGTH_SHORT).show();
                return;
            }

            mauth.createUserWithEmailAndPassword(email, password);
            mauth.signInWithEmailAndPassword(email, password);

            if (mauth.getCurrentUser() != null) {
                authActivity.startActivity(new MainActivity().getIntent());
                authActivity.finish();
            } else {
                Toast.makeText(authActivity, "Error al registrar usuario", Toast.LENGTH_SHORT).show();
            }
        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                authActivity.setBackButtonVisibility(false);
                authActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.auth_layout, new LoginFragment(authActivity))
                        .commit();
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
        return view;
    }
}