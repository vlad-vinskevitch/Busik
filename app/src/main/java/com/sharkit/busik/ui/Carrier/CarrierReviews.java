package com.sharkit.busik.ui.Carrier;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sharkit.busik.Adapter.ReviewsAdapter;
import com.sharkit.busik.Entity.ElseVariable;
import com.sharkit.busik.Entity.Review;
import com.sharkit.busik.R;

import java.util.ArrayList;

public class CarrierReviews extends Fragment {
    private ListView listReviews;
    private ArrayList<Review> reviews = new ArrayList<>();
    private ImageView back;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.carrier_reviews, container, false);
        findView(root);
        getReviewsFromDB();
        onClick();
        return root;
    }

    private void onClick() {
        back.setOnClickListener(v -> Navigation.findNavController(getActivity(), R.id.nav_host_carrier).navigate(R.id.nav_carrier_main));
    }

    private void getReviewsFromDB() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Reviews")
                .whereEqualTo("recipient", ElseVariable.getProfile())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                            reviews.add(queryDocumentSnapshot.toObject(Review.class));
                        }
                        setAdapter();
                    }
                });
    }

    private void setAdapter() {
        ReviewsAdapter adapter = new ReviewsAdapter(getContext(), reviews);
        listReviews.setAdapter(adapter);
    }

    private void findView(View root) {
        back = root.findViewById(R.id.back_xml);
        listReviews = root.findViewById(R.id.list_reviews_xml);
    }
}
