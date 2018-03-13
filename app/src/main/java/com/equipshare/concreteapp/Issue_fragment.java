package com.equipshare.concreteapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.equipshare.concreteapp.model.Result;
import com.equipshare.concreteapp.network.RetrofitInterface;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Jarvis on 21-12-2017.
 */

public class Issue_fragment extends Fragment {

    private RecyclerView recyclerView;
    TextView empty;
    ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.issue_fragment,null);
        progressDialog=new ProgressDialog(getContext());
        progressDialog.setMessage("Loading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        recyclerView=(RecyclerView)view.findViewById(R.id.issueRecyclerView);
        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(getContext());
        gridLayoutManager.setReverseLayout(true);
        gridLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        Result r1=((DashBoard)getActivity()).r;
        IssueAdapter issueAdapter=new IssueAdapter(r1.getResults().getIssues());
        recyclerView.setAdapter(issueAdapter);
        progressDialog.cancel();
        if(gridLayoutManager.getItemCount()==0)
        {
            empty=(TextView)view.findViewById(R.id.emptyissue);
            empty.setText("No Issues To Show");
        }
        else{
            View b = view.findViewById(R.id.emptyissue);
            b.setVisibility(View.GONE);
        }
        return view;

    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();


        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Issue");
    }
}
