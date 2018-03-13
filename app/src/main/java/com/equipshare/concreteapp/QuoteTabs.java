package com.equipshare.concreteapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Jarvis on 21-01-2018.
 */

public class QuoteTabs extends FragmentStatePagerAdapter
{
    String[] titles=new String[]{"Request Quote","Quotes Received"};
    public QuoteTabs(FragmentManager fm) {
        super(fm);
    }
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0: RequestQuoteFragment requestQuoteFragment=new RequestQuoteFragment();
                return requestQuoteFragment;
            case 1: AvailableQuote availableQuote=new AvailableQuote();
                return availableQuote;

            default:return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
