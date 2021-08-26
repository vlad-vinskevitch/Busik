package com.sharkit.busik.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.sharkit.busik.Entity.Flight;
import com.sharkit.busik.R;

import java.util.ArrayList;

public class SenderAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Flight> mGroup;

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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.carrier_flights_item, null);
        }

        return convertView;
    }
}
