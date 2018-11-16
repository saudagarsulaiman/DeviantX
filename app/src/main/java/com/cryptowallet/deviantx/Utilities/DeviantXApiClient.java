package com.cryptowallet.deviantx.Utilities;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/*
 * Created by Sulaiman on 28/3/2018.
 */

public class DeviantXApiClient {
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(CommonUtilities.URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit;
    }

    public static Retrofit getClientMarketCap() {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.coinmarketcap.com/v2/ticker/1")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
                return retrofit;
    }

    public static Retrofit getClientValues() {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://min-api.cryptocompare.com/data/")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        return retrofit;
    }
}
