package com.aequalis.deviantx.ServiceAPIs;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface USDValues {

    @GET("fsym={from_coin}&tsyms={to_coin}")
    Call<ResponseBody> getUsdConversion(@Path("from_coin") String from_coin, @Path("to_coin") String to_coin);

}
