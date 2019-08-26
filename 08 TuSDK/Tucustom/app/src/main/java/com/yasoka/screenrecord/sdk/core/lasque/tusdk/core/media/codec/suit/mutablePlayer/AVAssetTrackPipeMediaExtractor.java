// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.mutablePlayer;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.util.ArrayList;
import java.util.Iterator;
//import org.lasque.tusdk.core.utils.TLog;
//import junit.framework.Assert;
import java.util.List;

public class AVAssetTrackPipeMediaExtractor implements AVAssetTrackOutputSouce
{
    private TrackStreamPipeTimeline a;
    
    public AVAssetTrackPipeMediaExtractor(final List<AVAssetTrack> list) {
        this.a = new TrackStreamPipeTimeline(list);
    }
    
    @Override
    public AVSampleBuffer readNextSampleBuffer(final int n) {
        //Assert.assertNotNull("No pipe node output is currently available.", (Object)this.a.d);
        final AVSampleBuffer sampleBuffer = this.readSampleBuffer(n);
        this.advance();
        return sampleBuffer;
    }
    
    @Override
    public AVSampleBuffer readSampleBuffer(final int n) {
        //Assert.assertNotNull("No pipe node output is currently available.", (Object)this.a.d);
        final AVSampleBuffer sampleBuffer = this.a.d.readSampleBuffer(n);
        if (sampleBuffer == null && !this.isOutputDone()) {
            if (this.a.d.a() != null) {
                return new AVSampleBuffer(this.a.d.a().inputTrack().mediaFormat());
            }
            this.advance();
        }
        return sampleBuffer;
    }
    
    @Override
    public void setTimeRange(final AVTimeRange avTimeRange) {
        this.a.a(avTimeRange);
    }
    
    @Override
    public boolean seekTo(final long n, final int n2) {
        return this.a.a(n, n2);
    }
    
    @Override
    public boolean advance() {
        if (this.a.d == null) {
            TLog.w("advance no data", new Object[0]);
            return false;
        }
        return this.a.d.advance() || this.a.b();
    }
    
    @Override
    public boolean isDecodeOnly(final long n) {
        return n >= 0L && this.a.d != null && this.a.d.isDecodeOnly(n);
    }
    
