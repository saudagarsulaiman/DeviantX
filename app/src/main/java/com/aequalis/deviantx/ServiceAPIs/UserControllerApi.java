package com.aequalis.deviantx.ServiceAPIs;

/*
 * Created by Sulaiman on 26/10/2018.
 */


import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface UserControllerApi {

    @Headers("Content-Type: application/json")
    @POST("/api/account/create")
    Call<ResponseBody> createAccount(@Body String body);

    @GET("/api/account/verify/email/{token}")
    Call<ResponseBody> verifyToken(@Path("token") String token);

    @GET("/api/account/reset_password_link/{email}/")
    Call<ResponseBody> resetEmail(@Path("email") String email);

    @Headers("Content-Type: application/json")
    @POST("/api/account/reset_password_link/validate/{token}")
    Call<ResponseBody> recoverEmailPassword(@Body String body,@Path("token") String token);

    @Headers("Content-Type: application/json")
    @POST("/api/account/update_password")
    Call<ResponseBody> updatePassword(@Body String body,@Header("Authorization") String AuthorizationDX);



//    @FormUrlEncoded
//    @POST("/api/account/create")
//    Call<ResponseBody> createAccount(@Field("email") String emailDX, @Field("password") String passwordDX, @Field("userName") String userNameDX);

//    @GET("/api/account/get_seed")
//    Call<ResponseBody> getSeeds();

}
