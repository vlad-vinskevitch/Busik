package com.sharkit.busik.ui.Sender;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
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
import java.util.List;

public class SenderMain extends Fragment implements View.OnClickListener {
    private ImageView profile, filter;

    private TextInputEditText minPricePassenger, maxPricePassenger, maxPriceCargo, minPriceCargo,
            startDateDo, startDateAfter, finishDateDo, finishDateAfter;
    private AutoCompleteTextView startCountry,finishCountry, startCity, finishCity;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ListView listView;
    private ArrayList<Flight> flights;
    private Calendar calendar = Calendar.getInstance();


    private int year, month, day;
    private CollectionReference collectionReference = db.collection("Flights");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.sender_main, container, false);
        findView(root);
        onClick();
        setAllList(collectionReference);
        return root;
    }

    private void setAllList(Query query) {
        flights = new ArrayList<>();
                query
//                        .whereGreaterThan("finishDate", calendar.getTimeInMillis()+8640000)
                        .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot :queryDocumentSnapshots){
                            flights.add(queryDocumentSnapshot.toObject(Flight.class));
                        }
                        setAdapter();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAGA", e.getMessage());
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
        View view = inflater.inflate(R.layout.sender_filter, null);
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
        Query queryPricePassenger = collectionReference;
        if (!TextUtils.isEmpty(minPricePassenger.getText())){
            queryPricePassenger = getQueryWhereGreaterThan(queryPricePassenger, "pricePassenger", Float.parseFloat(minPricePassenger.getText().toString().trim()));
        }
        if (!TextUtils.isEmpty(maxPricePassenger.getText())){
            queryPricePassenger = getQueryWhereLessThen(queryPricePassenger, "pricePassenger", Float.parseFloat(maxPricePassenger.getText().toString().trim()));
        }
        Task taskPrisePassenger = queryPricePassenger.get();
        Query queryPriceCargo = collectionReference;
        if (!TextUtils.isEmpty(minPriceCargo.getText())){
            queryPriceCargo = getQueryWhereGreaterThan(queryPriceCargo, "priceCargo", Float.parseFloat(minPriceCargo.getText().toString().trim()));
        }
        if (!TextUtils.isEmpty(maxPriceCargo.getText())){
            queryPriceCargo = getQueryWhereLessThen(queryPriceCargo, "priceCargo", Float.parseFloat(maxPriceCargo.getText().toString().trim()));
        }
        Task taskPriceCargo = queryPriceCargo.get();
        Query queryCountry = collectionReference;
        if (!TextUtils.isEmpty(startCountry.getText())){
            queryCountry = getQueryWhereEqualTo (queryCountry,"startCountry", startCountry.getText().toString().trim());
        }
        if (!TextUtils.isEmpty(finishCountry.getText())){
            queryCountry = getQueryWhereEqualTo(queryCountry,"finishCountry", finishCountry.getText().toString().trim());
        }
        Task taskCountry = queryCountry.get();
        Query queryCity = collectionReference;
        if (!TextUtils.isEmpty(startCity.getText())){
            queryCity = getQueryWhereEqualTo (queryCity,"startCity", startCity.getText().toString().trim());
        }
        if (!TextUtils.isEmpty(finishCity.getText())){
            queryCity = getQueryWhereEqualTo(queryCity,"finishCity", finishCity.getText().toString().trim());
        }
        Task taskCity = queryCity.get();
        Query queryDateDo = collectionReference;
        if (!TextUtils.isEmpty(startDateDo.getText())){
            queryDateDo = getQueryWhereGreater(queryDateDo, "startDate", Filter.getStartDateDo());
        }
        if (!TextUtils.isEmpty(startDateDo.getText())){
            queryDateDo = getQueryWhereLess(queryDateDo, "finishDate", Filter.getFinishDateDo());
        }
        Task taskDateDo = queryDateDo.get();
        Query queryDateAfter = collectionReference;
        if (!TextUtils.isEmpty(finishDateDo.getText())){
            queryDateAfter = getQueryWhereGreater(queryDateAfter, "startDate", Filter.getStartDateDo());
        }
        if (!TextUtils.isEmpty(finishDateDo.getText())){
            queryDateAfter = getQueryWhereLess(queryDateAfter, "finishDate", Filter.getFinishDateDo());
        }
        Task taskDateAfter = queryDateAfter.get();
        Task<List<QuerySnapshot>> allTasks = Tasks.whenAllSuccess(taskCity, taskCountry, taskDateDo, taskDateAfter,taskPriceCargo,taskPrisePassenger);
        allTasks.addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
            @Override
            public void onSuccess(List<QuerySnapshot> querySnapshots) {


                for (QuerySnapshot queryDocumentSnapshots :querySnapshots){
                    flights = new ArrayList<>();
                    for (QueryDocumentSnapshot queryDocumentSnapshot :queryDocumentSnapshots){
                        flights.add(queryDocumentSnapshot.toObject(Flight.class));
                    }
                }
                setAdapter();
            }
        });












//        setAllList(query);
//        flights = new ArrayList<>();
//        Calendar calendar = Calendar.getInstance();
//        query.whereGreaterThan("finishDate", calendar.getTimeInMillis()+8640000);
//                .whereEqualTo("status", "Ожидает")
//                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                try {
//                    throw new ToastMessage(queryDocumentSnapshots.size()+"", getContext());
//                } catch (ToastMessage toastMessage) {
//                    toastMessage.printStackTrace();
//                }
//            }
//        });

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
