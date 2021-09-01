package com.sharkit.busik.ui.Sender;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sharkit.busik.Adapter.CarrierAdapter;
import com.sharkit.busik.Adapter.SenderAdapter;
import com.sharkit.busik.Entity.Filter;
import com.sharkit.busik.Entity.Flight;
import com.sharkit.busik.Exception.ToastMessage;
import com.sharkit.busik.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class SenderMain extends Fragment implements View.OnClickListener {
    private ImageView profile, filter;
    private TextInputEditText minPricePassenger, maxPricePassenger, maxPriceCargo, minPriceCargo,
    startCountry, finishCountry, startCity, finishCity, startDateDo, startDateAfter, finishDateDo, finishDateAfter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListView listView;
    private ArrayList<Flight> flights = new ArrayList<>();

    private int year, month, day;
    private CollectionReference collectionReference = db.collection("Flights");
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.sender_main, container, false);
        findView(root);
        onClick();
        setAllList();
        return root;
    }

    private void setAllList() {
        collectionReference.get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot :queryDocumentSnapshots){
                            flights.add(queryDocumentSnapshot.toObject(Flight.class));
                        }
                        setAdapter();
                    }
                });
    }

    private void setAdapter() {
        SenderAdapter adapter = new SenderAdapter(getContext(),flights);
        listView.setAdapter(adapter);
    }

    private void onClick() {
        profile.setOnClickListener(this);
        filter.setOnClickListener(this);
    }

    private void createAlertFilter() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view =inflater.inflate(R.layout.sender_filter, null);
        findViewFilter(view);

