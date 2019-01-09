package com.cryptowallet.deviantx.UI.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Adapters.GainerLoserExcDBRAdapter;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExchangeMarketSubFragment extends Fragment {

    @BindView(R.id.rview_coin_data)
    RecyclerView rview_coin;

    LinearLayoutManager linearLayoutVertical;
    GainerLoserExcDBRAdapter gainerLoserExcDBRAdapter;
    ArrayList<String> gainersLoserList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exchange_market_sub_fragment, container, false);
        ButterKnife.bind(this, view);

        Bundle bundle = getArguments();
        String selectedCoinName = bundle.getString("title");

        linearLayoutVertical = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rview_coin.setLayoutManager(linearLayoutVertical);
        gainersLoserList = new ArrayList<>();

        gainerLoserExcDBRAdapter = new GainerLoserExcDBRAdapter(getActivity(), gainersLoserList,selectedCoinName , false, true);
        rview_coin.setAdapter(gainerLoserExcDBRAdapter);


        return view;
    }


}
