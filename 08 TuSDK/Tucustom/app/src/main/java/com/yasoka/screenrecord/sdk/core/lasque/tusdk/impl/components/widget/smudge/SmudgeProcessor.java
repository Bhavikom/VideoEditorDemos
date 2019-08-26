// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.components.widget.smudge;

import android.graphics.ColorFilter;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Xfermode;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff;
import android.graphics.Paint;
import android.graphics.Matrix;
//import org.lasque.tusdk.core.utils.image.BitmapHelper;
//import org.lasque.tusdk.modules.view.widget.smudge.BrushData;
import android.graphics.Bitmap;
import android.graphics.Canvas;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.BitmapHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.smudge.BrushData;

public final class SmudgeProcessor extends SimpleProcessor
{
    private Canvas a;
    private Bitmap b;
    
    @Override
    protected void setBrush(final BrushData brush) {
        if (brush == null) {
            return;
        }
        super.setBrush(brush);
        BitmapHelper.recycled(this.b);
        if (brush.getType() == BrushData.BrushType.TypeMosaic && this.originalSnap != null) {
            this.a = new Canvas(this.brushSnap);
            this.b = this.a(this.originalSnap);
        }
    }
    
    @Override
    protected final void drawAtPoint(final float n, final float n2, final float n3, final float n4, final float n5) {
        final float n6 = 1.0f - 0.5f / (1.0f + n3 / this.mBrushScale);
        if (n >= 0.0f && n < this.getImageWidth() && n2 >= 0.0f && n2 < this.getImageHeight()) {
            final BrushData.BrushType type = this.getBrush().getType();
            if (type == BrushData.BrushType.TypeEraser) {
                this.b(n, n2, n4);
            }
            else if (type == BrushData.BrushType.TypeMosaic) {
                this.a(n, n2, n4);
            }
            else {
                this.a(n, n2, n4, n6, n5);
            }
        }
    }
    
    private void a(float a, float a2, final float n) {
        final int width = this.originalSnap.getWidth();
        final int height = this.originalSnap.getHeight();
        if (a >= 0.0f && a < width && a2 >= 0.0f && a2 < height) {
            a = (float)Math.round(a);
            a2 = (float)Math.round(a2);
            final float n2 = this.brushSnap.getWidth() * n;
            final int n3 = width / 30;
            final int n4 = height / 30;
            final Matrix matrix = new Matrix();
            matrix.postScale((width + 1.0f) / n3 / n, (height + 1.0f) / n4 / n);
            matrix.postTranslate((n2 / 2.0f - a) / n, (n2 / 2.0f - a2) / n);
            final Paint paint = new Paint();
            paint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            this.a.drawBitmap(this.b, matrix, paint);
            final Matrix matrix2 = new Matrix();
            matrix2.postScale(n, n);
            matrix2.postTranslate(a - n2 / 2.0f, a2 - n2 / 2.0f);
            final Paint paint2 = new Paint();
            paint2.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
            this.smudgeCanvas.drawBitmap(this.brushSnap, matrix2, paint2);
        }
    }
    
    private void b(final float n, final float n2, final float n3) {
        final int width = this.originalSnap.getWidth();
        final int height = this.originalSnap.getHeight();
        if (n >= 0.0f && n < width && n2 >= 0.0f && n2 < height) {
            final int n4 = (int)(this.brushSnap.getWidth() * n3);
            final Matrix matrix = new Matrix();
            matrix.postScale(n3, n3);
            matrix.postTranslate(n - n4 / 2, n2 - n4 / 2);
            final Paint paint = new Paint();
            paint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
            this.smudgeCanvas.drawBitmap(this.brushSnap, matrix, paint);
        }
    }
    
    private void a(double n, double n2, double n3, double n4, double n5) {
        final BrushData brush = this.getBrush();
        final BrushData.RotateType rotateType = brush.getRotateType();
        if (rotateType == BrushData.RotateType.RotateRandom) {
            n5 = Math.random() * 360.0;
        }
        else if (rotateType == BrushData.RotateType.RotateLimitRandom) {
            n5 += (Math.random() - 0.5) * 60.0;
        }
        else if (rotateType == BrushData.RotateType.RotateNone) {
            n5 = 0.0;
        }
        if (brush.getPositionType() == BrushData.PositionType.PositionRandom) {
            final double n6 = this.brushSnap.getWidth() * n3;
            final double n7 = n6 * (Math.random() - 0.5);
            final double n8 = n6 * (Math.random() - 0.5);
            n += n7;
            n2 += n8;
        }
        if (brush.getSizeType() == BrushData.SizeType.SizeRandom) {
            final double random = Math.random();
            n3 *= random;
            n4 *= 1.0 - 0.25 * random;
        }
        this.b(n, n2, n3, n5, n4);
    }
    
    private void b(final double n, final double n2, final double n3, final double n4, final double n5) {
        final int width = this.originalSnap.getWidth();
        final int height = this.originalSnap.getHeight();
        if (n >= 0.0 && n < width && n2 >= 0.0 && n2 < height) {
            final int n6 = (int)(this.brushSnap.getWidth() * n3);
            final int pixel = this.originalSnap.getPixel((int)n, (int)n2);
            final Matrix matrix = new Matrix();
            matrix.postScale((float)n3, (float)n3);
            matrix.postTranslate((float)(-n6 / 2), (float)(-n6 / 2));
            matrix.postRotate((float)n4);
            matrix.postTranslate((float)n, (float)n2);
            final Paint paint = new Paint();
            final PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(pixel, PorterDuff.Mode.SRC_ATOP);
            paint.setAlpha((int)(n5 * 255.0));
            paint.setColorFilter((ColorFilter)colorFilter);
            paint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
            this.smudgeCanvas.drawBitmap(this.brushSnap, matrix, paint);
        }
    }
    
    private Bitmap a(final Bitmap bitmap) {
        return Bitmap.createScaledBitmap(bitmap, bitmap.getWidth() / 30, bitmap.getHeight() / 30, true);
    }
    
    public void destroy() {
        super.destroy();
        BitmapHelper.recycled(this.b);
    }
}
