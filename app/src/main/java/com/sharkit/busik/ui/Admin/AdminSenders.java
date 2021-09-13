package com.sharkit.busik.ui.Admin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sharkit.busik.Adapter.Admin.AdminUserAdapter;
import com.sharkit.busik.Entity.OwnName;
import com.sharkit.busik.Entity.User;
import com.sharkit.busik.R;

import java.util.ArrayList;

public class AdminSenders extends Fragment implements View.OnClickListener {
    private AutoCompleteTextView country, city;
    private Button accept, clear;
    private ListView listView;
    private ArrayList<User> users;
    private ArrayList <String> cities, countries;
    @SuppressLint("StaticFieldLeak")
    private static final FirebaseFirestore DB = FirebaseFirestore.getInstance();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.admin_senders,container, false);
        findView(root);
        onClick();
        getAllData();
        getAllCountriesAndCities();
        return root;
    }

    private void onClick(){
        accept.setOnClickListener(this);
        clear.setOnClickListener(this);
    }
    private void getAllCountriesAndCities() {
        cities = new ArrayList<>();
        countries = new ArrayList<>();
        DB.collection("Countries")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                        countries.add(queryDocumentSnapshot.toObject(OwnName.class).getOwnName());
                    }
                    country.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, countries));

                });
        DB.collection("Cities")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                        cities.add(queryDocumentSnapshot.toObject(OwnName.class).getOwnName());
                    }
                    city.setAdapter(new ArrayAdapter<>(getContext(), android.R.layout.simple_dropdown_item_1line, cities));
                });
    }

    private Query findUserByCity(Query query, String value) {
        return query.whereEqualTo("city", value);
    }

    private Query findUserByCountry(Query query, String value) {
        return query.whereEqualTo("country", value);
    }

    private void findUser(Query query) {

        query.get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                        users.add(queryDocumentSnapshot.toObject(User.class));
                    }
                    setAdapter();
                });

    }


    private void getAllData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        users = new ArrayList<>();
        db.collection("Users")
                .whereEqualTo("role", "Sender")
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
        country = root.findViewById(R.id.country_xml);
        city = root.findViewById(R.id.city_xml);
        accept = root.findViewById(R.id.accept_xml);
        clear = root.findViewById(R.id.clear_xml);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.accept_xml:
                setFilter();
                break;
            case R.id.clear_xml:
                country.setText("");
                city.setText("");
                getAllData();
                break;
        }
    }

    private void setFilter() {
        Query query = DB.collection("Users")
                .whereEqualTo("role", "Sender");

        if (!TextUtils.isEmpty(country.getText())){
            query = findUserByCountry(query, country.getText().toString().trim());
        }
        if (!TextUtils.isEmpty(city.getText())){
            query = findUserByCity(query, city.getText().toString().trim());
        }
        users = new ArrayList<>();

        findUser(query);

    }
}

