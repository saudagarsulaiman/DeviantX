package com.cryptowallet.deviantx.UI.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Activities.ExchangeOrderHistoryActivity;
import com.cryptowallet.deviantx.UI.Adapters.ExpandableListViewAdapter;
import com.cryptowallet.deviantx.UI.Models.AllCoins;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExchangeFundsFragment extends Fragment {

    @BindView(R.id.expandable_list_view)
    ExpandableListView expandable_list_view;
    @BindView(R.id.img_history)
    ImageView img_history;

    private ExpandableListViewAdapter expandableListViewAdapter;

    View view;

    private ArrayList<String> listDataHeader;
    ArrayList<AllCoins> SubHeader, SubHeader1, SubHeader2, SubHeader3;
    private HashMap<String, ArrayList<AllCoins>> listDataChild;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.exchange_funds_fragment, container, false);
        ButterKnife.bind(this, view);

        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<String, ArrayList<AllCoins>>();
        SubHeader = new ArrayList<AllCoins>();
        SubHeader1 = new ArrayList<AllCoins>();
        SubHeader2 = new ArrayList<AllCoins>();
        SubHeader3 = new ArrayList<AllCoins>();

        listDataHeader.add("Trade Wallet");
        listDataHeader.add("My Test Wallet");
        listDataHeader.add("Holding Wallet");
        listDataHeader.add("My Test1 Wallet");

        SubHeader.add(new AllCoins(1, "Deviantcoin", "DEV",
                /*getResources().getDrawable(R.drawable.ic_dlg)*/
                "logo", 349.52, 5, 6511321.565,
                5461616.225, 64.56, 54.65, 45.54));

        SubHeader1.add(new AllCoins(2, "Bitcoin", "BTC",
                /*getResources().getDrawable(R.drawable.ic_dlg)*/
                "logo", 546.52, 8, 4453165.565,
                553215.225, 88.56, 55.65, 54.54));
        SubHeader1.add(new AllCoins(3, "Bitcoin Diamond", "BTCD",
                /*getResources().getDrawable(R.drawable.ic_dlg)*/
                "logo", 465.57, 78, 4453165.565,
                553215.225, 88.56, 55.65, 54.54));

        SubHeader2.add(new AllCoins(3, "Ethereum", "ETH",
                /*getResources().getDrawable(R.drawable.ic_dlg)*/
                "logo", 874.65, 8, 65431.565,
                5461616.225, 64.56, 54.65, 45.54));

        SubHeader3.add(new AllCoins(4, "Litcoin", "LTC",
                /*getResources().getDrawable(R.drawable.ic_dlg)*/
                "logo", 5453.52, 18, 54.5,
                457.56, 88.56, 55.65, 54.54));

        listDataChild.put(listDataHeader.get(0), SubHeader);
        listDataChild.put(listDataHeader.get(1), SubHeader1);
        listDataChild.put(listDataHeader.get(2), SubHeader2);
        listDataChild.put(listDataHeader.get(3), SubHeader3);

        expandableListViewAdapter = new ExpandableListViewAdapter(getActivity(), listDataHeader, listDataChild);
        expandable_list_view.setAdapter(expandableListViewAdapter);
//        expandable_list_view.setBackgroundResource(R.drawable.rec_gray_white);


//        Closing/Collapsing Previously Opened ListView
        expandable_list_view.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousItem = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousItem)
                    expandable_list_view.collapseGroup(previousItem);
                previousItem = groupPosition;
            }
        });

        img_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ExchangeOrderHistoryActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }


}
