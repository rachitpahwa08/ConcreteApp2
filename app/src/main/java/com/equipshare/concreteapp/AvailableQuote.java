package com.equipshare.concreteapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.equipshare.concreteapp.model.CustomerSite;
import com.equipshare.concreteapp.model.Result;
import com.equipshare.concreteapp.model.User_;
import com.equipshare.concreteapp.network.RetrofitInterface;
import com.equipshare.concreteapp.utils.Constants;
import com.equipshare.concreteapp.utils.SessionManagement;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Jarvis on 05-01-2018.
 */

public class AvailableQuote extends Fragment {
    private RecyclerView recyclerView;
    User_ u;
    TextView empty;
    LinearLayoutManager gridLayoutManager;
    Gson gson = new GsonBuilder().setLenient().create();
    OkHttpClient client = new OkHttpClient();
    Retrofit.Builder builder=new Retrofit.Builder().baseUrl(Constants.BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create(gson));
    Retrofit retrofit=builder.build();
    RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);
    List<CustomerSite> site;
    ProgressDialog progressDialog;
    String token;
    SessionManagement session;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.available_quote,container,false);
        session = new SessionManagement(getContext());
        HashMap<String, String> user1 = session.getUserDetails();
     token=user1.get(SessionManagement.KEY_TOKEN);
        recyclerView=(RecyclerView)view.findViewById(R.id.quote_recyclerview);
       gridLayoutManager = new LinearLayoutManager(getContext());
        gridLayoutManager.setReverseLayout(true);
        gridLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        final Result res1=((RequestQuote)getActivity()).res;
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e("USER Quote", "value:"+u );
        Call<User_> call=retrofitInterface.show_profile(token);
        call.enqueue(new Callback<User_>() {
            @Override
            public void onResponse(Call<User_> call, Response<User_> response) {
                u=response.body();
                Log.e("Userinfo Response ", "response 33: " + new Gson().toJson(response.body()));
                //name=user.getUser().getName();
                 site=u.getCustomerSite();
                QuotesAdapter quotesAdapter=new QuotesAdapter(res1.getResults().getQuotes(),site,u,res1.getToken());
                recyclerView.setAdapter(quotesAdapter);
                progressDialog.cancel();
                if(gridLayoutManager.getItemCount()==0)
                {
                    empty=(TextView)view.findViewById(R.id.emptyquote);
                    empty.setText("No Records Of Quotes Found");
                }
                else{
                    View b = view.findViewById(R.id.emptyquote);
                    b.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<User_> call, Throwable t) {
                Log.e("TAG", "response 33: " + t.getMessage());
            }
        });

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}
