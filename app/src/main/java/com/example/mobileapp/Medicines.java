package com.example.mobileapp;

import org.json.JSONException;
import org.json.JSONObject;

public class Medicines {
    public int ID;
    public String Name;
    public String Storage;
    public int Count;

    public Medicines(int ID, String Name, String Storage, int Count)
    {
        this.ID = ID;
        this.Name = Name;
        this.Storage = Storage;
        this.Count = Count;
    }

    public JSONObject toJSONObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ID", this.ID);
        jsonObject.put("Name", this.Name);
        jsonObject.put("Storage", this.Storage);
        jsonObject.put("Count", this.Count);
        return jsonObject;
    }
}
