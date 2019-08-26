package com.meishe.sdkdemo.utils;

import android.graphics.PointF;
import android.graphics.RectF;
import android.text.TextUtils;
import android.util.Log;

import com.meicam.sdk.NvsAudioClip;
import com.meicam.sdk.NvsAudioResolution;
import com.meicam.sdk.NvsAudioTrack;
import com.meicam.sdk.NvsColor;
import com.meicam.sdk.NvsRational;
import com.meicam.sdk.NvsSize;
import com.meicam.sdk.NvsStreamingContext;
import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsTimelineAnimatedSticker;
import com.meicam.sdk.NvsTimelineCaption;
import com.meicam.sdk.NvsTimelineCompoundCaption;
import com.meicam.sdk.NvsTimelineVideoFx;
import com.meicam.sdk.NvsVideoClip;
import com.meicam.sdk.NvsVideoFx;
import com.meicam.sdk.NvsVideoResolution;
import com.meicam.sdk.NvsVideoTrack;
import com.meishe.sdkdemo.edit.data.FilterItem;
import com.meishe.sdkdemo.edit.watermark.WaterMarkData;
import com.meishe.sdkdemo.edit.watermark.WaterMarkUtil;
import com.meishe.sdkdemo.utils.dataInfo.CaptionInfo;
import com.meishe.sdkdemo.utils.dataInfo.ClipInfo;
import com.meishe.sdkdemo.utils.dataInfo.CompoundCaptionInfo;
import com.meishe.sdkdemo.utils.dataInfo.MusicInfo;
import com.meishe.sdkdemo.utils.dataInfo.RecordAudioInfo;
import com.meishe.sdkdemo.utils.dataInfo.StickerInfo;
import com.meishe.sdkdemo.utils.dataInfo.TimelineData;
import com.meishe.sdkdemo.utils.dataInfo.TransitionInfo;
import com.meishe.sdkdemo.utils.dataInfo.VideoClipFxInfo;

import java.util.ArrayList;
import java.util.List;

import static com.meishe.sdkdemo.edit.watermark.WaterMarkConstant.WATERMARK_DYNAMICS_FXNAME;

/**
 * Created by admin on 2018/5/29.
 */

public class TimelineUtil {
    private static String TAG = "TimelineUtil";
    public final static long TIME_BASE = 1000000;
    //主编辑页面时间线API
    public static NvsTimeline createTimeline(){
        NvsTimeline timeline = newTimeline(TimelineData.instance().getVideoResolution());
        if(timeline == null) {
            Log.e(TAG, "failed to create timeline");
            return null;
        }
        if(!buildVideoTrack(timeline)) {
            return timeline;
        }

        timeline.appendAudioTrack(); // 音乐轨道
        timeline.appendAudioTrack(); // 录音轨道

        setTimelineData(timeline);

        return timeline;
    }
    //片段编辑页面时间线API
    public static NvsTimeline createSingleClipTimeline(ClipInfo clipInfo,boolean isTrimClip){
        NvsTimeline timeline = newTimeline(TimelineData.instance().getVideoResolution());
        if(timeline == null) {
            Log.e(TAG, "failed to create timeline");
            return null;
        }
        buildSingleClipVideoTrack(timeline,clipInfo,isTrimClip);
        return timeline;
    }
    //片段编辑页面时间线扩展API
    public static NvsTimeline createSingleClipTimelineExt(NvsVideoResolution videoEditRes,String filePath){
        NvsTimeline timeline = newTimeline(videoEditRes);
        if(timeline == null) {
            Log.e(TAG, "failed to create timeline");
            return null;
        }
        buildSingleClipVideoTrackExt(timeline,filePath);
        return timeline;
    }

