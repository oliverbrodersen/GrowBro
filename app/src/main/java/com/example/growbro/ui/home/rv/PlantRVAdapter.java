package com.example.growbro.ui.home.rv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.growbro.Models.Plant;
import com.example.growbro.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PlantRVAdapter extends RecyclerView.Adapter<PlantRVAdapter.ViewHolder> {
    ArrayList<Plant> plantArrayList;
    final private OnListItemClickListener mOnListItemClickListener;
    boolean clickable;

    public PlantRVAdapter(OnListItemClickListener mOnListItemClickListener, boolean clickable) {
        this.mOnListItemClickListener = mOnListItemClickListener;
        this.clickable = clickable;
    }

    public void setPlantArrayList(ArrayList<Plant> plantArrayList) {
        this.plantArrayList = plantArrayList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_plants_in_greenhouse,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Plant plant = plantArrayList.get(position);
        holder.plantName.setText(plant.getName());
        Glide.with(holder.context)
                .load(plant.getImage())
                .into(holder.plantImage);
    }

    @Override
    public int getItemCount() {
        return plantArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView plantName;
        ImageView plantImage;
        Context context;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            plantName = itemView.findViewById(R.id.plantName);
            plantImage = itemView.findViewById(R.id.plantImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickable)
                mOnListItemClickListener.onPlantListItemClick(getAdapterPosition());
        }
    }
    public interface OnListItemClickListener {
        void onPlantListItemClick(int clickedItemIndex);
    }

}
