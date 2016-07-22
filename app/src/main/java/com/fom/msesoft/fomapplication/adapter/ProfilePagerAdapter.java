package com.fom.msesoft.fomapplication.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fom.msesoft.fomapplication.fragment.ProfileFragmentPhoto;
import com.fom.msesoft.fomapplication.fragment.ProfileFragmentPhoto_;
import com.fom.msesoft.fomapplication.fragment.ProfileFragmentSettings;
import com.fom.msesoft.fomapplication.fragment.ProfileFragmentSettings_;


public class ProfilePagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public ProfilePagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                ProfileFragmentPhoto tab1 = new ProfileFragmentPhoto_();
                return tab1;
            case 1:
                ProfileFragmentSettings tab2 = new ProfileFragmentSettings_();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}