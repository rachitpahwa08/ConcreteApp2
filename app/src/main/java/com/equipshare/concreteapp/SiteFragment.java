package com.equipshare.concreteapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.equipshare.concreteapp.model.Result;
import com.equipshare.concreteapp.model.User_;

/**
 * Created by Jarvis on 21-01-2018.
 */

public class SiteFragment extends Fragment
{

    User_ user;
    Result result;
    private RecyclerView recyclerView;
    RelativeLayout relativeLayout;
    TextView empty;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.site_fragment,null);

         user=(((DashBoard) getActivity()).u);
         result=(((DashBoard) getActivity()).r);
        FloatingActionButton addsite=(FloatingActionButton)view.findViewById(R.id.add_site);
        addsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),AddSite.class);
                i.putExtra("User",user.getUser());
                i.putExtra("Result",result);
                startActivity(i);
            }
        });
        relativeLayout=(RelativeLayout)view.findViewById(R.id.site_layout);
        recyclerView=(RecyclerView)view.findViewById(R.id.siteRecyclerView);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1);
        recyclerView.setLayoutManager(gridLayoutManager);
        SiteAdapter siteAdapter=new SiteAdapter(user.getUser().getCustomerSite(),user.getUser(),relativeLayout,getContext(),getActivity(),result);
        recyclerView.setAdapter(siteAdapter);
        if(gridLayoutManager.getItemCount()==0)
        {
         empty=(TextView)view.findViewById(R.id.emptysite);
         empty.setText("To Add Site Tap On + Icon ");
        }
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Customer Site");
    }
}
