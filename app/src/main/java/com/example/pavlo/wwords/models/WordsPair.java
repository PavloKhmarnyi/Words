package com.example.pavlo.wwords.models;

/**
 * Created by pavlo on 21.07.16.
 */
public class WordsPair {

    private long id = -1;

    private String englishWord;
    private String ukrainianWord;

    public WordsPair(String englishWord, String ukrainianWord) {
        this.englishWord = englishWord;
        this.ukrainianWord = ukrainianWord;
    }

    public WordsPair(String englishWord, String ukrainianWord, long id) {
        this.englishWord = englishWord;
        this.ukrainianWord = ukrainianWord;

        this.id = id;
    }

    public String getEnglishWord() {
        return englishWord;
    }

    public String getUkrainianWord() {
        return ukrainianWord;
    }

    public long getId() {
        return id;
    }
}
