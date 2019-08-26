package com.meishe.sdkdemo.boomrang.fragment;

/**
 * Created by CaoZhiChao on 2018/12/17 13:51
 */
public interface BaseRecordInterface {
    void onCaptureRecordingStarted(int i);

    void onCaptureRecordingDuration(int i, long l);

    void onCaptureRecordingFinished(int i);

    void onCaptureRecordingError(int i);

    void onCaptureDevicePreviewStarted(int i);
}
