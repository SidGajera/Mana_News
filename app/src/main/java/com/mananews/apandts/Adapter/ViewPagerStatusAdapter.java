package com.mananews.apandts.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.mananews.apandts.Model_Class.StatusModel.Datum;
import com.mananews.apandts.OnItemClickListner;
import com.mananews.apandts.R;
import com.mananews.apandts.databinding.ItemStatusCellBinding;

import java.util.ArrayList;

public class ViewPagerStatusAdapter extends RecyclerView.Adapter<ViewPagerStatusAdapter.ViewHolder> {

    public Activity activity;
    ArrayList<Datum> newsArrayList;
    private OnItemClickListner onItemClickListners;

    public ViewPagerStatusAdapter(Activity activity, ArrayList<Datum> imageList, OnItemClickListner onItemClickListner) {
        this.activity = activity;
        this.newsArrayList = imageList;
        this.onItemClickListners = onItemClickListner;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemStatusCellBinding.inflate(activity.getLayoutInflater(), parent, false));
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        final Datum item = newsArrayList.get(position);
        String imagePath = activity.getString(R.string.server_url);
        String image = item.getImage().split("\\.\\.")[1];
        Log.e("FATZ", "Image: " + imagePath + image);
        Glide.with(activity).load(imagePath + image).placeholder(Color.WHITE).into(holder.binding.imageview);
        holder.binding.txtDescription.setText(item.getName());
        holder.binding.imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListners.onItemClick(imagePath + image, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ItemStatusCellBinding binding;

        public ViewHolder(@NonNull ItemStatusCellBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
        }
    }
}
