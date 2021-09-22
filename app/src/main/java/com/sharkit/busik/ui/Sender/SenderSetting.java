package com.sharkit.busik.ui.Sender;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sharkit.busik.Entity.OwnName;
import com.sharkit.busik.Entity.StaticUser;
import com.sharkit.busik.Entity.User;
import com.sharkit.busik.Exception.NoConnectInternet;
import com.sharkit.busik.Exception.ToastMessage;
import com.sharkit.busik.R;
import com.sharkit.busik.Validation.Configuration;
import com.sharkit.busik.Validation.ValidationRegistration;

import java.util.ArrayList;

public class SenderSetting extends Fragment {
    private TextInputLayout pass;
    private TextInputEditText name, last_name,  phone, email, password;
    private AutoCompleteTextView country,
    city;
    private TextView rating;
    private Button save, changePass;
    private String mail;
    private ImageView back;

    private ValidationRegistration registration;
    private final User user = new User();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.carrier_setting, container, false);
        findView(root);
        writeInput();
        onClick();
        saveVisible();
        getAutoCompleteText();
        return root;
    }

    private void getAutoCompleteText() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ArrayList<String> cities = new ArrayList<>();
        ArrayList<String> countries = new ArrayList<>();
        db.collection("Countries")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                        countries.add(queryDocumentSnapshot.toObject(OwnName.class).getOwnName());
                    }
                    country.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line, countries));

                });
        db.collection("Cities")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                        cities.add(queryDocumentSnapshot.toObject(OwnName.class).getOwnName());
                    }
                });
        city.setAdapter(new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line, cities));
    }

    private void saveVisible() {
        editChange(name, StaticUser.getName());
        editChange(last_name, StaticUser.getLast_name());
        editChangeAutoComplete(country, StaticUser.getCountry());
        editChangeAutoComplete(city, StaticUser.getCity());
        editChange(phone, StaticUser.getPhone());
        emailChange(email, StaticUser.getEmail());
    }

    private void emailChange(TextInputEditText input, String user) {
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals(user)){
                    save.setVisibility(View.VISIBLE);
                    pass.setVisibility(View.VISIBLE);
                }else if (isNotChange()){
                    save.setVisibility(View.GONE);
                    pass.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private boolean isNotChange(){
        return name.getText().toString().equals(StaticUser.getName()) &&
                last_name.getText().toString().equals(StaticUser.getLast_name()) &&
                country.getText().toString().equals(StaticUser.getCountry()) &&
                city.getText().toString().equals(StaticUser.getCity()) &&
                phone.getText().toString().equals(StaticUser.getPhone()) &&
                email.getText().toString().equals(StaticUser.getEmail());
    }
    private void editChange(TextInputEditText input, String user) {
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(user)){
                    save.setVisibility(View.VISIBLE);
                }else if (isNotChange()){
                    save.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void editChangeAutoComplete(AutoCompleteTextView input, String user) {
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(user)){
                    save.setVisibility(View.VISIBLE);
                }else if (isNotChange()){
                    save.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }


    private void onClick() {
        registration = new ValidationRegistration(name,
                last_name, country, city, password, phone, email, password, getContext());
        save.setOnClickListener(v -> {

            if (!Configuration.hasConnection(getContext())){
                try {
                    throw new NoConnectInternet(getContext());
                } catch (NoConnectInternet noConnectInternet) {
                    noConnectInternet.printStackTrace();
                }
                return;
            }


            if (registration.isValidChange()){
                if (!email.getText().toString().equals(StaticUser.getEmail())) {
                    if (!password.getText().toString().equals(StaticUser.getPassword())) {
                        try {
                            throw new ToastMessage("Введите пароль", getContext());
                        } catch (ToastMessage toastMessage) {
                            toastMessage.printStackTrace();
                        }
                        return;
                    }
                    updateEmail();
                    writeToObject();
                    writeToFirestore();
                    return;
                }
                writeToObject();
                writeToFirestore();
            }
        });
        back.setOnClickListener(v -> Navigation.findNavController(getActivity(), R.id.nav_host_sender).navigate(R.id.nav_sender_profile));
        changePass.setOnClickListener(v -> createAlertDialog());
    }

    private void createAlertDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.alert_change_email,null);
        EditText text = view.findViewById(R.id.email_xml);
        text.setText(StaticUser.getEmail());

        dialog.setPositiveButton("Изменить", (dialog1, which) -> {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            if (Configuration.hasConnection(getContext())){
                mAuth.sendPasswordResetEmail(StaticUser.getEmail())
                        .addOnSuccessListener(unused -> {
                            try {
                                throw new ToastMessage("Проверте свою почту", getContext());
                            } catch (ToastMessage toastMessage) {
                                toastMessage.printStackTrace();
                            }
                        });
            }else {
                try {
                    throw new NoConnectInternet(getContext());
                } catch (NoConnectInternet noConnectInternet) {
                    noConnectInternet.printStackTrace();
                }
            }
        });
        dialog.setOnDismissListener(DialogInterface::dismiss);
        dialog.setView(view);
        dialog.show();
    }

    private void updateEmail() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if (TextUtils.isEmpty(password.getText())){
            return;
        }
        AuthCredential credential = EmailAuthProvider
                .getCredential(mAuth.getCurrentUser().getEmail(), password.getText().toString());
        firebaseUser.reauthenticate(credential);
        firebaseUser.updateEmail(email.getText().toString()).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TAG", e.getMessage());
            }
        });

    }

    private void writeToObject() {
        user.setTagCountry(generateKey(country.getText().toString().trim()));
        user.setTagCity(generateKey(city.getText().toString().trim()));
        user.setEmail(email.getText().toString().trim());
        user.setName(name.getText().toString().trim());
        user.setLast_name(last_name.getText().toString().trim());
        user.setPhone(phone.getText().toString().trim());
        user.setCountry(country.getText().toString().trim());
        user.setCity(city.getText().toString().trim());
        StaticUser.setName(user.getName());
        StaticUser.setLast_name(user.getLast_name());
        StaticUser.setEmail(user.getEmail());
        StaticUser.setPhone(user.getPhone());
        StaticUser.setCountry(user.getCountry());
        StaticUser.setCity(user.getCity());
    }

    private void writeToFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users")
                .document(mail)
                .delete();
        db.collection("Users")
                .document(StaticUser.getEmail())
                .set(user)
                .addOnSuccessListener(unused -> {
                    try {
                        throw new ToastMessage("Изменения сохранены", getContext());
                    } catch (ToastMessage toastMessage) {
                        toastMessage.printStackTrace();
                    }
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_carrier);
                    navController.navigate(R.id.nav_carrier_main);
                });
    }

    private void writeInput() {
        mail = StaticUser.getEmail();

        name.setText(StaticUser.getName());
        last_name.setText(StaticUser.getLast_name());
        country.setText(StaticUser.getCountry());
        city.setText(StaticUser.getCity());
        phone.setText(StaticUser.getPhone());
        email.setText(StaticUser.getEmail());
        rating.setText(String.valueOf(StaticUser.getRating()));

        user.setName(StaticUser.getName());
        user.setLast_name(StaticUser.getLast_name());
        user.setCountry(StaticUser.getCountry());
        user.setCity(StaticUser.getCity());
        user.setPhone(StaticUser.getPhone());
        user.setRating(StaticUser.getRating());
        user.setPassword(StaticUser.getPassword());
        user.setRole(StaticUser.getRole());
        user.setEmail(StaticUser.getEmail());
    }

    private void findView(View root) {
        back = root.findViewById(R.id.back_xml);
        changePass = root.findViewById(R.id.change_pass_xml);
        pass = root.findViewById(R.id.pass_layout_xml);
        name = root.findViewById(R.id.name_xml);
        last_name = root.findViewById(R.id.last_name_xml);
        country = root.findViewById(R.id.country_xml);
        city = root.findViewById(R.id.city_xml);
        phone = root.findViewById(R.id.phone_xml);
        email = root.findViewById(R.id.email_xml);
        rating = root.findViewById(R.id.rating_xml);
        password = root.findViewById(R.id.password_xml);
        save = root.findViewById(R.id.save_xml);
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