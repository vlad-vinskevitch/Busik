package com.sharkit.busik.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sharkit.busik.Entity.Flight;
import com.sharkit.busik.Entity.StaticUser;
import com.sharkit.busik.Exception.ToastMessage;
import com.sharkit.busik.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class SenderAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Flight> mGroup;
    private TextView direction, priceCargo, pricePassenger, startDate, finishDate, status, note;
    private ImageView dropdownMenu;
    private Flight flight;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

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

        dropdownMenu.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add("Зарегистрируватся")
                        .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                    getFlight(position);
                                return true;
                            }
                        });
                menu.add("Отменить посаку")
                        .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                cancelBoarding(position);
                                return true;
                            }
                        });
            }
        });
        return convertView;
    }

    private void cancelBoarding(int position) {
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
                        for (int i = 0; i < flight.getPassengers().size(); i++) {
                            if (flight.getPassengers().get(i).equals(StaticUser.getEmail())) {
                                flight.getPassengers().remove(i);
                                db.collection("Flights")
                                        .document(flight.getName())
                                        .update("passengers", flight.getPassengers());
                                try {
                                    throw new ToastMessage("Вы успешно сняты с посадки", mContext);
                                } catch (ToastMessage toastMessage) {
                                    toastMessage.printStackTrace();
                                }
                                return;
                            }
                        }
                        try {
                            throw new ToastMessage("Вы не пассажир этого рейса", mContext);
                        } catch (ToastMessage toastMessage) {
                            toastMessage.printStackTrace();
                        }
                    }
                });
    }

    private void getFlight(int position) {

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
                        addPassenger();
                    }
                });
    }

    private void addPassenger() {

                for (int i = 0; i < flight.getPassengers().size(); i++){
                    if (flight.getPassengers().get(i).equals(StaticUser.getEmail())){
                        try {
                            throw new ToastMessage("Пассажир уже зарегестрируван на рейс",mContext);
                        } catch (ToastMessage toastMessage) {
                            toastMessage.printStackTrace();
                        }
                        return;
                    }
                }
                flight.getPassengers().add(StaticUser.getEmail());

                db.collection("Flights")
                        .document(flight.getName())
                        .update("passengers", flight.getPassengers());
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
    }
}
