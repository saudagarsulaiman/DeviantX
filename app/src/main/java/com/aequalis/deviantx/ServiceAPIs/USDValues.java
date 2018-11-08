package com.aequalis.deviantx.ServiceAPIs;

import com.aequalis.deviantx.UI.Models.USDValue;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface USDValues {

    @GET("price?tsyms=USD")
    Call<USDValue> getUsdConversion(@Query("fsym") String from_coin/*, @Path("to_coin") String to_coin*/);

}
