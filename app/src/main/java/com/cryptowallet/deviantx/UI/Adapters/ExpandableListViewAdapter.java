package com.cryptowallet.deviantx.UI.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Models.AllCoins;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by SSS on 3/22/2018.
 */

public class ExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private ArrayList<String> _listDataHeader;
    private HashMap<String, ArrayList<AllCoins>> _listDataChild;
    private ArrayList<AllCoins> SubHeader;

    public ExpandableListViewAdapter(Context _context, ArrayList<String> _listDataHeader, HashMap<String, ArrayList<AllCoins>> _listDataChild) {
        this._context = _context;
        this._listDataHeader = _listDataHeader;
        this._listDataChild = _listDataChild;
        this.SubHeader = new ArrayList<>();
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        String headerText = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandable_list_item_layout, null);
        }

        TextView exp_tview_item = convertView.findViewById(R.id.exp_tview_item);
        ImageView exp_iview_item = convertView.findViewById(R.id.exp_iview_item);

        if (isExpanded) {
            exp_iview_item.setImageResource(R.drawable.up_yellow);
        } else {
            exp_iview_item.setImageResource(R.drawable.down_yellow);
        }
        exp_tview_item.setText(headerText);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

//        String childText = (String) getChild(groupPosition, childPosition);
        SubHeader = _listDataChild.get(_listDataHeader.get(groupPosition));
        String coinName = /*(String) getChild(groupPosition, childPosition)*/SubHeader.get(childPosition).getStr_coin_name();
        String coinCode = /*(String) getChild(groupPosition, childPosition)*/SubHeader.get(childPosition).getStr_coin_code();
        String coinLogo = /*(String) getChild(groupPosition, childPosition)*/SubHeader.get(childPosition).getStr_coin_logo();
        Double coinBal = /*(String) getChild(groupPosition, childPosition)*/SubHeader.get(childPosition).getStr_coin_usdValue();
        Double coinBalUSD = /*(String) getChild(groupPosition, childPosition)*/SubHeader.get(childPosition).getStr_coin_usdValue();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this._context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.expandable_list_sub_item_layout, null);
        }

        ImageView img_coin_logo = convertView.findViewById(R.id.img_coin_logo);
        TextView txt_coin_name = convertView.findViewById(R.id.txt_coin_name);
        TextView txt_coin_code = convertView.findViewById(R.id.txt_coin_code);
        TextView txt_coin_bal = convertView.findViewById(R.id.txt_coin_bal);
        TextView txt_coin_usd_value = convertView.findViewById(R.id.txt_coin_usd_value);

//        Picasso.with(_context).load().into(img_coin_logo);
        txt_coin_name.setText(coinName);
        txt_coin_code.setText(coinCode);
        txt_coin_bal.setText(""+coinBal);
        txt_coin_usd_value.setText(""+coinBalUSD);


//        TvChildItems.setText(childText);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
