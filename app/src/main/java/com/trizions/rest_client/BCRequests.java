package com.trizions.rest_client;

import android.net.Uri;


import com.trizions.model.common.ErrorPojo;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BCRequests {
    private static BCRequests instance = null;
    private BCRestService bcRestService;
    private Retrofit bcRetrofit;
    private static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
            .connectTimeout(100, TimeUnit.SECONDS)
            .readTimeout(250, TimeUnit.SECONDS)
            .writeTimeout(250, TimeUnit.SECONDS);


    public static BCRequests getInstance() {
        if (instance == null) {
            instance = new BCRequests();
        }
        return instance;
    }

    private BCRequests() {
        bcRetrofit = new Retrofit.Builder()
                .client(httpClient.build())
                .baseUrl(Uri.encode(BuildConfig.FD_URL, ALLOWED_URI_CHARS))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        bcRestService = bcRetrofit.create(BCRestService.class);
    }

    public BCRestService getBCRestService() {
        return bcRestService;
    }

    public ErrorPojo parseError(retrofit2.Response<?> response) {
        Converter<ResponseBody, ErrorPojo> converter =
                bcRetrofit.responseBodyConverter(ErrorPojo.class, new Annotation[0]);
        ErrorPojo error;
        try {
            if(response.errorBody() != null)
                error = converter.convert(response.errorBody());
            else
                return new ErrorPojo();
        } catch (IOException e) {
            return new ErrorPojo();
        }
        return error;
    }
}
