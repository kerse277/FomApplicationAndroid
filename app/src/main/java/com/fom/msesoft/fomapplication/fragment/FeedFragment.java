package com.fom.msesoft.fomapplication.fragment;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.util.Base64;

import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.model.Person;
import com.fom.msesoft.fomapplication.repository.PersonRepository;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.rest.spring.annotations.RestService;

import java.io.ByteArrayOutputStream;

@EFragment(R.layout.feed_fragment)
public class FeedFragment extends Fragment {

    @RestService
    PersonRepository personRepository;

    @Click(R.id.uploadPhotoButton)
    void upload(){
        upPhoto();
    }

    @Background
    void upPhoto(){
        Resources resources = this.getResources();
        Bitmap bitmap= BitmapFactory.decodeResource(resources , R.mipmap.ic_launcher);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] image = stream.toByteArray();
        byte[] encodedImage = Base64.encode(image, Base64.DEFAULT);

        personRepository.uploadPhoto(encodedImage);
    }
}