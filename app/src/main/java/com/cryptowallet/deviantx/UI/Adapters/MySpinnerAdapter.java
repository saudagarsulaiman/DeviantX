package com.cryptowallet.deviantx.UI.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;

import java.util.ArrayList;

public class MySpinnerAdapter extends ArrayAdapter</*AllCoins*/String> {
    LayoutInflater inflater;
    ArrayList</*AllCoins*/String> objects;
    ViewHolder holder = null;

    public MySpinnerAdapter(Context context, int textViewResourceId, ArrayList<String> objects) {
        super(context, textViewResourceId, objects);
        inflater = ((Activity) context).getLayoutInflater();
        this.objects = objects;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        /*AllCoins*/
        String allCoins = objects.get(position);
        View row = convertView;
        if (null == row) {
            holder = new ViewHolder();
            row = inflater.inflate(R.layout.spinner_row_lyt, parent, false);
            holder.name = (TextView) row.findViewById(R.id.txt_spnr_coin);
            holder.img = (ImageView) row.findViewById(R.id.img_spnr_coin);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        holder.name.setText(allCoins);
/*
        holder.name.setText(allCoins.getStr_coin_code());
        Picasso.with(getContext()).load(allCoins.getStr_coin_logo()).into(holder.img);
*/
//        holder.img.setBackgroundResource(allCoins.getStr_coin_logo());
        return row;
    }

    static class ViewHolder {
        TextView name;
        ImageView img;
    }
}