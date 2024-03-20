package com.example.mobileapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.mobileapp.utils.AsyncDataManager;
import com.example.mobileapp.utils.HttpRequestType;
import com.example.mobileapp.utils.NotificationManager;

import org.json.JSONException;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.Objects;

public class EditActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_GALLERY = 1;
    Medicines originalMedicine;
    EditText editTextName;
    EditText editTextStorage;
    EditText editTextCount;
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

        editTextName = findViewById(R.id.editTextName);
        editTextStorage = findViewById(R.id.editTextStorage);
        editTextCount = findViewById(R.id.editTextCount);
        imageView = findViewById(R.id.imageView);

        fillData();
    }

    private void fillData() {
        Bundle extras = getIntent().getExtras();

        if(extras == null) {
            goToBackActivity();
            return;
        }

        originalMedicine = getIntent().getParcelableExtra("Medicines");
        editTextName.setText(originalMedicine.getName());
        editTextStorage.setText(originalMedicine.getStorage());
        editTextCount.setText(String.valueOf(originalMedicine.getCount()));

        Bitmap image = originalMedicine.getPhoto();
        if (image != null)
            imageView.setImageBitmap(image);
    }

    private void updateMedicine(Medicines medicine) {
        try {
            AsyncDataManager.sendRequest(HttpRequestType.PUT, "updateMedicine?id=" + medicine.getID(), medicine.toJSONObject()).thenAccept(data -> runOnUiThread(() -> onUpdateMedicine(medicine, data)));
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    private void goToBackActivity()
    {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    //********************События********************//
    public void onImageViewBack_Clicked(View view) {
        goToBackActivity();
    }

    public void onImageView_Clicked(View view) {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryIntent, REQUEST_IMAGE_GALLERY);
    }

    public void onButtonEdit_Clicked(View view) {
        try {
            String name = editTextName.getText().toString();
            String storage = editTextStorage.getText().toString();
            int count = Integer.parseInt(editTextCount.getText().toString());
            Bitmap photo = null;

            Drawable drawable = imageView.getDrawable();
            if (!(Objects.equals(drawable, ContextCompat.getDrawable(this, android.R.drawable.ic_menu_gallery))))
                photo = ((BitmapDrawable) drawable).getBitmap();

            updateMedicine(new Medicines(originalMedicine.getID(), name, storage, count, photo));
        } catch (NumberFormatException e) {
            NotificationManager.showNotification(this, "Произошла ошибка при изменении препарата, входные данные невалидны");
        }
    }

    private void onUpdateMedicine(Medicines medicine, int status) {
        if (status == HttpURLConnection.HTTP_OK) {
            NotificationManager.showNotification(this, "Препарат " + medicine.getName() + " успешно изменен");
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else
            NotificationManager.showNotification(this, "Произошла ошибка при изменении препарата - " + medicine.getName());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                ImageView imageView = findViewById(R.id.imageView);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                NotificationManager.showNotification(this, "Не удалось загрузить изображение");
                e.printStackTrace();
            }
        }
    }
}