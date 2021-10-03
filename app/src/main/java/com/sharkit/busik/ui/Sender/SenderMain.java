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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

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
import com.sharkit.busik.Adapter.SenderAdapter;
import com.sharkit.busik.Entity.Filter;
import com.sharkit.busik.Entity.Flight;
import com.sharkit.busik.Entity.OwnName;
import com.sharkit.busik.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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
    private ArrayList<String> country;
    private ArrayList<String> city;

    private int year, month, day;
    private CollectionReference collectionReference = db.collection("Flights");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.sender_main, container, false);
        findView(root);
        onClick();
        setAdaptive();
        findCountriesAndCities();
        closeTheFlight();
        return root;
    }

    private void closeTheFlight() {
        collectionReference.whereLessThanOrEqualTo("finishDate", Calendar.getInstance().getTimeInMillis())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for (QueryDocumentSnapshot queryDocumentSnapshot :queryDocumentSnapshots){
                    if (queryDocumentSnapshot.toObject(Flight.class).getFinishDate() + 172800000 < Calendar.getInstance().getTimeInMillis()) {
                        updateStatus(queryDocumentSnapshot.toObject(Flight.class).getName());
                    }
                }
                setAllList();
            }
        });
    }

    private void updateStatus(String name) {
        collectionReference.document(name).update("status", "Завершен");
    }

    private void findCountriesAndCities() {
        country = new ArrayList<>();
        city = new ArrayList<>();
        db.collection("Countries")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot queryDocumentSnapshot :queryDocumentSnapshots){
                        country.add(queryDocumentSnapshot.toObject(OwnName.class).getOwnName());
                    }
                });
        db.collection("Cities")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                        city.add(queryDocumentSnapshot.toObject(OwnName.class).getOwnName());
                    }
                });
    }

    private void setAllList() {
        flights = new ArrayList<>();
                collectionReference
                        .whereGreaterThan("finishDate", Calendar.getInstance().getTimeInMillis())
                        .orderBy("finishDate")
                        .orderBy("startDate", Query.Direction.ASCENDING)
                        .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (QueryDocumentSnapshot queryDocumentSnapshot :queryDocumentSnapshots){
                        flights.add(queryDocumentSnapshot.toObject(Flight.class));
                    }
                    setAdapter();
                }).addOnFailureListener(e -> Log.d("TAGA", e.getMessage()));
    }

    private void setAdapter() {
        SenderAdapter adapter = new SenderAdapter(getContext(),flights);
        listView.setAdapter(adapter);
    }

    private void onClick() {
        profile.setOnClickListener(this);
        filter.setOnClickListener(this);
    }
    private void setAdaptive(){
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int h = metrics.heightPixels;
        int w = metrics.widthPixels;

        if(h < 1800){
            LinearLayout.LayoutParams params_icon = new LinearLayout.LayoutParams(90,90);
            filter.setLayoutParams(params_icon);
            profile.setLayoutParams(params_icon);
        }

    }

    private void createAlertFilter() {
        Filter.setFinishDateDo(0);
        Filter.setFinishDateAfter(0);
        Filter.setStartDateDo(0);
        Filter.setStartDateAfter(0);
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.sender_filter, null);
        findViewFilter(view);
        onDataChang();
        onTextComplete();
        dialog.setPositiveButton("Применить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    setFilter();
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    private void onTextComplete() {
        ArrayAdapter adapterCity = new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line, city);
        startCity.setAdapter(adapterCity);
        finishCity.setAdapter(adapterCity);

        ArrayAdapter adapterCountry = new ArrayAdapter<String>(getContext(),android.R.layout.simple_dropdown_item_1line, country);
        startCountry.setAdapter(adapterCountry);
        finishCountry.setAdapter(adapterCountry);
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

    private void setFilter() {

        Task<List<QuerySnapshot>> allTasks = Tasks.whenAllSuccess(
                getTaskMinPrice("priceCargo", minPriceCargo),
                getTaskMaxPrice("priceCargo", maxPriceCargo),
                getTaskMinPrice("pricePassenger", minPricePassenger),
                getTaskMaxPrice("pricePassenger", maxPricePassenger),
                getTaskWhereEqualTo("startCountry", startCountry),
                getTaskWhereEqualTo("finishCountry", finishCountry),
                getTaskWhereEqualTo("startCity", startCity),
                getTaskWhereEqualTo("finishCity", finishCity),
                getTaskMinDate("startDate", startDateDo, Filter.getStartDateDo()),
                getTaskMaxDate("startDate", startDateAfter, Filter.getStartDateAfter()),
                getTaskMinDate("finishDate", finishDateDo, Filter.getFinishDateDo()),
                getTaskMaxDate("finishDate", finishDateAfter, Filter.getFinishDateAfter())
        );
        allTasks.addOnSuccessListener(new OnSuccessListener<List<QuerySnapshot>>() {
            @Override
            public void onSuccess(List<QuerySnapshot> querySnapshots) {
                ArrayList<String> names = new ArrayList<>();
                flights = new ArrayList<>();
                for (QuerySnapshot queryDocumentSnapshots : querySnapshots) {
                    if (queryDocumentSnapshots.size() == 0) {
                        setAdapter();
                        return;
                    }
                    ArrayList<String> flightArrayList = new ArrayList<>();
                    for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                        flightArrayList.add(queryDocumentSnapshot.toObject(Flight.class).getName());
                        if (names.size() != 0 && !names.contains(queryDocumentSnapshot.toObject(Flight.class).getName())) {
                            flightArrayList.remove(queryDocumentSnapshot.toObject(Flight.class).getName());
                        }
                    }
                        if (flightArrayList.isEmpty()) {
                            setAdapter();
                            return;
                        }
                    if (names.size() != 0) {
                        flightArrayList.retainAll(names);
                    }
                    names = flightArrayList;
                }

                for (int i = 0; i < names.size(); i++) {
                    getToFilter(names.get(i));
                }
            }
        });

    }
    private void getToFilter(String name){
        collectionReference
                .whereEqualTo("name", name)
                .whereGreaterThan("finishDate", Calendar.getInstance().getTimeInMillis())
                .orderBy("finishDate")
                .orderBy("startDate", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots) {
                            flights.add(queryDocumentSnapshot.toObject(Flight.class));
                        }
                        setAdapter();
                    }
                });
    }

    private Task getTaskMinPrice(String key, TextInputEditText edit) {
        if (!TextUtils.isEmpty(edit.getText())) {
            return collectionReference.whereGreaterThanOrEqualTo(key, Float.parseFloat(edit.getText().toString().trim())).get();
        } else {
            return collectionReference.get();
        }
    }

    private Task getTaskMaxPrice(String key, TextInputEditText edit) {
        if (!TextUtils.isEmpty(edit.getText())) {
            return collectionReference.whereLessThanOrEqualTo(key, Float.parseFloat(edit.getText().toString().trim())).get();
        } else {
            return collectionReference.get();
        }
    }

    private Task getTaskWhereEqualTo(String key, AutoCompleteTextView edit) {
        if (!TextUtils.isEmpty(edit.getText())) {
            return collectionReference.whereEqualTo(key, edit.getText().toString().trim()).get();
        } else {
            return collectionReference.get();
        }
    }

    private Task getTaskMinDate(String key, TextInputEditText edit, long date) {
        if (!TextUtils.isEmpty(edit.getText())) {
            return collectionReference.whereGreaterThanOrEqualTo(key, date).get();
        } else {
            return collectionReference.get();
        }
    }

    private Task getTaskMaxDate(String key, TextInputEditText edit, long date) {
        if (!TextUtils.isEmpty(edit.getText())) {
            return collectionReference.whereLessThanOrEqualTo(key, date).get();
        } else {
            return collectionReference.get();
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
