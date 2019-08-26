package com.meishe.sdkdemo.picinpic.data;

import java.util.ArrayList;

/**
 * Created by admin on 2018/10/12.
 */

public class PicInPicTemplateAsset {
    private String mTemplateName;
    private String mTemplateCover;
    private ArrayList<String> mTempatePackageID;
    private boolean isBundle = false;
    public boolean isBundle() {
        return isBundle;
    }

    public void setBundle(boolean bundle) {
        isBundle = bundle;
    }

    public String getTemplateName() {
        return mTemplateName;
    }

    public void setTemplateName(String templateName) {
        this.mTemplateName = templateName;
    }

    public ArrayList<String> getTempatePackageID() {
        return mTempatePackageID;
    }

    public void setTempatePackageID(ArrayList<String> tempatePackageID) {
        mTempatePackageID = tempatePackageID;
    }

    public String getTemplateCover() {
        return mTemplateCover;
    }

    public void setTemplateCover(String templateCover) {
        mTemplateCover = templateCover;
    }



    public PicInPicTemplateAsset() {}

    public static class PicInPicJsonFileInfo{
        public ArrayList<PicInPicJsonFileInfoList> getPipInfoList() {
            return pipInfoList;
        }
        private ArrayList<PicInPicJsonFileInfoList> pipInfoList;
    }
    public static class PicInPicJsonFileInfoList {
        public String getName() {
            return name;
        }

        private String name;

        public String getFileDirName() {
            return fileDirName;
        }

        private String fileDirName;

        public String getPipPackageName1() {
            return pipPackageName1;
        }

        private String pipPackageName1;

        public String getPipPackageName2() {
            return pipPackageName2;
        }

        private String pipPackageName2;

        public String getCoverImageName() {
            return coverImageName;
        }

        private String coverImageName;

        public boolean isBundle() {
            return isBundle;
        }

        private boolean isBundle;
    }
}
