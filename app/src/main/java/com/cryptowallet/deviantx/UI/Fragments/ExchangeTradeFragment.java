package com.cryptowallet.deviantx.UI.Fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.UI.Activities.ExchangeCoinInfoActivity;
import com.cryptowallet.deviantx.UI.Activities.ExchangeOrderHistoryActivity;
import com.cryptowallet.deviantx.UI.Activities.WithdrawADClaimActivity;
import com.cryptowallet.deviantx.UI.Adapters.ExchangeOrderHistoryRAdapter;
import com.cryptowallet.deviantx.UI.Adapters.MarketDephRAdapter;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ExchangeTradeFragment extends Fragment {

    @BindView(R.id.img_chart)
    ImageView img_chart;
    @BindView(R.id.img_history)
    ImageView img_history;
    @BindView(R.id.txt_title)
    TextView txt_title;

    @BindView(R.id.rview_bid)
    RecyclerView rview_bid;
    @BindView(R.id.rview_ask)
    RecyclerView rview_ask;
    @BindView(R.id.lnr_dropdown)
    LinearLayout lnr_dropdown;
    @BindView(R.id.img_dropdown)
    ImageView img_dropdown;

    @BindView(R.id.txt_market)
    TextView txt_market;
    @BindView(R.id.rltv_market)
    RelativeLayout rltv_market;
    @BindView(R.id.rltv_market_view)
    RelativeLayout rltv_market_view;
    @BindView(R.id.txt_limit)
    TextView txt_limit;
    @BindView(R.id.rltv_limit)
    RelativeLayout rltv_limit;
    @BindView(R.id.rltv_limit_view)
    RelativeLayout rltv_limit_view;
    @BindView(R.id.img_limit)
    ImageView img_limit;
    @BindView(R.id.img_market)
    ImageView img_market;
    @BindView(R.id.lnt_btn_market)
    LinearLayout lnt_btn_market;
    @BindView(R.id.lnt_btn_limit)
    LinearLayout lnt_btn_limit;
    @BindView(R.id.txt_btn_buy)
    TextView txt_btn_buy;
    @BindView(R.id.txt_btn_sell)
    TextView txt_btn_sell;
    @BindView(R.id.txt_btn_limit)
    TextView txt_btn_limit;
    @BindView(R.id.txt_btn_stop)
    TextView txt_btn_stop;

    @BindView(R.id.txt_discount)
    TextView txt_discount;

    @BindView(R.id.lnr_minus_price)
    LinearLayout lnr_minus_price;
    @BindView(R.id.edt_price)
    EditText edt_price;
    @BindView(R.id.txt_code_price)
    TextView txt_code_price;
    @BindView(R.id.lnr_plus_price)
    LinearLayout lnr_plus_price;

    @BindView(R.id.lnr_minus_amount)
    LinearLayout lnr_minus_amount;
    @BindView(R.id.edt_amount)
    EditText edt_amount;
    @BindView(R.id.txt_code_amount)
    TextView txt_code_amount;
    @BindView(R.id.lnr_plus_amount)
    LinearLayout lnr_plus_amount;

    @BindView(R.id.edt_pin)
    EditText edt_pin;
    @BindView(R.id.edt_fees)
    EditText edt_fees;
    @BindView(R.id.txt_total)
    TextView txt_total;

    @BindView(R.id.btn_buy)
    Button btn_buy;
    @BindView(R.id.btn_sell)
    Button btn_sell;
    @BindView(R.id.btn_make_order_limit)
    Button btn_make_order_limit;
    @BindView(R.id.btn_make_order_stop)
    Button btn_make_order_stop;

    @BindView(R.id.rview_order_history)
    RecyclerView rview_order_history;
    @BindView(R.id.lnr_no_trans)
    LinearLayout lnr_no_trans;


    MarketDephRAdapter marketDephRAdapter;
    LinearLayoutManager linearLayoutManagerDephBid, linearLayoutManagerDephAsk, linearLayoutManagerOrdersHistory;

    boolean isShort;
    ArrayList<String> bidList;
    ArrayList<String> askList;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressDialog progressDialog;

    ExchangeOrderHistoryRAdapter exchangeOrderHistoryRAdapter;

    View view;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.exchange_trade_fragment, container, false);
        ButterKnife.bind(this, view);
        sharedPreferences = getActivity().getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        /*final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);*/

        bidList = new ArrayList<>();
        askList = new ArrayList<>();
        isShort = true;

        linearLayoutManagerDephBid = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rview_bid.setLayoutManager(linearLayoutManagerDephBid);
        linearLayoutManagerDephAsk = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rview_ask.setLayoutManager(linearLayoutManagerDephAsk);
        linearLayoutManagerOrdersHistory = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rview_order_history.setLayoutManager(linearLayoutManagerOrdersHistory);


        marketDephRAdapter = new MarketDephRAdapter(getActivity(), true, bidList, isShort);
        rview_bid.setAdapter(marketDephRAdapter);
        marketDephRAdapter = new MarketDephRAdapter(getActivity(), false, askList, isShort);
        rview_ask.setAdapter(marketDephRAdapter);
        exchangeOrderHistoryRAdapter = new ExchangeOrderHistoryRAdapter(getActivity(), true);
        rview_order_history.setAdapter(exchangeOrderHistoryRAdapter);


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

        lnr_dropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isShort) {
                    isShort = false;
                    Picasso.with(getActivity()).load(R.drawable.up_yellow).into(img_dropdown);

                    marketDephRAdapter = new MarketDephRAdapter(getActivity(), true, bidList, isShort);
                    rview_bid.setAdapter(marketDephRAdapter);
                    marketDephRAdapter = new MarketDephRAdapter(getActivity(), false, askList, isShort);
                    rview_ask.setAdapter(marketDephRAdapter);
                } else {
                    isShort = true;
                    Picasso.with(getActivity()).load(R.drawable.down_yellow).into(img_dropdown);

                    marketDephRAdapter = new MarketDephRAdapter(getActivity(), true, bidList, isShort);
                    rview_bid.setAdapter(marketDephRAdapter);
                    marketDephRAdapter = new MarketDephRAdapter(getActivity(), false, askList, isShort);
                    rview_ask.setAdapter(marketDephRAdapter);
                }
            }
        });

        rltv_market.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_market.setTextColor(getResources().getColor(R.color.yellow));
                rltv_market_view.setVisibility(View.VISIBLE);
                img_market.setImageDrawable(getResources().getDrawable(R.drawable.selected_market));
                txt_limit.setTextColor(getResources().getColor(R.color.white));
                rltv_limit_view.setVisibility(View.GONE);
                img_limit.setImageDrawable(getResources().getDrawable(R.drawable.unselected_limit));

                lnt_btn_market.setVisibility(View.VISIBLE);
                lnt_btn_limit.setVisibility(View.GONE);

                txt_btn_limit.setBackground(getResources().getDrawable(R.drawable.selected_buy));
                txt_btn_stop.setBackground(getResources().getDrawable(R.drawable.unselected));

                buttonsVisiblity();
                btn_buy.setVisibility(View.VISIBLE);

            }
        });

        rltv_limit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_limit.setTextColor(getResources().getColor(R.color.yellow));
                rltv_limit_view.setVisibility(View.VISIBLE);
                img_market.setImageDrawable(getResources().getDrawable(R.drawable.unselected_market));
                txt_market.setTextColor(getResources().getColor(R.color.white));
                rltv_market_view.setVisibility(View.GONE);
                img_limit.setImageDrawable(getResources().getDrawable(R.drawable.selected_limit));

                lnt_btn_market.setVisibility(View.GONE);
                lnt_btn_limit.setVisibility(View.VISIBLE);

                txt_btn_buy.setBackground(getResources().getDrawable(R.drawable.selected_buy));
                txt_btn_sell.setBackground(getResources().getDrawable(R.drawable.unselected));

                buttonsVisiblity();
                btn_make_order_limit.setVisibility(View.VISIBLE);

            }
        });


        txt_btn_buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_btn_buy.setBackground(getResources().getDrawable(R.drawable.selected_buy));
                txt_btn_sell.setBackground(getResources().getDrawable(R.drawable.unselected));

                buttonsVisiblity();
                btn_buy.setVisibility(View.VISIBLE);

            }
        });

        txt_btn_sell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_btn_buy.setBackground(getResources().getDrawable(R.drawable.unselected));
                txt_btn_sell.setBackground(getResources().getDrawable(R.drawable.selected_sell));

                buttonsVisiblity();
                btn_sell.setVisibility(View.VISIBLE);

            }
        });

        txt_btn_limit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_btn_limit.setBackground(getResources().getDrawable(R.drawable.selected_buy));
                txt_btn_stop.setBackground(getResources().getDrawable(R.drawable.unselected));

                buttonsVisiblity();
                btn_make_order_limit.setVisibility(View.VISIBLE);

            }
        });

        txt_btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_btn_limit.setBackground(getResources().getDrawable(R.drawable.unselected));
                txt_btn_stop.setBackground(getResources().getDrawable(R.drawable.selected_sell));

                buttonsVisiblity();
                btn_make_order_stop.setVisibility(View.VISIBLE);

            }
        });

        lnr_plus_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edtValue = edt_price.getText().toString().trim();
                Double edtVal = Double.parseDouble(edtValue);
