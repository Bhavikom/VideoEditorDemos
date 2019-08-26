// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.sync;

//import org.lasque.tusdk.core.media.codec.video.TuSdkVideoInfo;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSlicePatch;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioResample;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioPitchHardImpl;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioResampleHardImpl;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioTrackImpl;
import android.media.MediaCodec;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import java.util.concurrent.locks.ReentrantLock;
//import org.lasque.tusdk.core.utils.TuSdkSemaphore;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioTrackWrap;
//import org.lasque.tusdk.core.media.codec.audio.TuSdkAudioTrack;
import java.util.HashMap;
//import org.lasque.tusdk.core.utils.hardware.HardwareHelper;
//import org.lasque.tusdk.core.utils.ThreadHelper;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSliceEntity;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
//import org.lasque.tusdk.core.api.extend.TuSdkAudioRender;
//import org.lasque.tusdk.core.media.codec.extend.TuSdkMediaFileCuterTimeline;
import java.util.Map;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.extend.TuSdkAudioRender;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.TuSdkMediaExtractor;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioPitchHardImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioResample;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioResampleHardImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioTrack;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioTrackImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.audio.TuSdkAudioTrackWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaCodec;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaFileCuterTimeline;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSliceEntity;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeSlicePatch;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaTimeline;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.extend.TuSdkMediaUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.video.TuSdkVideoInfo;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TuSdkSemaphore;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.HardwareHelper;

@TargetApi(16)
public class TuSdkMediaFileDirectorPlayerSync implements TuSdkMediaDecodecSync
{
    private static final Map<String, String> a;
    private static final Map<String, String> b;
    private TuSdkEffectFrameCalc c;
    private TuSdkDirectorPlayerStateCallback d;
    private final TuSdkMediaFileCuterTimeline e;
    private Object f;
    private boolean g;
    private _AudioDecodecSync h;
    private _VideoDecodecSync i;
    private boolean j;
    private boolean k;
    private boolean l;
    private TuSdkAudioRender m;
    private TuSdkAVSynchronizerImpl n;
    private long o;
    
    public TuSdkMediaFileDirectorPlayerSync() {
        this.e = new TuSdkMediaFileCuterTimeline();
        this.f = new Object();
        this.g = false;
        this.j = true;
        this.k = false;
        this.n = new TuSdkAVSynchronizerImpl();
        this.o = -1L;
    }
    
    public void setEffectFrameCalc(final TuSdkEffectFrameCalc c) {
        this.c = c;
    }
    
    public void setDirectorPlayerStateCallback(final TuSdkDirectorPlayerStateCallback d) {
        this.d = d;
    }
    
    @Override
    public TuSdkVideoDecodecSync buildVideoDecodecSync() {
        return this.getVideoDecodecSync();
    }
    
    @Override
    public TuSdkAudioDecodecSync buildAudioDecodecSync() {
        return this.getAudioDecodecSync();
    }
    
    @Override
    public TuSdkVideoDecodecSync getVideoDecodecSync() {
        if (this.i == null) {
            this.i = new _VideoDecodecSync();
        }
        return this.i;
    }
    
    @Override
    public TuSdkAudioDecodecSync getAudioDecodecSync() {
        if (this.h == null) {
            (this.h = new _AudioDecodecSync()).setPuaseLocker(this.f);
        }
        return this.h;
    }
    
    public void setHaveAudio(final boolean haveAudio) {
        this.n.setHaveAudio(haveAudio);
    }
    
    public void setMixerRender(final TuSdkAudioRender m) {
        this.m = m;
    }
    
    public void setProgressOutputMode(final int progressOutputMode) {
        this.e.setProgressOutputMode(progressOutputMode);
    }
    
    public void enableLoadFirstFramePause(final boolean l) {
        this.l = l;
    }
    
    @Override
    public void release() {
        if (this.g) {
            return;
        }
        this.g = true;
        if (this.i != null) {
            this.i.release();
        }
        if (this.h != null) {
            this.h.release();
            this.h = null;
        }
        if (this.n != null) {
            this.n.reset();
        }
    }
    
    public long outputTimeUs() {
        if (this.i == null) {
            return 0L;
        }
        return this.i.outputTimeUs();
    }
    
    public long decodeFrameTimeUs() {
        if (this.i == null) {
            return 0L;
        }
        if (this.i.lastVideoFrameTimestampUs() > this.totalVideInputDurationUs()) {
            return this.totalVideInputDurationUs();
        }
        return this.i.lastVideoFrameTimestampUs();
    }
    
    public long totalVideoDurationUs() {
        if (this.e == null || this.i == null) {
            return -1L;
        }
        return this.e.getOutputDurationUs() - this.i.frameIntervalUs();
    }
    
    public long totalVideInputDurationUs() {
        if (this.e == null || this.i == null) {
            return -1L;
        }
        long n = this.e.getRemoveOverSliceDurationUs();
        if (n > this.totalVideoDurationUs() && this.totalVideoDurationUs() > 0L) {
            n = this.totalVideoDurationUs();
        }
        return n;
    }
    
