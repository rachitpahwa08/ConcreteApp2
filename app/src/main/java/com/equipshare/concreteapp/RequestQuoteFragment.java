package com.equipshare.concreteapp;

import android.app.DatePickerDialog;

import android.app.ProgressDialog;

import android.content.Context;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.io.IOException;

import java.text.SimpleDateFormat;

import com.equipshare.concreteapp.model.Result;
import com.equipshare.concreteapp.model.User_;
import com.equipshare.concreteapp.network.RetrofitInterface;
import com.equipshare.concreteapp.utils.Constants;

import com.equipshare.concreteapp.utils.DirectingClass;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Jarvis on 21-01-2018.
 */

public class RequestQuoteFragment extends android.support.v4.app.Fragment {

    private EditText validtill,quantity;
    private Spinner customersite;
    private String cust_id;

    String name;
    LinearLayout linearLayout;
    Result res1;
    User_ user;
    Calendar myCalendar;
    Spinner quality;
    long milli_valdate;
    LinearLayout request;
    ProgressDialog progressDialog;
    Gson gson = new GsonBuilder().setLenient().create();
    OkHttpClient client = new OkHttpClient();
    Retrofit.Builder builder=new Retrofit.Builder().baseUrl(Constants.BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create(gson));
    Retrofit retrofit=builder.build();
    RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
      final View view=inflater.inflate(R.layout.request_quote_fragment,null);
        myCalendar = Calendar.getInstance();
        validtill=(EditText)view.findViewById(R.id.quote_valid_date);
        quantity=(EditText)view.findViewById(R.id.quote_quantity1);
        customersite=(Spinner)view.findViewById(R.id.customer_site_spinner);
        quality=(Spinner)view.findViewById(R.id.quote_qualtiy_spinner1);
        Button submit=(Button)view.findViewById(R.id.submit_quote);
        linearLayout=(LinearLayout)view.findViewById(R.id.request_quote);

        request=(LinearLayout)view.findViewById(R.id.request_quote1);

        res1=((RequestQuote)getActivity()).res;
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e("TAG", "Time instance: "+myCalendar.getTime()+Locale.getDefault());
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

        validtill.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        Call<User_> call=retrofitInterface.show_profile(res1.getToken());
        call.enqueue(new Callback<User_>() {
            @Override
            public void onResponse(Call<User_> call, Response<User_> response) {
                user =response.body();
                Log.e("Userinfo Response ", "response 33: " + new Gson().toJson(response.body()));
                name=user.getUser().getName();
                if(user.getUser().getCustomerSite().isEmpty())
                {
                    List<String> list = new ArrayList<String>();
                    ArrayAdapter<String> adapter;
                    list.add("No Site Added by User ");
                    adapter = new ArrayAdapter<String>(view.getContext(),
                            android.R.layout.simple_spinner_item, list);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    customersite.setAdapter(adapter);
                    progressDialog.cancel();
                }
                else{
                List<String> list = new ArrayList<String>();
                ArrayAdapter<String> adapter;
                for(int j=0;j<user.getUser().getCustomerSite().size();j++)
                {
                    list.add(user.getUser().getCustomerSite().get(j).getName());
                }

                adapter = new ArrayAdapter<String>(view.getContext(),
                        android.R.layout.simple_spinner_item, list);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                customersite.setAdapter(adapter);
                progressDialog.cancel();
                customersite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        cust_id=user.getUser().getCustomerSite().get(i).getId();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }}

            @Override
            public void onFailure(Call<User_> call, Throwable t) {
                Log.e("TAG", "response 33: " + t.getMessage());
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                progressDialog=new ProgressDialog(getContext());
                progressDialog.setMessage("Processing");
                progressDialog.setCancelable(false);
                progressDialog.show();
                startquote();
            }
        });

      return view;

    }
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        Date date=myCalendar.getTime();
         milli_valdate=date.getTime();
        Log.e("TAG", "Time 33: "+milli_valdate+Locale.getDefault());
        validtill.setText(sdf.format(myCalendar.getTime()));

    }

    private void startquote() {

        if(validtill.getText().toString().isEmpty()){
            validtill.setError("Required Field");
            validtill.requestFocus();
            progressDialog.cancel();
            return;
        }
        if(quantity.getText().toString().isEmpty()){
            quantity.setError("Required Field");
            quantity.requestFocus();
            progressDialog.cancel();
            return;
        }
        if(customersite.getSelectedItem().toString().equals("No Site Added by User "))
        {
            quantity.setError("No Site Selected");
            quantity.requestFocus();
            progressDialog.cancel();
            return;
        }
        if((milli_valdate<System.currentTimeMillis()))
        {
            validtill.setError("Date Entered Should Not Be Less From Current Date");
            validtill.requestFocus();
            progressDialog.cancel();
            return;
        }

        startRequsetQuote(quantity.getText().toString(),quality.getSelectedItem().toString());
    }

    private void startRequsetQuote (String quantity, String quality)
    {

        Log.e("TAG", "response 33: "+cust_id+name+milli_valdate);
        RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);
        Map<String,String> map=new HashMap<>();
        map.put("quality",quality);
        map.put("quantity",quantity);
        map.put("customerSite",cust_id);
        map.put("requiredDate", String.valueOf(milli_valdate));
        map.put("requestedBy",name);
        map.put("name",name);

        Call<ResponseBody> call=retrofitInterface.quote_request(res1.getToken(),map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("TAG", "response 33: "+response.body().string());

                } catch (IOException e) {
                    e.printStackTrace();
                }
                progressDialog.cancel();

                Snackbar snackbar = Snackbar
                        .make(linearLayout, "Quote Requested Successfully", Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.RED);
                snackbar.show();
                DirectingClass directingClass=new DirectingClass(getContext(),getActivity());
                directingClass.performLogin();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private View.OnClickListener onClick() {
        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                request.addView(createNewTextView(quantity.getText().toString()));
            }
        };
    }
    private TextView createNewTextView(String text) {
        final ViewGroup.LayoutParams lparams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        final TextView textView = new TextView(getContext());
        textView.setLayoutParams(lparams);
        textView.setText("New text: " + text);
        return textView;
    }
}

