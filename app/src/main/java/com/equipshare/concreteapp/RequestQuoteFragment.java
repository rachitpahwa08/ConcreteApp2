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
import com.equipshare.concreteapp.utils.SessionManagement;
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

    private EditText validtill;
    EditText[] quantity1=new EditText[6];
    private Spinner customersite;
    private String cust_id;
     int check=1;
    String name,company;
    Button r1,r2,r3,r4,r5;
    LinearLayout linearLayout;
    LinearLayout[] quan_qual=new LinearLayout[6];
    SessionManagement session;
    Result res1;
    String token;
    User_ user;
    Calendar myCalendar;
    Spinner[] quality=new Spinner[6];
    long milli_valdate;
    LinearLayout request;
    ProgressDialog progressDialog;
    Button add;
    List<String> qual;
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
        quantity1[0]=(EditText)view.findViewById(R.id.quote_quantity1);
        quantity1[1]=(EditText)view.findViewById(R.id.quote_quantity2);
        quantity1[2]=(EditText)view.findViewById(R.id.quote_quantity3);
        quantity1[3]=(EditText)view.findViewById(R.id.quote_quantity4);
        quantity1[4]=(EditText)view.findViewById(R.id.quote_quantity5);
        quantity1[5]=(EditText)view.findViewById(R.id.quote_quantity6);
        customersite=(Spinner)view.findViewById(R.id.customer_site_spinner);
        quality[0]=(Spinner)view.findViewById(R.id.quote_qualtiy_spinner1);
        quality[1]=(Spinner)view.findViewById(R.id.quote_qualtiy_spinner2);
        quality[2]=(Spinner)view.findViewById(R.id.quote_qualtiy_spinner3);
        quality[3]=(Spinner)view.findViewById(R.id.quote_qualtiy_spinner4);
        quality[4]=(Spinner)view.findViewById(R.id.quote_qualtiy_spinner5);
        quality[5]=(Spinner)view.findViewById(R.id.quote_qualtiy_spinner6);
        Button submit=(Button)view.findViewById(R.id.submit_quote);
        quan_qual[0]=(LinearLayout)view.findViewById(R.id.quan_qual1);
        quan_qual[1]=(LinearLayout)view.findViewById(R.id.quan_qual2);
        quan_qual[2]=(LinearLayout)view.findViewById(R.id.quan_qual3);
        quan_qual[3]=(LinearLayout)view.findViewById(R.id.quan_qual4);
        quan_qual[4]=(LinearLayout)view.findViewById(R.id.quan_qual5);
        quan_qual[5]=(LinearLayout)view.findViewById(R.id.quan_qual6);
        r1=(Button)view.findViewById(R.id.delete_button_1);
        r2=(Button)view.findViewById(R.id.delete_button_2);
        r3=(Button)view.findViewById(R.id.delete_button_3);
        r4=(Button)view.findViewById(R.id.delete_button_4);
        r5=(Button)view.findViewById(R.id.delete_button_5);
        qual=new ArrayList<>();
        linearLayout=(LinearLayout)view.findViewById(R.id.request_quote);
        add=(Button)view.findViewById(R.id.add_button_d);
       for(int j=1;j<6;j++)
        {
            quan_qual[j].setVisibility(View.GONE);

        }
        add.setVisibility(View.GONE);
