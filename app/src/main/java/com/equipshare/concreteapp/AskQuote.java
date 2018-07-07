package com.equipshare.concreteapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import com.equipshare.concreteapp.model.Result;
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

/**
 * Created by Jarvis on 05-01-2018.
 */

public class AskQuote extends Fragment {

    private EditText validtill,quantity,price;
    Spinner quality,customersite,supplier;
    String cust_id,supp_id;
    LinearLayout linearLayout;
    int price_string=-1;
    long milli_valdate;
    Calendar myCalendar;
    String povalid="val";
    User_ user;
    Result resl;
    private static Retrofit.Builder builder=new Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());
    public static Retrofit retrofit=builder.build();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.ask_quote,container,false);
        user=((DashBoard)getActivity()).u;
        resl=((DashBoard)getActivity()).r;
        myCalendar = Calendar.getInstance();
        validtill=(EditText)view.findViewById(R.id.valid_date);
        quantity=(EditText)view.findViewById(R.id.quantity);
        customersite=(Spinner) view.findViewById(R.id.customer_site);
        price=(EditText)view.findViewById(R.id.price_PO);

        quality=(Spinner)view.findViewById(R.id.quality_spinner);
        supplier=(Spinner)view.findViewById(R.id.supplier_spinner);
        Button submit=(Button)view.findViewById(R.id.submit_createPO);
        linearLayout=(LinearLayout)view.findViewById(R.id.createpo);
        price.setText("0");

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager)
                        getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
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

        /*validtill.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });*/

        List<String> list = new ArrayList<String>();
        final List<String> qlist = new ArrayList<String>();
        final List<Integer> price_list = new ArrayList<>();
        final List<String> valdate_list=new ArrayList<>();
        final List<String> quantity_list=new ArrayList<>();
        ArrayAdapter<String> adapter;
        for(int j=0;j<user.getCustomerSite().size();j++)
        {
            list.add(user.getCustomerSite().get(j).getName());
        }
        adapter = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customersite.setAdapter(adapter);
        customersite.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cust_id=user.getCustomerSite().get(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        List<String> list1 = new ArrayList<String>();
        ArrayAdapter<String> adapter1;
        for(int j=0;j<resl.getResults().getQuotes().size();j++)
        {   if(resl.getResults().getQuotes().get(j).getResponses()!=null) {
            for (int k = 0; k < resl.getResults().getQuotes().get(j).getResponses().size(); k++) {
                list1.add("name=" + resl.getResults().getQuotes().get(j).getResponses().get(k).getRmxId() + "price=" + resl.getResults().getQuotes().get(j).getResponses().get(k).getPrice());
                qlist.add(resl.getResults().getQuotes().get(j).getResponses().get(k).getRmxId());
                price_list.add(resl.getResults().getQuotes().get(j).getResponses().get(k).getPrice());
                valdate_list.add(resl.getResults().getQuotes().get(j).getResponses().get(k).getValidTill());
                quantity_list.add(resl.getResults().getQuotes().get(j).getQuantity());
            }
          }
        }
        adapter1 = new ArrayAdapter<String>(view.getContext(),
                android.R.layout.simple_spinner_item, list1);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        supplier.setAdapter(adapter1);
        supplier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            supp_id=qlist.get(i);
                Log.e("TAG","suppid"+supp_id);
                Log.e("TAG","suppid"+price_list.get(i));
             price_string=price_list.get(i);
             povalid=valdate_list.get(i);
            quantity.setText(quantity_list.get(i));
             if(price_string!=-1)
             { price.setText(String.valueOf(price_string));}
            if(!povalid.equals("val"))
            {validtill.setText(povalid);}
            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        Log.e("TAG","suppid"+supp_id);
        return view;

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
        createPO(quantity.getText().toString(),quality.getSelectedItem().toString(),price.getText().toString());
    }

    private void createPO(String quantity, String quality,String price)
    {
        RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);
        Map<String,String> map=new HashMap<>();

        map.put("validTill",povalid);
        map.put("quantity",quantity);
        map.put("quality",quality);
        map.put("price", String.valueOf(price_string));
        map.put("customerSite",cust_id);
        map.put("requestedBy",user.getUser().getName());
        map.put("requestedById",String.valueOf(user.getUser().getUserId()));
        map.put("supplierId",supp_id);

        Call<ResponseBody> call=retrofitInterface.create_po(resl.getToken(),map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Snackbar snackbar = Snackbar
                        .make(linearLayout, "PO Created Successfully", Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.RED);
                snackbar.show();
                Log.e("TAG", "response 33: "+new Gson().toJson(response.body()));
                DirectingClass directingClass=new DirectingClass(getContext(),getActivity());
                directingClass.performLogin();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getActivity(),t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date date=myCalendar.getTime();
        milli_valdate=date.getTime();
        Log.e("TAG", "Time 33: "+milli_valdate);
        validtill.setText(sdf.format(myCalendar.getTime()));

    }
}
