package com.cryptowallet.deviantx.UI.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Adapters.ExchangeCoinsDataPagerAdapter;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.framing.Framedata;
import org.java_websocket.handshake.ServerHandshake;

import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.ByteBuffer;
import java.nio.channels.NotYetConnectedException;
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
//    @BindView(R.id.rview_coin)
//    RecyclerView rview_coin;

//    LinearLayoutManager linearLayoutVertical;
//    GainerLoserExcDBRAdapter gainerLoserExcDBRAdapter;
//    ArrayList<String> gainersLoserList;

    private ExchangeCoinsDataPagerAdapter exchangeCoinsDataPagerAdapter;

    ArrayList<String> coinsList;

    View view;

    private WebSocketClient mWebSocketClient;
    private WebSocket webSocket;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.exchange_market_fragment, container, false);
        ButterKnife.bind(this, view);
        coinsList = new ArrayList<>();

//        connectWebSocketClient();


//        linearLayoutVertical = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        rview_coin.setLayoutManager(linearLayoutVertical);
//        gainersLoserList = new ArrayList<>();

//        gainerLoserExcDBRAdapter = new GainerLoserExcDBRAdapter(getActivity(), gainersLoserList, false, true);
//        rview_coin.setAdapter(gainerLoserExcDBRAdapter);

/*
        coinsList.add("Favorites");
        coinsList.add("DEV");
        coinsList.add("BTC");
        coinsList.add("ETH");
        coinsList.add("BTCP");
        coinsList.add("USDT");
        coinsList.add("LTC");
        coinsList.add("NEM");

        setupViewPager(view_pager_Sup_product);
        tab_lyt_coinsList.setupWithViewPager(view_pager_Sup_product);
*/
//        tab_lyt_coinsList.setSelectedTabIndicator(R.color.graph_brdr_green);
//        tab_lyt_coinsList.setSelectedTabIndicator(getResources().getDrawable(R.drawable.indicator));
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
        }
        viewPager.setAdapter(exchangeCoinsDataPagerAdapter);
    }

    private void connectWebSocketClient() {
        URI uri;
        try {
//            uri = new URI("ws://websockethost:8080");
            uri = new URI("ws://142.93.51.57:3323/deviant/websocket");
//            uri = new URI("ws://142.93.51.57:3323/deviant/websocket/topic/exchange_pair/BTC");
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri, new Draft_17()) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Log.e("Websocket", "Opened");
//                mWebSocketClient.send("Hello from " + Build.MANUFACTURER + " " + Build.MODEL);
                Log.e("Connection:::::1::::", mWebSocketClient.getConnection().toString());
                Log.e("URI:::::1::::", mWebSocketClient.getURI().toString());
//                mWebSocketClient.send("/topic/exchange_pair/BTC");
//                mWebSocketClient.send("/topic/exchange_pair/DEV");
//                String uir = mWebSocketClient.getURI().toString() + "/topic/exchange_pair/BTC";
                String uir = uri + "/topic/exchange_pair/BTC";
//                mWebSocketClient.send(uir);
                Log.e("Success1:::::1::::", "SUCCESS");
                Log.e("UIR:::::1::::", uir);
            }

            @Override
            public void onMessage(String s) {
                final String message = s;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("MESSAGE:::::::::", message);
                        Log.e("Connection:::::2::::", mWebSocketClient.getConnection().toString());
                        Log.e("URI:::::2::::", mWebSocketClient.getURI().toString());

//                        TextView textView = (TextView)findViewById(R.id.messages);
//                        textView.setText(textView.getText() + "\n" + message);
                    }
                });
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.e("Websocket", "Closed " + s);
                Log.e("Connection:::::3::::", mWebSocketClient.getConnection().toString());
                Log.e("URI:::::3::::", mWebSocketClient.getURI().toString());
            }

            @Override
            public void onError(Exception e) {
                Log.e("Websocket", "Error " + e.getMessage());
                Log.e("Connection:::::4::::", mWebSocketClient.getConnection().toString());
                Log.e("URI:::::4::::", mWebSocketClient.getURI().toString());
            }
        };
        mWebSocketClient.connect();
    }

}
