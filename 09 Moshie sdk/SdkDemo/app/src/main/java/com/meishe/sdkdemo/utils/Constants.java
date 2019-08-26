package com.meishe.sdkdemo.utils;

import com.meishe.sdkdemo.utils.asset.NvAsset;

/**
 * Created by admin on 2018-5-29.
 */

public class Constants {
    /**
     * sp中用到的key
     */
    public static final String KEY_PARAMTER = "paramter";

    public static final int EDIT_MODE_CAPTION = 0;
    public static final int EDIT_MODE_STICKER = 1;
    public static final int EDIT_MODE_WATERMARK = 2;
    public static final int EDIT_MODE_THEMECAPTION = 3;
    public static final int EDIT_MODE_COMPOUND_CAPTION = 4;

    public static final long NS_TIME_BASE = 1000000;

    public static final int MEDIA_TYPE_AUDIO = 1;

    public static final int ACTIVITY_START_CODE_MUSIC_SINGLE = 100;
    public static final int ACTIVITY_START_CODE_MUSIC_MULTI = 101;

    public static final String START_ACTIVITY_FROM_CAPTURE = "start_activity_from_capture";
    public static final String CAN_USE_ARFACE_FROM_MAIN = "can_use_arface_from_main";

    public static final int FROMMAINACTIVITYTOVISIT = 1001;//从主页面进入视频选择页面
    public static final int FROMCLIPEDITACTIVITYTOVISIT = 1002;//从片段编辑页面进入视频选择页面
    public static final int FROMPICINPICACTIVITYTOVISIT = 1003;//从画中画面进入视频选择页面

    /**
     * 图片运动
     */
    public static final int EDIT_MODE_PHOTO_AREA_DISPLAY = 2001;//图片运动-区域显示
    public static final int EDIT_MODE_PHOTO_TOTAL_DISPLAY = 2002;//图片运动-全图显示

    /**
     * 自定义贴纸
     */
    public static final int CUSTOMSTICKER_EDIT_FREE_MODE = 2003;//自由
    public static final int CUSTOMSTICKER_EDIT_CIRCLE_MODE = 2004;//圆形
    public static final int CUSTOMSTICKER_EDIT_SQUARE_MODE = 2005;//正方

    /**
     * 无特效的ID
     */
    public static final String NO_FX = "None";

    /**
     * music
     */
    public static final String MUSIC_EXTRA_AUDIOCLIP = "extra";
    public static final String MUSIC_EXTRA_LAST_AUDIOCLIP = "extra_last";
    public static final long MUSIC_MIN_DURATION = 1000000;

    public static final String SELECT_MUSIC_FROM = "select_music_from";
    public static final int SELECT_MUSIC_FROM_DOUYIN = 5001;
    public static final int SELECT_MUSIC_FROM_EDIT = 5002;
    public static final int SELECT_MUSIC_FROM_MUSICLYRICS = 5003;

    /**
     * 视音频音量值
     */
    public static final float VIDEOVOLUME_DEFAULTVALUE = 1.0f;
    public static final float VIDEOVOLUME_MAXVOLUMEVALUE = 2.0f;
    public static final int VIDEOVOLUME_MAXSEEKBAR_VALUE = 100;

    /**
     * 屏幕点击常量定义
     */
    //点击时长，单位微秒
    public final static int HANDCLICK_DURATION = 200;
    //touch移动距离，单位像素值
    public final static double HANDMOVE_DISTANCE = 10.0;

    public final static String FX_TRANSFORM_2D = "Transform 2D";
    public final static String FX_TRANSFORM_2D_SCALE_X = "Scale X";
    public final static String FX_TRANSFORM_2D_SCALE_Y = "Scale Y";
    /**
     * Color Property 颜色属性
     */
    public final static String FX_COLOR_PROPERTY = "Color Property";
    public final static String FX_COLOR_PROPERTY_BRIGHTNESS = "Brightness";
    public final static String FX_COLOR_PROPERTY_CONTRAST = "Contrast";
    public final static String FX_COLOR_PROPERTY_SATURATION = "Saturation";
    /**
     * Vignette 暗角
     */
    public final static String FX_VIGNETTE = "Vignette";
    public final static String FX_VIGNETTE_DEGREE = "Degree";
    /**
     * Sharpen 锐度
     */
    public final static String FX_SHARPEN = "Sharpen";
    public final static String FX_SHARPEN_AMOUNT = "Amount";

