package com.equipshare.concreteapp;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;

import com.equipshare.concreteapp.model.Result;
import com.equipshare.concreteapp.model.User;
import com.equipshare.concreteapp.network.RetrofitInterface;
import com.equipshare.concreteapp.utils.Constants;
import com.equipshare.concreteapp.utils.SessionManagement;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private Button signin;
    TextView forgotpass;
    Button signup;
    ProgressDialog progressDialog;
    public static final String TAG = LoginActivity.class.getSimpleName();
    private String email,password;
    Result result=null;
    Context context;
    SessionManagement session;//to store user credentials
    /**
     * code to initialize retrofit api to connect to server
     */
    Gson gson = new GsonBuilder().setLenient().create();
    OkHttpClient client = new OkHttpClient();
    Retrofit.Builder builder=new Retrofit.Builder().baseUrl(Constants.BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create(gson));
  Retrofit retrofit=builder.build();
   RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        signin = (Button) findViewById(R.id.email_sign_in_button);
        context = this.getApplicationContext();
        session = new SessionManagement(getApplicationContext());
        signup=(Button)findViewById(R.id.SignUp);
        forgotpass=(TextView)findViewById(R.id.forgot_button);

        signin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                 email=mEmailView.getText().toString();
                 password=mPasswordView.getText().toString();
                startSignin();
            }
        });
        signup.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,SignUp.class);
                startActivity(intent);
            }
        });
        forgotpass.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,Password_reset.class);
                startActivity(intent);
            }
        });
    }



    public void startSignin(){

        String email=mEmailView.getText().toString();
        String password=mPasswordView.getText().toString();

        if(email.isEmpty()){
          mEmailView.setError("Email is Required");
          mEmailView.requestFocus();
          return;
      }
      if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
          mEmailView.setError("Please enter a valid email or password");
          mEmailView.requestFocus();
          return;
      }
        if(password.isEmpty()){
            mPasswordView.setError("Password is Required");
            mPasswordView.requestFocus();
            return;
        }
        if(password.length()<3){
            mPasswordView.setError("Minimum length of Password should be 6 characters");
            mPasswordView.requestFocus();
            return;
        }
        loginprocess();
        progressDialog=new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Signing In");
        progressDialog.setCancelable(false);
        progressDialog.show();

    }
   private void loginprocess()
   {   /**
        *sending login request to server
       **/
       Call<Result> call=retrofitInterface.login(email,password);

       call.enqueue(new Callback<Result>() {
           @Override
           public void onResponse(Call<Result> call, retrofit2.Response<Result> response) {

               result=  response.body(); // have your all data
               Log.e("TAG", "response 33: " + new Gson().toJson(response.body()));
               if(result.getMsg()!=null&&result.getMsg().equals("wrong password"))
             {
                 progressDialog.cancel();
                 Log.e("TAG", "response 33: " + new Gson().toJson(response.body()));
              mPasswordView.setError("Incorrect Password");
              mPasswordView.requestFocus();
                 return ;
             }
             if(result.getMsg()!=null&&result.getMsg().equals("user with this username does not exist"))
             {
                 progressDialog.cancel();
                 mEmailView.setError("Invalid Username");
                 mEmailView.requestFocus();
                 return;
              }
               boolean check=false;
               check=result.getSuccess();
               if(!check)
              {
                  new AlertDialog.Builder(LoginActivity.this)
                          .setTitle("Can't Connect to Server")
                          .setMessage("Can't Connect to server Please Check after some time")
                          .setPositiveButton("Exit", new DialogInterface.OnClickListener() {
                              @Override
                              public void onClick(DialogInterface dialogInterface, int i) {
                                  finish();
                                  System.exit(0);
                              }
                          })
                          .show();
              }
                 else {

                 progressDialog.cancel();
                 Log.e("TAG", "response 33: " + new Gson().toJson(response.body()));
                 Log.e("TAG", "response 33: " + response.body());
                   session.createLoginSession(email, password,result.getToken());
                 Intent intent = new Intent(LoginActivity.this, DashBoard.class);
                 intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                 intent.putExtra("Result", result);
                 startActivity(intent);
                 finish();
             }

             }
           @Override
           public void onFailure(Call<Result> call, Throwable t) {
               Toast.makeText(LoginActivity.this,t.getMessage(),Toast.LENGTH_SHORT).show();
               Log.e("TAG", "error: " + t);
               progressDialog.cancel();
           }

       });
   }
   Result getResult()
{
    return result;
}
}

