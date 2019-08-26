// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.encoder;

//import org.lasque.tusdk.core.utils.FileHelper;
//import org.lasque.tusdk.core.TuSdk;
import android.media.MediaFormat;
//import org.lasque.tusdk.core.utils.StringHelper;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkCodecCapabilities;
//import org.lasque.tusdk.core.utils.TLog;
import android.media.MediaCodec;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdk;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkMediaProgress;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkCodecCapabilities;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.FileHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
//import org.lasque.tusdk.core.api.extend.TuSdkSurfaceRender;
import java.io.File;
//import org.lasque.tusdk.core.api.extend.TuSdkMediaProgress;

public abstract class TuSdkMediaEncoderBase
{
    public static final int TRANS_STATE_UNINITIALIZED = -1;
    public static final int TRANS_STATE_STARTED = 0;
    public static final int TRANS_STATE_STOPPED = 1;
    protected int mState;
    protected String mOutputFilePath;
    protected TuSdkMediaProgress mProgress;
    protected File mTempFile;
    protected TuSdkSurfaceRender mSurfaceRender;
    protected TuSdkAudioRender mAudioRender;
    protected TuSdkSize mOutputSize;
    protected TuSdkVideoSurfaceEncoder mVideoEncoder;
    protected TuSdkAudioEncoder mAudioEncoder;
    protected TuSdkMediaFileMuxer mMediaMuxer;
    protected final TuSdkVideoSurfaceEncoderListener mVideoEncoderListener;
    protected final TuSdkEncoderListener mAudioEncoderListener;
    
    public TuSdkMediaEncoderBase() {
        this.mState = -1;
        this.mVideoEncoderListener = new TuSdkVideoSurfaceEncoderListenerImpl() {
            @Override
            public void onSurfaceCreated(final GL10 gl10, final EGLConfig eglConfig) {
                TuSdkMediaEncoderBase.this._onSurfaceCreated(gl10, eglConfig);
            }
            
            @Override
            public void onEncoderDrawFrame(final long n, final boolean b) {
                TuSdkMediaEncoderBase.this._onEncoderDrawFrame(n, b);
            }
            
            @Override
            public void onEncoderUpdated(final MediaCodec.BufferInfo bufferInfo) {
                TuSdkMediaEncoderBase.this._notifyProgress(false);
            }
            
            @Override
            public void onEncoderCompleted(final Exception ex) {
                if (ex == null) {
                    TLog.d("%s encodec Video updatedToEOS", "TuSdkMediaEncoderBase");
                    TuSdkMediaEncoderBase.this._notifyProgress(true);
                }
                else {
                    TLog.e(ex, "%s VideoEncoderListener thread catch exception, The thread will exit.", "TuSdkMediaEncoderBase");
                }
                TuSdkMediaEncoderBase.this._notifyCompleted(ex);
            }
        };
        this.mAudioEncoderListener = new TuSdkEncoderListener() {
            @Override
            public void onEncoderUpdated(final MediaCodec.BufferInfo bufferInfo) {
                TuSdkCodecCapabilities.logBufferInfo("AudioEncoderListener updated", bufferInfo);
            }
            
            @Override
            public void onEncoderCompleted(final Exception ex) {
                if (ex == null) {
                    TLog.d("%s encodec Audio updatedToEOS", "TuSdkMediaEncoderBase");
                    TuSdkMediaEncoderBase.this._notifyProgress(true);
                }
                else {
                    TLog.e(ex, "%s AudioEncoderListener thread catch exception, The thread will exit.", "TuSdkMediaEncoderBase");
                }
                TuSdkMediaEncoderBase.this._notifyCompleted(ex);
            }
        };
    }
    
    public void setOutputFilePath(final String mOutputFilePath) {
        if (this.mState != -1) {
            TLog.w("%s setOutputFilePath need before run.", "TuSdkMediaEncoderBase");
            return;
        }
        if (StringHelper.isEmpty(mOutputFilePath)) {
            TLog.w("%s setOutputFilePath need a valid file path: %s", "TuSdkMediaEncoderBase", mOutputFilePath);
            return;
        }
        this.mOutputFilePath = mOutputFilePath;
    }
    
