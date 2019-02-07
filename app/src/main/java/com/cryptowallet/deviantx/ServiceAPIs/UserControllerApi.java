package com.cryptowallet.deviantx.ServiceAPIs;

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

    @GET("/api/account/disable_2fa/{password}/{totp}")
    Call<ResponseBody> disable2FA(@Path("password") String passwordX, @Path("totp") String totpX, @Header("Authorization") String AuthorizationDX);

    @GET("/api/account/enable_2fa/{password}/{totp}")
    Call<ResponseBody> enable2FA(@Path("password") String passwordX, @Path("totp") String totpX, @Header("Authorization") String AuthorizationDX);

    @GET("/api/account/generate_2fa/{password}/{secret}/{totp}")
    Call<ResponseBody> generate2FA(@Path("password") String passwordX, @Path("secret") String secretX, @Path("totp") String totpX, @Header("Authorization") String AuthorizationDX);

    @GET("/api/account/get_seed")
    Call<ResponseBody> getSeed();

    @GET("/api/account/get_two_factor")
    Call<ResponseBody> get2FACode(@Header("Authorization") String AuthorizationDX);

//    @GET("/api/account/get_two_factor_status")
//    Call<ResponseBody> get2FAStatus(@Header("Authorization") String AuthorizationDX);

    @Headers("Content-Type: application/json")
    @POST("/api/account/reset_password_link/validate/{token}")
    Call<ResponseBody> recoverEmailPassword(@Body String body, @Path("token") String token);

    @GET("/api/account/reset_password_link/{email}/")
    Call<ResponseBody> resetEmail(@Path("email") String email);

    @Headers("Content-Type: application/json")
    @POST("/api/account/restore_seed")
    Call<ResponseBody> restoreSeed(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("/api/account/update_password")
    Call<ResponseBody> updatePassword(@Body String body, @Header("Authorization") String AuthorizationDX);

    @GET("/api/account/verify/email/{token}")
    Call<ResponseBody> verifyToken(@Path("token") String token);

    @GET("/api/account/verify_2fa/{totp}")
    Call<ResponseBody> verify2FA(@Path("totp") String totpX, @Header("Authorization") String AuthorizationDX);

}
