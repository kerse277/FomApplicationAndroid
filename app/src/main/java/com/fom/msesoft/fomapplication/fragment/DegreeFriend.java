package com.fom.msesoft.fomapplication.fragment;


import android.annotation.TargetApi;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.activity.MainActivity;
import com.fom.msesoft.fomapplication.activity.MainActivity_;
import com.fom.msesoft.fomapplication.adapter.CircleTransform;
import com.fom.msesoft.fomapplication.adapter.DegreeViewAdapter;
import com.fom.msesoft.fomapplication.adapter.OnLoadMoreListener;
import com.fom.msesoft.fomapplication.model.Person;
import com.fom.msesoft.fomapplication.repository.PersonRepository;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ItemClick;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

import java.util.ArrayList;
import java.util.List;

@EFragment(R.layout.friend_fragment)
public class DegreeFriend extends Fragment {

    DegreeViewAdapter mAdapter;

    int limitSize=20,degree=2;
    private GridLayoutManager lLayout;
    List<Person> itemsData = new ArrayList<>();
    List<Person> freshItemsData = new ArrayList<>();

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
    void listRefresh(List<Person> itemsData,int degree,int limit){
        preRefreshExecute();

        Person[] persons = personRepository.findDegreeFriend(((MainActivity)getActivity()).getPerson().getUniqueId(),String.valueOf(degree).toString(),String.valueOf(limit).toString());
        freshItemsData.clear();
        for(int i = 0 ;i<persons.length;i++){

            freshItemsData.add(persons[i]);
        }
        itemsData.addAll(freshItemsData.subList(limit-21,limit-1));
        postRefreshExecute();

    }

    @Background
    void listAll(){
        preExecute();


        Person[] persons = personRepository.findDegreeFriend(((MainActivity)getActivity()).getPerson().getUniqueId(),"2","20");

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

        itemsData.remove(itemsData.size() - 1);
        mAdapter.notifyItemRemoved(itemsData.size());

    }


    @UiThread
    void postRefreshExecute(){

        mAdapter.notifyDataSetChanged();
        mAdapter.setLoaded();
    }

    @UiThread
    void postExecute(final List<Person> itemsData){

        lLayout = new GridLayoutManager(getActivity(),3);

        recyclerView.setHasFixedSize(true);

        recyclerView.setLayoutManager(lLayout);

        progressBar.setVisibility(View.GONE);


        mAdapter = new DegreeViewAdapter(getActivity(),itemsData,recyclerView,((MainActivity)getActivity()).getPerson());

        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                limitSize+=20;
                if(itemsData.size()>=limitSize-20){
                    listRefresh(itemsData,degree,limitSize);
                }else {
                    limitSize=20;
                    degree+=1;
                    listRefresh(itemsData,degree,limitSize);
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