package com.example.pavlo.wwords.online_translator.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by pavlo on 19.07.16.
 */
public class TranslateResult {

    @SerializedName("code")
    private int code;

    @SerializedName("lang")
    private String lang;

    @SerializedName("text")
    private String[] text;

    public int getCode() {
        return code;
    }

    public String getLang() {
        return lang;
    }

    public String[] getText() {
        return text;
    }
}
