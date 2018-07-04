package com.equipshare.concreteapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.equipshare.concreteapp.model.Result;
import com.equipshare.concreteapp.network.RetrofitInterface;
import com.equipshare.concreteapp.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUp extends AppCompatActivity {
    private EditText name,mobile,email,password,confirm_password,pan,gstin,company;
    Spinner usertype;
    ProgressDialog progressDialog;
    Button register;
    Gson gson = new GsonBuilder().setLenient().create();
    OkHttpClient client = new OkHttpClient();
    Retrofit.Builder builder=new Retrofit.Builder().baseUrl(Constants.BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create(gson));
    Retrofit retrofit=builder.build();
    RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

           name=(EditText)findViewById(R.id.fullName);
           mobile=(EditText)findViewById(R.id.mobile);
           email=(EditText)findViewById(R.id.email);
           password=(EditText)findViewById(R.id.password);
           confirm_password=(EditText)findViewById(R.id.confirmPass);
           pan=(EditText)findViewById(R.id.PAN);
           gstin=(EditText)findViewById(R.id.GSTIN);
           company=(EditText)findViewById(R.id.company);
           usertype=(Spinner)findViewById(R.id.user_type_spinner);

           gstin.setVisibility(View.GONE);
           pan.setVisibility(View.GONE);
           usertype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
               @Override
               public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                   switch (i)
                   {
                       case 0:company.setVisibility(View.GONE);
                              break;
                       case 1:company.setVisibility(View.VISIBLE);
                               break;
                   }
               }

               @Override
               public void onNothingSelected(AdapterView<?> adapterView) {

               }
           });
            register=(Button)findViewById(R.id.SignUp);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog=new ProgressDialog(SignUp.this);
                progressDialog.setMessage("Creating Account");
                progressDialog.setCancelable(false);
                progressDialog.show();
                startRegister();
            }
        });
    }

    private void startRegister() {
        if(name.getText().toString().isEmpty()){
            name.setError("Name is Required");
            name.requestFocus();
            progressDialog.cancel();
            return;
        }
        if(email.getText().toString().isEmpty()){
            email.setError("Email is Required");
            email.requestFocus();
            progressDialog.cancel();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()){
            email.setError("Please enter a valid email or password");
            email.requestFocus();
            progressDialog.cancel();
            return;
        }
        if(mobile.getText().toString().isEmpty()){
            mobile.setError("Phone is Required");
            mobile.requestFocus();
            progressDialog.cancel();
            return;
        }
        if(password.getText().toString().isEmpty()){
            password.setError("Password is Required");
            password.requestFocus();
            progressDialog.cancel();
            return;
        }
        if(!confirm_password.getText().toString().equals(password.getText().toString())){
            confirm_password.setError("Password does not match");
            confirm_password.requestFocus();
           progressDialog.cancel();
            return;

        }

      /*  if(gstin.getText().toString().isEmpty()){
            gstin.setError("Required Field");
            gstin.requestFocus();
            progressDialog.cancel();
            return;
        }
        if(pan.getText().toString().isEmpty()){
            pan.setError("Required Field");
            pan.requestFocus();
            progressDialog.cancel();
            return;
        }*/

        RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);
        Call<Result> call=retrofitInterface.check_usename(email.getText().toString());
        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Result result=response.body();
                Log.e("Signup", "onResponse:"+new Gson().toJson(response.body()));
                if(result.getUser())
                {
                    email.setError("Account Exists");
                    email.requestFocus();
                    progressDialog.cancel();
                    return;

                }
                else{
                    startSignup(name.getText().toString(),email.getText().toString(),password.getText().toString(),confirm_password.getText().toString(),pan.getText().toString(),gstin.getText().toString(),mobile.getText().toString());

                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                Toast.makeText(SignUp.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    private void startSignup(String name, String email, String password, String confirm_password, String pan, String gstin,String mobile)
    {

        RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);
        Map<String,String> map=new HashMap<>();
        map.put("name",name);
        map.put("email",email);
        map.put("contact",mobile);
        map.put("pan",pan);
        map.put("gstin",gstin);
        map.put("password",password);
        map.put("password2",confirm_password);
        if(usertype.getSelectedItem().toString().equals("Individual"))
        {map.put("customertype",usertype.getSelectedItem().toString());}
        else {
            map.put("company",company.getText().toString());
        }
        Call<Result> call=retrofitInterface.signup(map);

        call.enqueue(new Callback<Result>() {
            @Override
            public void onResponse(Call<Result> call, Response<Result> response) {
                Result result=response.body();
                Log.e("Signup", "onResponse:"+new Gson().toJson(response.body()) );
                if(result.getMsg().equals("user created"))
                {
                    progressDialog.cancel();
                    new AlertDialog.Builder(SignUp.this).setTitle("User Created Successfully")
                            .setMessage("Please Login to Continue")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent=new Intent(SignUp.this,LoginActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }).show();
                }
                else{
                    progressDialog.cancel();
                    Toast.makeText(SignUp.this,result.getMsg(),Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Result> call, Throwable t) {
                progressDialog.cancel();
                 Toast.makeText(SignUp.this,t.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }

    public void gotoLogin(View view)
    {
        Intent i=new Intent(this,LoginActivity.class);
        startActivity(i);
        finish();
    }
}
