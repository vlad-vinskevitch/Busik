package com.sharkit.busik.ui.Sender;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.sharkit.busik.MainActivity;
import com.sharkit.busik.R;

public class SenderProfile extends Fragment implements View.OnClickListener {
    private LinearLayout flight, message, review, setting, exit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.sender_profile, container, false  );
        findView(root);
        onClick();
        return root;
    }

    private void findView(View root) {
        flight = root.findViewById(R.id.flight_xml);
        message = root.findViewById(R.id.message_xml);
        review = root.findViewById(R.id.reviews_xml);
        setting = root.findViewById(R.id.settings_xml);
        exit = root.findViewById(R.id.exit_xml);
    }
    private void onClick(){
        flight.setOnClickListener(this);
        message.setOnClickListener(this);
        review.setOnClickListener(this);
        setting.setOnClickListener(this);
        exit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_sender);
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        switch (v.getId()){
            case R.id.flight_xml:
                navController.navigate(R.id.nav_sender_main);
                break;
            case R.id.message_xml:
                break;
            case R.id.reviews_xml:
                break;
            case R.id.settings_xml:
                break;
            case R.id.exit_xml:
                mAuth.signOut();
                startActivity(new Intent(getActivity(), MainActivity.class));
                break;
        }
    }
}
