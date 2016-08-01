package com.fom.msesoft.fomapplication.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.adapter.DegreePager;
import com.fom.msesoft.fomapplication.extras.Preferences_;
import com.fom.msesoft.fomapplication.model.CustomPerson;
import com.fom.msesoft.fomapplication.model.Person;
import com.fom.msesoft.fomapplication.repository.PersonRepository;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.RestService;

import lombok.Getter;
import lombok.Setter;

@EActivity(R.layout.activity_main)
@Fullscreen
public class MainActivity extends AppCompatActivity {

    @ViewById(R.id.pager)
    ViewPager viewPager;

    @ViewById(R.id.tab_layout)
    TabLayout tabLayout;

    @Getter
    @Setter
    String token;

    @RestService
    PersonRepository personRepository;

    @AfterViews
    void afterViews(){

        token = getIntent().getStringExtra("token");


        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.find2));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.profile));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.feed));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final DegreePager adapter = new DegreePager
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==0){
                    tab.setIcon(R.drawable.find2);
                }else if(tab.getPosition()==1){
                    tab.setIcon(R.drawable.profile2);
                }else if(tab.getPosition()==2){
                    tab.setIcon(R.drawable.feed2);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(tab.getPosition()==0){
                    tab.setIcon(R.drawable.find);
                }else if(tab.getPosition()==1){
                    tab.setIcon(R.drawable.profile);
                }else if(tab.getPosition()==2){
                    tab.setIcon(R.drawable.feed);
                }

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


}