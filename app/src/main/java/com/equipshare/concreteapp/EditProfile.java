package com.equipshare.concreteapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.equipshare.concreteapp.model.Result;
import com.equipshare.concreteapp.model.User;
import com.equipshare.concreteapp.model.User_;
import com.equipshare.concreteapp.network.RetrofitInterface;
import com.equipshare.concreteapp.utils.Constants;
import com.equipshare.concreteapp.utils.DirectingClass;
import com.equipshare.concreteapp.utils.SessionManagement;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class EditProfile extends AppCompatActivity {
     EditText name,email,contact,pan,gst;
     LinearLayout linearLayout;
    User_ user;
    Result result;
    SessionManagement session;
    String token;
    private static Retrofit.Builder builder=new Retrofit.Builder().baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());
    public static Retrofit retrofit=builder.build();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent i=getIntent();
        user=i.getParcelableExtra("user");
        result=i.getParcelableExtra("Result");
        session = new SessionManagement(EditProfile.this);
        HashMap<String, String> user1 = session.getUserDetails();
        token=user1.get(SessionManagement.KEY_TOKEN);
        name=(EditText)findViewById(R.id.name);
        email=(EditText)(EditText)findViewById(R.id.email_profile);
        contact=(EditText)findViewById(R.id.contact_profile);
        pan=(EditText)findViewById(R.id.pan_profile);
        gst=(EditText)findViewById(R.id.gst_profile);
        linearLayout=(LinearLayout)findViewById(R.id.edit_profile);
        name.setText(user.getUser().getName());
        email.setText(user.getUser().getEmail());
        contact.setText(String.valueOf(user.getUser().getContact()));
        pan.setText(user.getUser().getPan());
        gst.setText(user.getUser().getGstin());
        Button submit=(Button)findViewById(R.id.submit_profile);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                startEditProfile();
            }
        });
    }

    private void startEditProfile() {
       if(name.getText().toString().isEmpty())
       {   name.setError("Required Field");
           name.requestFocus();
           return;
       }
        if(email.getText().toString().isEmpty())
        {   email.setError("Required Field");
            email.requestFocus();
            return;
        }
        if(contact.getText().toString().isEmpty())
        {   contact.setError("Required Field");
            contact.requestFocus();
            return;
        }
        if(pan.getText().toString().isEmpty())
        {   pan.setError("Required Field");
            pan.requestFocus();
            return;
        }
        if(gst.getText().toString().isEmpty())
        {   gst.setError("Required Field");
            gst.requestFocus();
            return;
        }
        startSubmit();
    }

    private void startSubmit()
    {
        RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);
        Map<String,String> map=new HashMap<>();
        map.put("id",String.valueOf(user.getUser().getUserId()));
        map.put("name",name.getText().toString());
        map.put("email",email.getText().toString());
        map.put("contact",contact.getText().toString());
        map.put("pan",pan.getText().toString());
        map.put("gstin",gst.getText().toString());
        Call<ResponseBody> call=retrofitInterface.edit_profile(token,map);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Snackbar snackbar = Snackbar
                        .make(linearLayout, "Profile Edited Successfully", Snackbar.LENGTH_LONG);
                snackbar.setActionTextColor(Color.RED);
                snackbar.show();
                Log.e("TAG", "response 33: "+new Gson().toJson(response.body()));
                DirectingClass directingClass=new DirectingClass(EditProfile.this,EditProfile.this);
                directingClass.performLogin();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(EditProfile.this,t.getMessage(),Toast.LENGTH_SHORT).show();
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
