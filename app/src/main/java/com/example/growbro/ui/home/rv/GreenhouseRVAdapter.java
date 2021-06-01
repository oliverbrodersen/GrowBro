package com.example.growbro.ui.home.rv;

import android.content.Context;
import android.content.SharedPreferences;
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
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.growbro.Models.Data.SensorData;
import com.example.growbro.Models.Greenhouse;
import com.example.growbro.R;
import com.example.growbro.Settings.SettingsActivity;
import com.example.growbro.ui.home.HomeViewModel;
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

        //System.out.println("#TTT: TEST after merge: GreenhouseRVAdapter constructor: unit: " + unit);
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

                                if(sensorData.getValue() < greenhouse.getCo2Threshold().get(0) || sensorData.getValue() > greenhouse.getCo2Threshold().get(1))
                                    holder.accentC02.setBackgroundResource(R.color.criticalHealth);
                                else
                                    holder.accentC02.setBackgroundResource(R.color.goodHealth);



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

                                if(sensorData.getValue() < greenhouse.getTemperatureThreshold().get(0) || sensorData.getValue() > greenhouse.getTemperatureThreshold().get(1))
                                    holder.accentTemperature.setBackgroundResource(R.color.criticalHealth);
                                else
                                    holder.accentTemperature.setBackgroundResource(R.color.goodHealth);

                                break;
                            case "humidity":
                                holder.valueHumidity.setText(((int)sensorData.getValue()) + "%");
                                holder.valueHumidity.setAutoSizeTextTypeUniformWithConfiguration(6, 100, 1, TypedValue.COMPLEX_UNIT_DIP);

                                if(sensorData.getValue() < greenhouse.getHumidityThreshold().get(0) || sensorData.getValue() > greenhouse.getHumidityThreshold().get(1))
                                    holder.accentHumidity.setBackgroundResource(R.color.criticalHealth);
                                else
                                    holder.accentHumidity.setBackgroundResource(R.color.goodHealth);

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
        TextView accentC02;

        Chip windowIsOpen;
        RecyclerView plantRV;
        PlantRVAdapter plantRVAdapter;
        CardView addPlantCard;

        HomeViewModel homeViewModel;

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
            accentC02 = itemView.findViewById(R.id.accentCO2);

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
