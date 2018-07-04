package com.equipshare.concreteapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.equipshare.concreteapp.model.History;
import com.equipshare.concreteapp.model.Result;
import com.equipshare.concreteapp.model.User_;
import com.equipshare.concreteapp.network.RetrofitInterface;
import com.equipshare.concreteapp.utils.Constants;
import com.equipshare.concreteapp.utils.SessionManagement;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Jarvis on 21-12-2017.
 */

public class History_fragment extends Fragment {

    private RecyclerView recyclerView;

    Result res;
    User_ user;
    TextView empty;
    LinearLayoutManager gridLayoutManager;
    Gson gson = new GsonBuilder().setLenient().create();
    SessionManagement session;
    String token;
    OkHttpClient client = new OkHttpClient();
    Retrofit.Builder builder=new Retrofit.Builder().baseUrl(Constants.BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create(gson));
    Retrofit retrofit=builder.build();
    RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view=inflater.inflate(R.layout.history,null);
        session = new SessionManagement(getContext());
        session.checkLogin(getActivity());
        HashMap<String, String> user1 = session.getUserDetails();
        token=user1.get(SessionManagement.KEY_TOKEN);
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerViewHistory);
        gridLayoutManager = new LinearLayoutManager(getContext());
        gridLayoutManager.setReverseLayout(true);
        gridLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        res=((DashBoard)getActivity()).r;
        user=((DashBoard)getActivity()).u;
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<History> call=retrofitInterface.history(token);
        call.enqueue(new Callback<History>() {
            @Override
            public void onResponse(Call<History> call, Response<History> response) {
                History history=response.body();
                Log.e("TAG", "response 33: "+new Gson().toJson(response.body()));
                Log.e("TAG", "response 33: "+new Gson().toJson(response.body()));
               CustomAdapter customAdapter=new CustomAdapter(history.getOrders(),user.getUser().getCustomerSite(),res.getToken());
                recyclerView.setAdapter(customAdapter);
                Log.e("TAG", "count 33: "+gridLayoutManager.getItemCount());
                progressDialog.cancel();
                if(gridLayoutManager.getItemCount()==0)
                {
                    empty=(TextView)view.findViewById(R.id.emptyhistory);
                    empty.setText("No Orders Were Placed");
                }
                else{
                    View b = view.findViewById(R.id.emptyhistory);
                    b.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<History> call, Throwable t) {
                Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void onResume() {
        super.onResume();

        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("History");
    }
}
