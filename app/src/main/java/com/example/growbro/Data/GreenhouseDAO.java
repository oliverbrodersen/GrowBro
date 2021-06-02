package com.example.growbro.Data;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.example.growbro.Models.Data.ApiCurrentDataPackage;
import com.example.growbro.Models.Data.ApiResponseId;
import com.example.growbro.Models.Data.CurrentDataResultFromApi;
import com.example.growbro.Models.Data.GreenhouseUpload;
import com.example.growbro.Models.Data.SensorData;
import com.example.growbro.Models.Greenhouse;
import com.example.growbro.Models.Plant;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GreenhouseDAO {
    private static GreenhouseDAO instance;
    private MutableLiveData<List<Greenhouse>> greenhouseList;
    private MutableLiveData<List<Greenhouse>> friendsGreenhouseList;
    private MutableLiveData<List<ApiCurrentDataPackage>> sensorDataHistory;
    private final int SECONDS_BETWEEN_CHECK = 1000;

    private GreenhouseDAO() {
        greenhouseList = new MutableLiveData<>();
        friendsGreenhouseList = new MutableLiveData<>();
        sensorDataHistory = new MutableLiveData<>();
        ArrayList<Greenhouse> greenhouseArrayList = new ArrayList<>();
        ArrayList<Greenhouse> friendsGreenhouseArrayList = new ArrayList<>();
    }

    public static GreenhouseDAO getInstance() {
        if (instance == null)
            instance = new GreenhouseDAO();
        return instance;
    }

    public List<Greenhouse> getGreenhouseList() {
        return greenhouseList.getValue();
    }
    public Greenhouse getGreenhouse(int id){
        for (Greenhouse gh:greenhouseList.getValue()) {
            if (gh.getId() == id)
                return gh;
        }
        for (Greenhouse gh:friendsGreenhouseList.getValue()) {
            if (gh.getId() == id)
                return gh;
        }
        return null;
    }
    public MutableLiveData<List<SensorData>> getLiveData(int greenhouseId){
        return getGreenhouse(greenhouseId).getCurentLiveData();
    }

    public MutableLiveData<List<Greenhouse>> getGreenhouseListAsLiveData(){
        return greenhouseList;
    }
    public MutableLiveData<List<Greenhouse>> getFriendsGreenhouseListAsLiveData(){
        return friendsGreenhouseList;
    }


    public boolean updateGreenhouse(Greenhouse greenhouse){
        for (int i = 0; i < greenhouseList.getValue().size(); i++) {
            if (greenhouseList.getValue().get(i).getId() == greenhouse.getId()){
                greenhouseList.getValue().remove(i);
                greenhouseList.getValue().add(greenhouse);
                return true;
            }
        }
        Log.d("Greenhouse", "Greenhouse to be updated, was not found in DAO");
        return false;
    }

    public void apiGetCurrentData(int userId, int greenhouseId){
        GrowBroApi growBroApi = ServiceGenerator.getGrowBroApi();
        Call<CurrentDataResultFromApi> call = growBroApi.getCurrentData(userId,greenhouseId);
        call.enqueue(
                new Callback<CurrentDataResultFromApi>(){
                    @Override
                    public void onResponse(Call<CurrentDataResultFromApi> call, Response<CurrentDataResultFromApi> response) {
                        if (response.code() == 200){
                            Log.d("Api", "Response: " + response.body().getData());

                            getGreenhouse(greenhouseId).setCurrentData(response.body().getData());

                            if (!response.body().getLastDataPoint().equals(getGreenhouse(greenhouseId).getLastMeasurementToString()))
                                getGreenhouse(greenhouseId).setStopCheckForNewMeasurement(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<CurrentDataResultFromApi> call, Throwable t) {
                        Log.e("Api error", "apiGetCurrentData failed: " + t.toString());
                    }
                }
        );
    }
    public void getDummyData(int greenhouseId){
        GrowBroApi growBroApi = ServiceGenerator.getGrowBroApi();
        Call<ApiCurrentDataPackage> call = growBroApi.getDummyData();
        call.enqueue(
                new Callback<ApiCurrentDataPackage>(){
                    @Override
                    public void onResponse(Call<ApiCurrentDataPackage> call, Response<ApiCurrentDataPackage> response) {
                        if (response.code() == 200){
                            Log.d("API","Dummy data response: " + response.toString());
                            //Timestamp bliver sat her og IKKE hentet fra api
                            //Ændre når vi er færdige med at bruge mock api
                            //getGreenhouse(greenhouseId).setLastMeasurement(response.body().getLastDataPoint());
                            getGreenhouse(greenhouseId).setLastMeasurementAndResetLiveData(new Timestamp(new Date().getTime()));
                            getGreenhouse(greenhouseId).setCurrentData(response.body().getData());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiCurrentDataPackage> call, Throwable t) {
                        Log.e("Api error", "getDummyData failed: " + t.toString());
                    }
                }
        );
    }
    public void apiWaterNow(int userId, int greenhouseId){
        GrowBroApi growBroApi = ServiceGenerator.getGrowBroApi();
        Call<Void> call = growBroApi.waterNow(userId,greenhouseId);
        call.enqueue(
                new Callback<Void>(){
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        Log.i("Api response", response.toString());
                        if (response.code() == 400) {
                            try {
                                Log.i("Api 400", response.errorBody().string());
                            } catch (IOException e) {
                                // handle failure to read error
                            }
                        }
                        if (response.code() == 200){

                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("Api error", "apiWaterNow failed: " + t.toString());
                    }
                }
        );
    }
    public void apiGetAverageData(int userId, int greenhouseId, Timestamp timeFrom, Timestamp timeTo){
        GrowBroApi growBroApi = ServiceGenerator.getGrowBroApi();
        ApiCurrentDataPackage apiCurrentDataPackage;
        // prepare call in Retrofit 2.0
        try {
            JSONObject paramObject = new JSONObject();
            paramObject.put("timeFrom", timeFrom);
            paramObject.put("timeTo", timeTo);
            Call<ApiCurrentDataPackage> call = growBroApi.getAverageData(userId, greenhouseId, paramObject.toString());
            call.enqueue(
                    new Callback<ApiCurrentDataPackage>(){
                        @Override
                        public void onResponse(Call<ApiCurrentDataPackage> call, Response<ApiCurrentDataPackage> response) {
                            if (response.code() == 200){
                                //Not implemented
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiCurrentDataPackage> call, Throwable t) {
                            Log.e("Api error", t.toString());
                        }
                    }
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public void apiGetGreenhouseList(int userId){
        GrowBroApi growBroApi = ServiceGenerator.getGrowBroApi();
        Call<List<Greenhouse>> call = growBroApi.getGreenhouseList(userId);
        call.enqueue(
                new Callback<List<Greenhouse>>(){
                    @Override
                    public void onResponse(Call<List<Greenhouse>> call, Response<List<Greenhouse>> response) {
                        if (response.code() == 200){
                            //Set sensordata
                            for (Greenhouse g: response.body()) {
                                g.setCurrentData(g.getSensorData());
                                g.getMinutesToNextMeasurementLiveData().observeForever(new Observer<Integer>() {
                                    @Override
                                    public void onChanged(Integer integer) {
                                        if (integer == 0) {
                                            TimerTask task = new TimerTask() {
                                                @Override
                                                public void run() {
                                                    if (g.isStopCheckForNewMeasurement())
                                                        return;
                                                    apiGetCurrentData(userId, g.getId());
                                                }
                                            };

                                            Timer timer = new Timer("apiDataListener/" + g.getId());
                                            timer.scheduleAtFixedRate(task, SECONDS_BETWEEN_CHECK, SECONDS_BETWEEN_CHECK);
                                        }
                                    }
                                });
                            }

                            greenhouseList.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Greenhouse>> call, Throwable t) {
                        Log.e("Api error", t.toString());
                        Log.e("Api error", call.toString());
                    }
                }
        );
    }
    public void apiGetFriendsGreenhouseList(int userId){
        GrowBroApi growBroApi = ServiceGenerator.getGrowBroApi();
        Call<List<Greenhouse>> call = growBroApi.getFriendsGreenhouses(userId);
        call.enqueue(
                new Callback<List<Greenhouse>>(){
                    @Override
                    public void onResponse(Call<List<Greenhouse>> call, Response<List<Greenhouse>> response) {
                        if (response.code() == 200){
                            friendsGreenhouseList.setValue(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<List<Greenhouse>> call, Throwable t) {
                        Log.e("Api error", t.toString());
                    }
                }
        );
    }
    public void apiGetGreenhouse(int userId, int greenhouseId){
        GrowBroApi growBroApi = ServiceGenerator.getGrowBroApi();
        Call<Greenhouse> call = growBroApi.getGreenhouse(userId, greenhouseId);
        call.enqueue(
                new Callback<Greenhouse>(){
                    @Override
                    public void onResponse(Call<Greenhouse> call, Response<Greenhouse> response) {
                        if (response.code() == 200){
                            if (!updateGreenhouse(response.body()))
                                addGreenhouse(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<Greenhouse> call, Throwable t) {
                        Log.e("Api error", t.toString());
                    }
                }
        );
    }

    private void addGreenhouse(Greenhouse body) {
        ArrayList<Greenhouse> greenhouseArrayList = (ArrayList<Greenhouse>) greenhouseList.getValue();
        greenhouseArrayList.add(body);
        greenhouseList.setValue(greenhouseArrayList);
    }

    public void apiOpenWindow(int userId, int greenhouseId, int openWindow){
        GrowBroApi growBroApi = ServiceGenerator.getGrowBroApi();
        Call<Void> call = growBroApi.openWindow(userId, greenhouseId, openWindow);
        Log.i("DAO after call to ", "apiOpenWindow: ");
        call.enqueue(
                new Callback<Void>(){
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.code() == 200){
                            int position = 0;
                            for (int i = 0; i < greenhouseList.getValue().size(); i++) {
                                if (greenhouseList.getValue().get(i).getId() == greenhouseId) {
                                    position = i;
                                    break;
                                }
                            }

                            if(openWindow == 1)
                                greenhouseList.getValue().get(position).setWindowIsOpen(true);
                            else
                                greenhouseList.getValue().get(position).setWindowIsOpen(false);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e("Api error", t.toString());
                    }
                }
        );
    }

    public void apiAddGreenhouse(int userId, Greenhouse greenhouse){
        GrowBroApi growBroApi = ServiceGenerator.getGrowBroApi();
        // prepare call in Retrofit 2.0
        GreenhouseUpload greenhouseUpload = new GreenhouseUpload(greenhouse);

        Log.i("-> Api in JSON", new Gson().toJson(greenhouseUpload));
        Call<ApiResponseId> call = growBroApi.addGreenhouse(userId, greenhouseUpload);
        call.enqueue(
                new Callback<ApiResponseId>(){
                    @Override
                    public void onResponse(Call<ApiResponseId> call, Response<ApiResponseId> response) {
                        Log.i("Api response", response.toString() + "");
                        if (response.code() == 200){
                            ArrayList<Greenhouse> glist = (ArrayList<Greenhouse>) greenhouseList.getValue();
                            glist.add(greenhouse);
                            greenhouseList.setValue(glist);
                        }
                        if (response.code() == 400) {
                            try {
                                Log.i("Api 400", response.errorBody().string());
                            } catch (IOException e) {
                                // handle failure to read error
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiResponseId> call, Throwable t) {
                        Log.e("Api error", t.toString());
                    }
                }
        );
    }
    public void apiAddPlant(int userId, int greenhouseId, Plant plant){
        GrowBroApi growBroApi = ServiceGenerator.getGrowBroApi();
        // prepare call in Retrofit 2.0
        try {
            JSONObject paramObject = new JSONObject();
            paramObject.put("Plant", plant);
            Call<Integer> call = growBroApi.addPlant(userId, greenhouseId, paramObject.toString());
            call.enqueue(
                    new Callback<Integer>(){
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            if (response.code() == 200){
                                plant.setPlantId(response.body());
                                Greenhouse greenhouse = getGreenhouse(greenhouseId);
                                greenhouse.addPlant(plant);
                                updateGreenhouse(greenhouse);
                            }
                        }

                        @Override
                        public void onFailure(Call<Integer> call, Throwable t) {
                            Log.e("Api error", t.toString());
                        }
                    }
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public HashMap<Long, Float> apiGetSensorDataHistory(int userId, String parameterName, String selectedGreenhouseId, Timestamp timeFrom, Timestamp timeTo) {
        HashMap<Long, Float> dummyHashMap = new HashMap<>();

        //Dummy Data
        Random rn = new Random();
        int dummyDataPoints = 14;
        Long key;
        Float value;
        ChronoUnit unit = ChronoUnit.DAYS;
        Instant now = Instant.now();

        switch (parameterName) {
            case "Temperature":
                for (int i = 0; i < dummyDataPoints; i++) {
                    key = now.minus(i, unit).getEpochSecond();
                    value = 20 * (1F * rn.nextFloat() * 1.5F);
                    dummyHashMap.put(key, value);
                }
                break;
            case "CO2":
                for (int i = 0; i < dummyDataPoints; i++) {
                    key = now.minus(i, unit).getEpochSecond();
                    value = 900 * (0.8F * rn.nextFloat() * 1.2F);
                    dummyHashMap.put(key, value);
                }
                break;
            case "Humidity":
                for (int i = 0; i < dummyDataPoints; i++) {
                    key = now.minus(i, unit).getEpochSecond();
                    value = 50 * (0.8F * rn.nextFloat() * 1.2F);
                    dummyHashMap.put(key, value);
                }
                break;
        }
        return dummyHashMap;
    }
}
