package com.aequalis.deviantx.ServiceAPIs;

/*
 * Created by Sulaiman on 26/10/2018.
 */

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthenticationApi {

//    @FormUrlEncoded
//    @POST("/api/authenticate/add_seed")
//    Call<ResponseBody> getBookDetailsResponse(@Field("maximumRows") int xMaxRows, @Field("startRowIndex") int xPageNum);

//    @FormUrlEncoded
//    @POST("/api/authenticate/admin/login")
//    Call<ResponseBody> getBookDetailsResponse(@Field("maximumRows") int xMaxRows, @Field("startRowIndex") int xPageNum);

//    @FormUrlEncoded  USING
//    @POST("/api/authenticate/login")
//    Call<ResponseBody> getBookDetailsResponse(@Field("maximumRows") int xMaxRows, @Field("startRowIndex") int xPageNum);

    @Headers("Content-Type: application/json")
    @POST("/api/authenticate/login")
    Call<ResponseBody> loginAccount(@Body String body);

}
