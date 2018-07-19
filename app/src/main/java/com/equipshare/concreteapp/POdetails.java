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
    TextView[] quality=new TextView[5];
    TextView[] quantity=new TextView[5];
    TextView[] price =new TextView[5];
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
        TextView gendate,valid,requestedby,status,site;
        gendate=(TextView)findViewById(R.id.po_gen_date);
        valid=(TextView)findViewById(R.id.po_valid);
       /* quantity=(TextView)findViewById(R.id.po_quantity);
        quality=(TextView)findViewById(R.id.po_quality);*/
        requestedby=(TextView)findViewById(R.id.po_request);
        status=(TextView)findViewById(R.id.po_status);
        site=(TextView)findViewById(R.id.po_site);
        quality[0]=(TextView)findViewById(R.id.qual1_podetails);
        quality[1]=(TextView)findViewById(R.id.qual2_podetails);
        quality[2]=(TextView)findViewById(R.id.qual3_podetails);
        quality[3]=(TextView)findViewById(R.id.qual4_podetails);
        quality[4]=(TextView)findViewById(R.id.qual5_podetails);
        quantity[0]=(TextView)findViewById(R.id.quan1_podetails);
        quantity[1]=(TextView)findViewById(R.id.quan2_podetails);
        quantity[2]=(TextView)findViewById(R.id.quan3_podetails);
        quantity[3]=(TextView)findViewById(R.id.quan4_podetails);
        quantity[4]=(TextView)findViewById(R.id.quan5_podetails);
        price[0]=(TextView)findViewById(R.id.price1_podetails);
        price[1]=(TextView)findViewById(R.id.price2_podetails);
        price[2]=(TextView)findViewById(R.id.price3_podetails);
        price[3]=(TextView)findViewById(R.id.price4_podetails);
        price[4]=(TextView)findViewById(R.id.price5_podetails);
        linearLayout=(LinearLayout)findViewById(R.id.podetails);
        Button button=(Button)findViewById(R.id.deletepobutton);
        for(int k=0;k<p.getValues().size();k++)
        {
            quality[k].setText(p.getValues().get(k).getQuality());
            quantity[k].setText(p.getValues().get(k).getQuantity());
            price[k].setText(p.getValues().get(k).getPrice());
        }
        for(int z=p.getValues().size();z<5;z++)
        {
            quality[z].setVisibility(View.GONE);
            quantity[z].setVisibility(View.GONE);
            price[z].setVisibility(View.GONE);
        }
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
                                map.put("id", String.valueOf(p.getPOId()));
                                Call<ResponseBody> call=retrofitInterface.delete_po(token,map);
                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                        Snackbar snackbar = Snackbar
                                                .make(linearLayout, "PO Deleted Successfully", Snackbar.LENGTH_LONG);
                                        snackbar.setActionTextColor(Color.RED);
                                        snackbar.show();
                                        Log.e("TAG", "response 33: "+response.body()+"id="+p.getPOId());
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
            if(p.getValidTill().equals("07 September,2019"))
            {
                valid.setText("PO Vaild Till:"+p.getValidTill());
            }
          else{  long valid_milli = Long.parseLong(p.getValidTill());
            Date date1 = new Date(valid_milli);
            valid.setText("PO valid till:" + formatter.format(date1));
        }}}
        else {
           valid.setText("PO valid till:"+p.getValidTill());
          }

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
