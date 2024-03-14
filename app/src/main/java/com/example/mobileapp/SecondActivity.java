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

import com.example.mobileapp.api.utils.AsyncDataManager;
import com.example.mobileapp.api.utils.HttpRequestType;

import org.json.JSONException;

import java.net.HttpURLConnection;

public class SecondActivity extends AppCompatActivity {
    private static final AsyncDataManager asyncDataManager = new AsyncDataManager();

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

    public void onButtonAddMedicine_Clicked(View view) {
        try {
            int id = Integer.parseInt(((EditText)findViewById(R.id.ID)).getText().toString());
            String name = ((EditText)findViewById(R.id.Name)).getText().toString();
            String storage = ((EditText)findViewById(R.id.Storage)).getText().toString();
            int count = Integer.parseInt(((EditText)findViewById(R.id.Count)).getText().toString());

            addMedicine(new Medicines(id, name, storage, count));
        } catch (NumberFormatException e) {
            showNotification("Произошла ошибка при добавлении препарата, входные данные невалидны");
        }
    }

    public void addMedicine(Medicines medicine) {
        try {
            asyncDataManager.sendRequest(HttpRequestType.POST, "addMedicine", medicine.toJSONObject()).thenAccept(data -> runOnUiThread(() -> onAddMedicine(medicine, data)));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void onAddMedicine(Medicines medicine, int status) {
        if (status == HttpURLConnection.HTTP_OK) {
            showNotification("Препарат " + medicine.Name + " успешно добавлен");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else
            showNotification("Произошла ошибка при добавлении препарата - " + medicine.Name);
    }

    public void showNotification(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        toast.show();
    }
}