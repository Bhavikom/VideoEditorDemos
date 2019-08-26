// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image;

import java.util.Iterator;

import android.annotation.SuppressLint;
import android.os.Message;
import android.os.Looper;
import java.lang.ref.WeakReference;
import android.os.Handler;
import java.util.Locale;
import android.graphics.BitmapShader;
import android.os.SystemClock;
import android.graphics.Canvas;
import android.graphics.Shader;
import android.graphics.Matrix;
import java.util.concurrent.TimeUnit;
import android.graphics.ColorFilter;
import android.net.Uri;
import android.content.ContentResolver;
import java.io.FileDescriptor;
import android.content.res.AssetFileDescriptor;
import java.io.File;
import android.util.TypedValue;
import java.io.IOException;
import android.content.res.Resources;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ScheduledFuture;
import android.graphics.RectF;
import android.graphics.Rect;
import android.graphics.Bitmap;
import android.graphics.Paint;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;

public final class TuSdkGifDrawable extends Drawable implements Animatable {
    final ScheduledThreadPoolExecutor a;
    volatile boolean b;
    private long e;
    private final Paint f;
    public final Bitmap mBuffer;
    final boolean c;
    public final GifHelper mGifHelper;
    private final Rect g;
    private final RectF h;
    private int i;
    private int j;
    final TuSdkGifDrawable.InvalidationHandler d;
    private final TuSdkGifDrawable.RenderTask k;
    private ScheduledFuture<?> l;
    private float m;
    private final ConcurrentLinkedQueue<TuSdkGifDrawable.TuGifAnimationListener> n;

    public static TuSdkGifDrawable createFromResource(Resources var0, int var1) {
        return new TuSdkGifDrawable(var0, var1);
    }

    public TuSdkGifDrawable(Resources var1, int var2) {
        this(var1.openRawResourceFd(var2));
        float var3 = this.a(var1, var2);
        this.j = (int)((float)this.mGifHelper.getHeight() * var3);
        this.i = (int)((float)this.mGifHelper.getWidth() * var3);
    }

    private float a(Resources var1, int var2) {
        TypedValue var3 = new TypedValue();
        var1.getValue(var2, var3, true);
        int var4 = var3.density;
        int var5;
        if (var4 == 0) {
            var5 = 160;
        } else if (var4 != 65535) {
            var5 = var4;
        } else {
            var5 = 0;
        }

        int var6 = var1.getDisplayMetrics().densityDpi;
        return var5 > 0 && var6 > 0 ? (float)var6 / (float)var5 : 1.0F;
    }

    public TuSdkGifDrawable(String var1) {
        this(GifHelper.parseFile(var1), (TuSdkGifDrawable)null, (ScheduledThreadPoolExecutor)null, true);
    }

    public TuSdkGifDrawable(File var1) {
        this(GifHelper.parseFile(var1.getPath()), (TuSdkGifDrawable)null, (ScheduledThreadPoolExecutor)null, true);
    }

    public TuSdkGifDrawable(AssetFileDescriptor var1) {
        this(GifHelper.openAssetFileDescriptor(var1), (TuSdkGifDrawable)null, (ScheduledThreadPoolExecutor)null, true);
    }

    public TuSdkGifDrawable(FileDescriptor var1) {
        this(GifHelper.parseFd(var1), (TuSdkGifDrawable)null, (ScheduledThreadPoolExecutor)null, true);
    }

    public TuSdkGifDrawable(ContentResolver var1, Uri var2) {
        this(GifHelper.openURI(var1, var2), (TuSdkGifDrawable)null, (ScheduledThreadPoolExecutor)null, true);
    }

    public TuSdkGifDrawable(GifHelper var1, TuSdkGifDrawable var2, ScheduledThreadPoolExecutor var3, boolean var4) {
        this.b = false;
        this.e = -9223372036854775808L;
        this.f = new Paint(6);
        this.g = new Rect();
        this.h = new RectF();
        this.k = new TuSdkGifDrawable.RenderTask(this);
        this.n = new ConcurrentLinkedQueue();
        this.c = var4;
        this.a = (ScheduledThreadPoolExecutor)(var3 != null ? var3 : GifRenderingExecutor.getInstance());
        this.mGifHelper = var1;
        Bitmap var5 = null;
        if (var2 != null) {
            synchronized(var2.mGifHelper) {
                if (!var2.mGifHelper.isRecycled() && var2.mGifHelper.getHeight() >= this.mGifHelper.getHeight() && var2.mGifHelper.getWidth() >= this.mGifHelper.getWidth()) {
                    var2.a();
                    var5 = var2.mBuffer;
                    var5.eraseColor(0);
                }
            }
        }

        if (var5 == null) {
            this.mBuffer = Bitmap.createBitmap(this.mGifHelper.getWidth(), this.mGifHelper.getHeight(), Bitmap.Config.ARGB_8888);
        } else {
            this.mBuffer = var5;
        }

        this.d = new TuSdkGifDrawable.InvalidationHandler(this);
        this.k.doWork();
        this.i = this.mGifHelper.getWidth();
        this.j = this.mGifHelper.getHeight();
    }

