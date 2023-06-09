package com.musicentertainment.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.musicentertainment.countrymusic.R;
import com.musicentertainment.item.ItemGenres;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AdapterGenresHome extends RecyclerView.Adapter<AdapterGenresHome.MyViewHolder> {

    private ArrayList<ItemGenres> arrayList;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        RoundedImageView imageView_artist;

        MyViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.tv_home_artist);
            imageView_artist = view.findViewById(R.id.iv_home_artist);
        }
    }

    public AdapterGenresHome(ArrayList<ItemGenres> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_home_artist, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.textView.setText(arrayList.get(position).getName());
        Picasso.get()
                .load(arrayList.get(position).getImage())
                .placeholder(R.drawable.placeholder_artist)
                .into(holder.imageView_artist);

    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}