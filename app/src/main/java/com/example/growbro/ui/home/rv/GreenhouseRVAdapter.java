package com.example.growbro.ui.home.rv;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.growbro.Models.Data.SensorData;
import com.example.growbro.Models.Greenhouse;
import com.example.growbro.R;
import com.example.growbro.ui.Settings.SettingsActivity;
import com.example.growbro.util.Converter;
import com.google.android.material.chip.Chip;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GreenhouseRVAdapter extends RecyclerView.Adapter<GreenhouseRVAdapter.ViewHolder> {
    private ArrayList<Greenhouse> greenhouseArrayList;
    private HashMap<Integer, Integer> nextMeasurementMinutesByGreenhouseId;
    private HashMap<Integer, Integer> nextWaterMinutesByGreenhouseId;
    final private OnListItemClickListener mOnListItemClickListener;
    SharedPreferences sharedPreferences;
    private boolean fahrenheit;
    private String temperatureUnit;

    public GreenhouseRVAdapter(OnListItemClickListener mOnListItemClickListener, Context ctx) {
        this.mOnListItemClickListener = mOnListItemClickListener;
        greenhouseArrayList = new ArrayList<>();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(ctx);
        fahrenheit = sharedPreferences.getBoolean
                (SettingsActivity.KEY_PREF_FAHRENHEIT_SWITCH, false);

        if(fahrenheit)
            temperatureUnit = ctx.getString(R.string.fahrenheit);
        else
            temperatureUnit = ctx.getString(R.string.celsius);

    }

    public void setDataset(ArrayList<Greenhouse> greenhouseArrayList){
        this.greenhouseArrayList = greenhouseArrayList;
    }

    public void setNextMeasurementMinutesByGreenhouseId(HashMap<Integer, Integer> minutes) {
        nextMeasurementMinutesByGreenhouseId = minutes;
    }

    public void setNextWaterMinutesByGreenhouseId(HashMap<Integer, Integer> minutes) {
        nextWaterMinutesByGreenhouseId = minutes;
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
        holder.plantRVAdapter.setPlantArrayList(greenhouse.getPlants());
        holder.plantRV.setAdapter(holder.plantRVAdapter);

        holder.addPlantCard.setVisibility(View.GONE);
        if (position == greenhouseArrayList.size()-1){
            holder.addPlantCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NavOptions navOptions = new NavOptions.Builder()
                            .setPopUpTo(R.id.nav_home, true)
                            .build();

                    Navigation.findNavController(v).navigate(R.id.action_nav_home_to_editGreenhouseFragment, null, navOptions);
                }
            });
            holder.addPlantCard.setVisibility(View.VISIBLE);
        }

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
                                holder.thresholdCO2.setText(Math.round(greenhouse.getCo2Threshold().get(0)) + " - " + Math.round(greenhouse.getCo2Threshold().get(1)));
                                holder.accentCO2.setBackgroundResource(greenhouse.gethealthColor("co2"));
                                break;

                            case "temperature":
                                if (sensorData.getValue() % 1 == 0) {
                                    if(fahrenheit)
                                        holder.valueTemperature.setText((int) Converter.convertToFahrenheit(sensorData.getValue()) + temperatureUnit);
                                    else
                                        holder.valueTemperature.setText((int) sensorData.getValue() + temperatureUnit);
                                }
                                else {
                                    if(fahrenheit)
                                        holder.valueTemperature.setText(Converter.convertToFahrenheit(sensorData.getValue()) + temperatureUnit);
                                    else
                                        holder.valueTemperature.setText(sensorData.getValue() + temperatureUnit);
                                }
                                holder.valueTemperature.setAutoSizeTextTypeUniformWithConfiguration(6, 100, 1, TypedValue.COMPLEX_UNIT_DIP);
                                holder.thresholdTemperature.setText(Math.round(greenhouse.getTemperatureThreshold().get(0)) + "° - " + Math.round(greenhouse.getTemperatureThreshold().get(1)) + "°");
                                holder.accentTemperature.setBackgroundResource(greenhouse.gethealthColor("temperature"));
                                break;

                            case "humidity":
                                holder.valueHumidity.setText(((int)sensorData.getValue()) + "%");
                                holder.valueHumidity.setAutoSizeTextTypeUniformWithConfiguration(6, 100, 1, TypedValue.COMPLEX_UNIT_DIP);
                                holder.thresholdHumidity.setText(Math.round(greenhouse.getHumidityThreshold().get(0)) + "% - " + Math.round(greenhouse.getHumidityThreshold().get(1)) + "%");
                                holder.accentHumidity.setBackgroundResource(greenhouse.gethealthColor("humidity"));
                                break;
                        }
                    }
                }
            }
        });

        greenhouse.getMinutesToNextMeasurementLiveData().observeForever(new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                if (greenhouse.isTimeToRestartMeasurementTimer()) {

                    greenhouse.startCountDownTimerNextMeasurement();
                    greenhouse.setIsTimeToRestartMeasurementTimer(false);
                }

                if(integer < 0)
                    holder.nextMeasureValue.setText(R.string.awaiting_data);
                else
                    holder.nextMeasureValue.setText(integer+" minutes");
            }
        });
        greenhouse.startCountDownTimerNextMeasurement();

        greenhouse.getMinutesToNextWaterLiveData().observeForever(new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                if (greenhouse.isTimeToRestartWaterTimer()) {
                    greenhouse.startCountDownTimerNextWater();
                    greenhouse.setIsTimeToRestartWaterTimer(false);
                }

                if(integer < 0)
                    holder.nextWaterValue.setText(R.string.awaiting_data);
                else
                    holder.nextWaterValue.setText(integer+" minutes");
            }
        });
        greenhouse.startCountDownTimerNextWater();
    }


    @Override
    public int getItemCount() {
        return greenhouseArrayList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PlantRVAdapter.OnListItemClickListener{
        TextView greenhouseName;

        TextView valueTemperature;
        TextView valueHumidity;
        TextView valueCO2;
        TextView nextWaterValue;
        TextView nextMeasureValue;

        TextView accentTemperature;
        TextView accentHumidity;
        TextView accentCO2;

        TextView thresholdTemperature;
        TextView thresholdHumidity;
        TextView thresholdCO2;

        Chip windowIsOpen;
        RecyclerView plantRV;
        PlantRVAdapter plantRVAdapter;
        CardView addPlantCard;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            greenhouseName = itemView.findViewById(R.id.greenhouseName);
            plantRV = itemView.findViewById(R.id.plantRV);
            addPlantCard = itemView.findViewById(R.id.addCardView);
            valueTemperature = itemView.findViewById(R.id.valueTemperature);
            valueHumidity = itemView.findViewById(R.id.valueHumidity);
            valueCO2 = itemView.findViewById(R.id.valueCO2);
            windowIsOpen = itemView.findViewById(R.id.windowIsOpen);
            nextWaterValue = itemView.findViewById(R.id.next_water_value_list_item);
            nextMeasureValue = itemView.findViewById(R.id.next_measure_value_list_item);

            accentTemperature = itemView.findViewById(R.id.accentTemperature);
            accentHumidity = itemView.findViewById(R.id.accentHumidity);
            accentCO2 = itemView.findViewById(R.id.accentCO2);

            thresholdTemperature = itemView.findViewById(R.id.thresholdTemperature);
            thresholdHumidity = itemView.findViewById(R.id.thresholdHumidity);
            thresholdCO2 = itemView.findViewById(R.id.thresholdCO2);

            plantRV.hasFixedSize();
            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false);

            plantRV.setLayoutManager(layoutManager);
            plantRVAdapter = new PlantRVAdapter(this, false);
        }

        @Override
        public void onClick(View v) {
            mOnListItemClickListener.onListItemClick(getAdapterPosition());
        }

        @Override
        public void onPlantListItemClick(int clickedItemIndex) {
            //Unclickable
        }
    }

    public interface OnListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }
}