//                Double txtVal = Double.parseDouble(txt_avail_coins.getText().toString().trim());
                edtVal++;
//                if (edtVal > txtVal) {
//                    CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.insufficient_fund));
//                } else {
                edt_price.setText("" + edtVal);
//                }
            }
        });
        lnr_minus_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edt_value = edt_price.getText().toString().trim();
                Double edtVal = Double.parseDouble(edt_value);
                edtVal--;
                if (edtVal < 0) {
                    edt_price.setText("0.0");
                } else {
                    edt_price.setText("" + edtVal);
                }

            }
        });

        lnr_plus_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edtValue = edt_amount.getText().toString().trim();
                Double edtVal = Double.parseDouble(edtValue);
//                Double txtVal = Double.parseDouble(txt_avail_coins.getText().toString().trim());
                edtVal++;
//                if (edtVal > txtVal) {
//                    CommonUtilities.ShowToastMessage(getActivity(), getResources().getString(R.string.insufficient_fund));
//                } else {
                    edt_amount.setText("" + edtVal);
//                }
            }
        });
        lnr_minus_amount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edt_value = edt_amount.getText().toString().trim();
                Double edtVal = Double.parseDouble(edt_value);
                edtVal--;
                if (edtVal < 0) {
                    edt_amount.setText("0.0");
                } else {
                    edt_amount.setText("" + edtVal);
                }

            }
        });



        return view;
    }

    private void buttonsVisiblity() {
        btn_buy.setVisibility(View.GONE);
        btn_sell.setVisibility(View.GONE);
        btn_make_order_limit.setVisibility(View.GONE);
        btn_make_order_stop.setVisibility(View.GONE);
    }


}