    @Override
    public boolean isOutputDone() {
        for (TrackStreamPipeTimeline.TrackStreamPipeNode trackStreamPipeNode = this.a.d; trackStreamPipeNode != null; trackStreamPipeNode = trackStreamPipeNode.a()) {
            if (!trackStreamPipeNode.isOutputDone()) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public long durationTimeUs() {
        return this.a.d();
    }
    
    @Override
    public long outputTimeUs() {
        if (this.a.d == null) {
            return 0L;
        }
        return this.a.d.d + this.a.d.outputTimeUs();
    }
    
    @Override
    public long calOutputTimeUs(final long n) {
        if (this.a.d == null) {
            return 0L;
        }
        return this.a.d.c(n);
    }
    
    @Override
    public void setAlwaysCopiesSampleData(final boolean alwaysCopiesSampleData) {
        for (TrackStreamPipeTimeline.TrackStreamPipeNode trackStreamPipeNode = this.a.d; trackStreamPipeNode != null; trackStreamPipeNode = trackStreamPipeNode.a()) {
            trackStreamPipeNode.setAlwaysCopiesSampleData(alwaysCopiesSampleData);
        }
    }
    
    @Override
    public AVAssetTrack inputTrack() {
        final TrackStreamPipeTimeline.TrackStreamPipeNode a = this.a.d;
        if (a != null) {
            return a.inputTrack();
        }
        return null;
    }
    
    @Override
    public void reset() {
        this.a.c();
    }
    
    private class TrackStreamPipeTimeline
    {
        private List<AVAssetTrack> b;
        private List<TrackStreamPipeNode> c;
        private TrackStreamPipeNode d;
        
        public TrackStreamPipeTimeline(final List<AVAssetTrack> b) {
            //Assert.assertEquals("Please input a valid tracks.", true, b.size() > 0);
            this.b = b;
            this.a();
            //Assert.assertEquals("Please input a valid tracks.", true, this.c.size() > 0);
            this.d = this.c.get(0);
        }
        
        private boolean a(final long n, final int n2) {
            for (final TrackStreamPipeNode d : this.c) {
                d.reset();
                if (d.a(n)) {
                    this.d = d;
                    return d.seekTo(d.b(n), n2);
                }
            }
            return false;
        }
        
        private void a() {
            //Assert.assertNotNull("Please input a valid tracks.", (Object)this.b);
            //Assert.assertEquals("Please input a valid track", true, this.b.size() > 0);
            this.c = new ArrayList<TrackStreamPipeNode>(2);
            TrackStreamPipeNode trackStreamPipeNode = null;
            long c = 0L;
            long d = 0L;
            final Iterator<AVAssetTrack> iterator = this.b.iterator();
            while (iterator.hasNext()) {
                final TrackStreamPipeNode trackStreamPipeNode2 = new TrackStreamPipeNode(iterator.next());
                if (trackStreamPipeNode == null) {
                    trackStreamPipeNode = trackStreamPipeNode2;
                }
                else {
                    trackStreamPipeNode.c = trackStreamPipeNode2;
                    trackStreamPipeNode = trackStreamPipeNode2;
                }
                trackStreamPipeNode2.d = c;
                trackStreamPipeNode2.e = c + trackStreamPipeNode2.durationTimeUs();
                c = trackStreamPipeNode2.e;
                trackStreamPipeNode2.f = d;
                trackStreamPipeNode2.g = d + trackStreamPipeNode2.inputTrack().durationTimeUs();
                d = trackStreamPipeNode2.g;
                this.c.add(trackStreamPipeNode2);
            }
        }
        
        private void a(final AVTimeRange avTimeRange) {
            final List<TrackStreamPipeNode> c = this.c;
            long c2 = 0L;
            for (final TrackStreamPipeNode trackStreamPipeNode : c) {
                final AVTimeRange a = trackStreamPipeNode.a(avTimeRange);
                if (a == null) {
                    trackStreamPipeNode.b = false;
                }
                else {
                    trackStreamPipeNode.b = true;
                    final long durationUs = a.durationUs();
                    trackStreamPipeNode.setTimeRange(a);
                    trackStreamPipeNode.d = c2;
                    trackStreamPipeNode.e = c2 + durationUs;
                    c2 = trackStreamPipeNode.e;
                }
            }
            this.d = null;
            for (final TrackStreamPipeNode d : this.c) {
                if (d.b) {
                    this.d = d;
                    break;
                }
            }
            if (this.d == null) {
                this.a();
                this.d = this.c.get(0);
                TLog.e("Please set a valid cropping time.", new Object[0]);
            }
        }
        
        private boolean b() {
            if (this.d == null) {
                return false;
            }
            final TrackStreamPipeNode a = this.d.a();
            if (a == null) {
                return false;
            }
            a.reset();
            this.d = a;
            return true;
        }
        
        private void c() {
            this.d = null;
            final Iterator<TrackStreamPipeNode> iterator = this.c.iterator();
            while (iterator.hasNext()) {
                iterator.next().reset();
            }
            if (this.c.size() > 0) {
                this.d = this.c.get(0);
            }
        }
        
        private long d() {
            long n = 0L;
            for (final TrackStreamPipeNode trackStreamPipeNode : this.c) {
                n += (trackStreamPipeNode.b ? trackStreamPipeNode.durationTimeUs() : 0L);
            }
            return n;
        }
        
        private class TrackStreamPipeNode extends AVAssetTrackMediaExtractor
        {
            private boolean b;
            private TrackStreamPipeNode c;
            private long d;
            private long e;
            private long f;
            private long g;
            
            public TrackStreamPipeNode(final AVAssetTrack avAssetTrack) {
                super(avAssetTrack);
                this.b = true;
                this.d = -1L;
                this.e = -1L;
                this.f = -1L;
                this.g = -1L;
            }
            
            private TrackStreamPipeNode a() {
                if (this.b && this.c != null && this.c.b) {
                    return this.c;
                }
                return null;
            }
            
            private boolean a(final long n) {
                return n >= this.d && n <= this.e;
            }
            
            private long b(final long n) {
                return n - this.d + this.timeRange().startUs();
            }
            
            private long c(final long n) {
                return Math.max(0L, this.d + n - this.timeRange().startUs());
            }
            
            private AVTimeRange a(final AVTimeRange avTimeRange) {
                final AVTimeRange avTimeRangeMake = AVTimeRange.AVTimeRangeMake(this.f, this.g - this.f);
                if (this.f >= avTimeRange.endUs()) {
                    return null;
                }
                final long a = this.g - this.f;
                if (avTimeRange.startUs() <= this.f && avTimeRange.endUs() >= this.g) {
                    return AVTimeRange.AVTimeRangeMake(0L, a);
                }
                if (!avTimeRangeMake.containsTimeUs(avTimeRange.startUs()) && !avTimeRangeMake.containsTimeUs(avTimeRange.endUs())) {
                    return null;
                }
                final long max = Math.max(avTimeRange.startUs() - this.f, 0L);
                long n = Math.min(a, avTimeRange.endUs() - max);
                if (avTimeRange.endUs() <= this.g) {
                    n = Math.min(n, avTimeRange.endUs() - this.f - max);
                }
                final long min = Math.min(this.g - max, n);
                if (min <= 0L) {
                    return null;
                }
                return AVTimeRange.AVTimeRangeMake(max, min);
            }
        }
    }
}
