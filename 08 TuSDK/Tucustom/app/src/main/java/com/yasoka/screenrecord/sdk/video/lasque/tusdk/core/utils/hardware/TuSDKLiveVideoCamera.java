// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.utils.hardware;

//import org.lasque.tusdk.core.secret.ColorSpaceConvert;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
//import org.lasque.tusdk.core.seles.tusdk.FilterManager;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.secret.SdkValid;
//import org.lasque.tusdk.core.seles.video.SelesSurfaceEncoderInterface;
//import org.lasque.tusdk.impl.components.camera.TuVideoFocusTouchView;
//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import java.nio.IntBuffer;
//import org.lasque.tusdk.core.utils.ThreadHelper;
//import org.lasque.tusdk.core.encoder.video.TuSDKSoftVideoDataEncoderInterface;
//import org.lasque.tusdk.core.type.ColorFormatType;
import android.graphics.ImageFormat;
//import org.lasque.tusdk.core.video.TuSDKVideoCaptureSetting;
import android.graphics.PointF;
import android.widget.RelativeLayout;
import android.content.Context;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.face.FaceAligment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.ColorSpaceConvert;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.SdkValid;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.output.SelesOffscreen;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterManager;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.type.ColorFormatType;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.CameraConfigs;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.video.TuSDKSoftVideoDataEncoderInterface;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.seles.video.SelesSurfaceEncoderInterface;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.video.TuSDKVideoCaptureSetting;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.impl.components.camera.TuVideoFocusTouchView;

import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.seles.output.SelesOffscreen;

public class TuSDKLiveVideoCamera extends TuSDKVideoCamera
{
    private SelesOffscreen a;
    private ByteBuffer b;
    private TuSDKLiveVideoCameraDelegate c;
    private SelesOffscreen.SelesOffscreenDelegate d;
    
