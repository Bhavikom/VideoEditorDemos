// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.mutablePlayer;

//import org.lasque.tusdk.core.utils.ThreadHelper;
import java.nio.Buffer;
import android.opengl.GLES20;
import java.nio.IntBuffer;
import java.nio.FloatBuffer;
//import org.lasque.tusdk.core.seles.sources.SelesSurfaceReceiver;
import android.graphics.Bitmap;
import android.view.Surface;
import android.graphics.SurfaceTexture;

import java.util.Iterator;
//import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
//import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCropBuilderImpl;
//import org.lasque.tusdk.core.utils.TLog;
import android.media.MediaFormat;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.egl.SelesEGLCoreSingleSurface;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCropBuilderImpl;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesSurfaceReceiver;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkMediaDataSource;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;

import java.util.ArrayList;
//import org.lasque.tusdk.core.struct.TuSdkMediaDataSource;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import java.util.List;
//import org.lasque.tusdk.core.seles.egl.SelesEGLCoreSingleSurface;

public class TuSdkVideoImageExtractor
{
    private SelesEGLCoreSingleSurface a;
    private List<AVAssetTrack> b;
    private AVAssetTrackOutputSouce c;
    private AVAssetTrackCodecDecoder d;
    private ImageSurfaceReceiver e;
    private TuSdkSize f;
    private float g;
    private int h;
    private AVMediaProcessQueue i;
    private TuSdkVideoImageExtractorListener j;
    private List<VideoImage> k;
    private AVAssetTrackSampleBufferOutput.AVAssetTrackSampleBufferInput l;
    
