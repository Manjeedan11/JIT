package com.affc.ait.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GeoLocationParser {

    private double lat, lng;
    private String link;

    private static final String AZURE_MAPS_SUBSCRIPTION_KEY = "YuAD1rFoXVTjQa_-EmN-0zcFygvuCAsrncE4LVdOd0k";


    public double[] parseLocation(String location) {
        double[] coods = new double[2];
        //find location with azure maps

        OkHttpClient client = new OkHttpClient();

        String url = "https://atlas.microsoft.com/search/address/json?api-version=1.0&subscription-key=" + AZURE_MAPS_SUBSCRIPTION_KEY + "&query="+ location;

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            try {
                JSONObject jsonObject = new JSONObject(response.body().string());
                Log.d("JSON", jsonObject.toString());
                JSONArray results = jsonObject.getJSONArray("results");
                Log.d("JSON", results.toString());
                JSONObject firstResult = results.getJSONObject(0);
                Log.d("JSON", firstResult.toString());
                JSONObject position = firstResult.getJSONObject("position");
                coods[0] = position.getDouble("lat");
                coods[1] = position.getDouble("lon");
                System.out.println("Latitude: " + coods[0]);
                System.out.println("Longitude: " + coods[1]);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        return coods;
    }




}
