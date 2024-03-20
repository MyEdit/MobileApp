package com.example.mobileapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mobileapp.utils.AsyncDataManager;
import com.example.mobileapp.utils.HttpRequestType;
import com.example.mobileapp.utils.NotificationManager;

import org.json.JSONArray;
import org.json.JSONException;

import java.net.HttpURLConnection;

public class MainActivity extends AppCompatActivity {
    private static final TableRow.LayoutParams paramsName = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
    Medicines selectedMedicine = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        fillMedicinesTable();
    }

    private void fillMedicinesTable() {
        TableLayout tableLayout = findViewById(R.id.mainTable);
        AsyncDataManager.getTableData("getMedicines").thenAccept(data -> runOnUiThread(() -> fillTable(tableLayout, data)));
    }

    public void deleteMedicine(Medicines medicine) {
        if (medicine == null)
            return;

        AsyncDataManager.sendRequest(HttpRequestType.DELETE, "deleteMedicine?id=" + medicine.getID(), null).thenAccept(data -> runOnUiThread(() -> onDeleteMedicine(medicine, data)));
    }

    public void fillTable(TableLayout tableLayout, JSONArray rows) {
        if (rows == null) {
            NotificationManager.showNotification(this, "Не удалось загрузить данные :(");
            return;
        }

        resetMedicines(tableLayout);

        for (int i = 0; i < rows.length(); i++) {
            try {
                Medicines medicines = new Medicines(rows.getJSONObject(i));
                addToTable(tableLayout, medicines);
                Medicines.currentMedicines.add(medicines);
            } catch (JSONException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private void addToTable(TableLayout tableLayout, Medicines medicines) throws IllegalAccessException {
        String[] values = {String.valueOf(medicines.getID()), medicines.getName(), medicines.getStorage(), String.valueOf(medicines.getCount())};
        TableRow tableRow = new TableRow(this);

        for (String value : values) {
            TextView textView = new TextView(this);
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(paramsName);
            textView.setText(value);
            tableRow.addView(textView);
        }

        tableRow.setOnClickListener(v -> {
            for (int j = 0; j < tableLayout.getChildCount(); j++) {
                TableRow currentRow = (TableRow) tableLayout.getChildAt(j);
                currentRow.setBackgroundColor(Color.rgb(20,18,24));
            }
            tableRow.setBackgroundColor(Color.rgb(34, 31, 41));
            selectedMedicine = medicines;
        });

        tableLayout.addView(tableRow);
    }

    private void resetMedicines(TableLayout tableLayout)
    {
        tableLayout.removeViews(1, tableLayout.getChildCount() - 1);
        Medicines.currentMedicines.clear();
        selectedMedicine = null;
    }

    //**********События работы AsyncDataManager**********//
    private void onDeleteMedicine(Medicines medicine, int status) {
        if (status == HttpURLConnection.HTTP_OK) {
            NotificationManager.showNotification(this, "Препарат " + medicine.getName() + " успешно удален");
            fillMedicinesTable();
        }
        else
            NotificationManager.showNotification(this, "Произошла ошибка при удалении препарата - " + medicine.getName());
    }

    //**********События кнопок**********//
    public void onButtonRefresh_Clicked(View view) {
        fillMedicinesTable();
        NotificationManager.showNotification(this, "Данные успешно обновлены");
    }

    public void onButtonAddMedicine_Clicked(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }

    public void onButtonDeleteMedicine_Clicked(View view) {
        deleteMedicine(selectedMedicine);
    }

    public void onButtonEditMedicine_Clicked(View view) {
        if (selectedMedicine == null)
            return;

        Intent intent = new Intent(this, EditActivity.class);
        intent.putExtra("Medicines", selectedMedicine);
        startActivity(intent);
    }

    public void onButtonMoreDetailsMedicine_Clicked(View view) {

    }
}
/*
List<String[]> rows = Arrays.asList(
        new String[] {"Парацетамол", "Пушкинский", "597"},
        new String[] {"Цитрамон", "Пушкинский", "987"},
        new String[] {"Нош-па", "Алексеевский", "687"}
);
*/