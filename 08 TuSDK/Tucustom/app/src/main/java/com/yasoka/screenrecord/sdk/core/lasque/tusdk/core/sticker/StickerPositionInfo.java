// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.sticker;

//import org.lasque.tusdk.core.utils.json.JsonHelper;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.DataBase;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.JsonBaseBean;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.JsonHelper;

import org.json.JSONObject;
import java.util.ArrayList;
//import org.lasque.tusdk.core.utils.json.DataBase;
import java.io.Serializable;
//import org.lasque.tusdk.core.utils.json.JsonBaseBean;

public class StickerPositionInfo extends JsonBaseBean implements Serializable
{
    @DataBase("model_width")
    public int modelWidth;
    @DataBase("model_height")
    public int modelHeight;
    @DataBase("screen_width")
    public int screenWidth;
    @DataBase("screem_height")
    public int screenHeight;
    @DataBase("model_type")
    public int categoryId;
    @DataBase("pos_type")
    public int posType;
    @DataBase("render_type")
    public int renderType;
    @DataBase("ratio")
    public float ratio;
    @DataBase("scale")
    public float scale;
    @DataBase("offset_x")
    public float offsetX;
    @DataBase("offset_y")
    public float offsetY;
    @DataBase("rotation")
    public float rotation;
    @DataBase("animation_interval")
    public int frameInterval;
    @DataBase("animation_files")
    public ArrayList<String> resourceList;
    @DataBase("animation_loop")
    public int loopMode;
    @DataBase("animation_loop_start")
    public int loopStartIndex;
    
    public StickerPositionInfo() {
    }
    
    public StickerPositionInfo(final JSONObject jsonObject) {
        this.deserialize(jsonObject);
    }
    
    public int getFrameInterval() {
        if (this.frameInterval <= 0) {
            this.frameInterval = 100;
        }
        return this.frameInterval;
    }
    
    public StickerPositionType getPosType() {
        switch (this.posType) {
            case 1: {
                return StickerPositionType.StickerPosEyeBrow;
            }
            case 2: {
                return StickerPositionType.StickerPosEye;
            }
            case 3: {
                return StickerPositionType.StickerPosNose;
            }
            case 4: {
                return StickerPositionType.StickerPosMouth;
            }
            case 5: {
                return StickerPositionType.StickerPosCheek;
            }
            case 6: {
                return StickerPositionType.StickerPosHead;
            }
            case 7: {
                return StickerPositionType.StickerPosJaw;
            }
            case 8: {
                return StickerPositionType.StickerPosEyeShadow;
            }
            case 9: {
                return StickerPositionType.StickerPosLip;
            }
            case 100: {
                return StickerPositionType.StickerPosFullScreen;
            }
            case 101: {
                return StickerPositionType.StickerPosScreenLeftTop;
            }
            case 102: {
                return StickerPositionType.StickerPosScreenRightTop;
            }
            case 103: {
                return StickerPositionType.StickerPosScreenLeftBottom;
            }
            case 104: {
                return StickerPositionType.StickerPosScreenRightBottom;
            }
            case 105: {
                return StickerPositionType.StickerPosScreenCenter;
            }
            case 106: {
                return StickerPositionType.StickerPosScreenRightCenter;
            }
            case 107: {
                return StickerPositionType.StickerPosScreenLeftCenter;
            }
            case 108: {
                return StickerPositionType.StickerPosScreenTopCenter;
            }
            case 109: {
                return StickerPositionType.StickerPosScreenBottomCenter;
            }
            default: {
                return StickerPositionType.StickerPosHead;
            }
        }
    }
    
    public StickerRenderType getRenderType() {
        switch (this.renderType) {
            case 1: {
                return StickerRenderType.lsqRenderAlphaBlend;
            }
            case 2: {
                return StickerRenderType.lsqrenderBlendMultipy;
            }
            case 3: {
                return StickerRenderType.lsqRenderLightGlare;
            }
            default: {
                return StickerRenderType.lsqRenderAlphaBlend;
            }
        }
    }
    
    public TuSdkSize getDesignScreenSize() {
        if (this.screenWidth > 0 && this.screenHeight > 0) {
            return TuSdkSize.create(this.screenWidth, this.screenHeight);
        }
        return TuSdkSize.create(800, 1416);
    }
    
    public boolean hasAnimationSupported() {
        return this.resourceList != null && this.resourceList.size() > 0;
    }
    
    public void deserialize(final JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        this.modelWidth = jsonObject.optInt("model_width", 0);
        this.modelHeight = jsonObject.optInt("model_height", 0);
        this.screenWidth = jsonObject.optInt("screen_width", 0);
        this.screenHeight = jsonObject.optInt("screen_height", 0);
        this.categoryId = jsonObject.optInt("model_type", 0);
        this.posType = jsonObject.optInt("pos_type", 0);
        this.renderType = jsonObject.optInt("render_type", 0);
        this.ratio = (float)jsonObject.optDouble("ratio", 0.0);
        this.scale = (float)jsonObject.optDouble("scale", 0.0);
        this.offsetX = (float)jsonObject.optDouble("offset_x", 0.0);
        this.offsetY = (float)jsonObject.optDouble("offset_y", 0.0);
        this.rotation = (float)jsonObject.optDouble("rotation", 0.0);
        this.frameInterval = jsonObject.optInt("animation_interval", 0);
        this.resourceList = JsonHelper.toStringList(JsonHelper.getJSONArray(jsonObject, "animation_files"));
        this.loopMode = jsonObject.optInt("animation_loop", 0);
        this.loopStartIndex = jsonObject.optInt("animation_loop_start", 0);
    }
    
    public enum StickerRenderType
    {
        lsqRenderAlphaBlend, 
        lsqrenderBlendMultipy, 
        lsqRenderLightGlare;
    }
    
    public enum StickerLoopMode
    {
        lsqStickerLoop, 
        lsqStickerLoopReverse, 
        lsqStickerLoopRandom;
    }
    
    public enum StickerPositionType
    {
        StickerPosEyeBrow(1), 
        StickerPosEye(2), 
        StickerPosNose(3), 
        StickerPosMouth(4), 
        StickerPosCheek(5), 
        StickerPosHead(6), 
        StickerPosJaw(7), 
        StickerPosEyeShadow(8), 
        StickerPosLip(9), 
        StickerPosFullScreen(100), 
        StickerPosScreenLeftTop(101), 
        StickerPosScreenRightTop(102), 
        StickerPosScreenLeftBottom(103), 
        StickerPosScreenRightBottom(104), 
        StickerPosScreenCenter(105), 
        StickerPosScreenRightCenter(106), 
        StickerPosScreenLeftCenter(107), 
        StickerPosScreenTopCenter(108), 
        StickerPosScreenBottomCenter(109);
        
        private int a;
        
        private StickerPositionType(final int a) {
            this.a = a;
        }
        
        public int getValue() {
            return this.a;
        }
    }
}
