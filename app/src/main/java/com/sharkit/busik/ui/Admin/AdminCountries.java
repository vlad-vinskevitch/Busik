package com.sharkit.busik.ui.Admin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sharkit.busik.Adapter.Admin.AdminOwnNameAdapter;
import com.sharkit.busik.Entity.OwnName;
import com.sharkit.busik.R;

import java.util.ArrayList;

public class AdminCountries extends Fragment implements View.OnClickListener {
    private ListView listView;
    private ArrayList<String> ownNames;
    private Button add;
    ArrayList<String> list;
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
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.alert_add_contry_city, null);
        TextView ownName = view.findViewById(R.id.own_name_xml);
        AutoCompleteTextView name = view.findViewById(R.id.name_xml);
        ownName.setText("Напишите название страны");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line, ownNames);
        name.setAdapter(adapter);

        dialog.setOnDismissListener(DialogInterface::dismiss);
        dialog.setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(name.getText().toString())){
                    setToastMessage("Напишите название страны");
                    return;
                }
                addNewOwnName(name.getText().toString().trim());
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();




    }

//    private ArrayList<String> dropDownList(AutoCompleteTextView name) {
//
//        name.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//        return list;
//    }

    private void addNewOwnName(String s) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Countries")
                .document(s)
                .set(new OwnName(s, generateKey(s)))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        setToastMessage("Страна добавленв");
                        Navigation.findNavController(getActivity(), R.id.nav_host_admin).navigate(R.id.nav_admin_countries);
                    }
                });
    }
    private ArrayList generateKey(String inputText) {
        String inputString = inputText.toLowerCase();
        String [] tagArray = inputString.split(" ");
        ArrayList<String> tags = new ArrayList<>();



        for (String word : tagArray){
            String a = "";
            char [] b = inputString.toCharArray();

            for (int i = 0; i < b.length; i++){
                a += b[i];
                tags.add(a);
            }
            inputString = inputString.replace(word, "").trim();
        }
        return  tags;
    }

    private void setToastMessage(String message){
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

}

