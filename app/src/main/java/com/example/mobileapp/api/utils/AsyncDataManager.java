package com.example.mobileapp.api.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class AsyncDataManager  {
    public static final String API_URL = "http://192.168.0.105:5016/api/Medicines/";
    private static final Map<HttpRequestType, String> RequestMethods = new HashMap<HttpRequestType, String>();

    static {
        RequestMethods.put(HttpRequestType.GET, "GET");
        RequestMethods.put(HttpRequestType.POST, "POST");
        RequestMethods.put(HttpRequestType.PUT, "PUT");
        RequestMethods.put(HttpRequestType.DELETE, "DELETE");
    }

    public CompletableFuture<JSONArray> getTableData(String stringURL) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                String jsonData = getResponse(new URL(API_URL + stringURL));

                if (jsonData == null)
                    return null;

                return new JSONArray(jsonData);

            } catch (JSONException | MalformedURLException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    public CompletableFuture<Integer> sendRequest(HttpRequestType requestType, String stringURL, JSONObject jsonBody) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                URL url = new URL(API_URL + stringURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod(RequestMethods.get(requestType));
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoOutput(true);

                if (jsonBody != null) {
                    OutputStream outputStream = connection.getOutputStream();
                    outputStream.write(jsonBody.toString().getBytes());
                    outputStream.flush();
                    outputStream.close();
                }
                return connection.getResponseCode();

            } catch (Exception e) {
                e.printStackTrace();
                return HttpURLConnection.HTTP_INTERNAL_ERROR;
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
