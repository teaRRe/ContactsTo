package com.example.myapplication;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by TEARREAL on 2018/5/27.
 */

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

    private List<ContactsItem> mContacts;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View searchView;
        ImageView item_img;
        TextView item_name;
        TextView item_COUNT;
        public ViewHolder(View view){
            super(view);
            searchView = view;
            item_img = (ImageView) view.findViewById(R.id.item_img);
            item_name = (TextView) view.findViewById(R.id.item_name);
            item_COUNT = (TextView) view.findViewById(R.id.item_COUNT);
        }
    }

    public SearchAdapter(List<ContactsItem> list){
        mContacts = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ietms, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ContactsItem item = mContacts.get(position);
        try {
            holder.item_img.setImageURI(Uri.parse(item.getImg()));
        }catch (Exception e){
            e.printStackTrace();
        }
        holder.item_name.setText(item.getName());
        holder.item_COUNT.setText(item.getCOUNT());
    }

    @Override
    public int getItemCount() {
        return mContacts.size();
    }

}
