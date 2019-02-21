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

public interface OrderBookControllerApi {

    @GET("/api_v2/order_book/cancel/{order_id}")
    Call<ResponseBody> cancelOrder(@Header("Authorization") String AuthorizationDX, @Path("order_id") String order_idX);

    @GET("/api_v2/order_book/get/all")
    Call<ResponseBody> getAll(@Header("Authorization") String AuthorizationDX);

    @GET("/api_v2/order_book/get/all/completed")
    Call<ResponseBody> getAllCompleted(@Header("Authorization") String AuthorizationDX);

    @GET("/api_v2/order_book/get/all/open_pending")
    Call<ResponseBody> getAllOpen(@Header("Authorization") String AuthorizationDX);

    @GET("/api_v2/order_book/get/buy")
    Call<ResponseBody> getBuy(@Header("Authorization") String AuthorizationDX);

    @GET("/api_v2/order_book/get/sell")
    Call<ResponseBody> getSell(@Header("Authorization") String AuthorizationDX);

    @Headers("Content-Type: application/json")
    @POST("/api_v2/order_book/init/order")
    Call<ResponseBody> getOrder(@Header("Authorization") String AuthorizationDX, @Body String body);

}
