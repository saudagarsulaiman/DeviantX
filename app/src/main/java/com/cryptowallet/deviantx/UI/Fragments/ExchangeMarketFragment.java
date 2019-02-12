package com.cryptowallet.deviantx.UI.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Adapters.ExchangeCoinsDataPagerAdapter;
import com.cryptowallet.deviantx.UI.Services.CoinPairsFetch;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExchangeMarketFragment extends Fragment {


    //    @BindView(R.id.)
//            ;
    @BindView(R.id.img_search)
    ImageView img_search;
    @BindView(R.id.tab_lyt_coinsList)
    TabLayout tab_lyt_coinsList;
    @BindView(R.id.view_pager_Sup_product)
    ViewPager view_pager_Sup_product;

    private ExchangeCoinsDataPagerAdapter exchangeCoinsDataPagerAdapter;

    ArrayList<String> coinsList;

    View view;

/*
    private static final String TAG = "DEVIANTX";
    private StompClient stompClient;
*/

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.exchange_market_fragment, container, false);
        ButterKnife.bind(this, view);
        coinsList = new ArrayList<>();

/*
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://142.93.51.57:3323/deviant/websocket");
        stompClient.connect();

        stompClient.topic("/topic/exchange_pair/BTC").subscribe(new Action1<StompMessage>() {
            @Override
            public void call(StompMessage message) {
                Log.e(TAG, "*****Received BTC*****: " + message.getPayload());
            }
        });
        stompClient.topic("/topic/exchange_pair/DEV").subscribe(new Action1<StompMessage>() {
            @Override
            public void call(StompMessage message) {
                Log.e(TAG, "*****Received DEV*****: " + message.getPayload());
            }
        });
*/

//        coinsList.add("Favorites");
        coinsList.add("DEV");
        coinsList.add("BTC");
//        coinsList.add("ETH");
//        coinsList.add("BTCP");
//        coinsList.add("USDT");
//        coinsList.add("LTC");
//        coinsList.add("NEM");

        setupViewPager(view_pager_Sup_product);
        tab_lyt_coinsList.setupWithViewPager(view_pager_Sup_product);

        return view;
    }

    private void setupViewPager(ViewPager viewPager) {
        exchangeCoinsDataPagerAdapter = new ExchangeCoinsDataPagerAdapter(getChildFragmentManager());
        for (int i = 0; i < coinsList.size(); i++) {
            Bundle bundle = new Bundle();
            bundle.putString("title", coinsList.get(i));
            ExchangeMarketSubFragment fragment = new ExchangeMarketSubFragment();
            fragment.setArguments(bundle);
            exchangeCoinsDataPagerAdapter.addFrag(fragment, coinsList.get(i));
/*
            Intent serviceIntent = new Intent(getActivity(), CoinPairsFetch.class);
            serviceIntent.putExtra(CONSTANTS.selectedCoinName, coinsList.get(i));
            getActivity().startService(serviceIntent);
*/
        }
        viewPager.setAdapter(exchangeCoinsDataPagerAdapter);

/*
        Intent serviceIntent = new Intent(getActivity(), CoinPairsFetch.class);
        serviceIntent.putExtra(CONSTANTS.selectedCoinName, "");
        getActivity().startService(serviceIntent);
*/

    }


}
