package com.example.growbro.ui.editGreenhouse;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.growbro.Models.Greenhouse;
import com.example.growbro.Models.Plant;
import com.example.growbro.R;
import com.example.growbro.ui.home.rv.PlantRVAdapter;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.Locale;

public class EditGreenhouseFragment extends Fragment implements TimePickerFragment.TimePickedListener, PlantRVAdapter.OnListItemClickListener {

    private EditGreenhouseViewModel mViewModel;
    private CardView pickTime, addPlantCard, addPlantDialog;
    private TextView pickTimeText, scheduleError, header;
    private RecyclerView plantRVadd;
    private PlantRVAdapter plantRVAdapter;
    private ArrayList<Plant> plantList;
    private String time = "";
    private boolean edit;
    public static final String ARG_SELECTED_GREENHOUSE_ID = "selectedGreenhouseId";
    public Greenhouse greenhouse;
    private String selectedGreenhouseId;

    public static EditGreenhouseFragment newInstance() {
        return new EditGreenhouseFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(EditGreenhouseViewModel.class);
        View root =  inflater.inflate(R.layout.edit_greenhouse_fragment, container, false);
        edit = false;
        Bundle args = getArguments();

        header = root.findViewById(R.id.textView12);
        pickTime = root.findViewById(R.id.timePicker);
        pickTimeText = root.findViewById(R.id.waterTimeText);
        addPlantCard = root.findViewById(R.id.addPlantCard);
        addPlantDialog = root.findViewById(R.id.addPlantDialog);
        MaterialButton addPlantButton = root.findViewById(R.id.addPlantButton);

        scheduleError = root.findViewById(R.id.textView15);
        TextView expandAdvancedSettings = root.findViewById(R.id.textView16);
        CardView advancedSettings = root.findViewById(R.id.cardView2);

        TextInputLayout growBroNameTextField = root.findViewById(R.id.textField);
        TextInputLayout growBroIdTextField = root.findViewById(R.id.textField2);
        Slider daysBetweenWater = root.findViewById(R.id.slider);
        Slider amountOfWater = root.findViewById(R.id.slider2);
        TextInputLayout plantNameTextField = root.findViewById(R.id.textField3);
        TextInputLayout plantUrlTextField = root.findViewById(R.id.textField4);
        Button addGrowBro = root.findViewById(R.id.button);
        RangeSlider tempRange = root.findViewById(R.id.range_slider);
        RangeSlider humRange = root.findViewById(R.id.range_slider2);
        RangeSlider coRange = root.findViewById(R.id.range_slider3);

        //If edit
        if (args != null && args.containsKey(ARG_SELECTED_GREENHOUSE_ID)){
            edit = true;
            selectedGreenhouseId = args.getString(ARG_SELECTED_GREENHOUSE_ID);
            greenhouse = mViewModel.getGreenhouse(selectedGreenhouseId);
            header.setText("Edit GrowBro");
            growBroNameTextField.getEditText().setText(greenhouse.getName());
            growBroIdTextField.getEditText().setText(greenhouse.getId() + "");
            daysBetweenWater.setValue(greenhouse.getWaterFrequency());
            amountOfWater.setValue((float) greenhouse.getWaterVolume());
            onTimePicked(greenhouse.getWaterTimeOfDay());
            plantList = greenhouse.getListPlants();
            addGrowBro.setText("Save changes");

            tempRange.setValues(greenhouse.getTemperatureThreshold());
            humRange.setValues(greenhouse.getHumidityThreshold());
            coRange.setValues(greenhouse.getCo2Threshold());
        }
        else{
            header.setText("Add GrowBro");
            growBroNameTextField.getEditText().setText("");
            plantList = new ArrayList<>();
            addGrowBro.setText("Add GrowBro");
            pickTimeText.setText("Set water schedule");
        }

        //Setup advanced menu
        advancedSettings.setVisibility(View.GONE);
        humRange.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                //It is just an example
                return String.format(Locale.UK, "%.0f", value) + "%";
            }
        });
        tempRange.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                //It is just an example
                return String.format(Locale.UK, "%.1f", value) + "℃";
            }
        });
        coRange.setLabelFormatter(new LabelFormatter() {
            @NonNull
            @Override
            public String getFormattedValue(float value) {
                //It is just an example
                return String.format(Locale.UK, "%.0f", value) + "ppm";
            }
        });

        //Recycler setup
        plantRVadd = root.findViewById(R.id.plantRVadd);
        plantRVadd.hasFixedSize();
        plantRVAdapter = new PlantRVAdapter(this, true);

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(root.getContext(), LinearLayoutManager.HORIZONTAL, false);

        plantRVadd.setLayoutManager(layoutManager);
        plantRVAdapter.setPlantArrayList(plantList);
        plantRVadd.setAdapter(plantRVAdapter);

        //Setup water schedule button
        TimePickerFragment.TimePickedListener listener = this;

        pickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment(listener);
                newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
            }
        });

        //Setup add plant
        addPlantDialog.setVisibility(View.GONE);
        addPlantCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addPlantDialog.getVisibility() == View.GONE)
                    addPlantDialog.setVisibility(View.VISIBLE);
                else
                    addPlantDialog.setVisibility(View.GONE);
            }
        });
        addPlantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean done = true;
                if (plantNameTextField.getEditText().getText().toString().equals("")){
                    plantNameTextField.getEditText().setError("Name is required");
                    done = false;
                }
                if (plantUrlTextField.getEditText().getText().toString().equals("")){
                    plantUrlTextField.getEditText().setError("Photo is required");
                    done = false;
                }
                if (done){
                    plantList.add(new Plant(plantNameTextField.getEditText().getText().toString(), plantList.size()+1 , plantUrlTextField.getEditText().getText().toString()));
                    plantRVAdapter.setPlantArrayList(plantList);
                    plantRVadd.setAdapter(plantRVAdapter);

                    addPlantDialog.setVisibility(View.GONE);
                    plantNameTextField.getEditText().setText("");
                    plantUrlTextField.getEditText().setText("");
                }
            }
        });

        addGrowBro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean done = true;
                if (time.equals("")){
                    done = false;
                    scheduleError.setVisibility(View.VISIBLE);
                }
                else{
                    scheduleError.setVisibility(View.GONE);
                }

                if (growBroNameTextField.getEditText().getText().toString().equals("")){
                    done = false;
                    growBroNameTextField.getEditText().setError("Name is required");
                }

                if (growBroIdTextField.getEditText().getText().toString().equals("")){
                    done = false;
                    growBroIdTextField.getEditText().setError("Id is required");
                }

                if (done){
                    if (!edit) {
                        Greenhouse g = new Greenhouse();
                        g.setName(growBroNameTextField.getEditText().getText().toString());
                        g.setId(Integer.parseInt(growBroIdTextField.getEditText().getText().toString()));
                        g.setWaterFrequency((int) daysBetweenWater.getValue());
                        g.setWaterVolume(amountOfWater.getValue());
                        g.setWaterTimeOfDay(time);
                        g.setListPlants(plantList);
                        g.setTemperatureThreshold((ArrayList<Float>) tempRange.getValues());
                        g.setHumidityThreshold((ArrayList<Float>) humRange.getValues());
                        g.setCo2Threshold((ArrayList<Float>) coRange.getValues());
                        mViewModel.addGreenhouse(g);
                    }
                    else{
                        greenhouse.setName(growBroNameTextField.getEditText().getText().toString());
                        greenhouse.setId(Integer.parseInt(growBroIdTextField.getEditText().getText().toString()));
                        greenhouse.setWaterFrequency((int) daysBetweenWater.getValue());
                        greenhouse.setWaterVolume(amountOfWater.getValue());
                        greenhouse.setWaterTimeOfDay(time);
                        greenhouse.setListPlants(plantList);
                        greenhouse.setTemperatureThreshold((ArrayList<Float>) tempRange.getValues());
                        greenhouse.setHumidityThreshold((ArrayList<Float>) humRange.getValues());
                        greenhouse.setCo2Threshold((ArrayList<Float>) coRange.getValues());
                        mViewModel.updateGreenhouse(greenhouse);
                    }
                    NavOptions navOptions = new NavOptions.Builder()
                            .setPopUpTo(R.id.editGreenhouseFragment, true)
                            .build();

                    Navigation.findNavController(v).navigate(R.id.action_editGreenhouseFragment_to_nav_home, null, navOptions);
                }
            }
        });

        expandAdvancedSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (advancedSettings.getVisibility() == View.GONE){
                    advancedSettings.setVisibility(View.VISIBLE);
                }
                else
                    advancedSettings.setVisibility(View.GONE);
            }
        });
        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // TODO: Use the ViewModel
    }

    @Override
    public void onTimePicked(String time) {
        this.time = time;
        scheduleError.setVisibility(View.GONE);
        pickTimeText.setText("Waters at: " + time);
    }

    @Override
    public void onPlantListItemClick(int clickedItemIndex) {
        new AlertDialog.Builder(getContext())
                .setTitle("Delete plant")
                .setMessage("Are you sure you want to delete " + plantList.get(clickedItemIndex).getName() + " from your GrowBro?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        plantList.remove(plantList.get(clickedItemIndex));

                        plantRVAdapter.setPlantArrayList(plantList);
                        plantRVadd.setAdapter(plantRVAdapter);
                    }
                })
                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(R.drawable.ic_exclamation_mark)
                .show();
    }
}