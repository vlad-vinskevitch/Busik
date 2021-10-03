package com.sharkit.busik.ui.Carrier;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sharkit.busik.Adapter.ReviewsAdapter;
import com.sharkit.busik.Entity.ElseVariable;
import com.sharkit.busik.Entity.Flight;
import com.sharkit.busik.Entity.Passenger;
import com.sharkit.busik.Entity.Review;
import com.sharkit.busik.Entity.StaticUser;
import com.sharkit.busik.Entity.User;
import com.sharkit.busik.Exception.ToastMessage;
import com.sharkit.busik.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class CarrierReviewProfile extends Fragment {
    private TextView name, phone, country, city, rating;
    private ListView list_reviews;
    private final ArrayList<Review> reviews = new ArrayList<>();
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static User user;
    private static Flight flight;
    private Button addReview;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.profile, container, false);
        findView(root);
        getFieldFromDB();
        onClick();
        getListReviews();
        return root;
    }

    private void onClick() {
        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ElseVariable.getStatusPassenger().equals("Подтвержден")){
                    setToastMessage("Вы должны подтвердить пассажира");
                    return;
                }
                if (flight.getStatus().equals("Ожидает")){
                    setToastMessage("Вы сначала должны завершить рейс");
                    return;
                }
                addReview();
            }
        });
    }

    private void addReview() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.create_reviews, null);
        RatingBar ratingBar = view.findViewById(R.id.rating_xml);
        TextInputEditText text = view.findViewById(R.id.text_xml);
        dialog.setOnDismissListener(DialogInterface::dismiss);
        dialog.setPositiveButton("Оставить отзыв", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(text.getText())){
                    setToastMessage("Введите сообщение");
                    return;
                }
                createReview(text.getText().toString(), ratingBar.getRating(), dialog);
            }
        });

        dialog.setView(view);
        dialog.show();
    }

    private void createReview(String text, float ratingBarRating, DialogInterface dialog) {
        Review review = new Review();
        Calendar calendar = Calendar.getInstance();
        review.setText(text);
        review.setDate(calendar.getTimeInMillis());
        review.setRecipient(user.getEmail());
        review.setRating(ratingBarRating);
        review.setOwner(StaticUser.getName() + " " + StaticUser.getLast_name());
        review.setFlight(flight.getStartCountry() + "(" + flight.getStartCity() + ") - " +
                flight.getFinishCountry() + "(" + flight.getFinishCity() + ")");

        db.collection("Users")
                .whereEqualTo("email", user.getEmail())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                            user = queryDocumentSnapshot.toObject(User.class);
                        }

                        db.collection("Users")
                                .document(user.getEmail())
                                .update("rating", user.getRating() + ratingBarRating);

                        db.collection("Reviews")
                                .document(String.valueOf(review.getDate()))
                                .set(review)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        setToastMessage("Отзыв оставлен");
                                        dialog.dismiss();
                                    }
                                });
                        Navigation.findNavController(getActivity(), R.id.nav_host_carrier).navigate(R.id.nav_profile);
                    }
                });
    }


    private void setToastMessage(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
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

        db.collection("Flights")
                .document(ElseVariable.getNameFlight())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        flight = documentSnapshot.toObject(Flight.class);
                    }
                });
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
        addReview = root.findViewById(R.id.add_reviews_xml);
        name = root.findViewById(R.id.name_xml);
        phone = root.findViewById(R.id.phone_xml);
        country = root.findViewById(R.id.country_xml);
        city = root.findViewById(R.id.city_xml);
        list_reviews = root.findViewById(R.id.list_reviews_xml);
        rating = root.findViewById(R.id.rating_xml);
    }

}