    public boolean isPause() {
        return this.i != null && this.i.isPause();
    }
    
    public void setPause() {
        synchronized (this.f) {
            if (this.i != null) {
                this.i.setPause();
            }
            if (this.h != null) {
                this.h.setPause();
            }
        }
    }
    
    public void setPlay() {
        synchronized (this.f) {
            if (this.i != null) {
                this.i.setPlay();
            }
            if (this.h != null) {
                this.h.setPlay();
            }
            if (this.h != null) {
                this.h.resetIsPauseSave();
            }
        }
    }
    
    public void setReset() {
        this.setTimeline(new TuSdkMediaTimeline(-1.0f, -1.0f));
    }
    
    public void pauseSave() {
        synchronized (this.f) {
            if (this.i != null) {
                this.i.pauseSave();
            }
            if (this.h != null) {
                this.h.pauseSave();
            }
        }
    }
    
    public void resumeSave() {
        synchronized (this.f) {
            if (this.i != null) {
                this.i.resumeSave();
            }
            if (this.h != null) {
                this.h.resumeSave();
            }
        }
    }
    
    public int setVolume(final float volume) {
        if (this.h != null) {
            return this.h.setVolume(volume);
        }
        return -1;
    }
    
    private void a() {
        if (this.i != null) {
            this.i.syncRestart();
        }
        if (this.h != null) {
            this.h.syncRestart();
        }
    }
    
    public boolean syncNeedRestart() {
        if (this.b()) {
            this.a();
            this.e.reset();
            return true;
        }
        return false;
    }
    
    private boolean b() {
        return (this.i != null && this.i.isNeedRestart()) || (this.h != null && this.h.isNeedRestart());
    }
    
    public boolean isVideoEos() {
        return this.i == null || this.i.isVideoEos();
    }
    
    public void syncVideoDecodeCompleted() {
        if (this.i == null) {
            return;
        }
        this.i.syncVideoDecodeCompleted();
        if (this.h != null && this.h.c != null) {
            this.h.c.reset();
        }
    }
    
    public void syncAudioDecodeCompleted() {
        if (this.h == null) {
            return;
        }
        this.h.syncAudioDecodeCompleted();
    }
    
    public boolean isAudioDecodeCrashed() {
        return this.h != null && this.h.isAudioDecodeCrashed();
    }
    
    public void setTimeline(final TuSdkMediaTimeline tuSdkMediaTimeline) {
        this.pauseSave();
        this.e.fresh(tuSdkMediaTimeline);
        this.k = true;
        this.resumeSave();
    }
    
    public long calInputTimeUs(final long n) {
        if (!this.e.isFixTimeSlices()) {
            return -1L;
        }
        final TuSdkMediaTimeSliceEntity sliceWithOutputTimeUs = this.e.sliceWithOutputTimeUs(n);
        if (sliceWithOutputTimeUs == null) {
            return -1L;
        }
        if (sliceWithOutputTimeUs.previous != null && sliceWithOutputTimeUs.previous.overlapIndex > -1) {
            return sliceWithOutputTimeUs.calInputTimeUs(n, this.e);
        }
        return sliceWithOutputTimeUs.calInputTimeUs(n);
    }
    
    public long calOutputTimeUs(final long n) {
        if (!this.e.isFixTimeSlices()) {
            return -1L;
        }
        final TuSdkMediaTimeSliceEntity sliceWithInputTimeUs = this.e.sliceWithInputTimeUs(n);
        if (sliceWithInputTimeUs == null) {
            return -1L;
        }
        return sliceWithInputTimeUs.calOutputTimeUs(n);
    }
    
    public void syncSeektoTimeUs(final long o) {
        this.o = o;
        this.j = true;
    }
    
    public long getSeekToTimeUs() {
        return this.o;
    }
    
    public void syncFlushAndSeekto(final long n) {
        this.a();
        if (this.i != null) {
            this.i.syncFlushAndSeekto(n);
        }
        if (this.h != null) {
            this.h.syncFlushAndSeekto(n);
        }
        this.o = -1L;
        this.e.reset();
    }
    
    public TuSdkMediaFileCuterTimeline getTimeline() {
        return this.e;
    }
    
    private void a(final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity, final long n) {
        if (this.h == null || tuSdkMediaTimeSliceEntity == null) {
            return;
        }
        this.h.setTimeSlice(tuSdkMediaTimeSliceEntity, n);
        while (!ThreadHelper.isInterrupted() && !this.g && this.h.isTimelineFresh() && !this.k) {}
        if (this.k) {
            this.k = false;
        }
    }
    
    private void a(final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity) {
        if (this.h == null || tuSdkMediaTimeSliceEntity == null) {
            return;
        }
        this.h.waitVideo(tuSdkMediaTimeSliceEntity);
    }
    
    private boolean b(final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity) {
        return this.i != null && tuSdkMediaTimeSliceEntity != null && this.i.waitAudio(tuSdkMediaTimeSliceEntity);
    }
    
