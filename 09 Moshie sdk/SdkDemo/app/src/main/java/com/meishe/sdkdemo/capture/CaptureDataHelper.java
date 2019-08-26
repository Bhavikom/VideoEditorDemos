package com.meishe.sdkdemo.capture;

import android.content.Context;

import com.meishe.sdkdemo.R;

import java.util.ArrayList;

import static com.meishe.sdkdemo.utils.Constants.HUMAN_AI_TYPE_MS;

/**
 * @Class: com.meishe.sdkdemo.capture.CaptureDataHelper.java
 * @Time: 2019/3/22 0022 14:20
 * @author: mlj
 * @Description: 获取美颜美型数据帮助类
 */
public class CaptureDataHelper {
    /**
     * @param context
     * @param beautyType
     * @return 美颜集合
     */
    public ArrayList<BeautyShapeDataItem> getBeautyDataListByType(Context context, int beautyType) {
        ArrayList<BeautyShapeDataItem> list = new ArrayList<>();
        if (beautyType == HUMAN_AI_TYPE_MS) {
            // 磨皮
            BeautyShapeDataItem beauty_strength = new BeautyShapeDataItem();
            beauty_strength.name = context.getResources().getString(R.string.strength);
            beauty_strength.resId = R.mipmap.strength;
            beauty_strength.beautyShapeId = "Beauty Strength";
            list.add(beauty_strength);
            // 美白
            BeautyShapeDataItem beauty_whitening = new BeautyShapeDataItem();
            beauty_whitening.name = context.getResources().getString(R.string.whitening);
            beauty_whitening.resId = R.mipmap.whitening;
            beauty_whitening.beautyShapeId = "Beauty Whitening";
            list.add(beauty_whitening);
            // 红润
            BeautyShapeDataItem beauty_reddening = new BeautyShapeDataItem();
            beauty_reddening.name = context.getResources().getString(R.string.ruddy);
            beauty_reddening.resId = R.mipmap.reddening;
            beauty_reddening.beautyShapeId = "Beauty Reddening";
            list.add(beauty_reddening);
            // 较色
            BeautyShapeDataItem adjustColor = new BeautyShapeDataItem();
            adjustColor.name = context.getResources().getString(R.string.correctionColor);
            adjustColor.resId = R.mipmap.beauty_adjust_color;
            adjustColor.beautyShapeId = "Default Beauty Enabled";
            list.add(adjustColor);
            // 锐度
            BeautyShapeDataItem sharpen = new BeautyShapeDataItem();
            sharpen.name = context.getResources().getString(R.string.sharpness);
            sharpen.resId = R.mipmap.beauty_sharpen;
            sharpen.beautyShapeId = "Default Sharpen Enabled";
            list.add(sharpen);
        } else {
            // 磨皮
            BeautyShapeDataItem beauty_strength = new BeautyShapeDataItem();
            beauty_strength.name = context.getResources().getString(R.string.strength);
            beauty_strength.resId = R.mipmap.strength;
            beauty_strength.beautyShapeId = "Strength";
            list.add(beauty_strength);
            // 美白
            BeautyShapeDataItem beauty_whitening = new BeautyShapeDataItem();
            beauty_whitening.name = context.getResources().getString(R.string.whitening);
            beauty_whitening.resId = R.mipmap.whitening;
            beauty_whitening.beautyShapeId = "Whitening";
            list.add(beauty_whitening);
            // 红润
            BeautyShapeDataItem beauty_reddening = new BeautyShapeDataItem();
            beauty_reddening.name = context.getResources().getString(R.string.ruddy);
            beauty_reddening.resId = R.mipmap.reddening;
            beauty_reddening.beautyShapeId = "Reddening";
            list.add(beauty_reddening);
            // 较色
            BeautyShapeDataItem adjustColor = new BeautyShapeDataItem();
            adjustColor.name = context.getResources().getString(R.string.correctionColor);
            adjustColor.resId = R.mipmap.beauty_adjust_color;
            adjustColor.beautyShapeId = "Default Beauty Enabled";
            list.add(adjustColor);
            // 锐度
            BeautyShapeDataItem sharpen = new BeautyShapeDataItem();
            sharpen.name = context.getResources().getString(R.string.sharpness);
            sharpen.resId = R.mipmap.beauty_sharpen;
            sharpen.beautyShapeId = "Default Sharpen Enabled";
            list.add(sharpen);
        }
        return list;
    }

