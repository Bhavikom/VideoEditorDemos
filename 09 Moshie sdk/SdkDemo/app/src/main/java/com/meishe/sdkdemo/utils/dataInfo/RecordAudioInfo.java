package com.meishe.sdkdemo.utils.dataInfo;

import com.meishe.sdkdemo.utils.Constants;

/**
 * Created by ms on 2018/8/10 0010.
 */

public class RecordAudioInfo {
    private long id;
    private String path;
    private long inPoint;
    private long outPoint;
    private long trimIn;
    private String fxID;
    private float volume;

    public void clear() {
        id = -1;
        path = "";
        inPoint = -1;
        outPoint = -1;
        trimIn = 0;
        fxID = Constants.NO_FX;
        volume = 1.0f;
    }

    public RecordAudioInfo clone() {
        RecordAudioInfo cloneInfo = new RecordAudioInfo();
        cloneInfo.setId(this.getId());
        cloneInfo.setPath(this.getPath());
        cloneInfo.setInPoint(this.getInPoint());
        cloneInfo.setOutPoint(this.getOutPoint());
        cloneInfo.setTrimIn(this.getTrimIn());
        cloneInfo.setFxID(this.getFxID());
        cloneInfo.setVolume(this.getVolume());
        return cloneInfo;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }

    public long getInPoint() {
        return inPoint;
    }
    public void setInPoint(long inPoint) {
        this.inPoint = inPoint;
    }

    public long getOutPoint() {
        return outPoint;
    }
    public void setOutPoint(long outPoint) {
        this.outPoint = outPoint;
    }

    public long getTrimIn() {
        return trimIn;
    }
    public void setTrimIn(long trimIn) {
        this.trimIn = trimIn;
    }

    public String getFxID() {
        return fxID;
    }
    public void setFxID(String fxID) {
        this.fxID = fxID;
    }

    public float getVolume() {
        return volume;
    }
    public void setVolume(float volume) {
        this.volume = volume;
    }
}
