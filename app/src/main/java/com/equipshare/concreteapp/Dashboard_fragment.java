package com.equipshare.concreteapp;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.equipshare.concreteapp.model.Quote;
import com.equipshare.concreteapp.model.Result;
import com.equipshare.concreteapp.model.User_;
import com.equipshare.concreteapp.network.RetrofitInterface;
import com.equipshare.concreteapp.utils.Constants;
import com.equipshare.concreteapp.utils.SessionManagement;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Jarvis on 21-12-2017.
 */

public class Dashboard_fragment extends Fragment {

    Result res1;
    TextView msg;
    User_ u;
    CardView cardView;
    SessionManagement session;
    Gson gson = new GsonBuilder().setLenient().create();
    OkHttpClient client = new OkHttpClient();
    Retrofit.Builder builder=new Retrofit.Builder().baseUrl(Constants.BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create(gson));
    Retrofit retrofit=builder.build();
    RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View rootView = inflater.inflate(R.layout.dashboard_fragment, container, false);
        cardView = rootView.findViewById(R.id.gstnotcomp);
        cardView.setVisibility(View.GONE);
        session = new SessionManagement(getContext());
        msg=(TextView)rootView.findViewById(R.id.msg);
        res1=((DashBoard)getActivity()).r;
        u=((DashBoard)getActivity()).u;
        final User_ user=((DashBoard)getActivity()).u;
        final Result result=((DashBoard)getActivity()).r;
        final Button request_quote = (Button) rootView.findViewById(R.id.quote_request);
        final Button order_book = (Button) rootView.findViewById(R.id.order_book);
        HashMap<String, String> user1 = session.getUserDetails();
        String token=user1.get(SessionManagement.KEY_TOKEN);
        Call<User_> call=retrofitInterface.show_profile(token);
        call.enqueue(new Callback<User_>() {
            @Override
            public void onResponse(Call<User_> call, Response<User_> response) {
                u=response.body();
                Log.e("Dashboard fragment", "getprofile: " + new Gson().toJson(response.body()));
                Log.e("cust_site", "value:"+u.getCustomerSite());
                if(u.getCustomerSite().isEmpty()&&u!=null) {
                    msg.setText("Please Add Construction Site to Continue");
                    order_book.setEnabled(false);
                    request_quote.setEnabled(false);
                    cardView.setVisibility(View.VISIBLE);
                    cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(getContext(),AddSite.class);
                            //intent.putExtra("user",u);
                            startActivity(intent);
                        }
                    });
                }
                else if(res1.getResults().getQuotes().isEmpty()){
                    Log.e("quote", "value:"+res1.getResults().getQuotes());
                    Log.e("Dashboard fragment", "getprofile: " + new Gson().toJson(res1));
                    cardView.setVisibility(View.VISIBLE);
                    msg.setText("Please Add Quotation");
                    cardView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent=new Intent(getContext(),RequestQuote.class);
                            //intent.putExtra("user",u);
                            intent.putExtra("User",user);
                            intent.putExtra("Result",result);
                            startActivity(intent);
                        }
                    });

                }
                else {
                    cardView.setVisibility(View.GONE);

                }
            }

            @Override
            public void onFailure(Call<User_> call, Throwable t) {
                Log.e("TAG", "response 33: " + t.getMessage());

            }
        });
       //Set onclick Listener for buttons in Dashboard fragment
        Button about_concrete = (Button) rootView.findViewById(R.id.about_concrete);
        about_concrete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AboutConcrete.class);
                startActivity(intent);
            }
        });
        Button quality = (Button) rootView.findViewById(R.id.quality);
        quality.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), Quality.class);
                startActivity(intent);
            }
        });


        Log.e("Dashboard fragment", "response 33: " + u+"rseult:"+res1);
        order_book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), OrderBook.class);
                intent.putExtra("Result",res1);
                intent.putExtra("User",u);
                startActivity(intent);
            }
        });

        request_quote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), RequestQuote.class);
                intent.putExtra("User",user);
                intent.putExtra("Result",result);
                startActivity(intent);
            }
        });


        return rootView;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("DashBoard");
    }


}
