// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.decoder;

//import org.lasque.tusdk.core.utils.TLog;
import java.util.LinkedList;
import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
//import org.lasque.tusdk.core.common.TuSDKMediaDataSource;
//import org.lasque.tusdk.core.common.TuSDKAVPacket;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import android.annotation.TargetApi;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common.TuSDKAVPacket;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common.TuSDKMediaDataSource;

@TargetApi(16)
public class TuSDKMoviePacketReader extends TuSDKMovieReader
{
    private ReadMode a;
    protected long mCurrentSampleTime;
    private boolean b;
    private TuSDKMovieReaderPacketDelegate c;
    private VideoPacketBufferProducer d;
    private PacketBufferConsumer e;
    private ThreadPoolExecutor f;
    private LinkedBlockingDeque<TuSDKAVPacket> g;
    private TuSDKVideoTimeEffectController h;
    
    public TuSDKMoviePacketReader(final TuSDKMediaDataSource tuSDKMediaDataSource) {
        super(tuSDKMediaDataSource);
        this.a = ReadMode.SequenceMode;
        this.mCurrentSampleTime = 0L;
        this.d = new VideoPacketBufferProducer();
        this.e = new PacketBufferConsumer();
        this.f = this.a();
        this.g = new LinkedBlockingDeque<TuSDKAVPacket>();
        this.h = TuSDKVideoTimeEffectController.create(TuSDKVideoTimeEffectController.TimeEffectMode.NoMode);
    }
    
    private ThreadPoolExecutor a() {
        return new ThreadPoolExecutor(2, 2, 2L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(2));
    }
    
    public void setDelegate(final TuSDKMovieReaderPacketDelegate c) {
        this.c = c;
    }
    
    public void setReadMode(final ReadMode a) {
        if (a == ReadMode.ReverseMode) {
            this.h.setTimeEffectMode(TuSDKVideoTimeEffectController.TimeEffectMode.NoMode);
        }
        if (this.a == a) {
            return;
        }
        this.a = a;
        if (!this.g.isEmpty()) {
            this.d.seekToTimeUs(this.g.getFirst().getSampleTimeUs());
        }
        this.g.clear();
    }
    
    public ReadMode getReadMode() {
        return this.a;
    }
    
    public void setTimeEffectController(final TuSDKVideoTimeEffectController h) {
        this.h = h;
        this.d.seekToTimeUs(0L);
        this.g.clear();
    }
    
    @Override
    public void seekTo(final long n, final int n2) {
        super.seekTo(n, n2);
        this.d.seekToTimeUs(n);
    }
    
    @Override
    public long getSampleTime() {
        if (this.mMediaExtractor == null) {
            return 0L;
        }
        return this.mCurrentSampleTime;
    }
    
    @Override
    public int readSampleData(final ByteBuffer byteBuffer, final int n) {
        throw new RuntimeException("Please call start medthod");
    }
    
    public void setReadAudioPacketEnable(final boolean b) {
        this.b = b;
    }
    
    protected synchronized TuSDKAVPacket readSamplePacket(final int n) {
        if (this.mMediaExtractor == null) {
            return null;
        }
        final TuSDKAVPacket tuSDKAVPacket = new TuSDKAVPacket(n);
        final int sampleData = this.mMediaExtractor.readSampleData(tuSDKAVPacket.getByteBuffer(), 0);
        if (sampleData <= 0) {
            return null;
        }
        tuSDKAVPacket.setSampleTimeUs(this.mMediaExtractor.getSampleTime());
        tuSDKAVPacket.setFlags(this.mMediaExtractor.getSampleFlags());
        tuSDKAVPacket.setChunkSize(sampleData);
        tuSDKAVPacket.setPacketType(this.isVideoSampleTrackIndex() ? 1 : 2);
        this.advance();
        return tuSDKAVPacket;
    }
    
    public void start() {
        this.g.clear();
        this.d.start();
        this.e.start();
    }
    
