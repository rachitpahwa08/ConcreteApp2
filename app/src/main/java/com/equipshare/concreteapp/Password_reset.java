package com.equipshare.concreteapp;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Password_reset extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_reset);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Button button=(Button)findViewById(R.id.pass_reset);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Password_reset.this)
                        .setTitle("Reset Password")
                        .setMessage("This Feature is Currently Unavailable")
                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent=new Intent(Password_reset.this,LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).show();
            }
        });
    }
}
