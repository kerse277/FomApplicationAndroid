package com.fom.msesoft.fomapplication.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.fragment.ProfileSettingsData;

/**
 * Created by oguz on 15.07.2016.
 */
public class ProfileSettingsAdapter extends RecyclerView.Adapter<ProfileSettingsAdapter.ViewHolder> {
    private ProfileSettingsData[] profileSettingsDatas;

    public  ProfileSettingsAdapter(ProfileSettingsData[] profileSettingsDatas) {
        this.profileSettingsDatas = profileSettingsDatas;
    }
    @Override
    public ProfileSettingsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.profile_item_layout, null);

        // create ViewHolder

        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        // - get data from your itemsData at this position
        // - replace the contents of the view with that itemsData

        viewHolder.txtViewTitle.setText(profileSettingsDatas[position].getTitle());
        viewHolder.imgViewIcon.setImageResource(profileSettingsDatas[position].getImageUrl());


    }

    // inner class to hold a reference to each item of RecyclerView
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtViewTitle;
        public ImageView imgViewIcon;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.item);
            imgViewIcon = (ImageView) itemLayoutView.findViewById(R.id.item_icon);
        }
    }


    // Return the size of your itemsData (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return profileSettingsDatas.length;
    }
}
