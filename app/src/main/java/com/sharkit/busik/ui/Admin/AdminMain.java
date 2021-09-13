package com.sharkit.busik.ui.Admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.sharkit.busik.Admin;
import com.sharkit.busik.MainActivity;
import com.sharkit.busik.R;
import com.sharkit.busik.ui.Main.Main;

public class AdminMain extends Fragment implements View.OnClickListener {
    private Button carrier, sender, countries, cities, exit;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.admin_main, container, false);
        findView(root);
        onClickListener();
        return root;
    }

    private void onClickListener() {
        exit.setOnClickListener(this);
        carrier.setOnClickListener(this);
        sender.setOnClickListener(this);
        countries.setOnClickListener(this);
        cities.setOnClickListener(this);
    }

    private void findView(View root) {
        exit = root.findViewById(R.id.exit_xml);
        carrier = root.findViewById(R.id.carrier_xml);
        sender = root.findViewById(R.id.sender_xml);
        countries = root.findViewById(R.id.country_xml);
        cities = root.findViewById(R.id.city_xml);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_admin);
        switch (v.getId()){
            case R.id.carrier_xml:
            navController.navigate(R.id.nav_admin_carriers);
                break;
            case R.id.sender_xml:
                navController.navigate(R.id.nav_admin_senders);
                break;
            case R.id.country_xml:
                navController.navigate(R.id.nav_admin_countries);
                break;
            case R.id.city_xml:
                navController.navigate(R.id.nav_admin_cities);
                break;
            case R.id.exit_xml:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(getActivity(), MainActivity.class));
        }

    }
}
