package com.aequalis.deviantx.ServiceAPIs;

/*
 * Created by Sulaiman on 26/10/2018.
 */

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CryptoControllerApi {

//    @GET("/api/crypto/balance_of_account")
//    Call<ResponseBody> getPagedItemDataResponse(@Query("vendorid") int xvendorid, @Query("CategoryID") int xCategoryID, @Query("itemSearchText") String xitemSearchText, @Query("pageno") int xpageno);

//    @FormUrlEncoded
//    @POST("/api/crypto/balance_of_address")  using for particular wallet coin
//    Call<ResponseBody> getBookDetailsResponse(@Field("maximumRows") int xMaxRows, @Field("startRowIndex") int xPageNum);

//    @FormUrlEncoded
//    @POST("/api/crypto/export_privatekey")
//    Call<ResponseBody> getBookDetailsResponse(@Field("maximumRows") int xMaxRows, @Field("startRowIndex") int xPageNum);

//    @GET("/api/crypto/get_account_wallet")   using getting wallets details
//    Call<ResponseBody> getPagedItemDataResponse(@Query("vendorid") int xvendorid, @Query("CategoryID") int xCategoryID, @Query("itemSearchText") String xitemSearchText, @Query("pageno") int xpageno);

//    @GET("/api/crypto/get_all_transactions")
//    Call<ResponseBody> getPagedItemDataResponse(@Query("vendorid") int xvendorid, @Query("CategoryID") int xCategoryID, @Query("itemSearchText") String xitemSearchText, @Query("pageno") int xpageno);

//    @GET("/api/crypto/new_wallet/{coin_code}/{name}")
//    Call<ResponseBody> getPagedItemDataResponse(@Query("vendorid") int xvendorid, @Query("CategoryID") int xCategoryID, @Query("itemSearchText") String xitemSearchText, @Query("pageno") int xpageno);

    @GET("/api/crypto/get_all_transactions")
    Call<ResponseBody> getTransactions( @Header("Authorization") String AuthorizationDX);

    @GET("/api/crypto/get_account_wallet")
    Call<ResponseBody> getAccountWallet(@Header("Authorization") String AuthorizationDX);

    @GET("/api/crypto/new_wallet/{coin_code}/{name}")
    Call<ResponseBody> createNewWallet(@Path("coin_code") String coin_codeDX, @Path("name") String nameDX, @Header("Authorization") String AuthorizationDX);


    @Headers("Content-Type: application/json")
    @POST("/api/crypto/export_privatekey")
    Call<ResponseBody> getPrivateKey(@Body String body, @Header("Authorization") String AuthorizationDX);

    @Headers("Content-Type: application/json")
    @POST("/api/crypto/transfer")
    Call<ResponseBody> transferCoins(@Body String body, @Header("Authorization") String AuthorizationDX);

}
