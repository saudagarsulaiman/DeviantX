package com.cryptowallet.deviantx.ServiceAPIs;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface WalletControllerApi {

    //    @Headers("Content-Type: application/json")
    @GET("/api/wallet/get_all_wallet")
    Call<ResponseBody> getAllWallet(@Header("Authorization") String tokenDX);

    //    @Headers("Content-Type: application/json")
    @GET("/api/wallet/get_all_wallet_with_coin")
    Call<ResponseBody> getAllWalletsDetails(@Header("Authorization") String tokenDX);

    //    @Headers("Content-Type: application/json")
    @GET("/api/wallet/new_wallet/{name}/{defaultWallet}")
    Call<ResponseBody> getAddNewWallet(@Header("Authorization") String tokenDX, @Path("name") String walletNameX, @Path("defaultWallet") boolean defaultWallet);


    //    @Headers("Content-Type: application/json")
    @GET("/api/wallet/update_wallet/{name}/{new_name}/{defaultWallet}")
    Call<ResponseBody> updateWallet(@Header("Authorization") String tokenDX, @Path("name") String walletNameX, @Path("new_name") String new_Name, @Path("defaultWallet") boolean defaultWallet);

}