add.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {

        Log.e("check", "value: "+check);
       switch (check)
       {
           case 1:quan_qual[1].setVisibility(View.VISIBLE);
                  check=2;
                  break;
           case 2:  quan_qual[2].setVisibility(View.VISIBLE);
                    check=3;
                    break;
           case 3:  quan_qual[3].setVisibility(View.VISIBLE);
               check=4;
               break;
           case 4:  quan_qual[4].setVisibility(View.VISIBLE);
               check=5;
               break;
           case 5:  quan_qual[5].setVisibility(View.VISIBLE);
                   check=6;
                    break;
       }
      /*  for(int j=0;j<6;j++)
        {
            qual.add(quantity1[j].getText().toString());

        }
        Log.e("QuanCheck", "value :"+qual );
        for(int j=0;j<6;j++)
        {
            if(qual.get(j).isEmpty())
            {
                qual.remove(j);
            }
        }
        Log.e("QuanCheck2", "value :"+qual );
    */}
});
       r1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Log.e("QuanCheck2", "value :"+check);
               if(check==2)
               {
                   quan_qual[1].setVisibility(View.GONE);
                   quantity1[1].setError(null);
                   quantity1[1].setText("");
                   check=1;
               }
               else {
                   Log.e("QuanCheck2", "else :"+check);
                   quantity1[1].setError("Please Remove Last Quantity and Quality");
                   quantity1[1].requestFocus();
                   return;
               }
           }
       });
        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("QuanCheck2", "value :"+check);
                if(check==3)
                {
                    quan_qual[2].setVisibility(View.GONE);
                    quantity1[2].setError(null);
                    quantity1[2].setText("");
                    check=2;
                }
                else {
                    quantity1[2].setError("Please Remove Last Quantity and Quality");
                    quantity1[2].requestFocus();
                    return;
                }
            }
        });
        r3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("QuanCheck2", "value :"+check);
                if(check==4)
                {
                    quan_qual[3].setVisibility(View.GONE);
                    quantity1[3].setError(null);
                    quantity1[3].setText("");
                    check=3;
                }
                else {
                    quantity1[3].setError("Please Remove Last Quantity and Quality");
                    quantity1[3].requestFocus();
                    return;
                }
            }
        });
        r4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("QuanCheck2", "value :"+check);
                if(check==5)
                {
                    quan_qual[4].setVisibility(View.GONE);
                    quantity1[4].setError(null);
                    quantity1[4].setText("");
                    check=4;
                }
                else {
                    quantity1[4].setError("Please Remove Last Quantity and Quality");
                    quantity1[4].requestFocus();
                    return;
                }
            }
        });
        r5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("QuanCheck2", "value :"+check);
               if(check==6) {
                   quan_qual[5].setVisibility(View.GONE);
                   quantity1[5].setError(null);
                   quantity1[5].setText("");
                   check = 5;
               }
            }
        });
        request=(LinearLayout)view.findViewById(R.id.request_quote1);
        session = new SessionManagement(getContext());
        HashMap<String, String> user1 = session.getUserDetails();
         token=user1.get(SessionManagement.KEY_TOKEN);
        res1=((RequestQuote)getActivity()).res;
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Log.e("TAG", "Time instance: "+myCalendar.getTime()+Locale.getDefault());

        /*add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LayoutInflater layoutInflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View rowView = layoutInflater.inflate(R.layout.add_edittext, null);
                request.addView(rowView, request.getChildCount() - 1);
            }
        });*/

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


        Call<User_> call=retrofitInterface.show_profile(token);
        call.enqueue(new Callback<User_>() {
            @Override
            public void onResponse(Call<User_> call, Response<User_> response) {
                user =response.body();
                Log.e("Userinfo Response ", "response 33: " + new Gson().toJson(response.body()));
                name=user.getUser().getName();
                company=user.getUser().getCompany();

                Log.e("company name","value:"+ company);
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
                        cust_id=user.getUser().getCustomerSite().get(i).getAddress();
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
                for(int j=0;j<check;j++)
                {
                    qual.add(quality[j].getSelectedItem().toString());
                }
                Log.e("TAG", "quality" +qual);
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
        if(quantity1[0].getText().toString().isEmpty()){
            quantity1[0].setError("Required Field");
            quantity1[0].requestFocus();
            progressDialog.cancel();
            return;
        }
        if(customersite.getSelectedItem().toString().equals("No Site Added by User "))
        {
            quantity1[0].setError("No Site Selected");
            quantity1[0].requestFocus();
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

        startRequsetQuote(quantity1[0].getText().toString(),quality[0].getSelectedItem().toString());
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
        if(company==null)
        {
            Log.e("Company test", "individual" );
            map.put("company","Individual");
        }
        else{
            map.put("company",company);
        }

        Call<ResponseBody> call=retrofitInterface.quote_request(token,map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Log.e("TAG", "response 33: "+response.body().string());
                    Snackbar snackbar = Snackbar
                                .make(linearLayout, "Quote Requested Successfully", Snackbar.LENGTH_LONG);
                        snackbar.setActionTextColor(Color.RED);
                        snackbar.show();
                        DirectingClass directingClass=new DirectingClass(getContext(),getActivity());
                        directingClass.performLogin();


                } catch (IOException e) {
                    e.printStackTrace();
                }
                progressDialog.cancel();


            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void onDelete(View view) {
        LayoutInflater layoutInflater=(LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View rowView = layoutInflater.inflate(R.layout.add_edittext, null);
        request.removeView((View)rowView.getParent());
    }
}