     public static boolean buildSingleClipVideoTrack(NvsTimeline timeline,ClipInfo clipInfo,boolean isTrimClip) {
         if(timeline == null || clipInfo == null) {
             return false;
         }

         NvsVideoTrack videoTrack = timeline.appendVideoTrack();
         if(videoTrack == null){
             Log.e(TAG, "failed to append video track");
             return false;
         }
         addVideoClip(videoTrack,clipInfo,isTrimClip);
         return true;
     }
    public static boolean buildSingleClipVideoTrackExt(NvsTimeline timeline,String filePath) {
        if(timeline == null || filePath == null) {
            return false;
        }

        NvsVideoTrack videoTrack = timeline.appendVideoTrack();
        if(videoTrack == null){
            Log.e(TAG, "failed to append video track");
            return false;
        }
        NvsVideoClip videoClip = videoTrack.appendClip(filePath);
        if (videoClip == null){
            Log.e(TAG, "failed to append video clip");
            return false;
        }
        videoClip.changeTrimOutPoint(8000000,true);
        return true;
    }
    public static void setTimelineData(NvsTimeline timeline) {
        if(timeline == null)
            return;
        // 此处注意是clone一份音乐数据，因为添加主题的接口会把音乐数据删掉
        List<MusicInfo> musicInfoClone = TimelineData.instance().cloneMusicData();
        String themeId = TimelineData.instance().getThemeData();
        applyTheme(timeline,themeId);

        if(musicInfoClone != null) {
            TimelineData.instance().setMusicList(musicInfoClone);
            buildTimelineMusic(timeline, musicInfoClone);
        }

        VideoClipFxInfo videoClipFxData = TimelineData.instance().getVideoClipFxData();
        buildTimelineFilter(timeline, videoClipFxData);
        TransitionInfo transitionInfo = TimelineData.instance().getTransitionData();
        setTransition(timeline, transitionInfo);
        ArrayList<StickerInfo> stickerArray = TimelineData.instance().getStickerData();
        setSticker(timeline, stickerArray);

        ArrayList<CaptionInfo> captionArray = TimelineData.instance().getCaptionData();
        setCaption(timeline, captionArray);

        //compound caption
        ArrayList<CompoundCaptionInfo> compoundCaptionArray = TimelineData.instance().getCompoundCaptionArray();
        setCompoundCaption(timeline,compoundCaptionArray);

        ArrayList<RecordAudioInfo> recordArray = TimelineData.instance().getRecordAudioData();
        buildTimelineRecordAudio(timeline, recordArray);

        WaterMarkData waterMarkData = TimelineData.instance().getWaterMarkData();
        WaterMarkUtil.setWaterMark(timeline, waterMarkData);
    }

    public static boolean removeTimeline(NvsTimeline timeline){
        if(timeline == null)
            return false;

        NvsStreamingContext context = NvsStreamingContext.getInstance();
        if(context == null)
            return false;

        return context.removeTimeline(timeline);
    }

    public static boolean buildVideoTrack(NvsTimeline timeline) {
        if(timeline == null) {
            return false;
        }

        NvsVideoTrack videoTrack = timeline.appendVideoTrack();
        if(videoTrack == null){
            Log.e(TAG, "failed to append video track");
            return false;
        }

        ArrayList<ClipInfo> videoClipArray = TimelineData.instance().getClipInfoData();
        for (int i = 0;i < videoClipArray.size();i++) {
            ClipInfo clipInfo = videoClipArray.get(i);
            addVideoClip(videoTrack,clipInfo,true);
        }
        float videoVolume = TimelineData.instance().getOriginVideoVolume();
        videoTrack.setVolumeGain(videoVolume,videoVolume);

        return true;
    }

    public static boolean reBuildVideoTrack(NvsTimeline timeline) {
        if(timeline == null) {
            return false;
        }
        int videoTrackCount = timeline.videoTrackCount();
        NvsVideoTrack videoTrack = videoTrackCount == 0 ? timeline.appendVideoTrack() : timeline.getVideoTrackByIndex(0);
        if(videoTrack == null){
            Log.e(TAG, "failed to append video track");
            return false;
        }
        videoTrack.removeAllClips();
        ArrayList<ClipInfo> videoClipArray = TimelineData.instance().getClipInfoData();
        for (int i = 0;i < videoClipArray.size();i++) {
            ClipInfo clipInfo = videoClipArray.get(i);
            addVideoClip(videoTrack,clipInfo,true);
        }
        setTimelineData(timeline);
        float videoVolume = TimelineData.instance().getOriginVideoVolume();
        videoTrack.setVolumeGain(videoVolume,videoVolume);

        return true;
    }

