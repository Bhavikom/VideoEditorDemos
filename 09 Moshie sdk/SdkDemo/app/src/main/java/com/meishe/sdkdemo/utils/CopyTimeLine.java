package com.meishe.sdkdemo.utils;

import android.util.Log;

import com.meicam.sdk.NvsAudioClip;
import com.meicam.sdk.NvsAudioFx;
import com.meicam.sdk.NvsAudioResolution;
import com.meicam.sdk.NvsAudioTrack;
import com.meicam.sdk.NvsFx;
import com.meicam.sdk.NvsFxDescription;
import com.meicam.sdk.NvsRational;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsTimelineAnimatedSticker;
import com.meicam.sdk.NvsTimelineCaption;
//import com.meicam.sdk.NvsTimelineVideoFx;
import com.meicam.sdk.NvsTimelineVideoFx;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoFx;
import com.meicam.sdk.NvsVideoResolution;
import com.meicam.sdk.NvsVideoTrack;
import com.meicam.sdk.NvsVideoTransition;

import java.util.List;

import static com.meicam.sdk.NvsFxDescription.ParamInfoObject.PARAM_BOOL_DEF_VAL;
import static com.meicam.sdk.NvsFxDescription.ParamInfoObject.PARAM_INT_DEF_VAL;
import static com.meicam.sdk.NvsFxDescription.ParamInfoObject.PARAM_NAME;
import static com.meicam.sdk.NvsFxDescription.ParamInfoObject.PARAM_STRING_DEF;
import static com.meicam.sdk.NvsFxDescription.ParamInfoObject.PARAM_TYPE;

public class CopyTimeLine {

    private static String TAG = "CopyTimeLine";

