package com.meishe.sdkdemo.capturescene.httputils.download;


import com.meishe.sdkdemo.capturescene.httputils.ResultCallback;

import okhttp3.Request;

/**
 * Created by CaoZhiChao on 2018/12/1 15:42
 */
public abstract class DownLoadResultCallBack<T> extends ResultCallback<T> {

    @Override
    public void onError(Request request, Exception e) {

    }

    @Override
    public void onResponse(T response) {

    }

    public abstract void onProgress(long now, long total, int progress);

}
