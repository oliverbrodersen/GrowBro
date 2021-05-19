package com.example.growbro.ui.home.rv;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.growbro.Models.Data.SensorData;
import com.example.growbro.Models.Greenhouse;
import com.example.growbro.R;
import com.google.android.material.chip.Chip;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GreenhouseRVAdapter extends RecyclerView.Adapter<GreenhouseRVAdapter.ViewHolder> {

    private ArrayList<Greenhouse> greenhouseArrayList;

    public GreenhouseRVAdapter() {
    }

    public void setDataset(ArrayList<Greenhouse> greenhouseArrayList){
        this.greenhouseArrayList = greenhouseArrayList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_greenhouse_home,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        Greenhouse greenhouse = greenhouseArrayList.get(position);
        holder.greenhouseName.setText(greenhouse.getName());
        holder.plantRVAdapter.setPlantArrayList(greenhouse.getListPlants());
        holder.plantRV.setAdapter(holder.plantRVAdapter);

        if (greenhouse.isWindowIsOpen())
            holder.windowIsOpen.setVisibility(View.VISIBLE);
        else
            holder.windowIsOpen.setVisibility(View.GONE);

        greenhouse.getCurentLiveData().observeForever(new Observer<List<SensorData>>() {
            @Override
            public void onChanged(List<SensorData> data) {
                if (data != null) {
                    for (SensorData sensorData : data) {
                        switch (sensorData.getType().toLowerCase()) {
                            case "co2":
                                holder.valueCO2.setText(((int)sensorData.getValue()) + "");
                                holder.valueCO2.setAutoSizeTextTypeUniformWithConfiguration(6, 100, 1, TypedValue.COMPLEX_UNIT_DIP);
                                break;
                            case "temperature":
                                if (sensorData.getValue() % 1 == 0)
                                    holder.valueTemperature.setText((int)sensorData.getValue() + "°");
                                else
                                    holder.valueTemperature.setText(sensorData.getValue() + "°");
                                holder.valueTemperature.setAutoSizeTextTypeUniformWithConfiguration(6, 100, 1, TypedValue.COMPLEX_UNIT_DIP);
                                break;
                            case "humidity":
                                holder.valueHumidity.setText(((int)sensorData.getValue()) + "%");
                                holder.valueHumidity.setAutoSizeTextTypeUniformWithConfiguration(6, 100, 1, TypedValue.COMPLEX_UNIT_DIP);
                                break;
                        }
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return greenhouseArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView greenhouseName;

        TextView valueTemperature;
        TextView valueHumidity;
        TextView valueCO2;

        Chip windowIsOpen;
        RecyclerView plantRV;
        PlantRVAdapter plantRVAdapter;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            greenhouseName = itemView.findViewById(R.id.greenhouseName);
            plantRV = itemView.findViewById(R.id.plantRV);
            valueTemperature = itemView.findViewById(R.id.valueTemperature);
            valueHumidity = itemView.findViewById(R.id.valueHumidity);
            valueCO2 = itemView.findViewById(R.id.valueCO2);
            windowIsOpen = itemView.findViewById(R.id.windowIsOpen);

            plantRV.hasFixedSize();
            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);

            plantRV.setLayoutManager(layoutManager);
            plantRVAdapter = new PlantRVAdapter();
        }
    }
}