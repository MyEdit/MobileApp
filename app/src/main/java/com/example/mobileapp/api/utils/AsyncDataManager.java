package com.example.mobileapp.api.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.CompletableFuture;

public class AsyncDataManager  {
    public CompletableFuture<JSONArray> getTableData(String stringURL) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String jsonData = getResponse(new URL(stringURL));

                if (jsonData == null)
                    return null;

                JSONObject jsonObject = new JSONObject(jsonData);
                return jsonObject.getJSONArray("data");

            } catch (JSONException | MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    private static String getResponse(URL url) {
        BufferedReader in = null;
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000);
            connection.connect();

            in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString();

        } catch (SocketTimeoutException e) {
            System.out.println("Время ожидания соединения истекло");
        } catch (IOException e) {
            e.printStackTrace();
        }

        finally {
            try {
                if (connection != null)
                    connection.disconnect();

                if (in != null)
                    in.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
