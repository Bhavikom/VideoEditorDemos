package com.meishe.sdkdemo.capturescene.httputils;


import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.meishe.sdkdemo.capturescene.httputils.upload.CountingRequestBody;
import com.meishe.sdkdemo.capturescene.httputils.upload.UpLoadResultCallBack;
import com.google.gson.Gson;
import com.meishe.sdkdemo.capturescene.httputils.download.DownLoadResultCallBack;
import com.meishe.sdkdemo.capturescene.httputils.download.DownloadInfo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by CaoZhiChao on 2018/11/30 19:07
 */
public class OkHttpClientManager {
    private static final String TAG = "OkHttpClientManager";
    private static final String SESSION_KEY = "Set-Cookie";
    private static final String mSessionKey = "JSESSIONID";
    private static OkHttpClientManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler uiHandler;
    private Gson mGoon;
    private Map<String, String> mSessions = new HashMap<String, String>();

    private OkHttpClientManager() {
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        uiHandler = new Handler(Looper.getMainLooper());
        mGoon = new Gson();
    }

    /**
     * 同步的Get请求
     *
     * @param url
     * @return Response
     */
    public static Response getAsyn(String url) throws IOException {
        Response response = getInstance()._getAsyn(url);
        if (!response.isSuccessful()) {
            throw new IOException("Unexpected code " + response);
        }
        return response;
    }

    private Response _getAsyn(String url) throws IOException {
        final Request request = new Request.Builder()
                .url(url)
                .build();
        Call call = mOkHttpClient.newCall(request);
        Response execute = call.execute();
        return execute;
    }

