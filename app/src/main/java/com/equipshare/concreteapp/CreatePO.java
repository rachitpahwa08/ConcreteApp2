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
import com.equipshare.concreteapp.utils.SessionManagement;
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
    private EditText validtill,customersite,supplier;

    String cust_id,supp_id,token,site;
    LinearLayout linearLayout;
    long price_string=-1;
    SessionManagement session;
    long milli_valdate;
    Calendar myCalendar;
    String povalid="val";
    String supp_name;
    Quote quote;
    User_ user;
    long price1;
    ArrayList<String> qlist;
    ArrayList<String> price_list;
    ArrayList<String> quantity_list;
    ProgressDialog progressDialog;
    EditText[] quality=new EditText[5];
    EditText[] quantity=new EditText[5];
    EditText[] priceview=new EditText[5];
    LinearLayout[] linearLayouts=new LinearLayout[5];
    private static Retrofit.Builder builder=new Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());
    public static Retrofit retrofit=builder.build();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session = new SessionManagement(CreatePO.this);
        session.checkLogin(CreatePO.this);
        HashMap<String, String> user = session.getUserDetails();
        Intent i=getIntent();
        quote=i.getParcelableExtra("Quote");
        setContentView(R.layout.activity_create_po);
        site=i.getStringExtra("sitename");
        token=user.get(SessionManagement.KEY_TOKEN);
        supp_name=i.getStringExtra("name");
        supp_id=i.getStringExtra("suppid");
        price_list=i.getStringArrayListExtra("price");
        price1=i.getLongExtra("totalprice",0);
        myCalendar = Calendar.getInstance();
        validtill=(EditText)findViewById(R.id.valid_date);
        customersite=(EditText) findViewById(R.id.customersite);
        quality[0]=(EditText)findViewById(R.id.Quality_po1);
        quality[1]=(EditText)findViewById(R.id.Quality_po2);
        quality[2]=(EditText)findViewById(R.id.Quality_po3);
        quality[3]=(EditText)findViewById(R.id.Quality_po4);
        quality[4]=(EditText)findViewById(R.id.Quality_po5);
        quantity[0]=(EditText)findViewById(R.id.Quantity_po1);
        quantity[1]=(EditText)findViewById(R.id.Quantity_po2);
        quantity[2]=(EditText)findViewById(R.id.Quantity_po3);
        quantity[3]=(EditText)findViewById(R.id.Quantity_po4);
        quantity[4]=(EditText)findViewById(R.id.Quantity_po5);
        priceview[0]=(EditText)findViewById(R.id.price_po1);
        priceview[1]=(EditText)findViewById(R.id.price_po2);
        priceview[2]=(EditText)findViewById(R.id.price_po3);
        priceview[3]=(EditText)findViewById(R.id.price_po4);
        priceview[4]=(EditText)findViewById(R.id.price_po5);
        linearLayouts[0]=(LinearLayout)findViewById(R.id.po_quan_qua11);
        linearLayouts[1]=(LinearLayout)findViewById(R.id.po_quan_qua12);
        linearLayouts[2]=(LinearLayout)findViewById(R.id.po_quan_qua13);
        linearLayouts[3]=(LinearLayout)findViewById(R.id.po_quan_qua14);
        linearLayouts[4]=(LinearLayout)findViewById(R.id.po_quan_qua15);
        supplier=(EditText)findViewById(R.id.supplier);
        Button submit=(Button)findViewById(R.id.submit_createPO);
        linearLayout=(LinearLayout)findViewById(R.id.createpo);

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
        qlist = new ArrayList<String>();
        final List<String> valdate_list=new ArrayList<>();
         quantity_list=new ArrayList<>();
        Log.e("CreatePO", "Site name "+site);
        customersite.setText(site);
       qlist= (ArrayList<String>) quote.getQuality();
       quantity_list= (ArrayList<String>) quote.getQuantity();
        for(int k=0;k<qlist.size();k++)
        {
            quality[k].setText(qlist.get(k));
            quantity[k].setText(quantity_list.get(k));
            priceview[k].setText(price_list.get(k));

        }
        for(int z=qlist.size();z<5;z++)
        {
            linearLayouts[z].setVisibility(View.GONE);
        }
       cust_id=quote.getCustomerSite();
       povalid=i.getStringExtra("valdate");
        Date date4 = new Date(Long.parseLong(povalid));
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM,yyyy", Locale.ENGLISH);
        Log.e("TAG","valdate"+povalid+cust_id);
        List<String> list1 = new ArrayList<String>();
        ArrayAdapter<String> adapter1;
        Log.e("CreatePO","qual and quan list"+qlist+quantity_list+"Supplier name"+supp_name);
         supplier.setText(supp_name);
         price_string=price1;
        // quality1.setText(quote.getQuality());
                Log.e("TAG","suppid"+supp_id);
                Log.e("TAG","price"+price_string);
               Log.e("TAG","User"+quote.getRequestedBy()+quote.getRequestedById());

          //      quantity.setText(quote.getQuantity());

                if(!povalid.equals("val"))
                {validtill.setText(formatter.format(date4));}



        Log.e("TAG","suppid"+supp_id);
    }
    private void startPo()
    {

        if(validtill.getText().toString().isEmpty()){
            validtill.setError("Required Field");
            validtill.requestFocus();
            return;
        }


        createPO();
    }

    private void createPO()
    {
        RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);
        Map<String,String> map=new HashMap<>();

        String s=quote.getRequestedByCompany();
        map.put("validTill",povalid);
       // map.put("quantity",);
        //map.put("quality",quality);
        //map.put("price", String.valueOf(price_string));
        map.put("customerSite",cust_id);
        map.put("requestedBy",quote.getRequestedBy());
        map.put("requestedById",quote.getRequestedById());
        map.put("supplierId",supp_id);
        if(s==null)
        {map.put("companyName","Individual");}
        else {
            map.put("companyName",quote.getRequestedByCompany());
        }
        Call<ResponseBody> call=retrofitInterface.create_po(token,map,qlist,quantity_list,price_list);
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
