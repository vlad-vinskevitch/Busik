package com.sharkit.busik.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sharkit.busik.Entity.ElseVariable;
import com.sharkit.busik.Entity.Flight;
import com.sharkit.busik.Entity.StaticUser;
import com.sharkit.busik.Exception.ToastMessage;
import com.sharkit.busik.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CarrierAdapter extends BaseAdapter {
    private ArrayList<Flight> mGroup;
    private Context mContext;
    private TextView direction, priceCargo, pricePassenger, startDate, finishDate, status, note;
    private ImageView dropdownMenu;
    private static Flight flight;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();

    public CarrierAdapter(ArrayList<Flight> mGroup, Context mContext) {
        this.mGroup = mGroup;
        this.mContext = mContext;
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

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.carrier_flights_item, null);
        }
        findView(convertView);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy");
        direction.setText(mGroup.get(position).getStartCountry() + "(" + mGroup.get(position).getStartCity() + ") - " +
                mGroup.get(position).getFinishCountry() + "(" + mGroup.get(position).getFinishCity() + ")");
        priceCargo.setText("- " + mGroup.get(position).getPriceCargo() + " $/kg");
        pricePassenger.setText("- " + mGroup.get(position).getPricePassenger() + " $/пассажир");

        startDate.setText(startDate.getText() + " " + simpleDateFormat.format(mGroup.get(position).getStartDate()));
        finishDate.setText(finishDate.getText() + " " + simpleDateFormat.format(mGroup.get(position).getFinishDate()));
        note.setText(note.getText() + " " + mGroup.get(position).getNote());

        dropdownMenuListener(position);


        return convertView;
    }

    private void dropdownMenuListener(int position) {

        NavController navController = Navigation.findNavController((Activity) mContext, R.id.nav_host_carrier);

        dropdownMenu.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                db.collection("Flights")
                        .whereEqualTo("owner", StaticUser.getEmail())
                        .whereEqualTo("startCountry", mGroup.get(position).getStartCountry())
                        .whereEqualTo("finishCountry", mGroup.get(position).getFinishCountry())
                        .whereEqualTo("startCity", mGroup.get(position).getStartCity())
                        .whereEqualTo("finishCity", mGroup.get(position).getFinishCity())
                        .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
                            flight = queryDocumentSnapshot.toObject(Flight.class);
                        }
                    }
                });
                menu.add("Завершить").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                    db.collection("Flights")
                            .document(flight.getName())
                            .update("status", "complete");
                        return true;
                    }
                });
                menu.add("Пассажиры").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        ElseVariable.setNameFlight(flight.getName());
                        navController.navigate(R.id.nav_carrier_passengers);
                        return true;
                    }
                });
                menu.add("Изменить описание").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        createAlertDialogChangeDescription();
                        return true;
                    }
                });
                menu.add("Написать сообщение").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        writeTheMessage();
                        return true;
                    }
                });
                menu.add("Удалить").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        db.collection("Flights")
                                .document(flight.getName())
                                .delete();
                        navController.navigate(R.id.nav_carrier_flights);
                        return true;
                    }
                });
            }
        });
    }

    private void writeTheMessage() {
    }

    private void createAlertDialogChangeDescription() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.change_descriptions, null);

        TextInputEditText text = view.findViewById(R.id.description_xml);
        dialog.setPositiveButton("Изменить", (dialog1, which) -> changeDescription(text.getText().toString()));
        dialog.setOnDismissListener(DialogInterface::dismiss);
        dialog.setView(view);
        dialog.show();
    }

    private void changeDescription( String text) {
        db.collection("Flights")
                .document(flight.getName())
                .update("note", text)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        try {
                            throw new ToastMessage("Описание успешно изменено", mContext );
                        } catch (ToastMessage toastMessage) {
                            toastMessage.printStackTrace();
                        }
                        NavController navController = Navigation.findNavController((Activity) mContext, R.id.nav_host_carrier);
                        navController.navigate(R.id.nav_carrier_flights);
                    }
                });
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
    }
}
