package com.cdut.playtask.network;

import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GLMService {

    private static final String BASE_URL = "https://open.bigmodel.cn/";
    // TODO: 换成你的真实 API Key，切勿泄露到公网仓库
    private static final String API_KEY = "替换为你的API Key";

    private static GLMApi instance;

    public static GLMApi getInstance() {
        if (instance != null) return instance;

        // 日志拦截器
        HttpLoggingInterceptor log = new HttpLoggingInterceptor();
        log.setLevel(HttpLoggingInterceptor.Level.BODY);

        // 自定义 OkHttpClient：连接 / 读取 / 写入 超时统一 60 秒
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(log)
                .addInterceptor((Interceptor.Chain chain) -> {
                    Request original = chain.request();
                    Request req = original.newBuilder()
                            .header("Authorization", "Bearer " + API_KEY)
                            .header("Content-Type", "application/json")
                            .method(original.method(), original.body())
                            .build();
                    return chain.proceed(req);
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        instance = retrofit.create(GLMApi.class);
        return instance;
    }
}