    public final static String[] CaptionColors = {
            "#ffffffff", "#ff000000", "#ffd0021b",
            "#ff4169e1", "#ff05d109", "#ff02c9ff",
            "#ff9013fe", "#ff8b6508", "#ffff0080",
            "#ff02F78E", "#ff00FFFF", "#ffFFD709",
            "#ff4876FF", "#ff19FF2F", "#ffDA70D6",
            "#ffFF6347", "#ff5B45AE", "#ff8B1C62",
            "#ff8B7500", "#ff228B22", "#ffC0FF3E",
            "#ff00BFFF", "#ffABABAB", "#ff6495ED",
            "#ff0000E3", "#ffE066FF", "#ffF08080"
    };

    public final static String[] FilterColors = {
            "#80d0021b", "#804169e1", "#8005d109",
            "#8002c9ff", "#809013fe", "#808b6508",
            "#80ff0080", "#8002F78E", "#8000FFFF",
            "#80FFD709", "#804876FF", "#8019FF2F",
            "#80DA70D6", "#80FF6347", "#805B45AE",
            "#808B1C62", "#808B7500", "#80228B22",
            "#80C0FF3E", "#8000BFFF", "#80ABABAB",
            "#806495ED", "#800000E3", "#80E066FF",
            "#80F08080"
    };

    /**
     * 素材下载状态值
     */
    public static final int ASSET_LIST_REQUEST_SUCCESS = 106;
    public static final int ASSET_LIST_REQUEST_FAILED = 107;
    public static final int ASSET_DOWNLOAD_SUCCESS = 108;
    public static final int ASSET_DOWNLOAD_FAILED = 109;
    public static final int ASSET_DOWNLOAD_INPROGRESS = 110;
    public static final int ASSET_DOWNLOAD_START_TIMER = 111;//素材下载启动定时器更新进度标识

    /**
     * 拍摄
     */
    public static final int RECORD_TYPE_NULL = 3000;
    public static final int RECORD_TYPE_PICTURE = 3001;
    public static final int RECORD_TYPE_VIDEO = 3002;

    public static final String SELECT_MEDIA_FROM = "select_media_from"; // key
    public static final int SELECT_IMAGE_FROM_WATER_MARK = 4001;        // 从水印入口进入单个图片选择页面
    public static final int SELECT_IMAGE_FROM_MAKE_COVER = 4002;        // 从制作封面入口进入单个图片选择页面
    public static final int SELECT_IMAGE_FROM_CUSTOM_STICKER = 4003;    // 从自定义贴纸入口进入单个图片选择页面
    public static final int SELECT_VIDEO_FROM_DOUYINCAPTURE = 4004;     // 从视频拍摄入口进入单个视频选择页面

    public static final int SELECT_VIDEO_FROM_FLIP_CAPTION = 4005;        // 从翻转字幕页面入口进入视频选择选择视频
    public static final int SELECT_VIDEO_FROM_MUSIC_LYRICS = 4006;        // 从音乐歌词入口进入视频选择选择视频

    public static final int SELECT_VIDEO_FROM_PARTICLE = 4010;          // 从粒子入口进入视频选择页面

    public static final int POINT16V9 = NvAsset.AspectRatio_16v9;
    public static final int POINT1V1 = NvAsset.AspectRatio_1v1;
    public static final int POINT9V16 = NvAsset.AspectRatio_9v16;
    public static final int POINT3V4 = NvAsset.AspectRatio_3v4;
    public static final int POINT4V3 = NvAsset.AspectRatio_4v3;

    /**
     * 拍摄-美型
     */
    public static final double NORMAL_VELUE_INTENSITY_FORHEAD = 0.5;
    public static final double NORMAL_VELUE_INTENSITY_CHIN = 0.5;
    public static final double NORMAL_VELUE_INTENSITY_MOUTH = 0.5;
    /**
     * 拍摄-变焦、曝光
     */
    public static final int CAPTURE_TYPE_ZOOM = 2;
    public static final int CAPTURE_TYPE_EXPOSE = 3;
    /**
     * 拍摄-最小录制时长
     */
    public static final int MIN_RECORD_DURATION = 1000000;

    /**
     * 人脸类型
     */
    public static final int HUMAN_AI_TYPE_NONE = 0;//SDK普通版
    public static final int HUMAN_AI_TYPE_MS = 1;//SDK meishe人脸模块
    public static final int HUMAN_AI_TYPE_FU = 2;//FU
}
