package com.cryptowallet.deviantx.ServiceAPIs;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface WithdrawControllerApi {

    @Headers("Content-Type: application/json")
    @POST("/api_v2/withdraw/balance_withdraw")
    Call<ResponseBody> transferCoins(@Body String body, @Header("Authorization") String AuthorizationDX);

    @GET("/api_v2/withdraw/withdraw_details/{coin_code}")
    Call<ResponseBody> detail(@Header("Authorization") String AuthorizationDX, @Path("coin_code") String coin_codeX);

    @GET("/api_v2/withdraw/withdraw_details/{wallet_name}/{coin_code}")
    Call<ResponseBody> getSentTransactions(@Header("Authorization") String AuthorizationDX, @Path("wallet_name") String walletNameX, @Path("coin_code") String coin_codeX);

    @Headers("Content-Type: application/json")
    @POST("/api_v2/withdraw/get_transaction_fee")
    Call<ResponseBody> getTransactionFee(@Body String body, @Header("Authorization") String AuthorizationDX);

}
