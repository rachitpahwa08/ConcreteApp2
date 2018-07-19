package com.equipshare.concreteapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.equipshare.concreteapp.model.Quote;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ResponseDetails extends AppCompatActivity {

    TextView[] quality=new TextView[5];
    TextView[] quantity=new TextView[5];
    TextView[] price =new TextView[5];
    Quote quote;
    ArrayList<String> price_list;
    int position;
    String suppname;
    String sitename,suppid,token;
    TextView gendate,valdate,Sitename,Suppname;
    Button po;
    long price1=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response_details);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        quality[0]=(TextView)findViewById(R.id.qual1_response);
        quality[1]=(TextView)findViewById(R.id.qual2_response);
        quality[2]=(TextView)findViewById(R.id.qual3_response);
        quality[3]=(TextView)findViewById(R.id.qual4_response);
        quality[4]=(TextView)findViewById(R.id.qual5_response);
        quantity[0]=(TextView)findViewById(R.id.quan1_response);
        quantity[1]=(TextView)findViewById(R.id.quan2_response);
        quantity[2]=(TextView)findViewById(R.id.quan3_response);
        quantity[3]=(TextView)findViewById(R.id.quan4_response);
        quantity[4]=(TextView)findViewById(R.id.quan5_response);
        price[0]=(TextView)findViewById(R.id.price1_response);
        price[1]=(TextView)findViewById(R.id.price2_response);
        price[2]=(TextView)findViewById(R.id.price3_response);
        price[3]=(TextView)findViewById(R.id.price4_response);
        price[4]=(TextView)findViewById(R.id.price5_response);
        gendate=(TextView)findViewById(R.id.gen_date_responseinfo);
        valdate=(TextView)findViewById(R.id.responseinfo_valdate);
        Sitename=(TextView)findViewById(R.id.sitename_responseinfo);
        Suppname=(TextView)findViewById(R.id.responseinfo_suppname);
        po=(Button)findViewById(R.id.placepo);
        Intent intent=getIntent();
        quote=intent.getParcelableExtra("quote");
        price_list=intent.getStringArrayListExtra("price");
        position=intent.getIntExtra("position",0);
        suppname=intent.getStringExtra("suppname");
        sitename=intent.getStringExtra("sitename");
        suppid=intent.getStringExtra("suppid");
        token=intent.getStringExtra("token");
        Log.e("ResponseDetails", "price val"+price_list );
        for(int k=0;k<quote.getQuality().size();k++)
        {
            quality[k].setText(quote.getQuality().get(k));
            quantity[k].setText(quote.getQuantity().get(k));
            price[k].setText(price_list.get(k));
            price1= price1+Long.parseLong(price_list.get(k));
        }
        for(int z=quote.getQuality().size();z<5;z++)
        {
            quality[z].setVisibility(View.GONE);
            quantity[z].setVisibility(View.GONE);
            price[z].setVisibility(View.GONE);
        }
        Sitename.setText("Site:"+sitename);
        Suppname.setText("Supplier Name:"+suppname);
        gendate.setText("Quote Created On:"+getDate(Long.parseLong(quote.getGenerationDate())));
        valdate.setText("Valid Till:"+getDate(Long.parseLong(quote.getResponses().get(position).getValidTill())));
po.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent intent=new Intent(getApplicationContext(),CreatePO.class);
        intent.putExtra("Quote",quote);
        intent.putExtra("sitename",sitename);
        intent.putExtra("token",token);
        intent.putExtra("name",suppname);
        intent.putExtra("suppid",suppid);
        intent.putExtra("price",price_list);
        intent.putExtra("valdate",quote.getResponses().get(position).getValidTill());
        intent.putExtra("totalprice",price1);
        startActivity(intent);
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
    String getDate(long milli)
    {
        Date date = new Date(milli);
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM,yyyy", Locale.ENGLISH);
        return  formatter.format(date);
    }
}
