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
    @POST("/api_v2/user_airdrop/claim_airdrop_amount")
    Call<ResponseBody> postClaimADAmount(@Body String body, @Header("Authorization") String AuthorizationDX);

    @Headers("Content-Type: application/json")
    @POST("/api_v2/user_airdrop/cancel_airdrop")
    Call<ResponseBody> cancelCreatorAirdrop(@Body String body, @Header("Authorization") String AuthorizationDX);

    @Headers("Content-Type: application/json")
    @POST("/api_v2/user_airdrop/create_new_airdrop")
    Call<ResponseBody> createNewAD(@Body String body,@Header("Authorization") String AuthorizationDX);

    @GET("/api_v2/user_airdrop/get_airdrop_history")
    Call<ResponseBody> getADHistory(@Header("Authorization") String AuthorizationDX);

    @GET("/api_v2/user_airdrop/get_claim_airdrop")
    Call<ResponseBody> getClaimADAmount(@Header("Authorization") String AuthorizationDX);

    @GET("/api_v2/user_airdrop/get_user_airdrop")
    Call<ResponseBody> getUserAD(@Header("Authorization") String AuthorizationDX);

    @GET("/api_v2/user_airdrop/get_creator_airdrop")
    Call<ResponseBody> getCreatorADHistory(@Header("Authorization") String AuthorizationDX);

}