    public static NvsTimeline copyTimeline(NvsTimeline oldTimeline, NvsStreamingContext streamingContext) {

        if(streamingContext == null || oldTimeline == null){
            Log.e(TAG,"timeline or streamingContext is null!！");
            return null;
        }

        NvsVideoResolution videoEditRes = oldTimeline.getVideoRes();
        NvsRational videoFps = oldTimeline.getVideoFps();
        NvsAudioResolution audioEditRes = oldTimeline.getAudioRes();

        NvsTimeline newTimeline = streamingContext.createTimeline(videoEditRes,videoFps,audioEditRes);

        if(newTimeline == null){
            Log.e(TAG,"create Timeline failed！");
            return null;
        }
        /* 拷贝音轨 */
        for (int i = 0; i < oldTimeline.audioTrackCount(); i++) {
            NvsAudioTrack oldAudioTrack = oldTimeline.getAudioTrackByIndex(i);
            NvsAudioTrack newAudioTrack = newTimeline.appendAudioTrack();
            if(newAudioTrack == null){
                Log.e(TAG,"add audio track failed！");
                return null;
            }

            newAudioTrack.setVolumeGain(oldAudioTrack.getVolumeGain().leftVolume,oldAudioTrack.getVolumeGain().rightVolume);

            for(int j = 0;j < oldAudioTrack.getClipCount();j++){
                NvsAudioClip oldAudioClip = oldAudioTrack.getClipByIndex(j);
                NvsAudioClip newAudioClip = newAudioTrack.addClip(oldAudioClip.getFilePath(),oldAudioClip.getInPoint(),oldAudioClip.getTrimIn(),oldAudioClip.getTrimOut());
                if(newAudioClip == null){
                    Log.e(TAG,"add audio clip failed！");
                    return null;
                }

                newAudioClip.setFadeInDuration(oldAudioClip.getFadeInDuration());
                newAudioClip.setFadeOutDuration(oldAudioClip.getFadeOutDuration());

                newAudioClip.changeSpeed(oldAudioClip.getSpeed());
                newAudioClip.setVolumeGain(oldAudioClip.getVolumeGain().leftVolume,oldAudioClip.getVolumeGain().rightVolume);

                for(int k = 0; k < oldAudioClip.getFxCount();k++){
                    NvsAudioFx oldFx = oldAudioClip.getFxByIndex(k);
                    if(oldFx != null){
                        NvsAudioFx newFx = newAudioClip.insertFx(oldFx.getBuiltinAudioFxName(),k);
                        if(newFx == null){
                            Log.e(TAG,"add audio fx failed！");
                        }
                    }
                }
            }
            /*音轨转场*/
            for(int j = 0;j < oldAudioTrack.getClipCount();j++){
                if(oldAudioTrack.getTransitionWithSourceClipIndex(j) != null)
                    newAudioTrack.setBuiltinTransition(j,oldAudioTrack.getTransitionWithSourceClipIndex(j).getDescription().getName());
            }
        }

        /* 拷贝视频轨 */
        for (int i = 0; i < oldTimeline.videoTrackCount(); i++) {
            NvsVideoTrack oldVideoTrack = oldTimeline.getVideoTrackByIndex(i);
            NvsVideoTrack newVideoTrack = newTimeline.appendVideoTrack();
            if(newVideoTrack == null){
                Log.e(TAG,"add video track failed！");
                return null;
            }

            newVideoTrack.setVolumeGain(oldVideoTrack.getVolumeGain().leftVolume,oldVideoTrack.getVolumeGain().rightVolume);

            for(int j = 0;j < oldVideoTrack.getClipCount();j++){
                NvsVideoClip oldVideoClip = oldVideoTrack.getClipByIndex(j);
                if(oldVideoClip != null){
                    NvsVideoClip newVideoClip = newVideoTrack.addClip(oldVideoClip.getFilePath(),oldVideoClip.getInPoint(),oldVideoClip.getTrimIn(),oldVideoClip.getTrimOut());
                    if(newVideoClip == null){
                        Log.e(TAG,"add video clip failed！");
                        return null;
                    }

                    newVideoClip.changeSpeed(oldVideoClip.getSpeed());
                    newVideoClip.setVolumeGain(oldVideoClip.getVolumeGain().leftVolume,oldVideoClip.getVolumeGain().rightVolume);

                    newVideoClip.setPlayInReverse(oldVideoClip.getPlayInReverse());
                    newVideoClip.setExtraVideoRotation(oldVideoClip.getExtraVideoRotation());
                    newVideoClip.setSoftWareDecoding(oldVideoClip.isSoftWareDeocedUsed());

                    newVideoClip.disableAmbiguousCrop(oldVideoClip.isAmbiguousCropDisabled());
                    newVideoClip.setPanAndScan(oldVideoClip.getPanAndScan().pan,oldVideoClip.getPanAndScan().scan);
                    newVideoClip.setSourceBackgroundMode(oldVideoClip.getSourceBackgroundMode());

                    if(oldVideoClip.getVideoType() == NvsVideoClip.VIDEO_CLIP_TYPE_IMAGE){
                        newVideoClip.setImageMotionMode(oldVideoClip.getImageMotionMode());
                        newVideoClip.setImageMotionAnimationEnabled(oldVideoClip.getImageMotionAnimationEnabled());
                        newVideoClip.setImageMotionROI (oldVideoClip.getStartROI(),oldVideoClip.getEndROI());
                        newVideoClip.setImageMaskROI(oldVideoClip.getImageMaskROI());
                    }
                    newVideoClip.setClipWrapMode(oldVideoClip.getClipWrapMode());

                    for(int k = 0; k < oldVideoClip.getFxCount();k++){
                        NvsVideoFx oldFx = oldVideoClip.getFxByIndex(k);
                        if(oldFx != null){
                            if(oldFx.getVideoFxType() == NvsVideoFx.VIDEOFX_TYPE_BUILTIN){
                                NvsVideoFx newBuiltFx = newVideoClip.insertBuiltinFx(oldFx.getBuiltinVideoFxName(),k);
                                if(newBuiltFx != null){
                                    copyBuiltFx(streamingContext,oldFx,newBuiltFx);
                                }
                            }else if(oldFx.getVideoFxType() == NvsVideoFx.VIDEOFX_TYPE_PACKAGE){
                                NvsVideoFx newPackageFx = newVideoClip.insertPackagedFx(oldFx.getVideoFxPackageId(),k);
                                if(newPackageFx != null){
                                    newPackageFx.setFilterIntensity(oldFx.getFilterIntensity());
                                }
                            }
                        }
                    }
                }
            }
            /*视频轨转场*/
            for(int j = 0 ;j < oldVideoTrack.getClipCount(); j++){
                if(oldVideoTrack.getTransitionBySourceClipIndex(j) != null){
                    if(oldVideoTrack.getTransitionBySourceClipIndex(j).getVideoTransitionType() == NvsVideoTransition.VIDEO_TRANSITION_TYPE_BUILTIN){
                        NvsVideoTransition videoBuiltTransition = newVideoTrack.setBuiltinTransition(j,oldVideoTrack.getTransitionBySourceClipIndex(j).getBuiltinVideoTransitionName());
                        if(videoBuiltTransition != null)
                            videoBuiltTransition.setVideoTransitionDurationScaleFactor(oldVideoTrack.getTransitionBySourceClipIndex(j).getVideoTransitionDurationScaleFactor());
                    }else if(oldVideoTrack.getTransitionBySourceClipIndex(j).getVideoTransitionType() == NvsVideoTransition.VIDEO_TRANSITION_TYPE_PACKAGE){
                        NvsVideoTransition videoPackageTransition = newVideoTrack.setPackagedTransition(j,oldVideoTrack.getTransitionBySourceClipIndex(j).getVideoTransitionPackageId());
                        if(videoPackageTransition != null)
                            videoPackageTransition.setVideoTransitionDurationScaleFactor(oldVideoTrack.getTransitionBySourceClipIndex(j).getVideoTransitionDurationScaleFactor());
                    }
                }
            }
        }


        //字幕
        NvsTimelineCaption oldFirstCaption = oldTimeline.getFirstCaption();
        if (oldFirstCaption != null){
            while (oldFirstCaption != null){
                copyCation(newTimeline,oldFirstCaption);
                NvsTimelineCaption next = oldTimeline.getNextCaption(oldFirstCaption);
                oldFirstCaption = next;
            }
        }
        //贴纸
        NvsTimelineAnimatedSticker oldFirstSticker = oldTimeline.getFirstAnimatedSticker();
        if (oldFirstSticker != null){
            while (oldFirstSticker != null){
                copySticker(newTimeline,oldFirstSticker);
                NvsTimelineAnimatedSticker next = oldTimeline.getNextAnimatedSticker(oldFirstSticker);
                oldFirstSticker = next;
            }
        }

        //时间线视频特效
        NvsTimelineVideoFx oldFirstVideoFx = oldTimeline.getFirstTimelineVideoFx();
        if (oldFirstVideoFx != null){
            while (oldFirstVideoFx != null){
                copyVideoFx(streamingContext,newTimeline,oldFirstVideoFx);
                NvsTimelineVideoFx next = oldTimeline.getNextTimelineVideoFx(oldFirstVideoFx);
                oldFirstVideoFx = next;
            }
        }

        //主题
        newTimeline.applyTheme(oldTimeline.getCurrentThemeId());
        newTimeline.setThemeMusicVolumeGain(oldTimeline.getThemeMusicVolumeGain().leftVolume,oldTimeline.getThemeMusicVolumeGain().rightVolume);

        newTimeline.setAudioFadeOutDuration(oldTimeline.getAudioFadeOutDuration());

        return newTimeline;
    }

