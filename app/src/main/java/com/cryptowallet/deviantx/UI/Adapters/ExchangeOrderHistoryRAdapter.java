package com.cryptowallet.deviantx.UI.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.OrderBookControllerApi;
import com.cryptowallet.deviantx.UI.Models.ExcOrders;
import com.cryptowallet.deviantx.UI.Services.ExcOrdersFetch;
import com.cryptowallet.deviantx.UI.Services.WalletDataFetch;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;
import com.orhanobut.dialogplus.DialogPlus;

import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.instabug.library.Instabug.getApplicationContext;

public class ExchangeOrderHistoryRAdapter extends RecyclerView.Adapter<ExchangeOrderHistoryRAdapter.ViewHolder> {
    Context context;
    boolean isOnlyOpen;
    ArrayList<ExcOrders> allExcOrder;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String loginResponseMsg, loginResponseStatus;

    public ExchangeOrderHistoryRAdapter(Context context, ArrayList<ExcOrders> allExcOrder, boolean isOnlyOpen) {
        this.loginResponseMsg = " ";
        this.loginResponseStatus = " ";
        this.context = context;
        this.isOnlyOpen = isOnlyOpen;
        this.allExcOrder = allExcOrder;
        sharedPreferences = context.getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.exc_order_history_lyt, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {

        if (isOnlyOpen) {
            if (allExcOrder.get(i).getStr_orderStatus().equals("partially_executed")) {
                String str = allExcOrder.get(i).getStr_coinPair() + " Partial ";
                String beforeSlash = allExcOrder.get(i).getStr_coinPair().split("/")[0];
                String afterSlash = allExcOrder.get(i).getStr_coinPair().split("/")[1];
                SpannableString spannableString = new SpannableString(/*"LTC/USDT  OPEN ORDER "*/str);
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {
                    }

                    @Override
                    public void updateDrawState(final TextPaint textPaint) {
                        textPaint.setColor(context.getResources().getColor(R.color.black));
                        textPaint.setUnderlineText(false);
//                    BackgroundColorSpan backgroundColorSpan= new BackgroundColorSpan(context.getResources().getColor(R.color.yellow));
                        textPaint.bgColor = context.getResources().getColor(R.color.yellow);
                    }
                };
                if (allExcOrder.get(i).getStr_coinPair().length() > 7) {
                    spannableString.setSpan(new RelativeSizeSpan(0.6f), spannableString.length() - 13, spannableString.length() - 0, 0); // set size
                } else {
                    spannableString.setSpan(new RelativeSizeSpan(0.6f), spannableString.length() - 12, spannableString.length() - 0, 0); // set size
                }
                spannableString.setSpan(clickableSpan, spannableString.length() - 9, spannableString.length() - 0, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.txt_coin_name.setText(spannableString, TextView.BufferType.SPANNABLE);
                viewHolder.txt_coin_name.setHighlightColor(context.getResources().getColor(R.color.yellow));
                viewHolder.txt_coin_name.setMovementMethod(LinkMovementMethod.getInstance());

                viewHolder.txt_date.setText(getTime(allExcOrder.get(i).getStr_createdAt()));
                viewHolder.txt_coin_bal.setText(String.format("%.4f", allExcOrder.get(i).getDbl_amount()));
                viewHolder.txt_coin_bal_usd_code.setText(beforeSlash);
                viewHolder.txt_coin_bal_usd.setText(String.format("%.4f", allExcOrder.get(i).getDbl_total()));
                viewHolder.txt_coin_bal_usd_code.setText(afterSlash);

                viewHolder.lnr_close.setVisibility(View.VISIBLE);

                viewHolder.img_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelOrderDialog(allExcOrder.get(i), i);
                    }
                });

            } else if (allExcOrder.get(i).getStr_orderStatus().equals("pending")) {
                String str = allExcOrder.get(i).getStr_coinPair() + " Pending ";
                String beforeSlash = allExcOrder.get(i).getStr_coinPair().split("/")[0];
                String afterSlash = allExcOrder.get(i).getStr_coinPair().split("/")[1];
                SpannableString spannableString = new SpannableString(/*"LTC/USDT  OPEN ORDER "*/str);
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {
                    }

                    @Override
                    public void updateDrawState(final TextPaint textPaint) {
                        textPaint.setColor(context.getResources().getColor(R.color.black));
                        textPaint.setUnderlineText(false);
//                    BackgroundColorSpan backgroundColorSpan= new BackgroundColorSpan(context.getResources().getColor(R.color.yellow));
                        textPaint.bgColor = context.getResources().getColor(R.color.yellow);
                    }
                };
                if (allExcOrder.get(i).getStr_coinPair().length() > 7) {
                    spannableString.setSpan(new RelativeSizeSpan(0.6f), spannableString.length() - 13, spannableString.length() - 0, 0); // set size
                } else {
                    spannableString.setSpan(new RelativeSizeSpan(0.6f), spannableString.length() - 12, spannableString.length() - 0, 0); // set size
                }
                spannableString.setSpan(clickableSpan, spannableString.length() - 9, spannableString.length() - 0, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.txt_coin_name.setText(spannableString, TextView.BufferType.SPANNABLE);
                viewHolder.txt_coin_name.setHighlightColor(context.getResources().getColor(R.color.yellow));
                viewHolder.txt_coin_name.setMovementMethod(LinkMovementMethod.getInstance());

                viewHolder.txt_date.setText(getTime(allExcOrder.get(i).getStr_createdAt()));
                viewHolder.txt_coin_bal.setText(String.format("%.4f", allExcOrder.get(i).getDbl_amount()));
                viewHolder.txt_coin_bal_usd_code.setText(beforeSlash);
                viewHolder.txt_coin_bal_usd.setText(String.format("%.4f", allExcOrder.get(i).getDbl_total()));
                viewHolder.txt_coin_bal_usd_code.setText(afterSlash);

                viewHolder.lnr_close.setVisibility(View.VISIBLE);

                viewHolder.img_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelOrderDialog(allExcOrder.get(i), i);
                    }
                });

            } else if (allExcOrder.get(i).getStr_orderStatus().equals("open")) {
                String str = allExcOrder.get(i).getStr_coinPair() + " " + allExcOrder.get(i).getStr_orderStatus() + " ";
                String beforeSlash = allExcOrder.get(i).getStr_coinPair().split("/")[0];
                String afterSlash = allExcOrder.get(i).getStr_coinPair().split("/")[1];
                SpannableString spannableString = new SpannableString(/*"LTC/USDT  OPEN ORDER "*/str);
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {
                    }

                    @Override
                    public void updateDrawState(final TextPaint textPaint) {
                        textPaint.setColor(context.getResources().getColor(R.color.black));
                        textPaint.setUnderlineText(false);
//                    BackgroundColorSpan backgroundColorSpan= new BackgroundColorSpan(context.getResources().getColor(R.color.yellow));
                        textPaint.bgColor = context.getResources().getColor(R.color.yellow);
                    }
                };
                if (allExcOrder.get(i).getStr_coinPair().length() > 7) {
                    spannableString.setSpan(new RelativeSizeSpan(0.6f), spannableString.length() - 10, spannableString.length() - 0, 0); // set size
                } else {
                    spannableString.setSpan(new RelativeSizeSpan(0.6f), spannableString.length() - 9, spannableString.length() - 0, 0); // set size
                }
                spannableString.setSpan(clickableSpan, spannableString.length() - 6, spannableString.length() - 0, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.txt_coin_name.setText(spannableString, TextView.BufferType.SPANNABLE);
                viewHolder.txt_coin_name.setHighlightColor(context.getResources().getColor(R.color.yellow));
                viewHolder.txt_coin_name.setMovementMethod(LinkMovementMethod.getInstance());

                viewHolder.txt_date.setText(getTime(allExcOrder.get(i).getStr_createdAt()));
                viewHolder.txt_coin_bal.setText(String.format("%.4f", allExcOrder.get(i).getDbl_amount()));
                viewHolder.txt_coin_bal_usd_code.setText(beforeSlash);
                viewHolder.txt_coin_bal_usd.setText(String.format("%.4f", allExcOrder.get(i).getDbl_total()));
                viewHolder.txt_coin_bal_usd_code.setText(afterSlash);

                viewHolder.lnr_close.setVisibility(View.VISIBLE);

                viewHolder.img_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelOrderDialog(allExcOrder.get(i), i);
                    }
                });
            }

        } else {
            if (allExcOrder.get(i).getStr_orderStatus().equals("open")) {
                String str = allExcOrder.get(i).getStr_coinPair() + " " + allExcOrder.get(i).getStr_orderStatus() + " ";
                String beforeSlash = allExcOrder.get(i).getStr_coinPair().split("/")[0];
                String afterSlash = allExcOrder.get(i).getStr_coinPair().split("/")[1];
                SpannableString spannableString = new SpannableString(/*"LTC/USDT  OPEN ORDER "*/str);
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {
                    }

                    @Override
                    public void updateDrawState(final TextPaint textPaint) {
                        textPaint.setColor(context.getResources().getColor(R.color.black));
                        textPaint.setUnderlineText(false);
//                    BackgroundColorSpan backgroundColorSpan= new BackgroundColorSpan(context.getResources().getColor(R.color.yellow));
                        textPaint.bgColor = context.getResources().getColor(R.color.yellow);
                    }
                };
                if (allExcOrder.get(i).getStr_coinPair().length() > 7) {
                    spannableString.setSpan(new RelativeSizeSpan(0.6f), spannableString.length() - 10, spannableString.length() - 0, 0); // set size
                } else {
                    spannableString.setSpan(new RelativeSizeSpan(0.6f), spannableString.length() - 9, spannableString.length() - 0, 0); // set size
                }
                spannableString.setSpan(clickableSpan, spannableString.length() - 6, spannableString.length() - 0, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.txt_coin_name.setText(spannableString, TextView.BufferType.SPANNABLE);
                viewHolder.txt_coin_name.setHighlightColor(context.getResources().getColor(R.color.yellow));
                viewHolder.txt_coin_name.setMovementMethod(LinkMovementMethod.getInstance());

                viewHolder.txt_date.setText(getTime(allExcOrder.get(i).getStr_createdAt()));
                viewHolder.txt_coin_bal.setText(String.format("%.4f", allExcOrder.get(i).getDbl_amount()));
                viewHolder.txt_coin_bal_usd_code.setText(beforeSlash);
                viewHolder.txt_coin_bal_usd.setText(String.format("%.4f", allExcOrder.get(i).getDbl_total()));
                viewHolder.txt_coin_bal_usd_code.setText(afterSlash);

                viewHolder.lnr_close.setVisibility(View.VISIBLE);

                viewHolder.img_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelOrderDialog(allExcOrder.get(i), i);
                    }
                });
            } else if (allExcOrder.get(i).getStr_orderStatus().equals("cancelled")) {
                String str = allExcOrder.get(i).getStr_coinPair() + " " + allExcOrder.get(i).getStr_orderStatus() + " ";
                String beforeSlash = allExcOrder.get(i).getStr_coinPair().split("/")[0];
                String afterSlash = allExcOrder.get(i).getStr_coinPair().split("/")[1];
                SpannableString spannableString = new SpannableString(/*"LTC/USDT  OPEN ORDER "*/str);
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {
                    }

                    @Override
                    public void updateDrawState(final TextPaint textPaint) {
                        textPaint.setColor(context.getResources().getColor(R.color.white));
                        textPaint.setUnderlineText(false);
//                    BackgroundColorSpan backgroundColorSpan= new BackgroundColorSpan(context.getResources().getColor(R.color.yellow));
                        textPaint.bgColor = context.getResources().getColor(R.color.google_red);
                    }
                };

                if (allExcOrder.get(i).getStr_coinPair().length() > 7) {
                    spannableString.setSpan(new RelativeSizeSpan(0.6f), spannableString.length() - 15, spannableString.length() - 0, 0); // set size
                } else {
                    spannableString.setSpan(new RelativeSizeSpan(0.6f), spannableString.length() - 14, spannableString.length() - 0, 0); // set size
                }
                spannableString.setSpan(clickableSpan, spannableString.length() - 11, spannableString.length() - 0, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                viewHolder.txt_coin_name.setText(spannableString, TextView.BufferType.SPANNABLE);
                viewHolder.txt_coin_name.setHighlightColor(context.getResources().getColor(R.color.yellow));
                viewHolder.txt_coin_name.setMovementMethod(LinkMovementMethod.getInstance());

                viewHolder.txt_date.setText(getTime(allExcOrder.get(i).getStr_createdAt()));
                viewHolder.txt_coin_bal.setText(String.format("%.4f", allExcOrder.get(i).getDbl_amount()));
                viewHolder.txt_coin_bal_usd_code.setText(beforeSlash);
                viewHolder.txt_coin_bal_usd.setText(String.format("%.4f", allExcOrder.get(i).getDbl_total()));
                viewHolder.txt_coin_bal_usd_code.setText(afterSlash);

                viewHolder.lnr_close.setVisibility(View.GONE);
            } else if (allExcOrder.get(i).getStr_orderStatus().equals("executed")) {
                String str = allExcOrder.get(i).getStr_coinPair() + " " + allExcOrder.get(i).getStr_orderStatus() + " ";
                String beforeSlash = allExcOrder.get(i).getStr_coinPair().split("/")[0];
                String afterSlash = allExcOrder.get(i).getStr_coinPair().split("/")[1];
                SpannableString spannableString = new SpannableString(/*"LTC/USDT  OPEN ORDER "*/str);
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {
                    }

                    @Override
                    public void updateDrawState(final TextPaint textPaint) {
                        textPaint.setColor(context.getResources().getColor(R.color.white));
                        textPaint.setUnderlineText(false);
//                    BackgroundColorSpan backgroundColorSpan= new BackgroundColorSpan(context.getResources().getColor(R.color.yellow));
                        textPaint.bgColor = context.getResources().getColor(R.color.graph_brdr_green);
                    }
                };

                if (allExcOrder.get(i).getStr_coinPair().length() > 7) {
                    spannableString.setSpan(new RelativeSizeSpan(0.6f), spannableString.length() - 14, spannableString.length() - 0, 0); // set size
                } else {
                    spannableString.setSpan(new RelativeSizeSpan(0.6f), spannableString.length() - 13, spannableString.length() - 0, 0); // set size
                }
                spannableString.setSpan(clickableSpan, spannableString.length() - 10, spannableString.length() - 0, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                viewHolder.txt_coin_name.setText(spannableString, TextView.BufferType.SPANNABLE);
                viewHolder.txt_coin_name.setHighlightColor(context.getResources().getColor(R.color.yellow));
                viewHolder.txt_coin_name.setMovementMethod(LinkMovementMethod.getInstance());

                viewHolder.txt_date.setText(getTime(allExcOrder.get(i).getStr_createdAt()));
                viewHolder.txt_coin_bal.setText(String.format("%.4f", allExcOrder.get(i).getDbl_amount()));
                viewHolder.txt_coin_bal_usd_code.setText(beforeSlash);
                viewHolder.txt_coin_bal_usd.setText(String.format("%.4f", allExcOrder.get(i).getDbl_total()));
                viewHolder.txt_coin_bal_usd_code.setText(afterSlash);
                viewHolder.lnr_close.setVisibility(View.GONE);
            } else if (allExcOrder.get(i).getStr_orderStatus().equals("partially_executed")) {
                String str = allExcOrder.get(i).getStr_coinPair() + " Partial ";
                String beforeSlash = allExcOrder.get(i).getStr_coinPair().split("/")[0];
                String afterSlash = allExcOrder.get(i).getStr_coinPair().split("/")[1];
                SpannableString spannableString = new SpannableString(/*"LTC/USDT  OPEN ORDER "*/str);
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {
                    }

                    @Override
                    public void updateDrawState(final TextPaint textPaint) {
                        textPaint.setColor(context.getResources().getColor(R.color.black));
                        textPaint.setUnderlineText(false);
//                    BackgroundColorSpan backgroundColorSpan= new BackgroundColorSpan(context.getResources().getColor(R.color.yellow));
                        textPaint.bgColor = context.getResources().getColor(R.color.yellow);
                    }
                };
                if (allExcOrder.get(i).getStr_coinPair().length() > 7) {
                    spannableString.setSpan(new RelativeSizeSpan(0.6f), spannableString.length() - 13, spannableString.length() - 0, 0); // set size
                } else {
                    spannableString.setSpan(new RelativeSizeSpan(0.6f), spannableString.length() - 12, spannableString.length() - 0, 0); // set size
                }
                spannableString.setSpan(clickableSpan, spannableString.length() - 9, spannableString.length() - 0, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.txt_coin_name.setText(spannableString, TextView.BufferType.SPANNABLE);
                viewHolder.txt_coin_name.setHighlightColor(context.getResources().getColor(R.color.yellow));
                viewHolder.txt_coin_name.setMovementMethod(LinkMovementMethod.getInstance());

                viewHolder.txt_date.setText(getTime(allExcOrder.get(i).getStr_createdAt()));
                viewHolder.txt_coin_bal.setText(String.format("%.4f", allExcOrder.get(i).getDbl_amount()));
                viewHolder.txt_coin_bal_usd_code.setText(beforeSlash);
                viewHolder.txt_coin_bal_usd.setText(String.format("%.4f", allExcOrder.get(i).getDbl_total()));
                viewHolder.txt_coin_bal_usd_code.setText(afterSlash);

                viewHolder.lnr_close.setVisibility(View.VISIBLE);

                viewHolder.img_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelOrderDialog(allExcOrder.get(i), i);
                    }
                });

            } else if (allExcOrder.get(i).getStr_orderStatus().equals("pending")) {
                String str = allExcOrder.get(i).getStr_coinPair() + " Pending ";
                String beforeSlash = allExcOrder.get(i).getStr_coinPair().split("/")[0];
                String afterSlash = allExcOrder.get(i).getStr_coinPair().split("/")[1];
                SpannableString spannableString = new SpannableString(/*"LTC/USDT  OPEN ORDER "*/str);
                ClickableSpan clickableSpan = new ClickableSpan() {
                    @Override
                    public void onClick(View textView) {
                    }

                    @Override
                    public void updateDrawState(final TextPaint textPaint) {
                        textPaint.setColor(context.getResources().getColor(R.color.black));
                        textPaint.setUnderlineText(false);
//                    BackgroundColorSpan backgroundColorSpan= new BackgroundColorSpan(context.getResources().getColor(R.color.yellow));
                        textPaint.bgColor = context.getResources().getColor(R.color.yellow);
                    }
                };
                if (allExcOrder.get(i).getStr_coinPair().length() > 7) {
                    spannableString.setSpan(new RelativeSizeSpan(0.6f), spannableString.length() - 13, spannableString.length() - 0, 0); // set size
                } else {
                    spannableString.setSpan(new RelativeSizeSpan(0.6f), spannableString.length() - 12, spannableString.length() - 0, 0); // set size
                }
                spannableString.setSpan(clickableSpan, spannableString.length() - 9, spannableString.length() - 0, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                viewHolder.txt_coin_name.setText(spannableString, TextView.BufferType.SPANNABLE);
                viewHolder.txt_coin_name.setHighlightColor(context.getResources().getColor(R.color.yellow));
                viewHolder.txt_coin_name.setMovementMethod(LinkMovementMethod.getInstance());

                viewHolder.txt_date.setText(getTime(allExcOrder.get(i).getStr_createdAt()));
                viewHolder.txt_coin_bal.setText(String.format("%.4f", allExcOrder.get(i).getDbl_amount()));
                viewHolder.txt_coin_bal_usd_code.setText(beforeSlash);
                viewHolder.txt_coin_bal_usd.setText(String.format("%.4f", allExcOrder.get(i).getDbl_total()));
                viewHolder.txt_coin_bal_usd_code.setText(afterSlash);

                viewHolder.lnr_close.setVisibility(View.VISIBLE);

                viewHolder.img_close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancelOrderDialog(allExcOrder.get(i), i);
                    }
                });

            }
        }

    }

    private void cancelOrderDialog(ExcOrders excOrders, int i) {
        //                Creating A Custom Dialog Using DialogPlus
        com.orhanobut.dialogplus.ViewHolder viewHolder = new com.orhanobut.dialogplus.ViewHolder(R.layout.dialog_cancel_order);
        final DialogPlus dialog = DialogPlus.newDialog(context)
                .setContentHolder(viewHolder)
                .setGravity(Gravity.BOTTOM)
                .setCancelable(true)
                .setInAnimation(R.anim.slide_in_bottom)
                .setOutAnimation(R.anim.slide_out_bottom)
                .setContentWidth(ViewGroup.LayoutParams.MATCH_PARENT)
                .setContentHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                .create();

//                Initializing Widgets
        View view = dialog.getHolderView();
        TextView txt_cancel = view.findViewById(R.id.txt_cancel);
        TextView txt_confirm = view.findViewById(R.id.txt_confirm);
/*
        TextView txt_amount = view.findViewById(R.id.txt_amount);
        TextView txt_amount_code = view.findViewById(R.id.txt_amount_code);
        TextView txt_price = view.findViewById(R.id.txt_price);
        TextView txt_price_code = view.findViewById(R.id.txt_price_code);
        LinearLayout lnr_stop_price = view.findViewById(R.id.lnr_stop_price);
        TextView txt_stop_price = view.findViewById(R.id.txt_stop_price);
        TextView txt_stop_price_code = view.findViewById(R.id.txt_stop_price_code);
        TextView txt_total = view.findViewById(R.id.txt_total);
        TextView txt_total_code = view.findViewById(R.id.txt_total_code);
        TextView txt_wallet_name = view.findViewById(R.id.txt_wallet_name);

        if (isStopLimit) {
            lnr_stop_price.setVisibility(View.VISIBLE);
            txt_stop_price.setText(String.format("%.6f", stop_price));
            txt_stop_price_code.setText(allCoinPairs.getStr_exchangeCoin());
        } else {
            lnr_stop_price.setVisibility(View.GONE);
        }

        txt_amount.setText(String.format("%.3f", amount));
        txt_amount_code.setText(allCoinPairs.getStr_pairCoin());
        txt_price.setText(String.format("%.6f", price));
        txt_price_code.setText(allCoinPairs.getStr_exchangeCoin());
        txt_total.setText(String.format("%.6f", total));
        txt_total_code.setText(allCoinPairs.getStr_exchangeCoin());
        txt_wallet_name.setText(wallet_name);
*/

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        txt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelOrder(excOrders, i);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void cancelOrder(ExcOrders excOrders, int i) {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
//            progressDialog = ProgressDialog.show(context, "", context.getResources().getString(R.string.please_wait), true);
            OrderBookControllerApi apiService = DeviantXApiClient.getClient().create(OrderBookControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.cancelOrder(CONSTANTS.DeviantMulti + token, excOrders.getStr_orderId());
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();
//                        progressDialog.dismiss();

                        if (!responsevalue.isEmpty() && responsevalue != null) {
                            JSONObject jsonObject = new JSONObject(responsevalue);
                            loginResponseMsg = jsonObject.getString("msg");
                            loginResponseStatus = jsonObject.getString("status");

                            if (loginResponseStatus.equals("true")) {
                                Intent serviceIntent = new Intent(context, ExcOrdersFetch.class);
                                context.startService(serviceIntent);
                                Intent serviceIntent1 = new Intent(getApplicationContext(), WalletDataFetch.class);
                                serviceIntent1.putExtra("walletName", "");
                                context.startService(serviceIntent1);
                                fetchAllOrders(i);
                                CommonUtilities.ShowToastMessage(context, loginResponseMsg);

                            } else {
                                CommonUtilities.ShowToastMessage(context, loginResponseMsg);
                            }

                        } else {
                            CommonUtilities.ShowToastMessage(context, loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
//                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(context, context.getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), context.getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
//                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(context, context.getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), context.getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
//                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(context, context.getResources().getString(R.string.networkerror));
                    } else {
//                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(context, context.getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), context.getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
//            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(context, context.getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), context.getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }

    }

    private void fetchAllOrders(int i) {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
//            progressDialog = ProgressDialog.show(context, "", context.getResources().getString(R.string.please_wait), true);
            OrderBookControllerApi apiService = DeviantXApiClient.getClient().create(OrderBookControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.getAll(CONSTANTS.DeviantMulti + token);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();
//                        progressDialog.dismiss();

                        if (!responsevalue.isEmpty() && responsevalue != null) {

                            try {
                                JSONObject jsonObject = new JSONObject(responsevalue);
                                loginResponseMsg = jsonObject.getString("msg");
                                loginResponseStatus = jsonObject.getString("status");

                                if (loginResponseStatus.equals("true")) {

                                    notifyItemRemoved(i);
//                                    notifyDataSetChanged();

                                } else {
                                    CommonUtilities.ShowToastMessage(context, loginResponseMsg);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        } else {
                            CommonUtilities.ShowToastMessage(context, loginResponseMsg);
//                            Toast.makeText(getApplicationContext(), responsevalue, Toast.LENGTH_LONG).show();
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
//                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(context, context.getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), context.getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
//                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(context, context.getResources().getString(R.string.Timeout));
//                        Toast.makeText(getApplicationContext(), context.getResources().getString(R.string.Timeout), Toast.LENGTH_SHORT).show();
                    } else if (t instanceof java.net.ConnectException) {
//                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(context, context.getResources().getString(R.string.networkerror));
                    } else {
//                        progressDialog.dismiss();
                        CommonUtilities.ShowToastMessage(context, context.getResources().getString(R.string.errortxt));
//                        Toast.makeText(getApplicationContext(), context.getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } catch (Exception ex) {
//            progressDialog.dismiss();
            ex.printStackTrace();
            CommonUtilities.ShowToastMessage(context, context.getResources().getString(R.string.errortxt));
//            Toast.makeText(getApplicationContext(), context.getResources().getString(R.string.errortxt), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getItemCount() {
/*        if (isOnlyOpen)
            return allExcOrder.size();
        else
            return 10;*/
        return allExcOrder.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.txt_coin_name)
        TextView txt_coin_name;
        @BindView(R.id.txt_date)
        TextView txt_date;
        @BindView(R.id.txt_coin_bal)
        TextView txt_coin_bal;
        @BindView(R.id.txt_coin_bal_code)
        TextView txt_coin_bal_code;
        @BindView(R.id.txt_coin_bal_usd)
        TextView txt_coin_bal_usd;
        @BindView(R.id.txt_coin_bal_usd_code)
        TextView txt_coin_bal_usd_code;
        @BindView(R.id.lnr_close)
        LinearLayout lnr_close;
        @BindView(R.id.img_close)
        ImageView img_close;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private String getTime(String started) {
        try {
            return CommonUtilities.convertToHumanReadable(Long.parseLong(started));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

}
