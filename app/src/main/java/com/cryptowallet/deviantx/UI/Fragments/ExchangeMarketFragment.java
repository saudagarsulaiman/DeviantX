package com.cryptowallet.deviantx.UI.Fragments;

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

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.exchange_market_fragment, container, false);
        ButterKnife.bind(this, view);
        coinsList = new ArrayList<>();
//        linearLayoutVertical = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
//        rview_coin.setLayoutManager(linearLayoutVertical);
//        gainersLoserList = new ArrayList<>();

//        gainerLoserExcDBRAdapter = new GainerLoserExcDBRAdapter(getActivity(), gainersLoserList, false, true);
//        rview_coin.setAdapter(gainerLoserExcDBRAdapter);

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

}
