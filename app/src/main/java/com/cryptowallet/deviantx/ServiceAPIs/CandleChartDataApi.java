package com.cryptowallet.deviantx.ServiceAPIs;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CandleChartDataApi {

    @GET("api/exchange_rates/json/marks?")/*symbol=BTC-USD&resolution=D&from=1514748600000&to=1514748600000*/
    Call<ResponseBody> getCandleChart(@Query("symbol") String symbol, @Query("resolution") String resolution, @Query("from") long from, @Query("to") long to);


}
