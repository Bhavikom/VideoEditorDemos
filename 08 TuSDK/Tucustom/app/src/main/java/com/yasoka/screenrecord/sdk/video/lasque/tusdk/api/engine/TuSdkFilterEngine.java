// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.engine;

import android.graphics.Bitmap;
import java.nio.IntBuffer;
//import org.lasque.tusdk.core.api.extend.TuSdkFilterListener;
import java.util.List;
//import org.lasque.tusdk.video.editor.TuSdkMediaEffectData;
//import org.lasque.tusdk.api.video.preproc.filter.TuSDKVideoProcesser;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.api.engine.TuSdkEngineProcessor;
//import org.lasque.tusdk.core.face.FaceAligment;
//import org.lasque.tusdk.modules.view.widget.sticker.StickerGroup;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
//import org.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
//import org.lasque.tusdk.core.utils.hardware.CameraConfigs;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.type.ColorFormatType;
import android.graphics.RectF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.engine.TuSdkEngineProcessor;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkFilterListener;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.face.FaceAligment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.type.ColorFormatType;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.CameraConfigs;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.InterfaceOrientation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker.StickerGroup;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.video.preproc.filter.TuSDKVideoProcesser;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkMediaEffectData;

public interface TuSdkFilterEngine
{
    void release();
    
    void setListener(final TuSdkFilterEngineListener p0);
    
    void onSurfaceCreated();
    
    void onSurfaceChanged(final int p0, final int p1);
    
    void setDisplayRect(final RectF p0, final float p1);
    
    void setDetectScale(final float p0);
    
    void setEnableLiveSticker(final boolean p0);
    
    void setEnableFaceDetection(final boolean p0);
    
    void setEnableOutputYUVData(final boolean p0);
    
    void setYuvOutputImageFormat(final ColorFormatType p0);
    
    void setYuvOutputOrienation(final ImageOrientation p0);
    
    CameraConfigs.CameraFacing getCameraFacing();
    
    void setCameraFacing(final CameraConfigs.CameraFacing p0);
    
    void setInterfaceOrientation(final InterfaceOrientation p0);
    
    void takeShot();
    
    void setInputImageOrientation(final ImageOrientation p0);
    
    void setOutputImageOrientation(final ImageOrientation p0);
    
    void setCordinateBuilder(final SelesVerticeCoordinateCorpBuilder p0);
    
    TuSdkSize getOutputImageSize();
    
    void switchFilter(final String p0);
    
    void removeAllLiveSticker();
    
    void showGroupSticker(final StickerGroup p0);
    
    void processFrame(final byte[] p0, final int p1, final int p2, final long p3);
    
    int processFrame(final int p0, final int p1, final int p2, final long p3);
    
    void snatchFrame(final byte[] p0);
    
    InterfaceOrientation getDeviceOrient();
    
    void setOriginalCaptureOrientation(final boolean p0);
    
    void setOutputCaptureMirrorEnabled(final boolean p0);
    
    void asyncProcessPictureData(final byte[] p0, final InterfaceOrientation p1);
    
    FaceAligment[] getFaceFeatures();
    
    void setFaceDetectionDelegate(final TuSdkEngineProcessor.TuSdkVideoProcesserFaceDetectionDelegate p0);
    
    float getDeviceAngle();
    
    void addTerminalNode(final SelesContext.SelesInput p0);
    
    void setMediaEffectDelegate(final TuSDKVideoProcesser.TuSDKVideoProcessorMediaEffectDelegate p0);
    
    boolean addMediaEffectData(final TuSdkMediaEffectData p0);
    
    boolean removeMediaEffectData(final TuSdkMediaEffectData p0);
    
     <T extends TuSdkMediaEffectData> List<T> mediaEffectsWithType(final TuSdkMediaEffectData.TuSdkMediaEffectDataType p0);
    
    List<TuSdkMediaEffectData> getAllMediaEffectData();
    
    void removeMediaEffectsWithType(final TuSdkMediaEffectData.TuSdkMediaEffectDataType p0);
    
    void removeAllMediaEffects();
    
    boolean hasMediaAudioEffects();
    
    void setFilterChangedListener(final TuSdkFilterEngineListener p0);
    
    boolean isGroupStickerUsed(final StickerGroup p0);
    
    public interface TuSdkFilterEngineListener extends TuSdkFilterListener
    {
        void onPictureDataCompleted(final IntBuffer p0, final TuSdkSize p1);
        
        void onPreviewScreenShot(final Bitmap p0);
    }
}