    private static void addVideoClip(NvsVideoTrack videoTrack,ClipInfo clipInfo,boolean isTrimClip){
        if(videoTrack == null || clipInfo == null)
            return;
        String filePath = clipInfo.getFilePath();
        NvsVideoClip videoClip = videoTrack.appendClip(filePath);
        if (videoClip == null) {
            Log.e(TAG, "failed to append video clip");
            return;
        }

        boolean blurFlag = ParameterSettingValues.instance().isUseBackgroudBlur();
        if(blurFlag) {
            videoClip.setSourceBackgroundMode(NvsVideoClip.ClIP_BACKGROUNDMODE_BLUR);
        }

        float brightVal = clipInfo.getBrightnessVal();
        float contrastVal = clipInfo.getContrastVal();
        float saturationVal = clipInfo.getSaturationVal();
        float vignette = clipInfo.getVignetteVal();
        float sharpen = clipInfo.getSharpenVal();
        if(brightVal >= 0 || contrastVal >= 0 || saturationVal >= 0){
            NvsVideoFx videoFxColor = videoClip.appendBuiltinFx(Constants.FX_COLOR_PROPERTY);
            if(videoFxColor != null){
                if(brightVal >= 0)
                    videoFxColor.setFloatVal(Constants.FX_COLOR_PROPERTY_BRIGHTNESS,brightVal);
                if(contrastVal >= 0)
                    videoFxColor.setFloatVal(Constants.FX_COLOR_PROPERTY_CONTRAST,contrastVal);
                if(saturationVal >= 0)
                    videoFxColor.setFloatVal(Constants.FX_COLOR_PROPERTY_SATURATION,saturationVal);
            }
        }
        if(vignette >= 0) {
            NvsVideoFx vignetteVideoFx = videoClip.appendBuiltinFx(Constants.FX_VIGNETTE);
            vignetteVideoFx.setFloatVal(Constants.FX_VIGNETTE_DEGREE, vignette);
        }
        if(sharpen >= 0) {
            NvsVideoFx sharpenVideoFx = videoClip.appendBuiltinFx(Constants.FX_SHARPEN);
            sharpenVideoFx.setFloatVal(Constants.FX_SHARPEN_AMOUNT, sharpen);
        }
        int videoType = videoClip.getVideoType();
        if(videoType == NvsVideoClip.VIDEO_CLIP_TYPE_IMAGE){//当前片段是图片
            long trimIn = videoClip.getTrimIn();
            long trimOut = clipInfo.getTrimOut();
            if(trimOut > 0 && trimOut > trimIn ) {
                videoClip.changeTrimOutPoint(trimOut, true);
            }
            int imgDisplayMode = clipInfo.getImgDispalyMode();
            if(imgDisplayMode == Constants.EDIT_MODE_PHOTO_AREA_DISPLAY){//区域显示
                videoClip.setImageMotionMode(NvsVideoClip.IMAGE_CLIP_MOTIONMMODE_ROI);
                RectF normalStartRectF = clipInfo.getNormalStartROI();
                RectF normalEndRectF = clipInfo.getNormalEndROI();
                if(normalStartRectF != null && normalEndRectF != null){
                    videoClip.setImageMotionROI(normalStartRectF,normalEndRectF);
                }
            }else {//全图显示
                videoClip.setImageMotionMode(NvsVideoClip.CLIP_MOTIONMODE_LETTERBOX_ZOOMIN);
            }

            boolean isOpenMove = clipInfo.isOpenPhotoMove();
            videoClip.setImageMotionAnimationEnabled(isOpenMove);
        }else{//当前片段是视频
            float volumeGain = clipInfo.getVolume();
            videoClip.setVolumeGain(volumeGain,volumeGain);
            float pan = clipInfo.getPan();
            float scan = clipInfo.getScan();
            videoClip.setPanAndScan(pan,scan);
            float speed = clipInfo.getSpeed();
            if(speed > 0){
                videoClip.changeSpeed(speed);
            }
            videoClip.setExtraVideoRotation(clipInfo.getRotateAngle());
            int scaleX = clipInfo.getScaleX();
            int scaleY = clipInfo.getScaleY();
            if(scaleX >= -1 || scaleY >= -1){
                NvsVideoFx videoFxTransform = videoClip.appendBuiltinFx(Constants.FX_TRANSFORM_2D);
                if(videoFxTransform != null){
                    if(scaleX >= -1)
                        videoFxTransform.setFloatVal(Constants.FX_TRANSFORM_2D_SCALE_X,scaleX);
                    if(scaleY >= -1)
                        videoFxTransform.setFloatVal(Constants.FX_TRANSFORM_2D_SCALE_Y,scaleY);
                }
            }

            if(!isTrimClip)//如果当前是裁剪页面，不裁剪片段
                return;
            long trimIn = clipInfo.getTrimIn();
            long trimOut = clipInfo.getTrimOut();
            if(trimIn > 0) {
                videoClip.changeTrimInPoint(trimIn, true);
            }
            if(trimOut > 0 && trimOut > trimIn) {
                videoClip.changeTrimOutPoint(trimOut, true);
            }
        }
    }

