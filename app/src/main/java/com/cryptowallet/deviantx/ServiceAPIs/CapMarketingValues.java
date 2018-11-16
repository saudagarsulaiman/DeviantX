package com.cryptowallet.deviantx.ServiceAPIs;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CapMarketingValues {
    @POST("convert={coin_name}")
    Call<ResponseBody> getCoinCapMarketingValues(@Path("coin_name") String coin_Name);

}
