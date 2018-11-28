package com.cryptowallet.deviantx.ServiceAPIs;

/*
 * Created by Sulaiman on 26/10/2018.
 */

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

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

    @Headers("Content-Type: application/json")
    @POST("/api/authenticate/add_seed")
    Call<ResponseBody> addSeed(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("/api/authenticate/login_2fa")
    Call<ResponseBody> Login2FA(@Body String body);


}
