package com.fom.msesoft.fomapplication.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.adapter.FriendViewAdapter;
import com.fom.msesoft.fomapplication.model.Person;
import com.fom.msesoft.fomapplication.repository.PersonRepository;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_friend_list)
@Fullscreen
public class FriendList extends AppCompatActivity {

    @RestService
    PersonRepository personRepository;

    @ViewById(R.id.recyclerView)
    RecyclerView recyclerView;

    @ViewById(R.id.friend_progress_bar)
    ProgressBar progressBar;

    @AfterViews
    void excute(){
        listAll();
    }

    ProgressDialog progressDialog;

    private GridLayoutManager lLayout;

    @Background
    void listAll(){
        preExecute();
        List<Person> itemsData=new ArrayList<>();


        Intent intent = getIntent();

        Person[] persons = personRepository.findByFirstDegreeFriend(((Person)intent.getSerializableExtra("Person")).getUniqueId());

        for(int i = 0 ;i<persons.length;i++){
            itemsData.add(persons[i]);
        }

        postExecute(itemsData);

    }

    @UiThread
    void preExecute(){
        progressBar.setVisibility(View.VISIBLE);

    }

    @UiThread
    void postExecute(final List<Person> itemsData){

        progressBar.setVisibility(View.GONE);

        lLayout = new GridLayoutManager(this,1);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(lLayout);


        final FriendViewAdapter mAdapter = new FriendViewAdapter(this,itemsData,recyclerView);


        recyclerView.setAdapter(mAdapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());


    }
}
