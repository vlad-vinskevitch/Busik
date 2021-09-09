package com.sharkit.busik.ui.Carrier;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.sharkit.busik.Entity.StaticUser;
import com.sharkit.busik.R;

import java.util.ArrayList;
import java.util.Calendar;

public class CarrierFlights extends Fragment {
    private Button add;
    private ListView listView;
    private ArrayList<Flight> flights;
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
        Calendar calendar = Calendar.getInstance();
        db.collection("Flights")
                .whereEqualTo("owner", StaticUser.getEmail())
//                .whereGreaterThan("finishDate", calendar.getTimeInMillis()+8640000)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        flights = new ArrayList<>();
                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                            Flight flight = queryDocumentSnapshot.toObject(Flight.class);
                            flights.add(flight);
                        }
                        setAdapter();
                    }
                });
    }

    private void setAdapter() {
        CarrierAdapter adapter = new CarrierAdapter(flights, getContext());
        listView.setAdapter(adapter);
    }

    private void onClick() {
        add.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_carrier);
            navController.navigate(R.id.nav_carrier_add_flights);
        });
    }

    private void findView(View root) {
        add = root.findViewById(R.id.add_flights_xml);
        listView = root.findViewById(R.id.list);
    }
}