    public static boolean buildTimelineFilter(NvsTimeline timeline, VideoClipFxInfo videoClipFxData) {
        if(timeline == null) {
            return false;
        }

        NvsVideoTrack videoTrack = timeline.getVideoTrackByIndex(0);
        if(videoTrack == null) {
            return false;
        }

        if(videoClipFxData == null)
            return false;

        ArrayList<ClipInfo> clipInfos = TimelineData.instance().getClipInfoData();

        int videoClipCount = videoTrack.getClipCount();
        for(int i = 0;i<videoClipCount;i++) {
            NvsVideoClip clip = videoTrack.getClipByIndex(i);
            if(clip == null)
                continue;

            removeAllVideoFx(clip);
            String clipFilPath = clip.getFilePath();
            boolean isSrcVideoAsset = false;
            for(ClipInfo clipInfo :clipInfos) {//过滤掉主题中自带片头或者片尾的视频
                String videoFilePath = clipInfo.getFilePath();
                if(clipFilPath.equals(videoFilePath)){
                    isSrcVideoAsset = true;
                    break;
                }
            }

            if(!isSrcVideoAsset)
                continue;

            String name = videoClipFxData.getFxId();
            if(TextUtils.isEmpty(name)) {
                continue;
            }
            int mode = videoClipFxData.getFxMode();
            float fxIntensity = videoClipFxData.getFxIntensity();
            if(mode == FilterItem.FILTERMODE_BUILTIN){//内建特效
                NvsVideoFx builtInFx;
                if(videoClipFxData.getIsCartoon()){
                    builtInFx = clip.appendBuiltinFx("Cartoon");
                    if(builtInFx != null){
                        builtInFx.setBooleanVal("Stroke Only", videoClipFxData.getStrokenOnly());
                        builtInFx.setBooleanVal("Grayscale", videoClipFxData.getGrayScale());
                    }else {
                        Logger.e(TAG,"Failed to append builtInFx-->" + "Cartoon");
                    }
                }else {
                    builtInFx = clip.appendBuiltinFx(name);
                }
                if(builtInFx != null) {
                    builtInFx.setFilterIntensity(fxIntensity);
                }else {
                    Logger.e(TAG,"Failed to append builtInFx-->" + name);
                }
            }else {////添加包裹特效
                NvsVideoFx packagedFx = clip.appendPackagedFx(name);
                if(packagedFx != null){
                    packagedFx.setFilterIntensity(fxIntensity);
                }else {
                    Logger.e(TAG,"Failed to append packagedFx-->" + name);
                }
            }
        }

        return true;
    }

    public static boolean applyTheme(NvsTimeline timeline, String themeId) {
        if(timeline == null)
            return false;

        timeline.removeCurrentTheme();
        if (themeId == null || themeId.isEmpty())
            return false;

        //设置主题片头和片尾
        String themeCaptionTitle = TimelineData.instance().getThemeCptionTitle();
        if(!themeCaptionTitle.isEmpty()){
            timeline.setThemeTitleCaptionText(themeCaptionTitle);
        }
        String themeCaptionTrailer = TimelineData.instance().getThemeCptionTrailer();
        if(!themeCaptionTrailer.isEmpty()){
            timeline.setThemeTrailerCaptionText(themeCaptionTrailer);
        }

        if(!timeline.applyTheme(themeId)) {
            Log.e(TAG, "failed to apply theme");
            return false;
        }

        timeline.setThemeMusicVolumeGain(1.0f, 1.0f);

        // 应用主题之后，要把已经应用的背景音乐去掉
        TimelineData.instance().setMusicList(null);
        TimelineUtil.buildTimelineMusic(timeline, null);
        return true;
    }