    private boolean c() {
        boolean matchDeviceModelAndManuFacturer = false;
        for (final Map.Entry<String, String> entry : TuSdkMediaFileDirectorPlayerSync.b.entrySet()) {
            matchDeviceModelAndManuFacturer = HardwareHelper.isMatchDeviceModelAndManuFacturer(entry.getKey(), entry.getValue());
            if (matchDeviceModelAndManuFacturer) {
                break;
            }
        }
        return matchDeviceModelAndManuFacturer;
    }
    
    static {
        a = new HashMap<String, String>();
        b = new HashMap<String, String>();
        TuSdkMediaFileDirectorPlayerSync.a.put("PADM00", "OPPO");
        TuSdkMediaFileDirectorPlayerSync.a.put("Le X620", "LeMobile");
        TuSdkMediaFileDirectorPlayerSync.a.put("MX5", "Meizu");
        TuSdkMediaFileDirectorPlayerSync.b.put("OD103", "Smartisan");
        TuSdkMediaFileDirectorPlayerSync.b.put("OS105", "Smartisan");
    }
    
    private class _AudioDecodecSync extends TuSdkAudioDecodecSyncBase implements TuSdkAudioPitchSync
    {
        private TuSdkAudioTrack b;
        private TuSdkAudioTrackWrap c;
        private boolean d;
        private boolean e;
        private long f;
        private TuSdkMediaTimeSliceEntity g;
        private TuSdkMediaTimeSliceEntity h;
        protected TuSdkSemaphore mAudioSemaphore;
        private ReentrantLock i;
        private TuSdkAudioInfo j;
        private long k;
        private float l;
        private boolean m;
        private TuSdkMediaExtractor n;
        private TuSdkAudioRender.TuSdkAudioRenderCallback o;
        
        public _AudioDecodecSync() {
            this.d = false;
            this.e = false;
            this.f = 0L;
            this.mAudioSemaphore = new TuSdkSemaphore(1);
            this.i = new ReentrantLock();
            this.l = 1.0f;
            this.m = false;
            this.o = new TuSdkAudioRender.TuSdkAudioRenderCallback() {
                @Override
                public boolean isEncodec() {
                    return false;
                }
                
                @Override
                public TuSdkAudioInfo getAudioInfo() {
                    return _AudioDecodecSync.this.j;
                }
                
                @Override
                public void returnRenderBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
                    _AudioDecodecSync.this.a(byteBuffer, bufferInfo);
                }
            };
        }
        
        @Override
        public void setPause() {
            super.setPause();
            if (this.b != null) {
                this.c.pause();
                this.b.pause();
                this.b.flush();
                this.c.setAudioBufferPts(this.k);
            }
        }
        
        public int setVolume(final float n) {
            if (this.b != null) {
                this.l = n;
                return this.b.setVolume(n);
            }
            return -1;
        }
        
        @Override
        public void setPlay() {
            super.setPlay();
            if (this.b != null) {
                this.c.resume();
                this.b.play();
            }
        }
        
        @Override
        public void release() {
            super.release();
            if (this.c != null) {
                this.c.release();
                this.c = null;
            }
            if (this.b != null) {
                this.b.release();
                this.b = null;
            }
            if (this.mAudioResample != null) {
                this.mAudioResample.release();
                this.mAudioResample = null;
            }
            if (this.mAudioPitch != null) {
                this.mAudioPitch.release();
                this.mAudioPitch = null;
            }
        }
        
        @Override
        public void syncFlushAndSeekto(final long n) {
            synchronized (this.mLocker) {
                this.e = false;
            }
        }
        
