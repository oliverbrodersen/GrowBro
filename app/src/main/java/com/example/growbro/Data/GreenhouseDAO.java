package com.example.growbro.Data;

import android.util.Log;
import com.example.growbro.Models.Data.ApiCurrentDataPackage;
import com.example.growbro.Models.Data.ApiReceipt;
import com.example.growbro.Models.Data.SensorData;
import com.example.growbro.Models.Greenhouse;
import com.example.growbro.Models.Plant;
import com.example.growbro.Models.User;
import com.google.gson.Gson;

import androidx.lifecycle.MutableLiveData;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GreenhouseDAO {
    private static GreenhouseDAO instance;
    private List<Greenhouse> greenhouseList;

    private GreenhouseDAO() {
        //Dummy data
        greenhouseList = new ArrayList<>();
        ArrayList<SensorData> data = new ArrayList<>();
        SensorData d = new SensorData("CO2",5);
        data.add(d);
        Greenhouse greenhouse = new Greenhouse("name",1,1,null,3,3,"idk",null, data);
        greenhouseList.add(greenhouse);
    }

    public static GreenhouseDAO getInstance() {
        if (instance == null)
            instance = new GreenhouseDAO();
        return instance;
    }

    public List<Greenhouse> getGreenhouseList() {
        return greenhouseList;
    }
    public Greenhouse getGreenhouse(int id){
        for (Greenhouse gh:greenhouseList) {
            if (gh.getId() == id)
                return gh;
        }
        return null;
    }
    public MutableLiveData<List<SensorData>> getLiveData(int greenhouseId){
        return getGreenhouse(greenhouseId).getCurentLiveData();
    }

    public boolean updateGreenhouse(Greenhouse greenhouse){
        for (int i = 0; i < greenhouseList.size(); i++) {
            if (greenhouseList.get(i).getId() == greenhouse.getId()){
                greenhouseList.remove(i);
                greenhouseList.add(greenhouse);
                return true;
            }
        }
        System.out.println("Greenhouse was not found in DAO");
        return false;
    }
    public void apiGetCurrentData(int userId, int greenhouseId){
        GrowBroApi growBroApi = ServiceGenerator.getGrowBroApi();
        Call<ApiCurrentDataPackage> call = growBroApi.getCurrentData(userId,greenhouseId);
        call.enqueue(
                new Callback<ApiCurrentDataPackage>(){

                    @Override
                    public void onResponse(Call<ApiCurrentDataPackage> call, Response<ApiCurrentDataPackage> response) {
                        if (response.code() == 200){
                            getGreenhouse(greenhouseId).setCurrentData(response.body().getData());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiCurrentDataPackage> call, Throwable t) {
                        Log.e("Api error", t.toString());
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
                            Log.d("API",response.body().toString());
                            //Timestamp bliver sat her og IKKE hentet fra api
                            //Ændre når vi er færdige med at bruge mock api
                            //getGreenhouse(greenhouseId).setLastMeasurement(response.body().getLastDataPoint());
                            getGreenhouse(greenhouseId).setLastMeasurement(new Timestamp(new Date().getTime()));
                            getGreenhouse(greenhouseId).setCurrentData(response.body().getData());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiCurrentDataPackage> call, Throwable t) {
                        Log.e("1Api error", t.toString());
                    }
                }
        );
    }
    public void apiWaterNow(int userId, int greenhouseId){
        GrowBroApi growBroApi = ServiceGenerator.getGrowBroApi();
        Call<ApiReceipt> call = growBroApi.waterNow(userId,greenhouseId);
        call.enqueue(
                new Callback<ApiReceipt>(){
                    @Override
                    public void onResponse(Call<ApiReceipt> call, Response<ApiReceipt> response) {
                        if (response.code() == 200){
                            getGreenhouse(greenhouseId).setLastWaterDate(response.body().getTimeOfExecution());
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiReceipt> call, Throwable t) {
                        Log.e("Api error", t.toString());
                    }
                }
        );
    }
    public void apiLogin(String username, String password){
        GrowBroApi growBroApi = ServiceGenerator.getGrowBroApi();

        // prepare call in Retrofit 2.0
        try {
            JSONObject paramObject = new JSONObject();
            paramObject.put("username", username);
            paramObject.put("pass", password);

            Call<User> call = growBroApi.login(paramObject.toString());
            call.enqueue(
                    new Callback<User>(){
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (response.code() == 200){
                                apiGetGreenhouseList(response.body().getUserId());
                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Log.e("Api error", t.toString());
                        }
                    }
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }

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
                                //TODO
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
                            greenhouseList = response.body();
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
                                greenhouseList.add(response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<Greenhouse> call, Throwable t) {
                        Log.e("Api error", t.toString());
                    }
                }
        );
    }
    public void apiOpenWindow(int userId, int greenhouseId){
        GrowBroApi growBroApi = ServiceGenerator.getGrowBroApi();
        Call<ApiReceipt> call = growBroApi.openWindow(userId,greenhouseId);
        call.enqueue(
                new Callback<ApiReceipt>(){
                    @Override
                    public void onResponse(Call<ApiReceipt> call, Response<ApiReceipt> response) {
                        if (response.code() == 200){
                            //TODO
                        }
                    }

                    @Override
                    public void onFailure(Call<ApiReceipt> call, Throwable t) {
                        Log.e("Api error", t.toString());
                    }
                }
        );
    }
    public void apiSetGreenhouseList(ArrayList<Greenhouse> greenhouseList) {
        this.greenhouseList = greenhouseList;
    }
    public void apiAddGreenhouse(int userId, Greenhouse greenhouse){
        GrowBroApi growBroApi = ServiceGenerator.getGrowBroApi();
        // prepare call in Retrofit 2.0
        try {
            JSONObject paramObject = new JSONObject();
            paramObject.put("Greenhouse", greenhouse);
            Call<Integer> call = growBroApi.addGreenhouse(userId, paramObject.toString());
            call.enqueue(
                    new Callback<Integer>(){
                        @Override
                        public void onResponse(Call<Integer> call, Response<Integer> response) {
                            if (response.code() == 200){
                                greenhouse.setId(response.body());
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
    public void apiAddUser(User user){
        GrowBroApi growBroApi = ServiceGenerator.getGrowBroApi();
        // prepare call in Retrofit 2.0
        try {
            JSONObject paramObject = new JSONObject();
            paramObject.put("User", user);
            Call<ApiReceipt> call = growBroApi.addUser(paramObject.toString());
            call.enqueue(
                    new Callback<ApiReceipt>(){
                        @Override
                        public void onResponse(Call<ApiReceipt> call, Response<ApiReceipt> response) {
                            if (response.code() == 200){
                                //TODO
                                //Bring user to login screen
                            }
                        }

                        @Override
                        public void onFailure(Call<ApiReceipt> call, Throwable t) {
                            Log.e("Api error", t.toString());
                        }
                    }
            );
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
