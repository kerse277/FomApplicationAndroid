package com.fom.msesoft.fomapplication.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fom.msesoft.fomapplication.R;
import com.fom.msesoft.fomapplication.model.Person;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FriendViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private List<Person> itemList;
    private Context context;

    private Person person;
    private int position;

    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private OnLoadMoreListener mOnLoadMoreListener;

    private boolean isLoading;
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;


    public FriendViewAdapter(Context context, List<Person> itemList, RecyclerView recyclerView) {
        this.itemList = itemList;
        this.context = context;
        this.person = person;
        final GridLayoutManager gridLayoutManager = (GridLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = gridLayoutManager.getItemCount();
                lastVisibleItem = gridLayoutManager.findLastVisibleItemPosition();

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (mOnLoadMoreListener != null) {
                        mOnLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    @Override
    public int getItemViewType(int position) {
        return itemList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend_listrow, null);
            return new FriendViewHolders(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(context).inflate(R.layout.layout_loading_item, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        this.position = position;
            FriendViewHolders userViewHolder = (FriendViewHolders) holder;
            userViewHolder.setPerson(itemList.get(position));
            userViewHolder.nameTxt.setText(itemList.get(position).getFirstName() + " " + itemList.get(position).getLastName());
            Picasso.with(context)
                    .load(itemList.get(position).getPhoto())
                    .transform(new CircleTransform())
                    .into(userViewHolder.personPhoto);
                    //.transform(new CircleTransform())
    }

    public void addFriend () {
        Person addPerson;
        addPerson = itemList.get(position);

    }


    @Override
    public int getItemCount() {
        return itemList == null ? 0 : itemList.size();
    }

    public void setLoaded() {
        isLoading = false;
    }
}
