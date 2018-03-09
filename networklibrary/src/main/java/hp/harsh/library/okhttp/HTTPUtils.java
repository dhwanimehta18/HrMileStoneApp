package hp.harsh.library.okhttp;

import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.Map;

public class HTTPUtils {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static OkHttpClient client = new OkHttpClient();

    public static String getRun(String url, Map<String, String> headers) throws IOException {
        Log.e(HTTPUtils.class.getSimpleName(), " URL:: "  + url);

        Request request = createRequestBuilder(url,null,headers).build();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    public static String postRun(String url, String json, Map<String, String> headers) throws IOException {
        Log.e(HTTPUtils.class.getSimpleName(), "URL:: " + url);
        Log.e(HTTPUtils.class.getSimpleName(), "Request Parameter:: " + json);

        Request request = createRequestBuilder(url,json,headers,JSON).build();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    public static String postRun(String url, RequestBody formBody, Map<String, String> headers) throws IOException {
        Log.e(HTTPUtils.class.getSimpleName(), " URL:: "  + url);

        Request request = createRequestBuilder(url,formBody,headers).build();
        Response response = client.newCall(request).execute();

        return response.body().string();
    }

    // Default method to create request builder
    private static Request.Builder createRequestBuilder(String url, RequestBody formBody, Map<String, String> headers) {
        // Create object of request builder
        Request.Builder buildRequest = new Request.Builder();
        buildRequest.url(url);

        // Add headers if available
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                buildRequest.addHeader(entry.getKey(), entry.getValue());
            }
        }

        // It only call when there are POST request. For GET, it is not required
        if (formBody != null) {
            buildRequest.post(formBody);
        }

        return buildRequest;
    }

    // When user select MediaType JSON, this method should call
    private static Request.Builder createRequestBuilder(String url, String formBody, Map<String, String> headers, MediaType mediaType) {
        // Create object of request builder
        Request.Builder buildRequest = new Request.Builder();
        buildRequest.url(url);

        // Add headers if available
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                buildRequest.addHeader(entry.getKey(), entry.getValue());
            }
        }

        // It only call when there are POST request. For GET, it is not required
        if (formBody != null) {
            RequestBody body = RequestBody.create(mediaType, formBody);
            buildRequest.post(body);
        }

        return buildRequest;
    }
}