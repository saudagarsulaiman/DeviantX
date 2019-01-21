package com.cryptowallet.deviantx.ServiceAPIs;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface NewsPanelControllerApi {

    //    @Headers("Content-Type: application/json")
    @GET("/api/news_panel/get_all_news_panels")
    Call<ResponseBody> getNewsPanel(@Header("Authorization") String tokenDX);

}
