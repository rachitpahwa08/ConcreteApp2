package com.equipshare.concreteapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.equipshare.concreteapp.model.Result;
import com.equipshare.concreteapp.model.User;
import com.equipshare.concreteapp.model.User_;


/**
 * Created by Jarvis on 21-12-2017.
 */

public class Profile_fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.profile_fragment,container,false);

        User_ user=((DashBoard)getActivity()).u;
        final Result result=((DashBoard)getActivity()).r;
        final User u=user.getUser();
        TextView name=(TextView) view.findViewById(R.id.Name);
        TextView customerprofile=(TextView) view.findViewById(R.id.customerProfile);
        TextView email=(TextView) view.findViewById(R.id.email);
        TextView contactNo=(TextView) view.findViewById(R.id.contactNo);
        TextView pan=(TextView) view.findViewById(R.id.PAN);
        TextView gst=(TextView)view.findViewById(R.id.GSTIN);
        TextView company=(TextView)view.findViewById(R.id.company);
        LinearLayout company_layout=(LinearLayout) view.findViewById(R.id.company_layout);
        FloatingActionButton fab=(FloatingActionButton)view.findViewById(R.id.edit_profile);
        name.setText(u.getName());
        customerprofile.setText(u.getUserType());
        email.setText(u.getEmail());
        contactNo.setText(String.valueOf(u.getContact()));
        pan.setText(u.getPan());
        gst.setText(u.getGstin());
        if(u.getCompany()!=null)
        {
            company.setText(u.getCompany());
        }
        else
        {
            company_layout.setVisibility(View.GONE);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),EditProfile.class);
                intent.putExtra("user",u);
                intent.putExtra("Result",result);
                startActivity(intent);
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Profile");
    }


}
