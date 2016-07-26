package com.fom.msesoft.fomapplication.fragment;

import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.activity.MainActivity;
import com.fom.msesoft.fomapplication.model.Person;
import com.fom.msesoft.fomapplication.model.Places;
import com.fom.msesoft.fomapplication.repository.PersonRepository;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@EFragment(R.layout.profile_photo_fragment)
public class ProfileFragmentPhoto extends Fragment {



}
