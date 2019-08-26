package com.meishe.sdkdemo.douvideo.bean;

import java.util.ArrayList;

/**
 * Created by admin on 2018/11/28.
 */

public class FilterJsonFileInfo {

    public ArrayList<JsonFileInfo> getFxInfoList() {
        return fxInfoList;
    }

    private ArrayList<JsonFileInfo> fxInfoList;
    public static class JsonFileInfo {
        public String getName() {
            return name;
        }

        private String name;

        public String getFxFileName() {
            return fxFileName;
        }

        private String fxFileName;

        public String getFxLicFileName() {
            return fxLicFileName;
        }

        private String fxLicFileName;

        public String getImageName() {
            return imageName;
        }

        private String imageName;

        public String getType() {
            return type;
        }

        private String type;

        public String getColor() {
            return color;
        }

        private String color;
    }
}
