package com.example.growbro.ui.editGreenhouse;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.growbro.R;

public class EditGreenhouseFragment extends Fragment implements TimePickerFragment.TimePickedListener {

    private EditGreenhouseViewModel mViewModel;
    private CardView pickTime;
    private TextView pickTimeText;

    public static EditGreenhouseFragment newInstance() {
        return new EditGreenhouseFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(EditGreenhouseViewModel.class);
        View root =  inflater.inflate(R.layout.edit_greenhouse_fragment, container, false);

        pickTime = root.findViewById(R.id.timePicker);
        pickTimeText = root.findViewById(R.id.waterTimeText);

        pickTimeText.setText("Set water schedule");
        TimePickerFragment.TimePickedListener listener = this;

        pickTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment(listener);
                newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
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
        pickTimeText.setText(time);
    }
}