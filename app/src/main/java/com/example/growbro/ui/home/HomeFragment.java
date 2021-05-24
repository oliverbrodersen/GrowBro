package com.example.growbro.ui.home;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.navigation.NavDirections;

import com.example.growbro.Models.Data.SensorData;
import com.example.growbro.Models.Greenhouse;
import com.example.growbro.R;
import com.example.growbro.ui.signin.SignInActivity;
import com.example.growbro.ui.home.rv.GreenhouseRVAdapter;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView greenhouseRV;
    private GreenhouseRVAdapter greenhouseRVAdapter;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        greenhouseRV = root.findViewById(R.id.greenhouseListRV);
        greenhouseRV.hasFixedSize();
        greenhouseRV.setLayoutManager(new LinearLayoutManager(getContext()));

        greenhouseRVAdapter = new GreenhouseRVAdapter();
        greenhouseRV.setAdapter(greenhouseRVAdapter);

        //Binds current sensor data to view
        homeViewModel.getGreenhouseListAsLiveData().observe(getViewLifecycleOwner(), new Observer<List<Greenhouse>>() {
            @Override
            public void onChanged(List<Greenhouse> data) {

                greenhouseRVAdapter.setDataset((ArrayList<Greenhouse>) data);
                greenhouseRV.setAdapter(greenhouseRVAdapter);
                //if (data != null) {
                //    for (SensorData sensorData : data) {
                //        switch (sensorData.getType().toLowerCase()) {
                //            case "co2":
                //                co2TextView.setText(((int)sensorData.getData()) + "");
                //                co2TextView.setAutoSizeTextTypeUniformWithConfiguration(6, 100, 1, TypedValue.COMPLEX_UNIT_DIP);
                //                break;
                //            case "temperature":
                //                temperatureTextView.setText(sensorData.getData() + "°");
                //                temperatureTextView.setAutoSizeTextTypeUniformWithConfiguration(6, 100, 1, TypedValue.COMPLEX_UNIT_DIP);
                //                break;
                //            case "humidity":
                //                humidityTextView.setText(((int)sensorData.getData()) + "%");
                //                humidityTextView.setAutoSizeTextTypeUniformWithConfiguration(6, 100, 1, TypedValue.COMPLEX_UNIT_DIP);
                //                break;
                //        }
                //    }
                //}
            }
        });
        return root;
    }
}