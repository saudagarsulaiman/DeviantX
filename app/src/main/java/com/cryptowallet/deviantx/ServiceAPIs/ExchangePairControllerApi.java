package com.cryptowallet.deviantx.ServiceAPIs;

/*
 * Created by Sulaiman on 26/10/2018.
 */

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface ExchangePairControllerApi {

    @GET("/api_v2/exchange_pair/exchange_pair_coins_details")
    Call<ResponseBody> getPairsList(/*@Header("Authorization") String AuthorizationDX*/);

    @GET("/api_v2/exchange_pair/get_all_pairs")
    Call<ResponseBody> getAllPairs(/*@Header("Authorization") String AuthorizationDX*/);

    @GET("/api_v2/exchange_pair/get_depth_chart/{coin_pair}/{coin_ex}")
    Call<ResponseBody> getDepthChart(@Header("Authorization") String AuthorizationDX, @Path("coin_pair") String coin_pair, @Path("coin_ex") String coin_ex);

    @GET("/api_v2/exchange_pair/get_pairs/{coin_code}")
    Call<ResponseBody> getPairs(@Header("Authorization") String AuthorizationDX, @Path("coin_code") String coinCodeX);

    @GET("/api_v2/exchange_pair/get_pairs_status/{coin_code}/{status}")
    Call<ResponseBody> getPairsStatus(@Header("Authorization") String AuthorizationDX, @Path("coin_code") String coinCodeX, @Path("status") String statusX);

}
