package com.cryptowallet.deviantx.UI.Fragments;

import android.app.ProgressDialog;
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
import com.cryptowallet.deviantx.UI.Interfaces.CoinPairsUIListener;
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
    String selectedCoinName;


    @Override
    public void onResume() {
        super.onResume();
//        myApplication.setCoinPairsUIListener(coinPairsUIListener);
    }

    @Override
    public void onPause() {
        super.onPause();
//        myApplication.setCoinPairsUIListener(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.exchange_market_sub_fragment, container, false);
        ButterKnife.bind(this, view);

        allCoinPairs = new ArrayList<>();

        linearLayoutVertical = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rview_coin.setLayoutManager(linearLayoutVertical);

        Bundle bundle = getArguments();
        selectedCoinName = bundle.getString("title");

       /* Intent serviceIntent = new Intent(getActivity(), CoinPairsFetch.class);
        serviceIntent.putExtra(CONSTANTS.selectedCoinName, selectedCoinName);
        getActivity().startService(serviceIntent);
*/
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, "ws://142.93.51.57:3323/deviant/websocket");
        stompClient.connect();

        stompClient.topic("/topic/exchange_pair/" + selectedCoinName).subscribe(new Action1<StompMessage>() {
            @Override
            public void call(StompMessage message) {
                Log.e(TAG, "*****Received " + selectedCoinName + "*****: EMSFselectedTab" + message.getPayload());
                CoinPairs[] coinsStringArray = GsonUtils.getInstance().fromJson(message.getPayload(), CoinPairs[].class);
                allCoinPairs = new ArrayList<CoinPairs>(Arrays.asList(coinsStringArray));
//                stompClient.disconnect();

//                if (allCoinPairs.size() > 0) {
                    gainerLoserExcDBRAdapter = new GainerLoserExcDBRAdapter(getActivity(), allCoinPairs, selectedCoinName, false, true);
                    rview_coin.setAdapter(gainerLoserExcDBRAdapter);
//                    stompClient.reconnect();
                /*}*/ /*else {
                    stompClient.disconnect();
                }*/

            }
        });


        return view;
    }


    CoinPairsUIListener coinPairsUIListener = new CoinPairsUIListener() {
        @Override
        public void onChangedCoinPairs(String selectedCoinName, ArrayList<CoinPairs> coinPairs) {
            gainerLoserExcDBRAdapter = new GainerLoserExcDBRAdapter(getActivity(), allCoinPairs, selectedCoinName, false, true);
            rview_coin.setAdapter(gainerLoserExcDBRAdapter);
        }
    };


}
