package com.sharkit.busik.ui.Carrier;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.sharkit.busik.Adapter.ReviewsAdapter;
import com.sharkit.busik.Entity.ElseVariable;
import com.sharkit.busik.Entity.Review;
import com.sharkit.busik.Entity.User;
import com.sharkit.busik.R;

import java.util.ArrayList;

public class Profile extends Fragment {
    private TextView name, phone, country, city, rating;
    private ListView list_reviews;
    private final ArrayList<Review> reviews = new ArrayList<>();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static User user;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.profile, container, false);
        findView(root);
        getFieldFromDB();
        getListReviews();
        return root;
    }

    private void getListReviews() {
        db.collection("Reviews")
                .whereEqualTo("recipient", ElseVariable.getProfile())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                        reviews.add(queryDocumentSnapshot.toObject(Review.class));
                    }
                   setAdapter();
                });
    }


    private void setAdapter(){
        ReviewsAdapter adapter = new ReviewsAdapter(getContext(), reviews);
        list_reviews.setAdapter(adapter);
    }
    private void getFieldFromDB() {

        db.collection("Users")
                .whereEqualTo("email", ElseVariable.getProfile())
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                        user = queryDocumentSnapshot.toObject(User.class);
                    }
                    writeField();
                });
    }

    @SuppressLint("SetTextI18n")
    private void writeField() {
        name.setText(user.getName() + " " + user.getLast_name());
        phone.setText(user.getPhone());
        country.setText(user.getCountry());
        city.setText(user.getCity());
        rating.setText(String.valueOf(user.getRating()));
    }

    private void findView(View root) {
        name = root.findViewById(R.id.name_xml);
        phone = root.findViewById(R.id.phone_xml);
        country = root.findViewById(R.id.country_xml);
        city = root.findViewById(R.id.city_xml);
        list_reviews = root.findViewById(R.id.list_reviews_xml);
        rating = root.findViewById(R.id.rating_xml);
    }
}
