package com.sharkit.busik.ui.Admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sharkit.busik.Adapter.Admin.AdminUserAdapter;
import com.sharkit.busik.Entity.User;
import com.sharkit.busik.R;

import java.util.ArrayList;

public class AdminCarriers extends Fragment {
    private ListView listView;
    private ArrayList<User> users;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.admin_carriers,container, false);
        findView(root);
        getAllData();
        findUserFromCountry();
        findUserFromCity();
        return root;
    }

    private void findUserFromCountry() {

    }

    private void findUserFromCity() {
    }

    private void getAllData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        users = new ArrayList<>();
        db.collection("Users")
                .whereEqualTo("role", "Carrier")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                        users.add(queryDocumentSnapshot.toObject(User.class));
                    }
                    setAdapter();
                });
    }

    private void setAdapter() {
        AdminUserAdapter adapter = new AdminUserAdapter(getContext(), users);
        listView.setAdapter(adapter);
    }

    private void findView(View root) {
        listView = root.findViewById(R.id.list_xml);
    }
}
