package com.example.growbro.Data;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.growbro.Models.Data.ApiCurrentDataPackage;
import com.example.growbro.Models.Data.ApiReceipt;
import com.example.growbro.Models.Data.CurrentDataResultFromApi;
import com.example.growbro.Models.Data.SensorData;
import com.example.growbro.Models.Greenhouse;
import com.example.growbro.Models.Plant;
import com.example.growbro.Models.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class GreenhouseDAO {
    private static GreenhouseDAO instance;
    private MutableLiveData<List<Greenhouse>> greenhouseList;

    private GreenhouseDAO() {
        //Dummy data
        greenhouseList = new MutableLiveData<>();
        ArrayList<Greenhouse> greenhouseArrayList = new ArrayList<>();

        //Responsible for getting data from postman
        getDummyData(1);

        ArrayList<SensorData> data = new ArrayList<>();
        SensorData d = new SensorData("CO2",5);
        data.add(d);
        ArrayList<Plant> plantArrayList = new ArrayList<>();
        ArrayList<Plant> plantArrayList2 = new ArrayList<>();
        plantArrayList.add(new Plant("Monstera", 1, "https://cdn.shopify.com/s/files/1/0059/8835/2052/products/Monstera_delisiosa_4_FGT_450x.jpg"));
        plantArrayList.add(new Plant("Pothos", 2, "https://hips.hearstapps.com/vader-prod.s3.amazonaws.com/1603654968-il_570xN.2485154742_kpa5.jpg"));
        plantArrayList.add(new Plant("Aglaonema", 3, "https://hips.hearstapps.com/vader-prod.s3.amazonaws.com/1603654887-1427228256-chinese-evergreen-plants-little-water.jpg"));
        plantArrayList.add(new Plant("Asparagus Fern", 4, "https://hips.hearstapps.com/vader-prod.s3.amazonaws.com/1554477330-beautiful-asparagus-fern-plant-in-a-basket-royalty-free-image-972247932-1546889240.jpg"));
        plantArrayList.add(new Plant("Chinese Money Plant", 5, "https://hips.hearstapps.com/vader-prod.s3.amazonaws.com/1557177323-pilea-peperomioides-money-plant-in-the-pot-single-royalty-free-image-917778022-1557177295.jpg"));

        plantArrayList2.add(new Plant("Jordbær plante", 6, "https://drupaller-prod.imgix.net/familiejournal/s3fs-public/storage_1/media/jordbaer5.jpg"));
        plantArrayList2.add(new Plant("Citron træ", 7, "https://www.gardeningknowhow.com/wp-content/uploads/2015/05/lemon-tree.jpg"));
        plantArrayList2.add(new Plant("Basilikum", 8, "https://kaere-hjem.imgix.net/s3fs-public/media/article/is-12854_preview.jpg"));


        Greenhouse greenhouse1 = new Greenhouse("Stue drivhus",1,1,plantArrayList,3,3,"idk",new Timestamp(new Date().getTime()), data, true);
        Greenhouse greenhouse2 = new Greenhouse("Altan drivhus",2,1,plantArrayList2,3,3,"idk",new Timestamp(new Date().getTime()), data, false);
        greenhouseArrayList.add(greenhouse1);
        greenhouseArrayList.add(greenhouse2);

        greenhouseList.setValue(greenhouseArrayList);
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
        return null;
    }
    public MutableLiveData<List<SensorData>> getLiveData(int greenhouseId){
        return getGreenhouse(greenhouseId).getCurentLiveData();
    }

    public MutableLiveData<List<Greenhouse>> getGreenhouseListAsLiveData(){
        return greenhouseList;
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
                            getGreenhouse(greenhouseId).setLastMeasurement(new Timestamp(new Date().getTime()));
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
                        Log.e("Api error", "apiWaterNow failed: " + t.toString());
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
                            greenhouseList.setValue(response.body());
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
        this.greenhouseList.setValue(greenhouseList);
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

    public HashMap<Long, Float> getSensorDataHistory(String parameterName, String selectedGreenhouseId) {
        HashMap<Long, Float> hashMap = new HashMap<>(); //Dummy Data //TODO use API instead
        hashMap.put(Instant.now().getEpochSecond(), 1f);
        hashMap.put(Instant.now().minus(1, ChronoUnit.DAYS).getEpochSecond(), 42f);
        hashMap.put(Instant.now().minus(2, ChronoUnit.DAYS).getEpochSecond(), 33f);
        hashMap.put(Instant.now().minus(3, ChronoUnit.DAYS).getEpochSecond(), 37f);
        hashMap.put(Instant.now().minus(4, ChronoUnit.DAYS).getEpochSecond(), 52f);
        return hashMap;
    }
}
