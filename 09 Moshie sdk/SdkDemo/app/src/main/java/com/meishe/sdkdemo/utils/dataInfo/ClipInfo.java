package com.meishe.sdkdemo.utils.dataInfo;

import android.graphics.RectF;

import com.meicam.sdk.NvsVideoStreamInfo;
import com.meishe.sdkdemo.utils.Constants;

import static com.meishe.sdkdemo.utils.Constants.VIDEOVOLUME_DEFAULTVALUE;

public class ClipInfo {
    public boolean isRecFile = false;
    public int rotation = NvsVideoStreamInfo.VIDEO_ROTATION_0;
    private String m_filePath;
    private float m_speed;
    private boolean m_mute;
    private long m_trimIn;
    private long m_trimOut;

    //校色数据
    private float m_brightnessVal;
    private float m_contrastVal;
    private float m_saturationVal;
    private float m_vignetteVal; // 暗角
    private float m_sharpenVal;  // 锐度

    //
    //音量
    private float m_volume;

    //调整
    private int m_rotateAngle;//旋转角度
    private int m_scaleX;//
    private int m_scaleY;



    //图片展示模式
    private int m_imgDispalyMode = Constants.EDIT_MODE_PHOTO_AREA_DISPLAY;
    //是否开启图片运动
    private boolean isOpenPhotoMove = true;
    //图片起始ROI
    private RectF m_normalStartROI;
    //图片终止ROI
    private RectF m_normalEndROI;

    //视频横向裁剪，纵向平移
    private float m_pan;
    private float m_scan;

    public float getPan() {
        return m_pan;
    }

    public void setPan(float pan) {
        this.m_pan = pan;
    }

    public float getScan() {
        return m_scan;
    }

    public void setScan(float scan) {
        this.m_scan = scan;
    }

    public RectF getNormalStartROI() {
        return m_normalStartROI;
    }

    public void setNormalStartROI(RectF normalStartROI) {
        this.m_normalStartROI = normalStartROI;
    }
    public RectF getNormalEndROI() {
        return m_normalEndROI;
    }

    public void setNormalEndROI(RectF normalEndROI) {
        this.m_normalEndROI = normalEndROI;
    }

    public boolean isOpenPhotoMove() {
        return isOpenPhotoMove;
    }

    public void setOpenPhotoMove(boolean openPhotoMove) {
        isOpenPhotoMove = openPhotoMove;
    }
    public int getImgDispalyMode() {
        return m_imgDispalyMode;
    }

    public void setImgDispalyMode(int imgDispalyMode) {
        m_imgDispalyMode = imgDispalyMode;
    }
    public int getScaleX() {
        return m_scaleX;
    }

    public void setScaleX(int scaleX) {
        this.m_scaleX = scaleX;
    }

    public int getScaleY() {
        return m_scaleY;
    }

    public void setScaleY(int scaleY) {
        this.m_scaleY = scaleY;
    }

    public int getRotateAngle() {
        return m_rotateAngle;
    }

    public void setRotateAngle(int rotateAngle) {
        this.m_rotateAngle = rotateAngle;
    }
    public float getVolume() {
        return m_volume;
    }

    public void setVolume(float volume) {
        this.m_volume = volume;
    }
    public float getBrightnessVal() {
        return m_brightnessVal;
    }

    public void setBrightnessVal(float brightnessVal) {
        this.m_brightnessVal = brightnessVal;
    }

    public float getContrastVal() {
        return m_contrastVal;
    }

    public void setContrastVal(float contrastVal) {
        this.m_contrastVal = contrastVal;
    }

    public float getSaturationVal() {
        return m_saturationVal;
    }

    public void setSaturationVal(float saturationVal) {
        this.m_saturationVal = saturationVal;
    }

    public float getVignetteVal() {
        return m_vignetteVal;
    }

    public void setVignetteVal(float vignetteVal) {
        this.m_vignetteVal = vignetteVal;
    }

    public float getSharpenVal() {
        return m_sharpenVal;
    }

    public void setSharpenVal(float sharpenVal) {
        this.m_sharpenVal = sharpenVal;
    }

    public ClipInfo() {
        m_filePath = null;
        m_speed = -1.0f;
        m_mute = false;
        m_trimIn = -1;
        m_trimOut = -1;
        m_brightnessVal = -1.0f;
        m_contrastVal = -1.0f;
        m_saturationVal = -1.0f;
        m_sharpenVal = 0;
        m_vignetteVal = 0;
        m_volume = VIDEOVOLUME_DEFAULTVALUE;
        m_rotateAngle = 0;
        m_scaleX = -2;//
        m_scaleY = -2;
        m_pan = 0.0f;
        m_scan = 0.0f;
    }


    public void setFilePath(String filePath) {
        m_filePath = filePath;
    }

    public String getFilePath() {
        return m_filePath;
    }

    public void setSpeed(float speed) {
        m_speed = speed;
    }

    public float getSpeed() {
        return m_speed;
    }

    public void setMute(boolean flag) {
        m_mute = flag;
    }

    public boolean getMute() {
        return m_mute;
    }

    public void changeTrimIn(long data) {
        m_trimIn = data;
    }

    public long getTrimIn() {
        return m_trimIn;
    }

    public void changeTrimOut(long data) {
        m_trimOut = data;
    }

    public long getTrimOut() {
        return m_trimOut;
    }

    public ClipInfo clone(){
        ClipInfo newClipInfo = new ClipInfo();
        newClipInfo.isRecFile = this.isRecFile;
        newClipInfo.rotation = this.rotation;
        newClipInfo.setFilePath(this.getFilePath());
        newClipInfo.setMute(this.getMute());
        newClipInfo.setSpeed(this.getSpeed());
        newClipInfo.changeTrimIn(this.getTrimIn());
        newClipInfo.changeTrimOut(this.getTrimOut());

        //copy data
        newClipInfo.setBrightnessVal(this.getBrightnessVal());
        newClipInfo.setSaturationVal(this.getSaturationVal());
        newClipInfo.setContrastVal(this.getContrastVal());
        newClipInfo.setVignetteVal(this.getVignetteVal());
        newClipInfo.setSharpenVal(this.getSharpenVal());
        newClipInfo.setVolume(this.getVolume());
        newClipInfo.setRotateAngle(this.getRotateAngle());
        newClipInfo.setScaleX(this.getScaleX());
        newClipInfo.setScaleY(this.getScaleY());

        //图片数据
        newClipInfo.setImgDispalyMode(this.getImgDispalyMode());
        newClipInfo.setOpenPhotoMove(this.isOpenPhotoMove());
        newClipInfo.setNormalStartROI(this.getNormalStartROI());
        newClipInfo.setNormalEndROI(this.getNormalEndROI());

        //视频横向裁剪，纵向平移
        newClipInfo.setPan(this.getPan());
        newClipInfo.setScan(this.getScan());

        return newClipInfo;
    }
}