    private static boolean removeAllVideoFx(NvsVideoClip videoClip) {
        if(videoClip == null)
            return false;

        int fxCount = videoClip.getFxCount();
        for(int i=0;i<fxCount;i++) {
            NvsVideoFx fx = videoClip.getFxByIndex(i);
            if(fx == null)
                continue;

            String name = fx.getBuiltinVideoFxName();
            Log.e("===>", "fx name: " + name);
            if(name.equals(Constants.FX_COLOR_PROPERTY) || name.equals(Constants.FX_VIGNETTE) ||
                    name.equals(Constants.FX_SHARPEN)|| name.equals(Constants.FX_TRANSFORM_2D)) {
                continue;
            }
            videoClip.removeFx(i);
            i--;
        }
        return true;
    }

    public static boolean setTransition(NvsTimeline timeline, TransitionInfo transitionInfo) {
        if(timeline == null) {
            return false;
        }

        NvsVideoTrack videoTrack = timeline.getVideoTrackByIndex(0);
        if(videoTrack == null) {
            return false;
        }

        if(transitionInfo == null)
            return false;

        int videoClipCount = videoTrack.getClipCount();
        if(videoClipCount <= 1)
            return false;

        for(int i = 0;i<videoClipCount - 1;i++) {
            if(transitionInfo.getTransitionMode() == TransitionInfo.TRANSITIONMODE_BUILTIN) {
                videoTrack.setBuiltinTransition(i, transitionInfo.getTransitionId());
            } else {
                videoTrack.setPackagedTransition(i, transitionInfo.getTransitionId());
            }
        }

        return true;
    }

    public static boolean buildTimelineMusic(NvsTimeline timeline, List<MusicInfo> musicInfos) {
        if(timeline == null) {
            return false;
        }
        NvsAudioTrack audioTrack =timeline.getAudioTrackByIndex(0);
        if(audioTrack == null) {
            return false;
        }
        if(musicInfos == null || musicInfos.isEmpty()) {
                audioTrack.removeAllClips();

            // 去掉音乐之后，要把已经应用的主题中的音乐还原
            String pre_theme_id = TimelineData.instance().getThemeData();
            if (pre_theme_id != null && !pre_theme_id.isEmpty()) {
                timeline.setThemeMusicVolumeGain(1.0f, 1.0f);
            }
            return false;
        }
            audioTrack.removeAllClips();
        for(MusicInfo oneMusic: musicInfos) {
            if(oneMusic == null) {
                continue;
            }
            NvsAudioClip audioClip = audioTrack.addClip(oneMusic.getFilePath(), oneMusic.getInPoint(), oneMusic.getTrimIn(), oneMusic.getTrimOut());
            if(audioClip != null) {
                audioClip.setFadeInDuration(oneMusic.getFadeDuration());
                if(oneMusic.getExtraMusic() <= 0 && oneMusic.getExtraMusicLeft() <= 0) {
                    audioClip.setFadeOutDuration(oneMusic.getFadeDuration());
                }
            }
            if(oneMusic.getExtraMusic() > 0) {
                for(int i = 0; i < oneMusic.getExtraMusic(); ++i) {
                    NvsAudioClip extra_clip = audioTrack.addClip(oneMusic.getFilePath(),
                            oneMusic.getOriginalOutPoint() + i * (oneMusic.getOriginalOutPoint() - oneMusic.getOriginalInPoint()),
                            oneMusic.getOriginalTrimIn(), oneMusic.getOriginalTrimOut());
                    if(extra_clip != null) {
                        extra_clip.setAttachment(Constants.MUSIC_EXTRA_AUDIOCLIP, oneMusic.getInPoint());
                        if(i == oneMusic.getExtraMusic() - 1 && oneMusic.getExtraMusicLeft() <= 0) {
                            extra_clip.setAttachment(Constants.MUSIC_EXTRA_LAST_AUDIOCLIP, oneMusic.getInPoint());
                            extra_clip.setFadeOutDuration(oneMusic.getFadeDuration());
                        }
                    }
                }
            }
            if(oneMusic.getExtraMusicLeft() > 0) {
                NvsAudioClip extra_clip = audioTrack.addClip(oneMusic.getFilePath(),
                        oneMusic.getOriginalOutPoint() + oneMusic.getExtraMusic() * (oneMusic.getOriginalOutPoint() - oneMusic.getOriginalInPoint()),
                        oneMusic.getOriginalTrimIn(),
                        oneMusic.getOriginalTrimIn() + oneMusic.getExtraMusicLeft());
                if(extra_clip != null) {
                    extra_clip.setAttachment(Constants.MUSIC_EXTRA_AUDIOCLIP, oneMusic.getInPoint());
                    extra_clip.setAttachment(Constants.MUSIC_EXTRA_LAST_AUDIOCLIP, oneMusic.getInPoint());
                    extra_clip.setFadeOutDuration(oneMusic.getFadeDuration());
                }
            }
        }
        float audioVolume = TimelineData.instance().getMusicVolume();
        audioTrack.setVolumeGain(audioVolume,audioVolume);

        // 应用音乐之后，要把已经应用的主题中的音乐去掉
        String pre_theme_id = TimelineData.instance().getThemeData();
        if (pre_theme_id != null && !pre_theme_id.isEmpty()) {
            timeline.setThemeMusicVolumeGain(0 , 0);
        }
        return true;
    }

