package com.example.growbro.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.growbro.Models.Greenhouse;
import com.example.growbro.R;
import com.example.growbro.ui.greenhousetab.GreenhouseTabFragment;
import com.example.growbro.ui.greenhousetab.greenhouse.GreenhouseFragment;
import com.example.growbro.ui.home.rv.GreenhouseRVAdapter;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements GreenhouseRVAdapter.OnListItemClickListener {

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

        greenhouseRVAdapter = new GreenhouseRVAdapter(this);
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

    @Override
    public void onListItemClick(int clickedItemIndex) {
        GreenhouseTabFragment greenhouseTabFragment = new GreenhouseTabFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        Bundle bundle = new Bundle();
        List<Greenhouse> greenhouseList = homeViewModel.getGreenhouseList();
        bundle.putString("greenhouseId", greenhouseList.get(clickedItemIndex).getId()+"");
        greenhouseTabFragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, greenhouseTabFragment).addToBackStack("TAG").commit();
    }
}