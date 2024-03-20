package com.example.mobileapp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Base64;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

//ALT + INSERT
public class Medicines implements Parcelable {
    private int ID;
    private String Name;
    private String Storage;
    private int Count;
    private String Photo = "";
    static public List<Medicines> currentMedicines = new ArrayList<>();

    public Medicines(Parcel in) {
        this.ID = in.readInt();
        this.Name = in.readString();
        this.Storage = in.readString();
        this.Count = in.readInt();
        this.Photo = in.readString();
    }

    public Medicines(String name, String storage, int count) {
        this.Name = name;
        this.Storage = storage;
        this.Count = count;
    }

    public Medicines(int ID, String name, String storage, int count, Bitmap photo) {
        this.ID = ID;
        this.Name = name;
        this.Storage = storage;
        this.Count = count;
        if (photo == null)
            this.Photo = "";
        else {
            this.Photo = getPhoto(photo);
            compressBase64Image();
        }
    }

    public Medicines(JSONObject jsonObject) {
        try {
            this.ID = jsonObject.getInt("id");
            this.Name = jsonObject.getString("name");
            this.Storage = jsonObject.getString("storage");
            this.Count = jsonObject.getInt("count");
            this.Photo = jsonObject.getString("photo");
            compressBase64Image();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public int getID() {
        return ID;
    }

    public String getName() {
        return Name;
    }

    public String getStorage() {
        return Storage;
    }

    public int getCount() {
        return Count;
    }

    public Bitmap getPhoto() {
        if (Photo == null || Photo.isEmpty())
            return null;

        byte[] array = Base64.decode(Photo, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(array, 0, array.length);
    }

    private String getPhoto (Bitmap image) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

        byte[] imageBytes = outputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.ID);
        jsonObject.put("name", this.Name);
        jsonObject.put("storage", this.Storage);
        jsonObject.put("count", this.Count);
        jsonObject.put("photo", this.Photo);
        return jsonObject;
    }

    private void compressBase64Image() {
        if (Photo == null || Photo.isEmpty())
            return;

        int quality = 100;
        int maxSize = (int) (0.1 * 1024 * 1024);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] decodedString = Base64.decode(Photo, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);

        while (outputStream.size() > maxSize && quality > 0) {
            outputStream.reset();
            quality -= 5;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
        }

        byte[] compressedImageBytes = outputStream.toByteArray();
        Photo = Base64.encodeToString(compressedImageBytes, Base64.DEFAULT);
    }

    public static final Creator<Medicines> CREATOR = new Creator<>() {
        @Override
        public Medicines createFromParcel(Parcel in) {
            return new Medicines(in);
        }

        @Override
        public Medicines[] newArray(int size) {
            return new Medicines[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(ID);
        dest.writeString(Name);
        dest.writeString(Storage);
        dest.writeInt(Count);
        dest.writeString(Photo);
    }
}
