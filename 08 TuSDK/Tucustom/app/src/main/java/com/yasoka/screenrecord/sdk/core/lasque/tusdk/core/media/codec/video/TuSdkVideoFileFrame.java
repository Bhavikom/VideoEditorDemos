// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.video;

import java.util.Random;
//import org.lasque.tusdk.core.media.codec.decoder.TuSdkMediaFileExtractor;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.media.codec.exception.TuSdkNoMediaTrackException;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
//import org.lasque.tusdk.core.media.codec.TuSdkDecodecOperation;
//import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
//import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkDecodecOperation;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.decoder.TuSdkMediaFileExtractor;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.decoder.TuSdkMediaFrameInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.exception.TuSdkNoMediaTrackException;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.media.codec.decoder.TuSdkMediaFrameInfo;

@TargetApi(16)
public class TuSdkVideoFileFrame extends TuSdkMediaFrameInfo
{
    public static TuSdkVideoFileFrame sync(final String s) {
        final _TuSdkVideoFileFrame tuSdkVideoFileFrame = new _TuSdkVideoFileFrame();
        tuSdkVideoFileFrame.a(s);
        return tuSdkVideoFileFrame.b();
    }
    
    public static TuSdkVideoFileFrame sync(final TuSdkMediaDataSource tuSdkMediaDataSource) {
        final _TuSdkVideoFileFrame tuSdkVideoFileFrame = new _TuSdkVideoFileFrame();
        tuSdkVideoFileFrame.a(tuSdkMediaDataSource);
        return tuSdkVideoFileFrame.b();
    }
    
    public static void async(final String s, final AysncTest aysncTest) {
        final _TuSdkVideoFileFrame tuSdkVideoFileFrame = new _TuSdkVideoFileFrame();
        tuSdkVideoFileFrame.a(s);
        tuSdkVideoFileFrame.a(aysncTest);
    }
    
    public static TuSdkVideoFileFrame keyFrameInfo(final TuSdkMediaFileExtractor tuSdkMediaExtractor) {
        final _TuSdkVideoFileFrame tuSdkVideoFileFrame = new _TuSdkVideoFileFrame();
        tuSdkVideoFileFrame.a(tuSdkMediaExtractor);
        return tuSdkVideoFileFrame;
    }
    
    private static class _TuSdkVideoFileFrame extends TuSdkVideoFileFrame
    {
        private TuSdkMediaExtractor a;
        private TuSdkMediaDataSource b;
        private boolean c;
        private AysncTest d;
        private TuSdkDecodecOperation e;
        
        private void a(final String s) {
            this.a(new TuSdkMediaDataSource(s));
        }
        
        private void a(final TuSdkMediaDataSource b) {
            this.b = b;
        }
        
        private _TuSdkVideoFileFrame() {
            this.c = false;
            this.e = new TuSdkDecodecOperation() {
                private boolean b = false;
                
                @Override
                public void flush() {
                }
                
                @Override
                public boolean decodecInit(final TuSdkMediaExtractor tuSdkMediaExtractor) {
                    final int mediaTrackIndex = TuSdkMediaUtils.getMediaTrackIndex(tuSdkMediaExtractor, "video/");
                    if (mediaTrackIndex < 0) {
                        this.decodecException(new TuSdkNoMediaTrackException(String.format("%s decodecInit can not find media track: %s", "TuSdkVideoFileFrame", "video/")));
                        return false;
                    }
                    tuSdkMediaExtractor.selectTrack(mediaTrackIndex);
                    return _TuSdkVideoFileFrame.this.a(tuSdkMediaExtractor);
                }
                
                @Override
                public boolean decodecProcessUntilEnd(final TuSdkMediaExtractor tuSdkMediaExtractor) {
                    return true;
                }
                
                @Override
                public void decodecRelease() {
                    _TuSdkVideoFileFrame.this.a();
                }
                
                @Override
                public void decodecException(final Exception ex) {
                    if (this.b) {
                        return;
                    }
                    this.b = true;
                    _TuSdkVideoFileFrame.this.a();
                    if (_TuSdkVideoFileFrame.this.d != null) {
                        _TuSdkVideoFileFrame.this.d.onTestResult(_TuSdkVideoFileFrame.this);
                    }
                    if (ex == null) {
                        return;
                    }
                    TLog.w(ex.getMessage(), new Object[0]);
                }
            };
        }
        
        private void a() {
            if (this.c) {
                return;
            }
            this.c = true;
            if (this.a != null) {
                this.a.release();
                this.a = null;
            }
        }
        
