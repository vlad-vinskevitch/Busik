package com.sharkit.busik.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.LayoutDirection;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sharkit.busik.Entity.ElseVariable;
import com.sharkit.busik.Entity.Flight;
import com.sharkit.busik.Entity.Passenger;
import com.sharkit.busik.Entity.Review;
import com.sharkit.busik.Entity.StaticUser;
import com.sharkit.busik.Entity.User;
import com.sharkit.busik.Exception.ToastMessage;
import com.sharkit.busik.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

public class SenderAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Flight> mGroup;
    private TextView direction, priceCargo, pricePassenger, startDate, finishDate, status, note;
    private ImageView dropdownMenu, filter, profile;
    private Flight flight;
    private Review review;
    private User user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private LinearLayout linear_flight, linear_cargo, linear_passenger, linear_departure,linear_arrival,linear_status,linear_details, linear_item;

    public SenderAdapter(Context mContext, ArrayList<Flight> mGroup) {
        this.mContext = mContext;
        this.mGroup = mGroup;
    }

    @Override
    public int getCount() {
        return mGroup.size();
    }

    @Override
    public Object getItem(int position) {
        return mGroup.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.carrier_flights_item, null);
        }
        findView(convertView);
        writeToField(position);
        setAdaptive();

        dropdownMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu = new PopupMenu(mContext, v);
                MenuInflater menuInflater = menu.getMenuInflater();
                menuInflater.inflate(R.menu.drop_down_sender, menu.getMenu());
                if (mGroup.get(position).getPassengers().containsKey(StaticUser.getEmail())){
                    menu.getMenu().getItem(0).setEnabled(false);
                }else {
                    menu.getMenu().getItem(1).setEnabled(false);
                }

                menu.show();
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.registration_xml:
                                getFlight(position, menu.getMenu().getItem(0), menu.getMenu().getItem(1));
                                break;
                            case R.id.cancel_xml:
                                cancelBoarding(position, menu.getMenu().getItem(0), menu.getMenu().getItem(1));
                                break;
                            case R.id.send_review_xml:
                                leaveReview(position);
                                break;
                            case R.id.information_xml:
