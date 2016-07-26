package com.fom.msesoft.fomapplication.fragment;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;

import com.fom.msesoft.fomapplication.activity.FriendList;
import com.fom.msesoft.fomapplication.activity.FriendList_;
import com.fom.msesoft.fomapplication.activity.MainActivity;
import com.fom.msesoft.fomapplication.adapter.CircleTransform;
import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.adapter.ProfilePagerAdapter;
import com.fom.msesoft.fomapplication.model.Person;
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

import java.util.Arrays;
import java.util.List;


@EFragment(R.layout.profile_fragment)
public class ProfileFragment extends Fragment {

    private  Person person;

    @RestService
    PersonRepository personRepository;

    @RestService
    PlacesRepository placesRepository;

    @ViewById(R.id.profilePicture)
    ImageView profilePicture;

    @ViewById(R.id.textView)
    TextView textView;

    @ViewById(R.id.friendNumber)
    TextView friendNumber;

    @ViewById(R.id.profileName)
    TextView profileName;

    @ViewById(R.id.profilePager)
    ViewPager viewPager;

    @ViewById(R.id.profileTab)
    TabLayout tabLayout;

    @ViewById(R.id.work)
    TextView work;

    @ViewById(R.id.hoby)
    TextView hoby;

    @ViewById(R.id.friendsText)
    TextView friendsText;


    @AfterViews
    void profileView () {

        profileConnection();

        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_perm_media_white_24dp));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_settings_white_24dp));
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
                    tab.setIcon(R.drawable.ic_perm_media_black_24dp);
                }else if(tab.getPosition()==1){
                    tab.setIcon(R.drawable.ic_settings_black_24dp);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                if(tab.getPosition()==0){
                    tab.setIcon(R.drawable.ic_perm_media_white_24dp);
                }else if(tab.getPosition()==1){
                    tab.setIcon(R.drawable.ic_settings_white_24dp);
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    @Background
    void profileConnection () {
        person = ((MainActivity)getActivity()).getPerson();
        List<Person> firstDegreeFriend = Arrays.asList(personRepository.findByFirstDegreeFriend(((MainActivity)getActivity()).getPerson().getUniqueId()));
        Places places = placesRepository.personWorkSearch(person.getUniqueId());
        profileNumber(firstDegreeFriend,person,places);
    }

    @UiThread
    void profileNumber(List<Person> firstDegreeFriend, Person person,Places places) {
        friendNumber.setText(firstDegreeFriend.size() + "");
        Picasso.with(getActivity())
                .load(person.getPhoto().toString())
                .resize(200,200)
                .transform(new CircleTransform())
                .into(profilePicture);
        profileName.setText(person.getFirstName()+" "+person.getLastName());
        work.setText(places.getType()+", "+places.getName());
        hoby.setText(person.getHoby());

    }
    @Click(R.id.friendNumber)
    void friendList () {

        Intent i = new Intent(getActivity(),FriendList_.class);
        i.putExtra("Person",person);
        startActivity(i);

    }
    @Click(R.id.friendsText)
    void friedList() {
        Intent i = new Intent(getActivity(),FriendList_.class);
        i.putExtra("Person",person);
        startActivity(i);
    }
    @Click(R.id.checkButton)
    void CheckButton () {


    }

}