    public void recycle() {
        this.a();
        this.mBuffer.recycle();
    }

    private void a() {
        this.b = false;
        this.d.removeMessages(-1);
        this.mGifHelper.recycle();
    }

    public boolean isRecycled() {
        return this.mGifHelper.isRecycled();
    }

    public int getIntrinsicHeight() {
        return this.j;
    }

    public int getIntrinsicWidth() {
        return this.i;
    }

    public void setAlpha(int var1) {
        this.f.setAlpha(var1);
    }

    public void setColorFilter(ColorFilter var1) {
        this.f.setColorFilter(var1);
    }

    public ColorFilter getColorFilter() {
        return this.f.getColorFilter();
    }

    @SuppressLint("WrongConstant")
    public int getOpacity() {
        return -2;
    }

    public void start() {
        synchronized(this) {
            if (this.b) {
                return;
            }

            this.b = true;
        }

        long var1 = this.mGifHelper.restoreRemainder();
        this.a(var1);
    }

    private void a(long var1) {
        if (this.c) {
            this.e = 0L;
            this.d.sendEmptyMessageAtTime(-1, 0L);
        } else {
            this.b();
            this.l = this.a.schedule(this.k, Math.max(var1, 0L), TimeUnit.MILLISECONDS);
        }

    }

    public void reset() {
        this.a.execute(new TuSdkGifDrawable.SafeRunnable(this) {
            public void doWork() {
                if (TuSdkGifDrawable.this.mGifHelper.reset()) {
                    TuSdkGifDrawable.this.start();
                }

            }
        });
    }

    public void stop() {
        synchronized(this) {
            if (!this.b) {
                return;
            }

            this.b = false;
        }

        this.b();
        this.mGifHelper.saveRemainder();
    }

    private void b() {
        if (this.l != null) {
            this.l.cancel(false);
        }

        this.d.removeMessages(-1);
    }

    public boolean isRunning() {
        return this.b;
    }

    public int getLoopCount() {
        return this.mGifHelper.getLoopCount();
    }

    public void setLoopCount(int var1) {
        this.mGifHelper.setLoopCount(var1);
    }

    public int getNumberOfFrames() {
        return this.mGifHelper.getFrameCount();
    }

    public GifHelper.GifError getError() {
        return GifHelper.GifError.fromCode(this.mGifHelper.getErrorCode());
    }

    public void setSpeed(float var1) {
        this.mGifHelper.setSpeed(var1);
    }

    public void pause() {
        this.stop();
    }

    public int getDuration() {
        return this.mGifHelper.getDuration();
    }

    public int getCurrentPosition() {
        return this.mGifHelper.getCurrentPosition();
    }

    protected void onBoundsChange(Rect var1) {
        this.g.set(var1);
        this.h.set(this.g);
        Shader var2 = this.f.getShader();
        if (var2 != null) {
            Matrix var3 = new Matrix();
            var3.setTranslate(this.h.left, this.h.top);
            var3.preScale(this.h.width() / (float)this.mBuffer.getWidth(), this.h.height() / (float)this.mBuffer.getHeight());
            var2.setLocalMatrix(var3);
            this.f.setShader(var2);
        }

    }

    public void draw(Canvas var1) {
        if (this.f.getShader() == null) {
            Rect var2 = new Rect(0, 0, this.mGifHelper.getWidth(), this.mGifHelper.getHeight());
            var1.drawBitmap(this.mBuffer, var2, this.g, this.f);
        } else {
            var1.drawRoundRect(this.h, this.m, this.m, this.f);
        }

        if (this.c && this.b && this.e != -9223372036854775808L) {
            long var4 = Math.max(0L, this.e - SystemClock.uptimeMillis());
            this.e = -9223372036854775808L;
            this.a.remove(this.k);
            this.l = this.a.schedule(this.k, var4, TimeUnit.MILLISECONDS);
        }

    }

    public int getAlpha() {
        return this.f.getAlpha();
    }

    @Deprecated
    public void setDither(boolean var1) {
        this.f.setDither(var1);
        this.invalidateSelf();
    }

    public void addAnimationListener(TuSdkGifDrawable.TuGifAnimationListener var1) {
        this.n.add(var1);
    }

