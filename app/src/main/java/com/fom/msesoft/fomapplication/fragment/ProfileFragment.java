package com.fom.msesoft.fomapplication.fragment;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.fom.msesoft.fomapplication.activity.FriendList_;
import com.fom.msesoft.fomapplication.activity.MainActivity;
import com.fom.msesoft.fomapplication.extras.CircleTransform;
import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.adapter.ProfilePagerAdapter;
import com.fom.msesoft.fomapplication.model.CustomPerson;
import com.fom.msesoft.fomapplication.model.Places;
import com.fom.msesoft.fomapplication.repository.PersonRepository;
import com.fom.msesoft.fomapplication.repository.PlacesRepository;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


@EFragment(R.layout.profile_fragment)
public class ProfileFragment extends Fragment {

    @Getter
    @Setter
    private  CustomPerson customPerson;

    String token;

    @RestService
    PersonRepository personRepository;

    @RestService
    PlacesRepository placesRepository;

    @ViewById(R.id.profilePicture)
    ImageView profilePicture;

    @ViewById(R.id.textView)
    TextView textView;

    @ViewById(R.id.friendCount)
    TextView friendNumber;

    @ViewById(R.id.profileName)
    TextView profileName;

    @ViewById(R.id.profilePager)
    ViewPager viewPager;

    @ViewById(R.id.profileTab)
    TabLayout tabLayout;

    @ViewById(R.id.workName)
    TextView work;




    @AfterViews
    void profileView () {
        token = ((MainActivity)getActivity()).getToken();
        profileConnection();

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.profil_tab_pic_icon2));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.profile_tab_set_icon));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        final ProfilePagerAdapter adapter = new ProfilePagerAdapter
                (((MainActivity)getActivity()).getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                //viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition()==0){
                    tab.setIcon(R.drawable.profil_tab_pic_icon2);
                }else if(tab.getPosition()==1){
                    tab.setIcon(R.drawable.profile_tab_set_icon2);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(tab.getPosition()==0){
                    tab.setIcon(R.drawable.profil_tab_pic_icon);
                }else if(tab.getPosition()==1){
                    tab.setIcon(R.drawable.profile_tab_set_icon);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    @Background
    void profileConnection () {

        setCustomPerson(personRepository.findByToken(((MainActivity)getActivity()).getToken()));
        List<CustomPerson> firstDegreeFriend = Arrays.asList(personRepository.findByFirstDegreeFriend(token));
        Places places = placesRepository.personWorkSearch(customPerson.getUniqueId());
        profileNumber(firstDegreeFriend,customPerson,places);
    }

   @UiThread
    void profileNumber(List<CustomPerson> firstDegreeFriend, CustomPerson customPerson, Places places) {
        friendNumber.setText(firstDegreeFriend.size() + "");
        Picasso.with(getActivity())
                .load(customPerson.getPhoto().toString())

                .transform(new CircleTransform())
                .into(profilePicture);
        profileName.setText(customPerson.getFirstName()+" "+customPerson.getLastName());

        if(places!=null){
        work.setText(places.getType()+", "+places.getName());

        } else{
            work.setText("No Work");
        }

    }
    @Click(R.id.friendCount)
    void friendList () {

        Intent i = new Intent(getActivity(),FriendList_.class);
        i.putExtra("token",token);
        startActivity(i);

    }

    @Click(R.id.checkButton)
    void CheckButton () {


    }

}