package com.example.growbro.ui.greenhousetab.greenhouse;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.growbro.Models.Data.SensorData;
import com.example.growbro.Models.Greenhouse;
import com.example.growbro.R;
import com.example.growbro.ui.Settings.SettingsActivity;
import com.example.growbro.ui.greenhousetab.greenhouse.rv.SharedRVAdapter;
import com.example.growbro.ui.home.rv.PlantRVAdapter;
import com.example.growbro.util.Converter;
import com.google.android.material.chip.Chip;

import java.util.List;

public class GreenhouseFragment extends Fragment implements SharedRVAdapter.OnListItemClickListener, PlantRVAdapter.OnListItemClickListener {

    public static final String ARG_SELECTED_GREENHOUSE_ID = "selectedGreenhouseId";
    public Greenhouse greenhouse;
    private String selectedGreenhouseId;
    private String temperatureUnit;
    private RecyclerView plantRV;
    private PlantRVAdapter plantRVAdapter;
    private RecyclerView sharedRV;
    private SharedRVAdapter sharedRVAdapter;

    private GreenhouseViewModel mViewModel;
    private android.preference.PreferenceManager PreferenceManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        selectedGreenhouseId = args.getString(ARG_SELECTED_GREENHOUSE_ID);
        temperatureUnit = getString(R.string.celsius); //default
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
        TextView sharedHeading = root.findViewById(R.id.textView5);
        TextView accentTemperature = root.findViewById(R.id.accentTemperature);
        TextView accentHumidity = root.findViewById(R.id.accentHumidity);
        TextView accentCO2 = root.findViewById(R.id.accentCO2);

        Button inviteButton = root.findViewById(R.id.inviteButton);
        Button deleteButton = root.findViewById(R.id.deleteButton);
        ImageButton sendInviteButton = root.findViewById(R.id.sendInviteButton);
        ImageView edit = root.findViewById(R.id.edit);
        CardView inviteView = root.findViewById(R.id.inviteFriendView);
        EditText inviteEditText = root.findViewById(R.id.inviteEditText);
        sharedRV = root.findViewById(R.id.sharedRV);

        TextView nextMeasureValue = root.findViewById(R.id.next_measure_value_greenhouse_fragment);
        TextView nextWaterValue = root.findViewById(R.id.next_water_value_greenhouse_fragment);

        //Setup plant Recycler view
        plantRV = root.findViewById(R.id.plantRVgreenhouse);
        plantRV.hasFixedSize();
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(root.getContext(), LinearLayoutManager.HORIZONTAL, false);

        plantRV.setLayoutManager(layoutManager);
        plantRVAdapter = new PlantRVAdapter(this, false);

        plantRVAdapter.setPlantArrayList(greenhouse.getPlants());
        plantRV.setAdapter(plantRVAdapter);

        Chip water = root.findViewById(R.id.waterButton);
        water.setOnClickListener(v ->
                mViewModel.water(greenhouse.getOwnerId(),greenhouse.getId()));

        Button window = root.findViewById(R.id.windowButton);
        if (greenhouse.isWindowIsOpen())
            window.setText("Close window");
        else
            window.setText("Open window");

