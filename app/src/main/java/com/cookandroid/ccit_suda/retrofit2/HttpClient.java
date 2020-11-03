package com.cookandroid.ccit_suda.retrofit2;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class HttpClient {

    private static Retrofit retrofit;

    // Http 통신을 위한 Retrofit 객체반환
    public static Retrofit getRetrofit() {
        if( retrofit == null )
        {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(10, TimeUnit.MINUTES)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(15, TimeUnit.SECONDS)
                    .build();
            Retrofit.Builder builder = new Retrofit.Builder();
            builder.baseUrl( "http://ccit2020.cafe24.com:8082/api/" );//http://ccit2020.cafe24.com:8082/http://10.0.2.2
//            builder.addConverterFactory( GsonConverterFactory.create() );  // 받아오는 Json 구조의 데이터를 객체 형태로 변환
            builder.addConverterFactory(ScalarsConverterFactory.create());  // String 등 처리시
            builder.client(okHttpClient);


            retrofit = builder.build();
        }

        return retrofit;
    }
}