    private static void copyBuiltFx(NvsStreamingContext streamingContext, NvsFx oldBuiltFx, NvsFx newBuiltFx){
        NvsFxDescription fxDescription =  streamingContext.getVideoFxDescription(oldBuiltFx.getDescription().getName());
        if(fxDescription != null){
            List<NvsFxDescription.ParamInfoObject> paramInfo = fxDescription.getAllParamsInfo();
            for (NvsFxDescription.ParamInfoObject param : paramInfo) {
                String paramType = param.getString(PARAM_TYPE);
                String paramName = param.getString(PARAM_NAME);

                switch (paramType){
                    case NvsFxDescription.ParamInfoObject.PARAM_TYPE_INT:
                        if(param.getInteger(PARAM_INT_DEF_VAL) != oldBuiltFx.getIntVal(paramName))
                            newBuiltFx.setIntVal(paramName,oldBuiltFx.getIntVal(paramName));
                        break;
                    case NvsFxDescription.ParamInfoObject.PARAM_TYPE_FLOAT:
                        newBuiltFx.setFloatVal(paramName,oldBuiltFx.getFloatVal(paramName));
                        break;
                    case NvsFxDescription.ParamInfoObject.PARAM_TYPE_BOOL:
                        if(param.getBoolean(PARAM_BOOL_DEF_VAL) != oldBuiltFx.getBooleanVal(paramName))
                            newBuiltFx.setBooleanVal(paramName,oldBuiltFx.getBooleanVal(paramName));
                        break;
                    case NvsFxDescription.ParamInfoObject.PARAM_TYPE_MENU:
                        newBuiltFx.setMenuVal(paramName,oldBuiltFx.getMenuVal(paramName));
                        break;
                    case NvsFxDescription.ParamInfoObject.PARAM_TYPE_STRING:
                        if(param.getString(PARAM_STRING_DEF) != null && !param.getString(PARAM_STRING_DEF).equals(oldBuiltFx.getStringVal(paramName)))
                            newBuiltFx.setStringVal(paramName,oldBuiltFx.getStringVal(paramName));
                        break;
                    case NvsFxDescription.ParamInfoObject.PARAM_TYPE_COLOR:
                        newBuiltFx.setColorVal(paramName,oldBuiltFx.getColorVal(paramName));
                        break;
                    case NvsFxDescription.ParamInfoObject.PARAM_TYPE_POSITION2D:
                        newBuiltFx.setPosition2DVal(paramName,oldBuiltFx.getPosition2DVal(paramName));
                        break;
                    case NvsFxDescription.ParamInfoObject.PARAM_TYPE_POSITION3D:
                        newBuiltFx.setPosition3DVal(paramName,oldBuiltFx.getPosition3DVal(paramName));
                        break;
                }
            }
        }
        newBuiltFx.setFilterIntensity(oldBuiltFx.getFilterIntensity());
    }

