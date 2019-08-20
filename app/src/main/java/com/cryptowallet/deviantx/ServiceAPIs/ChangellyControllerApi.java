package com.cryptowallet.deviantx.ServiceAPIs;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ChangellyControllerApi {

    @Headers("Content-Type: application/json")
    @POST("/api_v2/changelly_exchange/createTransaction")
    Call<ResponseBody> createTransaction(@Header("Authorization") String tokenDX, @Query("from") String from, @Query("to") String to, @Query("address") String address, @Query("amount") String amount);

    @GET("/api_v2/changelly_exchange/get_account_transaction")
    Call<ResponseBody> getAllTransactions(@Header("Authorization") String tokenDX);

    @GET("/api_v2/changelly_exchange/get_currencies")
    Call<ResponseBody> getCurrencies(@Header("Authorization") String tokenDX);

    @Headers("Content-Type: application/json")
    @POST("/api_v2/changelly_exchange/get_expected")
    Call<ResponseBody> getExpectedAmount(@Header("Authorization") String tokenDX, @Query("from") String from, @Query("to") String to, @Query("amount") String amount);

    @Headers("Content-Type: application/json")
    @POST("/api_v2/changelly_exchange/get_minimum_amount")
    Call<ResponseBody> getMinimumAmount(@Header("Authorization") String tokenDX, @Query("from") String from, @Query("to") String to);

    @Headers("Content-Type: application/json")
    @POST("/api_v2/changelly_exchange/get_transaction_by_id")
    Call<ResponseBody> getTransactionById(@Header("Authorization") String tokenDX, @Query("id") String id);

}
