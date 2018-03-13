package com.equipshare.concreteapp;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

/**
 * Created by Jarvis on 31-01-2018.
 */

public class Quality_fragment extends android.support.v4.app.Fragment {
    Spinner quality;
    TextView about;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
       View view=inflater.inflate(R.layout.quality_fragment,null);
       quality=(Spinner)view.findViewById(R.id.qual_spinner);
       about=(TextView)view.findViewById(R.id.about_quality);
       quality.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               switch (i)
               {
                   case 0:about.setText(R.string.M05);
                   break;
                   case 1:about.setText(R.string.M7_5);
                   break;
                   case 2:about.setText(R.string.M10);
                       break;
                   case 3:about.setText(R.string.M15);
                       break;
                   case 4:about.setText(R.string.M20);
                       break;
                   case 5:about.setText(R.string.M25);
                       break;
                   case 6:about.setText(R.string.M30);
                       break;
                   case 7:about.setText(R.string.M35);
                       break;
                   case 8:about.setText(R.string.M40);
                       break;
                   case 9:about.setText(R.string.M45);
                       break;
                   case 10:about.setText(R.string.M50);
                       break;
                   case 11:about.setText(R.string.M55);
                       break;
                   case 12:about.setText(R.string.M60);
                       break;
                   case 13:about.setText(R.string.M70);
                       break;
                   case 14:about.setText(R.string.M80);
                       break;
                   case 15:about.setText(R.string.M100);
                       break;
                   default:about.setText("Error!!!");
               }
           }

           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {

           }
       });
       return view;
    }
}
