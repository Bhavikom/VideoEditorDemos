// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.mutablePlayer;

import android.media.MediaCodec;
//import org.lasque.tusdk.core.utils.TLog;
//import junit.framework.Assert;
import java.nio.ByteBuffer;
import android.media.MediaExtractor;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

@TargetApi(16)
class AVAssetTrackMediaExtractor implements AVAssetTrackExtractor
{
    private AVAssetTrack a;
    private MediaExtractor b;
    private ByteBuffer c;
    private AVTimeRange d;
    private long e;
    private boolean f;
    private boolean g;
    
    public AVAssetTrackMediaExtractor(final AVAssetTrack a) {
        this.e = -1L;
        //Assert.assertNotNull(String.format("%s : The track parameter cannot be null ", this), (Object)a);
        //Assert.assertEquals(String.format("%s : Please check whether the track information is valid", this), true, a.getTrackID() >= 0);
        this.a = a;
        if (this.a.timeRange() != null) {
            this.d = this.a.timeRange();
        }
        else {
            this.d = AVTimeRange.AVTimeRangeMake(0L, this.a.durationTimeUs());
        }
        this.prepare();
    }
    
    public AVTimeRange timeRange() {
        return this.d;
    }
    
    @Override
    public void setTimeRange(final AVTimeRange avTimeRange) {
        if (avTimeRange == null) {
            return;
        }
        if (avTimeRange.durationUs() <= 0L) {
            return;
        }
        this.d = AVTimeRange.AVTimeRangeMake(avTimeRange.startUs(), Math.min(avTimeRange.durationUs(), this.inputTrack().durationTimeUs()));
    }
    
    protected boolean prepare() {
        if (this.b != null) {
            return true;
        }
        this.reset();
        this.b = this.a.getAsset().createExtractor();
        try {
            if (this.b.getTrackCount() <= 0) {
                TLog.e("%s \u8d44\u4ea7\u9519\u8bef\u65e0\u53ef\u8bfb\u53d6\u7684\u8f68\u9053\u4fe1\u606f", this);
                this.b.release();
                return false;
            }
            this.b.selectTrack(this.a.getTrackID());
            if (this.a.mediaFormat() == null) {
                TLog.e("%s : %d \u8f68\u9053\u7d22\u5f15\u9519\u8bef", this.a.getTrackID());
                this.b.release();
                return false;
            }
            this.b.seekTo(this.d.startUs(), 0);
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
        return false;
    }
    
    @Override
    public AVAssetTrack inputTrack() {
        return this.a;
    }
    
    @Override
    public boolean isDecodeOnly(final long n) {
        if (this.e > 0L) {
            return n < this.e;
        }
        return !this.d.containsTimeUs(n);
    }
    
    @Override
    public long durationTimeUs() {
        if (this.inputTrack() == null) {
            return 0L;
        }
        if (this.d != null) {
            return this.d.durationUs();
        }
        return this.inputTrack().durationTimeUs();
    }
    
    @Override
    public long outputTimeUs() {
        return this.b.getSampleTime();
    }
    
    @Override
    public long calOutputTimeUs(final long n) {
        return n;
    }
    
    @Override
    public boolean seekTo(final long e, final int n) {
        if (this.b == null || e < 0L) {
            return false;
        }
        this.e = e;
        this.b.seekTo(e, n);
        return true;
    }
    
    @Override
    public AVSampleBuffer readNextSampleBuffer(final int n) {
        //Assert.assertNotNull(String.format("%s : The track parameter cannot be null ", this), (Object)this.a);
        final AVSampleBuffer sampleBuffer = this.readSampleBuffer(n);
        this.advance();
        return sampleBuffer;
    }
    
    @Override
    public AVSampleBuffer readSampleBuffer(final int n) {
        //Assert.assertNotNull(String.format("%s : The track parameter cannot be null ", this), (Object)this.a);
        if (!this.a()) {
            TLog.i("readSampleBuffer read done. ", new Object[0]);
            this.f = true;
            return null;
        }
        Label_0101: {
            if (this.c != null) {
                if (!this.g) {
                    break Label_0101;
                }
            }
            try {
                this.c = ByteBuffer.allocate(this.a.mediaFormat().getInteger("max-input-size"));
            }
            catch (Exception ex) {
                TLog.w("get max input size error", new Object[0]);
                this.c = ByteBuffer.allocate(4096);
            }
        }
        this.c.clear();
        final int sampleData = this.b.readSampleData(this.c, n);
        if (sampleData <= 0) {
            return null;
        }
        final MediaCodec.BufferInfo bufferInfo = new MediaCodec.BufferInfo();
        bufferInfo.offset = 0;
        bufferInfo.presentationTimeUs = this.b.getSampleTime();
        bufferInfo.size = sampleData;
        bufferInfo.flags = this.b.getSampleFlags();
        return new AVSampleBuffer(this.c, bufferInfo, this.a.mediaFormat(), this.isDecodeOnly(bufferInfo.presentationTimeUs) ? 2 : 0);
    }
    
    private boolean a() {
        return this.outputTimeUs() <= this.d.endUs();
    }
    
    @Override
    public boolean advance() {
        //Assert.assertNotNull(String.format("%s : The track parameter cannot be null ", this), (Object)this.a);
        if (!this.a()) {
            this.f = true;
            TLog.i("readSampleBuffer read done. can't advance", new Object[0]);
            return false;
        }
        final boolean advance = this.b.advance();
        this.f = !advance;
        return advance;
    }
    
    @Override
    public boolean isOutputDone() {
        return this.b != null && this.f;
    }
    
    @Override
    public void reset() {
        this.f = false;
        this.e = -1L;
        if (this.b != null) {
            this.b.seekTo(this.d.startUs(), 2);
        }
    }
    
    @Override
    public void setAlwaysCopiesSampleData(final boolean g) {
        this.g = g;
    }
}
