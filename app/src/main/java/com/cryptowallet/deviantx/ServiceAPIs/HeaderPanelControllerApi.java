package com.cryptowallet.deviantx.ServiceAPIs;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface HeaderPanelControllerApi {

    //    @Headers("Content-Type: application/json")
    @GET("/api/header_panel/get_all_panels")
    Call<ResponseBody> getHeaderPanel(@Header("Authorization") String tokenDX);

}
