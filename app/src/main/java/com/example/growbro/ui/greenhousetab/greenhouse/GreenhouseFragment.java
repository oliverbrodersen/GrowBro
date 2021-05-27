package com.example.growbro.ui.greenhousetab.greenhouse;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
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
import com.example.growbro.ui.greenhousetab.greenhouse.rv.SharedRVAdapter;
import com.example.growbro.ui.home.rv.PlantRVAdapter;
import com.google.android.material.chip.Chip;

import java.util.List;

public class GreenhouseFragment extends Fragment implements SharedRVAdapter.OnListItemClickListener, PlantRVAdapter.OnListItemClickListener {

    public static final String ARG_SELECTED_GREENHOUSE_ID = "selectedGreenhouseId";
    public Greenhouse greenhouse;
    private String selectedGreenhouseId;
    private RecyclerView plantRV;
    private PlantRVAdapter plantRVAdapter;
    private RecyclerView sharedRV;
    private SharedRVAdapter sharedRVAdapter;

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
        TextView sharedHeading = root.findViewById(R.id.textView5);
        Button inviteButton = root.findViewById(R.id.inviteButton);
        Button settingsButton = root.findViewById(R.id.settingsButton);
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

        plantRVAdapter.setPlantArrayList(greenhouse.getListPlants());
        plantRV.setAdapter(plantRVAdapter);

        Chip water = root.findViewById(R.id.waterButton);
        water.setOnClickListener(v ->
                mViewModel.water(greenhouse.getOwnerId(),greenhouse.getId()));

        Chip window = root.findViewById(R.id.windowButton);
        window.setOnClickListener(v ->
                mViewModel.openWindow(greenhouse.getOwnerId(),greenhouse.getId()));

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

                if (greenhouse.isTimeToRestartMeasurementTimer()) {
                    greenhouse.startCountDownTimerNextMeasurement();
                    greenhouse.setIsTimeToRestartMeasurementTimer(false);
                }

                nextMeasureValue.setText(integer+" minutes");
            }
        });
        //greenhouse.startCountDownTimerNextMeasurement(); Not necessary to start countdown here since it was already started in HomeFragment

        greenhouse.getMinutesToNextWaterLiveData().observeForever(new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {

                if (greenhouse.isTimeToRestartWaterTimer()) {
                    greenhouse.startCountDownTimerNextWater();
                    greenhouse.setIsTimeToRestartWaterTimer(false);
                }

                nextWaterValue.setText(integer+" minutes");

            }
        });
        //greenhouse.startCountDownTimerNextMeasurement();  Not necessary to start countdown here since it was already started in HomeFragment

        name.setText(greenhouse.getName());

        //Remove functionality, if the greenhouse belongs to a friend
        if (mViewModel.getCurrentUserId() != greenhouse.getOwnerId()){
            inviteButton.setVisibility(View.GONE);
            settingsButton.setVisibility(View.GONE);
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
            settingsButton.setVisibility(View.VISIBLE);
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
                    hideKeyboard(getActivity());
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

                    hideKeyboard(getActivity());
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
                Navigation.findNavController(getView()).navigate(R.id.action_greenhouseFragment_to_editGreenhouseFragment, bundle, navOptions);
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        view.clearFocus();
        view.requestFocus();
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
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