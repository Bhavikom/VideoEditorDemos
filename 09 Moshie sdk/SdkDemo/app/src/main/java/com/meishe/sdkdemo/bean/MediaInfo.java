package com.meishe.sdkdemo.bean;

import android.text.TextUtils;

import com.meicam.sdk.NvsRational;

import java.io.Serializable;

public class MediaInfo implements Serializable {
    private String path;
    private String compressPath;
    private String cutPath;
    private long duration;
    private boolean isChecked = false;
    private boolean isCut;
    public int position;
    private int num;
    private int mimeType;
    private String pictureType;
    private boolean compressed;
    private int width;
    private int height;
    private int size;
    private String datetime;
    private boolean isPano = false;
    private String title;
    private NvsRational videoFps;
    private float fps;

    public MediaInfo() {

    }

    public MediaInfo(String path, long duration, int mimeType, String pictureType, int width, int height, int size, String datetime) {
        this.path = path;
        this.duration = duration;
        this.mimeType = mimeType;
        this.pictureType = pictureType;
        this.width = width;
        this.height = height;
        this.size = size;
        this.datetime = datetime;
    }


    public NvsRational getVideoFps() {
        return videoFps;
    }


    public float getFps() {
        return fps;
    }

    public void setFps(float fps) {
        this.fps = fps;
    }

    public void setVideoFps(NvsRational videoFps) {
        this.videoFps = videoFps;
        if(videoFps.den == 0){
            setFps(0);
        }else{
            setFps((float) videoFps.num/videoFps.den);
        }
    }

    public String getPictureType() {
        if (TextUtils.isEmpty(pictureType)) {
            pictureType = "image/jpeg";
        }
        return pictureType;
    }

    public void setPictureType(String pictureType) {
        this.pictureType = pictureType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCompressPath() {
        return compressPath;
    }

    public void setCompressPath(String compressPath) {
        this.compressPath = compressPath;
    }

    public String getCutPath() {
        return cutPath;
    }

    public void setCutPath(String cutPath) {
        this.cutPath = cutPath;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isCut() {
        return isCut;
    }

    public void setCut(boolean cut) {
        isCut = cut;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getMimeType() {
        return mimeType;
    }

    public void setMimeType(int mimeType) {
        this.mimeType = mimeType;
    }

    public boolean isCompressed() {
        return compressed;
    }

    public void setCompressed(boolean compressed) {
        this.compressed = compressed;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public boolean isPano() {
        return isPano;
    }

    public void setPano(boolean pano) {
        isPano = pano;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
