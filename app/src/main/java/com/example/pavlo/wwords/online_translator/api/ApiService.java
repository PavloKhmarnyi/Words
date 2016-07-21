package com.example.pavlo.wwords.online_translator.api;

import com.example.pavlo.wwords.online_translator.models.AvailableLanguages;
import com.example.pavlo.wwords.online_translator.models.TranslateResult;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by pavlo on 19.07.16.
 */
public interface ApiService {

    @GET("tr.json/getLangs")
    Observable<AvailableLanguages> getAvailableLanguages(@Query("key") String key, @Query("ui") String ui);

    @GET("tr.json/translate")
    Observable<TranslateResult> translateText(@Query("key") String key, @Query("text") String text, @Query("lang") String lang);
}
