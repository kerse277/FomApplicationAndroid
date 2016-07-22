package com.fom.msesoft.fomapplication.fragment;


import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.activity.MainActivity;
import com.fom.msesoft.fomapplication.adapter.ProfileSettingsAdapter;
import com.fom.msesoft.fomapplication.model.Person;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

@EFragment(R.layout.profile_settings_fragment)
public class ProfileFragmentSettings extends Fragment{

    @ViewById(R.id.recyclerView)
    RecyclerView recyclerView;

    @AfterViews
    void afterViews() {

        Person person = ((MainActivity)getActivity()).getPerson();
        ProfileSettingsData profileSettingsData[] = {
                new ProfileSettingsData(person.getFirstName()+" "+person.getLastName(),R.drawable.ic_account_circle_black_24dp),
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
