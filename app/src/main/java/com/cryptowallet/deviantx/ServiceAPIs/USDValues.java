package com.cryptowallet.deviantx.ServiceAPIs;

import com.cryptowallet.deviantx.UI.Models.CoinValue;
import com.cryptowallet.deviantx.UI.Models.USDValue;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface USDValues {

    @GET("price?tsyms=USD")
    Call<USDValue> getUsdConversion(@Query("fsym") String from_coin/*, @Path("to_coin") String to_coin*/);

/*
    @GET("price?")*/
/*tsyms=USD*//*

    Call<CoinValue> getCoinConversion(@Query("fsym") String beforeSlash, @Query("tsyms") String afterSlash);
*/

    @GET("price?")/*tsyms=USD*/
    Call<ResponseBody> getCoinConversion(@Query("fsym") String beforeSlash, @Query("tsyms") String afterSlash);

}
