package com.equipshare.concreteapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class QR_result extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_result);
        Intent i=getIntent();
        String result=i.getStringExtra("result");
        TextView textView=(TextView)findViewById(R.id.results);
        textView.setText(result);
    }
}
