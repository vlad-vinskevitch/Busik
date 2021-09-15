package com.sharkit.busik.Adapter.Admin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sharkit.busik.Entity.OwnName;
import com.sharkit.busik.Exception.ToastMessage;
import com.sharkit.busik.R;

import java.util.ArrayList;

public class AdminOwnNameAdapter extends BaseAdapter {
    private String type;
    private Context mContext;
    private ArrayList<String> list;
    private TextView ownName;

    public AdminOwnNameAdapter(Context mContext, ArrayList<String> list, String type) {
        this.mContext = mContext;
        this.list = list;
        this.type = type;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_admin_country_cities, null);
        }
        findView(convertView);
        writeToField(position);
        onCreateContextMenuListener(parent ,position);
        return convertView;
    }

    private void onCreateContextMenuListener(ViewGroup parent, int position) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection(type).document(list.get(position));
        parent.setOnCreateContextMenuListener((menu, v, menuInfo) -> {
            menu.add("Редактировать")
                    .setOnMenuItemClickListener(item -> {
                        changeOwnName(docRef, position);
                        return true;
                    });
            menu.add("Удалить")
                    .setOnMenuItemClickListener(item -> {
                        docRef.delete().addOnSuccessListener(unused -> {
                            try {
                                throw new ToastMessage("Изменения приняты", mContext);
                            } catch (ToastMessage toastMessage) {
                                toastMessage.printStackTrace();
                            }
                            NavController navController = Navigation.findNavController((Activity) mContext, R.id.nav_host_admin);
                            if (type.equals("Countries")) {
                                navController.navigate(R.id.nav_admin_countries);
                            }else {
                                navController.navigate(R.id.nav_admin_cities);
                            }
                        });
                        return true;
                    });
        });
    }

    @SuppressLint("SetTextI18n")
    private void changeOwnName(DocumentReference docRef, int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.alert_add_contry_city,null);
        TextView text = view.findViewById(R.id.own_name_xml);
        AutoCompleteTextView name = view.findViewById(R.id.name_xml);
        text.setText("Изменить: " + list.get(position) + " на ..");
        dialog.setOnDismissListener(DialogInterface::dismiss);
        dialog.setPositiveButton("Изменить", (dialog1, which) -> {
            docRef.delete();
            OwnName ownName = new OwnName(name.getText().toString().trim(), generateKey(name.getText().toString().trim()));
            FirebaseFirestore.getInstance().collection(type)
                    .add(ownName)
                    .addOnSuccessListener(documentReference -> {
                        try {
                            throw new ToastMessage("Изменения приняты", mContext);
                        } catch (ToastMessage toastMessage) {
                            toastMessage.printStackTrace();
                        }
                        NavController navController = Navigation.findNavController((Activity) mContext, R.id.nav_host_admin);
                        if (type.equals("Countries")) {
                            navController.navigate(R.id.nav_admin_countries);
                        }else {
                            navController.navigate(R.id.nav_admin_cities);
                        }
                    });
        });

        dialog.setView(view);
        dialog.show();
    }

    private void writeToField(int position) {
        ownName.setText(list.get(position));
    }

    private void findView(View convertView) {
        ownName = convertView.findViewById(R.id.name_xml);
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

}
