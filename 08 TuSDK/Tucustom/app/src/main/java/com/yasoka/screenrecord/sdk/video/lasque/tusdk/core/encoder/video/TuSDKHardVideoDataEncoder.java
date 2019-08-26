// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.video;

import java.io.IOException;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.utils.ThreadHelper;
import android.os.Bundle;
import android.media.MediaCodecInfo;
import android.media.MediaCrypto;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.common.TuSDKMediaUtils;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.os.Build;
import android.media.MediaFormat;
//import org.lasque.tusdk.core.utils.TuSdkDeviceInfo;
//import org.lasque.tusdk.core.encoder.TuSDKVideoDataEncoderDelegate;
import android.media.MediaCodec;
import android.view.Surface;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TuSdkDeviceInfo;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common.TuSDKMediaUtils;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.TuSDKVideoDataEncoderDelegate;

@TargetApi(18)
@SuppressLint({ "InlinedApi" })
public class TuSDKHardVideoDataEncoder implements TuSDKHardVideoDataEncoderInterface
{
    private final int a = 500;
    private Surface b;
    private MediaCodec c;
    private TuSDKVideoDataEncoderDelegate d;
    private TuSDKVideoEncoderSetting e;
    private long f;
    private long g;
    private int h;
    private int i;
    private boolean j;
    
    public TuSDKHardVideoDataEncoder() {
        this.f = 0L;
    }
    
    @Override
    public void setDelegate(final TuSDKVideoDataEncoderDelegate d) {
        this.d = d;
    }
    
    public TuSDKVideoDataEncoderDelegate getDelegate() {
        return this.d;
    }
    
    public void setDefaultVideoQuality(final int h, final int i) {
        this.h = h;
        this.i = i;
    }
    
    public TuSDKVideoEncoderSetting getVideoEncoderSetting() {
        if (this.e == null) {
            this.e = new TuSDKVideoEncoderSetting();
        }
        return this.e;
    }
    
