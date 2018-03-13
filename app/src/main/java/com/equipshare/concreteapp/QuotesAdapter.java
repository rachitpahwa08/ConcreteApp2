package com.equipshare.concreteapp;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.equipshare.concreteapp.model.CustomerSite;
import com.equipshare.concreteapp.model.Quote;
import com.equipshare.concreteapp.model.User_;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Jarvis on 24-01-2018.
 */

public class QuotesAdapter extends RecyclerView.Adapter<QuotesAdapter.ViewHolder>{

    private List<Quote> quoteList;
    private User_ u;
    private List<CustomerSite> customerSite;
    private String token;

    public QuotesAdapter(List<Quote> quoteList,List<CustomerSite> customerSite,User_ u,String token) {
        this.quoteList = quoteList;
        this.customerSite=customerSite;
        this.u=u;
        this.token=token;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.quotes_card,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        long milliseconds=Long.parseLong(quoteList.get(position).getGenerationDate());
        Date date = new Date(milliseconds);
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM,yyyy", Locale.ENGLISH);
        holder.gendate.setText(formatter.format(date));
        holder.customersite.setText(getSitename(quoteList.get(position).getCustomerSite()));
        holder.requestedby.setText("Requested By"+quoteList.get(position).getRequestedBy());

        holder.info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(view.getContext(),QuoteInfo.class);
                i.putExtra("quote", quoteList.get(position));
                i.putExtra("customersite_quote",getSitename(quoteList.get(position).getCustomerSite()));
                i.putExtra("User",u);
                i.putExtra("token",token);
                view.getContext().startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return quoteList.size();
    }

    public  class ViewHolder extends  RecyclerView.ViewHolder{

        public TextView gendate,requestedby,customersite,status;
        public Button info;

        public ViewHolder(View itemView) {
            super(itemView);
            gendate = (TextView) itemView.findViewById(R.id.quote_gen_date);
            requestedby=(TextView)itemView.findViewById(R.id.quote_requestedby);
            customersite=(TextView)itemView.findViewById(R.id.quotecustomer_site);
            info=(Button)itemView.findViewById(R.id.quote_details);
            status=(TextView)itemView.findViewById(R.id.status);

        }
    }
   String getSitename(String id)
   { int position1=0;
    for(int i=0;i<customerSite.size();i++)
    {
        if(id.equals(customerSite.get(i).getId()))
        {
            position1=i;
            Log.e("TAG", "response 33: "+customerSite.get(i).getId()+"id="+id+"position1="+position1+"customersite"+customerSite.get(position1).getName());
        }
    }

    return customerSite.get(position1).getName();
   }
}