    public int setOutputVideoFormat(final MediaFormat outputFormat) {
        if (this.mState != -1) {
            TLog.w("%s setOutputVideoFormat need before run.", "TuSdkMediaEncoderBase");
            return -1;
        }
        this.mVideoEncoder = new TuSdkVideoSurfaceEncoder();
        final int setOutputFormat = this.mVideoEncoder.setOutputFormat(outputFormat);
        if (setOutputFormat != 0) {
            this.mVideoEncoder = null;
            TLog.w("%s setOutputVideoFormat has a error code: %d, %s", "TuSdkMediaEncoderBase", setOutputFormat, outputFormat);
            return setOutputFormat;
        }
        this.mVideoEncoder.setSurfaceRender(this.mSurfaceRender);
        this.mVideoEncoder.setListener(this.mVideoEncoderListener);
        return 0;
    }
    
    public int setOutputAudioFormat(final MediaFormat outputFormat) {
        if (this.mState != -1) {
            TLog.w("%s setOutputAudioFormat need before run.", "TuSdkMediaEncoderBase");
            return -1;
        }
        this.mAudioEncoder = new TuSdkAudioEncoder();
        final int setOutputFormat = this.mAudioEncoder.setOutputFormat(outputFormat);
        if (setOutputFormat != 0) {
            this.mAudioEncoder = null;
            TLog.w("%s setOutputAudioFormat has a error code: %d, %s", "TuSdkMediaEncoderBase", setOutputFormat, outputFormat);
            return setOutputFormat;
        }
        this.mAudioEncoder.setAudioRender(this.mAudioRender);
        this.mAudioEncoder.setListener(this.mAudioEncoderListener);
        return 0;
    }
    
    public void setSurfaceRender(final TuSdkSurfaceRender tuSdkSurfaceRender) {
        this.mSurfaceRender = tuSdkSurfaceRender;
        if (this.mVideoEncoder != null) {
            this.mVideoEncoder.setSurfaceRender(tuSdkSurfaceRender);
        }
    }
    
    public void setAudioRender(final TuSdkAudioRender tuSdkAudioRender) {
        this.mAudioRender = tuSdkAudioRender;
        if (this.mAudioEncoder != null) {
            this.mAudioEncoder.setAudioRender(tuSdkAudioRender);
        }
    }
    
    protected abstract boolean _init();
    
    protected abstract void _notifyProgress(final boolean p0);
    
    protected abstract void _notifyCompleted(final Exception p0);
    
    protected abstract void _onSurfaceCreated(final GL10 p0, final EGLConfig p1);
    
    protected abstract void _onEncoderDrawFrame(final long p0, final boolean p1);
    
    public boolean run(final TuSdkMediaProgress mProgress) {
        if (this.mState != -1) {
            TLog.w("%s run can not after initialized.", "TuSdkMediaEncoderBase");
            return false;
        }
        if (this.mOutputFilePath == null) {
            TLog.w("%s run need a output file path.", "TuSdkMediaEncoderBase");
            return false;
        }
        if (this.mVideoEncoder == null) {
            TLog.w("%s run need set Output Video Format.", "TuSdkMediaEncoderBase");
            return false;
        }
        if (this.mAudioEncoder == null) {
            TLog.w("%s run can not find Output Audio Format, then ignore audio.", "TuSdkMediaEncoderBase");
        }
        this.mTempFile = new File(TuSdk.getAppTempPath(), String.format("lsq_media_tmp_%d.tmp", System.currentTimeMillis()));
        this.mProgress = mProgress;
        this.mState = 0;
        return this._init();
    }
    
    public void stop() {
        if (this.mState == 1) {
            TLog.w("%s already stoped.", "TuSdkMediaEncoderBase");
            return;
        }
        this.mState = 1;
        if (this.mVideoEncoder != null) {
            this.mVideoEncoder.release();
            this.mVideoEncoder = null;
        }
        if (this.mAudioEncoder != null) {
            this.mAudioEncoder.release();
            this.mAudioEncoder = null;
        }
        if (this.mMediaMuxer != null) {
            this.mMediaMuxer.release();
            this.mMediaMuxer = null;
        }
        FileHelper.delete(this.mTempFile);
    }
    
    @Override
    protected void finalize() {
        this.stop();
        try {
            super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
}
