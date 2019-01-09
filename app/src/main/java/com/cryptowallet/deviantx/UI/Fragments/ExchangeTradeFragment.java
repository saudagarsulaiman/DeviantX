package com.cryptowallet.deviantx.UI.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Activities.ExchangeCoinInfoActivity;
import com.cryptowallet.deviantx.UI.Activities.ExchangeOrderHistoryActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExchangeTradeFragment extends Fragment {

    @BindView(R.id.img_chart)
    ImageView img_chart;
    @BindView(R.id.img_history)
    ImageView img_history;
    @BindView(R.id.txt_title)
    TextView txt_title;

    View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.exchange_trade_fragment, container, false);
        ButterKnife.bind(this, view);

        img_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ExchangeOrderHistoryActivity.class);
                startActivity(intent);
            }
        });
        img_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ExchangeCoinInfoActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }


}
