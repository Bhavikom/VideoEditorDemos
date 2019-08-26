// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.audio;

//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import android.media.MediaCodec;
import java.nio.ByteBuffer;
import android.media.MediaFormat;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.AlbumHelper;

import java.io.IOException;
//import org.lasque.tusdk.core.utils.TLog;
import java.io.FileNotFoundException;
import java.io.File;
//import org.lasque.tusdk.core.utils.StringHelper;
//import org.lasque.tusdk.core.utils.image.AlbumHelper;
import java.io.FileOutputStream;

public class TuSDKAACAudioFileEncoder extends TuSDKAudioDataEncoder
{
    private String a;
    private FileOutputStream b;
    
    public void setDelegate(final TuSDKAACAudioFileEncoderDelegate delegate) {
        super.setDelegate(delegate);
    }
    
    @Override
    public TuSDKAACAudioFileEncoderDelegate getDelegate() {
        return (TuSDKAACAudioFileEncoderDelegate)super.getDelegate();
    }
    
    @Override
    public boolean initEncoder(final TuSDKAudioEncoderSetting tuSDKAudioEncoderSetting) {
        tuSDKAudioEncoderSetting.enableBuffers = false;
        return super.initEncoder(tuSDKAudioEncoderSetting);
    }
    
    public TuSDKAACAudioFileEncoder setOutputFilePath(final String a) {
        this.a = a;
        return this;
    }
    
    public String getOutputFilePath() {
        if (this.a == null) {
            this.a = new File(AlbumHelper.getAblumPath(), String.format("lsq_%s.aac", StringHelper.timeStampString())).getPath();
        }
        return this.a;
    }
    
    protected FileOutputStream getMovieWirter() {
        if (this.b != null) {
            return this.b;
        }
        try {
            this.b = new FileOutputStream(this.getOutputFilePath());
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return this.b;
    }
    
    @Override
    protected void onStopeed() {
        TLog.d("onStopeed====", new Object[0]);
        super.onStopeed();
        try {
            if (this.getMovieWirter() != null) {
                this.getMovieWirter().close();
            }
            this.b = null;
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        if (this.getDelegate() != null) {
            this.getDelegate().onAACAudioFileEncoderComplete(this.a);
        }
    }
    
    @Override
    public final void onAudioEncoderStarted(final MediaFormat mediaFormat) {
        super.onAudioEncoderStarted(mediaFormat);
    }
    
    @Override
    public final void onAudioEncoderFrameDataAvailable(final long n, final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
        super.onAudioEncoderFrameDataAvailable(n, byteBuffer, bufferInfo);
        byteBuffer.position(bufferInfo.offset);
        byteBuffer.limit(bufferInfo.offset + bufferInfo.size);
        final int size = bufferInfo.size;
        final int n2 = size + 7;
        byteBuffer.position(bufferInfo.offset);
        byteBuffer.limit(bufferInfo.offset + size);
        final byte[] array = new byte[size + 7];
        TuSdkMediaUtils.addADTStoPacket(array, n2, this.getAudioFormat());
        byteBuffer.get(array, 7, size);
        try {
            this.getMovieWirter().write(array, 0, array.length);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public interface TuSDKAACAudioFileEncoderDelegate extends TuSDKAudioDataEncoderInterface.TuSDKAudioDataEncoderDelegate
    {
        void onAACAudioFileEncoderComplete(final String p0);
    }
}
