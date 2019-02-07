package com.cryptowallet.deviantx.ServiceAPIs;

/*
 * Created by Sulaiman on 26/10/2018.
 */

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface OrderBookControllerApi {

    @GET("/order_book/get/all")
    Call<ResponseBody> getAll(@Header("Authorization") String AuthorizationDX);

    @GET("/order_book/get/buy")
    Call<ResponseBody> getBuy(@Header("Authorization") String AuthorizationDX);

    @GET("/order_book/get/sell")
    Call<ResponseBody> getSell(@Header("Authorization") String AuthorizationDX);

    @FormUrlEncoded
    @POST("/order_book/init/order")
    Call<ResponseBody> getOrder(@Header("Authorization") String AuthorizationDX, @Body String body);

}
