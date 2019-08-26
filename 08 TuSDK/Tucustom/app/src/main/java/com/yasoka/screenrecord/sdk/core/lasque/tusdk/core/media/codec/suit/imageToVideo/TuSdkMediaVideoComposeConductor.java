// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.suit.imageToVideo;

//import org.lasque.tusdk.core.struct.TuSdkSize;
import android.graphics.SurfaceTexture;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.media.codec.encoder.TuSdkMediaFileEncoder;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;

//import org.lasque.tusdk.core.media.codec.encoder.TuSdkMediaFileEncoder;
import java.util.LinkedList;

class TuSdkMediaVideoComposeConductor implements TuSdkMediaVideoComposProcesser.ComposProcesserListener
{
    private LinkedList<TuSdkComposeItem> a;
    private int b;
    private int c;
    private long d;
    private long e;
    private TuSdkMediaFileEncoder f;
    private TuSdkMediaVideoComposProcesser g;
    public TuSdkImageComposeItem mImageItem;
    private Object h;
    private boolean i;
    private boolean j;
    private boolean k;
    
    TuSdkMediaVideoComposeConductor() {
        this.b = 30;
        this.c = 0;
        this.d = 0L;
        this.e = 0L;
        this.h = new Object();
        this.j = true;
        this.k = false;
    }
    
    public void setItemList(final LinkedList<TuSdkComposeItem> a) {
        this.a = a;
        for (final TuSdkComposeItem tuSdkComposeItem : a) {
            ((TuSdkImageComposeItem)tuSdkComposeItem).alignTimeRange(this.e);
            this.e += ((TuSdkImageComposeItem)tuSdkComposeItem).getDurationUs();
        }
    }
    
    public void setMediaFileEncoder(final TuSdkMediaFileEncoder f) {
        this.f = f;
    }
    
    public void setComposProcesser(final TuSdkMediaVideoComposProcesser g) {
        this.g = g;
    }
    
    public void onFrameAvailable(final SurfaceTexture surfaceTexture) {
        if (this.i) {
            return;
        }
        synchronized (this.h) {
            this.d = (long)(1000000.0f / this.b * this.c);
            this.g.setCurrentFrameUs(this.d * 1000L);
            this.f.requestVideoRender(this.d * 1000L);
            if (this.j) {
                this.f.requestVideoKeyFrame();
            }
            if (this.d > this.e) {
                this.i = true;
                this.f.requestVideoKeyFrame();
                if (!this.k) {
                    this.f.requestVideoKeyFrame();
                    this.f.autoFillAudioMuteData(0L, this.e, true);
                    this.f.signalVideoEndOfInputStream();
                    this.k = true;
                }
            }
            ++this.c;
        }
    }
    
    public void run() {
        this.g.setComposProcesserListener(this);
        this.f.requestVideoRender(0L);
    }
    
    @Override
    public TuSdkImageComposeItem drawItemComposeItem() {
        synchronized(this.h) {
            if (this.a.size() == 0) {
                return this.mImageItem;
            } else {
                if (this.mImageItem == null || !this.mImageItem.isContainTimeRange(this.d)) {
                    this.mImageItem = (TuSdkImageComposeItem)this.a.removeFirst();
                    this.g.setInputSize(TuSdkSize.create(this.mImageItem.getImageBitmap()));
                }

                return this.mImageItem;
            }
        }
    }
    
    public float calculateProgress() {
        return this.d / (float)this.e;
    }
    
    public void setIsAllKeyFrame(final boolean j) {
        this.j = j;
    }
}
