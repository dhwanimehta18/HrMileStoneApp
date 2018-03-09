package com.hrmilestoneapp;



import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.concurrent.TimeUnit;



public class HttpUtils {
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    public static OkHttpClient client = new OkHttpClient();

    public static String getRun(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.setConnectTimeout(90, TimeUnit.SECONDS);
        client.setReadTimeout(90, TimeUnit.SECONDS);
        client.setWriteTimeout(90, TimeUnit.SECONDS);
        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public static String postRun(String type, RequestBody formBody) throws IOException {

        Response response = null;

        Request request = new Request.Builder()
                .url(type)
                .post(formBody)
                .build();
        client.setConnectTimeout(90, TimeUnit.SECONDS);
        client.setReadTimeout(90, TimeUnit.SECONDS);
        client.setWriteTimeout(90, TimeUnit.SECONDS);
        response = client.newCall(request).execute();
        return response.body().string();
    }
}