    private static void copyVideoFx(NvsStreamingContext streamingContext, NvsTimeline newTimeline, NvsTimelineVideoFx oldVideoFx) {
        if(oldVideoFx.getTimelineVideoFxType() == NvsTimelineVideoFx.TIMELINE_VIDEOFX_TYPE_BUILTIN){
            NvsTimelineVideoFx newBuiltVideoFx = newTimeline.addBuiltinTimelineVideoFx(oldVideoFx.getInPoint(),oldVideoFx.getOutPoint()-oldVideoFx.getInPoint(),oldVideoFx.getBuiltinTimelineVideoFxName());
            if(newBuiltVideoFx != null){
                copyBuiltFx(streamingContext,newBuiltVideoFx,oldVideoFx);
            }

        }else if(oldVideoFx.getTimelineVideoFxType() == NvsTimelineVideoFx.TIMELINE_VIDEOFX_TYPE_PACKAGE){
            NvsTimelineVideoFx newPackageVideoFx =  newTimeline.addPackagedTimelineVideoFx(oldVideoFx.getInPoint(),oldVideoFx.getOutPoint()-oldVideoFx.getInPoint(),oldVideoFx.getTimelineVideoFxPackageId());
            if(newPackageVideoFx != null){
                newPackageVideoFx.setFilterIntensity(oldVideoFx.getFilterIntensity());
            }
        }
    }

