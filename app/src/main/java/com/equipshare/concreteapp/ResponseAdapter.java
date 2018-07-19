package com.equipshare.concreteapp;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.equipshare.concreteapp.model.Quote;
import com.equipshare.concreteapp.model.Result;
import com.equipshare.concreteapp.network.RetrofitInterface;
import com.equipshare.concreteapp.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Jarvis on 14-07-2018.
 */

public class ResponseAdapter extends RecyclerView.Adapter<ResponseAdapter.ViewHolder> {

    private Quote quote;
    private String token;
    private String sitename;
    String suppname;
    Gson gson = new GsonBuilder().setLenient().create();
    OkHttpClient client = new OkHttpClient();
    Retrofit.Builder builder=new Retrofit.Builder().baseUrl(Constants.BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create(gson));
    Retrofit retrofit=builder.build();
    RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);
    public ResponseAdapter(Quote quote, String token,  String sitename) {
        this.quote = quote;
        this.token = token;
        this.sitename = sitename;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.response_cards,parent,false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        long price1 = 0;
        if (quote.getResponses().get(position).getValidTill() != null) {
            long milliseconds = Long.parseLong(quote.getResponses().get(position).getValidTill());
            Date date = new Date(milliseconds);
            SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM,yyyy", Locale.ENGLISH);
            holder.val_date.setText("Quote Valid Till:" + formatter.format(date));
            RetrofitInterface retrofitInterface = retrofit.create(RetrofitInterface.class);
            Call<Result> call = retrofitInterface.supp_name(String.valueOf(quote.getResponses().get(position).getRmxId()));
            call.enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    Log.e("TAG", "response 33: " + new Gson().toJson(response.body()));
                    Result result = response.body();
                    holder.supp_name.setText("Supplier Name:" + result.getSuppname());
                    suppname = result.getSuppname();
                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    Log.e("Response Adapter", "Error:" + t.getMessage());
                }
            });
//
            final ArrayList<String> price_list = new ArrayList<String>();
            Log.e("Response Adapter", "quote:" + gson.toJson(quote.getResponses()) + "quote id from response:" + quote.getResponses().get(position).getId() + "quote id from price" + quote.getPrice().getId().get(position));
            price_list.clear();
            for (int z = 0; z < quote.getPrice().getId().size(); z++) {
                if (quote.getResponses().get(position).getId().equals(quote.getPrice().getId().get(z))) {
                    price1 = price1 + Long.parseLong(quote.getPrice().getPrice().get(z));
                    price_list.add(quote.getPrice().getPrice().get(z));
                    Log.e("Response Adapter", "Price: " + quote.getPrice().getPrice().get(z) + " Price1:" + price1 + "z=" + z + "id:" + quote.getResponses().get(position).getId() + "id from price:" + quote.getPrice().getId().get(z));
                }
            }
            holder.accept_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(view.getContext(), ResponseDetails.class);
                    intent.putExtra("price", price_list);
                    intent.putExtra("position", position);
                    intent.putExtra("quote", quote);
                    intent.putExtra("suppname", suppname);
                    intent.putExtra("sitename", sitename);
                    intent.putExtra("suppid", String.valueOf(quote.getResponses().get(position).getRmxId()));
                    intent.putExtra("token", token);
                    view.getContext().startActivity(intent);
                }
            });
        }
        else{
            holder.cards.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return quote.getResponses().size();
    }
    public  class ViewHolder extends  RecyclerView.ViewHolder{
        private TextView val_date,price,supp_name;
        private Button accept_button;
        CardView cards;
        public ViewHolder(View itemView) {
            super(itemView);
            val_date=(TextView)itemView.findViewById(R.id.response_gen_date);
            supp_name=(TextView)itemView.findViewById(R.id.response_suppname);
            accept_button=(Button)itemView.findViewById(R.id.response_details);
            cards=(CardView)itemView.findViewById(R.id.response_card);
        }
    }

}