        window.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                    {
                        int openWindow;
                        if(greenhouse.isWindowIsOpen()) {
                                openWindow = 0;
                                window.setText("Open window");
                            }

                        else {
                                openWindow = 1;
                                window.setText("Close window");
                            }
                        mViewModel.openWindow(greenhouse.getOwnerId(), greenhouse.getId(), openWindow);
                    }
            });


        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        Boolean fahrenheit = sharedPref.getBoolean(SettingsActivity.KEY_PREF_FAHRENHEIT_SWITCH, false);

        if(fahrenheit)
            temperatureUnit = getString(R.string.fahrenheit);

        greenhouse.getCurentLiveData().observeForever(new Observer<List<SensorData>>() {
            @Override
            public void onChanged(List<SensorData> data) {
                if (data != null) {
                    for (SensorData sensorData : data) {
                        switch (sensorData.getType().toLowerCase()) {
                            case "co2":
                                valueCO2.setText(((int)sensorData.getValue()) + "");
                                //Sets co2 health color
                                accentCO2.setBackgroundResource(greenhouse.gethealthColor(sensorData.getType().toLowerCase()));
                                break;
                            case "temperature":
                                if (sensorData.getValue() % 1 == 0) {
                                    if(fahrenheit)
                                        valueTemperature.setText((int) Converter.convertToFahrenheit(sensorData.getValue()) + temperatureUnit);
                                    else
                                        valueTemperature.setText((int) sensorData.getValue() + temperatureUnit);
                                }
                                else {
                                    if(fahrenheit)
                                        valueTemperature.setText(Converter.convertToFahrenheit(sensorData.getValue()) + temperatureUnit);
                                    else
                                        valueTemperature.setText(sensorData.getValue() + temperatureUnit);
                                }
                                //Sets co2 health color
                                accentTemperature.setBackgroundResource(greenhouse.gethealthColor(sensorData.getType().toLowerCase()));
                                break;
                            case "humidity":
                                valueHumidity.setText(((int)sensorData.getValue()) + "%");
                                //Sets co2 health color
                                accentHumidity.setBackgroundResource(greenhouse.gethealthColor(sensorData.getType().toLowerCase()));
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
                    nextMeasureValue.setText(R.string.awaiting_data);
                else
                    nextMeasureValue.setText(integer+" minutes");
            }
        });

        greenhouse.getMinutesToNextWaterLiveData().observeForever(new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                if (greenhouse.isTimeToRestartWaterTimer()) {
                    greenhouse.startCountDownTimerNextWater();
                    greenhouse.setIsTimeToRestartWaterTimer(false);
                }

                if(integer < 0)
                    nextWaterValue.setText(R.string.awaiting_data);
                else
                    nextWaterValue.setText(integer+" minutes");

            }
        });

        name.setText(greenhouse.getName());

        //Remove functionality, if the greenhouse belongs to a friend
        if (mViewModel.getCurrentUserId() != greenhouse.getOwnerId()){
            inviteButton.setVisibility(View.GONE);
            sharedHeading.setVisibility(View.GONE);
            sharedRV.setVisibility(View.GONE);
            deleteButton.setVisibility(View.VISIBLE);

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(getContext())
                            .setTitle("Leave GrowBro")
                            .setMessage("Are you sure you want to remove your access to " + greenhouse.getName() + "? Only the owner can invite you again.")

                            // Specifying a listener allows you to take an action before dismissing the dialog.
                            // The dialog is automatically dismissed when a dialog button is clicked.
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    //mViewModel.removeAccess
                                }
                            })
                            // A null listener allows the button to dismiss the dialog and take no further action.
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(R.drawable.ic_exclamation_mark)
                            .show();
                }
            });
        }
        else {
            //Setup shared Recycler view
            sharedRV.hasFixedSize();
            LinearLayoutManager layoutManagerShared
                    = new LinearLayoutManager(root.getContext(), LinearLayoutManager.HORIZONTAL, false);

            sharedRV.setLayoutManager(layoutManagerShared);
            sharedRVAdapter = new SharedRVAdapter(this);
            sharedRVAdapter.setItemList(greenhouse.getSharedWith());
            sharedRV.setAdapter(sharedRVAdapter);

            inviteButton.setVisibility(View.VISIBLE);
            deleteButton.setVisibility(View.GONE);

            sharedHeading.setVisibility(View.VISIBLE);
            sharedRV.setVisibility(View.VISIBLE);
        }

        //Set onclick listeners for buttons
        inviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inviteView.getVisibility() == View.VISIBLE){
                    inviteView.setVisibility(View.GONE);
                }
                else
                    inviteView.setVisibility(View.VISIBLE);
            }
        });

        sendInviteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (inviteEditText.getText().toString().equals("") || inviteEditText.getText() == null)
                    inviteEditText.setError("Please enter a username");
                else{
                    Toast.makeText(getContext(), "Invite sent to " + inviteEditText.getText(), Toast.LENGTH_SHORT).show();
                    inviteView.setVisibility(View.GONE);
                    greenhouse.shareGreenhouse(inviteEditText.getText().toString());
                    inviteEditText.setText("");

                    sharedRVAdapter.setItemList(greenhouse.getSharedWith());
                    sharedRV.setAdapter(sharedRVAdapter);
                }
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavOptions navOptions = new NavOptions.Builder()
                        .setPopUpTo(R.id.greenhouseFragment, true)
                        .build();
                Bundle bundle = new Bundle();
                bundle.putString("selectedGreenhouseId", greenhouse.getId() + "");
                Navigation.findNavController(getView()).navigate(R.id.action_greenhouseTabFragment_to_editGreenhouseFragment, bundle, navOptions);
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onListItemClick (int clickedItemIndex) {
        new AlertDialog.Builder(getContext())
                .setTitle("Revoke access")
                .setMessage("Are you sure you want revoke " + greenhouse.getSharedWith().get(clickedItemIndex) + "'s access to your GrowBro?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        greenhouse.removeShare(greenhouse.getSharedWith().get(clickedItemIndex));
                        sharedRVAdapter.setItemList(greenhouse.getSharedWith());
                        sharedRV.setAdapter(sharedRVAdapter);
                    }
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(R.drawable.ic_exclamation_mark)
                .show();
    }

    @Override
    public void onPlantListItemClick(int clickedItemIndex) {
        //Unclickable
    }
}