    private static OkHttpClientManager getInstance() {
        if (mInstance == null) {
            synchronized (OkHttpClientManager.class) {
                if (mInstance == null) {
                    mInstance = new OkHttpClientManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 同步的Get请求
     *
     * @param url
     * @return 字符串
     */
    public static String getAsString(String url) throws IOException {
        return getInstance()._getAsString(url);
    }

    private String _getAsString(String url) throws IOException {
        Response execute = _getAsyn(url);
        return execute.body().string();
    }

    /**
     * 异步的get请求
     *
     * @param url
     * @param callback 泛型返回解析以后的内容
     */
    public static void getAsyn(String url, ResultCallback callback) {
        getInstance()._getAsyn(url, callback);
    }

    private void _getAsyn(String url, final ResultCallback callback) {
        final Request request = new Request.Builder()
                .url(url)
                .tag(url)
                .build();
        deliveryResult(callback, request);
    }

    private void deliveryResult(final ResultCallback callback, Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull final Call call, @NonNull final IOException e) {
                if (e.toString().contains("closed")) {
                    //如果是主动取消的情况下
                } else {
                    //其他情况下
                    sendFailedStringCallback(call.request(), e, callback);
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull final Response response) {
                try {
                    final String string = response.body().string();
                    if (callback.mType == String.class) {
                        sendSuccessResultCallback(string, callback);
                    } else {
                        Object o = mGoon.fromJson(string, callback.mType);
                        sendSuccessResultCallback(o, callback);
                    }
                } catch (IOException | com.google.gson.JsonParseException | NullPointerException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                }
            }

        });
    }

    private void sendFailedStringCallback(final Request request, final Exception e, final ResultCallback callback) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onError(request, e);
                }
            }
        });
    }

    private void sendSuccessResultCallback(final Object object, final ResultCallback callback) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onResponse(object);
                }
            }
        });
    }

    private void sendProgressCallback(final long byteWritten, final long contentLength, final ResultCallback callback) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    if (callback instanceof UpLoadResultCallBack) {
                        ((UpLoadResultCallBack) callback).onProgress(byteWritten, contentLength, (int) (byteWritten * 1.0 / contentLength * 100));
                    } else if (callback instanceof DownLoadResultCallBack) {
                        ((DownLoadResultCallBack) callback).onProgress(byteWritten, contentLength, (int) (byteWritten * 1.0 / contentLength * 100));
                    }
                }
            }
        });
    }

    public static Response post(String url, Param... params) throws IOException {
        return getInstance()._post(url, params);
    }

    /**
     * 同步的Post请求
     *
     * @param url
     * @param params post的参数
     * @return
     */
    private Response _post(String url, Param... params) throws IOException {
        Request request = buildPostRequest(url, params);
        Response response = mOkHttpClient.newCall(request).execute();
        return response;
    }

    private Request buildPostRequest(String url, Param[] params) {
        if (params == null) {
            params = new Param[0];
        }
        FormBody.Builder builder = new FormBody.Builder();
        for (Param param : params) {
            builder.add(param.key, param.value);
        }
        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .tag(url)
                .url(url)
                .post(requestBody)
                .build();
    }

    private Request buildPostRequest_Json(String url, String json) {
        if (TextUtils.isEmpty(url) || TextUtils.isEmpty(json)) {
            throw new NullPointerException("参数不能为空！");
        }
        MediaType JSON = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(JSON, json);
        return new Request.Builder()
                .url(url)
                .post(body)
                .build();
    }

    public static String postAsString(String url, Param... params) throws IOException {
        return getInstance()._postAsString(url, params);
    }

    /**
     * 同步的Post请求
     *
     * @param url
     * @param params post的参数
     * @return 字符串
     */
    private String _postAsString(String url, Param... params) throws IOException {
        Response response = _post(url, params);
        return response.body().string();
    }


    //*************对外公布的方法************

    public static void postAsyn(String url, final ResultCallback callback, Param... params) {
        getInstance()._postAsyn(url, callback, params);
    }

    public static void postAsynJson(String url, String json, final ResultCallback callback) {
        getInstance()._postAsynJson(url, callback, json);
    }

    /**
     * 异步的post请求
     *
     * @param url
     * @param callback
     * @param params
     */
    private void _postAsyn(String url, final ResultCallback callback, Param... params) {
        Request request = buildPostRequest(url, params);
        deliveryResult(callback, request);
    }

    /**
     * 异步的post请求
     *
     * @param url
     * @param callback
     * @param json
     */
    private void _postAsynJson(String url, final ResultCallback callback, String json) {
        Request request = buildPostRequest_Json(url, json);
        deliveryResult(callback, request);
    }

    public static void postAsyn(String url, final ResultCallback callback, Map<String, String> params) {
        getInstance()._postAsyn(url, callback, params);
    }

    /**
     * 异步的post请求
     *
     * @param url
     * @param callback
     * @param params
     */
    private void _postAsyn(String url, final ResultCallback callback, Map<String, String> params) {
        Param[] paramsArr = map2Params(params);
        Request request = buildPostRequest(url, paramsArr);
        deliveryResult(callback, request);
    }

    private Param[] map2Params(Map<String, String> params) {
        if (params == null) return new Param[0];
        int size = params.size();
        Param[] res = new Param[size];
        Set<Map.Entry<String, String>> entries = params.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries) {
            res[i++] = new Param(entry.getKey(), entry.getValue());
        }
        return res;
    }

    public static void downloadAsyn(String url, String destDir, ResultCallback callback) {
        getInstance()._downloadAsyn(url, destDir, callback);
    }

    public static void downloadAsyn(String url, String destDir, long start, long total, ResultCallback callback) {
        getInstance()._downloadAsyn(url, destDir, start, total, callback);
    }

    /**
     * 断点续传
     *
     * @param url         地址
     * @param destFileDir 本地路径
     * @param start       开始的位置
     * @param total       {@link #getContentLength(String)}
     * @param callback    下载回调
     */
    private void _downloadAsyn(final String url, final String destFileDir, long start, long total, final ResultCallback callback) {
        Request request = new Request.Builder()
                //确定下载的范围,添加此头,则服务器就可以跳过已经下载好的部分
                .addHeader("RANGE", "bytes=" + start + "-" + total)
                .url(url)
                .build();
        download(request, url, destFileDir, callback, true);
    }

    /**
     * 异步下载文件
     *
     * @param url         地址
     * @param destFileDir 本地文件存储的文件夹
     * @param callback    测试下载地址http://yixin.dl.126.net/update/installer/yixin.apk
     */
    private void _downloadAsyn(String url, String destFileDir, ResultCallback callback) {
        Request request = new Request.Builder()
                .tag(url)
                .url(url)
                .build();
        download(request, url, destFileDir, callback, false);
    }

    private void download(Request request, final String url, final String destFileDir, final ResultCallback callback, final boolean isAdded) {
        final Call call = mOkHttpClient.newCall(request);
        final long[] totalLength = new long[1];
        call.enqueue(new Callback() {

            @Override
            public void onFailure(final Call call1, final IOException e) {
                sendFailedStringCallback(call1.request(), e, callback);
            }

            @Override
            public void onResponse(Call call, Response response) {
                totalLength[0] = Long.parseLong(response.header("Content-Length"));
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    File file = new File(destFileDir, getFileName(url));
                    fos = new FileOutputStream(file, isAdded);
                    long sum = 0;
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                        sum += len;
                        sendProgressCallback(sum, totalLength[0], callback);
                    }
                    fos.flush();
                    //如果下载文件成功，第一个参数为文件的绝对路径
                    sendSuccessResultCallback(file.getAbsolutePath(), callback);
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, callback);
                } finally {
                    try {
                        if (is != null) is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null) fos.close();
                    } catch (IOException e) {
                    }
                }

            }
        });
    }

    public static void uploadAsyn(String url, File file, ResultCallback callback) {
        getInstance().doUpload(url, file, callback);
    }

    /**
     * 模拟表单上传文件：通过MultipartBody
     * private static final MediaType MEDIA_OBJECT_STREAM = MediaType.parse("application/octet-stream");
     * //mdiatype 这个需要和服务端保持一致 你需要看下你们服务器设置的ContentType 是不是这个，他们设置的是哪个 我们要和他们保持一致
     */
    private void doUpload(String url, File file, final ResultCallback callBack) {
//        File file = new File(Environment.getExternalStorageDirectory(), "toly/ds4Android.apk");
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();
        //对请求体进行包装成CountingRequestBody
        CountingRequestBody countingRequestBody = new CountingRequestBody(requestBody, new CountingRequestBody.Listener() {
            @Override
            public void onReqProgress(long byteWritten, long contentLength) {
                sendProgressCallback(byteWritten, contentLength, callBack);
            }
        });
        Request request = new Request.Builder()
                .url(url)
                .post(countingRequestBody).build();
        //3.将Request封装为Call对象
        Call call = mOkHttpClient.newCall(request);
        //4.执行Call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "onFailure: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.e(TAG, "onResponse: " + result);
//                runOnUiThread(() -> Toast.makeText(MainActivity.this, result, Toast.LENGTH_SHORT).show());
            }
        });
    }

    /**
     * 获取下载长度
     *
     * @param downloadUrl
     * @return
     */
    public static long getContentLength(String downloadUrl) {
        Request request = new Request.Builder()
                .url(downloadUrl)
                .build();
        try {
            Response response = getInstance().mOkHttpClient.newCall(request).execute();
//            Call call = getInstance().mOkHttpClient.newCall(request);
//            Response execute = call.execute();
            if (response != null && response.isSuccessful()) {
                long contentLength = response.body().contentLength();
                //todo response.close();
                return contentLength == 0 ? DownloadInfo.TOTAL_ERROR : contentLength;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return DownloadInfo.TOTAL_ERROR;
    }

    private String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    private Param[] validateParam(Param[] params) {
        if (params == null)
            return new Param[0];
        else return params;
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    //****************************
    private void setErrorResId(final ImageView view, final int errorResId) {
        uiHandler.post(new Runnable() {
            @Override
            public void run() {
                view.setImageResource(errorResId);
            }
        });
    }

    /**
     * 根据Tag取消请求
     */
    public static void cancelTag(Object tag) {
        if (tag == null) return;
        for (Call call : getInstance().mOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : getInstance().mOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /**
     * 根据Tag取消请求
     */
    public static void cancelTag(OkHttpClient client, Object tag) {
        if (client == null || tag == null) return;
        for (Call call : client.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : client.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
    }

    /**
     * 取消所有请求请求
     */
    public void cancelAll() {
        for (Call call : getInstance().mOkHttpClient.dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : getInstance().mOkHttpClient.dispatcher().runningCalls()) {
            call.cancel();
        }
    }

    /**
     * 取消所有请求请求
     */
    public static void cancelAll(OkHttpClient client) {
        if (client == null) return;
        for (Call call : client.dispatcher().queuedCalls()) {
            call.cancel();
        }
        for (Call call : client.dispatcher().runningCalls()) {
            call.cancel();
        }
    }


}
