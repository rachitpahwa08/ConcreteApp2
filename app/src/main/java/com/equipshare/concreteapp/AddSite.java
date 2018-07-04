package com.equipshare.concreteapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;


import com.equipshare.concreteapp.model.Result;
import com.equipshare.concreteapp.model.User;
import com.equipshare.concreteapp.network.RetrofitInterface;
import com.equipshare.concreteapp.utils.Constants;
import com.equipshare.concreteapp.utils.DirectingClass;

import com.equipshare.concreteapp.utils.SessionManagement;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddSite extends AppCompatActivity {
    User user;
    Result result;
    Gson gson = new GsonBuilder().setLenient().create();
    OkHttpClient client = new OkHttpClient();
    SessionManagement session;
    String token;
    Retrofit.Builder builder=new Retrofit.Builder().baseUrl(Constants.BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create(gson));
    Retrofit retrofit=builder.build();
    RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);
    LinearLayout linearLayout;
    EditText sitename,site_address;
    Spinner city;
    List<String> cityList;
    ArrayAdapter adapter1;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_site);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session = new SessionManagement(AddSite.this);
        HashMap<String, String> user1 = session.getUserDetails();
        token=user1.get(SessionManagement.KEY_TOKEN);
        Intent i=getIntent();
        user=i.getParcelableExtra("User");
        result=i.getParcelableExtra("Result");
        sitename=(EditText) findViewById(R.id.sitename);
        site_address=(EditText) findViewById(R.id.site_address);
        city=(Spinner)findViewById(R.id.city);
        Button submit=(Button)findViewById(R.id.submit_site);
        linearLayout=(LinearLayout)findViewById(R.id.addsite_layout);
        cityList=new ArrayList<>();
        progressDialog=new ProgressDialog(AddSite.this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
       Call<Result> call1=retrofitInterface.get_cityname();
       call1.enqueue(new Callback<Result>() {
           @Override
           public void onResponse(Call<Result> call, Response<Result> response) {
               Log.e("TAG", "response 33: "+new Gson().toJson(response.body()));
               Result result=response.body();
             if(result.getSuccess())
             {   for(int j=0;j<result.getCities().size();j++) {
                 cityList.add(result.getCities().get(j).getCityName());
             }
                 adapter1 = new ArrayAdapter<String>(AddSite.this,
                         android.R.layout.simple_spinner_item, cityList);
                 adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                 city.setAdapter(adapter1);
                 progressDialog.cancel();
             }
           }

           @Override
           public void onFailure(Call<Result> call, Throwable t) {
               Toast.makeText(AddSite.this,t.getMessage(),Toast.LENGTH_SHORT).show();
           }
       });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                startAddsite();
            }
        });
    }

    private void startAddsite()
    {
        if(sitename.getText().toString().isEmpty()){
            sitename.setError("Required Field");
            sitename.requestFocus();
            return;
        }
        if(site_address.getText().toString().isEmpty()){
            site_address.setError("Required Field");
            site_address.requestFocus();
            return;
        }
        submitAddsite(sitename.getText().toString(),site_address.getText().toString());
        progressDialog=new ProgressDialog(AddSite.this);
        progressDialog.setMessage("Creating Site");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    private void submitAddsite(String name, String address)
    {
        RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);
        Map<String,String> map=new HashMap<>();
        map.put("name",name);
        map.put("lat","0");
        map.put("long","0");
        map.put("address",address);
        map.put("city",city.getSelectedItem().toString());

        Call<Result> call=retrofitInterface.add_site(token,map);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Result r=response.body();
                if(r.getMsg().equals("user created"))
                {Snackbar snackbar = Snackbar
                        .make(linearLayout, "Site Added Successfully", Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.RED);
                snackbar.show();
                Log.e("TAG", "response 33: "+new Gson().toJson(response.body()));
                progressDialog.cancel();
                DirectingClass directingClass=new DirectingClass(getApplicationContext(),AddSite.this);
                directingClass.performLogin();

            }
            else {
                    Toast.makeText(AddSite.this,"Error Adding Site",Toast.LENGTH_LONG).show();
                }}

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                progressDialog.cancel();
                Toast.makeText(AddSite.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
