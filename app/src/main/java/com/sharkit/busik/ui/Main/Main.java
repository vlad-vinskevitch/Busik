package com.sharkit.busik.ui.Main;

import android.annotation.SuppressLint;
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

import com.sharkit.busik.MainActivity;
import com.sharkit.busik.R;

public class Main extends Fragment implements View.OnClickListener {
    private Button sign, registration;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.main, container, false);
        findView(root);
        onClickButton();
        return root;
    }

    private void onClickButton() {
        sign.setOnClickListener(this);
        registration.setOnClickListener(this);
    }

    private void findView(View root) {
        sign = root.findViewById(R.id.sign_xml);
        registration = root.findViewById(R.id.registration_xml);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
        switch (v.getId()){
            case R.id.sign_xml:
                navController.navigate(R.id.nav_login);
                break;
            case R.id.registration_xml:
                navController.navigate(R.id.nav_registration);
                break;
        }
    }
}

