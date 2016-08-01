package com.fom.msesoft.fomapplication.adapter;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.extras.CircleTransform;
import com.fom.msesoft.fomapplication.extras.Dondurme3dAnimasyon;
import com.fom.msesoft.fomapplication.model.CustomPerson;
import com.fom.msesoft.fomapplication.model.FriendRelationship;
import com.squareup.picasso.Picasso;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

import lombok.Setter;


public class DegreeViewHolders extends RecyclerView.ViewHolder {
    @Setter
    private CustomPerson person;

    @Setter
    private CustomPerson mePerson;

    @Setter
    private String token;

    RelativeLayout infoLayout;

    Handler handler;


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


                        new GetCustomPerson().execute();
                        Toast.makeText(itemView.getContext(),"İstek gönderildi", Toast.LENGTH_LONG).show();
                    }

                });
                final RelativeLayout animLayout = (RelativeLayout)dialog.findViewById(R.id.animLayout);
                final RelativeLayout animLayout2 = (RelativeLayout)dialog.findViewById(R.id.animLayout2);
                infoClickView = (ImageView) dialog.findViewById(R.id.infoClickView);
                infoClickView.setImageResource(R.drawable.ic_info_white_48dp);
                handler = new Handler();
                infoClickView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(infoClick == true){

                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    image.setVisibility(View.GONE);
                                    infoLayout.setVisibility(View.VISIBLE);
                                    Picasso.with(itemView.getContext())
                                            .load(person.getPhoto())
                                            .transform(new CircleTransform())
                                            .into(infoClickView);
                                    infoClick = false;
                                }
                            }, 300);

                            applyRotation(animLayout);
                            applyRotation(animLayout2);

                        }
                        else {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    image.setVisibility(View.VISIBLE);
                                    infoLayout.setVisibility(View.GONE);
                                    infoClickView.setImageResource(R.drawable.ic_info_white_48dp);
                                    infoClick = true;
                                }
                            }, 300);

                            applyRotation(animLayout);
                            applyRotation(animLayout2);
                        }
                    }
                });

                dialog.show();

            }


        });

    }
    private void applyRotation(RelativeLayout relativeLayout)
    {
        final Dondurme3dAnimasyon rotation = new Dondurme3dAnimasyon(relativeLayout);
        rotation.applyPropertiesInRotation();
        relativeLayout.startAnimation(rotation);
    }


    private class GetCustomPerson extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            HashMap<String, Object> urlVariables = new HashMap<String, Object>();
            urlVariables.put("token", token);
            mePerson=restTemplate.exchange("http://192.168.2.120:8081/person".concat("/findByToken?token={token}"), HttpMethod.GET, null, CustomPerson.class, urlVariables).getBody();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            new FriendAdd().execute();
        }
    }

    private class FriendAdd extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            HashMap<String, Object> urlVariables = new HashMap<String, Object>();
            urlVariables.put("token", token);
            urlVariables.put("uniqueId",person.getUniqueId());
            restTemplate.exchange("http://192.168.2.120:8081/friendRelationShip".concat("/gcmAddFriendNTF?friendAdder={token}&friendAdded={uniqueId}"), HttpMethod.GET, null, CustomPerson.class, urlVariables).getBody();
            return null;
        }
    }
}
