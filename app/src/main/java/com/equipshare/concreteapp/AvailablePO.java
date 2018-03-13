package com.equipshare.concreteapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.equipshare.concreteapp.model.PO;

import com.equipshare.concreteapp.model.POget;
import com.equipshare.concreteapp.model.Result;
import com.equipshare.concreteapp.model.User;
import com.equipshare.concreteapp.model.User_;
import com.equipshare.concreteapp.network.RetrofitInterface;
import com.equipshare.concreteapp.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.content.ContentValues.TAG;

/**
 * Created by Jarvis on 23-01-2018.
 */

public class AvailablePO extends Fragment {

    TextView empty;
    Result r1;
    User_ user;
    ProgressDialog progressDialog;
    Gson gson = new GsonBuilder().create();
    Retrofit.Builder builder=new Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson));
    Retrofit retrofit=builder.build();
    RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);
    private RecyclerView recyclerView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view =inflater.inflate(R.layout.available_po,null);
        recyclerView=(RecyclerView)view.findViewById(R.id.recyclerViewPO);
        final LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        gridLayoutManager.setReverseLayout(true);
        gridLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(gridLayoutManager);
         r1=((DashBoard)getActivity()).r;
         user=((DashBoard)getActivity()).u;
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
       // Log.e("TAG", "response 33: " + r1.getUser().getId());
        Call<POget> call=retrofitInterface.getpo(r1.getToken());
        call.enqueue(new Callback<POget>() {
            @Override
            public void onResponse(Call<POget> call, retrofit2.Response<POget> response) {

                POget pOget=response.body(); // have your all data

                Log.e("TAG", "response 33: " + new Gson().toJson(response.body()));
                Log.e("TAG", "response 33: " + response.body());
                PoAdapter poAdapter=new PoAdapter(pOget.getData(),user.getUser().getCustomerSite(),r1.getToken());
                recyclerView.setAdapter(poAdapter);
                progressDialog.cancel();
                if(gridLayoutManager.getItemCount()==0)
                {
                    empty=(TextView)view.findViewById(R.id.emptypo);
                    empty.setText("No Records Of PO");
                }
                else{
                    View b = view.findViewById(R.id.emptypo);
                    b.setVisibility(View.GONE);
                }
            }
            @Override
            public void onFailure(Call<POget> call, Throwable t) {
                Toast.makeText(getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }

        });

        return view;
    }
}

