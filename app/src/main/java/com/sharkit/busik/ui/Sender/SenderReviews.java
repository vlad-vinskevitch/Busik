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
import com.sharkit.busik.Adapter.ReviewsAdapter;
import com.sharkit.busik.Entity.ElseVariable;
import com.sharkit.busik.Entity.Review;
import com.sharkit.busik.R;

import java.util.ArrayList;

public class SenderReviews extends Fragment {
    private ListView listReviews;
    private ArrayList<Review> reviews = new ArrayList<>();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.sender_reviews, container, false);
        findView(root);
        getReviewsFromDB();
        return root;
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
        listReviews = root.findViewById(R.id.list_reviews_xml);
    }
}