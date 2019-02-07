package com.cryptowallet.deviantx.UI.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Adapters.GainerLoserExcDBRAdapter;
import com.cryptowallet.deviantx.UI.Models.CoinPairs;
import com.cryptowallet.deviantx.Utilities.GsonUtils;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.client.StompClient;
import ua.naiksoftware.stomp.client.StompMessage;

public class ExchangeMarketSubFragment extends Fragment {

    @BindView(R.id.rview_coin_data)
    RecyclerView rview_coin;

    LinearLayoutManager linearLayoutVertical;
    GainerLoserExcDBRAdapter gainerLoserExcDBRAdapter;
    ArrayList<String> gainersLoserList;

    private static final String TAG = "DEVIANTX";
    private StompClient stompClient;
    ArrayList<CoinPairs> allCoinPairs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exchange_market_sub_fragment, container, false);
        ButterKnife.bind(this, view);

        allCoinPairs = new ArrayList<>();

        linearLayoutVertical = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rview_coin.setLayoutManager(linearLayoutVertical);

        Bundle bundle = getArguments();
        String selectedCoinName = bundle.getString("title");

        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://142.93.51.57:3323/deviant/websocket");
        stompClient.connect();

        stompClient.topic("/topic/exchange_pair/" + selectedCoinName).subscribe(new Action1<StompMessage>() {
            @Override
            public void call(StompMessage message) {
                Log.e(TAG, "*****Received " + selectedCoinName + "*****: " + message.getPayload());
                CoinPairs[] coinsStringArray = GsonUtils.getInstance().fromJson(message.getPayload(), CoinPairs[].class);
                allCoinPairs = new ArrayList<CoinPairs>(Arrays.asList(coinsStringArray));

                gainerLoserExcDBRAdapter = new GainerLoserExcDBRAdapter(getActivity(), allCoinPairs, selectedCoinName, false, true);
                rview_coin.setAdapter(gainerLoserExcDBRAdapter);
            }
        });


        return view;
    }


}
