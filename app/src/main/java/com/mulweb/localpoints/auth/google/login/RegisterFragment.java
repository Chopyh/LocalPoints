package com.mulweb.localpoints.auth.google.login;

import android.content.Intent;
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

import com.google.firebase.auth.*;
import com.google.firebase.database.FirebaseDatabase;
import com.mulweb.localpoints.MainActivity;
import com.mulweb.localpoints.R;
import com.mulweb.localpoints.auth.google.AuthActivity;
import com.mulweb.localpoints.components.DatePickerFragment;
import com.mulweb.localpoints.entities.User;

import java.util.Objects;


public class RegisterFragment extends Fragment implements DatePickerFragment.DatePickerListener {
    private final AuthActivity authActivity;
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
        Button datePicker = view.findViewById(R.id.btn_datePicker);

        datePicker.setOnClickListener(v -> {
            var newFragment = new DatePickerFragment();
            newFragment.setDatePickerListener(this);
            newFragment.show(getParentFragmentManager(), "datePicker");
        });

        signUpButton.setOnClickListener(v -> {

            if (!checkFields()) return;

            CheckBox checkBox = view.findViewById(R.id.chb_terms);
            CheckBox checkBox2 = view.findViewById(R.id.chb_privacy);

            EditText emailEditText = view.findViewById(R.id.ed_Email);
            EditText passwordEditText = view.findViewById(R.id.ed_password);
            EditText userNameEditText = view.findViewById(R.id.ed_username);
            EditText fullNameEditText = view.findViewById(R.id.ed_fullname);
            EditText dateEditText = view.findViewById(R.id.ed_birthdate);

            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();

            if (!checkBox.isChecked() || !checkBox2.isChecked()) {
                Toast.makeText(authActivity, "Por favor acepta los términos y condiciones", Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(authActivity, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                return;
            }

            mauth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            }).addOnCompleteListener(authResult -> {
                try {
                    throw Objects.requireNonNull(authResult.getException());
                } catch (FirebaseAuthWeakPasswordException e) {
                    passwordEditText.setError("Campo obligatorio\nLa contraseña debe tener al menos 6 caracteres");
                    Toast.makeText(authActivity, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                } catch (FirebaseAuthUserCollisionException e) {
                    emailEditText.setError("El email ya está en uso");
                    Toast.makeText(authActivity, "El email ya está en uso", Toast.LENGTH_SHORT).show();
                } catch (FirebaseAuthEmailException e) {
                    emailEditText.setError("El email no es válido");
                    Toast.makeText(authActivity, "El email no es válido", Toast.LENGTH_SHORT).show();
                } catch (NullPointerException ignored) {
                } catch (Exception e) {
                    Toast.makeText(authActivity, "Ha ocurrido un error. \nPruebe a registrarse más tarde.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }).addOnSuccessListener(authResult -> {
                User user = new User(
                    userNameEditText.getText().toString(),
                    fullNameEditText.getText().toString(),
                    email,
                    dateEditText.getText().toString()
                );

                var userId = Objects.requireNonNull(mauth.getCurrentUser()).getUid();
                var databaseReference = FirebaseDatabase.getInstance().getReference();
                databaseReference.child("users").child(userId).setValue(user);

                Toast.makeText(authActivity, "Usuario registrado", Toast.LENGTH_SHORT).show();
                mauth.signInWithEmailAndPassword(email, password);
                startActivity(new Intent(authActivity, MainActivity.class));
            });
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
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
        return view;
    }

    public boolean checkFields() {
        EditText emailEditText = getView().findViewById(R.id.ed_Email);
        EditText passwordEditText = getView().findViewById(R.id.ed_password);
        EditText userNameEditText = getView().findViewById(R.id.ed_username);
        EditText fullNameEditText = getView().findViewById(R.id.ed_fullname);
        EditText dateEditText = getView().findViewById(R.id.ed_birthdate);

        String email = emailEditText.getText().toString();
        String password = passwordEditText.getText().toString();
        String userName = userNameEditText.getText().toString();
        String fullName = fullNameEditText.getText().toString();
        String date = dateEditText.getText().toString();

        if (email.isEmpty() || password.isEmpty() || userName.isEmpty() || fullName.isEmpty() || date.isEmpty()) {
            if (email.isEmpty()) {
                emailEditText.setError("Campo obligatorio");
            }

            if (password.isEmpty()) {
                passwordEditText.setError("Campo obligatorio\nLa contraseña debe tener al menos 6 caracteres");
            }

            if (userName.isEmpty()) {
                userNameEditText.setError("Campo obligatorio");
            }

            if (fullName.isEmpty()) {
                fullNameEditText.setError("Campo obligatorio");
            }

            if (date.isEmpty()) {
                dateEditText.setError("Campo obligatorio");
            }

            Toast.makeText(authActivity, "Por favor rellena todos los campos", Toast.LENGTH_SHORT).show();

            return false;
        }

        return true;
    }

    @Override
    public void onDateSet(int year, int month, int day) {
        EditText dateEditText = getView().findViewById(R.id.ed_birthdate);
        String date = day + "/" + month + "/" + year;
        dateEditText.setText(date);
    }
}