package com.e.wixmovies.repo;


import android.content.Context;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class TMDBRetrofitInstance {

    private static Retrofit retrofitTMDb = null;
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    public static final String BASE_THUMBNAIL_URL = "https://image.tmdb.org/t/p/w500";
    private static OkHttpClient okHttpClient;
    private static int cacheSize = 10 * 1024 * 1024; // 10 MB
    private static Cache cache;


    public static ITMDBService getTMDBService(Context context) {
        if (okHttpClient == null) {
            initOkHttp(context);
        }
        if (retrofitTMDb == null) {
            retrofitTMDb = new Retrofit.Builder().baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofitTMDb.create(ITMDBService.class);
    }

    private static void initOkHttp(Context context) {
        cache = new Cache(context.getCacheDir(), cacheSize);
        OkHttpClient.Builder httpClient = new OkHttpClient().newBuilder()
                .cache(cache);
        okHttpClient = httpClient.build();
    }

    public static void resetCache() {
        try {
            cache.evictAll();
        }
        catch (Throwable e) {
          //do nothing
        }
    }
}