    public void stop() {
        if (this.d != null) {
            this.d.stop();
        }
        if (this.e != null) {
            this.e.stop();
        }
        this.mCurrentSampleTime = 0L;
        try {
            if (this.f != null) {
                this.f.awaitTermination(200L, TimeUnit.MILLISECONDS);
            }
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        if (this.g != null) {
            this.g.clear();
        }
    }
    
    @Override
    protected void destroy() {
        super.destroy();
        this.stop();
    }
    
    public class VideoPacketBufferProducer
    {
        private long b;
        private long c;
        private long d;
        private boolean e;
        private Runnable f;
        
        public VideoPacketBufferProducer() {
            this.b = 50L;
            this.c = 2000000L;
            this.d = -1L;
            this.e = false;
        }
        
        public void seekToTimeUs(final long a) {
            this.d = Math.min(a, TuSDKMoviePacketReader.this.getVideoInfo().durationTimeUs);
            TuSDKMoviePacketReader.this.g.clear();
        }
        
        private void a() {
            TuSDKMoviePacketReader.this.g.clear();
            if (this.d <= 0L) {
                if (TuSDKMoviePacketReader.this.getReadMode() == ReadMode.ReverseMode) {
                    this.d = Math.min(TuSDKMoviePacketReader.this.getTimeRange().getEndTimeUS(), TuSDKMoviePacketReader.this.getVideoInfo().durationTimeUs);
                }
                else {
                    this.d = Math.min(TuSDKMoviePacketReader.this.getTimeRange().getStartTimeUS(), TuSDKMoviePacketReader.this.getVideoInfo().durationTimeUs);
                }
            }
        }
        
        public boolean isFinsihed() {
            return !this.e;
        }
        
        private boolean b() {
            return TuSDKMoviePacketReader.this.g.size() < this.b;
        }
        
        private synchronized boolean c() {
            if (!this.e) {
                return true;
            }
            TuSDKMoviePacketReader.this.selectVideoTrack();
            if (!this.b()) {
                try {
                    Thread.sleep(500L);
                }
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                return false;
            }
            LinkedList<TuSDKAVPacket> c;
            if (TuSDKMoviePacketReader.this.a == ReadMode.ReverseMode) {
                c = this.a(this.d - this.c, this.d, true);
            }
            else {
                c = this.a(this.d, this.d + this.c, false);
            }
            if (this.d == 0L) {
                TuSDKMoviePacketReader.this.h.reset();
            }
            if (c != null && c.size() > 0) {
                if (TuSDKMoviePacketReader.this.h.getTimeEffectMode() != TuSDKVideoTimeEffectController.TimeEffectMode.NoMode) {
                    TuSDKMoviePacketReader.this.h.doPacketTimeEffectExtract(c);
                }
                this.d = c.getLast().getSampleTimeUs();
                TuSDKMoviePacketReader.this.g.addAll(c);
                return (TuSDKMoviePacketReader.this.a == ReadMode.ReverseMode && this.d == 0L) || c.size() == 1;
            }
            return true;
        }
        
        private void d() {
            if (!this.e) {
                return;
            }
            TuSDKMoviePacketReader.this.selectAudioTrack();
            TuSDKMoviePacketReader.this.mMediaExtractor.seekTo(TuSDKMoviePacketReader.this.getTimeRange().getStartTimeUS(), 2);
            while (this.e) {
                final TuSDKAVPacket samplePacket = TuSDKMoviePacketReader.this.readSamplePacket(262144);
                if (samplePacket == null) {
                    break;
                }
                if (TuSDKMoviePacketReader.this.c == null) {
                    continue;
                }
                TuSDKMoviePacketReader.this.c.onAVPacketAvailable(samplePacket);
            }
        }
        
        private LinkedList<TuSDKAVPacket> a(long max, long min, final boolean b) {
            final LinkedList<TuSDKAVPacket> list = new LinkedList<TuSDKAVPacket>();
            max = Math.max(max, TuSDKMoviePacketReader.this.getTimeRange().getStartTimeUS());
            min = Math.min(min, TuSDKMoviePacketReader.this.getTimeRange().getEndTimeUS());
            if (TuSDKMoviePacketReader.this.mMediaExtractor == null || min <= max || min - max < 500L) {
                TLog.e("can't extract video for startTimeUs : %d  and  endTimeUs : %d ", new Object[] { max, min });
                return list;
            }
            TuSDKMoviePacketReader.this.selectVideoTrack();
            TuSDKMoviePacketReader.this.mMediaExtractor.seekTo(max, 0);
            while (TuSDKMoviePacketReader.this.mMediaExtractor.getSampleTime() < max && TuSDKMoviePacketReader.this.mMediaExtractor.getSampleTime() != -1L) {
                TuSDKMoviePacketReader.this.advance();
            }
            try {
                while (TuSDKMoviePacketReader.this.mMediaExtractor.getSampleTime() >= max && TuSDKMoviePacketReader.this.mMediaExtractor.getSampleTime() <= min) {
                    final TuSDKAVPacket samplePacket = TuSDKMoviePacketReader.this.readSamplePacket(TuSDKMoviePacketReader.this.getVideoTrackFormat().getInteger("max-input-size"));
                    if (samplePacket == null) {
                        break;
                    }
                    if (b) {
                        list.addFirst(samplePacket);
                    }
                    else {
                        list.addLast(samplePacket);
                    }
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
            return list;
        }
        
        public void start() {
            if (this.e) {
                return;
            }
            this.e = true;
            this.a();
            TuSDKMoviePacketReader.this.f.execute(this.f = new Runnable() {
                @Override
                public void run() {
                    boolean b;
                    for (b = false; VideoPacketBufferProducer.this.e && !b; b = VideoPacketBufferProducer.this.c()) {}
                    if (b && VideoPacketBufferProducer.this.e && TuSDKMoviePacketReader.this.b) {
                        VideoPacketBufferProducer.this.d();
                    }
                    VideoPacketBufferProducer.this.stop();
                }
            });
        }
        
        public void stop() {
            this.e = false;
            this.d = -1L;
            TuSDKMoviePacketReader.this.f.remove(this.f);
            TuSDKMoviePacketReader.this.unselectVideoTrack();
        }
    }
    
    public enum ReadMode
    {
        SequenceMode, 
        ReverseMode;
    }
    
    public interface TuSDKMovieReaderPacketDelegate
    {
        void onAVPacketAvailable(final TuSDKAVPacket p0);
        
        void onReadComplete();
    }
    
    public class PacketBufferConsumer
    {
        private boolean b;
        private Runnable c;
        
        public PacketBufferConsumer() {
            this.b = false;
        }
        
        public void start() {
            if (this.b) {
                return;
            }
            this.b = true;
            TuSDKMoviePacketReader.this.g.clear();
            TuSDKMoviePacketReader.this.f.execute(this.c = new Runnable() {
                @Override
                public void run() {
                    while (PacketBufferConsumer.this.b) {
                        final TuSDKAVPacket takePacket = PacketBufferConsumer.this.takePacket();
                        if (takePacket != null) {
                            TuSDKMoviePacketReader.this.mCurrentSampleTime = takePacket.getSampleTimeUs();
                            if (TuSDKMoviePacketReader.this.c != null) {
                                TuSDKMoviePacketReader.this.c.onAVPacketAvailable(takePacket);
                            }
                        }
                        if (TuSDKMoviePacketReader.this.g.isEmpty() && TuSDKMoviePacketReader.this.d.isFinsihed()) {
                            PacketBufferConsumer.this.stop();
                            if (TuSDKMoviePacketReader.this.c != null) {
                                TuSDKMoviePacketReader.this.c.onReadComplete();
                                break;
                            }
                            break;
                        }
                    }
                }
            });
        }
        
        public void stop() {
            this.b = false;
            TuSDKMoviePacketReader.this.f.remove(this.c);
        }
        
        public TuSDKAVPacket takePacket() {
            try {
                return TuSDKMoviePacketReader.this.g.take();
            }
            catch (InterruptedException ex) {
                ex.printStackTrace();
                return null;
            }
        }
    }
}
