package com.meishe.sdkdemo.edit.record;

import android.view.View;

/**
 * Created by ms on 2018/8/9 0009.
 */

public class SqAreaInfo {
    private long inPoint;
    private long outPoint;
    private int inPosition;
    private View areaView;
    private View leftHandle;
    private View rightHandle;

    public SqAreaInfo clone() {
        SqAreaInfo cloneInfo = new SqAreaInfo();
        cloneInfo.setInPoint(this.getInPoint());
        cloneInfo.setOutPoint(this.getOutPoint());
        cloneInfo.setInPosition(this.getInPosition());
        cloneInfo.setAreaView(this.getAreaView());
        cloneInfo.setLeftHandle(this.getLeftHandle());
        cloneInfo.setRightHandle(this.getRightHandle());
        return cloneInfo;
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

    public int getInPosition() {
        return inPosition;
    }
    public void setInPosition(int inPosition) {
        this.inPosition = inPosition;
    }

    public View getAreaView() {
        return areaView;
    }
    public void setAreaView(View areaView) {
        this.areaView = areaView;
    }

    public View getLeftHandle() {
        return leftHandle;
    }
    public void setLeftHandle(View leftHandle) {
        this.leftHandle = leftHandle;
    }

    public View getRightHandle() {
        return rightHandle;
    }
    public void setRightHandle(View rightHandle) {
        this.rightHandle = rightHandle;
    }
}
