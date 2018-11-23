package com.cryptowallet.deviantx.Utilities;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/*
 * Created by Sulaiman on 28/3/2018.
 */

public class DeviantXApiClient {
    private static Retrofit retrofit = null;

    static final OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(60, TimeUnit.SECONDS)
            .connectTimeout(60, TimeUnit.SECONDS)
            .build();

    public static Retrofit getClient() {
        retrofit = new Retrofit.Builder()
                .baseUrl(CommonUtilities.URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit;
    }

    public static Retrofit getClientMarketCap() {
            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.coinmarketcap.com/v2/ticker/1")
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build();
                return retrofit;
    }

    public static Retrofit getCoinValues() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://min-api.cryptocompare.com/data/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit;
    }

    public static Retrofit getCoinGraph() {
        retrofit = new Retrofit.Builder()
//                .baseUrl("https://www.binance.com/info-api/v1/public/data/")
                .baseUrl("https://min-api.cryptocompare.com/data/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        return retrofit;
    }
}
