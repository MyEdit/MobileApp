package com.example.mobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mobileapp.utils.AsyncDataManager;
import com.example.mobileapp.utils.HttpRequestType;
import com.example.mobileapp.utils.NotificationManager;

import org.json.JSONException;

import java.net.HttpURLConnection;

public class SecondActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_second);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void addMedicine(Medicines medicine) {
        try {
            AsyncDataManager.sendRequest(HttpRequestType.POST, "addMedicine", medicine.toJSONObject()).thenAccept(data -> runOnUiThread(() -> onAddMedicine(medicine, data)));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    //**********События работы AsyncDataManager**********//
    private void onAddMedicine(Medicines medicine, int status) {
        if (status == HttpURLConnection.HTTP_OK) {
            NotificationManager.showNotification(this, "Препарат " + medicine.getName() + " успешно добавлен");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else
            NotificationManager.showNotification(this, "Произошла ошибка при добавлении препарата - " + medicine.getName());
    }

    //**********События кнопок**********//
    public void onButtonAddMedicine_Clicked(View view) {
        try {
            String name = ((EditText)findViewById(R.id.Name)).getText().toString();
            String storage = ((EditText)findViewById(R.id.Storage)).getText().toString();
            int count = Integer.parseInt(((EditText)findViewById(R.id.Count)).getText().toString());

            addMedicine(new Medicines(name, storage, count));
        } catch (NumberFormatException e) {
            NotificationManager.showNotification(this, "Произошла ошибка при добавлении препарата, входные данные невалидны");
        }
    }
}