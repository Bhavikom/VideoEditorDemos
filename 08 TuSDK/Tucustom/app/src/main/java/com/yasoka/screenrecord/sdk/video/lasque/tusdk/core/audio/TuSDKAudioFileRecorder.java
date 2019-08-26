// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.audio;

//import org.lasque.tusdk.core.utils.StringHelper;
//import org.lasque.tusdk.core.utils.image.AlbumHelper;
//import org.lasque.tusdk.video.editor.TuSDKMovieWriterInterface;
import java.io.IOException;
import java.io.FileNotFoundException;
//import org.lasque.tusdk.core.utils.ThreadHelper;
//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.core.utils.TLog;
import android.annotation.TargetApi;
import android.media.MediaCodec;
import java.nio.ByteBuffer;
import android.media.MediaFormat;
//import org.lasque.tusdk.core.encoder.audio.TuSDKAudioEncoderSetting;
//import org.lasque.tusdk.core.encoder.audio.TuSDKAudioDataEncoderInterface;
//import org.lasque.tusdk.video.editor.TuSDKMovieWriter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.AlbumHelper;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.audio.TuSDKAudioDataEncoderInterface;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.encoder.audio.TuSDKAudioEncoderSetting;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSDKMovieWriter;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSDKMovieWriterInterface;

import java.io.FileOutputStream;
import java.io.File;

public class TuSDKAudioFileRecorder extends TuSDKAudioRecorderCore
{
    private OutputFormat a;
    private RecordState b;
    private File c;
    private FileOutputStream d;
    private TuSDKMovieWriter e;
    private TuSDKRecordAudioDelegate f;
    private boolean g;
    private boolean h;
    private TuSDKAudioDataEncoderInterface.TuSDKAudioDataEncoderDelegate i;
    
    public TuSDKAudioFileRecorder() {
        this(TuSDKAudioCaptureSetting.defaultCaptureSetting(), TuSDKAudioEncoderSetting.defaultEncoderSetting());
    }
    
    public TuSDKAudioFileRecorder(final TuSDKAudioCaptureSetting tuSDKAudioCaptureSetting, final TuSDKAudioEncoderSetting tuSDKAudioEncoderSetting) {
        super(tuSDKAudioCaptureSetting, tuSDKAudioEncoderSetting);
        this.a = OutputFormat.AAC;
        this.g = false;
        this.h = false;
        this.i = new TuSDKAudioDataEncoderInterface.TuSDKAudioDataEncoderDelegate() {
            @Override
            public void onAudioEncoderStarted(final MediaFormat mediaFormat) {
                if (!TuSDKAudioFileRecorder.this.isRecording() || TuSDKAudioFileRecorder.this.getMovieWriter() == null || !TuSDKAudioFileRecorder.this.getMovieWriter().canAddAudioTrack()) {
                    return;
                }
                TuSDKAudioFileRecorder.this.getMovieWriter().addAudioTrack(mediaFormat);
                TuSDKAudioFileRecorder.this.getMovieWriter().start();
            }
            
            @Override
            public void onAudioEncoderStoped() {
            }
            
            @Override
            public void onAudioEncoderFrameDataAvailable(final long n, final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
                TuSDKAudioFileRecorder.this.a(byteBuffer, bufferInfo);
            }
            
            @Override
            public void onAudioEncoderCodecConfig(final long n, final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
            }
        };
        this.getAudioEncoder().setDelegate(this.i);
    }
    
    public void enableSplicing(final boolean g) {
        this.g = g;
    }
    
    @TargetApi(16)
    @Override
    protected void onRawAudioFrameDataAvailable(final byte[] array) {
        if (this.b != RecordState.Recording) {
            return;
        }
        if (this.a == OutputFormat.PCM) {
            this.a(array);
        }
        else {
            super.onRawAudioFrameDataAvailable(array);
        }
    }
    
    public void start() {
        if (this.isRecording() && this.b == RecordState.Recording) {
            return;
        }
        this.initWriter();
        super.startRecording();
        if (!this.isPrepared()) {
            this.notifyRecordingError(RecordError.InitializationFailed);
            return;
        }
        this.notifyRecordingState(RecordState.Recording);
        TLog.d("Recording start", new Object[0]);
    }
    
    public void pauseRecord() {
        this.h = true;
    }
    
    public void resumeRecord() {
        this.h = false;
    }
    
