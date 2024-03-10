package com.example.mobileapp;

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

import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String API_URL = "http://192.168.0.105:5016/api/";
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
        asyncDataManager.getTableData(API_URL + "GetMedicines").thenAccept(data -> runOnUiThread(() -> fillTable(tableLayout, data)));
    }

    public void fillTable(TableLayout tableLayout, List<String[]> rows) {
        if (rows == null) {
            showNotification("Не удалось загрузить данные :(");
            return;
        }

        for(String[] row : rows) {
            TableRow tableRow = new TableRow(this);
            for (String collumn : row) {
                TextView textView = new TextView(this);
                textView.setGravity(Gravity.CENTER);
                textView.setLayoutParams(paramsName);
                textView.setText(collumn);
                tableRow.addView(textView);
            }
            tableLayout.addView(tableRow);
        }
    }

    public void showNotification(String text) {
        Toast toast = Toast.makeText(this, text, Toast.LENGTH_LONG);
        toast.show();
    }

    public void onButtonRefresh_Clicked(View view) {
        fillMedicinesTable();
    }
}

/*
List<String[]> rows = Arrays.asList(
        new String[] {"Парацетамол", "Пушкинский", "597"},
        new String[] {"Цитрамон", "Пушкинский", "987"},
        new String[] {"Нош-па", "Алексеевский", "687"}
);
*/