    public boolean removeAnimationListener(TuSdkGifDrawable.TuGifAnimationListener var1) {
        return this.n.remove(var1);
    }

    public boolean setVisible(boolean var1, boolean var2) {
        boolean var3 = super.setVisible(var1, var2);
        if (!this.c) {
            if (var1) {
                if (var2) {
                    this.reset();
                }

                if (var3) {
                    this.start();
                }
            } else if (var3) {
                this.stop();
            }
        }

        return var3;
    }

    public int getCurrentFrameIndex() {
        return this.mGifHelper.getCurrentFrameIndex();
    }

    public int getCurrentLoop() {
        int var1 = this.mGifHelper.getCurrentLoop();
        return var1 != 0 && var1 >= this.mGifHelper.getLoopCount() ? var1 - 1 : var1;
    }

    public boolean isAnimationCompleted() {
        return this.mGifHelper.isAnimationCompleted();
    }

    public int getFrameDuration(int var1) {
        return var1 >= 0 && var1 <= this.getNumberOfFrames() ? this.mGifHelper.getFrameDuration(var1) : 0;
    }

    public void setCornerRadius(float var1) {
        this.m = var1;
        BitmapShader var2;
        if (var1 > 0.0F) {
            var2 = new BitmapShader(this.mBuffer, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        } else {
            var2 = null;
        }

        this.f.setShader(var2);
    }

    public float getCornerRadius() {
        return this.m;
    }

    public String toString() {
        return String.format(Locale.US, "GIF: size: %dx%d, frames: %d, error: %d", this.mGifHelper.getWidth(), this.mGifHelper.getHeight(), this.mGifHelper.getFrameCount(), this.mGifHelper.getErrorCode());
    }

    private class InvalidationHandler extends Handler {
        private final WeakReference<TuSdkGifDrawable> b;

        public InvalidationHandler(TuSdkGifDrawable var2) {
            super(Looper.getMainLooper());
            this.b = new WeakReference(var2);
        }

        public void handleMessage(Message var1) {
            TuSdkGifDrawable var2 = (TuSdkGifDrawable)this.b.get();
            if (var2 != null) {
                if (var1.what == -1) {
                    var2.invalidateSelf();
                } else {
                    Iterator var3 = var2.n.iterator();

                    while(var3.hasNext()) {
                        TuSdkGifDrawable.TuGifAnimationListener var4 = (TuSdkGifDrawable.TuGifAnimationListener)var3.next();
                        var4.onGifAnimationCompleted(var1.what);
                    }
                }

            }
        }
    }

    private class RenderTask extends TuSdkGifDrawable.SafeRunnable {
        public RenderTask(TuSdkGifDrawable var2) {
            super(var2);
        }

        public final void doWork() {
            long var1 = this.mGifDrawable.mGifHelper.renderFrame(this.mGifDrawable.mBuffer);
            if (var1 >= 0L) {
                this.mGifDrawable.e = SystemClock.uptimeMillis() + var1;
                if (this.mGifDrawable.isVisible() && this.mGifDrawable.b && !this.mGifDrawable.c) {
                    this.mGifDrawable.a.remove(this);
                    this.mGifDrawable.l = this.mGifDrawable.a.schedule(this, var1, TimeUnit.MILLISECONDS);
                }

                if (!this.mGifDrawable.n.isEmpty() && this.mGifDrawable.getCurrentFrameIndex() == this.mGifDrawable.mGifHelper.getFrameCount() - 1) {
                    this.mGifDrawable.d.sendEmptyMessageAtTime(this.mGifDrawable.getCurrentLoop(), this.mGifDrawable.e);
                }
            } else {
                this.mGifDrawable.e = -9223372036854775808L;
                this.mGifDrawable.b = false;
            }

            if (this.mGifDrawable.isVisible() && !this.mGifDrawable.d.hasMessages(-1)) {
                this.mGifDrawable.d.sendEmptyMessageAtTime(-1, 0L);
            }

        }
    }

    private abstract class SafeRunnable implements Runnable {
        protected final TuSdkGifDrawable mGifDrawable;

        SafeRunnable(TuSdkGifDrawable var2) {
            this.mGifDrawable = var2;
        }

        public final void run() {
            try {
                if (!this.mGifDrawable.isRecycled()) {
                    this.doWork();
                }
            } catch (Throwable var3) {
                Thread.UncaughtExceptionHandler var2 = Thread.getDefaultUncaughtExceptionHandler();
                if (var2 != null) {
                    var2.uncaughtException(Thread.currentThread(), var3);
                }
            }

        }

        abstract void doWork();
    }

    public interface TuGifAnimationListener {
        void onGifAnimationCompleted(int var1);
    }
}