    public TuSDKLiveVideoCamera(final Context context, final RelativeLayout relativeLayout) {
        super(context, new TuSDKVideoCaptureSetting(), relativeLayout);
        this.d = (SelesOffscreen.SelesOffscreenDelegate)new SelesOffscreen.SelesOffscreenDelegate() {
            public boolean onFrameRendered(final SelesOffscreen selesOffscreen) {
                if (!TuSDKLiveVideoCamera.this.isRecording()) {
                    return false;
                }
                final IntBuffer renderBuffer = selesOffscreen.renderBuffer();
                if (renderBuffer == null) {
                    return true;
                }
                final IntBuffer intBuffer = renderBuffer;
                if (TuSDKLiveVideoCamera.this.getVideoCaptureSetting().videoAVCodecType == TuSDKVideoCaptureSetting.AVCodecType.HW_CODEC) {
                    return true;
                }
                if (TuSDKLiveVideoCamera.this.b == null) {
                    final TuSdkSize videoSize = TuSDKLiveVideoCamera.this.getVideoCaptureSetting().videoSize;
                    TuSDKLiveVideoCamera.this.b = ByteBuffer.allocate(videoSize.width * videoSize.height * ImageFormat.getBitsPerPixel(17) / 8);
                }
                TuSDKLiveVideoCamera.this.b.position(0);
                if (TuSDKLiveVideoCamera.this.getVideoCaptureSetting().videoAVCodecType == TuSDKVideoCaptureSetting.AVCodecType.SW_CODEC) {
                    TuSDKLiveVideoCamera.this.a(intBuffer, TuSDKLiveVideoCamera.this.b.array(), ColorFormatType.NV21);
                    final TuSDKSoftVideoDataEncoderInterface tuSDKSoftVideoDataEncoderInterface = (TuSDKSoftVideoDataEncoderInterface)TuSDKLiveVideoCamera.this.getVideoDataEncoder();
                    if (tuSDKSoftVideoDataEncoderInterface != null) {
                        tuSDKSoftVideoDataEncoderInterface.queueVideo(TuSDKLiveVideoCamera.this.b.array());
                    }
                }
                else {
                    TuSDKLiveVideoCamera.this.a(intBuffer, TuSDKLiveVideoCamera.this.b.array(), TuSDKLiveVideoCamera.this.getVideoCaptureSetting().imageFormatType);
                    ThreadHelper.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            if (TuSDKLiveVideoCamera.this.getFrameDelegate() != null) {
                                TuSDKLiveVideoCamera.this.getFrameDelegate().onFrameDataAvailable(TuSDKLiveVideoCamera.this.b.array());
                            }
                        }
                    });
                }
                return true;
            }
        };
    }
    
    public TuSDKLiveVideoCamera(final Context context, final TuSDKVideoCaptureSetting tuSDKVideoCaptureSetting, final RelativeLayout relativeLayout) {
        super(context, tuSDKVideoCaptureSetting, relativeLayout);
        this.d = (SelesOffscreen.SelesOffscreenDelegate)new SelesOffscreen.SelesOffscreenDelegate() {
            public boolean onFrameRendered(final SelesOffscreen selesOffscreen) {
                if (!TuSDKLiveVideoCamera.this.isRecording()) {
                    return false;
                }
                final IntBuffer renderBuffer = selesOffscreen.renderBuffer();
                if (renderBuffer == null) {
                    return true;
                }
                final IntBuffer intBuffer = renderBuffer;
                if (TuSDKLiveVideoCamera.this.getVideoCaptureSetting().videoAVCodecType == TuSDKVideoCaptureSetting.AVCodecType.HW_CODEC) {
                    return true;
                }
                if (TuSDKLiveVideoCamera.this.b == null) {
                    final TuSdkSize videoSize = TuSDKLiveVideoCamera.this.getVideoCaptureSetting().videoSize;
                    TuSDKLiveVideoCamera.this.b = ByteBuffer.allocate(videoSize.width * videoSize.height * ImageFormat.getBitsPerPixel(17) / 8);
                }
                TuSDKLiveVideoCamera.this.b.position(0);
                if (TuSDKLiveVideoCamera.this.getVideoCaptureSetting().videoAVCodecType == TuSDKVideoCaptureSetting.AVCodecType.SW_CODEC) {
                    TuSDKLiveVideoCamera.this.a(intBuffer, TuSDKLiveVideoCamera.this.b.array(), ColorFormatType.NV21);
                    final TuSDKSoftVideoDataEncoderInterface tuSDKSoftVideoDataEncoderInterface = (TuSDKSoftVideoDataEncoderInterface)TuSDKLiveVideoCamera.this.getVideoDataEncoder();
                    if (tuSDKSoftVideoDataEncoderInterface != null) {
                        tuSDKSoftVideoDataEncoderInterface.queueVideo(TuSDKLiveVideoCamera.this.b.array());
                    }
                }
                else {
                    TuSDKLiveVideoCamera.this.a(intBuffer, TuSDKLiveVideoCamera.this.b.array(), TuSDKLiveVideoCamera.this.getVideoCaptureSetting().imageFormatType);
                    ThreadHelper.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            if (TuSDKLiveVideoCamera.this.getFrameDelegate() != null) {
                                TuSDKLiveVideoCamera.this.getFrameDelegate().onFrameDataAvailable(TuSDKLiveVideoCamera.this.b.array());
                            }
                        }
                    });
                }
                return true;
            }
        };
    }
    
    public TuSDKLiveVideoCamera(final Context context, final TuSDKVideoCaptureSetting tuSDKVideoCaptureSetting, final RelativeLayout relativeLayout, final Boolean b, final Boolean b2) {
        super(context, tuSDKVideoCaptureSetting, relativeLayout, b, b2);
        this.d = (SelesOffscreen.SelesOffscreenDelegate)new SelesOffscreen.SelesOffscreenDelegate() {
            public boolean onFrameRendered(final SelesOffscreen selesOffscreen) {
                if (!TuSDKLiveVideoCamera.this.isRecording()) {
                    return false;
                }
                final IntBuffer renderBuffer = selesOffscreen.renderBuffer();
                if (renderBuffer == null) {
                    return true;
                }
                final IntBuffer intBuffer = renderBuffer;
                if (TuSDKLiveVideoCamera.this.getVideoCaptureSetting().videoAVCodecType == TuSDKVideoCaptureSetting.AVCodecType.HW_CODEC) {
                    return true;
                }
                if (TuSDKLiveVideoCamera.this.b == null) {
                    final TuSdkSize videoSize = TuSDKLiveVideoCamera.this.getVideoCaptureSetting().videoSize;
                    TuSDKLiveVideoCamera.this.b = ByteBuffer.allocate(videoSize.width * videoSize.height * ImageFormat.getBitsPerPixel(17) / 8);
                }
                TuSDKLiveVideoCamera.this.b.position(0);
                if (TuSDKLiveVideoCamera.this.getVideoCaptureSetting().videoAVCodecType == TuSDKVideoCaptureSetting.AVCodecType.SW_CODEC) {
                    TuSDKLiveVideoCamera.this.a(intBuffer, TuSDKLiveVideoCamera.this.b.array(), ColorFormatType.NV21);
                    final TuSDKSoftVideoDataEncoderInterface tuSDKSoftVideoDataEncoderInterface = (TuSDKSoftVideoDataEncoderInterface)TuSDKLiveVideoCamera.this.getVideoDataEncoder();
                    if (tuSDKSoftVideoDataEncoderInterface != null) {
                        tuSDKSoftVideoDataEncoderInterface.queueVideo(TuSDKLiveVideoCamera.this.b.array());
                    }
                }
                else {
                    TuSDKLiveVideoCamera.this.a(intBuffer, TuSDKLiveVideoCamera.this.b.array(), TuSDKLiveVideoCamera.this.getVideoCaptureSetting().imageFormatType);
                    ThreadHelper.post((Runnable)new Runnable() {
                        @Override
                        public void run() {
                            if (TuSDKLiveVideoCamera.this.getFrameDelegate() != null) {
                                TuSDKLiveVideoCamera.this.getFrameDelegate().onFrameDataAvailable(TuSDKLiveVideoCamera.this.b.array());
                            }
                        }
                    });
                }
                return true;
            }
        };
    }
    
    public TuSDKLiveVideoCameraDelegate getFrameDelegate() {
        return this.c;
    }
    
    public void setFrameDelegate(final TuSDKLiveVideoCameraDelegate c) {
        this.c = c;
    }
    
    @Override
    protected void initCamera() {
        super.initCamera();
        this.setEnableFaceTrace(true);
        this.setEnableLiveSticker(false);
    }
    
    @Override
    protected void onCameraStarted() {
        super.onCameraStarted();
        StatisticsManger.appendComponent(9437184L);
    }
    
    @Override
    public void initOutputSettings() {
        super.initOutputSettings();
        this.setFocusTouchView(new TuVideoFocusTouchView(this.getContext()));
        this.setEnableAudioCapture(true);
    }
    
    @Override
    protected void updateOutputFilterSettings() {
        super.updateOutputFilterSettings();
        final boolean b = this.isDisableMirrorFrontFacing() && this.isFrontFacingCameraPresent() && this.isHorizontallyMirrorFrontFacingCamera();
        if (this.getVideoCaptureSetting().videoAVCodecType != TuSDKVideoCaptureSetting.AVCodecType.HW_CODEC) {
            if (this.a != null) {
                this.a.setEnableHorizontallyFlip(b);
            }
        }
        else {
            final SelesSurfaceEncoderInterface selesSurfaceEncoderInterface = (SelesSurfaceEncoderInterface)this.getVideoDataEncoder();
            if (selesSurfaceEncoderInterface != null) {
                selesSurfaceEncoderInterface.setEnableHorizontallyFlip(b);
            }
        }
    }
    
    private boolean a() {
        if (!SdkValid.shared.videoStreamEnabled()) {
            TLog.e("The video streaming feature has been expired, please contact us via http://tusdk.com", new Object[0]);
            return false;
        }
        return true;
    }
    
    @Override
    public void switchFilter(final String s) {
        if (s == null || this.isFilterChanging() || this.mFilterWrap.equalsCode(s)) {
            return;
        }
        if (!FilterManager.shared().isFilterEffect(s) || !this.a()) {
            return;
        }
        final int groupTypeByCode = FilterManager.shared().getGroupTypeByCode(s);
        if (!FilterManager.shared().isNormalFilter(s) && groupTypeByCode != 1) {
            TLog.d("Only live video filter [%s] could be used here, please visit http://tusdk.com.", new Object[] { s });
            return;
        }
        super.switchFilter(s);
    }

    @Override
    public void updateFaceFeatures(FaceAligment[] p0, float p1) {

    }

    @Override
    protected void applyFilterWrap(final FilterWrap filterWrap) {
        super.applyFilterWrap(filterWrap);
        if (this.getVideoCaptureSetting().videoAVCodecType != TuSDKVideoCaptureSetting.AVCodecType.HW_CODEC && this.a != null) {
            filterWrap.getFilter().addTarget((SelesContext.SelesInput)this.a);
        }
    }
    
    @Override
    public void stopCameraCapture() {
        super.stopCameraCapture();
        StatisticsManger.appendComponent(9437185L);
    }

    @Override
    public void setOutputSize(TuSdkSize p0) {

    }

    @Override
    public void autoFocus(CameraConfigs.CameraAutoFocus p0, PointF p1, TuSdkAutoFocusCallback p2) {

    }

    @Override
    public void autoFocus(TuSdkAutoFocusCallback p0) {

    }

    @Override
    protected void startVideoDataEncoder() {
        super.startVideoDataEncoder();
        if (this.getVideoCaptureSetting().videoAVCodecType != TuSDKVideoCaptureSetting.AVCodecType.HW_CODEC) {
            this.c();
            this.b().startWork();
            this.updateOutputFilter();
        }
    }
    
    @Override
    protected void stopVideoDataEncoder() {
        super.stopVideoDataEncoder();
        if (this.getVideoCaptureSetting().videoAVCodecType != TuSDKVideoCaptureSetting.AVCodecType.HW_CODEC) {
            if (this.a == null) {
                return;
            }
            this.runOnDraw((Runnable)new Runnable() {
                @Override
                public void run() {
                    TuSDKLiveVideoCamera.this.c();
                }
            });
        }
    }
    
    private SelesOffscreen b() {
        if (this.a == null) {
            (this.a = new SelesOffscreen()).setDelegate(this.d);
            this.a.setOutputSize(this.getVideoCaptureSetting().videoSize);
        }
        return this.a;
    }
    
    private void c() {
        if (this.a != null) {
            this.a.setEnabled(false);
            this.a.destroy();
            this.a.setDelegate((SelesOffscreen.SelesOffscreenDelegate)null);
            this.a = null;
        }
    }
    
    @Override
    public void startRecording() {
        if (this.isRecording()) {
            return;
        }
        if (this.getVideoCaptureSetting().videoAVCodecType != TuSDKVideoCaptureSetting.AVCodecType.HW_CODEC) {
            this.b().startWork();
            this.updateOutputFilter();
        }
        super.startRecording();
    }
    
    @Override
    public void stopRecording() {
        if (this.getVideoCaptureSetting().videoAVCodecType != TuSDKVideoCaptureSetting.AVCodecType.HW_CODEC && this.a != null) {
            this.runOnDraw((Runnable)new Runnable() {
                @Override
                public void run() {
                    TuSDKLiveVideoCamera.this.a.setEnabled(false);
                }
            });
        }
        super.stopRecording();
    }
    
    private Boolean a(final IntBuffer intBuffer, final byte[] array, final ColorFormatType colorFormatType) {
        if (intBuffer == null || array == null) {
            return false;
        }
        final TuSdkSize videoSize = this.getVideoCaptureSetting().videoSize;
        final int i = videoSize.width * videoSize.height * ImageFormat.getBitsPerPixel(17) / 8;
        if (i != array.length) {
            TLog.e("bytes size not equal: %d, %d", new Object[] { i, array.length });
            return false;
        }
        if (colorFormatType == ColorFormatType.NV21) {
            ColorSpaceConvert.rgbaToNv21(intBuffer.array(), videoSize.width, videoSize.height, array);
        }
        else if (colorFormatType == ColorFormatType.YV12) {
            ColorSpaceConvert.rgbaToYv12(intBuffer.array(), videoSize.width, videoSize.height, array);
        }
        else if (colorFormatType == ColorFormatType.I420) {
            ColorSpaceConvert.rgbaToI420(intBuffer.array(), videoSize.width, videoSize.height, array);
        }
        return true;
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.c();
        if (this.b != null) {
            this.b.clear();
            this.b = null;
        }
    }
    
    public interface TuSDKLiveVideoCameraDelegate
    {
        void onFrameDataAvailable(final byte[] p0);
    }
}
