package com.sharkit.busik.ui.Main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sharkit.busik.Entity.OwnName;
import com.sharkit.busik.Entity.User;
import com.sharkit.busik.Exception.NoConnectInternet;
import com.sharkit.busik.Exception.ToastMessage;
import com.sharkit.busik.R;
import com.sharkit.busik.Validation.Configuration;
import com.sharkit.busik.Validation.ValidationRegistration;

import java.util.ArrayList;

public class Registration extends Fragment {
    private TextInputEditText name, last_name, phone, email, password, accept_pass;
    private AutoCompleteTextView country, city;
    private RadioButton sender, transport;
    private Button registration;
    private Spinner spinner;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.registration, container, false);
        findView(root);
        onClick();
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

    private void onClick() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(), R.array.spinner_dropdown_array, R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        registration.setOnClickListener(v -> {

            ValidationRegistration validationRegistration = new ValidationRegistration(
                    name,last_name, country,
                    city,password,phone,email,
                    accept_pass, getContext()
            );
            if (!Configuration.hasConnection(getContext())){
                try {
                    throw new NoConnectInternet(getContext());
                } catch (NoConnectInternet noConnectInternet) {
                    noConnectInternet.printStackTrace();
                }
                return;
            }
                if (validationRegistration.isEmptyInput()){
                    registrationProfile();
                }


        });
    }


    private void registrationProfile() {
        User user = new User();
        writeUser(user);
        createUser(user);

    }

    private  void createUser(User user){
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(user.getEmail(),user.getPassword())
                .addOnSuccessListener(authResult -> {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("Users").document(user.getEmail())
                            .set(user)
                            .addOnSuccessListener(documentReference -> {
                                try {
                                    throw new ToastMessage("???????????????? ??????????????????????",getContext());
                                } catch (ToastMessage toastMessage) {
                                    toastMessage.printStackTrace();
                                }

                                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                                navController.navigate(R.id.nav_main);
                            });
                });
    }

    private void writeUser(User user) {
        String s;
        if (spinner.getSelectedItemPosition() == 0){
            s = "Carrier";
        }else {
            s = "Sender";
        }
        user.setTagCity(generateKey(city.getText().toString().trim()));
        user.setTagCountry(generateKey(country.getText().toString().trim()));
        user.setName(name.getText().toString().trim());
        user.setLast_name(last_name.getText().toString().trim());
        user.setCountry(country.getText().toString().trim());
        user.setCity(city.getText().toString().trim());
        user.setPhone(phone.getText().toString().trim());
        user.setEmail(email.getText().toString().trim());
        user.setPassword(password.getText().toString());
        user.setRole(s);
        user.setFlight(s);
    }

    private void findView(View root) {
        spinner = root.findViewById(R.id.spinner_xml);
        sender = root.findViewById(R.id.sender_xml);
        transport = root.findViewById(R.id.carrier_xml);
        name = root.findViewById(R.id.name_xml);
        last_name = root.findViewById(R.id.last_name_xml);
        country = root.findViewById(R.id.country_xml);
        city = root.findViewById(R.id.city_xml);
        phone = root.findViewById(R.id.phone_xml);
        email = root.findViewById(R.id.email_xml);
        password = root.findViewById(R.id.password_xml);
        accept_pass = root.findViewById(R.id.accept_pass_xml);
        registration = root.findViewById(R.id.registration_xml);
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