//            setTextToField();

        onDataChang();
        dialog.setPositiveButton("Применить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    setFilter();
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    @SuppressLint("SimpleDateFormat")
    private void setTextToField() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Calendar calendar = Calendar.getInstance();

        minPriceCargo.setText(String.valueOf(Filter.getMinPriceCargo()));
        maxPriceCargo.setText(String.valueOf(Filter.getMaxPriceCargo()));
        minPricePassenger.setText(String.valueOf(Filter.getMinPricePassenger()));
        maxPricePassenger.setText(String.valueOf(Filter.getMaxPricePassenger()));
        startCountry.setText(Filter.getStartCountry());
        finishCountry.setText(Filter.getFinishCountry());
        startCity.setText(Filter.getStartCity());
        finishCity.setText(Filter.getFinishCity());
        startDateDo.setText(simpleDateFormat.format(Filter.getStartDateDo()));
        finishDateDo.setText(simpleDateFormat.format(Filter.getFinishDateDo()));
        startDateAfter.setText(simpleDateFormat.format(Filter.getStartDateAfter()));
        finishDateAfter.setText(simpleDateFormat.format(Filter.getFinishDateAfter()));
    }

    private void onDataChang() {
        startDateDo.setOnClickListener(v -> createDataPicker(startDateDo));
        finishDateDo.setOnClickListener(v -> createDataPicker(finishDateDo));
        startDateAfter.setOnClickListener(v -> createDataPicker(startDateAfter));
        finishDateAfter.setOnClickListener(v -> createDataPicker(finishDateAfter));
    }

    private void createDataPicker(TextInputEditText text) {

        DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth, 0, 0 , 0);
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                text.setText(format.format(calendar.getTimeInMillis()));
                if (text == startDateDo){
                    Filter.setStartDateDo(calendar.getTimeInMillis());
                }else if (text == startDateAfter){
                    Filter.setStartDateAfter(calendar.getTimeInMillis());
                }else if (text == finishDateDo){
                    Filter.setFinishDateDo(calendar.getTimeInMillis());
                }else if (text == finishDateAfter){
                    Filter.setFinishDateAfter(calendar.getTimeInMillis());
                }
            }
        }, year, month, day);

        dialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dialog.setOnDismissListener(DialogInterface::dismiss);
        dialog.show();

    }

    private void setFilter(){
        Query query = collectionReference;
        if (!TextUtils.isEmpty(minPricePassenger.getText())){
            query = getQueryWhereGreaterThan(query, "pricePassenger", Float.parseFloat(minPricePassenger.getText().toString()));
        }
        if (!TextUtils.isEmpty(maxPricePassenger.getText())){
            query = getQueryWhereLessThen(query, "pricePassenger", Float.parseFloat(maxPricePassenger.getText().toString()));
        }
        if (!TextUtils.isEmpty(minPriceCargo.getText())){
            query = getQueryWhereGreaterThan(query, "priceCargo", Float.parseFloat(minPriceCargo.getText().toString()));
        }
        if (!TextUtils.isEmpty(maxPriceCargo.getText())){
            query = getQueryWhereLessThen(query, "priceCargo", Float.parseFloat(maxPriceCargo.getText().toString()));
        }
        if (!TextUtils.isEmpty(startCountry.getText())){
            query = getQueryWhereEqualTo (query,"startCountry", startCountry.getText().toString());
        }
        if (!TextUtils.isEmpty(finishCountry.getText())){
            query = getQueryWhereEqualTo(query,"finishCountry", finishCountry.getText().toString());
        }
        if (!TextUtils.isEmpty(startCity.getText())){
            query = getQueryWhereEqualTo (query,"startCity", startCity.getText().toString());
        }
        if (!TextUtils.isEmpty(finishCity.getText())){
            query = getQueryWhereEqualTo(query,"finishCity", finishCity.getText().toString());
        }
        if (!TextUtils.isEmpty(startDateDo.getText())){
            query = getQueryWhereGreater(query, "startDate", Filter.getStartDateDo());
        }
        if (!TextUtils.isEmpty(startDateDo.getText())){
            query = getQueryWhereLess(query, "finishDate", Filter.getFinishDateDo());
        }
        if (!TextUtils.isEmpty(finishDateDo.getText())){
            query = getQueryWhereGreater(query, "startDate", Filter.getStartDateDo());
        }
        if (!TextUtils.isEmpty(finishDateDo.getText())){
            query = getQueryWhereLess(query, "finishDate", Filter.getFinishDateDo());
        }

        Calendar calendar = Calendar.getInstance();
        query.whereGreaterThan("finishDate", calendar.getTimeInMillis()+8640000)
                .whereEqualTo("status", "Ожидает")
                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                try {
                    throw new ToastMessage(queryDocumentSnapshots.size()+"", getContext());
                } catch (ToastMessage toastMessage) {
                    toastMessage.printStackTrace();
                }
            }
        });

    }

    private Query getQueryWhereLess(Query query, String key, long value){
        if (query == null){
            return collectionReference.whereLessThanOrEqualTo(key, value);
        }else {
            return query.whereLessThanOrEqualTo(key, value);
        }
    }
    private Query getQueryWhereLessThen(Query query, String key, float value){
        if (query == null){
            return collectionReference.whereLessThanOrEqualTo(key, value);
        }else {
            return query.whereLessThanOrEqualTo(key, value);
        }
    }

    private Query getQueryWhereGreater(Query query, String key, long value){
        if (query == null){
            return collectionReference.whereGreaterThanOrEqualTo(key, value);
        }else {
            return query.whereGreaterThanOrEqualTo(key, value);
        }
    }

    private Query getQueryWhereGreaterThan(Query query, String key, float value){
        if (query == null){
            return collectionReference.whereGreaterThanOrEqualTo(key, value);
        }else {
            return query.whereGreaterThanOrEqualTo(key, value);
        }
    }
    private Query getQueryWhereEqualTo(Query query, String key, String value){
        if(query == null){
            return  collectionReference.whereEqualTo(key, value);
        }else{
            return  query.whereEqualTo(key, value);
        }
    }

    private void findViewFilter(View view) {
        minPriceCargo = view.findViewById(R.id.min_price_cargo_xml);
        maxPriceCargo = view.findViewById(R.id.max_price_cargo_xml);
        minPricePassenger = view.findViewById(R.id.min_price_passenger_xml);
        maxPricePassenger = view.findViewById(R.id.max_price_passenger_xml);
        startCountry = view.findViewById(R.id.start_country_xml);
        finishCountry = view.findViewById(R.id.finish_country_xml);
        startCity = view.findViewById(R.id.start_city_xml);
        finishCity = view.findViewById(R.id.finish_city_xml);
        startDateDo = view.findViewById(R.id.start_date_do_xml);
        startDateAfter = view.findViewById(R.id.start_date_after_xml);
        finishDateDo = view.findViewById(R.id.finish_date_do_xml);
        finishDateAfter = view.findViewById(R.id.finish_date_after_xml);
    }

    private void findView(View root) {
        listView = root.findViewById(R.id.list_xml);
        profile = root.findViewById(R.id.profile_xml);
        filter = root.findViewById(R.id.filter_xml);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_sender);
        switch (v.getId()){
            case R.id.filter_xml:
                createAlertFilter();
                break;
            case R.id.profile_xml:
                navController.navigate(R.id.nav_sender_profile);
                break;

        }
    }
}
