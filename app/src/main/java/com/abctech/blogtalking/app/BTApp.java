package com.abctech.blogtalking.app;

import com.abctech.blogtalking.api.BTRestService;
import com.abctech.blogtalking.model.BTError;
import com.abctech.blogtalking.util.JacksonUtil;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;


public class BTApp extends BaseApp {
    public static BTApp instance;

    // Rest API;
    private static BTRestService                    mRestApi;
    private static Converter<ResponseBody, BTError> mRetrofitConverter;

    @Override
    public void onCreate() {
        super.onCreate();

        mAppContext = getApplicationContext();

        // Realm
        Realm.init(this);
        RealmConfiguration config = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(config);

        // Init Retrofit2
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(BTRestService.BASE_URL);
        builder.client(makeOkHttpClient());
        builder.addConverterFactory(JacksonConverterFactory.create(JacksonUtil.getInstance().getJackson()));
        builder.addCallAdapterFactory(RxJavaCallAdapterFactory.create());
        Retrofit retrofit = builder.build();
        mRetrofitConverter = retrofit.responseBodyConverter(BTError.class, new Annotation[0]);

        mRestApi = retrofit.create(BTRestService.class);
    }

    private OkHttpClient makeOkHttpClient() {
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder();

        // Time out
        okHttpBuilder.retryOnConnectionFailure(true);
        okHttpBuilder.connectTimeout(30, TimeUnit.SECONDS);
        okHttpBuilder.readTimeout(30, TimeUnit.SECONDS);
        okHttpBuilder.writeTimeout(60, TimeUnit.SECONDS);

        // logging
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpBuilder.addInterceptor(interceptor);

        return okHttpBuilder.build();
    }

    public static BTRestService getRestService() {
        return mRestApi;
    }

    public static BTError parseError(Response<?> response) {
        BTError error;
        try {
            error = mRetrofitConverter.convert(response.errorBody());
        } catch (IOException e) {
            return new BTError();
        }

        return error;
    }
}
