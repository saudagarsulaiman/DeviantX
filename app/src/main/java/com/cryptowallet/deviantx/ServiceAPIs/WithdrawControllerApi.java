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
    @POST("/api/crypto/transfer")
    Call<ResponseBody> transferCoins(@Body String body, @Header("Authorization") String AuthorizationDX);

    @GET("/api/withdraw/withdraw_details/{coin_code}")
    Call<ResponseBody> detail(@Header("Authorization") String AuthorizationDX, @Path("coin_code") String coin_codeX);

    @GET("/api/withdraw/withdraw_details/{wallet_name}/{coin_code}")
    Call<ResponseBody> detail(@Header("Authorization") String AuthorizationDX, @Path("coin_code") String coin_codeX, @Path("wallet_name") String walletNameX);


}
