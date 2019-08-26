package com.meishe.sdkdemo.edit.watermark;

import com.meicam.sdk.NvsTimeline;
import com.meicam.sdk.NvsTimelineVideoFx;
import com.meishe.sdkdemo.utils.PathUtils;

import static com.meishe.sdkdemo.edit.watermark.WaterMarkConstant.ASSETS_WATERMARK_PICTURE_PATH;
import static com.meishe.sdkdemo.edit.watermark.WaterMarkConstant.DEFAULT_WATERMARK_PICTURE;
import static com.meishe.sdkdemo.edit.watermark.WaterMarkConstant.WATERMARK_DYNAMICS_FXNAME;

/**
 * Created by CaoZhiChao on 2018/10/25 13:52
 */
public class WaterMarkUtil {
    public static void setWaterMark(NvsTimeline mTimeline, WaterMarkData waterMarkData){
        if (waterMarkData == null || mTimeline == null){
            return;
        }
        int type = waterMarkData.getWaterMarkItemData().getItemWaterMarkType();
        int pictureW = waterMarkData.getPicWidth();
        int pictureH = waterMarkData.getPicHeight();
        String waterMarkPath = waterMarkData.getWaterMarkItemData().getWaterMarkpath();
        int marginX = waterMarkData.getExcursionX();
        int marginY = waterMarkData.getExcursionY();
        if (type == WaterMarkConstant.WATERMARKTYPE_STATIC){
            mTimeline.addWatermark(waterMarkPath, pictureW,pictureH,1, NvsTimeline.NvsTimelineWatermarkPosition_TopRight , marginX, marginY);
        }else {
            mTimeline.deleteWatermark();
            String pictureName = waterMarkData.getWaterMarkItemData().getItemPicturePath();
            String dir = ASSETS_WATERMARK_PICTURE_PATH;
            if (pictureName.contains(DEFAULT_WATERMARK_PICTURE)){
                dir = PathUtils.getWatermarkCafDirectoryDir();
            }
            int sceneWidth = waterMarkData.getPointOfLiveWindow().x;
            int sceneHeight =waterMarkData.getPointOfLiveWindow().y;
            float transX = waterMarkData.getTransX();
            float transY = waterMarkData.getTransY();
            setDynamicWaterMark(mTimeline,sceneWidth,sceneHeight,waterMarkPath,pictureW,pictureH,dir,transX,transY,1);
        }
    }
    /**
     * 动态水印的默认位置是中心位置
     * @param sceneWidth liveWindow的宽
     * @param sceneHeight 同上：高
     * @param fileName 文件名称 ps: 123.caf
     * @param width 动态水印的宽
     * @param height 同上 高
     * @param dir 文件路径  ps：assets: 后面没有/
     * @param transX 偏移量
     * @param transY 偏移量
     * @param scale 缩放值  默认为1
     */
    public static NvsTimelineVideoFx setDynamicWaterMark(NvsTimeline nvsTimeline,int sceneWidth, int sceneHeight, String fileName,
                                           int width, int height, String dir,float transX, float transY,float scale){
        boolean isRepeat = true;
        int during = 2000;
        NvsTimelineVideoFx story = nvsTimeline.addBuiltinTimelineVideoFx(0, nvsTimeline.getDuration(), WATERMARK_DYNAMICS_FXNAME);
        String descString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>        " +
                "<storyboard sceneWidth=\"" + sceneWidth + "\" sceneHeight=\"" + sceneHeight + "\">           " +
                "<track source=\"" + fileName + "\" width=\"" + width + "\" height=\"" + height + "\" clipStart=\"0\" clipDuration=\""+during+"\" repeat=\""+isRepeat+"\">" +
                "<effect name=\"transform\">" +
                "<param name=\"opacity\" value=\"1\"/>" +
                "<param name=\"transX\" value=\"0\"/>" +
                "<param name=\"transY\" value=\"0\"/>" +
                "</effect></track></storyboard>";
        story.setStringVal("Resource Dir", dir);
        story.setStringVal("Description String", descString);
        story.setBooleanVal("Is Animated Sticker", true);
        story.setFloatVal("Sticker Scale", scale);//缩放
        ////平移
        story.setFloatVal("Sticker TransX", transX);
        story.setFloatVal("Sticker TransY", -transY);
        return story;
    }
}
