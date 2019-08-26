package com.meishe.sdkdemo.edit.watermark;

/**
 * Created by CaoZhiChao on 2018/10/16 16:33
 */
public class WaterMarkItemData {
    public static final int ASSETSPICTURE = 0;
    public static final int SDCARDPICTURE = 1;
    private int itemPictureType;
    private int itemWaterMarkType;
    private String waterMarkpath;
    private String itemPicturePath;

    public WaterMarkItemData() {
    }

    public WaterMarkItemData(int itemPictureType, int itemWaterMarkType, String waterMarkpath, String itemPicturePath) {
        this.itemPictureType = itemPictureType;
        this.itemWaterMarkType = itemWaterMarkType;
        this.waterMarkpath = waterMarkpath;
        this.itemPicturePath = itemPicturePath;
    }

//    public WaterMarkItemData(int type, String waterMarkPath, String itemPicturePath) {
//        this.itemPictureType = type;
//        this.waterMarkpath = waterMarkPath;
//        this.itemPicturePath = itemPicturePath;
//    }

    public int getItemWaterMarkType() {
        return itemWaterMarkType;
    }

    public void setItemWaterMarkType(int itemWaterMarkType) {
        this.itemWaterMarkType = itemWaterMarkType;
    }

    public int getItemPictureType() {
        return itemPictureType;
    }

    public void setItemPictureType(int itemPictureType) {
        this.itemPictureType = itemPictureType;
    }

    public String getWaterMarkpath() {
        return waterMarkpath == null ? "" : waterMarkpath;
    }

    public void setWaterMarkpath(String waterMarkpath) {
        this.waterMarkpath = waterMarkpath == null ? "" : waterMarkpath;
    }

    public String getItemPicturePath() {
        return itemPicturePath == null ? "" : itemPicturePath;
    }

    public void setItemPicturePath(String itemPicturePath) {
        this.itemPicturePath = itemPicturePath == null ? "" : itemPicturePath;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof WaterMarkItemData) {
            WaterMarkItemData question = (WaterMarkItemData) o;
            return this.waterMarkpath.equals(question.waterMarkpath);
        }
        return super.equals(o);
    }
}
