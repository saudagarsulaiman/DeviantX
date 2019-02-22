package com.cryptowallet.deviantx.ServiceAPIs;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CoinsControllerApi {

    @GET("/api/v2/coins/getall")
    Call<ResponseBody> getAllCoins(@Header("Authorization") String tokenDX);

    @GET("/api/v2/coins/get_chart_data_by_coin_code")
    Call<ResponseBody> getCoinChartData(@Header("Authorization") String tokenDX,@Query("coinCode") String xCoinCode);
}
