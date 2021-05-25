package com.example.growbro.ui.greenhousetab.greenhouse;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.growbro.Models.Data.SensorData;
import com.example.growbro.Models.Greenhouse;
import com.example.growbro.R;
import com.example.growbro.ui.home.rv.PlantRVAdapter;

import java.util.List;

public class GreenhouseFragment extends Fragment {

    public static final String ARG_SELECTED_GREENHOUSE_ID = "selectedGreenhouseId";
    public Greenhouse greenhouse;
    private String selectedGreenhouseId;
    private RecyclerView plantRV;
    private PlantRVAdapter plantRVAdapter;

    private GreenhouseViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        selectedGreenhouseId = args.getString(ARG_SELECTED_GREENHOUSE_ID);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.greenhouse_fragment, container, false);
        mViewModel = new ViewModelProvider(this).get(GreenhouseViewModel.class);
        greenhouse = mViewModel.getGreenhouseFromId(Integer.parseInt(selectedGreenhouseId));

        TextView name = root.findViewById(R.id.textViewName);
        TextView valueCO2 = root.findViewById(R.id.valueCO2);
        TextView valueHumidity = root.findViewById(R.id.valueHumidity);
        TextView valueTemperature = root.findViewById(R.id.valueTemperature);
        TextView nextMeasureValue = root.findViewById(R.id.next_measure_value_greenhouse_fragment);
        TextView nextWaterValue = root.findViewById(R.id.next_water_value_greenhouse_fragment);
        plantRV = root.findViewById(R.id.plantRVgreenhouse);
        plantRV.hasFixedSize();
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(root.getContext(), LinearLayoutManager.HORIZONTAL, false);

        plantRV.setLayoutManager(layoutManager);
        plantRVAdapter = new PlantRVAdapter();

        plantRVAdapter.setPlantArrayList(greenhouse.getListPlants());
        plantRV.setAdapter(plantRVAdapter);

        greenhouse.getCurentLiveData().observeForever(new Observer<List<SensorData>>() {
            @Override
            public void onChanged(List<SensorData> data) {
                if (data != null) {
                    for (SensorData sensorData : data) {
                        switch (sensorData.getType().toLowerCase()) {
                            case "co2":
                                valueCO2.setText(((int)sensorData.getValue()) + "");
                                valueCO2.setAutoSizeTextTypeUniformWithConfiguration(6, 100, 1, TypedValue.COMPLEX_UNIT_DIP);
                                break;
                            case "temperature":
                                if (sensorData.getValue() % 1 == 0)
                                    valueTemperature.setText((int)sensorData.getValue() + "°");
                                else
                                    valueTemperature.setText(sensorData.getValue() + "°");
                                valueTemperature.setAutoSizeTextTypeUniformWithConfiguration(6, 100, 1, TypedValue.COMPLEX_UNIT_DIP);
                                break;
                            case "humidity":
                                valueHumidity.setText(((int)sensorData.getValue()) + "%");
                                valueHumidity.setAutoSizeTextTypeUniformWithConfiguration(6, 100, 1, TypedValue.COMPLEX_UNIT_DIP);
                                break;
                        }
                    }
                }
            }
        });

        greenhouse.getMinutesToNextMeasurementLiveData().observeForever(new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                nextMeasureValue.setText(integer+" minutes");
                nextWaterValue.setText(integer+" minutes");
            }
        });
        greenhouse.startCountDownTimer();

        name.setText(greenhouse.getName());
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}