    public static void buildTimelineRecordAudio(NvsTimeline timeline, ArrayList<RecordAudioInfo> recordAudioInfos) {
        if(timeline == null) {
            return;
        }
        NvsAudioTrack audioTrack =timeline.getAudioTrackByIndex(1);
        if(audioTrack != null) {
            audioTrack.removeAllClips();
            if(recordAudioInfos != null) {
                for (int i = 0; i < recordAudioInfos.size(); ++i) {
                    RecordAudioInfo recordAudioInfo = recordAudioInfos.get(i);
                    if (recordAudioInfo == null) {
                        continue;
                    }
                    NvsAudioClip audioClip = audioTrack.addClip(recordAudioInfo.getPath(), recordAudioInfo.getInPoint(), recordAudioInfo.getTrimIn(),
                            recordAudioInfo.getOutPoint() - recordAudioInfo.getInPoint() + recordAudioInfo.getTrimIn());
                    if(audioClip != null) {
                        audioClip.setVolumeGain(recordAudioInfo.getVolume(), recordAudioInfo.getVolume());
                        if(recordAudioInfo.getFxID() != null && !recordAudioInfo.getFxID().equals(Constants.NO_FX)) {
                            audioClip.appendFx(recordAudioInfo.getFxID());
                        }
                    }
                }
            }
            float audioVolume = TimelineData.instance().getRecordVolume();
            audioTrack.setVolumeGain(audioVolume,audioVolume);
        }
    }

    public static boolean setSticker(NvsTimeline timeline, ArrayList<StickerInfo> stickerArray) {
        if(timeline == null)
            return false;

        NvsTimelineAnimatedSticker deleteSticker = timeline.getFirstAnimatedSticker();
        while (deleteSticker != null) {
            deleteSticker = timeline.removeAnimatedSticker(deleteSticker);
        }

        for(StickerInfo sticker : stickerArray) {
            long duration = sticker.getOutPoint() - sticker.getInPoint();
            boolean isCutsomSticker = sticker.isCustomSticker();
            NvsTimelineAnimatedSticker newSticker = isCutsomSticker ?
                    timeline.addCustomAnimatedSticker(sticker.getInPoint(),duration,sticker.getId(),sticker.getCustomImagePath())
                    : timeline.addAnimatedSticker(sticker.getInPoint(), duration, sticker.getId());
            if(newSticker == null)
                continue;
            newSticker.setZValue(sticker.getAnimateStickerZVal());
            newSticker.setHorizontalFlip(sticker.isHorizFlip());
            PointF translation = sticker.getTranslation();
            float scaleFactor = sticker.getScaleFactor();
            float rotation = sticker.getRotation();
            newSticker.setScale(scaleFactor);
            newSticker.setRotationZ(rotation);
            newSticker.setTranslation(translation);
            float volumeGain = sticker.getVolumeGain();
            newSticker.setVolumeGain(volumeGain,volumeGain);
        }
        return true;
    }

    public static boolean setCaption(NvsTimeline timeline,  ArrayList<CaptionInfo> captionArray) {
        if(timeline == null)
            return false;

        NvsTimelineCaption deleteCaption = timeline.getFirstCaption();
        while (deleteCaption != null) {
            int capCategory = deleteCaption.getCategory();
            Logger.e(TAG,"capCategory = " + capCategory);
            int roleTheme = deleteCaption.getRoleInTheme();
            if(capCategory == NvsTimelineCaption.THEME_CATEGORY
                        && roleTheme != NvsTimelineCaption.ROLE_IN_THEME_GENERAL){//主题字幕不作删除
                deleteCaption = timeline.getNextCaption(deleteCaption);
            }else {
                deleteCaption = timeline.removeCaption(deleteCaption);
            }
        }

        for(CaptionInfo caption : captionArray) {
            long duration = caption.getOutPoint() - caption.getInPoint();
            NvsTimelineCaption newCaption = timeline.addCaption(caption.getText(), caption.getInPoint(),
                    duration,null);
            updateCaptionAttribute(newCaption,caption);
        }
        return true;
    }

