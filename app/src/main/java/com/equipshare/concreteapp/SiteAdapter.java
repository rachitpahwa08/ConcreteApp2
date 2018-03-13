package com.equipshare.concreteapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.equipshare.concreteapp.model.CustomerSite;
import com.equipshare.concreteapp.model.Result;
import com.equipshare.concreteapp.model.User;
import com.equipshare.concreteapp.network.RetrofitInterface;
import com.equipshare.concreteapp.utils.Constants;
import com.equipshare.concreteapp.utils.DirectingClass;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Jarvis on 26-01-2018.
 */

public class SiteAdapter extends RecyclerView.Adapter<SiteAdapter.ViewHolder> {
    Gson gson = new GsonBuilder().setLenient().create();
    OkHttpClient client = new OkHttpClient();
    Retrofit.Builder builder=new Retrofit.Builder().baseUrl(Constants.BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create(gson));
    Retrofit retrofit=builder.build();
    RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);
    private List<CustomerSite> siteList;
    private User user;
    RelativeLayout relativeLayout;
    Context context;
    Activity activity;
    Result result;

    public SiteAdapter(List<CustomerSite> siteList, User user, RelativeLayout relativeLayout, Context context, Activity activity, Result result) {
        this.siteList = siteList;
        this.user=user;
        this.context=context;
        this.relativeLayout = relativeLayout;
        this.activity=activity;
        this.result=result;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.site_card,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
       if(siteList.isEmpty())
       {

       }
       else
       {holder.sitename.setText("Site Name:"+siteList.get(position).getName());
        holder.siteaddress.setText(siteList.get(position).getAddress());
        holder.deletesite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                new AlertDialog.Builder(context)
                        .setTitle("Delete this site")
                        .setMessage("Are you sure you want to delete this site?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Map<String,String> map=new HashMap<>();
                                map.put("siteid",siteList.get(position).getId());
                                Call<Result> call=retrofitInterface.delete_site(result.getToken(),map);
                                call.enqueue(new Callback<Result>() {
                                    @Override
                                    public void onResponse(Call<Result> call, Response<Result> response) {
                                       Result r=response.body();
                                        if(r.getMsg().equals("site deleted"))
                                        {Snackbar snackbar = Snackbar
                                                .make(relativeLayout, "Site Deleted Successfully", Snackbar.LENGTH_LONG);
                                        snackbar.setActionTextColor(Color.RED);
                                        snackbar.show();
                                        Log.e("TAG", "response 33: "+new Gson().toJson(response.body()));
                                        DirectingClass directingClass=new DirectingClass(context,activity);
                                        directingClass.performLogin();
                                    }
                                    else {
                                            Toast.makeText(context,"Cannot Delete Site",Toast.LENGTH_LONG).show();
                                        }}

                                    @Override
                                    public void onFailure(Call<Result> call, Throwable t) {
                                        Toast.makeText(view.getContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });

                            }

                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        });
    }}

    @Override
    public int getItemCount() {
        return siteList.size();
    }


    public  class ViewHolder extends  RecyclerView.ViewHolder{

        public TextView sitename,siteaddress;
        Button deletesite;
        public ViewHolder(View itemView) {
            super(itemView);
            sitename = (TextView) itemView.findViewById(R.id.site_name);
            siteaddress=(TextView)itemView.findViewById(R.id.siteaddress);
            deletesite=(Button)itemView.findViewById(R.id.delete_site);
        }
    }
}