    public TuSdkVideoImageExtractor(final List<TuSdkMediaDataSource> list) {
        this.g = 0.0f;
        this.h = 21;
        this.k = new ArrayList<VideoImage>();
        this.l = new AVAssetTrackSampleBufferOutput.AVAssetTrackSampleBufferInput() {
            float a;
            
            @Override
            public void newFrameReady(final AVSampleBuffer avSampleBuffer) {
                if (avSampleBuffer.isDecodeOnly()) {
                    return;
                }
                if (TuSdkVideoImageExtractor.this.j == null || TuSdkVideoImageExtractor.this.k.size() >= TuSdkVideoImageExtractor.this.h) {
                    return;
                }
                if (avSampleBuffer.renderTimeUs() - this.a < 0.0f) {
                    return;
                }
                this.a = avSampleBuffer.renderTimeUs() + TuSdkVideoImageExtractor.this.getExtractFrameIntervalTimeUs();
                TuSdkVideoImageExtractor.this.e.setSurfaceTexTimestampNs(avSampleBuffer.renderTimeUs());
                synchronized (TuSdkVideoImageExtractor.this.k) {
                    try {
                        TuSdkVideoImageExtractor.this.k.wait(100L);
                    }
                    catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                TuSdkVideoImageExtractor.this.e.updateSurfaceTexImage();
            }
            
            @Override
            public void outputFormatChaned(final MediaFormat mediaFormat, final AVAssetTrack avAssetTrack) {
                TuSdkVideoImageExtractor.this.e.setInputRotation(avAssetTrack.orientation());
                TuSdkVideoImageExtractor.this.e.setInputSize(avAssetTrack.naturalSize());
            }
        };
        if (list == null || list.size() == 0) {
            TLog.e("Please set at least one video source.", new Object[0]);
            return;
        }
        this.b = new ArrayList<AVAssetTrack>(list.size());
        final Iterator<TuSdkMediaDataSource> iterator = list.iterator();
        while (iterator.hasNext()) {
            final List<AVAssetTrack> tracksWithMediaType = new AVAssetDataSource(iterator.next()).tracksWithMediaType(AVMediaType.AVMediaTypeVideo);
            if (tracksWithMediaType.size() == 0) {
                continue;
            }
            this.b.addAll(tracksWithMediaType);
        }
        if (this.b.size() == 0) {
            TLog.e("The input video source did not find the video tracks.", new Object[0]);
            return;
        }
        this.i = new AVMediaProcessQueue();
        (this.e = new ImageSurfaceReceiver()).setTextureCoordinateBuilder(new SelesVerticeCoordinateCropBuilderImpl(false));
        this.c = new AVAssetTrackPipeMediaExtractor(this.b);
        (this.d = new AVAssetTrackCodecDecoder(this.c)).addTarget(this.l);
        this.a();
    }
    
    private void a(final Runnable runnable) {
        this.i.runAsynchronouslyOnProcessingQueue(runnable);
    }
    
    private void a() {
        this.a(new Runnable() {
            @Override
            public void run() {
                TuSdkVideoImageExtractor.this.a = new SelesEGLCoreSingleSurface(null);
                TuSdkVideoImageExtractor.this.a.attachOffscreenSurface(TuSdkVideoImageExtractor.this.c.inputTrack().naturalSize().width, TuSdkVideoImageExtractor.this.c.inputTrack().naturalSize().height);
                TuSdkVideoImageExtractor.this.e.initInGLThread();
                TuSdkVideoImageExtractor.this.e.setOutputSize(TuSdkVideoImageExtractor.this.getOutputImageSize());
                TuSdkVideoImageExtractor.this.e.setInputRotation(TuSdkVideoImageExtractor.this.c.inputTrack().orientation());
                TuSdkVideoImageExtractor.this.e.setInputSize(TuSdkVideoImageExtractor.this.c.inputTrack().naturalSize());
                final SurfaceTexture requestSurfaceTexture = TuSdkVideoImageExtractor.this.e.requestSurfaceTexture();
                requestSurfaceTexture.setOnFrameAvailableListener((SurfaceTexture.OnFrameAvailableListener)new SurfaceTexture.OnFrameAvailableListener() {
                    public void onFrameAvailable(final SurfaceTexture surfaceTexture) {
                        synchronized (TuSdkVideoImageExtractor.this.k) {
                            TuSdkVideoImageExtractor.this.k.notify();
                        }
                        TuSdkVideoImageExtractor.this.a(new Runnable() {
                            @Override
                            public void run() {
                                TuSdkVideoImageExtractor.this.e.newFrameReadyInGLThread(0L);
                            }
                        });
                    }
                });
                TuSdkVideoImageExtractor.this.d.setOutputSurface(new Surface(requestSurfaceTexture));
            }
        });
    }
    
    public void extractImages() {
        this.a(new Runnable() {
            @Override
            public void run() {
                if (TuSdkVideoImageExtractor.this.d == null) {
                    TLog.i("%s : image extractor paused", this);
                    return;
                }
                if (TuSdkVideoImageExtractor.this.d.renderOutputBuffers()) {
                    TuSdkVideoImageExtractor.this.a(new Runnable() {
                        @Override
                        public void run() {
                            TuSdkVideoImageExtractor.this.b();
                        }
                    });
                }
            }
        });
    }
    
    private void b() {
        if (this.d == null) {
            TLog.i("%s : image extractor paused", this);
            return;
        }
        if (this.d.renderOutputBuffers()) {
            this.a(new Runnable() {
                @Override
                public void run() {
                    TuSdkVideoImageExtractor.this.b();
                }
            });
        }
        else {
            TLog.i("%s : image extractor done", this);
            this.d.reset();
            if (this.j != null) {
                if (this.k.size() < this.h && this.k.size() > 0) {
                    final VideoImage videoImage = this.k.get(this.k.size() - 1);
                    for (int size = this.k.size(), i = 0; i < this.h - size; ++i) {
                        final VideoImage videoImage2 = new VideoImage();
                        videoImage2.bitmap = videoImage.bitmap.copy(Bitmap.Config.ARGB_8888, false);
                        videoImage2.outputTimeUs = videoImage.outputTimeUs;
                        this.k.add(videoImage2);
                        this.j.onOutputFrameImage(videoImage2);
                    }
                }
                this.j.onImageExtractorCompleted(this.k);
            }
        }
    }
    
    public TuSdkVideoImageExtractor setImageListener(final TuSdkVideoImageExtractorListener j) {
        this.j = j;
        return this;
    }
    
    public TuSdkVideoImageExtractor setOutputImageSize(final TuSdkSize tuSdkSize) {
        this.f = tuSdkSize;
        if (this.e != null) {
            this.e.setOutputSize(tuSdkSize);
        }
        return this;
    }
    
    public TuSdkSize getOutputImageSize() {
        if (this.f == null) {
            return TuSdkSize.create(40, 40);
        }
        return this.f;
    }
    
    public TuSdkVideoImageExtractor setExtractFrameInterval(final float n) {
        this.g = n * 1000000.0f;
        return this;
    }
    
    public TuSdkVideoImageExtractor setExtractFrameCount(final int n) {
        if (this.c == null || n <= 0) {
            return this;
        }
        this.h = n + 2;
        return this;
    }
    
    public TuSdkVideoImageExtractor setOutputTimeRange(final long n, final long n2) {
        if (this.c == null) {
            return this;
        }
        this.c.setTimeRange(AVTimeRange.AVTimeRangeMake(n, n2 - n));
        this.g = 0.0f;
        return this;
    }
    
    public float getExtractFrameIntervalTimeUs() {
        if (this.c == null) {
            return 0.0f;
        }
        if (this.g == 0.0f) {
            this.g = this.c.durationTimeUs() / (this.h + 2.0f);
        }
        return this.g;
    }
    
    public void release() {
        this.i.runSynchronouslyOnProcessingQueue(new Runnable() {
            @Override
            public void run() {
                if (TuSdkVideoImageExtractor.this.d != null) {
                    TuSdkVideoImageExtractor.this.d.reset();
                }
                if (TuSdkVideoImageExtractor.this.e != null) {
                    TuSdkVideoImageExtractor.this.e.destroy();
                }
                TuSdkVideoImageExtractor.this.e = null;
                if (TuSdkVideoImageExtractor.this.a != null) {
                    TuSdkVideoImageExtractor.this.a.destroy();
                }
                TuSdkVideoImageExtractor.this.e = null;
            }
        });
        this.i.quit();
    }
    
    public interface TuSdkVideoImageExtractorListener
    {
        void onOutputFrameImage(final VideoImage p0);
        
        void onImageExtractorCompleted(final List<VideoImage> p0);
    }
    
    public class VideoImage
    {
        public Bitmap bitmap;
        public long outputTimeUs;
    }
    
    private class ImageSurfaceReceiver extends SelesSurfaceReceiver
    {
        @Override
        protected void renderToTexture(final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
            super.renderToTexture(floatBuffer, floatBuffer2);
            final int width = TuSdkVideoImageExtractor.this.getOutputImageSize().width;
            final int height = TuSdkVideoImageExtractor.this.getOutputImageSize().height;
            final IntBuffer allocate = IntBuffer.allocate(width * height);
            GLES20.glReadPixels(0, 0, width, height, 6408, 5121, (Buffer)allocate);
            final Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            bitmap.copyPixelsFromBuffer((Buffer)allocate);
            if (bitmap != null && TuSdkVideoImageExtractor.this.j != null) {
                final VideoImage videoImage = new VideoImage();
                videoImage.bitmap = bitmap;
                videoImage.outputTimeUs = TuSdkVideoImageExtractor.this.d.outputTimeUs();
                TuSdkVideoImageExtractor.this.k.add(videoImage);
                ThreadHelper.post(new Runnable() {
                    @Override
                    public void run() {
                        if (TuSdkVideoImageExtractor.this.k.size() > 1) {
                            TuSdkVideoImageExtractor.this.j.onOutputFrameImage(videoImage);
                        }
                    }
                });
            }
        }
    }
}
