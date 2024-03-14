package com.example.mobileapp;

import android.content.Intent;
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

import com.example.mobileapp.api.utils.AsyncDataManager;
import com.example.mobileapp.api.utils.HttpRequestType;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final AsyncDataManager asyncDataManager = new AsyncDataManager();
    private static final TableRow.LayoutParams paramsName = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);

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
        asyncDataManager.getTableData("getMedicines").thenAccept(data -> runOnUiThread(() -> fillTable(tableLayout, data)));
    }

    public void fillTable(TableLayout tableLayout, JSONArray rows) {
        if (rows == null) {
            showNotification("Не удалось загрузить данные :(");
            return;
        }

        tableLayout.removeViews(1, tableLayout.getChildCount() - 1);

        for (int i = 0; i < rows.length(); i++) {
            try {
                JSONObject itemObject = rows.getJSONObject(i);
                addToTable(tableLayout, itemObject);
            } catch (JSONException e) {e.printStackTrace();}
        }
    }

    public void addToTable(TableLayout tableLayout, JSONObject itemObject) throws JSONException {
        TableRow tableRow = new TableRow(this);
        Iterator<String> keys = itemObject.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            TextView textView = new TextView(this);
            textView.setGravity(Gravity.CENTER);
            textView.setLayoutParams(paramsName);
            textView.setText(itemObject.getString(key));
            tableRow.addView(textView);
        }
        tableLayout.addView(tableRow);
    }

    public void showNotification(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        toast.show();
    }

    public void onButtonRefresh_Clicked(View view) {
        fillMedicinesTable();
        showNotification("Данные успешно обновлены");
    }

    public void onButtonAddMedicine_Clicked(View view) {
        Intent intent = new Intent(this, SecondActivity.class);
        startActivity(intent);
    }
}

/*
List<String[]> rows = Arrays.asList(
        new String[] {"Парацетамол", "Пушкинский", "597"},
        new String[] {"Цитрамон", "Пушкинский", "987"},
        new String[] {"Нош-па", "Алексеевский", "687"}
);
*/