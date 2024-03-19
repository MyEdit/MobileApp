package com.example.mobileapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EditActivity extends AppCompatActivity {

    TextView textViewName;
    TextView textViewStorage;
    TextView textViewCount;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        textViewName = findViewById(R.id.textViewName);
        textViewStorage = findViewById(R.id.textViewStorage);
        textViewCount = findViewById(R.id.textViewCount);
        imageView = findViewById(R.id.imageView);


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            Medicines medicine = getIntent().getParcelableExtra("Medicines");

            System.out.println(medicine.getName());
            textViewName.setText(medicine.getName());
            textViewStorage.setText(medicine.getStorage());
            textViewCount.setText(String.valueOf(medicine.getCount()));
            imageView.setImageBitmap(medicine.getImage());
        }
    }

    public void onImageViewBack_Clicked(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}