        @Override
        protected void finalize() {
            this.a();
            try {
                super.finalize();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        
        private TuSdkVideoFileFrame b() {
            (this.a = new TuSdkMediaFileExtractor().setDataSource(this.b)).syncPlay();
            this.e.decodecInit(this.a);
            this.a();
            return this;
        }
        
        private void a(final AysncTest d) {
            if (this.b == null || !this.b.isValid()) {
                TLog.w("%s file path is not exists.", "TuSdkVideoFileFrame");
                if (d != null) {
                    d.onTestResult(this);
                }
                return;
            }
            this.d = d;
            (this.a = new TuSdkMediaFileExtractor().setDecodecOperation(this.e).setDataSource(this.b)).play();
        }
        
        private boolean a(final TuSdkMediaExtractor tuSdkMediaExtractor) {
            if (tuSdkMediaExtractor == null) {
                return false;
            }
            final long sampleTime = tuSdkMediaExtractor.getSampleTime();
            final boolean b = this.b(tuSdkMediaExtractor);
            tuSdkMediaExtractor.seekTo(sampleTime);
            return b;
        }
        
        private boolean b(final TuSdkMediaExtractor tuSdkMediaExtractor) {
            if (tuSdkMediaExtractor == null) {
                return false;
            }
            this.endTimeUs = tuSdkMediaExtractor.seekTo(21474836470L);
            this.startTimeUs = tuSdkMediaExtractor.seekTo(0L);
            if (this.startTimeUs < 0L || this.startTimeUs == this.endTimeUs || !tuSdkMediaExtractor.advance()) {
                this.e.decodecException(new TuSdkNoMediaTrackException(String.format("%s nothing frame.", "TuSdkVideoFileFrame")));
                return false;
            }
            final long sampleTime = tuSdkMediaExtractor.getSampleTime();
            if (sampleTime < 0L || sampleTime == this.startTimeUs) {
                this.e.decodecException(new TuSdkNoMediaTrackException(String.format("%s only one frame: next[%d]", "TuSdkVideoFileFrame", sampleTime)));
                return false;
            }
            this.intervalUs = sampleTime - this.startTimeUs;
            if (!this.c(tuSdkMediaExtractor)) {
                return false;
            }
            if (this.d != null) {
                this.d.onTestResult(this);
            }
            return true;
        }
        
        private boolean c(final TuSdkMediaExtractor tuSdkMediaExtractor) {
            long n = 0L;
            if (this.endTimeUs > this.startTimeUs) {
                n = new Random().nextInt((int)(this.endTimeUs / 2L));
            }
            final long seekTo = tuSdkMediaExtractor.seekTo(n);
            tuSdkMediaExtractor.advance();
            final long sampleTime = tuSdkMediaExtractor.getSampleTime();
            final long seekTo2 = tuSdkMediaExtractor.seekTo(sampleTime);
            if (seekTo2 == sampleTime && (tuSdkMediaExtractor.getSampleFlags() & 0x1) != 0x0) {
                this.keyFrameRate = 0;
                this.keyFrameIntervalUs = this.intervalUs;
                this.a(tuSdkMediaExtractor, seekTo2);
                return true;
            }
            final long[] array = { 1L, 1000L, this.intervalUs / 2L, this.intervalUs, this.endTimeUs };
            long seekTo3 = seekTo;
            for (int n2 = 0; n2 < array.length && seekTo3 == seekTo; seekTo3 = tuSdkMediaExtractor.seekTo(seekTo + this.skipMinUs, 1), ++n2) {
                this.skipMinUs = array[n2];
            }
            if (seekTo3 != seekTo) {
                this.keyFrameIntervalUs = seekTo3 - seekTo;
                this.keyFrameRate = (int)(this.keyFrameIntervalUs / this.intervalUs) - 1;
                return true;
            }
            this.skipMinUs = -1L;
            return true;
        }
        
        private void a(final TuSdkMediaExtractor tuSdkMediaExtractor, final long n) {
            final long[] array = { 1L, 1000L, this.intervalUs - 999L, this.intervalUs - 100L, this.intervalUs, this.intervalUs + 1000L };
            long seekTo = n;
            for (int n2 = 0; n2 < array.length && seekTo == n; seekTo = tuSdkMediaExtractor.seekTo(n + this.skipMinUs, 1), ++n2) {
                this.skipMinUs = array[n2];
            }
            if (seekTo == n) {
                TLog.e("%s all key frame seek to next failed.", "TuSdkVideoFileFrame");
                this.skipMinUs = -1L;
                return;
            }
            long seekTo2 = n;
            for (int n3 = 0; n3 < array.length && seekTo2 == n; seekTo2 = tuSdkMediaExtractor.seekTo(n - this.skipPreviousMinUs, 0), ++n3) {
                this.skipPreviousMinUs = array[n3];
            }
            if (seekTo2 == n) {
                TLog.e("%s all key frame seek to previous failed.", "TuSdkVideoFileFrame");
                this.skipPreviousMinUs = -1L;
            }
        }
    }
    
    public interface AysncTest
    {
        void onTestResult(final TuSdkVideoFileFrame p0);
    }
}
