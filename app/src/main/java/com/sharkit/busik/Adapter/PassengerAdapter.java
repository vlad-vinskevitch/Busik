package com.sharkit.busik.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.firestore.FirebaseFirestore;
import com.sharkit.busik.Entity.ElseVariable;
import com.sharkit.busik.Entity.Passenger;
import com.sharkit.busik.Entity.StaticUser;
import com.sharkit.busik.Entity.User;
import com.sharkit.busik.Exception.ToastMessage;
import com.sharkit.busik.R;

import java.util.ArrayList;
import java.util.Map;

public class PassengerAdapter extends BaseAdapter {
    private final Context mContext;
    private final ArrayList<User> mGroup;
    private Map<String, Passenger> passengers;
    private Button accept, cancel, info;
    private TextView name, phone, status;

    public PassengerAdapter(Context mContext, ArrayList<User> mGroup, Map<String, Passenger> passengers) {
        this.mContext = mContext;
        this.mGroup = mGroup;
        this.passengers = passengers;
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
            convertView = inflater.inflate(R.layout.item_application_on_passenger, null);
        }
        findView(convertView);
        writeField(position);
        onClick(position);
        Adaptive();

        return convertView;
    }
    public void Adaptive(){
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        int h = metrics.heightPixels;
        int w = metrics.widthPixels;

        if(h  > 1800){
            name.setTextSize(14);
            phone.setTextSize(14);
            status.setTextSize(14);

            LinearLayout.LayoutParams button_params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,(int)(h / 17), 1f );
            button_params.setMargins(10,10,10,5);
            accept.setLayoutParams(button_params);
            cancel.setLayoutParams(button_params);
            info.setLayoutParams(button_params);
        }else {
            name.setTextSize(11);
            phone.setTextSize(11);
            status.setTextSize(11);

            LinearLayout.LayoutParams button_params = new LinearLayout.LayoutParams(-2,(int)(h / 16.2), 1f);
            button_params.setMargins(10,10,10,5);
            accept.setLayoutParams(button_params);
            cancel.setLayoutParams(button_params);
            info.setLayoutParams(button_params);
        }



    }

    private void findView(View convertView) {

        name = convertView.findViewById(R.id.name_xml);
        phone = convertView.findViewById(R.id.phone_xml);
        status = convertView.findViewById(R.id.status_xml);
        accept = convertView.findViewById(R.id.accept_xml);
        cancel = convertView.findViewById(R.id.cancel_xml);
        info = convertView.findViewById(R.id.info_xml);
    }

    private void onClick(int position) {
        NavController navController = Navigation.findNavController((Activity) mContext, R.id.nav_host_carrier);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ElseVariable.setProfile(mGroup.get(position).getEmail());
                navController.navigate(R.id.nav_profile);
            }
        });
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (passengers.get(mGroup.get(position).getEmail()).getStatus().equals("Подтвержден")){
                    ToastMessage("Вы уже подтвердили даного пассажира");
                    return;
                }
                updateStatus("Подтвержден", position);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (passengers.get(mGroup.get(position).getEmail()).getStatus().equals("Отменен")){
                    ToastMessage("Вы уже сняли с посадки даного пассажира");
                    return;
                }
                updateStatus("Отменен", position);
            }
        });


    }

    private void updateStatus(String status, int position) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Passenger passenger = passengers.get(mGroup.get(position).getEmail());
        passenger.setStatus(status);
        passengers.put(mGroup.get(position).getEmail(), passenger);
        db.collection("Flights")
                .document(ElseVariable.getNameFlight())
                .update("passengers",passengers);
        NavController navController = Navigation.findNavController((Activity) mContext, R.id.nav_host_carrier);
        navController.navigate(R.id.nav_carrier_passengers);

    }
    private void ToastMessage(String message){
        try {
            throw new ToastMessage(message, mContext);
        } catch (ToastMessage toastMessage) {
            toastMessage.printStackTrace();
        }
    }
    @SuppressLint("SetTextI18n")
    private void writeField(int position) {
        name.setText(mGroup.get(position).getName() + " " + mGroup.get(position).getLast_name());
        phone.setText(mGroup.get(position).getPhone());
        status.setText((CharSequence) passengers.get(mGroup.get(position).getEmail()).getStatus());
    }
}