    private static void copySticker(NvsTimeline newTimeline, NvsTimelineAnimatedSticker oldSticker) {
        NvsTimelineAnimatedSticker newSticker = newTimeline.addAnimatedSticker(oldSticker.getInPoint(),oldSticker.getOutPoint()-oldSticker.getInPoint(),oldSticker.getAnimatedStickerPackageId());
        if(newSticker != null){
            newSticker.setClipAffinityEnabled(oldSticker.getClipAffinityEnabled());
            if(oldSticker.hasAudio()){
                newSticker.setVolumeGain(oldSticker.getVolumeGain().leftVolume,oldSticker.getVolumeGain().rightVolume);
            }
            newSticker.setScale(oldSticker.getScale());
            newSticker.setHorizontalFlip(oldSticker.getHorizontalFlip());
            newSticker.setVerticalFlip(oldSticker.getVerticalFlip());
            newSticker.setRotationZ(oldSticker.getRotationZ());
            newSticker.setTranslation(oldSticker.getTranslation());
            newSticker.setZValue(oldSticker.getZValue());
        }
    }

    private static void copyCation(NvsTimeline newTimeline, NvsTimelineCaption oldCaption){
        NvsTimelineCaption newCaption;
        if(oldCaption.isPanoramic()){
            newCaption =newTimeline.addPanoramicCaption(oldCaption.getText(),oldCaption.getInPoint(),oldCaption.getOutPoint()-oldCaption.getInPoint(),oldCaption.getCaptionStylePackageId());
            if(newCaption != null){
                newCaption.setPanoramicRotation(oldCaption.getPanoramicRotation());
                newCaption.setPanoramicScaleY(oldCaption.getPanoramicScaleY());
                newCaption.setPanoramicScaleX(oldCaption.getPanoramicScaleX());
                newCaption.setCenterPolarAngle(oldCaption.getCenterPolarAngle ());
                newCaption.setCenterAzimuthAngle(oldCaption.getCenterAzimuthAngle());
                newCaption.setPolarAngleRange (oldCaption.getPolarAngleRange());

            }
        }else{
            newCaption = newTimeline.addCaption(oldCaption.getText(),oldCaption.getInPoint(),oldCaption.getOutPoint()-oldCaption.getInPoint(),oldCaption.getCaptionStylePackageId());
        }

        if(newCaption != null){
            newCaption.setClipAffinityEnabled(oldCaption.getClipAffinityEnabled());
            newCaption.setTextAlignment(oldCaption.getTextAlignment());
            newCaption.setBold(oldCaption.getBold());
            newCaption.setItalic(oldCaption.getItalic());

            newCaption.setLetterSpacing(oldCaption.getLetterSpacing());
            newCaption.setTextColor(oldCaption.getTextColor());
            newCaption.setDrawOutline(oldCaption.getDrawOutline());
            newCaption.setOutlineColor(oldCaption.getOutlineColor());
            newCaption.setOutlineWidth(oldCaption.getOutlineWidth());
            newCaption.setDrawShadow(oldCaption.getDrawShadow());
            newCaption.setShadowColor(oldCaption.getShadowColor());

            newCaption.setShadowOffset(oldCaption.getShadowOffset());
            newCaption.setFontSize(oldCaption.getFontSize());
            newCaption.setFontByFilePath(oldCaption.getFontFilePath());
            newCaption.setFontFamily(oldCaption.getFontFamily());
            newCaption.setCaptionTranslation(oldCaption.getCaptionTranslation());

            newCaption.setAnchorPoint(oldCaption.getAnchorPoint());
            newCaption.setScaleX (oldCaption.getScaleX());
            newCaption.setScaleY(oldCaption.getScaleY());
            newCaption.setRotationZ(oldCaption.getRotationZ());
            newCaption.setZValue (oldCaption.getZValue());
        }
    }

}
