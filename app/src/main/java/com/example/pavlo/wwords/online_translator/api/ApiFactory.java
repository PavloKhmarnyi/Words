package com.example.pavlo.wwords.online_translator.api;

import com.example.pavlo.wwords.online_translator.models.AvailableLanguages;
import com.example.pavlo.wwords.online_translator.models.TranslateResult;
import com.example.pavlo.wwords.online_translator.utilities.Config;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by pavlo on 19.07.16.
 */
public class ApiFactory {

    private static final OkHttpClient CLIENT = new OkHttpClient();

    private ApiService apiService;

    public ApiFactory() {
        apiService = getService();
    }

    public static ApiService getService() {
        return getRetrofit().create(ApiService.class);
    }

    private static Retrofit getRetrofit() {
        return new Retrofit.Builder().
                baseUrl(Config.YANDEX_API_URL).
                addConverterFactory(GsonConverterFactory.create()).
                addCallAdapterFactory(RxJavaCallAdapterFactory.create()).
                client(CLIENT).
                build();
    }

    public Observable<AvailableLanguages> getAvailableLanguages(String ui) {
        Observable<AvailableLanguages> observable = apiService.
                getAvailableLanguages(Config.YANDEX_API_KEY, ui).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                cache();

        return observable;
    }

    public Observable<TranslateResult> translateText(String text, String lang) {
        Observable<TranslateResult> observable = apiService.
                translateText(Config.YANDEX_API_KEY, text, lang).
                subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).
                cache();

        return observable;
    }
}
