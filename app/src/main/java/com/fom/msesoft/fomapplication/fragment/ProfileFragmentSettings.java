package com.fom.msesoft.fomapplication.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.activity.LoginActivity;
import com.fom.msesoft.fomapplication.activity.LoginActivity_;
import com.fom.msesoft.fomapplication.activity.MainActivity;
import com.fom.msesoft.fomapplication.adapter.ProfileSettingsAdapter;
import com.fom.msesoft.fomapplication.extras.Preferences_;
import com.fom.msesoft.fomapplication.model.CustomPerson;
import com.fom.msesoft.fomapplication.model.Person;
import com.fom.msesoft.fomapplication.repository.PersonRepository;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.androidannotations.annotations.AfterTextChange;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.androidannotations.rest.spring.annotations.Body;
import org.androidannotations.rest.spring.annotations.RestService;

import java.io.IOException;

@EFragment(R.layout.profile_settings_fragment)
public class ProfileFragmentSettings extends Fragment{

    @Pref
    Preferences_ preferences;

    @ViewById(R.id.recyclerView)
    RecyclerView recyclerView;

    @ViewById(R.id.logout_button)
    Button logout;

    @ViewById(R.id.nameTxt)
    EditText nameTxt;

    @ViewById(R.id.trueImage)
    ImageView trueImage;

    @ViewById(R.id.falseImage)
    ImageView falseImage;


    @Click(R.id.logout_button)
    void logout()  {
     unRegisterGCM();
    }



    @RestService
    PersonRepository personRepository;

    @Background
    void unRegisterGCM(){
        personRepository.registerGCM(((MainActivity)getActivity()).getToken(),"null");
        logoutEnd();
    }
    @UiThread
    void logoutEnd(){
        preferences.clear();

        Intent intent = new Intent(getActivity(), LoginActivity_.class);
        startActivity(intent);
        getActivity().finish();
    }
    @AfterViews
    void afterViews() {
        String token = ((MainActivity)getActivity()).getToken();
        profileConnection(token);


    }


  /*  @AfterTextChange
    void nameTextChange () {
        falseImage.setVisibility(View.GONE);
        trueImage.setVisibility(View.GONE);

    }
*/
    @Background
    void profileConnection(String token) {
       CustomPerson customPerson = personRepository.findByToken(token);
        profileSetup(customPerson);
    }
    @UiThread
    void profileSetup (CustomPerson customPerson) {
        ProfileSettingsData profileSettingsData[] = {
                new ProfileSettingsData(customPerson.getFirstName()+" "+customPerson.getLastName(),R.drawable.ic_account_circle_black_24dp),
                new ProfileSettingsData("Hobi",R.drawable.ic_history_black_24dp),
                new ProfileSettingsData("İş",R.drawable.ic_work_black_24dp)};
        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // 3. create an adapter
        ProfileSettingsAdapter mAdapter = new ProfileSettingsAdapter(profileSettingsData);
        // 4. set adapter
        recyclerView.setAdapter(mAdapter);
        // 5. set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


}
