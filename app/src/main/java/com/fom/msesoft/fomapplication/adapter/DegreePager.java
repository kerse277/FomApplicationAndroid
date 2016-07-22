package com.fom.msesoft.fomapplication.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fom.msesoft.fomapplication.fragment.DegreeFriend;
import com.fom.msesoft.fomapplication.fragment.DegreeFriend_;
import com.fom.msesoft.fomapplication.fragment.FeedFragment;

import com.fom.msesoft.fomapplication.fragment.ProfileFragment;
import com.fom.msesoft.fomapplication.fragment.ProfileFragment_;
import com.fom.msesoft.fomapplication.fragment.FeedFragment_;

public class DegreePager extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public DegreePager(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                DegreeFriend tab1 = new DegreeFriend_();
                return tab1;
            case 1:
                ProfileFragment tab2 = new ProfileFragment_();
                return tab2;
            case 2:
                FeedFragment tab3 = new FeedFragment_();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}