package com.sharkit.busik.Adapter.Admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sharkit.busik.Entity.User;
import com.sharkit.busik.R;

import java.util.ArrayList;

public class AdminUserAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<User> mGroup;
    private TextView name, country, city;

    public AdminUserAdapter(Context mContext, ArrayList<User> mGroup) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_admin_carrier_sender, null);
        }
        findView(convertView);
        writeToField(position);
        return convertView;
    }



    @SuppressLint("SetTextI18n")
    private void writeToField(int position) {
        name.setText(mGroup.get(position).getName() + " " + mGroup.get(position).getLast_name());
        country.setText(mGroup.get(position).getCountry());
        city.setText(mGroup.get(position).getCity());
    }


    private void findView(View convertView) {
        name = convertView.findViewById(R.id.name_xml);
        country = convertView.findViewById(R.id.country_xml);
        city = convertView.findViewById(R.id.city_xml);
    }
}
