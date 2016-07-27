package com.fom.msesoft.fomapplication.adapter;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.model.CustomPerson;
import com.fom.msesoft.fomapplication.model.FriendRelationship;
import com.fom.msesoft.fomapplication.model.Person;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.androidannotations.annotations.Fullscreen;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import lombok.Setter;


public class DegreeViewHolders extends RecyclerView.ViewHolder {
    @Setter
    private CustomPerson person;

    @Setter
    private Person mePerson;

    RelativeLayout infoLayout;

    Dialog dialog;

    Button button ;

    ImageView infoClickView;

    boolean infoClick=true;


    public ImageView personPhoto;


    FriendRelationship friendRelationship;

    RestTemplate restTemplate;

    public DegreeViewHolders(final View itemView) {
        super(itemView);
        friendRelationship=new FriendRelationship();
        restTemplate=new RestTemplate();
        restTemplate.getMessageConverters().clear();
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        personPhoto = (ImageView)itemView.findViewById(R.id.personPhoto);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog = new Dialog(itemView.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogEnterAnimation;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.degree_dialog);
                // set the custom dialog components - text, image and button
                WindowManager wm = (WindowManager) (itemView.getContext()).getSystemService(Context.WINDOW_SERVICE);
                Display display = wm.getDefaultDisplay();
                DisplayMetrics metrics = new DisplayMetrics();
                display.getMetrics(metrics);
                int width = metrics.widthPixels;
                int height = metrics.heightPixels;

                infoLayout = (RelativeLayout)dialog.findViewById(R.id.infoLayout);
                infoLayout.setVisibility(View.GONE);
                infoLayout.getLayoutParams().height = height/2;
                infoLayout.getLayoutParams().width = width;

                TextView infoEmail = (TextView) dialog.findViewById(R.id.infoEmail);

                TextView infoName = (TextView) dialog.findViewById(R.id.infoName);

                ImageView infoImage = (ImageView) dialog.findViewById(R.id.infoImage);


                infoEmail.setText(person.getEmail());
                infoName.setText(person.getFirstName() + " " + person.getLastName());
                Picasso.with(itemView.getContext())
                        .load(person.getPhoto())
                        .resize(500,500)
                        .into(infoImage);

                final TextView text = (TextView) dialog.findViewById(R.id.dialogText);
                text.setText(person.getEmail());
                final ImageView image = (ImageView) dialog.findViewById(R.id.dialogImg);
                Picasso.with(itemView.getContext())
                        .load(person.getPhoto())
                        .resize(width,height/2)
                        .into(image);


                button = (Button)dialog.findViewById(R.id.addFriend);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        friendRelationship.setStartNode(person)
                                .setEndNode(mePerson)
                                .setFriendType("Facebook");
                        new FriendAdd().execute();
                        Toast.makeText(itemView.getContext(),"İstek gönderildi", Toast.LENGTH_LONG).show();
                    }

                });

                infoClickView = (ImageView) dialog.findViewById(R.id.infoClickView);
                infoClickView.setImageResource(R.drawable.ic_info_white_48dp);
                infoClickView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(infoClick == true){
                            image.setVisibility(View.GONE);
                            infoLayout.setVisibility(View.VISIBLE);
                            Picasso.with(itemView.getContext())
                                    .load(person.getPhoto())
                                    .transform(new CircleTransform())
                                    .into(infoClickView);
                            infoClick = false;
                        }
                        else {
                            image.setVisibility(View.VISIBLE);
                            infoLayout.setVisibility(View.GONE);
                            infoClickView.setImageResource(R.drawable.ic_info_white_48dp);
                            infoClick = true;
                        }
                    }
                });

                dialog.show();

            }


        });

    }
    private class FriendAdd extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            HttpEntity<FriendRelationship> requestEntity = new HttpEntity<FriendRelationship>(friendRelationship);
            restTemplate.exchange("http://fomserver.cloudapp.net:8081/friendRelationShip".concat("/saveFriend"), HttpMethod.POST, requestEntity, FriendRelationship.class).getBody();
            return null;
        }
    }
}
