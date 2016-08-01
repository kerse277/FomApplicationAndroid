package com.fom.msesoft.fomapplication.fragment;


import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.activity.MainActivity;
import com.fom.msesoft.fomapplication.adapter.DegreeViewAdapter;
import com.fom.msesoft.fomapplication.adapter.OnLoadMoreListener;
import com.fom.msesoft.fomapplication.model.CustomPerson;
import com.fom.msesoft.fomapplication.model.Person;
import com.fom.msesoft.fomapplication.repository.PersonRepository;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.friend_fragment)
public class DegreeFriend extends Fragment {

    DegreeViewAdapter mAdapter;

    int degree=2,skip=0;
    private GridLayoutManager lLayout;
    boolean isDegreeEnd=false;
    List<CustomPerson> itemsData = new ArrayList<>();
    List<CustomPerson> freshItemsData = new ArrayList<>();
    @ViewById(R.id.progress_bar)
    ProgressBar progressBar;

    @RestService
    PersonRepository personRepository;

    @ViewById(R.id.recyclerView)
    RecyclerView recyclerView;

    @TargetApi(Build.VERSION_CODES.M)
    @AfterViews
    void execute(){
        setHasOptionsMenu(true);
        listAll();
    }


    @Background
    void listRefresh(List<CustomPerson> itemsData, int degree,int skip){
        preRefreshExecute();
        CustomPerson[] persons = personRepository.findDegreeFriend(((MainActivity)getActivity()).getToken(),degree,skip);
        freshItemsData.clear();
        for(int i = 0 ;i<persons.length;i++){

            freshItemsData.add(persons[i]);
        }


        if(freshItemsData.size()<12){
            isDegreeEnd=true;
        }else {
            isDegreeEnd=false;
        }
        postRefreshExecute();

    }

    @Background
    void listAll(){
        preExecute();

        String token = ((MainActivity)getActivity()).getToken();

        CustomPerson[] persons = personRepository.findDegreeFriend(token,2,0);

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
    void preRefreshExecute(){
        itemsData.add(null);
        mAdapter.notifyItemInserted(itemsData.size() - 1);



    }


    @UiThread
    void postRefreshExecute(){
        itemsData.remove(itemsData.size() - 1);
        mAdapter.notifyItemRemoved(itemsData.size());

        itemsData.addAll(freshItemsData);

        mAdapter.notifyDataSetChanged();
        mAdapter.setLoaded();
    }

    @UiThread
    void postExecute(final List<CustomPerson> itemsData){

        lLayout = new GridLayoutManager(getActivity(),3);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(lLayout);

        progressBar.setVisibility(View.GONE);


        mAdapter = new DegreeViewAdapter(getActivity(),itemsData,recyclerView,((MainActivity)getActivity()).getToken());

        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {


                skip+=12;
                if(!isDegreeEnd){
                    listRefresh(itemsData,degree,skip);
                }else {
                    degree+=1;
                    skip=0;
                    listRefresh(itemsData,degree,skip);
                }


            }


        });

        recyclerView.setAdapter(mAdapter);

        recyclerView.setItemAnimator(new DefaultItemAnimator());


    }


    private List<Person> filter(List<Person> models, String query) {
        query = query.toLowerCase();

        final List<Person> filteredModelList = new ArrayList<>();
        for (Person model : models) {
            final String text = model.getFirstName().toLowerCase();
            if (text.contains(query)) {
                filteredModelList.add(model);
            }
        }
        return filteredModelList;
    }
}