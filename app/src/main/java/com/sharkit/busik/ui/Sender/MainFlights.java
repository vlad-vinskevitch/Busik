package com.sharkit.busik.ui.Sender;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sharkit.busik.Adapter.SenderAdapter;
import com.sharkit.busik.Entity.Flight;
import com.sharkit.busik.Entity.StaticUser;
import com.sharkit.busik.R;

import java.util.ArrayList;

public class MainFlights extends Fragment {
    private ListView listView;
    private ArrayList<Flight> flights = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.sender_flights, container, false);
        findView(root);
        getFlights();
        return root;
    }

    private void getFlights() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Flights")
                .whereArrayContains("passengers", StaticUser.getEmail())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                            flights.add(queryDocumentSnapshot.toObject(Flight.class));
                        }
                        setAdapter();
                    }
                });
    }

    private void setAdapter() {
        SenderAdapter adapter = new SenderAdapter(getContext(), flights);
        listView.setAdapter(adapter);
    }

    private void findView(View root) {
        listView = root.findViewById(R.id.list_xml);
    }
}
