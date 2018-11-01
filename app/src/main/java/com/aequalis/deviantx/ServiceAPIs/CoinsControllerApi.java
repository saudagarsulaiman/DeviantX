package com.aequalis.deviantx.ServiceAPIs;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;

public interface CoinsControllerApi {


//    @FormUrlEncoded
//    @POST("/api/coins/addCoin")
//    Call<ResponseBody> getBookDetailsResponse(@Field("maximumRows") int xMaxRows, @Field("startRowIndex") int xPageNum);


//    @GET("/api/coins/getall") USING for all coins
//    Call<ResponseBody> getPagedItemDataResponse(@Query("vendorid") int xvendorid, @Query("CategoryID") int xCategoryID, @Query("itemSearchText") String xitemSearchText, @Query("pageno") int xpageno);

//    @Headers("Content-Type: application/json")
    @GET("/api/coins/getall")
    Call<ResponseBody> getAllCoins(@Header("Authorization") String tokenDX);

}
