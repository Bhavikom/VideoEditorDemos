package com.meishe.sdkdemo.edit.watermark;

import android.graphics.Point;
import android.graphics.PointF;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CaoZhiChao on 2018/9/21 11:03
 */
public class WaterMarkData {
    private List<PointF> pointFInLiveWindow;
    private int excursionX ;
    private int excursionY ;
    private int picWidth ;
    private int picHeight ;
    private String picPath ;
    //动态水印才有
    private float transX;
    private float transY;
    private float scale;

    private Point pointOfLiveWindow ;
    public WaterMarkItemData getWaterMarkItemData() {
        return waterMarkItemData;
    }

    public void setWaterMarkItemData(WaterMarkItemData waterMarkItemData) {
        this.waterMarkItemData = waterMarkItemData;
    }

    private WaterMarkItemData waterMarkItemData;

    public WaterMarkData(List<PointF> pointFInLiveWindow, int excursionX, int excursionY, int picWidth, int picHeight, String picPath, Point pointOfLiveWindow, WaterMarkItemData waterMarkItemData) {
        this.pointFInLiveWindow = pointFInLiveWindow;
        this.excursionX = excursionX;
        this.excursionY = excursionY;
        this.picWidth = picWidth;
        this.picHeight = picHeight;
        this.picPath = picPath;
        this.waterMarkItemData = waterMarkItemData;
        this.pointOfLiveWindow = pointOfLiveWindow;
    }

    public WaterMarkData(List<PointF> pointFInLiveWindow, int excursionX, int excursionY, int picWidth, int picHeight, String picPath, Point pointOfLiveWindow) {
        this.pointFInLiveWindow = pointFInLiveWindow;
        this.excursionX = excursionX;
        this.excursionY = excursionY;
        this.picWidth = picWidth;
        this.picHeight = picHeight;
        this.picPath = picPath;
        this.pointOfLiveWindow = pointOfLiveWindow;
    }

    public Point getPointOfLiveWindow() {
        return pointOfLiveWindow;
    }

    public void setPointOfLiveWindow(Point pointOfLiveWindow) {
        this.pointOfLiveWindow = pointOfLiveWindow;
    }



    public List<PointF> getPointFInLiveWindow() {
        if (pointFInLiveWindow == null) {
            return new ArrayList<>();
        }
        return pointFInLiveWindow;
    }

    public void setPointFInLiveWindow(List<PointF> pointFInLiveWindow) {
        this.pointFInLiveWindow = pointFInLiveWindow;
    }

    public int getExcursionX() {
        return excursionX;
    }

    public void setExcursionX(int excursionX) {
        this.excursionX = excursionX;
    }

    public int getExcursionY() {
        return excursionY;
    }

    public void setExcursionY(int excursionY) {
        this.excursionY = excursionY;
    }

    public int getPicWidth() {
        return picWidth;
    }

    public void setPicWidth(int picWidth) {
        this.picWidth = picWidth;
    }

    public int getPicHeight() {
        return picHeight;
    }

    public void setPicHeight(int picHeight) {
        this.picHeight = picHeight;
    }

    public String getPicPath() {
        return picPath == null ? "" : picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath == null ? "" : picPath;
    }

    public float getTransX() {
        return transX;
    }

    public void setTransX(float transX) {
        this.transX = transX;
    }

    public float getTransY() {
        return transY;
    }

    public void setTransY(float transY) {
        this.transY = transY;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }
}
