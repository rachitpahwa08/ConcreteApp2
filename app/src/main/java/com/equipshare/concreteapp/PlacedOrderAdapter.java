package com.equipshare.concreteapp;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.equipshare.concreteapp.model.CustomerSite;
import com.equipshare.concreteapp.model.Order;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by Jarvis on 12-03-2018.
 */


public class PlacedOrderAdapter extends RecyclerView.Adapter<PlacedOrderAdapter.ViewHolder> {

    private List<Order> my_data;
    private List<CustomerSite> customerSite;
    String token;
    int position1;
    public PlacedOrderAdapter(List<Order> my_data,List<CustomerSite> customerSite,String token) {

        this.customerSite=customerSite;
        this.my_data = my_data;
        this.token=token;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.placedorder_card,parent,false);
        return new ViewHolder(itemView);

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        long milliseconds=Long.parseLong(my_data.get(position).getGenerationDate());
        Date date = new Date(milliseconds);
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM,yyyy", Locale.ENGLISH);
        if(my_data.get(position).getStatus().equals("submitted")||(my_data.get(position).getStatus().equals("approved"))){
            holder.orderdate.setText(formatter.format(date));
            holder.status.setText(my_data.get(position).getStatus());
            holder.requestedby.setText("Order Requested By:"+my_data.get(position).getRequestedBy());


            final Order order=my_data.get(position);
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i=new Intent(view.getContext(),HistoryInfo.class);
                    i.putExtra("Order", order);
                    i.putExtra("ordersite",getSitename(my_data.get(holder.getAdapterPosition()).getCustomerSite()));
                    i.putExtra("token",token);
                    view.getContext().startActivity(i);
                }
            });
        }

        else {
            holder.cardView.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return my_data.size();
    }

    public  class ViewHolder extends  RecyclerView.ViewHolder{

        public TextView orderdate,status,requestedby,price;
        CardView cardView;
        Button button;

        public ViewHolder(View itemView) {
            super(itemView);
            orderdate = (TextView) itemView.findViewById(R.id.order_date_placed);
            status=(TextView)itemView.findViewById(R.id.status_placed);
            requestedby=(TextView)itemView.findViewById(R.id.requestedby_placed);
            button=(Button)itemView.findViewById(R.id.placed_details);
            cardView=(CardView)itemView.findViewById(R.id.placed_card);
        }
    }
    String getSitename(String id)
    { int position1=0;
        for(int i=0;i<customerSite.size();i++)
        {
            Log.e("TAG", "response 33: "+customerSite.get(i).getId()+"id="+id+"position1="+position1+"customersite"+customerSite.get(position1).getName());
            if(id.equals(customerSite.get(i).getId()))
            {
                position1=i;
                Log.e("TAG", "response 33: "+customerSite.get(i).getId()+"id="+id+"position1="+position1+"customersite"+customerSite.get(position1).getName());
                break;

            }
        }

        return customerSite.get(position1).getName();
    }
}
