package com.example.growbro.ui.greenhousetab.statistics;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.growbro.R;
import com.example.growbro.Settings.SettingsActivity;
import com.example.growbro.util.Converter;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class StatisticsFragment extends Fragment {

    public static final String ARG_SELECTED_GREENHOUSE_ID = "selectedGreenhouseId";
    private String selectedGreenhouseId;
    private String temperatureUnit;
    private StatisticsViewModel statisticsViewModel;
    private LineChart lineChart;
    private boolean fahrenheit;

    private static final int DEFAULT_TIME_RANGE = 14; //14 days

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        selectedGreenhouseId = args.getString(ARG_SELECTED_GREENHOUSE_ID);
        temperatureUnit = getString(R.string.celsius); //default
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.statistics_fragment, container, false);
        lineChart = root.findViewById(R.id.chart_line_greenhouse);
        configureLineChart();

        statisticsViewModel = new ViewModelProvider(this).get(StatisticsViewModel.class);

        Spinner spinner = (Spinner) root.findViewById(R.id.spinner_parameters_statistics);

        String[] parameterNames = {"Temperature", "CO2", "Humidity"};
        Timestamp timestampFrom= new Timestamp(Instant.now().minus(DEFAULT_TIME_RANGE, ChronoUnit.DAYS).getEpochSecond());
        Timestamp timestampTo= new Timestamp(Instant.now().getEpochSecond());

        int userId = getActivity().getSharedPreferences("login",MODE_PRIVATE).getInt("current_user_id", -1);
        setLineData(userId, parameterNames[0], timestampFrom, timestampTo); //parameter at index 0 is currently the default parameter for the chart

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, parameterNames);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.simple_spinner_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setLineData(userId, parameterNames[position], timestampFrom, timestampTo);
                lineChart.invalidate();
                Log.v("item", (String) parent.getItemAtPosition(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());
         fahrenheit = sharedPref.getBoolean
                (SettingsActivity.KEY_PREF_FAHRENHEIT_SWITCH, false);

        if(fahrenheit)
            temperatureUnit = getString(R.string.fahrenheit);


        return root;
    }

    //https://learntodroid.com/how-to-display-a-line-chart-in-your-android-app/
    private void setLineData(int userId, String parameterName, Timestamp timeFrom, Timestamp timeTo) {
        ArrayList<Entry> chartEntries = statisticsViewModel.getChartEntries(userId, parameterName, selectedGreenhouseId, timeFrom, timeTo);

        if(fahrenheit)
            for (Entry entry : chartEntries) {
                double dEntry = Converter.convertToFahrenheit(entry.getY());
                entry.setY((float) dEntry);
            }

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        String unit = "";
        switch (parameterName) {
            case "Temperature":
                unit = temperatureUnit;
                break;
            case "CO2":
                unit = "ppm";
                break;
            case "Humidity":
                unit = "%";
                break;
        }
        LineDataSet lineDataSet = new LineDataSet(chartEntries, parameterName + " (" + unit +  ")");
        lineDataSet.setDrawCircles(true);
        lineDataSet.setCircleRadius(4);
        lineDataSet.setDrawValues(false);
        lineDataSet.setLineWidth(3);
        lineDataSet.setColor(Color.parseColor("#689733"));
        lineDataSet.setCircleColor(Color.parseColor("#9bc66d"));
        dataSets.add(lineDataSet);

        LineData lineData = new LineData(dataSets);

        if (chartEntries.size() > 0)
            lineChart.setData(lineData);
        else lineChart.setData(null);

        lineChart.setNoDataText("No entries for " + parameterName.toLowerCase() + " yet");
        lineChart.setNoDataTextColor(ContextCompat.getColor(getActivity(), R.color.red));
        Paint paint = lineChart.getPaint(Chart.PAINT_INFO);
        paint.setTextSize(60f);
    }

    private void configureLineChart() {
        lineChart.setDescription(null);
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new ValueFormatter() {
            private final SimpleDateFormat mFormat = new SimpleDateFormat("dd MMM", Locale.ENGLISH);
            @Override
            public String getFormattedValue(float value) {
                long millis = (long) value * 1000L;
                return mFormat.format(new Date(millis));
            }
        });
    }

}