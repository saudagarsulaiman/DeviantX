package com.cryptowallet.deviantx.ServiceAPIs;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ChartControllerApi {

    @Headers("Content-Type: application/json")
    @POST("/api_v2/chart/get/candle")
    Call<ResponseBody> getCandleChartData(@Body String body, @Header("Authorization") String AuthorizationDX);

}