    /**
     * @param context
     * @return 美型集合
     */
    public ArrayList<BeautyShapeDataItem> getShapeDataList(Context context) {
        ArrayList<BeautyShapeDataItem> list = new ArrayList<>();
        // 美摄美型数据
        // 瘦脸
        BeautyShapeDataItem cheek_thinning = new BeautyShapeDataItem();
        cheek_thinning.name = context.getResources().getString(R.string.cheek_thinning);
        cheek_thinning.resId = R.mipmap.cheek_thinning;
        cheek_thinning.beautyShapeId = "Face Size Warp Degree";
        list.add(cheek_thinning);
        // 大眼
        BeautyShapeDataItem eye_enlarging = new BeautyShapeDataItem();
        eye_enlarging.name = context.getResources().getString(R.string.eye_enlarging);
        eye_enlarging.resId = R.mipmap.eye_enlarging;
        eye_enlarging.type = "Default";
        eye_enlarging.beautyShapeId = "Eye Size Warp Degree";
        list.add(eye_enlarging);
        // 下巴
        BeautyShapeDataItem intensity_chin = new BeautyShapeDataItem();
        intensity_chin.name = context.getResources().getString(R.string.intensity_chin);
        intensity_chin.resId = R.mipmap.intensity_chin;
        intensity_chin.beautyShapeId = "Chin Length Warp Degree";
        intensity_chin.type = "Custom";
        list.add(intensity_chin);
        // 小脸
        BeautyShapeDataItem intensity_xiaolian = new BeautyShapeDataItem();
        intensity_xiaolian.name = context.getResources().getString(R.string.face_small);
        intensity_xiaolian.resId = R.mipmap.beauty_shape_face_little;
        intensity_xiaolian.beautyShapeId = "Face Length Warp Degree";
        intensity_xiaolian.type = "Custom";
        list.add(intensity_xiaolian);
        // 窄脸
        BeautyShapeDataItem intensity_zhailian = new BeautyShapeDataItem();
        intensity_zhailian.name = context.getResources().getString(R.string.face_thin);
        intensity_zhailian.resId = R.mipmap.beauty_shape_face_thin;
        intensity_zhailian.beautyShapeId = "Face Width Warp Degree";
        intensity_zhailian.type = "Custom";
        list.add(intensity_zhailian);
        // 额头
        BeautyShapeDataItem intensity_forehead = new BeautyShapeDataItem();
        intensity_forehead.name = context.getResources().getString(R.string.intensity_forehead);
        intensity_forehead.resId = R.mipmap.intensity_forehead;
        intensity_forehead.type = "Custom";
        intensity_forehead.beautyShapeId = "Forehead Height Warp Degree";
        list.add(intensity_forehead);
        // 瘦鼻
        BeautyShapeDataItem intensity_nose = new BeautyShapeDataItem();
        intensity_nose.name = context.getResources().getString(R.string.intensity_nose);
        intensity_nose.resId = R.mipmap.intensity_nose;
        intensity_nose.type = "Custom";
        intensity_nose.beautyShapeId = "Nose Width Warp Degree";
        list.add(intensity_nose);
        //长鼻
        BeautyShapeDataItem changbi_nose = new BeautyShapeDataItem();
        changbi_nose.name = context.getResources().getString(R.string.nose_long);
        changbi_nose.resId = R.mipmap.beauty_shape_nose_long;
        changbi_nose.type = "Custom";
        changbi_nose.beautyShapeId = "Nose Length Warp Degree";
        list.add(changbi_nose);
        // 眼角
        BeautyShapeDataItem yanjiao = new BeautyShapeDataItem();
        yanjiao.name = context.getResources().getString(R.string.eye_corner);
        yanjiao.resId = R.mipmap.beauty_shape_eye_corner;
        yanjiao.type = "Custom";
        yanjiao.beautyShapeId = "Eye Corner Stretch Degree";
        list.add(yanjiao);
        // 嘴形
        BeautyShapeDataItem intensity_mouth = new BeautyShapeDataItem();
        intensity_mouth.name = context.getResources().getString(R.string.intensity_mouth);
        intensity_mouth.resId = R.mipmap.intensity_mouth;
        intensity_mouth.type = "Custom";
        intensity_mouth.beautyShapeId = "Mouth Size Warp Degree";
        list.add(intensity_mouth);
        // 嘴角
        BeautyShapeDataItem intensity_zuijiao = new BeautyShapeDataItem();
        intensity_zuijiao.name = context.getResources().getString(R.string.mouse_corner);
        intensity_zuijiao.resId = R.mipmap.beauty_shape_mouse_corner;
        intensity_zuijiao.type = "Custom";
        intensity_zuijiao.beautyShapeId = "Mouth Corner Lift Degree";
        list.add(intensity_zuijiao);
        return list;
    }
}
