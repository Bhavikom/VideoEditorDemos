// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.video.lasque.tusdk.api.video.retriever;

import android.graphics.Rect;
import java.io.InputStream;
import android.graphics.BitmapFactory;
import java.io.ByteArrayInputStream;
import java.io.OutputStream;
import java.io.ByteArrayOutputStream;
//import org.lasque.tusdk.core.secret.StatisticsManger;
//import org.lasque.tusdk.core.utils.ThreadHelper;
import java.util.ArrayList;
import java.util.List;
//import org.lasque.tusdk.core.utils.image.BitmapHelper;
import android.graphics.Bitmap;
//import org.lasque.tusdk.core.TuSdkContext;
import android.text.TextUtils;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.video.editor.TuSdkTimeRange;
//import org.lasque.tusdk.core.common.TuSDKMediaDataSource;
import android.media.MediaMetadataRetriever;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.StatisticsManger;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.BitmapHelper;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.core.common.TuSDKMediaDataSource;
import com.yasoka.screenrecord.sdk.video.lasque.tusdk.video.editor.TuSdkTimeRange;

public class TuSDKVideoImageExtractor
{
    private MediaMetadataRetriever a;
    private TuSDKMediaDataSource b;
    private TuSdkTimeRange c;
    private int d;
    private int e;
    private TuSdkSize f;
    
    public TuSDKVideoImageExtractor() {
        this.f = TuSdkSize.create(80, 80);
    }
    
    public static TuSDKVideoImageExtractor createExtractor() {
        return new TuSDKVideoImageExtractor();
    }
    
    private MediaMetadataRetriever a() {
        if (this.a == null) {
            this.a = new MediaMetadataRetriever();
        }
        return this.a;
    }
    
    public TuSDKMediaDataSource getVideoDataSource() {
        return this.b;
    }
    
    public TuSDKVideoImageExtractor setVideoDataSource(final TuSDKMediaDataSource b) {
        this.b = b;
        return this;
    }
    
    public int getExtractFrameCount() {
        return this.d;
    }
    
    public TuSDKVideoImageExtractor setTimeRange(final TuSdkTimeRange c) {
        this.c = c;
        return this;
    }
    
    public TuSDKVideoImageExtractor setExtractFrameCount(final int d) {
        this.d = d;
        return this;
    }
    
    public TuSDKVideoImageExtractor setExtractFrameInterval(final int e) {
        this.e = e;
        return this;
    }
    
    public int getExtractFrameInterval() {
        return this.e;
    }
    
    public TuSDKVideoImageExtractor setOutputImageSize(final TuSdkSize f) {
        this.f = f;
        return this;
    }
    
    public TuSdkSize getOutputImageSize() {
        if (this.f == null) {
            this.f = new TuSdkSize(80, 80);
        }
        return this.f;
    }
    
    private boolean b() {
        if (this.a != null) {
            return true;
        }
        if (this.b == null || !this.b.isValid()) {
            TLog.e("please set video path", new Object[0]);
            return false;
        }
        try {
            if (!TextUtils.isEmpty((CharSequence)this.b.getFilePath())) {
                this.a().setDataSource(this.b.getFilePath());
            }
            else {
                this.a().setDataSource(TuSdkContext.context(), this.b.getFileUri());
            }
        }
        catch (Exception ex) {
            return false;
        }
        return true;
    }
    
    private void c() {
        if (this.a != null) {
            this.a.release();
        }
        this.a = null;
    }
    
    public Bitmap getFrameAtTime(final long n, final int n2) {
        if (!this.b()) {
            return null;
        }
        final Bitmap frameAtTime = this.a().getFrameAtTime(n);
        if (frameAtTime == null) {
            return null;
        }
        final Bitmap a = this.a(frameAtTime, n2);
        if (a.getWidth() == this.getOutputImageSize().width && a.getHeight() == this.getOutputImageSize().height) {
            return a;
        }
        final Bitmap imageScale = BitmapHelper.imageScale(a, this.getOutputImageSize().width, this.getOutputImageSize().height);
        BitmapHelper.recycled(a);
        return imageScale;
    }
    
    public List<Bitmap> extractImageList(final TuSDKVideoImageExtractorDelegate tuSDKVideoImageExtractorDelegate) {
        final ArrayList<Bitmap> list = new ArrayList<Bitmap>();
        if (this.getExtractFrameCount() <= 0 && this.getExtractFrameInterval() <= 0) {
            TLog.e("mExtractFrameCount and mExtractFrameInterval is invalid", new Object[0]);
            return list;
        }
        if (!this.b()) {
            return list;
        }
        final String metadata = this.a().extractMetadata(9);
        if (TextUtils.isEmpty((CharSequence)metadata)) {
            TLog.e("TuSDKVideoImageExtractor | Get media duration to fail, unable to extract bitmap", new Object[0]);
            return list;
        }
        final float float1 = Float.parseFloat(metadata);
        if (this.c == null || !this.c.isValid()) {
            this.c = TuSdkTimeRange.makeRange(0.0f, float1 / 1000.0f);
        }
        final float duration = this.c.duration();
        float n = (this.getExtractFrameCount() > 0) ? (duration / this.getExtractFrameCount()) : ((float)this.getExtractFrameInterval());
        if (n <= 0.0f) {
            n = 1.0f;
        }
        for (float startTime = this.c.getStartTime(); startTime < this.c.getEndTime(); startTime += n) {
            final Bitmap frameAtTime = this.getFrameAtTime((long)(startTime * 1000000.0f), 80);
            if (frameAtTime != null) {
                list.add(frameAtTime);
            }
            ThreadHelper.post((Runnable)new Runnable() {
                @Override
                public void run() {
                    tuSDKVideoImageExtractorDelegate.onVideoNewImageLoaded(frameAtTime);
                }
            });
        }
        this.c();
        StatisticsManger.appendComponent(9449477L);
        return list;
    }
    
    public void asyncExtractImageList(final TuSDKVideoImageExtractorDelegate tuSDKVideoImageExtractorDelegate) {
        if (tuSDKVideoImageExtractorDelegate == null) {
            return;
        }
        ThreadHelper.runThread((Runnable)new Runnable() {
            @Override
            public void run() {
                ThreadHelper.post((Runnable)new Runnable() {
                    final /* synthetic */ List a = TuSDKVideoImageExtractor.this.extractImageList(tuSDKVideoImageExtractorDelegate);
                    
                    @Override
                    public void run() {
                        tuSDKVideoImageExtractorDelegate.onVideoImageListDidLoaded(this.a);
                    }
                });
            }
        });
    }
    
    private Bitmap a(final Bitmap bitmap, final int n) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, n, (OutputStream)byteArrayOutputStream);
        return BitmapFactory.decodeStream((InputStream)new ByteArrayInputStream(byteArrayOutputStream.toByteArray()), (Rect)null, (BitmapFactory.Options)null);
    }
    
    public interface TuSDKVideoImageExtractorDelegate
    {
        void onVideoImageListDidLoaded(final List<Bitmap> p0);
        
        void onVideoNewImageLoaded(final Bitmap p0);
    }
}
