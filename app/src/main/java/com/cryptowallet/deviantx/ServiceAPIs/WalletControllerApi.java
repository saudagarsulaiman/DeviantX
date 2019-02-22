package com.cryptowallet.deviantx.ServiceAPIs;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface WalletControllerApi {

    @GET("/api/v2/wallet/get_all_transactions/{wallet_name}/{coin_code}")
    Call<ResponseBody> getAllHistory(@Header("Authorization") String tokenDX, @Path("wallet_name") String walletNameX, @Path("coin_code") String coin_codeX);

    @GET("/api/v2/wallet/get_all_wallet")
    Call<ResponseBody> getAllWallet(@Header("Authorization") String tokenDX);

    @GET("/api/v2/wallet/get_all_wallet_balances")
    Call<ResponseBody> getAllWalletBalance(@Header("Authorization") String tokenDX);

    @GET("/api/v2/wallet/get_all_wallet_with_coin")
    Call<ResponseBody> getAllWalletsDetails(@Header("Authorization") String tokenDX);

    @GET("/api/v2/wallet/new_wallet/{name}/{defaultWallet}")
    Call<ResponseBody> getAddNewWallet(@Header("Authorization") String tokenDX, @Path("name") String walletNameX, @Path("defaultWallet") boolean defaultWallet);

    @GET("/api/v2/wallet/update_wallet/{name}/{new_name}/{defaultWallet}")
    Call<ResponseBody> updateWallet(@Header("Authorization") String tokenDX, @Path("name") String walletNameX, @Path("new_name") String new_Name, @Path("defaultWallet") boolean defaultWallet);

}
