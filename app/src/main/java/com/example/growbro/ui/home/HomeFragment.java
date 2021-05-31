package com.example.growbro.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.growbro.Models.Greenhouse;
import com.example.growbro.R;
import com.example.growbro.ui.home.rv.GreenhouseRVAdapter;
import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HomeFragment extends Fragment implements GreenhouseRVAdapter.OnListItemClickListener {

    private HomeViewModel homeViewModel;
    private RecyclerView greenhouseRV;
    private GreenhouseRVAdapter greenhouseRVAdapter;
    private TextView showingTextView;
    private static String MY_GROWBROS = "Showing my GrowBros", FRIENDS_GROWBROS = "Showing friends GrowBros";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        homeViewModel.getGreenhouseListFromApi();

        Chip myGrowbrosButton = root.findViewById(R.id.myGreenhousesChip);
        CardView addCardView = root.findViewById(R.id.addCardView);
        Chip friendsGrowbrosButton = root.findViewById(R.id.friendsGreenhouses);
        showingTextView = root.findViewById(R.id.showingTextView);

        greenhouseRV = root.findViewById(R.id.greenhouseListRV);
        greenhouseRV.setLayoutManager(new LinearLayoutManager(getContext()));

        greenhouseRVAdapter = new GreenhouseRVAdapter(this);
        greenhouseRV.setAdapter(greenhouseRVAdapter);

        //Binds current sensor data to view
        homeViewModel.getGreenhouseListAsLiveData().observe(getViewLifecycleOwner(), new Observer<List<Greenhouse>>() {
            @Override
            public void onChanged(List<Greenhouse> data) {

                greenhouseRVAdapter.setDataset((ArrayList<Greenhouse>) data);
                greenhouseRV.setAdapter(greenhouseRVAdapter);

                homeViewModel.getMinutesToNextMeasurement().observe(getViewLifecycleOwner(), new Observer<HashMap<Integer, Integer>>() {
                    @Override
                    public void onChanged(HashMap<Integer, Integer> data) {
                        greenhouseRVAdapter.setNextMeasurementMinutesByGreenhouseId((HashMap<Integer, Integer>) data);


                    }
                });

                homeViewModel.getMinutesToNextWater().observe(getViewLifecycleOwner(), new Observer<HashMap<Integer, Integer>>() {
                    @Override
                    public void onChanged(HashMap<Integer, Integer> data) {
                        greenhouseRVAdapter.setNextWaterMinutesByGreenhouseId((HashMap<Integer, Integer>) data);
                    }
                });



            }
        });

        //Set OnClickListeners
        myGrowbrosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showingTextView.getText().equals(FRIENDS_GROWBROS)){
                    showingTextView.setText(MY_GROWBROS);

                    greenhouseRVAdapter.setDataset(new ArrayList<Greenhouse>());
                    greenhouseRV.setAdapter(greenhouseRVAdapter);

                    //replace recycler
                    homeViewModel.getGreenhouseListAsLiveData().observe(getViewLifecycleOwner(), new Observer<List<Greenhouse>>() {
                        @Override
                        public void onChanged(List<Greenhouse> greenhouses) {
                            greenhouseRVAdapter.setDataset((ArrayList<Greenhouse>) greenhouses);
                            greenhouseRV.setAdapter(greenhouseRVAdapter);
                        }
                    });
                }
            }
        });
        friendsGrowbrosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (showingTextView.getText().equals(MY_GROWBROS)){
                    showingTextView.setText(FRIENDS_GROWBROS);
                    greenhouseRVAdapter.setDataset(new ArrayList<Greenhouse>());
                    greenhouseRV.setAdapter(greenhouseRVAdapter);

                    //replace recycler
                    homeViewModel.getFriendsGreenhouseList().observe(getViewLifecycleOwner(), new Observer<List<Greenhouse>>() {
                        @Override
                        public void onChanged(List<Greenhouse> greenhouses) {
                            greenhouseRVAdapter.setDataset((ArrayList<Greenhouse>) greenhouses);
                            greenhouseRV.setAdapter(greenhouseRVAdapter);
                        }
                    });
                }
            }
        });

        return root;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        NavOptions navOptions = new NavOptions.Builder()
                .setPopUpTo(R.id.nav_home, true)
                .build();
        Bundle bundle = new Bundle();
        List<Greenhouse> greenhouseList;
        if (showingTextView.getText().equals(MY_GROWBROS))
            greenhouseList = homeViewModel.getGreenhouseList();
        else
            greenhouseList = homeViewModel.getFriendsGreenhouseList().getValue();
        bundle.putString("selectedGreenhouseId", greenhouseList.get(clickedItemIndex).getId()+"");
        Navigation.findNavController(getView()).navigate(R.id.action_nav_home_to_greenhouseTabFragment, bundle, navOptions);
    }
}