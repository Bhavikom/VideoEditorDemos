// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.components.widget.paintdraw;

import android.graphics.Bitmap;
//import org.lasque.tusdk.modules.view.widget.smudge.BrushSize;
import android.graphics.PointF;
import android.graphics.Path;
import android.graphics.Paint;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.components.widget.smudge.SimpleProcessor;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.smudge.BrushSize;
//import org.lasque.tusdk.impl.components.widget.smudge.SimpleProcessor;

public class PaintDrawProcessor extends SimpleProcessor
{
    private Paint.Cap a;
    private Paint.Join b;
    private int c;
    private float d;
    private Path e;
    private Paint f;
    private PointF g;
    private BrushSize.SizeType h;
    private float i;
    
    public void setMinDistance(final float n) {
        this.i = ((n < 10.0f) ? 10.0f : ((n > 20.0f) ? 20.0f : n));
    }
    
    @Override
    protected void drawAtPoint(final float n, final float n2, final float n3, final float n4, final float n5) {
        throw new UnsupportedOperationException();
    }
    
    @Override
    protected Bitmap getSmudgeImage(final Bitmap bitmap, final boolean b) {
        return super.getSmudgeImage(bitmap, b);
    }
    
    @Override
    protected void destroy() {
        super.destroy();
    }
    
    public void setPaintCap(final Paint.Cap a) {
        this.a = a;
        this.b();
    }
    
    public void setPaintJoin(final Paint.Join b) {
        this.b = b;
        this.b();
    }
    
    public void setPaintColor(final int c) {
        this.c = c;
        this.b();
    }
    
    public PaintDrawProcessor() {
        this.a = Paint.Cap.ROUND;
        this.b = Paint.Join.ROUND;
        this.c = -16777216;
        this.d = 0.0f;
        this.h = BrushSize.SizeType.MediumBrush;
        this.i = 10.0f;
        this.mBrushScale = 3.0f;
    }
    
    @Override
    protected void init(final Bitmap bitmap, final Bitmap bitmap2, final int n) {
        super.init(bitmap, bitmap2, n);
        this.b();
    }
    
    @Override
    protected int getImageWidth() {
        return super.getImageWidth();
    }
    
    @Override
    protected int getImageHeight() {
        return super.getImageHeight();
    }
    
    public void setBrushSize(final BrushSize.SizeType sizeType) {
        this.setBrushSize(sizeType, this.mBrushScale);
    }
    
    public void setBrushScale(final float n) {
        if (n < 1.0f || n > 3.0f) {
            return;
        }
        this.setBrushSize(this.h, n);
    }
    
    public void setBrushSize(final BrushSize.SizeType h, final float mBrushScale) {
        this.mBrushScale = mBrushScale;
        this.h = h;
        switch (h.ordinal()) {
            case 1: {
                this.d = 1.0f * mBrushScale;
                break;
            }
            case 2: {
                this.d = 2.0f * mBrushScale;
                break;
            }
            case 3: {
                this.d = 4.0f * mBrushScale;
                break;
            }
            case 4: {
                this.d = h.getCustomizeBrushValue() * 4.0f * mBrushScale;
                break;
            }
        }
        this.b();
    }
    
    @Override
    protected int getMaxUndoCount() {
        return super.getMaxUndoCount();
    }
    
    @Override
    protected void setMaxUndoCount(final int maxUndoCount) {
        super.setMaxUndoCount(maxUndoCount);
    }
    
    @Override
    protected int getRedoCount() {
        return super.getRedoCount();
    }
    
    @Override
    protected int getUndoCount() {
        return super.getUndoCount();
    }
    
    @Override
    protected void saveCurrentAsHistory() {
        super.saveCurrentAsHistory();
    }
    
    @Override
    protected Bitmap getRedoData() {
        return super.getRedoData();
    }
    
    @Override
    protected Bitmap getUndoData() {
        return super.getUndoData();
    }
    
    @Override
    protected Bitmap getCanvasImage() {
        return super.getCanvasImage();
    }
    
    @Override
    protected Bitmap getOriginalImage() {
        return super.getOriginalImage();
    }
    
    protected void touchBegan(final PointF pointF) {
        super.touchBegan();
        this.g = new PointF(pointF.x, pointF.y);
        (this.e = new Path()).moveTo(pointF.x, pointF.y);
    }
    
    protected void pathMove(final PointF pointF) {
        final int width = this.originalSnap.getWidth();
        final int height = this.originalSnap.getHeight();
        if (pointF.x >= 0.0f && pointF.y >= 0.0f && pointF.x < width && pointF.y < height) {
            if (Math.abs(this.g.x - pointF.x) < this.i && Math.abs(this.g.y - pointF.y) < this.i) {
                return;
            }
            this.e.quadTo(this.g.x, this.g.y, (this.g.x + pointF.x) / 2.0f, (this.g.y + pointF.y) / 2.0f);
            this.g.set(pointF.x, pointF.y);
            this.a();
        }
    }
    
    protected void touchEnd() {
        this.b();
    }
    
    private void a() {
        this.smudgeCanvas.drawPath(this.e, this.f);
    }
    
    private void b() {
        if (this.f == null) {
            this.f = new Paint();
        }
        else {
            this.f.reset();
        }
        this.f.setAntiAlias(true);
        this.f.setDither(true);
        this.f.setStyle(Paint.Style.STROKE);
        this.f.setStrokeWidth(this.d);
        this.f.setColor(this.c);
        this.f.setStrokeJoin(this.b);
        this.f.setStrokeCap(this.a);
    }
    
    @Override
    protected float getMaxTemplateDistance(final float n) {
        return super.getMaxTemplateDistance(n);
    }
}
