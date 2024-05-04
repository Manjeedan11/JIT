package com.affc.ait.utils;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GeoLocationParser {

    private static final String AZURE_MAPS_SUBSCRIPTION_KEY = "YuAD1rFoXVTjQa_-EmN-0zcFygvuCAsrncE4LVdOd0k";

    public double[] parseLocation(String location) throws ExecutionException, InterruptedException {
        Future<double[]> future = Executors.newSingleThreadExecutor().submit(() -> {
            double[] coods = new double[2];
            OkHttpClient client = new OkHttpClient();

            String url = "https://atlas.microsoft.com/search/address/json?api-version=1.0&subscription-key=" + AZURE_MAPS_SUBSCRIPTION_KEY + "&query="+ location;

            Request request = new Request.Builder()
                    .url(url)
                    .build();

            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    JSONArray results = jsonObject.getJSONArray("results");
                    JSONObject firstResult = results.getJSONObject(0);
                    JSONObject position = firstResult.getJSONObject("position");
                    coods[0] = position.getDouble("lat");
                    coods[1] = position.getDouble("lon");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return coods;
        });

        return future.get();
    }
}