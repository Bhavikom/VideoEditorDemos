package com.meishe.sdkdemo.douvideo.bean;

import com.meicam.sdk.NvsVideoClip;

import java.io.Serializable;

public class RecordClip implements Serializable{

    private String mFilePath;
    private long mDuration;
    private long mDurationBySpeed;

    private long mTrimIn;
    private long mTrimOut;
    private float mSpeed;
    private long mMusicPos;
    private boolean isCaptureVideo = true;//是否是拍摄视频
    private boolean mIsConvertSuccess = false;//视频默认未转码成功

    private int mRotateAngle = NvsVideoClip.ClIP_EXTRAVIDEOROTATION_0;
    public RecordClip(){

    }
    public RecordClip(String path,
                      long trimIn,
                      long trimOut,
                      float speed,
                      long musicPos){
        mFilePath = path;
        mTrimIn = trimIn;
        mTrimOut = trimOut;
        long duration = trimOut - trimIn;
        mDurationBySpeed = (long)(duration * 1f/speed);
        mDuration = duration;
        mSpeed = speed;
        mMusicPos = musicPos;
    }

    public int getRotateAngle() {
        return mRotateAngle;
    }

    public void setRotateAngle(int rotateAngle) {
        mRotateAngle = rotateAngle;
    }
    public boolean isCaptureVideo() {
        return isCaptureVideo;
    }

    public void setCaptureVideo(boolean captureVideo) {
        isCaptureVideo = captureVideo;
    }

    public long getTrimIn() {
        return mTrimIn;
    }

    public void setTrimIn(long trimIn) {
        this.mTrimIn = trimIn;
    }

    public long getTrimOut() {
        return mTrimOut;
    }

    public void setTrimOut(long trimOut) {
        this.mTrimOut = trimOut;
    }

    public long getMusicStartPos(){
        return mMusicPos;
    }

    public String getFilePath() {
        return mFilePath;
    }

    public void setFilePath(String filePath) {
        this.mFilePath = filePath;
    }

    public long getDuration() {
        return mDuration;
    }

    public long getDurationBySpeed(){
        return mDurationBySpeed;
    }

    public void setDurationBySpeed(long durationBySpeed){
        mDurationBySpeed = durationBySpeed;
    }

    public void setDuration(long duration) {
        this.mDuration = duration;
    }

    public float getSpeed() {
        return mSpeed;
    }

    public void setSpeed(float speed) {
        this.mSpeed = speed;
    }

    public void setIsConvertSuccess(boolean isSuccess){
        mIsConvertSuccess = isSuccess;
    }

    public boolean getIsConvertSuccess(){
        return mIsConvertSuccess;
    }
}
