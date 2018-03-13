package com.equipshare.concreteapp;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.Nullable;
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
import android.widget.TextView;
import android.widget.Toast;

import com.equipshare.concreteapp.model.Quote;
import com.equipshare.concreteapp.model.User_;
import com.equipshare.concreteapp.network.RetrofitInterface;
import com.equipshare.concreteapp.utils.Constants;
import com.equipshare.concreteapp.utils.DirectingClass;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CreatePO extends AppCompatActivity {
    private EditText validtill,quantity,price,customersite,supplier;

    String cust_id,supp_id,token,site;
    LinearLayout linearLayout;
    long price_string=-1;

    long milli_valdate;
    Calendar myCalendar;
    String povalid="val";
    String supp_name;
    Quote quote;
    User_ user;
    ProgressDialog progressDialog;
    private static Retrofit.Builder builder=new Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());
    public static Retrofit retrofit=builder.build();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i=getIntent();
        quote=i.getParcelableExtra("Quote");
        setContentView(R.layout.activity_create_po);
        site=i.getStringExtra("sitename");
        token=i.getStringExtra("token");
        supp_name=i.getStringExtra("name");
        supp_id=i.getStringExtra("suppid");
        price_string=i.getLongExtra("price",0);
        myCalendar = Calendar.getInstance();
        validtill=(EditText)findViewById(R.id.valid_date);
        quantity=(EditText)findViewById(R.id.quantity);
        customersite=(EditText) findViewById(R.id.customersite);
        price=(EditText)findViewById(R.id.price_PO);

        EditText quality1=(EditText)findViewById(R.id.quality_po);
        supplier=(EditText)findViewById(R.id.supplier);
        Button submit=(Button)findViewById(R.id.submit_createPO);
        linearLayout=(LinearLayout)findViewById(R.id.createpo);
        price.setText("0");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                progressDialog=new ProgressDialog(CreatePO.this);
                progressDialog.setMessage("Submitting");
                progressDialog.setCancelable(false);
                progressDialog.show();
                startPo();
            }
        });
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



        List<String> list = new ArrayList<String>();
        final List<String> qlist = new ArrayList<String>();
        final List<Integer> price_list = new ArrayList<>();
        final List<String> valdate_list=new ArrayList<>();
        final List<String> quantity_list=new ArrayList<>();
        Log.e("CreatePO", "Site name "+site);
        customersite.setText(site);

       cust_id=quote.getCustomerSite();
       povalid=i.getStringExtra("valdate");
        Log.e("TAG","valdate"+povalid+cust_id+quote.getQuality());
        List<String> list1 = new ArrayList<String>();
        ArrayAdapter<String> adapter1;

         supplier.setText(supp_name);
         quality1.setText(quote.getQuality());
                Log.e("TAG","suppid"+supp_id);
                Log.e("TAG","price"+price_string);
               Log.e("TAG","User"+quote.getRequestedBy()+quote.getRequestedById());

                quantity.setText(quote.getQuantity());
                if(price_string!=-1)
                { price.setText(String.valueOf(price_string));}
                if(!povalid.equals("val"))
                {validtill.setText(povalid);}



        Log.e("TAG","suppid"+supp_id);
    }
    private void startPo()
    {

        if(validtill.getText().toString().isEmpty()){
            validtill.setError("Required Field");
            validtill.requestFocus();
            return;
        }
        if(quantity.getText().toString().isEmpty()){
            quantity.setError("Required Field");
            quantity.requestFocus();
            return;
        }

        if(price.getText().toString().isEmpty()){
            price.setError("Required Field");
            customersite.requestFocus();
            return;
        }
        createPO(quantity.getText().toString(),quote.getQuality());
    }

    private void createPO(String quantity, String quality)
    {
        RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);
        Map<String,String> map=new HashMap<>();

        map.put("validTill",povalid);
        map.put("quantity",quantity);
        map.put("quality",quality);
        map.put("price", String.valueOf(price_string));
        map.put("customerSite",cust_id);
        map.put("requestedBy",quote.getRequestedBy());
        map.put("requestedById",quote.getRequestedById());
        map.put("supplierId",supp_id);

        Call<ResponseBody> call=retrofitInterface.create_po(token,map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
               progressDialog.cancel();
                Snackbar snackbar = Snackbar
                        .make(linearLayout, "PO Created Successfully", Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.RED);
                snackbar.show();
                Log.e("TAG", "response 33: "+new Gson().toJson(response.body()));
                DirectingClass directingClass=new DirectingClass(CreatePO.this,CreatePO.this);
                directingClass.performLogin();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.cancel();
                Toast.makeText(CreatePO.this,t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date date=myCalendar.getTime();
        milli_valdate=date.getTime();
        Log.e("TAG", "Time 33: "+milli_valdate);
        validtill.setText(sdf.format(myCalendar.getTime()));

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
