package com.sharkit.busik.ui.Carrier;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sharkit.busik.Entity.Flight;
import com.sharkit.busik.Entity.StaticUser;
import com.sharkit.busik.Exception.NoConnectInternet;
import com.sharkit.busik.Exception.ToastMessage;
import com.sharkit.busik.R;
import com.sharkit.busik.Validation.Configuration;
import com.sharkit.busik.Validation.ValidationFlight;
import com.sharkit.busik.Validation.ValidationRegistration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class NewFlights extends Fragment {
    private AutoCompleteTextView textView;
    private TextInputEditText  priceCargo, pricePassenger, note, startDate, finishDate;
    private Button create;
    private AutoCompleteTextView startCountry, finishCountry, startCity, finishCity;
    private int year, month, day;
    private final Flight flight = new Flight();
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.carrier_add_flights, container, false);
        findView(root);
        onClick();


//        String [] contain = getResources().getStringArray(R.array.countries_array);
//        ArrayAdapter<String> adapter =
//                new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, contain);
//        textView.setAdapter(adapter);
        return root;
    }

    private void onClick() {
        ValidationFlight validationFlight = new ValidationFlight(startCountry,finishCountry,startCity,finishCity,startDate,
                finishDate, priceCargo, pricePassenger, getContext());
        startDate.setOnClickListener(v -> createDatePicker(startDate,false));
        finishDate.setOnClickListener(v -> createDatePicker(finishDate, true));

        create.setOnClickListener(v -> {

            if (!Configuration.hasConnection(getContext())){
                try {
                    throw new NoConnectInternet(getContext());
                } catch (NoConnectInternet noConnectInternet) {
                    noConnectInternet.printStackTrace();
                }
                return;
            }
        if (validationFlight.isValidFlight()){
            writeToObject(flight);
            createNewFlights(flight);
        }


        });
    }

    private void createDatePicker(TextView text, boolean b) {
        DatePickerDialog dialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, dayOfMonth,0,0,0);
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
                if (!b){
                    flight.setStartDate(calendar.getTimeInMillis());
                }else {
                    flight.setFinishDate(calendar.getTimeInMillis());
                }
                text.setText(format.format(calendar.getTimeInMillis()));
            }
        }, year, month, day);
        dialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dialog.setOnDismissListener(DialogInterface::dismiss);
        dialog.show();


    }

    private void createNewFlights(Flight flight) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Calendar calendar = Calendar.getInstance();
        flight.setName(String.valueOf(calendar.getTimeInMillis()));
        db.collection("Flights")
                .document(flight.getName())
                .set(flight)
                .addOnSuccessListener(documentReference -> {
                    try {
                        throw new ToastMessage("Рейс успешно добавлен", getContext());
                    } catch (ToastMessage toastMessage) {
                        toastMessage.printStackTrace();
                    }
                    NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_carrier);
                    navController.navigate(R.id.nav_carrier_main);
                });
    }

    private void writeToObject(Flight flight) {
        flight.setStartCountry(startCountry.getText().toString().trim());
        flight.setFinishCountry(finishCountry.getText().toString().trim());
        flight.setStartCity(startCity.getText().toString().trim());
        flight.setFinishCity(finishCity.getText().toString().trim());
        flight.setPriceCargo(Float.parseFloat(priceCargo.getText().toString().trim()));
        flight.setPricePassenger(Float.parseFloat(pricePassenger.getText().toString().trim()));
        flight.setNote(note.getText().toString().trim());
        flight.setOwner(StaticUser.getEmail());
        flight.setPassengers(new HashMap<>());
        flight.setStatus("Ожидает");
    }

    private void findView(View root) {
//        textView = root.findViewById(R.id.autocomplete_country);
        startCountry = root.findViewById(R.id.start_country_xml);
        finishCountry = root.findViewById(R.id.finish_country_xml);
        startCity = root.findViewById(R.id.start_city_xml);
        finishCity = root.findViewById(R.id.finish_city_xml);
        startDate = root.findViewById(R.id.start_date_xml);
        finishDate = root.findViewById(R.id.finish_date_xml);
        priceCargo = root.findViewById(R.id.price_cargo_xml);
        pricePassenger = root.findViewById(R.id.price_passenger_xml);
        note = root.findViewById(R.id.note_xml);
        create = root.findViewById(R.id.create_xml);
    }
}
