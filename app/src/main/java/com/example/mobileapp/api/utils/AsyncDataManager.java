package com.example.mobileapp.api.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

public class AsyncDataManager  {
    public CompletableFuture<List<String[]>> getTableData(String stringURL) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                URL url = new URL(stringURL);
                return Arrays.stream(
                                getResponse(url).split("\\],\\["))
                        .map(str -> str.split(","))
                        .collect(Collectors.toList());

            } catch (NullPointerException | MalformedURLException e) {
                return null;
            }
        });
    }

    private static String getResponse(URL url) {
        try {
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(5000);
            connection.connect();

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            return response.toString()
                    .replace("[[", "")
                    .replace("]]", "")
                    .replace("\"", "");

        } catch (SocketTimeoutException e) {
            System.out.println("Время ожидания соединения истекло");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
