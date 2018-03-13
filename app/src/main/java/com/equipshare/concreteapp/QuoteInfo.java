package com.equipshare.concreteapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.equipshare.concreteapp.model.Quote;
import com.equipshare.concreteapp.model.Result;
import com.equipshare.concreteapp.model.User_;
import com.equipshare.concreteapp.network.RetrofitInterface;
import com.equipshare.concreteapp.utils.Constants;
import com.equipshare.concreteapp.utils.DirectingClass;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class QuoteInfo extends AppCompatActivity {
    Gson gson = new GsonBuilder().setLenient().create();
    OkHttpClient client = new OkHttpClient();
    Retrofit.Builder builder=new Retrofit.Builder().baseUrl(Constants.BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create(gson));
    Retrofit retrofit=builder.build();
    RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);
    Quote quote;
    ProgressDialog progressDialog;
    Spinner response1;
    long price1;
    List<String> list1,supp_list;
    ArrayAdapter<String> adapter1;
    String site,supplier_name,supp_id,val_date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_info);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i=getIntent();
        final User_ u;
        quote=i.getParcelableExtra("quote");
        u=i.getParcelableExtra("user");
        Log.e("quoteinfo", "User "+u );
        final String token=i.getStringExtra("token");
        site=i.getStringExtra("customersite_quote");
        Log.e("Quoteinfo", "Sitename:"+site);
        final TextView gendate,quantity,quality,requestedby,cust_site,valdate,price;
        gendate=(TextView)findViewById(R.id.Quote_gen_date);
        quality=(TextView)findViewById(R.id.Quote_quality);
        quantity=(TextView)findViewById(R.id.Quote_quantity);
        requestedby=(TextView)findViewById(R.id.Quote_requestedby);
        cust_site=(TextView)findViewById(R.id.Quote_site);
        price=(TextView)findViewById(R.id.Quote_price);
        valdate=(TextView)findViewById(R.id.Quote_valdate);
        response1=(Spinner)findViewById(R.id.response);
        Button cancel_quote=(Button)findViewById(R.id.deletequotebutton);
        Button accept_quote=(Button)findViewById(R.id.placepo);
        progressDialog=new ProgressDialog(QuoteInfo.this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        accept_quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("Quoteinfo", "Sitename:"+site);
                Intent intent=new Intent(QuoteInfo.this,CreatePO.class);
                intent.putExtra("Quote",quote);
                intent.putExtra("sitename",site);
                intent.putExtra("token",token);
                intent.putExtra("name",supplier_name);
                intent.putExtra("suppid",supp_id);
                intent.putExtra("price",price1);
                intent.putExtra("valdate",val_date);
                startActivity(intent);
            }
        });
        final LinearLayout linearLayout=(LinearLayout)findViewById(R.id.quote_info);
        cancel_quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new AlertDialog.Builder(QuoteInfo.this)
                        .setTitle("Delete Quote")
                        .setMessage("Are you sure you want to Cancel this Quote?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                progressDialog=new ProgressDialog(QuoteInfo.this);
                                progressDialog.setMessage("Deleting");
                                progressDialog.setCancelable(false);
                                progressDialog.show();
                                RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);
                                Map<String,String> map=new HashMap<>();
                                map.put("quoteId",quote.getId());
                                Call<Result> call=retrofitInterface.cancel_quote(map);
                                call.enqueue(new Callback<Result>() {
                                    @Override
                                    public void onResponse(Call<Result> call, Response<Result> response) {
                                        Result r=response.body();
                                        if(r.getMsg().contains("cancelled"))
                                        {progressDialog.cancel();
                                            Snackbar snackbar = Snackbar
                                                .make(linearLayout, "Quote Deleted Successfully", Snackbar.LENGTH_LONG);
                                        snackbar.setActionTextColor(Color.RED);
                                        snackbar.show();
                                        Log.e("TAG", "response 33: "+new Gson().toJson(response.body()));
                                        DirectingClass directingClass=new DirectingClass(getApplicationContext(),QuoteInfo.this);
                                        directingClass.performLogin();

                                    }
                                    else {
                                          progressDialog.cancel();
                                            Toast.makeText(view.getContext(),"Cannot Delete Quote,Please try again after some time",Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<Result> call, Throwable t) {
                                        Toast.makeText(view.getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                });
                            }

                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        });
        long milliseconds=Long.parseLong(quote.getGenerationDate());

        Date date = new Date(milliseconds);
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM,yyyy", Locale.ENGLISH);
        gendate.setText("Quote created on:"+formatter.format(date));
        quality.setText("Quality:"+quote.getQuality());
        quantity.setText("Quantity:"+quote.getQuantity());
        requestedby.setText("Quote requested by:"+quote.getRequestedBy());
        cust_site.setText("Customer Site:"+site);

        list1=new ArrayList<>();
        supp_list=new ArrayList<>();
        for (int j=0;j<quote.getResponses().size();j++)
        {
            list1.add(quote.getResponses().get(j).getRmxId());
        }
        if(!quote.getResponses().isEmpty())
        {
        for(int j=0;j<list1.size();j++)
        {
           RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);
            Call<Result> call=retrofitInterface.supp_name(list1.get(j));
            call.enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    Log.e("TAG", "response 33: "+new Gson().toJson(response.body()));
                    Result result=response.body();
                    supp_list.add(result.getSuppname());
                    Log.e("TAG", "supplist: "+supp_list);

                        adapter1 = new ArrayAdapter<String>(QuoteInfo.this,
                                android.R.layout.simple_spinner_item, supp_list);
                        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        response1.setAdapter(adapter1);
                        progressDialog.cancel();
                        response1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                price.setText("Price:"+String.valueOf(quote.getResponses().get(i).getPrice()));
                                valdate.setText("Valid Till:"+quote.getResponses().get(i).getValidTill());
                                supplier_name=supp_list.get(i);
                                supp_id=quote.getResponses().get(i).getRmxId();
                                price1=quote.getResponses().get(i).getPrice();
                                val_date=quote.getResponses().get(i).getValidTill();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }


                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                 Toast.makeText(QuoteInfo.this,t.getMessage(),Toast.LENGTH_LONG).show();
                // progressDialog.cancel();
                }
            });
        }

    }
    else{
            price.setVisibility(View.GONE);
            valdate.setVisibility(View.GONE);
            accept_quote.setVisibility(View.GONE);
            list1.clear();
            list1.add("No Response");
            progressDialog.cancel();
            adapter1 = new ArrayAdapter<String>(QuoteInfo.this,
                    android.R.layout.simple_spinner_item, list1);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            response1.setAdapter(adapter1);
        }
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
