package com.equipshare.concreteapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.equipshare.concreteapp.model.POget;
import com.equipshare.concreteapp.model.Result;
import com.equipshare.concreteapp.model.User_;
import com.equipshare.concreteapp.network.RetrofitInterface;
import com.equipshare.concreteapp.utils.Constants;
import com.equipshare.concreteapp.utils.DirectingClass;
import com.equipshare.concreteapp.utils.UserInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

public class OrderBook extends AppCompatActivity {
     EditText requierddate,quantity,del_time;
    Spinner quality,PO,customersite;
    Result resl;
    User_ user_;
    String cust_site,supp_id,po_id;
    Long quan;
    Calendar myCalendar;
    String name,id;
    LinearLayout linearLayout;
    POget pOget;
    ArrayAdapter<String> adapter1;
    long milli_req_date;
    List<String> list1,poid,supp_list;
    List<Long> quantity_list;
    ProgressDialog progressDialog;

  Gson gson = new GsonBuilder().setLenient().create();
    OkHttpClient client = new OkHttpClient();
    Retrofit.Builder builder=new Retrofit.Builder().baseUrl(Constants.BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create(gson));
    Retrofit retrofit=builder.build();
    RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_book);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i=getIntent();
        progressDialog=new ProgressDialog(OrderBook.this);
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        del_time=(EditText)findViewById(R.id.order_deltime);


        resl=i.getParcelableExtra("Result");
        Call<User_> call=retrofitInterface.show_profile(resl.getToken());
        call.enqueue(new Callback<User_>() {
            @Override
            public void onResponse(Call<User_> call, Response<User_> response) {
                user_ = response.body();
                Log.e("Userinfo Response ", "response 33: " + new Gson().toJson(response.body()));
                name = user_.getUser().getName();
                id = user_.getUser().getId();
                List<String> list = new ArrayList<String>();
                supp_list = new ArrayList<String>();
                poid = new ArrayList<String>();
                list1=new ArrayList<>();
                quantity_list = new ArrayList<Long>();
                ArrayAdapter<String> adapter;
                for (int j = 0; j < user_.getUser().getCustomerSite().size(); j++) {
                    list.add(user_.getUser().getCustomerSite().get(j).getName());
                }
                if (user_.getUser().getCustomerSite().isEmpty()) {
                    list.add("No Site Available");
                    adapter = new ArrayAdapter<String>(OrderBook.this,
                            android.R.layout.simple_spinner_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    customersite.setAdapter(adapter);
                    list1.add("No Supplier Available");
                    adapter1 = new ArrayAdapter<String>(OrderBook.this,
                            android.R.layout.simple_spinner_item, list1);
                    adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    PO.setAdapter(adapter1);
                    progressDialog.cancel();
                } else {
                    adapter = new ArrayAdapter<String>(OrderBook.this,
                            android.R.layout.simple_spinner_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    customersite.setAdapter(adapter);
                    progressDialog.cancel();
                    customersite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                            progressDialog=new ProgressDialog(OrderBook.this);
                            progressDialog.setMessage("Loading");
                            progressDialog.setCancelable(false);
                            progressDialog.show();
                            cust_site = user_.getUser().getCustomerSite().get(i).getId();
                            Call<POget> cal = retrofitInterface.getpo(resl.getToken());
                            cal.enqueue(new Callback<POget>() {
                                @Override
                                public void onResponse(Call<POget> cal, retrofit2.Response<POget> response) {

                                    pOget = response.body(); // have your all data
                                    Log.e("PO", "response 33: " + new Gson().toJson(response.body()));
                                    boolean check=false;
                                   if(pOget.getData().isEmpty())
                                   {
                                       list1.add("No Supplier Available");
                                       adapter1 = new ArrayAdapter<String>(OrderBook.this,
                                               android.R.layout.simple_spinner_item, list1);
                                       adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                       PO.setAdapter(adapter1);
                                       progressDialog.cancel();
                                   }
                                   else{ list1.clear();
                                    for (int j = 0; j < pOget.getData().size(); j++) {
                                        if (cust_site.equals(pOget.getData().get(j).getCustomerSite())&&!pOget.getData().get(j).getDeletedByContractor()&&pOget.getData().get(j).getConfirmedBySupplier()) {
                                            list1.add(pOget.getData().get(j).getSupplierId());
                                            poid.add(pOget.getData().get(j).getId());
                                            check=true;
                                            if(pOget.getData().get(j).getRemQuantity()==null)
                                            {
                                                quantity_list.add(Long.valueOf(pOget.getData().get(j).getQuantity()));
                                            }
                                            else quantity_list.add(Long.valueOf(pOget.getData().get(j).getRemQuantity()));
                                        }

                                    }
                                    if(list1.isEmpty())
                                    {
                                        list1.clear();
                                        list1.add("No Supplier Available");
                                        adapter1 = new ArrayAdapter<String>(OrderBook.this,
                                                android.R.layout.simple_spinner_item, list1);
                                        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        PO.setAdapter(adapter1);
                                        progressDialog.cancel();
                                    }
                                    else
                                       {for(int z=0;z<list1.size();z++)
                                      {supp_list.clear();
                                       Call<Result> call=retrofitInterface.supp_name(list1.get(z));
                                       call.enqueue(new Callback<Result>() {
                                           @Override
                                           public void onResponse(Call<Result> call, Response<Result> response) {
                                               Log.e("TAG", "response 33: "+new Gson().toJson(response.body()));
                                               Result result=response.body();
                                               supp_list.add(result.getSuppname());
                                               adapter1 = new ArrayAdapter<String>(OrderBook.this,
                                                       android.R.layout.simple_spinner_item, supp_list);
                                               adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                               PO.setAdapter(adapter1);
                                               progressDialog.cancel();
                                              PO.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                  @Override
                                                  public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                                      supp_id=list1.get(i);
                                                      po_id=poid.get(i);
                                                      quan=quantity_list.get(i);
                                                  }

                                                  @Override
                                                  public void onNothingSelected(AdapterView<?> adapterView) {

                                                  }
                                              });
                                           }


                                           @Override
                                           public void onFailure(Call<Result> call, Throwable t) {
                                               Toast.makeText(OrderBook.this,t.getMessage(),Toast.LENGTH_LONG).show();

                                           }
                                       });
                                   }



                                }}}

                                @Override
                                public void onFailure(Call<POget> cal, Throwable t) {
                                    Toast.makeText(OrderBook.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                            });

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> adapterView) {

                        }
                    });
                }
            }
            @Override
            public void onFailure(Call<User_> call, Throwable t) {
                Log.e("TAG", "response 33: " + t.getMessage());
            }
        });
        Log.e("UserInfo","user:"+user_);
        myCalendar = Calendar.getInstance();
        requierddate=(EditText)findViewById(R.id.Date_required_orderbook);
        quantity=(EditText)findViewById(R.id.Quantity_orderbook);
        customersite=(Spinner)findViewById(R.id.Location_orderbook);
        quality=(Spinner)findViewById(R.id.Quality_orderbook);
        PO=(Spinner)findViewById(R.id.PO_orderbook);
        linearLayout=(LinearLayout)findViewById(R.id.place_order);





     /*  for(int j=0;j<resl.getResults().getQuotes().size();j++)
        {   if(resl.getResults().getQuotes().get(j).getResponses()!=null) {
            for (int k = 0; k < resl.getResults().getQuotes().get(j).getResponses().size(); k++) {
                list1.add("name=" + resl.getResults().getQuotes().get(j).getResponses().get(k).getRmxId());
                qlist.add(resl.getResults().getQuotes().get(j).getResponses().get(k).getRmxId());

            }
        }
        }*/
        Log.e("check","suppid"+supp_id);
        Button submit=(Button)findViewById(R.id.Confirm);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        final TimePickerDialog.OnTimeSetListener time=new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hours, int minutes) {

                myCalendar.set(Calendar.HOUR_OF_DAY,hours);
                myCalendar.set(Calendar.MINUTE,minutes);
                updateLabel1();
            }
        };
        del_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TimePickerDialog(OrderBook.this,time
                        ,myCalendar.get(Calendar.HOUR)
                        ,myCalendar.get(Calendar.MINUTE)
                        ,false).show();
            }
        });
        requierddate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(OrderBook.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog=new ProgressDialog(OrderBook.this);
                progressDialog.setMessage("Processing");
                progressDialog.setCancelable(false);
                progressDialog.show();
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                startOrderbook();
            }
        });
    }

    private void startOrderbook()
    {
        if(requierddate.getText().toString().isEmpty()){
            requierddate.setError("Required Field");
            requierddate.requestFocus();
            progressDialog.cancel();
            return;
        }
        if(quantity.getText().toString().isEmpty()){
            quantity.setError("Required Field");
            quantity.requestFocus();
            progressDialog.cancel();
            return;
        }

        if(PO.getSelectedItem().toString().equals("No Supplier Available"))
        {
            quantity.setError("Supplier Required");
            quantity.requestFocus();
            progressDialog.cancel();
            return;
        }
        if(customersite.getSelectedItem().toString().equals("No Site Available"))
        {
            quantity.setError("Site Required");
            quantity.requestFocus();
            progressDialog.cancel();
            return;
        }
        long test= Long.parseLong(quantity.getText().toString());
        if(test>quan)
        {
            quantity.setError("Quantity Should Not Exceed "+quan+"cubic meter");
            quantity.requestFocus();
            progressDialog.cancel();
            return;
        }
        if((milli_req_date<System.currentTimeMillis()))
        {
            requierddate.setError("Date Entered Should More From Current Date");
            requierddate.requestFocus();
            progressDialog.cancel();
            return;
        }
        if(milli_req_date>(System.currentTimeMillis())+1209600000)
        {
            requierddate.setError("Date Entered Should Be Under 15 Days From Current Date");
            requierddate.requestFocus();
            progressDialog.cancel();
            return;
        }
        sendOrder(quantity.getText().toString(),quality.getSelectedItem().toString(),PO.getSelectedItem().toString(),name,id);
    }

    private void sendOrder(String quantity, String quality, String supplier_id,String name,String id)
    {
        Log.e("TAG","suppid"+supp_id);
        Log.e("TAG", "name "+name+id);
        RetrofitInterface retrofitInterface =retrofit.create(RetrofitInterface.class);
        Map<String,String> map=new HashMap<>();
        map.put("requiredDate", String.valueOf(milli_req_date));
        map.put("quantity",quantity);
        map.put("quality",quality);
        map.put("requestedBy",name);
        map.put("supplierId",supp_id);
        map.put("companyName","Abcd");
        map.put("customerSite",cust_site);
        map.put("POId",po_id);
        Call<ResponseBody> call=retrofitInterface.submit_order(resl.getToken(),map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.cancel();
                Snackbar snackbar = Snackbar
                        .make(linearLayout, "Order Placed Successfully", Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.RED);
                snackbar.show();
                Log.e("TAG", "response 33: "+new Gson().toJson(response.body()));
               DirectingClass directingClass=new DirectingClass(getApplicationContext(),OrderBook.this);
                directingClass.performLogin();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(OrderBook.this,t.getMessage(),Toast.LENGTH_SHORT).show();
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

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date date=myCalendar.getTime();
        milli_req_date=date.getTime();
        requierddate.setText(sdf.format(myCalendar.getTime()));

    }
    private void updateLabel1() {
        String myFormat = "HH:mm a"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        Date date=myCalendar.getTime();
        milli_req_date=date.getTime();
        Log.e("TAG", "Time 33: "+milli_req_date+date);
        del_time.setText(sdf.format(myCalendar.getTime()));

    }
}