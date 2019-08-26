package com.meishe.sdkdemo.bean;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ${gexinyu} on 2018/5/28.
 */

public class MediaBean implements Serializable{

    private int type;
    private String path;
    private String thumbPath;
    private int duration;
    private long size;
    private String displayName;
    public boolean isSelected;
    public int selectedNum;

    public MediaBean() {

    }

    //照片
    public MediaBean(int type, String path, long size, String displayName) {
        this.type = type;
        this.path = path;
        this.size = size;
        this.displayName = displayName;
    }


    public String getDate() {
        return new SimpleDateFormat("yyyy年MM月dd日")
                .format(new Date(size * 1000L));
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getThumbPath() {
        return thumbPath;
    }

    public void setThumbPath(String thumbPath) {
        this.thumbPath = thumbPath;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }


    public class Type {
        public static final int Image = 1;

        public static final int Video = 2;
    }
}
