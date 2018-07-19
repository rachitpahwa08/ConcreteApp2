package com.equipshare.concreteapp.network;

/**
 * Created by Jarvis on 04-01-2018.
 */


import com.equipshare.concreteapp.model.CustomerSite;

import com.equipshare.concreteapp.model.History;
import com.equipshare.concreteapp.model.POget;
import com.equipshare.concreteapp.model.Result;
import com.equipshare.concreteapp.model.User_;


import java.util.Map;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;

import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;


public interface RetrofitInterface {

    @GET("/api/")
    Call<Result>session_manage(@Header("authorization")String authtoken);

    @FormUrlEncoded
    @POST("/api/signup")
    Call<Result>signup(@FieldMap Map<String,String> map);

    @FormUrlEncoded
    @POST("/api/login")
    Call<Result>login(@Field(value="username",encoded = true) String username,@Field(value="password",encoded = true) String password);

    @FormUrlEncoded
    @POST("/api/requestquote")
    Call<ResponseBody>quote_request(@Header("authorization") String authtoken,@FieldMap Map<String,String> map,@Field("quality") List<String> qual,@Field("quantity") List<String> quan);

    @FormUrlEncoded
    @POST("/api/cancelquote")
    Call<Result>cancel_quote(@FieldMap Map<String,String> map);

    @FormUrlEncoded
    @POST("/api/createpo")
    Call<ResponseBody>create_po(@Header("authorization")String authtoken,@FieldMap Map<String,String> map,@Field("quality") List<String> qual,@Field("quantity") List<String> quan,@Field("price") List<String> price);

    @FormUrlEncoded
    @POST("/api/deletepo")
    Call<ResponseBody>delete_po(@Header("authorization")String authtoken,@FieldMap Map<String,String> map);

    @FormUrlEncoded
    @POST("/api/addorder")
    Call<ResponseBody>submit_order(@Header("authorization") String authtoken,@FieldMap Map<String,String> map,@Field("quality") List<String> qual,@Field("quantity") List<String> quan);

    @FormUrlEncoded
    @POST("/api/cancelorder")
    Call<ResponseBody>cancel_order(@FieldMap Map<String,String> map);

    @FormUrlEncoded
    @POST("/api/addissue")
    Call<ResponseBody>add_issue(@Header("authorization")String authtoken,@FieldMap Map<String,String> map);

    @FormUrlEncoded
    @POST("/api/addsite")
    Call<Result>add_site(@Header("authorization")String authtoken,@FieldMap Map<String,String> map);

    @FormUrlEncoded
    @POST("/api/deletesite")
    Call<Result>delete_site(@Header("authorization") String authtoken,@FieldMap Map<String,String> map);

    @FormUrlEncoded
    @POST("/api/profile")
    Call<ResponseBody>edit_profile(@Header("authorization") String authtoken,@FieldMap Map<String,String> map);

    @GET("/api/profile")
   Call<User_>show_profile(@Header("authorization")String authtoken);

    @GET("/api/history")
    Call<History>history(@Header("authorization")String authtoken);


    @GET("/api/pos")
    Call<POget>getpo(@Header("authorization")String authtoken);

    @FormUrlEncoded
    @POST("/api/forgot")
    Call<ResponseBody>forgot_pass(@FieldMap Map<String,String> map);

    @FormUrlEncoded
    @POST("/api/getsuppliername")
    Call<Result>supp_name(@Field("supplierId") String suppid);

    @FormUrlEncoded
    @POST("/api/doesexist")
    Call<Result>check_usename(@Field("email") String email);

    @GET("/api/getcities")
    Call<Result>get_cityname();

    @FormUrlEncoded
    @POST("/api/changepass")
    Call<Result>change_pass(@Header("authorization") String authtoken,@FieldMap Map<String,String> map);
}