    //add compound caption
    public static boolean setCompoundCaption(NvsTimeline timeline,  ArrayList<CompoundCaptionInfo> captionArray) {
        if(timeline == null)
            return false;

        NvsTimelineCompoundCaption deleteCaption = timeline.getFirstCompoundCaption();
        while (deleteCaption != null) {
            deleteCaption = timeline.removeCompoundCaption(deleteCaption);
        }

        for(CompoundCaptionInfo caption : captionArray) {
            long duration = caption.getOutPoint() - caption.getInPoint();
            NvsTimelineCompoundCaption newCaption = timeline.addCompoundCaption(caption.getInPoint(),
                    duration,caption.getCaptionStyleUuid());
            updateCompoundCaptionAttribute(newCaption,caption);
        }
        return true;
    }

    //update compound caption attribute
    private static void updateCompoundCaptionAttribute(NvsTimelineCompoundCaption newCaption,CompoundCaptionInfo caption){
        if(newCaption == null || caption == null)
            return;

        ArrayList<CompoundCaptionInfo.CompoundCaptionAttr> captionAttrList = caption.getCaptionAttributeList();
        int captionCount = newCaption.getCaptionCount();
        for (int index = 0; index < captionCount; ++index) {
            CompoundCaptionInfo.CompoundCaptionAttr captionAttr = captionAttrList.get(index);
            if(captionAttr == null){
                continue;
            }
            NvsColor textColor = ColorUtil.colorStringtoNvsColor(captionAttr.getCaptionColor());
            if (textColor != null) {
                newCaption.setTextColor(index, textColor);
            }

            String fontName = captionAttr.getCaptionFontName();
            if (!TextUtils.isEmpty(fontName)) {
                newCaption.setFontFamily(index, fontName);
            }
            String captionText = captionAttr.getCaptionText();
            if(!TextUtils.isEmpty(captionText)){
                newCaption.setText(index,captionText);
            }
        }

        // 放缩字幕
        float scaleFactorX = caption.getScaleFactorX();
        float scaleFactorY = caption.getScaleFactorY();
        newCaption.setScaleX(scaleFactorX);
        newCaption.setScaleY(scaleFactorY);
        float rotation = caption.getRotation();
        // 旋转字幕
        newCaption.setRotationZ(rotation);
        newCaption.setZValue(caption.getCaptionZVal());
        PointF translation = caption.getTranslation();
        if(translation != null){
            newCaption.setCaptionTranslation(translation);
        }
    }

