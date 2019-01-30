package com.cryptowallet.deviantx.UI.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;

import java.util.ArrayList;
import java.util.List;

public class SpinnerDaysAdapter extends ArrayAdapter<String> {
    LayoutInflater inflater;
    List<String> objects;
    ViewHolder holder = null;

    public SpinnerDaysAdapter(Context context, int spinner_item_days_dropdown, List<String> categories) {
        super(context, spinner_item_days_dropdown, categories);
        inflater = ((Activity) context).getLayoutInflater();
        this.objects = categories;
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
        String allCoins = objects.get(position);
        View row = convertView;
        if (null == row) {
            holder = new ViewHolder();
            row = inflater.inflate(R.layout.spinner_item_days_dropdown, parent, false);
            holder.name = (TextView) row.findViewById(R.id.txt_spnr_days);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }
        holder.name.setText(allCoins);
        return row;
    }

    public void setAllCoins(ArrayList<String> walletCoinsList) {
        this.objects = walletCoinsList;
        notifyDataSetChanged();
    }

    static class ViewHolder {
        TextView name;
    }
}
