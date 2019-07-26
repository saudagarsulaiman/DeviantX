package com.cryptowallet.deviantx.UI.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cryptowallet.deviantx.R;
import com.cryptowallet.deviantx.ServiceAPIs.UserAirdropControllerApi;
import com.cryptowallet.deviantx.UI.Activities.DashBoardActivity;
import com.cryptowallet.deviantx.UI.Models.CreatorAirdrop;
import com.cryptowallet.deviantx.Utilities.CONSTANTS;
import com.cryptowallet.deviantx.Utilities.CommonUtilities;
import com.cryptowallet.deviantx.Utilities.DeviantXApiClient;
import com.orhanobut.dialogplus.DialogPlus;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatorADHorizontalRAdapter extends RecyclerView.Adapter<CreatorADHorizontalRAdapter.ViewHolder> {
    Context context;
    ArrayList<CreatorAirdrop> creatorAirdrops;
    boolean isFull = false;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String loginResponseMsg, loginResponseStatus;

    public CreatorADHorizontalRAdapter(Context context, ArrayList<CreatorAirdrop> creatorAirdrops, boolean isFull) {
        this.context = context;
        this.creatorAirdrops = creatorAirdrops;
        this.isFull = isFull;
        sharedPreferences = context.getSharedPreferences("CommonPrefs", Activity.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.coins_ad_lyt, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        Picasso.with(context).load(creatorAirdrops.get(i).getStr_coinlogo()).into(viewHolder.img_coin);
        viewHolder.txt_coin_name_code.setText(creatorAirdrops.get(i).getStr_coinName() + " (" + creatorAirdrops.get(i).getStr_coinCode() + ")");
        viewHolder.txt_coin_value.setText(String.format("%.6f", creatorAirdrops.get(i).getDbl_airdropAmount()) + " (" + creatorAirdrops.get(i).getStr_coinCode() + ")");
        viewHolder.img_cancel.setVisibility(View.VISIBLE);
        viewHolder.img_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelOrderDialog(creatorAirdrops.get(i).getInt_id());
            }
        });
    }

    @Override
    public int getItemCount() {
 /*       if (isFull){
            if (creatorAirdrops.size()>)
        }else {

        }
 */
        return creatorAirdrops.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.img_coin)
        ImageView img_coin;
        @BindView(R.id.img_cancel)
        ImageView img_cancel;
        @BindView(R.id.txt_coin_name_code)
        TextView txt_coin_name_code;
        @BindView(R.id.txt_coin_value)
        TextView txt_coin_value;
        @BindView(R.id.lnr_item)
        LinearLayout lnr_item;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    private void cancelOrderDialog(int id) {
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

        txt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        txt_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelOrder(id);
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void cancelOrder(int id) {
        try {
            String token = sharedPreferences.getString(CONSTANTS.token, null);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id", id);
            } catch (Exception e) {
                e.printStackTrace();
            }
            UserAirdropControllerApi apiService = DeviantXApiClient.getClient().create(UserAirdropControllerApi.class);
            Call<ResponseBody> apiResponse = apiService.cancelCreatorAirdrop(jsonObject.toString(), CONSTANTS.DeviantMulti + token);
            apiResponse.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String responsevalue = response.body().string();
                        if (!responsevalue.isEmpty() && responsevalue != null) {
                            JSONObject jsonObject = new JSONObject(responsevalue);
                            loginResponseMsg = jsonObject.getString("msg");
                            loginResponseStatus = jsonObject.getString("status");

                            if (loginResponseStatus.equals("true")) {
                                Intent intent = new Intent(context, DashBoardActivity.class);
                                intent.putExtra(CONSTANTS.seletedTab, 2);
                                context.startActivity(intent);
                                CommonUtilities.ShowToastMessage(context, loginResponseMsg);

                            } else {
                                CommonUtilities.ShowToastMessage(context, loginResponseMsg);
                            }

                        } else {
                            loginResponseMsg = jsonObject.getString("msg");
                            CommonUtilities.ShowToastMessage(context, loginResponseMsg);
                            Log.i(CONSTANTS.TAG, "onResponse:\n" + responsevalue);
                        }

                    } catch (Exception e) {
                        CommonUtilities.ShowToastMessage(context, context.getResources().getString(R.string.errortxt));
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    if (t instanceof SocketTimeoutException) {
                        CommonUtilities.ShowToastMessage(context, context.getResources().getString(R.string.Timeout));
                    } else if (t instanceof java.net.ConnectException) {
                        CommonUtilities.ShowToastMessage(context, context.getResources().getString(R.string.networkerror));
                    } else {
                        CommonUtilities.ShowToastMessage(context, context.getResources().getString(R.string.errortxt));
                    }
                }
            });
        } catch (Exception ex) {
            CommonUtilities.ShowToastMessage(context, context.getResources().getString(R.string.errortxt));
        }

    }

}