    public boolean isPauseRecord() {
        return this.h;
    }
    
    public void stop() {
        if (!this.isRecording() || this.b != RecordState.Recording) {
            return;
        }
        super.stopRecording();
        this.stopWriter();
        this.notifyRecordingState(RecordState.Stoped);
        TLog.d("Recording stoped", new Object[0]);
        StatisticsManger.appendComponent(9449476L);
    }
    
    protected void notifyRecordingError(final RecordError recordError) {
        if (this.f == null) {
            return;
        }
        ThreadHelper.post((Runnable)new Runnable() {
            @Override
            public void run() {
                TuSDKAudioFileRecorder.this.f.onAudioRecordError(recordError);
            }
        });
    }
    
    protected void notifyRecordingState(final RecordState b) {
        this.b = b;
        if (this.f == null) {
            return;
        }
        ThreadHelper.post((Runnable)new Runnable() {
            @Override
            public void run() {
                TuSDKAudioFileRecorder.this.f.onAudioRecordStateChanged(b);
            }
        });
    }
    
    public void setAudioRecordDelegate(final TuSDKRecordAudioDelegate f) {
        this.f = f;
    }
    
    protected void initWriter() {
        if (this.a == OutputFormat.PCM) {
            this.a();
        }
        if (this.a == OutputFormat.AAC) {
            this.c();
        }
    }
    
    protected void stopWriter() {
        if (this.a == OutputFormat.PCM) {
            this.b();
        }
        if (this.a == OutputFormat.AAC) {
            this.d();
        }
        if (this.f != null) {
            this.f.onAudioRecordComplete(this.c);
        }
        if (!this.g) {
            this.c = null;
        }
    }
    
    private void a() {
        if (this.getOutputFile().exists() && !this.g) {
            this.getOutputFile().delete();
        }
        try {
            if (this.d == null) {
                this.d = new FileOutputStream(this.getOutputFile(), true);
            }
        }
        catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    
    private void a(final byte[] b) {
        if (this.d == null) {
            return;
        }
        try {
            this.d.write(b);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void b() {
        if (this.d == null) {
            return;
        }
        try {
            this.d.close();
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
        this.d = null;
    }
    
    private void c() {
        if (this.e == null) {
            if (this.getOutputFile().exists() && !this.g) {
                this.getOutputFile().delete();
            }
            this.e = TuSDKMovieWriter.create(this.getOutputFile().getPath(), TuSDKMovieWriterInterface.MovieWriterOutputFormat.MPEG_4);
        }
    }
    
    private void a(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
        if (this.isPauseRecord()) {
            return;
        }
        if (!this.isRecording() || this.getMovieWriter() == null || !this.getMovieWriter().isStarted() || !this.getMovieWriter().hasAudioTrack()) {
            return;
        }
        TLog.e("BufferInfoTime :%s", new Object[] { bufferInfo.presentationTimeUs });
        this.getMovieWriter().writeAudioSampleData(byteBuffer, bufferInfo);
    }
    
    private void d() {
        if (this.e == null) {
            return;
        }
        this.e.stop();
        if (this.g) {
            return;
        }
        this.e = null;
    }
    
    public void setOutputFormat(final OutputFormat a) {
        this.a = a;
    }
    
    public File getOutputFile() {
        if (this.c == null && this.a == OutputFormat.PCM) {
            this.c = new File(AlbumHelper.getAblumPath(), String.format("lsq_%s.pcm", StringHelper.timeStampString()));
        }
        if (this.c == null && this.a == OutputFormat.AAC) {
            this.c = new File(AlbumHelper.getAblumPath(), String.format("lsq_%s.aac", StringHelper.timeStampString()));
        }
        return this.c;
    }
    
    public void setOutputFile(final File c) {
        this.c = c;
    }
    
    protected TuSDKMovieWriter getMovieWriter() {
        return this.e;
    }
    
    public interface TuSDKRecordAudioDelegate
    {
        void onAudioRecordComplete(final File p0);
        
        void onAudioRecordStateChanged(final RecordState p0);
        
        void onAudioRecordError(final RecordError p0);
    }
    
    public enum RecordState
    {
        Recording, 
        Stoped;
    }
    
    public enum RecordError
    {
        InitializationFailed;
    }
    
    public enum OutputFormat
    {
        PCM, 
        AAC;
    }
}
