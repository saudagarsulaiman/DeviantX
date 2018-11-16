package com.cryptowallet.deviantx.ServiceAPIs;

import com.cryptowallet.deviantx.UI.Models.USDValue;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface USDValues {

    @GET("price?tsyms=USD")
    Call<USDValue> getUsdConversion(@Query("fsym") String from_coin/*, @Path("to_coin") String to_coin*/);

}
