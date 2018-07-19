package com.equipshare.concreteapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
    String site,supplier_name,supp_id;
    ResponseAdapter responseAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager gridLayoutManager;
    TextView response_status;
    int size;
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
        size=quote.getQuality().size();
        final TextView gendate,requestedby,cust_site,valdate;
        TextView[] quality=new TextView[5];
        final TextView[] quantity=new TextView[5];
        recyclerView=(RecyclerView)findViewById(R.id.quote_recyclerview_response);
        gridLayoutManager = new LinearLayoutManager(QuoteInfo.this);
        gridLayoutManager.setReverseLayout(true);
        gridLayoutManager.setStackFromEnd(true);
        response_status=(TextView) findViewById(R.id.response_status);
        recyclerView.setLayoutManager(gridLayoutManager);
        gendate=(TextView)findViewById(R.id.Quote_gen_date);
        quality[0]=(TextView)findViewById(R.id.qual1);
        quality[1]=(TextView)findViewById(R.id.qual2);
        quality[2]=(TextView)findViewById(R.id.qual3);
        quality[3]=(TextView)findViewById(R.id.qual4);
        quality[4]=(TextView)findViewById(R.id.qual5);
        quantity[0]=(TextView)findViewById(R.id.quan1);
        quantity[1]=(TextView)findViewById(R.id.quan2);
        quantity[2]=(TextView)findViewById(R.id.quan3);
        quantity[3]=(TextView)findViewById(R.id.quan4);
        quantity[4]=(TextView)findViewById(R.id.quan5);
        requestedby=(TextView)findViewById(R.id.Quote_requestedby);
        cust_site=(TextView)findViewById(R.id.Quote_site);

        progressDialog=new ProgressDialog(QuoteInfo.this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();

        for(int k=0;k<size;k++)
        {
            quality[k].setText(quote.getQuality().get(k));
            quantity[k].setText(quote.getQuantity().get(k));
        }
        for(int z=size;z<5;z++)
        {
            quality[z].setVisibility(View.GONE);
            quantity[z].setVisibility(View.GONE);
        }
        final LinearLayout linearLayout=(LinearLayout)findViewById(R.id.quote_info);

        long milliseconds=Long.parseLong(quote.getGenerationDate());

        Date date = new Date(milliseconds);
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM,yyyy", Locale.ENGLISH);
        gendate.setText("Quote created on:"+formatter.format(date));
        requestedby.setText("Quote requested by:"+quote.getRequestedBy());
        cust_site.setText("Customer Site:"+site);

        list1=new ArrayList<>();

        supp_list=new ArrayList<>();
        for (int j=0;j<quote.getResponses().size();j++)
        {
            list1.add(String.valueOf(quote.getResponses().get(j).getRmxId()));
        }
        if(!quote.getResponses().isEmpty())
        {
            response_status.setVisibility(View.GONE);
            responseAdapter=new ResponseAdapter(quote,token,site);
            recyclerView.setAdapter(responseAdapter);
        for(int j=0;j<list1.size();j++)
        {Log.e("TAG", "list1: "+list1);
           RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);
            Call<Result> call=retrofitInterface.supp_name(list1.get(j));
            call.enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    Log.e("TAG", "response 33: "+new Gson().toJson(response.body()));
                    Result result=response.body();
                    supp_list.add(result.getSuppname());
                    Log.e("TAG", "supplist: "+supp_list);

                        /*adapter1 = new ArrayAdapter<String>(QuoteInfo.this,
                                android.R.layout.simple_spinner_item, supp_list);
                        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        response1.setAdapter(adapter1);*/
                        progressDialog.cancel();

                      /*  response1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
*//*
                                for(int r=0;r<size;r++)
                                {
                                    price[r].setText("Price:"+String.valueOf(quote.getPrice().get(r)));
                                }*//*
                                Log.e("quoteinfo", "price"+quote.getPrice() );
                                valdate.setText("Valid Till: "+getDate(quote.getResponses().get(i).getValidTill()));
                                supplier_name=supp_list.get(i);
                                supp_id=quote.getResponses().get(i).getRmxId();
//                                price1=quote.getResponses().get(i).getPrice();
                                val_date=getDate(quote.getResponses().get(i).getValidTill());
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });*/
                    }


                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                 Toast.makeText(QuoteInfo.this,t.getMessage(),Toast.LENGTH_LONG).show();
                progressDialog.cancel();
                }
            });
        }

    }
   else{

            //accept_quote.setVisibility(View.GONE);
            /*list1.clear();
            list1.add("No Response");

            adapter1 = new ArrayAdapter<String>(QuoteInfo.this,
                    android.R.layout.simple_spinner_item, list1);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            response1.setAdapter(adapter1);*/

            progressDialog.cancel();
        }
    }

    String getDate(String milli)
    {
        long milliseconds=Long.parseLong(milli);
        Date date = new Date(milliseconds);
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM,yyyy", Locale.ENGLISH);
        String s=formatter.format(date);
        return s;

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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.quote_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.deletequote) {
            final LinearLayout linearLayout=(LinearLayout)findViewById(R.id.quote_info);
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
                                        Toast.makeText(getApplicationContext(),"Cannot Delete Quote,Please try again after some time",Toast.LENGTH_LONG).show();
                                    }
                                }

                                @Override
                                public void onFailure(Call<Result> call, Throwable t) {
                                    progressDialog.cancel();
                                    Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                                }
                            });
                        }

                    })
                    .setNegativeButton("No", null)
                    .show();
        }

        return super.onOptionsItemSelected(item);
    }
}
