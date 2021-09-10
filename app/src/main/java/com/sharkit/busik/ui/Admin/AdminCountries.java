package com.sharkit.busik.ui.Admin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sharkit.busik.Adapter.Admin.AdminOwnNameAdapter;
import com.sharkit.busik.Entity.OwnName;
import com.sharkit.busik.R;

import java.util.ArrayList;

public class AdminCountries extends Fragment implements View.OnClickListener {
    private ListView listView;
    private ArrayList<String> ownNames;
    private Button add;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.admin_countries,container, false);
        findView(root);
        getAllData();
        onClick();
        return root;
    }
    private void onClick(){
        add.setOnClickListener(this);
    }

    private void getAllData() {
        ownNames = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Countries")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for(QueryDocumentSnapshot queryDocumentSnapshot :queryDocumentSnapshots){
                        ownNames.add(queryDocumentSnapshot.toObject(OwnName.class).getOwnName());
                    }
                    setAdapter();
                });
    }

    private void setAdapter() {
        AdminOwnNameAdapter adapter = new AdminOwnNameAdapter(getContext(), ownNames);
        listView.setAdapter(adapter);
    }

    private void findView(View root) {
        listView = root.findViewById(R.id.list_xml);
        add = root.findViewById(R.id.add_xml);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_xml:
                createAlertDialog();
                break;
        }
    }

    private void createAlertDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext(),R.style.styleAlertDialog);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.item_admin_country_cities, null);
        TextView ownName = view.findViewById(R.id.own_name_xml);
        AutoCompleteTextView name = view.findViewById(R.id.name_xml);
        ownName.setText(" Страны");
        dialog.setOnDismissListener(DialogInterface::dismiss);
        dialog.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addNewOwnName(name.getText().toString());
            }
        })


    }

    private void addNewOwnName(String s) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Countries")
                .add(new OwnName(s, new ArrayList<>()));
    }
}