//                                ElseVariable.setStatusPassenger(passengers.get(mGroup.get(position).getEmail()).getStatus());
                                ElseVariable.setProfile(mGroup.get(position).getOwner());
                                Navigation.findNavController((Activity) mContext, R.id.nav_host_sender).navigate(R.id.nav_profile);
                            break;
                        }
                        return true;
                    }
                });
            }});


        return convertView;
    }

    @SuppressLint("SetTextI18n")
    private void writeToField(int position) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        direction.setText(mGroup.get(position).getStartCountry() + "(" + mGroup.get(position).getStartCity() + ") - " +
                mGroup.get(position).getFinishCountry() + "(" + mGroup.get(position).getFinishCity() + ")");

        priceCargo.setText(mGroup.get(position).getPriceCargo() + " $/kg");
        pricePassenger.setText(mGroup.get(position).getPricePassenger() + " $/пассажир");
        status.setText(mGroup.get(position).getStatus());
        startDate.setText(simpleDateFormat.format(mGroup.get(position).getStartDate()));
        finishDate.setText(simpleDateFormat.format(mGroup.get(position).getFinishDate()));
        note.setText( mGroup.get(position).getNote());
    }

    public void setAdaptive (){

        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        int h = metrics.heightPixels;
        int w = metrics.widthPixels;


        LinearLayout.LayoutParams linear_params = new LinearLayout.LayoutParams(-1,-2);
        linear_params.setMargins(0,0,0,0);

        linear_flight.setLayoutParams(linear_params);
        linear_cargo.setLayoutParams(linear_params);
        linear_passenger.setLayoutParams(linear_params);
        linear_passenger.setLayoutParams(linear_params);
        linear_details.setLayoutParams(linear_params);
        linear_status.setLayoutParams(linear_params);
        linear_arrival.setLayoutParams(linear_params);
        linear_departure.setLayoutParams(linear_params);

        direction.setPadding(0,0,0,0);
        pricePassenger.setPadding(0,0,0,0);
        priceCargo.setPadding(0,0,0,0);
        startDate.setPadding(0,0,0,0);
        finishDate.setPadding(0,0,0,0);
        status.setPadding(0,0,0,0);

        linear_flight.setPadding(10,0,0,0);
        linear_cargo.setPadding(10,0,0,0);
        linear_passenger.setPadding(10,0,0,0);
        linear_passenger.setPadding(10,0,0,0);
        linear_details.setPadding(10,0,0,0);
        linear_status.setPadding(10,0,0,0);
        linear_arrival.setPadding(10,0,0,0);
        linear_departure.setPadding(10,0,0,0);
        linear_details.setPadding(10,0,0,0);
        linear_item.setPadding(10,10,0,0);





        if(h > 1800){
            LinearLayout.LayoutParams params_drop = new LinearLayout.LayoutParams(150,150);
            params_drop.setMargins(0,10,20,0);
            dropdownMenu.setLayoutParams(params_drop);
            direction.setTextSize(14);
            priceCargo.setTextSize(14);
            pricePassenger.setTextSize(14);
            startDate.setTextSize(14);
            finishDate.setTextSize(14);
            status.setTextSize(14);
            note.setTextSize(14);
        }else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(70,70,1f);
            params.setMargins(0,10,0,0);




            dropdownMenu.setLayoutParams(params);
            direction.setTextSize(12);
            priceCargo.setTextSize(12);
            pricePassenger.setTextSize(12);
            startDate.setTextSize(12);
            finishDate.setTextSize(12);
            status.setTextSize(12);
            note.setTextSize(12);

        }

    }

    private void leaveReview(int position) {

        db.collection("Flights")
                .whereEqualTo("owner", mGroup.get(position).getOwner())
                .whereEqualTo("startCountry", mGroup.get(position).getStartCountry())
                .whereEqualTo("finishCountry", mGroup.get(position).getFinishCountry())
                .whereEqualTo("startCity", mGroup.get(position).getStartCity())
                .whereEqualTo("finishCity", mGroup.get(position).getFinishCity())
                .whereEqualTo("note",mGroup.get(position).getNote())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                            flight = queryDocumentSnapshot.toObject(Flight.class);
                        }
                        validationPassenger(position);
                    }
                });
    }

    private void validationPassenger(int position) {
        if (flight.getPassengers().size() == 0){
            try {
                throw new ToastMessage("Вы должны быть пассажиром даного рейса", mContext);
            } catch (ToastMessage toastMessage) {
                toastMessage.printStackTrace();
            }
            return;
        }


            if (!flight.getPassengers().containsKey(StaticUser.getEmail())){
                try {
                    throw new ToastMessage("Вы должны быть пассажиром даного рейса", mContext);
                } catch (ToastMessage toastMessage) {
                    toastMessage.printStackTrace();
                }
                return;
            }


        if (flight.getStatus().equals("Ожидает")){
            try {
                throw new ToastMessage("Вы не можете оставить отзыв пока рейс в ожидании, если водитель специально не завершает рейс он будет закрыт спустя 48 часов после даты прибытия", mContext);
            } catch (ToastMessage toastMessage) {
                toastMessage.printStackTrace();
            }
            return;
        }

        addReview(position);
    }

    private void addReview(int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.create_reviews, null);
        RatingBar ratingBar = view.findViewById(R.id.rating_xml);
        TextInputEditText text = view.findViewById(R.id.text_xml);
        dialog.setOnDismissListener(DialogInterface::dismiss);
        dialog.setPositiveButton("Оставить отзыв", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (TextUtils.isEmpty(text.getText())){
                    try {
                        throw new ToastMessage("Введите сообщение", mContext);
                    } catch (ToastMessage toastMessage) {
                        toastMessage.printStackTrace();
                    }
                    return;
                }
                if (flight.getPassengers().get(StaticUser.getEmail()).getReview().equals("true")){
                    try {
                        throw new ToastMessage("Вы уже оставили отзыв", mContext);
                    } catch (ToastMessage toastMessage) {
                        toastMessage.printStackTrace();
                    }
                    return;
                }
                createReview(text.getText().toString(), ratingBar.getRating(), position, dialog);
            }
        });

        dialog.setView(view);
        dialog.show();
    }

    private void createReview(String text, float ratingBarRating, int position, DialogInterface dialog) {
        Review review = new Review();
        Calendar calendar = Calendar.getInstance();
        review.setText(text);
        review.setDate(calendar.getTimeInMillis());
        review.setRecipient(mGroup.get(position).getOwner());
        review.setRating(ratingBarRating);
        review.setOwner(StaticUser.getName() + " " + StaticUser.getLast_name());
        review.setFlight(mGroup.get(position).getStartCountry() + "(" + mGroup.get(position).getStartCity() + ") - " +
                mGroup.get(position).getFinishCountry() + "(" + mGroup.get(position).getFinishCity() + ")");

        db.collection("Users")
                .whereEqualTo("email", mGroup.get(position).getOwner())
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

                        Map<String, Passenger> map = flight.getPassengers();
                        Passenger passenger = flight.getPassengers().get(StaticUser.getEmail());
                        passenger.setReview("true");
                        map.put(StaticUser.getEmail(), passenger);

                        db.collection("Flights")
                                .document(flight.getName())
                                .update("passengers", map);

                        db.collection("Reviews")
                                .document(String.valueOf(review.getDate()))
                                .set(review)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        try {
                                            throw new ToastMessage("Отзыв оставлен", mContext);
                                        } catch (ToastMessage toastMessage) {
                                            toastMessage.printStackTrace();
                                        }
                                        dialog.dismiss();
                                    }
                                });
                    }
                });
    }

    private void cancelBoarding(int position, MenuItem item, MenuItem menuItem) {
        db.collection("Flights")
                .whereEqualTo("owner", mGroup.get(position).getOwner())
                .whereEqualTo("startCountry", mGroup.get(position).getStartCountry())
                .whereEqualTo("finishCountry", mGroup.get(position).getFinishCountry())
                .whereEqualTo("startCity", mGroup.get(position).getStartCity())
                .whereEqualTo("finishCity", mGroup.get(position).getFinishCity())
                .whereEqualTo("note",mGroup.get(position).getNote())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("RestrictedApi")
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                            flight = queryDocumentSnapshot.toObject(Flight.class);
                        }
                            if (flight.getPassengers().containsKey(StaticUser.getEmail())) {
                                if (flight.getPassengers().get(StaticUser.getEmail()).getReview().equals("true")) {
                                    deleteReview(position);
                                }
                                flight.getPassengers().remove(StaticUser.getEmail());
                                flight.getEmailsPassengers().remove(StaticUser.getEmail());
                                db.collection("Flights")
                                        .document(flight.getName())
                                        .update("passengers", flight.getPassengers(), "emailsPassengers", flight.getEmailsPassengers());
                                if (Navigation.findNavController((Activity) mContext, R.id.nav_host_sender).getCurrentDestination()
                                        .getDisplayName().equals("com.sharkit.busik:id/nav_main_flights")){
                                    Navigation.findNavController((Activity) mContext, R.id.nav_host_sender).navigate(R.id.nav_main_flights);
                                }
                                Navigation.findNavController((Activity) mContext, R.id.nav_host_sender).navigate(R.id.nav_sender_main);

                                try {
                                    throw new ToastMessage("Вы успешно сняты с посадки", mContext);
                                } catch (ToastMessage toastMessage) {
                                    toastMessage.printStackTrace();
                                }
                                return;
                            }



                        try {
                            throw new ToastMessage("Вы не пассажир этого рейса", mContext);
                        } catch (ToastMessage toastMessage) {
                            toastMessage.printStackTrace();
                        }
                    }
                });
    }

    private void deleteReview(int position) {
        db.collection("Reviews")
                .whereEqualTo("flight", mGroup.get(position).getStartCountry() + "(" + mGroup.get(position).getStartCity() + ") - " +
                        mGroup.get(position).getFinishCountry() + "(" + mGroup.get(position).getFinishCity() + ")")
                .whereEqualTo("owner", StaticUser.getName() + " " + StaticUser.getLast_name())
                .whereEqualTo("recipient", mGroup.get(position).getOwner())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                            review = queryDocumentSnapshot.toObject(Review.class);
                        }
                        db.collection("Users")
                                .whereEqualTo("email", mGroup.get(position).getOwner())
                                .get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                                          @Override
                                                          public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                                              for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                                                                  user = queryDocumentSnapshot.toObject(User.class);
                                                              }

                                                              db.collection("Users")
                                                                      .document(user.getEmail())
                                                                      .update("rating", user.getRating() - review.getRating());
                                                          }
                                                      });

                        db.collection("Reviews")
                                .document(String.valueOf(review.getDate()))
                                .delete();
                    }
                });
    }

    private void getFlight(int position, MenuItem item, MenuItem menuItem) {

        db.collection("Flights")
                .whereEqualTo("owner", mGroup.get(position).getOwner())
                .whereEqualTo("startCountry", mGroup.get(position).getStartCountry())
                .whereEqualTo("finishCountry", mGroup.get(position).getFinishCountry())
                .whereEqualTo("startCity", mGroup.get(position).getStartCity())
                .whereEqualTo("finishCity", mGroup.get(position).getFinishCity())
                .whereEqualTo("note",mGroup.get(position).getNote())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                            flight = queryDocumentSnapshot.toObject(Flight.class);
                        }
                        addPassenger(item,menuItem);
                    }
                });
    }

    private void addPassenger(MenuItem registration, MenuItem cancelBoarding) {


        if (flight.getPassengers().containsKey(StaticUser.getEmail())){
            try {
                throw new ToastMessage("Пассажир уже зарегестрируван на рейс",mContext);
            } catch (ToastMessage toastMessage) {
                toastMessage.printStackTrace();
            }
            return;
        }
                Passenger passenger = new Passenger();
                passenger.setProfile(StaticUser.getEmail());
                passenger.setReview("false");
                passenger.setStatus("Ожидает решения");
                flight.getPassengers().put(StaticUser.getEmail(), passenger);
                flight.getEmailsPassengers().add(StaticUser.getEmail());
                db.collection("Flights")
                        .document(flight.getName())
                        .update("passengers", flight.getPassengers(), "emailsPassengers", flight.getEmailsPassengers());
        Navigation.findNavController((Activity) mContext, R.id.nav_host_sender).navigate(R.id.nav_sender_main);

        try {
            throw new ToastMessage("Посадка выполнена", mContext);
        } catch (ToastMessage toastMessage) {
            toastMessage.printStackTrace();
        }
    }


    private void findView(View convertView) {
        dropdownMenu = convertView.findViewById(R.id.dropdown_menu_xml);
        direction = convertView.findViewById(R.id.direction_xml);
        priceCargo = convertView.findViewById(R.id.price_cargo_xml);
        pricePassenger = convertView.findViewById(R.id.price_passenger_xml);
        startDate = convertView.findViewById(R.id.start_date_xml);
        finishDate = convertView.findViewById(R.id.finish_date_xml);
        status = convertView.findViewById(R.id.status_xml);
        note = convertView.findViewById(R.id.note_xml);

        linear_item = convertView.findViewById(R.id.linear_item);
        linear_flight = convertView.findViewById(R.id.linear_flight);
        linear_cargo = convertView.findViewById(R.id.linear_cargo);
        linear_passenger = convertView.findViewById(R.id.linear_passenger);
        linear_departure = convertView.findViewById(R.id.linear_departure);
        linear_arrival = convertView.findViewById(R.id.linear_arrival);
        linear_status = convertView.findViewById(R.id.linear_status);
        linear_details = convertView.findViewById(R.id.linear_details);

        direction = convertView.findViewById(R.id.direction_xml);
        priceCargo = convertView.findViewById(R.id.price_cargo_xml);
        pricePassenger = convertView.findViewById(R.id.price_passenger_xml);
        startDate = convertView.findViewById(R.id.start_date_xml);
        finishDate =convertView.findViewById(R.id.finish_date_xml);
        status = convertView.findViewById(R.id.status_xml);
        note = convertView.findViewById(R.id.note_xml);
        filter = convertView.findViewById(R.id.filter_xml);
        profile = convertView.findViewById(R.id.profile_xml);
    }
}
