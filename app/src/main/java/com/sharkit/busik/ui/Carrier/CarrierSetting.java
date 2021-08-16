package com.sharkit.busik.ui.Carrier;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sharkit.busik.Entity.StaticUser;
import com.sharkit.busik.Entity.User;
import com.sharkit.busik.Exception.ToastMessage;
import com.sharkit.busik.R;
import com.sharkit.busik.Validation.ValidationRegistration;

public class CarrierSetting extends Fragment {
    private TextInputEditText name, last_name, country, city, phone, email, rating, password;
    private Button save;
    private User user = new User();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.carrier_setting, container, false);
        findView(root);
        writeInput();
        onClick();
        saveVisible();
        return root;
    }

    private void saveVisible() {
        editChange(name, StaticUser.getName());
        editChange(last_name, StaticUser.getLast_name());
        editChange(country, StaticUser.getCountry());
        editChange(city, StaticUser.getCity());
        editChange(phone, StaticUser.getPhone());

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
                }else {
                    save.setVisibility(View.GONE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void onClick() {
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidationRegistration registration = new ValidationRegistration(name,
                        last_name, country, city, password, phone, email, password, getContext());
                if (registration.isEmptyInput()){
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("Users")
                            .document(StaticUser.getEmail())
                            .delete();
                    db.collection("Users")
                            .document(StaticUser.getEmail())
                            .set(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    try {
                                        throw new ToastMessage("Изменения сохранены", getContext());
                                    } catch (ToastMessage toastMessage) {
                                        toastMessage.printStackTrace();
                                    }
                                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_carrier);
                                    navController.navigate(R.id.nav_carrier_main);
                                }
                            });

                }
            }
        });
    }

    private void writeInput() {
        name.setText(StaticUser.getName());
        last_name.setText(StaticUser.getLast_name());
        country.setText(StaticUser.getCountry());
        city.setText(StaticUser.getCity());
        phone.setText(StaticUser.getPhone());
        email.setText(StaticUser.getEmail());
        rating.setText(String.valueOf(StaticUser.getRating()));
        password.setText(StaticUser.getPassword());

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
}