        public void setTimeSlice(final TuSdkMediaTimeSliceEntity g, final long mFlushAndSeekto) {
            try {
                this.mAudioSemaphore.acquire();
                synchronized (this.mLocker) {
                    this.g = g;
                    this.h = null;
                    this.mFlushAndSeekto = mFlushAndSeekto;
                    this.d = true;
                }
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
        
        public boolean isTimelineFresh() {
            return !this.mReleased && this.d;
        }
        
        @Override
        public void syncAudioDecodecInfo(final TuSdkAudioInfo j, final TuSdkMediaExtractor tuSdkMediaExtractor) {
            if (j == null || tuSdkMediaExtractor == null) {
                return;
            }
            super.syncAudioDecodecInfo(j, tuSdkMediaExtractor);
            this.j = j;
            this.b = new TuSdkAudioTrackImpl(j);
            (this.c = new TuSdkAudioTrackWrap()).setAudioTrack((TuSdkAudioTrackImpl)this.b, j);
            TuSdkMediaFileDirectorPlayerSync.this.n.setAudioTrackWarp(this.c);
            this.b.play();
            (this.mAudioResample = new TuSdkAudioResampleHardImpl(j)).setMediaSync(this);
            (this.mAudioPitch = new TuSdkAudioPitchHardImpl(j)).changeFormat(j);
            this.mAudioPitch.setMediaSync(this);
        }
        
        private void a(final TuSdkMediaExtractor tuSdkMediaExtractor) {
            if (this.g == null || tuSdkMediaExtractor == null) {
                return;
            }
            if (this.mAudioSemaphore.availablePermits() != 1) {
                synchronized (this.mLocker) {
                    this.a(tuSdkMediaExtractor, this.g);
                }
                return;
            }
            synchronized (this.mLocker) {
                this.a(tuSdkMediaExtractor, this.g = this.g.next);
            }
        }
        
        private void a(final TuSdkMediaExtractor tuSdkMediaExtractor, final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity) {
            if (tuSdkMediaTimeSliceEntity == null || tuSdkMediaExtractor == null) {
                return;
            }
            if (tuSdkMediaTimeSliceEntity.isReverse()) {
                if (tuSdkMediaTimeSliceEntity.isAudioReverse()) {
                    final long seekTo = tuSdkMediaExtractor.seekTo(tuSdkMediaTimeSliceEntity.endUs, !tuSdkMediaTimeSliceEntity.isReverse());
                    tuSdkMediaTimeSliceEntity.audioEndUs = seekTo;
                    this.f = seekTo;
                    tuSdkMediaTimeSliceEntity.audioStartUs = this.a(tuSdkMediaExtractor, true, tuSdkMediaTimeSliceEntity.startUs);
                }
                else {
                    final long a = this.a(tuSdkMediaExtractor, true, tuSdkMediaTimeSliceEntity.startUs);
                    tuSdkMediaTimeSliceEntity.audioEndUs = a;
                    this.f = a;
                    tuSdkMediaTimeSliceEntity.audioStartUs = tuSdkMediaExtractor.seekTo(tuSdkMediaTimeSliceEntity.endUs, tuSdkMediaTimeSliceEntity.isReverse());
                }
            }
            else {
                final long endUs = tuSdkMediaTimeSliceEntity.endUs;
                if (endUs > 0L) {
                    final long a2 = this.a(tuSdkMediaExtractor, true, endUs);
                    tuSdkMediaTimeSliceEntity.audioEndUs = a2;
                    this.f = a2;
                }
                tuSdkMediaTimeSliceEntity.audioStartUs = tuSdkMediaExtractor.seekTo(tuSdkMediaTimeSliceEntity.startUs, tuSdkMediaTimeSliceEntity.isReverse());
            }
            if (this.c != null) {
                this.c.reset();
            }
        }
        
        private long a(final TuSdkMediaExtractor tuSdkMediaExtractor, final boolean b, long n) {
            long seekTo = -1L;
            for (int n2 = 0; seekTo == -1L; seekTo = tuSdkMediaExtractor.seekTo(n, b), n -= 100L, ++n2) {}
            return seekTo;
        }
        
        private void a(final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity) {
            if (this.mAudioResample == null || tuSdkMediaTimeSliceEntity == null) {
                return;
            }
            synchronized (this.mLocker) {
                this.mAudioPitch.changeSpeed(tuSdkMediaTimeSliceEntity.speed);
                this.mAudioResample.changeSequence(tuSdkMediaTimeSliceEntity.isReverse() && tuSdkMediaTimeSliceEntity.isAudioReverse());
                TuSdkMediaFileDirectorPlayerSync.this.n.reset();
            }
        }
        
        @Override
        public boolean syncAudioDecodecExtractor(final TuSdkMediaExtractor n, final TuSdkMediaCodec tuSdkMediaCodec) {
            if (this.mReleased || n == null || tuSdkMediaCodec == null) {
                return true;
            }
            this.n = n;
            this.lockAudio();
            if (this.d) {
                this.flush(tuSdkMediaCodec);
                if (this.g != null) {
                    this.a(n, this.g);
                    this.a(this.g);
                    long n2 = this.mFlushAndSeekto;
                    if (this.g.isReverse() && !this.g.isAudioReverse()) {
                        n2 = this.g.calOutputTimeUs(this.mFlushAndSeekto);
                    }
                    final long seekTo = n.seekTo(n2, this.g.isReverse());
                    if (this.g.isAudioReverse()) {
                        this.k = this.g.calOutputTimeUs(this.mFlushAndSeekto);
                        this.c.setAudioBufferPts(this.g.calOutputTimeUs(this.mFlushAndSeekto));
                    }
                    else {
                        this.k = this.g.calOutputAudioTimeUs(seekTo);
                        this.c.setAudioBufferPts(this.g.calOutputAudioTimeUs(seekTo));
                    }
                }
                this.h = this.g;
                this.mFlushAndSeekto = -1L;
                this.e = true;
                this.d = false;
                this.mAudioSemaphore.release();
            }
            if (TuSdkMediaFileDirectorPlayerSync.this.getSeekToTimeUs() > -1L || !this.e) {
                n.seekTo(TuSdkMediaFileDirectorPlayerSync.this.getSeekToTimeUs());
                TuSdkMediaUtils.putBufferToCoderUntilEnd(n, tuSdkMediaCodec, false);
                this.unLockAudio();
                return false;
            }
            final long sampleTime = n.getSampleTime();
            final boolean putBufferToCoderUntilEnd = TuSdkMediaUtils.putBufferToCoderUntilEnd(n, tuSdkMediaCodec, false);
            this.mFrameIntervalUs = n.getFrameIntervalUs();
            if (this.g == null) {
                n.seekTo(this.f);
            }
            else if (this.g.overviewAudio(sampleTime) > 0) {
                this.a(n);
            }
            else if (this.g.isReverse() && this.g.isAudioReverse()) {
                if (this.mMinFrameTimeUs == sampleTime) {
                    this.a(n);
                    this.unLockAudio();
                    return false;
                }
                n.seekTo(sampleTime - 1L, 0);
            }
            else if (putBufferToCoderUntilEnd || n.getSampleTime() < 0L) {
                this.mMaxFrameTimeUs = n.seekTo(this.mDurationUs);
                this.a(n);
            }
            this.unLockAudio();
            return false;
        }
        
        @Override
        public void syncAudioDecodecOutputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo, final TuSdkAudioInfo tuSdkAudioInfo) {
            final TuSdkAudioResample mAudioResample = this.mAudioResample;
            final TuSdkMediaTimeSliceEntity h = this.h;
            if (bufferInfo == null || bufferInfo.size < 1 || mAudioResample == null || h == null || !this.e) {
                this.unLockAudio();
                return;
            }
            if (TuSdkMediaFileDirectorPlayerSync.this.getSeekToTimeUs() > -1L) {
                this.k = this.h.calOutputAudioTimeUs(bufferInfo.presentationTimeUs);
                if (this.c != null) {
                    this.c.setAudioBufferPts(this.h.calOutputAudioTimeUs(bufferInfo.presentationTimeUs));
                }
                this.unLockAudio();
                return;
            }
            if (this.n.getSampleTime() < bufferInfo.presentationTimeUs && this.n.getSampleTime() >= 0L && this.g != null && !this.g.isReverse()) {
                this.h = this.g;
                return;
            }
            final long presentationTimeUs = bufferInfo.presentationTimeUs;
            bufferInfo.presentationTimeUs = h.calOutputAudioTimeUs(presentationTimeUs);
            this.k = bufferInfo.presentationTimeUs;
            final int overviewAudio = h.overviewAudio(presentationTimeUs);
            if (overviewAudio < 0) {
                this.unLockAudio();
                return;
            }
            if (overviewAudio <= 0) {
                final MediaCodec.BufferInfo cloneBufferInfo = TuSdkMediaUtils.cloneBufferInfo(bufferInfo);
                cloneBufferInfo.presentationTimeUs = h.calOutputOrginTimeUs(presentationTimeUs);
                if (h.next == null && h.audioEndUs == presentationTimeUs) {
                    this.h = null;
                }
                mAudioResample.queueInputBuffer(byteBuffer, cloneBufferInfo);
                return;
            }
            this.h = h.next;
            if (this.h == null) {
                this.unLockAudio();
                return;
            }
            if (TuSdkMediaFileDirectorPlayerSync.this.b(this.h)) {
                this.unLockAudio();
                return;
            }
            this.a(this.h);
            if (this.h.overviewAudio(presentationTimeUs) == 0) {
                bufferInfo.presentationTimeUs = this.h.calOutputAudioTimeUs(presentationTimeUs);
            }
            this.unLockAudio();
        }
        
        public void waitVideo(final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity) {
            if (this.h == null) {
                return;
            }
            while (!this.isInterrupted() && this.e && TuSdkMediaFileDirectorPlayerSync.this.getSeekToTimeUs() < 0L) {
                if (this.h == null || this.h.index == tuSdkMediaTimeSliceEntity.index) {
                    return;
                }
            }
        }
        
        @Override
        public void syncAudioResampleOutputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
            if (bufferInfo == null || bufferInfo.size < 1) {
                return;
            }
            this.mAudioPitch.queueInputBuffer(byteBuffer, bufferInfo);
        }
        
