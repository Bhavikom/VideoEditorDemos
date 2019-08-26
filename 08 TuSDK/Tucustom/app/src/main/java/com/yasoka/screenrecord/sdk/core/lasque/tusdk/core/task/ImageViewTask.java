// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.task;

import android.text.TextUtils;
//import org.lasque.tusdk.core.utils.ThreadHelper;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.utils.image.ImageLoaderHelper;
import android.widget.ImageView;
import java.util.Collection;
import android.graphics.Bitmap;
//import org.lasque.tusdk.core.utils.TLog;
import android.os.Looper;
import android.os.Handler;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageLoaderHelper;

import java.util.ArrayList;

public abstract class ImageViewTask<T extends ImageViewTaskWare>
{
    private final ArrayList<T> a;
    private Handler b;
    private boolean c;
    private T d;
    
    public ImageViewTask() {
        this.a = new ArrayList<T>();
        this.b = new Handler(Looper.getMainLooper());
    }
    
    public void resetQueues() {
        this.a.clear();
        TLog.d("%s resetQueues", this.getClass().getName());
    }
    
    @Override
    protected void finalize() {
        this.resetQueues();
        try {
            super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    
    protected abstract String getCacheKey(final T p0);
    
    protected abstract Bitmap asyncTaskLoadImage(final T p0);
    
    private ArrayList<T> a() {
        return new ArrayList<T>((Collection<? extends T>)this.a);
    }
    
    public void cancelLoadImage(final ImageView imageView) {
        if (imageView == null) {
            return;
        }
        if (this.d != null && this.d.isEqualView(imageView)) {
            this.d.cancel();
        }
        for (final ImageViewTaskWare o : this.a()) {
            if (o.isEqualView(imageView)) {
                o.cancel();
                this.a.remove(o);
            }
        }
    }
    
    public void loadImage(final T t) {
        if (t == null) {
            return;
        }
        this.cancelLoadImage(t.getImageView());
        final Bitmap loadMemoryBitmap = ImageLoaderHelper.loadMemoryBitmap(this.getCacheKey(t), null);
        if (loadMemoryBitmap != null) {
            t.imageLoaded(loadMemoryBitmap, ImageViewTaskWare.LoadType.TypeMomery);
            return;
        }
        this.submitTask(t);
    }
    
    public void submitTask(final T e) {
        if (e != null) {
            this.a.add(e);
        }
        if (this.c) {
            return;
        }
        this.d = this.b();
        if (this.d == null) {
            return;
        }
        this.c = true;
        ThreadHelper.runThread(new Runnable() {
            @Override
            public void run() {
                try {
                    ImageViewTask.this.a(ImageViewTask.this.d);
                }
                catch (Exception ex) {
                    TLog.e(ex, "ImageViewTask: %s", this.getClass());
                }
            }
        });
    }
    
    private void a(final T t) {
        final String cacheKey = this.getCacheKey(t);
        if (TextUtils.isEmpty((CharSequence)cacheKey)) {
            return;
        }
        Bitmap bitmap = ImageLoaderHelper.loadDiscBitmap(cacheKey);
        ImageViewTaskWare.LoadType loadType = ImageViewTaskWare.LoadType.TypeDisk;
        if (bitmap == null) {
            bitmap = this.asyncTaskLoadImage(t);
            loadType = ImageViewTaskWare.LoadType.TypeBuild;
        }
        this.a(t, bitmap, loadType);
        if (loadType == ImageViewTaskWare.LoadType.TypeDisk) {
            ImageLoaderHelper.saveToMemoryCache(cacheKey, bitmap, null);
        }
        else if (t.isSaveToDisk()) {
            ImageLoaderHelper.save(cacheKey, bitmap, t.getImageCompress());
        }
    }
    
    private void a(final T t, final Bitmap bitmap, final ImageViewTaskWare.LoadType loadType) {
        this.b.post((Runnable)new Runnable() {
            @Override
            public void run() {
                ImageViewTask.this.b(t, bitmap, loadType);
            }
        });
    }
    
    private void b(final T t, final Bitmap bitmap, final ImageViewTaskWare.LoadType loadType) {
        t.imageLoaded(bitmap, loadType);
        this.c = false;
        this.submitTask(null);
    }
    
    private T b() {
        if (this.a.size() == 0) {
            return null;
        }
        do {
            final ImageViewTaskWare imageViewTaskWare = this.a.remove(0);
            if (imageViewTaskWare == null || imageViewTaskWare.getImageView() == null || imageViewTaskWare.isCancel()) {
                continue;
            }
            return (T)imageViewTaskWare;
        } while (this.a.size() > 0);
        return null;
    }
}
