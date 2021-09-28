package com.sharkit.busik.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.sharkit.busik.Entity.ElseVariable;
import com.sharkit.busik.Entity.Flight;
import com.sharkit.busik.Entity.Message;
import com.sharkit.busik.Entity.StaticUser;
import com.sharkit.busik.Exception.ToastMessage;
import com.sharkit.busik.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class CarrierAdapter extends BaseAdapter implements View.OnClickListener {
    private ArrayList<Flight> mGroup;
    private Context mContext;
    private TextView direction, priceCargo, pricePassenger, startDate, finishDate, status, note;
    private ImageView dropdownMenu;
    private LinearLayout linear_item, linear_flight, linear_cargo, linear_passenger, linear_departure,linear_arrival,linear_status,linear_details;
//    private static Flight flight;
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

        setAdaptive();


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

        dropdownMenuListener(position);
        return convertView;
    }

    public void setAdaptive (){

        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        int h = metrics.heightPixels;

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
        note.setPadding(0,0,0,0);

        linear_flight.setPadding(5,0,0,0);
        linear_cargo.setPadding(5,0,0,0);
        linear_passenger.setPadding(5,0,0,0);
        linear_passenger.setPadding(5,0,0,0);
        linear_details.setPadding(5,0,0,0);
        linear_status.setPadding(5,0,0,0);
        linear_arrival.setPadding(5,0,0,0);
        linear_departure.setPadding(5,0,0,0);
        linear_details.setPadding(5,0,0,0);





        if(h > 1800){
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//            params.setMarginEnd(20);
//            params.setMarginStart(20);
//            params.setMargins(0,10,0,10);

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

    private void dropdownMenuListener(int position) {

        NavController navController = Navigation.findNavController((Activity) mContext, R.id.nav_host_carrier);
//        db.collection("Flights")
//                .whereEqualTo("owner", StaticUser.getEmail())
//                .whereEqualTo("startCountry", mGroup.get(position).getStartCountry())
//                .whereEqualTo("finishCountry", mGroup.get(position).getFinishCountry())
//                .whereEqualTo("startCity", mGroup.get(position).getStartCity())
//                .whereEqualTo("finishCity", mGroup.get(position).getFinishCity())
//                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                for (QueryDocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots){
//                    flight = queryDocumentSnapshot.toObject(Flight.class);
//                }
//            }
//        });
        dropdownMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu menu =new PopupMenu(mContext, v);
                MenuInflater menuInflater = menu.getMenuInflater();
                menuInflater.inflate(R.menu.drop_down_carrier,menu.getMenu());
                menu.show();
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){

                            case R.id.close_xml:
                                db.collection("Flights")
                                        .document(mGroup.get(position).getName())
                                        .update("status", "Завершен");
                                navController.navigate(R.id.nav_carrier_flights);
                                break;
                            case R.id.passenger_xml:
                                ElseVariable.setNameFlight(mGroup.get(position).getName());
                                navController.navigate(R.id.nav_carrier_passengers);
                                break;
                            case R.id.change_description_xml:
                                createAlertDialogChangeDescription(position);
                                break;
                            case R.id.write_message_xml:
                                writeTheMessage(position);
                                break;
                            case R.id.delete_xml:
                                creteAlertCancelFlight(position);
                                break;
                        }
                        return true;
                    }
                });
            }
        });
    }

    private void creteAlertCancelFlight(int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_admin_country_cities, null);
        TextView textView = view.findViewById(R.id.name_xml);
        textView.setText("Вы точно хотите отменить рейс? После отмены пассажиры смогут оставить отзывы");
        dialog.setOnDismissListener(DialogInterface::dismiss);
        dialog.setPositiveButton("Подтвердить", (dialog1, which) -> {
            db.collection("Flights").document(mGroup.get(position).getName()).update("status", "Отменен");
            Navigation.findNavController((Activity) mContext, R.id.nav_host_carrier).navigate(R.id.nav_carrier_flights);
        });

        dialog.setNegativeButton("Назад", (dialog12, which) -> dialog12.dismiss());
        dialog.setView(view);
        dialog.show();
    }

    private void writeTheMessage(int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        View view = LayoutInflater.from(mContext).inflate(R.layout.create_new_massage, null);
        TextInputEditText message = view.findViewById(R.id.message_xml);
        dialog.setPositiveButton("Отправить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendNewMessage(message.getText().toString(), position);
            }
        });
        dialog.setOnDismissListener(DialogInterface::dismiss);
        dialog.setView(view);
        dialog.show();
    }

    private void sendNewMessage(String text, int position) {
        Message message = new Message();
        Calendar calendar = Calendar.getInstance();
        message.setMessage(text);
        message.setDate(calendar.getTimeInMillis());
        message.setName(mGroup.get(position).getOwner());
        message.setFlight(mGroup.get(position).getStartCountry() + "(" + mGroup.get(position).getStartCity() + ") - " +
                mGroup.get(position).getFinishCountry() + "(" + mGroup.get(position).getFinishCity() + ")");

        List<Object> list = Arrays.asList(mGroup.get(position).getPassengers().keySet().toArray());
        for (int i = 0; i <list.size(); i++) {
            db.collection("Users/" + list.get(i) + "/Message")
                    .document(String.valueOf(calendar.getTimeInMillis()))
                    .set(message);
        }

        ToastMessage("Сообщение отправлено");


    }

    private void createAlertDialogChangeDescription(int position) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.change_descriptions, null);

        TextInputEditText text = view.findViewById(R.id.description_xml);
        dialog.setPositiveButton("Изменить", (dialog1, which) -> changeDescription(text.getText().toString(), position));
        dialog.setOnDismissListener(DialogInterface::dismiss);
        dialog.setView(view);
        dialog.show();
    }

    private void changeDescription( String text, int position) {
        db.collection("Flights")
                .document(mGroup.get(position).getName())
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
    private void ToastMessage(String message){
        try {
            throw new ToastMessage(message, mContext);
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

    }


    @Override
    public void onClick(View v) {

    }
}
