package com.equipshare.concreteapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.Set;

public class ResetPass extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pass);
        onNewIntent(getIntent());
    }

    public ResetPass() {
    }

    protected void onNewIntent(Intent intent) {
        String action = intent.getAction();
        String data = intent.getDataString();
        if (Intent.ACTION_VIEW.equals(action) && data != null) {
            String recipeId = data.substring(data.lastIndexOf("/") + 1);
            Log.e("Url Value", ": "+recipeId );
            Uri contentUri = Uri.parse("https://u7236908.ct.sendgrid.net/wf/"+recipeId);
            Set<String> args = contentUri.getQueryParameterNames();
            Log.e("param", ": "+args );
            String path = contentUri.getPath();
            Log.e("path", ": "+path );
            String limit = contentUri.getQueryParameter("upn=");
            Log.e("limit", ": "+limit );
        }
    }
}
