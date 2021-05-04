package com.example.growbro.ui.home;

import android.os.Bundle;
import android.text.Html;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.growbro.Models.Data.SensorData;
import com.example.growbro.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        final TextView co2TextView = root.findViewById(R.id.tvCo2);
        final TextView humidityTextView = root.findViewById(R.id.tvHumidity);
        final TextView temperatureTextView = root.findViewById(R.id.tvTemp);
        final TextView minutesLeft = root.findViewById(R.id.minutesLeftTV);
        final Button button = root.findViewById(R.id.bSendData);

        //start countdown timer
        homeViewModel.startTimerToNextMeasurement(1);

        //Observe timer and bind to view
        homeViewModel.getMinutesToNextMeasurement().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                minutesLeft.setText(s);
            }
        });

        button.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                    {
                        homeViewModel.waterNow(0,1);
                    }
            });

        //Binds current sensor data to view
        homeViewModel.getLiveData(1).observe(getViewLifecycleOwner(), new Observer<List<SensorData>>() {
            @Override
            public void onChanged(List<SensorData> data) {
                if (data != null) {
                    for (SensorData sensorData : data) {
                        switch (sensorData.getType()) {
                            case "CO2":
                                co2TextView.setText(((int)sensorData.getValue()) + "");
                                co2TextView.setAutoSizeTextTypeUniformWithConfiguration(6, 100, 1, TypedValue.COMPLEX_UNIT_DIP);
                                break;
                            case "Temperature":
                                temperatureTextView.setText(sensorData.getValue() + "°");
                                temperatureTextView.setAutoSizeTextTypeUniformWithConfiguration(6, 100, 1, TypedValue.COMPLEX_UNIT_DIP);
                                break;
                            case "Humidity":
                                humidityTextView.setText(((int)sensorData.getValue()) + "%");
                                humidityTextView.setAutoSizeTextTypeUniformWithConfiguration(6, 100, 1, TypedValue.COMPLEX_UNIT_DIP);
                                break;
                        }
                    }
                }
            }
        });
        return root;
    }
}