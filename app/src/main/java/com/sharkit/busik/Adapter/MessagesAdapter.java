package com.sharkit.busik.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.ContextMenu;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sharkit.busik.Entity.Message;
import com.sharkit.busik.Entity.StaticUser;
import com.sharkit.busik.Exception.ToastMessage;
import com.sharkit.busik.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MessagesAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Message> mGroup;
    public MessagesAdapter(ArrayList<Message> mGroup, Context mContext) {
        this.mContext = mContext;
        this.mGroup = mGroup;
    }
    private TextView name, flight, text, status, date;

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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_massage, null);
        }
        findView(convertView);
        writeToField(position);
        setAdaptive();
        onClick(parent,position);
        return convertView;
    }
    private void setAdaptive(){
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        int h  = metrics.heightPixels;
        int w = metrics.widthPixels;

        name.setPadding(60,0,0,0);
        flight.setPadding(0,0,60,0);
//        status.setPadding(60,0,0,0);
        date.setPadding(0,0,60,0);
        text.setPadding(10,0,10,0);

        if(h > 1800){
            text.setPadding(20,0,20,0);
            name.setTextSize(14);
            flight.setTextSize(14);
            text.setTextSize(14);
//            status.setTextSize(14);
            date.setTextSize(14);
        }else {
            text.setPadding(10,0,10,0);
            name.setTextSize(11);
            flight.setTextSize(11);
            text.setTextSize(11);
//            status.setTextSize(11);
            date.setTextSize(11);
        }


    }

    private void onClick(ViewGroup parent, int position) {
        parent.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add("Удалить")
                        .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        db.collection("Users/" + StaticUser.getEmail()+ "/Message")
                                .document(String.valueOf(mGroup.get(position).getDate()))
                                .delete()
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                createToastMessage("Сообщение удалено");
                                NavController navController = Navigation.findNavController((Activity) mContext, R.id.nav_host_sender);
                                navController.navigate(R.id.nav_sender_message);
                            }
                        });
                        return true;
                    }
                });
            }
        });
    }

    @SuppressLint("SimpleDateFormat")
    private void writeToField(int position) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        date.setText(format.format(mGroup.get(position).getDate()));
        name.setText(mGroup.get(position).getName());
        flight.setText(mGroup.get(position).getFlight());
        text.setText(mGroup.get(position).getMessage());
    }

    private void findView(View convertView) {
        name = convertView.findViewById(R.id.name_xml);
        flight = convertView.findViewById(R.id.flight_xml);
        text = convertView.findViewById(R.id.text_xml);
        date = convertView.findViewById(R.id.date_xml);
//        status = convertView.findViewById(R.id.status_xml);
    }
    private void createToastMessage(String message){
        try {
            throw new ToastMessage(message,mContext);
        } catch (ToastMessage toastMessage) {
            toastMessage.printStackTrace();
        }
    }
}