        private void a(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
            final TuSdkAudioTrack b = this.b;
            if (b == null || bufferInfo == null || bufferInfo.size < 1) {
                this.unLockAudio();
                return;
            }
            this.mPreviousTimeUs = this.mLastTimeUs;
            this.mLastTimeUs = bufferInfo.presentationTimeUs;
            if (!this.m) {
                if (TuSdkMediaFileDirectorPlayerSync.this.l) {
                    b.setVolume(0.0f);
                }
                else {
                    this.setVolume(this.l);
                    this.m = true;
                }
            }
            byteBuffer.position(bufferInfo.offset);
            byteBuffer.limit(bufferInfo.offset + bufferInfo.size);
            b.write(byteBuffer);
            this.unLockAudio();
        }
        
        public void lockAudio() {
            if (this.i.isLocked()) {
                return;
            }
            this.i.lock();
        }
        
        public void unLockAudio() {
            if (!this.i.isLocked()) {
                return;
            }
            this.i.unlock();
        }
        
        @Override
        public void syncAudioPitchOutputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo) {
            final TuSdkAudioRender i = TuSdkMediaFileDirectorPlayerSync.this.m;
            if (bufferInfo == null || bufferInfo.size < 1) {
                return;
            }
            if (TuSdkMediaFileDirectorPlayerSync.this.m == null || !i.onAudioSliceRender(byteBuffer, bufferInfo, this.o)) {
                this.a(byteBuffer, bufferInfo);
            }
        }
        
        public long getVideoDisplayTimeUs() {
            if (this.c == null) {
                return 0L;
            }
            return this.c.getVideoDisplayTimeUs();
        }
    }
    
    private class _VideoDecodecSync extends TuSdkVideoDecodecSyncBase
    {
        private boolean b;
        private TuSdkMediaTimeSliceEntity c;
        private TuSdkMediaTimeSliceEntity d;
        private long e;
        private long f;
        private ReentrantLock g;
        private TuSdkMediaExtractor h;
        private boolean i;
        private TuSdkMediaTimeSlicePatch j;
        private boolean k;
        
        private _VideoDecodecSync() {
            this.b = false;
            this.e = 0L;
            this.f = 0L;
            this.g = new ReentrantLock();
            this.i = false;
            this.j = new TuSdkMediaTimeSlicePatch();
            this.k = false;
        }
        
        public boolean isVideoEos() {
            return this.b && this.d == null;
        }
        
        @Override
        public void syncFlushAndSeekto(final long n) {
            this.b = false;
            TuSdkMediaFileDirectorPlayerSync.this.j = true;
            super.syncFlushAndSeekto(n);
        }
        
        @Override
        public void syncVideoDecodecInfo(final TuSdkVideoInfo tuSdkVideoInfo, final TuSdkMediaExtractor h) {
            if (tuSdkVideoInfo == null || h == null) {
                return;
            }
            final long durationUs = tuSdkVideoInfo.durationUs;
            h.seekTo(durationUs, 0);
            long b = h.getSampleTime();
            while (h.advance()) {
                b = Math.max(h.getSampleTime(), b);
            }
            tuSdkVideoInfo.durationUs = ((b > 0L) ? b : durationUs);
            h.seekTo(0L);
            super.syncVideoDecodecInfo(tuSdkVideoInfo, h);
            TuSdkMediaFileDirectorPlayerSync.this.e.setInputDurationUs(tuSdkVideoInfo.durationUs);
            this.h = h;
        }
        
        private void a(final TuSdkMediaExtractor tuSdkMediaExtractor) {
            if (this.c == null || tuSdkMediaExtractor == null) {
                return;
            }
            this.c = this.c.next;
            if (this.c != null) {
                this.e = this.c.endUs;
                tuSdkMediaExtractor.seekTo(this.c.startUs);
            }
        }
        
        private long a(final long n) {
            if (n < 0L || this.c == null) {
                return -1L;
            }
            if (this.c.speed <= 1.0f || this.mFrameIntervalUs == 0L) {
                return n;
            }
            return (long)Math.floor(n + this.mFrameIntervalUs * this.c.speed);
        }
        
        private long b(final long n) {
            if (n < 0L || this.c == null) {
                return -1L;
            }
            if (this.c.speed <= 1.0f || this.mFrameIntervalUs == 0L) {
                return n - this.mFrameIntervalUs;
            }
            return (long)Math.ceil(n - this.mFrameIntervalUs * this.c.speed);
        }
        
        @Override
        public boolean syncVideoDecodecExtractor(final TuSdkMediaExtractor tuSdkMediaExtractor, final TuSdkMediaCodec tuSdkMediaCodec) {
            if (this.mReleased || tuSdkMediaExtractor == null || tuSdkMediaCodec == null) {
                return true;
            }
            this.lockVideo();
            long seekTo = -1L;
            if (!TuSdkMediaFileDirectorPlayerSync.this.e.isFixTimeSlices()) {
                this.flush(tuSdkMediaCodec);
                TuSdkMediaFileDirectorPlayerSync.this.e.fixTimeSlices(tuSdkMediaExtractor, this.isSupportPrecise(), true);
                final TuSdkMediaTimeSliceEntity sliceWithOutputTimeUs = TuSdkMediaFileDirectorPlayerSync.this.e.sliceWithOutputTimeUs(this.mFlushAndSeekto);
                this.c = sliceWithOutputTimeUs;
                this.d = sliceWithOutputTimeUs;
                if (this.c != null) {
                    this.e = this.c.endUs;
                    seekTo = tuSdkMediaExtractor.seekTo(this.c.calInputTimeUs(this.mFlushAndSeekto) - 1L);
                    this.f = seekTo;
                    this.mOutputTimeUs = this.c.calOutputTimeUs(seekTo);
                }
                TuSdkMediaFileDirectorPlayerSync.this.a(this.c, seekTo);
                this.mFlushAndSeekto = -1L;
                this.mRelativeStartNs = -1L;
                TuSdkMediaFileDirectorPlayerSync.this.n.setRelativeStartNs(this.mRelativeStartNs);
                TuSdkMediaFileDirectorPlayerSync.this.n.reset();
                this.b = true;
            }
            if (!this.b) {
                this.unLockVideo();
                return false;
            }
            if (!TuSdkMediaFileDirectorPlayerSync.this.j) {
                this.unLockVideo();
                return false;
            }
            if (TuSdkMediaFileDirectorPlayerSync.this.getSeekToTimeUs() > -1L) {
                long startUs = (this.c != null && this.c.isReverse()) ? seekTo : TuSdkMediaFileDirectorPlayerSync.this.getSeekToTimeUs();
                if (startUs == -1L && this.c != null) {
                    startUs = this.c.startUs;
                }
                final long seekTo2 = tuSdkMediaExtractor.seekTo(startUs);
                TuSdkMediaUtils.putBufferToCoderUntilEnd(tuSdkMediaExtractor, tuSdkMediaCodec, false);
                this.mOutputTimeUs = ((this.c == null) ? seekTo2 : this.c.calOutputTimeUs(seekTo2));
                TuSdkMediaFileDirectorPlayerSync.this.j = false;
                this.unLockVideo();
                return false;
            }
            final long sampleTime = tuSdkMediaExtractor.getSampleTime();
            final boolean putBufferToCoderUntilEnd = TuSdkMediaUtils.putBufferToCoderUntilEnd(tuSdkMediaExtractor, tuSdkMediaCodec, false);
            this.mFrameIntervalUs = tuSdkMediaExtractor.getFrameIntervalUs();
            if (this.c == null) {
                tuSdkMediaExtractor.seekTo(this.e);
            }
            else if (this.c.overview(sampleTime) > 0) {
                this.a(tuSdkMediaExtractor);
            }
            else if (this.c.isReverse()) {
                if (this.mMinFrameTimeUs == sampleTime) {
                    this.a(tuSdkMediaExtractor);
                    this.unLockVideo();
                    return false;
                }
                final long b = this.b(sampleTime);
                if (TuSdkMediaFileDirectorPlayerSync.this.c()) {
                    this.a(tuSdkMediaExtractor, b, sampleTime);
                }
                else {
                    tuSdkMediaExtractor.seekTo(b, 0);
                }
            }
            else if (putBufferToCoderUntilEnd || tuSdkMediaExtractor.getSampleTime() < 0L) {
                this.mMaxFrameTimeUs = tuSdkMediaExtractor.seekTo(this.mDurationUs);
                this.a(tuSdkMediaExtractor);
            }
            else if (this.c.speed > 1.0f) {
                tuSdkMediaExtractor.seekTo(this.a(sampleTime), 2);
            }
            this.unLockVideo();
            return false;
        }
        
        private void a(final TuSdkMediaExtractor tuSdkMediaExtractor, long n, final long n2) {
            if (!TuSdkMediaFileDirectorPlayerSync.this.c()) {
                return;
            }
            for (long seekTo = n2; seekTo >= n2; seekTo = tuSdkMediaExtractor.seekTo(n, 0), n -= 200L) {}
        }
        
        private long a(final long n, final MediaCodec.BufferInfo bufferInfo) {
            final long n2;
            synchronized (this.mLocker) {
                final TuSdkMediaTimeSliceEntity d = this.d;
                final long presentationTimeUs = bufferInfo.presentationTimeUs;
                final long calOutputTimeUs = d.calOutputTimeUs(presentationTimeUs);
                bufferInfo.presentationTimeUs = calOutputTimeUs;
                if (n < presentationTimeUs && n >= 0L && this.d != null && !this.d.isReverse() && this.c != null && !this.c.isReverse()) {
                    this.d = this.c;
                    return System.nanoTime();
                }
                if (this.j.isReturnFrame(n, presentationTimeUs)) {
                    return System.nanoTime();
                }
                if (this.j.overview(d, n, presentationTimeUs)) {
                    this.j.switchSliced();
                    this.d = d.next;
                    TuSdkMediaFileDirectorPlayerSync.this.a(this.d);
                    this.f = bufferInfo.presentationTimeUs;
                    this.save();
                    return System.nanoTime();
                }
                if (d.next == null && d.endUs == presentationTimeUs) {
                    this.d = null;
                }
                if (this.mRelativeStartNs < 0L) {
                    this.f = d.calOutputNoRepetTimeUs(calOutputTimeUs, TuSdkMediaFileDirectorPlayerSync.this.e);
                    this.mOutputTimeUs = bufferInfo.presentationTimeUs;
                    this.mRelativeStartNs = System.nanoTime();
                    TuSdkMediaFileDirectorPlayerSync.this.n.setRelativeStartNs(this.mRelativeStartNs);
                }
                this.f = d.calOutputNoRepetTimeUs(calOutputTimeUs, TuSdkMediaFileDirectorPlayerSync.this.e);
                this.mPreviousTimeUs = this.mOutputTimeUs;
                this.mOutputTimeUs = bufferInfo.presentationTimeUs;
                final long mRelativeStartNs = this.mRelativeStartNs + Math.abs(this.mOutputTimeUs - this.mPreviousTimeUs) * 1000L;
                this.mRelativeStartNs = mRelativeStartNs;
                n2 = mRelativeStartNs;
            }
            return n2;
        }
        
        @Override
        public void syncVideoDecodecOutputBuffer(final ByteBuffer byteBuffer, final MediaCodec.BufferInfo bufferInfo, final TuSdkVideoInfo tuSdkVideoInfo) {
            final TuSdkMediaFileCuterTimeline a = TuSdkMediaFileDirectorPlayerSync.this.e;
            if (!this.b) {
                this.unLockVideo();
                return;
            }
            if (bufferInfo == null || bufferInfo.size < 1 || this.h == null) {
                this.unLockVideo();
                return;
            }
            if (TuSdkMediaFileDirectorPlayerSync.this.getSeekToTimeUs() > -1L) {
                final long presentationTimeUs = bufferInfo.presentationTimeUs;
                final TuSdkMediaTimeSliceEntity existenceWithInputTimeUs = a.existenceWithInputTimeUs(presentationTimeUs);
                if (existenceWithInputTimeUs != null) {
                    this.mOutputTimeUs = existenceWithInputTimeUs.calOutputTimeUs(presentationTimeUs);
                    this.f = existenceWithInputTimeUs.calOutputTimeUs(presentationTimeUs);
                    bufferInfo.presentationTimeUs = this.mOutputTimeUs;
                    TuSdkMediaFileDirectorPlayerSync.this.n.reset();
                    TuSdkMediaFileDirectorPlayerSync.this.n.setVideoBufferTimeUs(this.f);
                }
                this.unLockVideo();
                return;
            }
            if (this.d == null) {
                this.unLockVideo();
                return;
            }
            final long calOutputTimeUs = this.d.calOutputTimeUs(bufferInfo.presentationTimeUs);
            final long a2 = this.a(this.h.getSampleTime(), bufferInfo);
            final long n = System.nanoTime() / 1000L;
            if (TuSdkMediaFileDirectorPlayerSync.this.n != null) {
                TuSdkMediaFileDirectorPlayerSync.this.n.setVideoBufferTimeUs(calOutputTimeUs);
                TuSdkMediaFileDirectorPlayerSync.this.n.getVideoDisplayTimeUs();
            }
            this.syncPlay(a2);
            this.unLockVideo();
        }
        
        public boolean waitAudio(final TuSdkMediaTimeSliceEntity tuSdkMediaTimeSliceEntity) {
            if (this.d == null) {
                return false;
            }
            while (!this.isInterrupted() && this.b && TuSdkMediaFileDirectorPlayerSync.this.getSeekToTimeUs() < 0L) {
                if (tuSdkMediaTimeSliceEntity == null || this.d.taskID != tuSdkMediaTimeSliceEntity.taskID) {
                    return true;
                }
                if (this.d == null || this.d.index == tuSdkMediaTimeSliceEntity.index) {
                    return false;
                }
            }
            return false;
        }
        
        @Override
        public void resumeSave() {
            super.resumeSave();
            TuSdkMediaFileDirectorPlayerSync.this.j = true;
        }
        
        @Override
        protected void syncPause() {
            if (this.isPause() && TuSdkMediaFileDirectorPlayerSync.this.d != null) {
                TuSdkMediaFileDirectorPlayerSync.this.d.onPauseWait();
            }
            while (!this.isInterrupted() && this.isPause() && !TuSdkMediaFileDirectorPlayerSync.this.k && TuSdkMediaFileDirectorPlayerSync.this.getSeekToTimeUs() <= -1L) {}
            if (TuSdkMediaFileDirectorPlayerSync.this.k) {
                TuSdkMediaFileDirectorPlayerSync.this.k = false;
            }
        }
        
        public void lockVideo() {
            if (this.g.isLocked()) {
                return;
            }
            this.g.lock();
        }
        
        public void unLockVideo() {
            if (!this.g.isLocked()) {
                return;
            }
            this.g.unlock();
        }
        
        public long lastVideoFrameTimestampUs() {
            return this.f;
        }
        
        @Override
        public long calcInputTimeUs(final long n) {
            if (TuSdkMediaFileDirectorPlayerSync.this.e == null || TuSdkMediaFileDirectorPlayerSync.this.e.getCalcMode() == 0 || !TuSdkMediaFileDirectorPlayerSync.this.e.isFixTimeSlices()) {
                return n;
            }
            final TuSdkMediaTimeSliceEntity sliceWithOutputTimeUs = TuSdkMediaFileDirectorPlayerSync.this.e.sliceWithOutputTimeUs(n);
            if (sliceWithOutputTimeUs == null) {
                return n;
            }
            return sliceWithOutputTimeUs.calOutputNoRepetTimeUs(n, TuSdkMediaFileDirectorPlayerSync.this.getTimeline());
        }
        
        @Override
        public long calcEffectFrameTimeUs(final long n) {
            if (TuSdkMediaFileDirectorPlayerSync.this.e == null || TuSdkMediaFileDirectorPlayerSync.this.e.getCalcMode() == 0 || !TuSdkMediaFileDirectorPlayerSync.this.e.isFixTimeSlices()) {
                return n;
            }
            if (TuSdkMediaFileDirectorPlayerSync.this.c == null) {
                return n;
            }
            return TuSdkMediaFileDirectorPlayerSync.this.c.calcEffectFrameUs(n, this.d);
        }
    }
    
    public interface TuSdkDirectorPlayerStateCallback
    {
        void onPauseWait();
    }
    
    public interface TuSdkEffectFrameCalc
    {
        long calcEffectFrameUs(final long p0, final TuSdkMediaTimeSliceEntity p1);
    }
}
