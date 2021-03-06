package com.sharkit.busik.ui.Carrier;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sharkit.busik.Adapter.CarrierAdapter;
import com.sharkit.busik.Entity.Flight;
import com.sharkit.busik.Entity.Passenger;
import com.sharkit.busik.Entity.StaticUser;
import com.sharkit.busik.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Map;

public class CarrierFlights extends Fragment {
    private Button add;
    private ListView listView;
    private ArrayList<Flight> flights;
    private ArrayList<Boolean> messages;
    private ImageView back;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.carrier_flights, container, false);
        findView(root);
        onClick();
        readFlights();
        return root;
    }

    private void readFlights() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Flights")
                .whereEqualTo("owner", StaticUser.getEmail())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        flights = new ArrayList<>();
                        messages = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                            Flight flight = queryDocumentSnapshot.toObject(Flight.class);
                            try {
                                messages.add(getMessages(flight.getPassengers()));
                            }catch (NullPointerException e){
                                messages.add(false);
                            }
                            flights.add(flight);
                        }
                        setAdapter();
                    }
                });
    }

    private Boolean getMessages(Map<String, Passenger> passengers) {
        ArrayList<Passenger> list = new ArrayList<>(passengers.values());
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getStatus().equals("??????????????")){
                return true;
            }
        }
        return false;
    }

    private void setAdapter() {
        CarrierAdapter adapter = new CarrierAdapter(flights, getContext(), messages);
        listView.setAdapter(adapter);
    }

    private void onClick() {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_carrier);
        add.setOnClickListener(v -> {
            navController.navigate(R.id.nav_carrier_add_flights);
        });
        back.setOnClickListener(v -> navController.navigate(R.id.nav_carrier_main));
    }

    private void findView(View root) {
        back = root.findViewById(R.id.back_xml);
        add = root.findViewById(R.id.add_flights_xml);
        listView = root.findViewById(R.id.list);
    }
}
