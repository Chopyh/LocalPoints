package com.mulweb.localpoints.fragment.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.mulweb.localpoints.R;
import com.mulweb.localpoints.auth.google.AuthActivity;

public class SettingsFragment extends Fragment {
    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        Button logOutButton = view.findViewById(R.id.btn_logOut);
        logOutButton.setOnClickListener(v -> {
            // Firebase sign out
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(getContext(), AuthActivity.class));
            getActivity().finish();
        });

        return view;
    }
}