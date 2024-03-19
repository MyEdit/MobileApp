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

    public Medicines(Parcel in)
    {
        this.ID = in.readInt();
        this.Name = in.readString();
        this.Storage = in.readString();
        this.Count = in.readInt();
        this.Photo = in.readString();
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

    public String getPhoto()
    {
        return this.Photo;
    }

    public Bitmap getImage() {
        byte[] array = Base64.decode(Photo, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(array, 0, array.length);
    }

    public Medicines(String name, String storage, int count) {
        this.Name = name;
        this.Storage = storage;
        this.Count = count;
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

    public static final Creator<Medicines> CREATOR = new Creator<Medicines>() {
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

    public JSONObject toJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.ID);
        jsonObject.put("name", this.Name);
        jsonObject.put("storage", this.Storage);
        jsonObject.put("count", this.Count);
        jsonObject.put("photo", this.Photo);
        return jsonObject;
    }

    public void compressBase64Image() {
        byte[] decodedString = Base64.decode(Photo, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);

        if (bitmap == null)
            return;

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, outputStream); // уменьшаем качество изображения до 50%

        byte[] compressedImageBytes = outputStream.toByteArray();

        Photo = Base64.encodeToString(compressedImageBytes, Base64.DEFAULT);
    }
}
