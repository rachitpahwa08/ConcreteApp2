package com.equipshare.concreteapp.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.equipshare.concreteapp.model.User_;
import com.equipshare.concreteapp.network.RetrofitInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Jarvis on 22-02-2018.
 */

public class UserInfo {

    String token;
    public UserInfo(String token) {

        this.token=token;
    }

    public User_ userinfo()
    {
        Gson gson = new GsonBuilder().setLenient().create();
        OkHttpClient client = new OkHttpClient();
        Retrofit.Builder builder=new Retrofit.Builder().baseUrl(Constants.BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create(gson));
        Retrofit retrofit=builder.build();
        RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);
        final User_[] u = new User_[1];
        Call<User_> call=retrofitInterface.show_profile(token);
        call.enqueue(new Callback<User_>() {
            @Override
            public void onResponse(Call<User_> call, Response<User_> response) {
                u[0] =response.body();
                Log.e("Userinfo Response ", "response 33: " + new Gson().toJson(response.body()));

            }

            @Override
            public void onFailure(Call<User_> call, Throwable t) {
                Log.e("TAG", "response 33: " + t.getMessage());
            }
        });
    return u[0];
    }
}
