/*
* Copyright 2016 Harsh Patel
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package hp.harsh.library.okhttp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.RequestBody;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import hp.harsh.library.interfaces.OkHttpInterface;

public class OkHttpRequest {

    private static final String TAG = "OkHttpRequest";
    private String mUrl = "";
    private ProgressDialog mProgressDialog;
    private Activity mActivity;
    private RequestBody mRequestBody;
    private Map<String, String> mBody = new HashMap<String, String>();
    private Map<String, String> mHeaders = new HashMap<String, String>();
    private Map<String, File> mFile = null;
    private boolean mIsShowProgressDialog;
    private int mRequestId;
    private Method mRequestType;
    private OkHttpInterface mObjInterface;

    // Get all requested parameter and initiate API call without File
    public OkHttpRequest(Activity activity, Method requestType, String url, Map<String,String> params, Map<String, String> headers, int type, boolean isShowDialog, OkHttpInterface objInterface) {
        this.mActivity = activity;
        this.mRequestType = requestType;
        this.mBody = params;
        this.mHeaders = headers;
        this.mFile = null;
        this.mIsShowProgressDialog = isShowDialog;
        this.mRequestId = type;
        this.mObjInterface = objInterface;
        this.mRequestBody = null;
        this.mUrl = url;

        // Call API Request
        callWebApiRequest();
    }

    // Get all requested parameter and initiate API call with File
    public OkHttpRequest(Activity activity, Method requestType, String url, Map<String,String> params, Map<String, String> headers, Map<String, File> files, int type, boolean isShowDialog, OkHttpInterface objInterface) {
        this.mActivity = activity;
        this.mRequestType = requestType;
        this.mBody = params;
        this.mHeaders = headers;
        this.mFile = files;
        this.mIsShowProgressDialog = isShowDialog;
        this.mRequestId = type;
        this.mObjInterface = objInterface;
        this.mRequestBody = null;
        this.mUrl = url;

        // Call API Request
        callWebApiRequest();
    }

    public OkHttpRequest(Activity activity, Method get, String url, int type, boolean isShowDialog, OkHttpInterface objInterface) {

        this.mActivity = activity;
        this.mRequestType = get;
        this.mIsShowProgressDialog = isShowDialog;
        this.mRequestId = type;
        this.mObjInterface = objInterface;
        this.mRequestBody = null;
        this.mUrl = url;
        callWebApiRequest();
    }

    /*GET method REQUEST*/

    private synchronized void callWebApiRequest() {
        // Request Method
        switch (mRequestType) {
            case GET:
                Log.e(TAG, "URL==>" + mUrl);
                mUrl = generateGetUrl();
                new networkAsyncCall().execute();
                break;
            case POST:
                Log.e(TAG, "URL==>" + mUrl);
                mRequestBody = generatePostUrl();

                if (mRequestBody != null) {
                    new networkAsyncCall().execute();
                } else {
                    Log.e(TAG,"Add minimum one parameter");
                }
                break;
            case DELETE:
                Log.e(TAG, "URL==>" + mUrl);
//                LoopjApiInitializer.delete(mUrl, requestParams, mHeaders, asyncHttpResponseHandler);
                break;
            default:
                break;
        }
    }

    // Show progress
    private void showProgress() {
        if (mIsShowProgressDialog) {
            mProgressDialog = getProgressDialog(mActivity);
        }
    }

    // Dismiss progress
    private void dismissProgress() {
        if (mIsShowProgressDialog) {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                mProgressDialog.dismiss();
            }
        }
    }

    private ProgressDialog getProgressDialog(Context c) {
        ProgressDialog pDialog = new ProgressDialog(c);
        pDialog.setMessage("Please wait..");
        pDialog.setCancelable(false);
        pDialog.show();
        return pDialog;
    }

    // Create Get url using HashMap
    private String generateGetUrl() {
        boolean isFirstParam = true;
        for ( String key : mBody.keySet() ) {
            if (isFirstParam) {
                mUrl = mUrl + "?" + key + "=" + mBody.get(key);
                isFirstParam = false;
            } else {
                mUrl = mUrl + "&" + key + "=" + mBody.get(key);
            }
        }
        return  mUrl;
    }

    // Create RequestBody using HashMap
    private RequestBody generatePostUrl() {
        if (mBody != null && mBody.size() < 1) {
            return null;
        }

        MultipartBuilder multipartBuilder = new MultipartBuilder().type(MultipartBuilder.FORM);
        for ( String key : mBody.keySet() ) {
            multipartBuilder.addFormDataPart(""+key,""+mBody.get(key));
        }

        // Add image files if available
        if (mFile != null) {
            for (String key : mFile.keySet()) {
                if (mFile.get(""+key) != null) {
                    multipartBuilder.addFormDataPart("" + key, "file", RequestBody.create(MediaType.parse("image/*"), new File(mFile.get("" + key).toString())));
                }
            }
        }

        RequestBody formBody = multipartBuilder.build();
        return  formBody;
    }

    // Request method
    public enum Method {
        POST, GET, DELETE
    }

    // Call request in background thread
    public class networkAsyncCall extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {
            showProgress();
            mObjInterface.onOkHttpStart(mRequestId);
        }

        @Override
        protected String doInBackground(Void... voids) {
            String response = ""+null;
            try {
                if (mRequestType==Method.POST) {
                    response = HTTPUtils.postRun(mUrl, mRequestBody, mHeaders);
                }
                if (mRequestType==Method.GET) {
                    response = HTTPUtils.getRun(mUrl, mHeaders);
                }

            } catch (final IOException e) {
                final String finalResponse = response;
                if (mActivity != null) {
                    mActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mObjInterface.onOkHttpFailure(mRequestId, 0, finalResponse, e);
                        }
                    });
                }
                e.printStackTrace();
            }
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                mObjInterface.onOkHttpSuccess(mRequestId, 1, response);
            }
            dismissProgress();
            mObjInterface.onOkHttpFinish(mRequestId);
        }
    }
}
