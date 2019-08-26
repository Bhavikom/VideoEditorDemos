package com.meishe.sdkdemo.edit.watermark;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CaoZhiChao on 2018/10/19 17:12
 */
public class WaterMarkCacheData {
    private List<WaterMarkItemData> picturePathName;

    public WaterMarkCacheData() {
    }

    public WaterMarkCacheData(List<WaterMarkItemData> picturePathName) {
        this.picturePathName = picturePathName;
    }

    public List<WaterMarkItemData> getPicturePathName() {
        if (picturePathName == null) {
            return new ArrayList<>();
        }
        return picturePathName;
    }

    public void setPicturePathName(List<WaterMarkItemData> picturePathName) {
        this.picturePathName = picturePathName;
    }
}
