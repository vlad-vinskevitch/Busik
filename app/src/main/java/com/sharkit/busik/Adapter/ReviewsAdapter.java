package com.sharkit.busik.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sharkit.busik.Entity.Review;
import com.sharkit.busik.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ReviewsAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<Review> mGroup;
    private TextView name, date, flight, rating, text_reviews;
    public ReviewsAdapter(Context mContext, ArrayList<Review> mGroup) {
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
            convertView = inflater.inflate(R.layout.item_reviews, null);
        }
        findView(convertView);
        writeToField(position);
        return convertView;
    }

    public void Adaptive(){
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        int h = metrics.heightPixels;
        int w = metrics.widthPixels;
        Log.d("qwerty", "");

        name.setPadding(0,0,0,0);
        date.setPadding(0,0,0,0);
        text_reviews.setPadding(0,0,0,0);
        rating.setPadding(0,0,0,0);
        flight.setPadding(0,0,0,0);



    }
    @SuppressLint("SimpleDateFormat")
    private void writeToField(int position) {
        Adaptive();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        name.setText(mGroup.get(position).getOwner());
        date.setText(dateFormat.format(mGroup.get(position).getDate()));
        text_reviews.setText(mGroup.get(position).getText());
        rating.setText(String.valueOf(mGroup.get(position).getRating()));
        flight.setText(String.valueOf(mGroup.get(position).getFlight()));
    }

    private void findView(View convertView) {
        name = convertView.findViewById(R.id.name_xml);
        date = convertView.findViewById(R.id.date_xml);
        flight = convertView.findViewById(R.id.flight_xml);
        rating = convertView.findViewById(R.id.rating_xml);
        text_reviews = convertView.findViewById(R.id.text_reviews);
    }
}
