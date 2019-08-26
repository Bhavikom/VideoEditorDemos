// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.components.widget.smudge;

import android.graphics.Matrix;
import android.graphics.Xfermode;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.ColorMatrix;
//import org.lasque.tusdk.core.utils.hardware.TuSdkGPU;
//import org.lasque.tusdk.modules.view.widget.smudge.BrushSize;
//import org.lasque.tusdk.core.utils.image.BitmapHelper;
//import org.lasque.tusdk.modules.view.widget.smudge.BrushData;
//import org.lasque.tusdk.core.utils.ThreadHelper;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.FilterWrap;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkGPU;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.BitmapHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.smudge.BrushData;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.smudge.BrushSize;
//import org.lasque.tusdk.core.seles.tusdk.FilterWrap;

public final class FilterSmudgeProcessor extends SimpleProcessor
{
    private FilterWrap a;
    private Canvas b;
    private Bitmap c;
    
    @Override
    protected void init(final Bitmap bitmap, final Bitmap bitmap2, final int n) {
        super.init(this.c, bitmap2, n);
        ThreadHelper.runThread(new Runnable() {
            @Override
            public void run() {
                FilterSmudgeProcessor.this.a(FilterSmudgeProcessor.this.b());
            }
        });
        this.a();
        this.b = new Canvas(this.brushSnap);
    }
    
    private void a(final Bitmap bitmap) {
        ThreadHelper.post(new Runnable() {
            @Override
            public void run() {
                FilterSmudgeProcessor.this.c = bitmap;
            }
        });
    }
    
    private void a() {
        final BrushData create = BrushData.create(-1L, "", "");
        create.setType(BrushData.BrushType.TypeMosaic);
        this.mCurrentBrush = create;
        this.brushSnap = BitmapHelper.createOvalImage(72, 72, -16777216);
    }
    
    @Override
    protected void setBrush(final BrushData brushData) {
    }
    
    @Override
    protected void setBrushSize(final BrushSize.SizeType sizeType) {
        if (sizeType == null) {
            return;
        }
        this.mBrushScale = (float)(int)((BrushSize.getBrushValue(sizeType) * 20.0 - 1.0) * 48.0);
    }
    
    protected FilterWrap getFilterWrap() {
        return this.a;
    }
    
    protected final void setFilterWrap(final FilterWrap a) {
        this.a = a;
    }
    
    private Bitmap b() {
        final FilterWrap filterWrap = this.getFilterWrap();
        if (filterWrap == null) {
            return null;
        }
        Bitmap bitmap = filterWrap.process(this.originalSnap);
        if (TuSdkGPU.lowPerformance()) {
            bitmap = this.b(bitmap);
        }
        return bitmap;
    }
    
    private Bitmap b(final Bitmap bitmap) {
        final Bitmap bitmap2 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap2);
        final ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.set(new float[] { 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 255.0f });
        final Paint paint = new Paint();
        paint.setColorFilter((ColorFilter)new ColorMatrixColorFilter(colorMatrix));
        paint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff.Mode.SRC));
        canvas.drawBitmap(bitmap, new Matrix(), paint);
        return bitmap2;
    }
    
    @Override
    protected float getMaxTemplateDistance(final float n) {
        return n * 0.1f;
    }
    
    @Override
    protected void drawAtPoint(final float n, final float n2, final float n3, final float n4, final float n5) {
        if (this.c == null) {
            return;
        }
        if (n >= 0.0f && n < this.getImageWidth() && n2 >= 0.0f && n2 < this.getImageHeight()) {
            this.a(n, n2, n4);
        }
    }
    
    private void a(float a, float a2, final float n) {
        final int width = this.originalSnap.getWidth();
        final int height = this.originalSnap.getHeight();
        if (a >= 0.0f && a < width && a2 >= 0.0f && a2 < height) {
            a = (float)Math.round(a);
            a2 = (float)Math.round(a2);
            final float n2 = this.brushSnap.getWidth() * n;
            final Matrix matrix = new Matrix();
            matrix.postScale(1.0f / n, 1.0f / n);
            matrix.postTranslate((n2 / 2.0f - a) / n, (n2 / 2.0f - a2) / n);
            final Paint paint = new Paint();
            paint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff.Mode.SRC_ATOP));
            this.b.drawBitmap(this.c, matrix, paint);
            final Matrix matrix2 = new Matrix();
            matrix2.postScale(n, n);
            matrix2.postTranslate(a - n2 / 2.0f, a2 - n2 / 2.0f);
            final Paint paint2 = new Paint();
            paint2.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
            this.smudgeCanvas.drawBitmap(this.brushSnap, matrix2, paint2);
        }
    }
    
    @Override
    protected void destroy() {
        super.destroy();
        BitmapHelper.recycled(this.c);
    }
}
