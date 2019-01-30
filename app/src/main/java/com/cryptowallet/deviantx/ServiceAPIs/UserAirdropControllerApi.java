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

public interface UserAirdropControllerApi {

    @Headers("Content-Type: application/json")
    @POST("/api/user_airdrop/claim_airdrop_amount")
    Call<ResponseBody> postClaimADAmount(@Body String body);

    @Headers("Content-Type: application/json")
    @POST("/api/user_airdrop/create_new_airdrop")
    Call<ResponseBody> createNewAD(@Body String body, @Path("token") String token);

    @GET("/api/user_airdrop/get_airdrop_history")
    Call<ResponseBody> getADHistory(@Header("Authorization") String AuthorizationDX);

    @GET("/api/user_airdrop/get_claim_airdrop")
    Call<ResponseBody> getClaimADAmount(@Header("Authorization") String AuthorizationDX);

    @GET("/api/user_airdrop/get_user_airdrop")
    Call<ResponseBody> getUserAD(@Header("Authorization") String AuthorizationDX);

}
