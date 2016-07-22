package com.fom.msesoft.fomapplication.adapter;
import android.app.Dialog;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.model.FriendRelationship;
import com.fom.msesoft.fomapplication.model.Person;
import com.squareup.picasso.Picasso;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import lombok.Setter;


public class DegreeViewHolders extends RecyclerView.ViewHolder {


    @Setter
    private Person person;
    @Setter
    private Person mePerson;

    Button button ;
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
                Dialog dialog = new Dialog(itemView.getContext());
                dialog.setContentView(R.layout.degree_dialog);

                dialog.getWindow().getAttributes().windowAnimations = R.style.CornerAnim;
                // set the custom dialog components - text, image and button
                TextView text = (TextView) dialog.findViewById(R.id.dialogText);
                text.setText(person.getEmail());


                ImageView image = (ImageView) dialog.findViewById(R.id.dialogImg);
                Picasso.with(itemView.getContext())
                        .load(person.getPhoto())
                        .resize(500,500)
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
                dialog.show();
            }
        });
    }

private class FriendAdd extends AsyncTask<Void,Void,Void>{


    @Override
    protected Void doInBackground(Void... voids) {

        HttpEntity<FriendRelationship> requestEntity = new HttpEntity<FriendRelationship>(friendRelationship);
        restTemplate.exchange("http://192.168.2.120:8081/friendRelationShip".concat("/saveFriend"), HttpMethod.POST, requestEntity, FriendRelationship.class).getBody();
        return null;
    }
}


}
