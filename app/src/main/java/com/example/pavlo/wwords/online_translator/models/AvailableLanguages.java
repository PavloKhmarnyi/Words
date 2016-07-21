package com.example.pavlo.wwords.online_translator.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by pavlo on 19.07.16.
 */
public class AvailableLanguages {

    @SerializedName("dirs")
    private String[] dirs;

    @SerializedName("langs")
    private LinkedHashMap<String, String> langs;

    public String[] getDirs() {
        return dirs;
    }

    public LinkedHashMap<String, String> getLangs() {
        return langs;
    }
}
