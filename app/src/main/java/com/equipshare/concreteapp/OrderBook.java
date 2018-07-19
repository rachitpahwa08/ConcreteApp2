package com.equipshare.concreteapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
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

import com.equipshare.concreteapp.model.PO;
import com.equipshare.concreteapp.model.POget;
import com.equipshare.concreteapp.model.Result;
import com.equipshare.concreteapp.model.User_;
import com.equipshare.concreteapp.network.RetrofitInterface;
import com.equipshare.concreteapp.utils.Constants;
import com.equipshare.concreteapp.utils.DirectingClass;

import com.equipshare.concreteapp.utils.SessionManagement;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
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
    Spinner PO,customersite;
    Result resl;
    User_ user_;
    int index;
    String cust_site,supp_id,po_id,cust_address,company_name;
    EditText[] quantity1=new EditText[5];
    EditText[] quality1=new EditText[5];
    EditText[] remQuantity=new EditText[5];
    SessionManagement session;
    Calendar myCalendar;
    String name,id;
    LinearLayout[] quan_qual=new LinearLayout[5];
    LinearLayout linearLayout;
    POget pOget;
    ArrayAdapter<String> adapter1;
    long milli_req_date;
    List<String> list1,poid,supp_list,qual_list,quantity_list;
    //List<Long> quantity_list;
    int size;
    ProgressDialog progressDialog;
    String token;
    com.equipshare.concreteapp.model.PO po;
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
        session = new SessionManagement(OrderBook.this);
        HashMap<String, String> user1 = session.getUserDetails();
        token=user1.get(SessionManagement.KEY_TOKEN);
        qual_list=new ArrayList<>();
        quantity_list= new ArrayList<>();
        quantity1[0]=(EditText)findViewById(R.id.Quantity_orderbook1);
        quantity1[1]=(EditText)findViewById(R.id.Quantity_orderbook2);
        quantity1[2]=(EditText)findViewById(R.id.Quantity_orderbook3);
        quantity1[3]=(EditText)findViewById(R.id.Quantity_orderbook4);
        quantity1[4]=(EditText)findViewById(R.id.Quantity_orderbook5);
        quality1[0]=(EditText) findViewById(R.id.Quality_orderbook1);
        quality1[1]=(EditText) findViewById(R.id.Quality_orderbook2);
        quality1[2]=(EditText) findViewById(R.id.Quality_orderbook3);
        quality1[3]=(EditText) findViewById(R.id.Quality_orderbook4);
        quality1[4]=(EditText) findViewById(R.id.Quality_orderbook5);
        quan_qual[0]=(LinearLayout)findViewById(R.id.quan_qua1);
        quan_qual[1]=(LinearLayout)findViewById(R.id.quan_qua2);
        quan_qual[2]=(LinearLayout)findViewById(R.id.quan_qua3);
        quan_qual[3]=(LinearLayout)findViewById(R.id.quan_qua4);
        quan_qual[4]=(LinearLayout)findViewById(R.id.quan_qua5);
        remQuantity[0]=(EditText) findViewById(R.id.RemQuantity_orderbook1);
        remQuantity[1]=(EditText) findViewById(R.id.RemQuantity_orderbook2);
        remQuantity[2]=(EditText) findViewById(R.id.RemQuantity_orderbook3);
        remQuantity[3]=(EditText) findViewById(R.id.RemQuantity_orderbook4);
        remQuantity[4]=(EditText) findViewById(R.id.RemQuantity_orderbook5);
        resl=i.getParcelableExtra("Result");

        final TextInputLayout textInputLayout=(TextInputLayout)findViewById(R.id.test1);
        for(int r=0;r<5;r++)
        {
            quan_qual[r].setVisibility(View.GONE);
            quantity1[r].setText("0");
        }
        quantity1[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity1[0].getText().clear();
            }
        });
        quantity1[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity1[0].setText(null);
            }
        });
        quantity1[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity1[0].setText("");
            }
        });
        quantity1[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity1[0].setText("");
            }
        });
        quantity1[4].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity1[0].setText("");
            }
        });
        textInputLayout.getEditText().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                quantity1[0].setText("");
            }
        });
        Call<User_> call=retrofitInterface.show_profile(token);
        call.enqueue(new Callback<User_>() {
            @Override
            public void onResponse(Call<User_> call, Response<User_> response){
                user_ = response.body();
                Log.e("Userinfo Response ", "response 33: " + new Gson().toJson(response.body()));
                name = user_.getUser().getName();
                id = String.valueOf(user_.getUser().getUserId());
                company_name=user_.getUser().getCompany();
                List<String> list = new ArrayList<String>();
                supp_list = new ArrayList<String>();
                poid = new ArrayList<String>();
                list1=new ArrayList<>();
                //quantity_list = new ArrayList<Long>();
                ArrayAdapter<String> adapter;
                for (int j = 0; j < user_.getCustomerSite().size(); j++) {
                    list.add(user_.getCustomerSite().get(j).getName());
                    Log.d("Orderbook", "onResponse() returned: " + user_.getCustomerSite().get(j).getName());
                }
                if (user_.getCustomerSite().isEmpty()) {
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
                            Log.e("Orderbook", "cust_site spinner"+user_.getCustomerSite().get(i).getId() );
                            cust_site = user_.getCustomerSite().get(i).getId();
                            cust_address=user_.getCustomerSite().get(i).getAddress();
                            Call<POget> cal = retrofitInterface.getpo(token);
                            cal.enqueue(new Callback<POget>() {
                                @Override
                                public void onResponse(Call<POget> cal, retrofit2.Response<POget> response) {

                                    pOget = response.body(); // have your all data
                                    Log.e("PO", "response 33: " + new Gson().toJson(response.body()));

                                   if(pOget.getData().isEmpty())
                                   {
                                       list1.add("No Supplier Available");
                                       poid.add("no value");
                                       adapter1 = new ArrayAdapter<String>(OrderBook.this,
                                               android.R.layout.simple_spinner_item, list1);
                                       adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                       PO.setAdapter(adapter1);
                                       progressDialog.cancel();
                                   }
                                   else{ list1.clear();
                                        poid.clear();
                                       qual_list.clear();
                                       quantity_list.clear();

                                      //  qual_list.clear();
                                        //quantity_list.clear();
                                       Log.e("OrderBook", "Cust_site"+cust_site );
                                    for (int j = 0; j < pOget.getData().size(); j++) {
                                        if (cust_site.equals(pOget.getData().get(j).getCustomerSite())&&pOget.getData().get(j).getDeletedByContractor().equals("false")&&pOget.getData().get(j).getConfirmedBySupplier().equals("true")) {
                                            list1.add(String.valueOf(pOget.getData().get(j).getSupplierId()));
                                            poid.add(String.valueOf(pOget.getData().get(j).getPOId()));
                                           // qual_list.add(pOget.getData().get(j).getQuality());

                                           /* if(pOget.getData().get(j).getRemQuantity()==null)
                                            {
                                                quantity_list.add(Long.valueOf(pOget.getData().get(j).getQuantity()));
                                            }
                                            else quantity_list.add(Long.valueOf(pOget.getData().get(j).getRemQuantity()));*/
                                            for (int z=0;z<po.getValues().size();z++) {
                                                quality1[z].setText("");
                                                quan_qual[z].setVisibility(View.GONE);
                                                remQuantity[z].setText("");
                                            }
                                        }
                                        else if(cust_address.equals(pOget.getData().get(j).getCustomerSite())&&pOget.getData().get(j).getDeletedByContractor().equals("false")&&pOget.getData().get(j).getConfirmedBySupplier().equals("true"))
                                            {
                                                list1.add(String.valueOf(pOget.getData().get(j).getSupplierId()));
                                                poid.add(String.valueOf(pOget.getData().get(j).getPOId()));
                                               // qual_list.add(pOget.getData().get(j).getQuality());

                                               /* if(pOget.getData().get(j).getRemQuantity()==null)
                                                {
                                                    quantity_list.add(Long.valueOf(pOget.getData().get(j).getQuantity()));
                                                }
                                                else quantity_list.add(Long.valueOf(pOget.getData().get(j).getRemQuantity()));*/

                                            }

                                    }
                                       Log.e("TAG", "list1: "+list1);
                                    if(list1.isEmpty())
                                    {
                                        list1.clear();
                                        list1.add("No Supplier Available");
                                        poid.add("no value");
                                        adapter1 = new ArrayAdapter<String>(OrderBook.this,
                                                android.R.layout.simple_spinner_item, list1);
                                        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                        PO.setAdapter(adapter1);
                                        progressDialog.cancel();
                                    }
                                    else
                                       {for(int z=0,x=0;z<list1.size();z++,x++)
                                      {supp_list.clear();

                                          Log.e("TAG", "list1p: "+list1.get(z)+"index:"+index);
                                       Call<Result> call=retrofitInterface.supp_name(list1.get(z));

                                          final int finalX = x;
                                          call.enqueue(new Callback<Result>() {
                                           @Override
                                           public void onResponse(Call<Result> call, Response<Result> response) {
                                               Log.e("TAG", "suppliername "+new Gson().toJson(response.body()));
                                               Result result=response.body();
                                               index= finalX;
                                               Log.e("qual+supplier check", "quality:"/*+qual_list.get(index)*/+" By "+result.getSuppname()+"index check"+index);
                                               supp_list.add("Quality:"+/*qual_list.get(index)*/" By "+result.getSuppname());
                                               adapter1 = new ArrayAdapter<String>(OrderBook.this,
                                                       android.R.layout.simple_spinner_item, supp_list);
                                               adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                               PO.setAdapter(adapter1);
                                               progressDialog.cancel();
                                              PO.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                  @Override
                                                  public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                                      supp_id=list1.get(i);
                                                      Log.e("POID", "value"+poid );
                                                    if(poid.get(i).equals("no value"))
                                                    {po_id="no value";
                                                    quantity_list.clear();
                                                    qual_list.clear();
                                                        for (int z=0;z<po.getValues().size();z++) {
                                                            quality1[z].setText("");
                                                            quan_qual[z].setVisibility(View.GONE);
                                                            remQuantity[z].setText("");
                                                        }
                                                    }
                                                    else{
                                                        po_id=poid.get(i);
                                                      //  quan=quantity_list.get(i);
                                                        //qual_val=qual_list.get(i);
//                                                        quality.setText(qual_val);
                                                        qual_list.clear();
                                                        quantity_list.clear();

                                                        for(int k=0;k<pOget.getData().size();k++)
                                                        {       if(String.valueOf(pOget.getData().get(k).getPOId()).equals(po_id)){
                                                         po=pOget.getData().get(k);
                                                         break;
                                                        }}
                                                        for (int z=0;z<5;z++) {
                                                            quality1[z].setText("");
                                                            quan_qual[z].setVisibility(View.GONE);
                                                            remQuantity[z].setText("");
                                                        }
                                                        size=po.getValues().size();
                                                        for (int z=0;z<po.getValues().size();z++) {
                                                            quality1[z].setText(po.getValues().get(z).getQuality());
                                                            qual_list.add(po.getValues().get(z).getQuality());
                                                            Log.e("Orderbook", "qualitylist "+qual_list+"Quantity List"+quantity_list+z);
                                                        quan_qual[z].setVisibility(View.VISIBLE);
                                                        remQuantity[z].setText(po.getValues().get(z).getRemQuantity());
                                                        }

                                                    }


                                                      Log.e("Orderbook", "quantity list"+quantity_list);
                                                      Log.e("Orderbook", "list1"+list1);
                                                      Log.e("Orderbook", "po list"+poid);
                                                      Log.e("Orderbook", "quality list"+qual_list);
                                                  }

                                                  @Override
                                                  public void onNothingSelected(AdapterView<?> adapterView) {

                                                  }
                                              });
                                           }


                                           @Override
                                           public void onFailure(Call<Result> call, Throwable t) {
                                               progressDialog.cancel();
                                               Toast.makeText(OrderBook.this,t.getMessage(),Toast.LENGTH_LONG).show();

                                           }
                                       });
                                   }



                                }}}

                                @Override
                                public void onFailure(Call<POget> cal, Throwable t) {
                                    progressDialog.cancel();
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
                progressDialog.cancel();
                if(t.getMessage().contains("timout"))
                {
                    new android.support.v7.app.AlertDialog.Builder(OrderBook.this)
                            .setTitle("Some Error Occurred")
                            .setMessage("Try After Sometime")
                            .setCancelable(false)
                            .setPositiveButton("ok", null).show();
                }

            }
        });
        Log.e("UserInfo","user:"+user_);
        myCalendar = Calendar.getInstance();
        requierddate=(EditText)findViewById(R.id.Date_required_orderbook);
       // quantity=(EditText)findViewById(R.id.Quantity_orderbook);
        customersite=(Spinner)findViewById(R.id.Location_orderbook);
        //quality=(EditText)findViewById(R.id.Quality_orderbook);
        PO=(Spinner)findViewById(R.id.PO_orderbook);
        linearLayout=(LinearLayout)findViewById(R.id.place_order);


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
                requierddate.setError(null);
                requierddate.requestFocus();
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
               /* InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);*/
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
      for(int a=0;a<size;a++){
       if(quantity1[a].getText().toString().isEmpty()){
            quantity1[a].setError("Required Field");
            quantity1[a].requestFocus();
            progressDialog.cancel();
            return;
        }}

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
      /*  long test= Long.parseLong(quantity.getText().toString());
        if(test>quan)
        {
            quantity.setError("Quantity Should Not Exceed "+quan+" cubic meter");
            quantity.requestFocus();
            progressDialog.cancel();
            return;
        }*/
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

        for(int s=0;s<size;s++)
        {quantity_list.add(quantity1[s].getText().toString());
            long test= Long.parseLong(quantity1[s].getText().toString());
            if(test>Long.parseLong(remQuantity[s].getText().toString()))
            {
                Log.e("OrderBook", "RemQuantity "+remQuantity[s].getText().toString() );
                quantity1[s].setError("Quantity Should Not Exceed "+remQuantity[s].getText().toString()+" cubic meter");
                quantity1[s].requestFocus();
                progressDialog.cancel();
                return;
            }
        }
        sendOrder(name,id);
    }

    private void sendOrder(String name,String id)
    {
        Log.e("TAG","suppid"+supp_id);
        Log.e("TAG", "name "+name+id);
        RetrofitInterface retrofitInterface =retrofit.create(RetrofitInterface.class);
        Map<String,String> map=new HashMap<>();
        map.put("requiredDate", String.valueOf(milli_req_date));
        /*map.put("quantity",quantity);
        map.put("quality",quality);*/
        map.put("requestedBy",name);
        map.put("supplierId",supp_id);
        map.put("customerSite",cust_address);
        if(company_name==null)
        {
            Log.e("Company test", "individual" );
            map.put("companyName","Individual");
        }
        else{
            map.put("companyName",company_name);
        }
        if(po_id.equals("no value"))
        {

        }else{
        map.put("POId",po_id);}
        Log.e("OrderBook", "quantity list"+quantity_list);
        Call<ResponseBody> call=retrofitInterface.submit_order(token,map,qual_list,quantity_list);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.cancel();

                try {
                    Log.e("TAG", "response 33: "+new Gson().toJson(response.body().string()));

                        progressDialog.cancel();
                        Snackbar snackbar = Snackbar
                                .make(linearLayout, "Order Placed Successfully", Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.RED);
                        snackbar.show();
                        DirectingClass directingClass = new DirectingClass(getApplicationContext(), OrderBook.this);
                        directingClass.performLogin();

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(OrderBook.this,t.getMessage(),Toast.LENGTH_SHORT).show();
                progressDialog.cancel();
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
