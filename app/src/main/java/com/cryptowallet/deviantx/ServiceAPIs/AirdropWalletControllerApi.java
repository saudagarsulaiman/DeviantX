package com.cryptowallet.deviantx.ServiceAPIs;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface AirdropWalletControllerApi {

    @GET("/api/airdrop_wallet/get_airdrop_Wallet")
    Call<ResponseBody> getAirdropWallet(@Header("Authorization") String tokenDX);


    @Headers("Content-Type: application/json")
    @POST("/api/airdrop_wallet/transfer_to_address")
    Call<ResponseBody> transferToAddress(@Body String body,@Header("Authorization") String AuthorizationDX);

    @Headers("Content-Type: application/json")
    @POST("/api/airdrop_wallet/transfer_to_wallet")
    Call<ResponseBody> transferToWallet(@Body String body,@Header("Authorization") String AuthorizationDX);

}