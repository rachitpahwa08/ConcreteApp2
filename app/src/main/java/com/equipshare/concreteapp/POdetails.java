package com.equipshare.concreteapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.equipshare.concreteapp.model.PO;
import com.equipshare.concreteapp.network.RetrofitInterface;
import com.equipshare.concreteapp.utils.Constants;
import com.equipshare.concreteapp.utils.DirectingClass;
import com.equipshare.concreteapp.utils.SessionManagement;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class POdetails extends AppCompatActivity {
    SessionManagement session;
    Gson gson = new GsonBuilder().setLenient().create();
    Retrofit.Builder builder=new Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create(gson));
    Retrofit retrofit=builder.build();
    PO p;
    String token;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_podetails);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session = new SessionManagement(POdetails.this);
        HashMap<String, String> user1 = session.getUserDetails();
       token=user1.get(SessionManagement.KEY_TOKEN);
        Intent i=getIntent();
        String customerSite;
        p=i.getParcelableExtra("PO");
        customerSite=i.getStringExtra("customersite");
        TextView gendate,valid,quantity,quality,price,requestedby,status,site;
        gendate=(TextView)findViewById(R.id.po_gen_date);
        valid=(TextView)findViewById(R.id.po_valid);
        quantity=(TextView)findViewById(R.id.po_quantity);
        quality=(TextView)findViewById(R.id.po_quality);
        requestedby=(TextView)findViewById(R.id.po_request);
        status=(TextView)findViewById(R.id.po_status);
        price=(TextView)findViewById(R.id.po_price);
        site=(TextView)findViewById(R.id.po_site);
        linearLayout=(LinearLayout)findViewById(R.id.podetails);
        Button button=(Button)findViewById(R.id.deletepobutton);
        if(p.getConfirmedBySupplier().equals("true"))
        {
            View view=findViewById(R.id.deletepobutton);
            view.setVisibility(View.GONE);
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                new AlertDialog.Builder(POdetails.this)
                        .setTitle("Delete PO")
                        .setMessage("Are you sure you want to Delete PO?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);
                                Map<String,String> map=new HashMap<>();
                                map.put("id",p.getId());
                                Call<ResponseBody> call=retrofitInterface.delete_po(token,map);
                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        Snackbar snackbar = Snackbar
                                                .make(linearLayout, "PO Deleted Successfully", Snackbar.LENGTH_LONG);
                                        snackbar.setActionTextColor(Color.RED);
                                        snackbar.show();
                                        Log.e("TAG", "response 33: "+response.body()+"id="+p.getId());
                                    DirectingClass directingClass=new DirectingClass(POdetails.this,POdetails.this);
                                    directingClass.performLogin();
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                                        Toast.makeText(view.getContext(),t.getMessage(),Toast.LENGTH_LONG).show();
                                    }
                                });

                            }

                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        });
        long milliseconds=Long.parseLong(p.getGenerationDate());
        Date date = new Date(milliseconds);
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM,yyyy", Locale.ENGLISH);
        gendate.setText("PO created on:"+formatter.format(date));
       if(!p.getValidTill().contains("/"))
        { if(p.getValidTill().contains("-"))
        {
            valid.setText("PO valid till:"+p.getValidTill());
        }
       else {
            long valid_milli = Long.parseLong(p.getValidTill());
            Date date1 = new Date(valid_milli);
            valid.setText("PO valid till:" + formatter.format(date1));
        }}
        else {
           valid.setText("PO valid till:"+p.getValidTill());
          }
        quality.setText("Quality:"+p.getQuality());
        quantity.setText("Quantity:"+p.getQuantity());
        requestedby.setText("PO requested by:"+p.getRequestedBy());
        site.setText("Customer Site:"+p.getCustomerSite());
        if((p.getConfirmedBySupplier().equals("false"))&&(p.getDeletedByContractor().equals("false")))
        {
            status.setText("PO status:In Progress");
        }
        else if(p.getDeletedByContractor().equals("true"))
        {
            status.setText("PO status:PO deleted");
            button.setVisibility(View.GONE);
        }
        else
            status.setText("PO status:Confirmed");


        price.setText("Price:\u20B9"+p.getPrice());
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
