package com.sharkit.busik.ui.Main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sharkit.busik.Admin;
import com.sharkit.busik.Entity.StaticUser;
import com.sharkit.busik.Entity.User;
import com.sharkit.busik.Exception.ToastMessage;
import com.sharkit.busik.MainActivity;
import com.sharkit.busik.R;
import com.sharkit.busik.Sender;
import com.sharkit.busik.Transport;
import com.sharkit.busik.Validation.ValidationAuthorisation;

public class Login extends Fragment {
    private TextInputEditText email, password;
    private Button signIn;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.login, container, false);
        findView(root);
        onClick();
        return root;
    }

    private void onClick() {
        signIn.setOnClickListener(v -> {
            ValidationAuthorisation validationAuthorisation = new ValidationAuthorisation(email, password, getContext());
            authorisation();
        });
    }

    private void authorisation() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();





        mAuth.signInWithEmailAndPassword(email.getText().toString().trim(), password.getText().toString().trim())
                .addOnSuccessListener(authResult -> {
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("Users")
                            .whereEqualTo("email", email.getText().toString())
                            .addSnapshotListener((value, error) -> {
                                for (QueryDocumentSnapshot queryDocumentSnapshot : value){
                                    User user = queryDocumentSnapshot.toObject(User.class);
                                    writeCurrentUser(user);
                                    if (user.getRole().equals("Sender")){
                                        startActivity(new Intent(getActivity(), Sender.class));
                                    }else if (user.getRole().equals("Carrier")){
                                        startActivity(new Intent(getActivity(), Transport.class));
                                    }else if (user.getRole().equals("Admin")){
                                        startActivity(new Intent(getActivity(), Admin.class));
                                    }
                                }
                            });

                }).addOnFailureListener(e -> {
                    try {
                        throw new ToastMessage("Введенные почта или пароль не верны", getContext());
                    } catch (ToastMessage toastMessage) {
                        toastMessage.printStackTrace();
                    }
                });
    }

    private void writeCurrentUser(User user) {
        StaticUser.setName(user.getName());
        StaticUser.setLast_name(user.getLast_name());
        StaticUser.setCountry(user.getCountry());
        StaticUser.setCity(user.getCity());
        StaticUser.setPhone(user.getPhone());
        StaticUser.setEmail(user.getEmail());
        StaticUser.setRole(user.getRole());
        StaticUser.setPassword(user.getPassword());
    }

    private void findView(View root) {
        email = root.findViewById(R.id.email_xml);
        password = root.findViewById(R.id.password_xml);
        signIn = root.findViewById(R.id.sign_xml);
    }
}
