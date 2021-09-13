package com.sharkit.busik.Adapter.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sharkit.busik.R;

import java.util.ArrayList;

public class AdminOwnNameAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<String> list;
    private TextView ownName;

    public AdminOwnNameAdapter(Context mContext, ArrayList<String> list) {
        this.mContext = mContext;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_admin_country_cities, null);
        }
        findView(convertView);
        writeToField(position);
        return convertView;
    }

    private void writeToField(int position) {
        ownName.setText(list.get(position));
    }

    private void findView(View convertView) {
        ownName = convertView.findViewById(R.id.name_xml);
    }
}
