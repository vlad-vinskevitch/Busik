package com.sharkit.busik.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sharkit.busik.Entity.Message;
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
        return convertView;
    }

    @SuppressLint("SimpleDateFormat")
    private void writeToField(int position) {
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");
        date.setText(format.format(mGroup.get(position).getDate()));
        name.setText(mGroup.get(position).getName());
        flight.setText(mGroup.get(position).getFlight());
        text.setText(mGroup.get(position).getMessage());
        status.setText(mGroup.get(position).getStatus());
    }

    private void findView(View convertView) {
        name = convertView.findViewById(R.id.name_xml);
        flight = convertView.findViewById(R.id.flight_xml);
        text = convertView.findViewById(R.id.text_xml);
        status = convertView.findViewById(R.id.status_xml);
        date = convertView.findViewById(R.id.date_xml);
    }
}
