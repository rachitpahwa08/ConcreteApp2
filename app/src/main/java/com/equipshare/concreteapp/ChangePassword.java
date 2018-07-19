package com.equipshare.concreteapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Call;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.equipshare.concreteapp.model.Result;
import com.equipshare.concreteapp.network.RetrofitInterface;
import com.equipshare.concreteapp.utils.Constants;
import com.equipshare.concreteapp.utils.DirectingClass;
import com.equipshare.concreteapp.utils.SessionManagement;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangePassword extends AppCompatActivity {
    SessionManagement session;
    ProgressDialog progressDialog;
    String token;
    Gson gson = new GsonBuilder().setLenient().create();
    OkHttpClient client = new OkHttpClient();
    Retrofit.Builder builder=new Retrofit.Builder().baseUrl(Constants.BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create(gson));
    Retrofit retrofit=builder.build();
    RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);
    EditText old_pass,new_pass,confirm_pass;
    LinearLayout linearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        session = new SessionManagement(getApplicationContext());
        HashMap<String, String> user1 = session.getUserDetails();
        token=user1.get(SessionManagement.KEY_TOKEN);
        old_pass=(EditText)findViewById(R.id.oldpassword);
        new_pass=(EditText)findViewById(R.id.newpassword1);
        confirm_pass=(EditText)findViewById(R.id.confirmpassword1);
        Button change=(Button)findViewById(R.id.change_pass_button);
        linearLayout=(LinearLayout)findViewById(R.id.changepassword);
        old_pass.setError(null);
        old_pass.requestFocus();
        confirm_pass.setError(null);
        confirm_pass.requestFocus();
        new_pass.setError(null);
        new_pass.requestFocus();
        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog=new ProgressDialog(ChangePassword.this);
                progressDialog.setMessage("Changing Password");
                progressDialog.setCancelable(false);
                progressDialog.show();
                startChange();
            }
        });
    }

    private void startChange()
    {
        if(old_pass.getText().toString().isEmpty()){
            old_pass.setError("Password is Required");
            old_pass.requestFocus();
            progressDialog.cancel();
            return;
        }
        if(new_pass.getText().toString().isEmpty()){
            new_pass.setError("Password is Required");
            new_pass.requestFocus();
            progressDialog.cancel();
            return;
        }
        if(confirm_pass.getText().toString().equals(old_pass.getText().toString())){
            confirm_pass.setError("Old Password and New Password Can't Be Same");
            confirm_pass.requestFocus();
            progressDialog.cancel();
            return;
        }
        if(!confirm_pass.getText().toString().equals(new_pass.getText().toString())){
            confirm_pass.setError("Password does not match");
            confirm_pass.requestFocus();
            progressDialog.cancel();
            return;
        }
        if(new_pass.length()<8){
            new_pass.setError("Minimum length of Password should be 8 characters");
            new_pass.requestFocus();
            progressDialog.cancel();
            return;
        }
    changePass();
    }

    private void changePass() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
        RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);
        Map<String,String> map=new HashMap<>();
        map.put("oldpass",old_pass.getText().toString());
        map.put("newpass",new_pass.getText().toString());
        map.put("newpass2",confirm_pass.getText().toString());
        retrofit2.Call<Result> call=retrofitInterface.change_pass(token,map);
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(retrofit2.Call<Result> call, Response<Result> response) {
                Log.e("Password Change", "Value"+new Gson().toJson(response.body()));
                progressDialog.cancel();
                Result result=response.body();
                if(result.getMsg().equals("password updates successfully"))
                {
                    new AlertDialog.Builder(ChangePassword.this).setTitle("Password Changed Successfully")
                            .setMessage("Please Login to Continue")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    session.logoutUser();
                                }
                            }).show();

                }
                else if(result.getMsg().equals("old password is not correct"))
                {
                    Snackbar snackbar = Snackbar
                            .make(linearLayout, "Old Password Is Not Correct", Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.RED);
                    snackbar.show();
                    old_pass.setError("Old Password Is Not Correct");
                    old_pass.requestFocus();
                }
                else {
                    Snackbar snackbar = Snackbar
                            .make(linearLayout, "Can't Change Password,Please Try Again After Sometime", Snackbar.LENGTH_LONG);
                    snackbar.setActionTextColor(Color.RED);
                    snackbar.show();
                    DirectingClass directingClass=new DirectingClass(ChangePassword.this,ChangePassword.this);
                    directingClass.performLogin();
                }
            }

            @Override
            public void onFailure(retrofit2.Call<Result> call, Throwable t) {
                Log.e("TAG", "response 33: " + t.getMessage());
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
}
