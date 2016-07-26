package com.fom.msesoft.fomapplication.adapter;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.model.Person;
import com.squareup.picasso.Picasso;

import lombok.Setter;

public class FriendViewHolders extends RecyclerView.ViewHolder {

    @Setter
    private Person person;

    public ImageView personPhoto;

    public  TextView nameTxt;

    public FriendViewHolders(final View itemView) {
        super(itemView);
        nameTxt = (TextView) itemView.findViewById(R.id.nameTxt);
        personPhoto = (ImageView)itemView.findViewById(R.id.personPhoto);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(itemView.getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().getAttributes().windowAnimations = R.style.DialogEnterAnimation;
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                dialog.setContentView(R.layout.friend_diaolog);
                // set the custom dialog components - text, image and button
                ImageView image = (ImageView) dialog.findViewById(R.id.dialogImg);
                Picasso.with(itemView.getContext())
                        .load(person.getPhoto())
                        .resize(500,500)
                        .into(image);
                dialog.show();

            }
        });
    }
}
