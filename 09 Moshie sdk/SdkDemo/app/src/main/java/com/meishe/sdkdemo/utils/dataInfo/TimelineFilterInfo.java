package com.meishe.sdkdemo.utils.dataInfo;

/**
 * Created by zd on 2017/6/29.
 */

public class TimelineFilterInfo {

    private String name;
    private long fxInPoint;
    private long fxOutPoint;
    private boolean addInReverseMode;

    public TimelineFilterInfo(String name,long inPoint,long outPoint, boolean reverseMode) {
        this.name = name;
        this.fxInPoint = inPoint;
        this.fxOutPoint = outPoint;
        this.addInReverseMode = reverseMode;
    }
    public String getName() {
        return name;
    }

    public long getInPoint() {
        return fxInPoint;
    }

    public void setInPoint(long inPoint) {
        this.fxInPoint = inPoint;
    }

    public long getOutPoint() {
        return fxOutPoint;
    }

    public void setOutPoint(long outPoint) {
        this.fxOutPoint = outPoint;
    }

    public boolean addInReverseMode() { return addInReverseMode; }
}
