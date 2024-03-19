package com.example.mobileapp;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class Medicines {
    private int ID;
    private String Name;
    private String Storage;
    private int Count;
    private String Photo = "";
    static public List<Medicines> currentMedicines = new ArrayList<>();

    public Medicines(String name, String storage, int count) {
        this.Name = name;
        this.Storage = storage;
        this.Count = count;
    }

    public Medicines(int ID, String name, String storage, int count, String photo) {
        this.ID = ID;
        this.Name = name;
        this.Storage = storage;
        this.Count = count;
        this.Photo = photo;
    }

    public Medicines(JSONObject jsonObject) {
        try {
            this.ID = jsonObject.getInt("id");
            this.Name = jsonObject.getString("name");
            this.Storage = jsonObject.getString("storage");
            this.Count = jsonObject.getInt("count");
            this.Photo = jsonObject.getString("photo");
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

    public JSONObject toJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", this.ID);
        jsonObject.put("name", this.Name);
        jsonObject.put("storage", this.Storage);
        jsonObject.put("count", this.Count);
        jsonObject.put("photo", this.Photo);
        return jsonObject;
    }
}
