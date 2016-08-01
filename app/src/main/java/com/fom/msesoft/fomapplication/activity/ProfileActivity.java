package com.fom.msesoft.fomapplication.activity;

import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.extras.CircleTransform;
import com.fom.msesoft.fomapplication.model.CustomPerson;
import com.fom.msesoft.fomapplication.repository.PersonRepository;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

@EActivity(R.layout.activity_profile)
public class ProfileActivity extends AppCompatActivity {

    @RestService
    PersonRepository personRepository;

    @ViewById(R.id.profile_activity_image)
    ImageView profilePhoto;

    @ViewById(R.id.profile_activity_profileName)
    TextView profileName;

    @ViewById(R.id.profile_activity_friendSize)
    TextView friendSize;

    @ViewById(R.id.profile_activity_hoby)
    TextView hoby;

    @Extra("uniqueId")
    String uniqueId;

    @AfterViews
    void afterView(){

        findPerson();
    }


    @Background
    void findPerson(){
        CustomPerson customPerson = personRepository.findPersonByUniqueId(uniqueId);
        postExecute(customPerson);
    }

    @UiThread
    void postExecute(CustomPerson customPerson){
        profileName.setText(customPerson.getFirstName()+" "+customPerson.getLastName());
        Picasso.with(this)
                .load(customPerson.getPhoto())
                .transform(new CircleTransform())
                .into(profilePhoto);
    }
}
