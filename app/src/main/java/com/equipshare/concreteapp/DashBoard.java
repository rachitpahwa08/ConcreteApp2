package com.equipshare.concreteapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.equipshare.concreteapp.model.Result;
import com.equipshare.concreteapp.model.User;
import com.equipshare.concreteapp.model.User_;
import com.equipshare.concreteapp.network.RetrofitInterface;
import com.equipshare.concreteapp.utils.Constants;
import com.equipshare.concreteapp.utils.SessionManagement;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.R.attr.fragment;

public class DashBoard extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    User_ u;
    Result r;
    TextView name;

    boolean check;
    boolean doubleBackToExitPressedOnce = false;
    SessionManagement session;
    Gson gson = new GsonBuilder().setLenient().create();
    OkHttpClient client = new OkHttpClient();
    Retrofit.Builder builder=new Retrofit.Builder().baseUrl(Constants.BASE_URL).client(client).addConverterFactory(GsonConverterFactory.create(gson));
    Retrofit retrofit=builder.build();
    RetrofitInterface retrofitInterface=retrofit.create(RetrofitInterface.class);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        Intent i=getIntent();
        r=i.getParcelableExtra("Result");
        Log.e("TAG", "DashResult " + new Gson().toJson(r));
           check=r.getSuccess();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        session = new SessionManagement(getApplicationContext());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        Log.e("User response", "User" +u);
        name = (TextView)header.findViewById(R.id.user_name);
       if(check)
       {profile_getrequest();}
        navigationView.setCheckedItem(R.id.dashboard);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.screen_area,new Dashboard_fragment());
        ft.commit();

    }

    private void profile_getrequest()
    {
        Call<User_> call=retrofitInterface.show_profile(r.getToken());
        call.enqueue(new Callback<User_>() {
            @Override
            public void onResponse(Call<User_> call, Response<User_> response) {
                u=response.body();
                Log.e("TAG", "response 33: " + new Gson().toJson(response.body()));
                name.setText(u.getUser().getName());
            }

            @Override
            public void onFailure(Call<User_> call, Throwable t) {
                Log.e("TAG", "response 33: " + t.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        else {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
            ft.replace(R.id.screen_area,new Dashboard_fragment());
            ft.commit();
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.dash_board, menu);
        return true;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        Fragment fragment=null;

        if (id == R.id.dashboard) {
            fragment= new Dashboard_fragment();

        } else if (id == R.id.profile) {
            fragment=new Profile_fragment();

        } else if (id == R.id.history) {
             fragment=new History_fragment();
        }else if(id==R.id.upcoming_history){
            fragment=new PlacedOrderFragment();
        }
        else if (id == R.id.purchase) {
             fragment=new Purchase_fragment();
        }else if (id == R.id.site) {
            fragment=new SiteFragment();
        }
        else if (id == R.id.issue) {
             fragment=new Issue_fragment();
        } else if (id == R.id.logout) {
            new AlertDialog.Builder(this)
                    .setTitle("Log Out")
                    .setMessage("Are you sure you want to Logout?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            session.logoutUser();

                        }

                    })
                    .setNegativeButton("No", null)
                    .show();

        }

        if(fragment!=null)
        {
            FragmentManager fragmentManager=getSupportFragmentManager();
            FragmentTransaction ft=fragmentManager.beginTransaction();
            ft.replace(R.id.screen_area,fragment);

            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
