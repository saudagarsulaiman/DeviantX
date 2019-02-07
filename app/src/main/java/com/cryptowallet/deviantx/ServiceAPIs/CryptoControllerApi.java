package com.cryptowallet.deviantx.ServiceAPIs;

/*
 * Created by Sulaiman on 26/10/2018.
 */

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CryptoControllerApi {

/*
    @GET("/api/crypto/get_all_transactions")
    Call<ResponseBody> getAllTransactions(@Header("Authorization") String AuthorizationDX);

    @GET("/api/crypto/get_transactions_by_address/{address}")
    Call<ResponseBody> getTransactions(@Header("Authorization") String AuthorizationDX, @Path("address") String addressX);
*/

    @Headers("Content-Type: application/json")
    @POST("/api/crypto/export_privatekey")
    Call<ResponseBody> getPrivateKey(@Body String body, @Header("Authorization") String AuthorizationDX);

    /*
        @Headers("Content-Type: application/json")
        @POST("/api/crypto/transfer")
        Call<ResponseBody> transferCoins(@Body String body, @Header("Authorization") String AuthorizationDX);
    */

    @GET("/api/crypto/get_account_wallet/{wallet_name}")
    Call<ResponseBody> getAccountWallet(@Header("Authorization") String AuthorizationDX, @Path("wallet_name") String walletNameX);

    @GET("/api/crypto/new_wallet/{coin_code}/{wallet_name}")
    Call<ResponseBody> createWalletCoin(@Header("Authorization") String AuthorizationDX, @Path("coin_code") String coinCodeX, @Path("wallet_name") String walletNameX);

    @GET("/api/crypto/update_fav/{id}/{state}")
    Call<ResponseBody> favAddRemove(@Header("Authorization") String AuthorizationDX, @Path("id") int id, @Path("state") boolean state);

    @GET("/api/crypto/deposit_wallet/{coin_code}/{wallet_name}")
    Call<ResponseBody> receiveCoins(@Header("Authorization") String AuthorizationDX, @Path("coin_code") String coin_codeX, @Path("wallet_name") String walletNameX);

}
