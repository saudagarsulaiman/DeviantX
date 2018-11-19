package com.cryptowallet.deviantx.ServiceAPIs;

import com.cryptowallet.deviantx.UI.Models.USDValue;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CoinGraphApi {

    @GET("k-lines?")/*symbol=DOGE&interval=1m&limit=800&startTime=1542279420000&endTime=1542282961000*/
    Call<ResponseBody> getCoinGraph(@Query("symbol") String symbol_coinCodeX, @Query("interval") String intervalX, @Query("limit") int limitX, @Query("startTime") long startTimeX, @Query("endTime") long endTimeX);

}
