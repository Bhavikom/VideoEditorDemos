package com.meishe.sdkdemo.capturescene.data;

/**
 * Created by CaoZhiChao on 2019/1/3 16:03
 */
public class Constants {
    private static final String BASEPATH = "https://vsapi.meishesdk.com/materialinfo/index.php?command=listMaterial&acceptAspectRatio=0&category=0&page=0&pageSize=10&lang=zh_CN&type=";
    private static final int TYPE_CAPTURE_SCENE = 8;
    public static final String CAPTURE_SCENE_PATH = BASEPATH + TYPE_CAPTURE_SCENE;
    public static final String RESOURCE_NEW_PATH = "http://omxuaeaki.bkt.clouddn.com";
    public static final String RESOURCE_OLD_PATH = "https://meishesdk.meishe-app.com";

    //CaptureScene类型标识
    public static final int CAPTURE_SCENE_LOCAL = 801;
    public static final int CAPTURE_SCENE_ONLINE = 802;


}