    private MediaCodec a(final String s) {
        final String model = TuSdkDeviceInfo.getModel();
        final String vender = TuSdkDeviceInfo.getVender();
        if (("OPPO".equalsIgnoreCase(vender) && model.equalsIgnoreCase("PADM00")) || ("HUAWEI".equalsIgnoreCase(vender) && !model.equalsIgnoreCase("PLK-TL01H") && !model.equalsIgnoreCase("HUAWEI NXT-AL10"))) {
            try {
                return MediaCodec.createByCodecName("OMX.google.h264.encoder");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        try {
            return MediaCodec.createEncoderByType(s);
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return null;
    }
    
    private MediaFormat a(final TuSDKVideoEncoderSetting tuSDKVideoEncoderSetting) {
        TuSdkDeviceInfo.getModel();
        final String vender = TuSdkDeviceInfo.getVender();
        final int n = (tuSDKVideoEncoderSetting.videoSize.width % 2 != 0) ? (tuSDKVideoEncoderSetting.videoSize.width + 1) : tuSDKVideoEncoderSetting.videoSize.width;
        final int n2 = (tuSDKVideoEncoderSetting.videoSize.height % 2 != 0) ? (tuSDKVideoEncoderSetting.videoSize.height + 1) : tuSDKVideoEncoderSetting.videoSize.height;
        if (Build.VERSION.SDK_INT >= 21 && !TuSDKMediaUtils.isVideoSizeSupported(TuSdkSize.create(n, n2), "video/avc")) {
            TLog.w("TuSDKHardVideoDataEncoder | May not support video size for " + this.e.videoSize, new Object[0]);
        }
        final MediaFormat videoFormat = MediaFormat.createVideoFormat("video/avc", n, n2);
        videoFormat.setInteger("color-format", 2130708361);
        videoFormat.setInteger("bitrate", (this.i > 0) ? this.i : tuSDKVideoEncoderSetting.videoQuality.getBitrate());
        videoFormat.setInteger("frame-rate", (this.h > 0) ? this.h : tuSDKVideoEncoderSetting.videoQuality.getFps());
        videoFormat.setInteger("profile", 1);
        videoFormat.setInteger("level", 512);
        videoFormat.setInteger("i-frame-interval", tuSDKVideoEncoderSetting.mediacodecAVCIFrameInterval);
        videoFormat.setInteger("bitrate-mode", tuSDKVideoEncoderSetting.bitrateMode);
        if (tuSDKVideoEncoderSetting.enableAllKeyFrame) {
            if ("OPPO".equalsIgnoreCase(vender)) {
                videoFormat.setInteger("i-frame-interval", 0);
            }
            else if ("XiaoMi".equalsIgnoreCase(vender)) {
                videoFormat.setInteger("i-frame-interval", 0);
            }
        }
        return videoFormat;
    }
    
    @SuppressLint({ "InlinedApi" })
    @Override
    public boolean initCodec(final TuSDKVideoEncoderSetting e) {
        if (this.c != null) {
            return false;
        }
        this.e = e;
        final MediaCodecInfo encoderCodecInfo = TuSDKMediaUtils.getEncoderCodecInfo("video/avc");
        if (encoderCodecInfo == null) {
            TLog.e("Unable to find an appropriate codec for video/avc", new Object[0]);
            return false;
        }
        TLog.d("choose codec [" + encoderCodecInfo.getName() + "] for " + "video/avc", new Object[0]);
        try {
            (this.c = this.a("video/avc")).configure(this.a(this.getVideoEncoderSetting()), null, null, 1);
            this.b = this.c.createInputSurface();
            this.c.start();
            this.j = false;
        }
        catch (Throwable t) {
            TLog.e("fail to create MediaCodec with name: %s", new Object[] { encoderCodecInfo.getName() });
            return false;
        }
        return true;
    }
    
    @Override
    public Surface getInputSurface() {
        return this.b;
    }
    
    @Override
    public void release() {
        if (this.c != null) {
            this.c.stop();
            this.c.release();
            this.c = null;
        }
    }
    
    @Override
    public void flush() {
        final String model = TuSdkDeviceInfo.getModel();
        if (model.equalsIgnoreCase("PADM00") || model.equalsIgnoreCase("MI 6")) {
            return;
        }
        if (this.c != null) {
            this.c.flush();
        }
    }
    
    private boolean a() {
        return this.c != null && Build.VERSION.SDK_INT > 19;
    }
    
    @Override
    public boolean requestKeyFrame() {
        if (!this.a()) {
            return false;
        }
        final Bundle parameters = new Bundle();
        parameters.putInt("request-sync", 0);
        this.c.setParameters(parameters);
        return true;
    }
    
    @Override
    public void drainEncoder(final boolean b) {
        if (this.c == null) {
            TLog.e("Unable to start the encoder", new Object[0]);
            return;
        }
        if (this.e.enableAllKeyFrame) {
            this.requestKeyFrame();
        }
        if (b) {
            TLog.d("sending EOS to encoder", new Object[0]);
            if (this.j) {
                this.c.signalEndOfInputStream();
            }
            else {
                ThreadHelper.postDelayed((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        TuSDKHardVideoDataEncoder.this.c.signalEndOfInputStream();
                    }
                }, 600L);
            }
        }
        while (true) {
            final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
            final int dequeueOutputBuffer = this.c.dequeueOutputBuffer(bufferInfo, 500L);
            if (dequeueOutputBuffer == -1) {
                if (!b) {
                    break;
                }
                continue;
            }
            else {
                if (dequeueOutputBuffer == -3) {
                    continue;
                }
                if (dequeueOutputBuffer == -2) {
                    this.onEncoderStarted(this.c.getOutputFormat());
                }
                else {
                    if (dequeueOutputBuffer < 0) {
                        continue;
                    }
                    final ByteBuffer byteBuffer = this.c.getOutputBuffers()[dequeueOutputBuffer];
                    if (byteBuffer == null) {
                        throw new RuntimeException("encoderOutputBuffer " + dequeueOutputBuffer + " was null");
                    }
                    if (bufferInfo.size > 0) {
                        byteBuffer.position(bufferInfo.offset);
                        byteBuffer.limit(bufferInfo.offset + bufferInfo.size);
                        if ((bufferInfo.flags & 0x2) != 0x0) {
                            this.onVideoEncoderCodecConfig(this.a(bufferInfo), byteBuffer, bufferInfo);
                        }
                        else if (this.e.enableAllKeyFrame) {
                            if ((bufferInfo.flags & 0x1) != 0x0) {
                                this.onVideoEncoderFrameDataAvailable(this.a(bufferInfo), byteBuffer, bufferInfo);
                            }
                        }
                        else {
                            this.onVideoEncoderFrameDataAvailable(this.a(bufferInfo), byteBuffer, bufferInfo);
                        }
                    }
                    this.c.releaseOutputBuffer(dequeueOutputBuffer, false);
                    this.j = true;
                    if ((bufferInfo.flags & 0x4) == 0x0) {
                        continue;
                    }
                    if (!b) {
                        TLog.w("reached end of stream unexpectedly", new Object[0]);
                        break;
                    }
                    TLog.d("end of stream reached", new Object[0]);
                    break;
                }
            }
        }
    }
    
    private long a(final MediaCodec.BufferInfo bufferInfo) {
        if (bufferInfo == null) {
            return 0L;
        }
        if (this.f == 0L) {
            this.f = bufferInfo.presentationTimeUs / 1000L;
        }
        return bufferInfo.presentationTimeUs / 1000L - this.f;
    }
    
    protected void onEncoderStarted(final MediaFormat mediaFormat) {
        if (this.getDelegate() != null) {
            this.getDelegate().onVideoEncoderStarted(mediaFormat);
        }
    }
    
    protected void onVideoEncoderCodecConfig(final long n, final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
        if (this.getDelegate() != null) {
            this.getDelegate().onVideoEncoderCodecConfig(n, byteBuffer, bufferInfo);
        }
    }
    
    protected void onVideoEncoderFrameDataAvailable(final long n, final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
        if (this.g <= 0L) {
            this.g = bufferInfo.presentationTimeUs;
        }
        else {
            if (this.g > bufferInfo.presentationTimeUs) {
                bufferInfo.presentationTimeUs = this.g - bufferInfo.presentationTimeUs + this.g + 1L;
            }
            this.g = bufferInfo.presentationTimeUs;
        }
        if (this.getDelegate() != null) {
            this.getDelegate().onVideoEncoderFrameDataAvailable(n, byteBuffer, bufferInfo);
        }
    }
}
