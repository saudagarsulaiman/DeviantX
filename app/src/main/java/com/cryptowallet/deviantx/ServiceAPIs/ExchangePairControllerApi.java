package com.cryptowallet.deviantx.ServiceAPIs;

/*
 * Created by Sulaiman on 26/10/2018.
 */

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ExchangePairControllerApi {

    @GET("/api/exchange_pair/get_pairs/{coin_code}")
    Call<ResponseBody> getPairs(@Header("Authorization") String AuthorizationDX, @Path("coin_code") String coinCodeX);

    @GET("/api/exchange_pair/get_pairs_status/{coin_code}/{status}")
    Call<ResponseBody> getPairsStatus(@Header("Authorization") String AuthorizationDX, @Path("coin_code") String coinCodeX, @Path("status") String statusX);

}
