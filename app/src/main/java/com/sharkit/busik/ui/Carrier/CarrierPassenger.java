package com.sharkit.busik.ui.Carrier;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sharkit.busik.Adapter.PassengerAdapter;
import com.sharkit.busik.Entity.ElseVariable;
import com.sharkit.busik.Entity.Flight;
import com.sharkit.busik.Entity.Passenger;
import com.sharkit.busik.Entity.User;
import com.sharkit.busik.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CarrierPassenger extends Fragment {
    private ListView listView;
    private Flight flight;
    private ArrayList<User> users;
    private Map<String, Passenger> passengers;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.passenger_list, container, false);
        findView(root);
        try {
            getListFromDB();
        }catch (IllegalArgumentException e){}
        return root;
    }

    private void getListFromDB() {
        users = new ArrayList<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Flights")
                .document(ElseVariable.getNameFlight())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                         flight = documentSnapshot.toObject(Flight.class);
                         passengers = flight.getPassengers();
                        db.collection("Users")
                                .whereIn("email", Arrays.asList(flight.getPassengers().keySet().toArray()))
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                                            users.add(queryDocumentSnapshot.toObject(User.class));
                                        }
                                        setAdapter();
                                    }
                                });
                    }

                });

    }

    private void setAdapter() {
        PassengerAdapter adapter = new PassengerAdapter(getContext(), users, passengers);
        listView.setAdapter(adapter);
    }

    private void findView(View root) {
        listView = root.findViewById(R.id.passenger_list_xml);
    }
}