    private static void updateCaptionAttribute(NvsTimelineCaption newCaption,CaptionInfo caption){
        if(newCaption == null) {
            return;
        }
        if(caption == null) {
            return;
        }

        //字幕StyleUuid需要首先设置，后面设置的字幕属性才会生效，
        // 因为字幕样式里面可能自带偏移，缩放，旋转等属性，最后设置会覆盖前面的设置的。
        String styleUuid = caption.getCaptionStyleUuid();
        newCaption.applyCaptionStyle(styleUuid);

        int alignVal = caption.getAlignVal();
        if(alignVal >= 0) {
            newCaption.setTextAlignment(alignVal);
        }
        int userColorFlag = caption.getUsedColorFlag();
        if(userColorFlag == CaptionInfo.ATTRIBUTE_USED_FLAG){
            NvsColor textColor = ColorUtil.colorStringtoNvsColor(caption.getCaptionColor());
            if(textColor != null){
                textColor.a = caption.getCaptionColorAlpha() / 100.0f;
                newCaption.setTextColor(textColor);
            }
        }

        int usedScaleFlag = caption.getUsedScaleRotationFlag();
        if(usedScaleFlag == CaptionInfo.ATTRIBUTE_USED_FLAG){
            // 放缩字幕
            float scaleFactorX = caption.getScaleFactorX();
            float scaleFactorY = caption.getScaleFactorY();
            newCaption.setScaleX(scaleFactorX);
            newCaption.setScaleY(scaleFactorY);
            float rotation = caption.getRotation();
            // 旋转字幕
            newCaption.setRotationZ(rotation);
        }

        newCaption.setZValue(caption.getCaptionZVal());
        int usedOutlineFlag = caption.getUsedOutlineFlag();
        if(usedOutlineFlag == CaptionInfo.ATTRIBUTE_USED_FLAG){
            boolean hasOutline = caption.isHasOutline();
            newCaption.setDrawOutline(hasOutline);
            if(hasOutline){
                NvsColor outlineColor = ColorUtil.colorStringtoNvsColor(caption.getOutlineColor());
                if(outlineColor != null){
                    outlineColor.a = caption.getOutlineColorAlpha() / 100.0f;
                    newCaption.setOutlineColor(outlineColor);
                    newCaption.setOutlineWidth(caption.getOutlineWidth());
                }
            }
        }

        String fontPath = caption.getCaptionFont();
        if(!fontPath.isEmpty()) {
            newCaption.setFontByFilePath(fontPath);
        }

        int usedBold = caption.getUsedIsBoldFlag();
        if(usedBold == CaptionInfo.ATTRIBUTE_USED_FLAG){
            boolean isBold = caption.isBold();
            newCaption.setBold(isBold);
        }

        int usedItalic = caption.getUsedIsItalicFlag();
        if(usedItalic == CaptionInfo.ATTRIBUTE_USED_FLAG){
            boolean isItalic = caption.isItalic();
            newCaption.setItalic(isItalic);
        }
        int usedShadow = caption.getUsedShadowFlag();
        if(usedShadow == CaptionInfo.ATTRIBUTE_USED_FLAG){
            boolean isShadow = caption.isShadow();
            newCaption.setDrawShadow(isShadow);
            if(isShadow) {
                PointF offset = new PointF(7, -7);
                NvsColor shadowColor = new NvsColor(0, 0, 0, 0.5f);
                newCaption.setShadowOffset(offset);  //字幕阴影偏移量
                newCaption.setShadowColor(shadowColor); // 字幕阴影颜色
            }
        }
//        float fontSize = caption.getCaptionSize();
//        if(fontSize >= 0) {
//            newCaption.setFontSize(fontSize);
//        }
        int usedTranslationFlag = caption.getUsedTranslationFlag();
        if(usedTranslationFlag == CaptionInfo.ATTRIBUTE_USED_FLAG){
            PointF translation = caption.getTranslation();
            if(translation != null) {
                newCaption.setCaptionTranslation(translation);
            }
        }
    }

    public static NvsTimeline newTimeline(NvsVideoResolution videoResolution){
        NvsStreamingContext context = NvsStreamingContext.getInstance();
        if(context == null) {
            Log.e(TAG, "failed to get streamingContext");
            return null;
        }

        NvsVideoResolution videoEditRes = videoResolution;
        videoEditRes.imagePAR = new NvsRational(1, 1);
        NvsRational videoFps = new NvsRational(25, 1);

        NvsAudioResolution audioEditRes = new NvsAudioResolution();
        audioEditRes.sampleRate = 44100;
        audioEditRes.channelCount = 2;

        NvsTimeline timeline = context.createTimeline(videoEditRes, videoFps, audioEditRes);
        return timeline;
    }

    public static NvsSize getTimelineSize(NvsTimeline timeline) {
        NvsSize size = new NvsSize(0, 0);
        if(timeline != null) {
            NvsVideoResolution resolution = timeline.getVideoRes();
            size.width = resolution.imageWidth;
            size.height = resolution.imageHeight;
            return size;
        }
        return null;
    }

    public static void checkAndDeleteExitFX(NvsTimeline mTimeline) {
        NvsTimelineVideoFx nvsTimelineVideoFx = mTimeline.getFirstTimelineVideoFx();
        while (nvsTimelineVideoFx != null) {
            String name = nvsTimelineVideoFx.getBuiltinTimelineVideoFxName();
            if (name.equals(WATERMARK_DYNAMICS_FXNAME)){
                mTimeline.removeTimelineVideoFx(nvsTimelineVideoFx);
                break;
            }else {
                nvsTimelineVideoFx = mTimeline.getNextTimelineVideoFx(nvsTimelineVideoFx);
            }
        }
    }
}
