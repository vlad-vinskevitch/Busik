package com.sharkit.busik.ui.Carrier;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.sharkit.busik.Entity.StaticUser;
import com.sharkit.busik.MainActivity;
import com.sharkit.busik.R;

public class CarrierMain extends Fragment implements View.OnClickListener {
    private LinearLayout flights, reviews, settings, exit;
    private TextView name;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.carrier_main, container, false);
        findView(root);
        onClickLinear();
        return root;
    }

    private void onClickLinear() {
        name.setText(StaticUser.getName());
        flights.setOnClickListener(this);
        reviews.setOnClickListener(this);
        settings.setOnClickListener(this);
        exit.setOnClickListener(this);

    }

    private void findView(View root) {
        name = root.findViewById(R.id.name_xml);
        flights = root.findViewById(R.id.flight_xml);
        reviews = root.findViewById(R.id.reviews_xml);
        settings = root.findViewById(R.id.settings_xml);
        exit = root.findViewById(R.id.exit_xml);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_carrier);
        switch (v.getId()){
            case R.id.flight_xml:
                navController.navigate(R.id.nav_carrier_flights);
                break;
            case R.id.reviews_xml:
                navController.navigate(R.id.nav_carrier_reviews);
                break;
            case R.id.settings_xml:
                navController.navigate(R.id.nav_carrier_setting);
                break;
            case R.id.exit_xml:
                mAuth.signOut();
                startActivity(new Intent(getActivity(), MainActivity.class));
                break;
        }
    }
}

