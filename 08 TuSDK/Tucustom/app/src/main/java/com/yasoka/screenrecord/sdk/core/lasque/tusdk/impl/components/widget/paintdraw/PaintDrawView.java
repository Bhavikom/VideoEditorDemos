// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.components.widget.paintdraw;

//import org.lasque.tusdk.core.utils.image.BitmapHelper;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;
import android.view.ViewGroup;
//import android.support.v7.widget.RecyclerView;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.secret.SdkValid;
import android.util.AttributeSet;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.MotionEvent;
import android.content.Context;
import android.view.View;
import android.graphics.Paint;
//import org.lasque.tusdk.modules.view.widget.smudge.BrushSize;
import android.graphics.PointF;
import android.widget.ImageView;

//import androidx.recyclerview.widget.RecyclerView;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.SdkValid;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.BitmapHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkRelativeLayout;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.smudge.BrushSize;
//import org.lasque.tusdk.core.view.TuSdkRelativeLayout;

public class PaintDrawView extends TuSdkRelativeLayout
{
    private PaintDrawViewDelagate a;
    private PaintDrawActionDelegate b;
    protected PaintDrawProcessor mPaintDrawProcessor;
    private ImageView c;
    private ImageView d;
    private ImageView e;
    private PointF f;
    private PointF g;
    private BrushSize.SizeType h;
    private float i;
    private int j;
    private Paint.Join k;
    private Paint.Cap l;
    private float m;
    private int n;
    protected View.OnTouchListener mOnTouchListener;
    
    public PaintDrawViewDelagate getDelegate() {
        return this.a;
    }
    
    public void setDelegate(final PaintDrawViewDelagate a) {
        this.a = a;
    }
    
    public PaintDrawActionDelegate getActionDelegate() {
        return this.b;
    }
    
    public void setActionDelegate(final PaintDrawActionDelegate b) {
        this.b = b;
    }
    
    public PaintDrawView(final Context context) {
        super(context);
        this.h = BrushSize.SizeType.MediumBrush;
        this.i = 3.0f;
        this.j = 0;
        this.k = Paint.Join.ROUND;
        this.l = Paint.Cap.ROUND;
        this.m = 10.0f;
        this.n = 5;
        this.mOnTouchListener = (View.OnTouchListener)new View.OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                if (PaintDrawView.this.getBrushSizePixel() <= 0) {
                    return false;
                }
                if (PaintDrawView.this.c == null || PaintDrawView.this.getProcessorInstance() == null || motionEvent.getPointerCount() > 1) {
                    return false;
                }
                if (PaintDrawView.this.f == null) {
                    PaintDrawView.this.f = new PointF(0.0f, 0.0f);
                }
                if (PaintDrawView.this.g != null) {
                    PaintDrawView.this.f.x = PaintDrawView.this.g.x;
                    PaintDrawView.this.f.y = PaintDrawView.this.g.y;
                }
                else {
                    PaintDrawView.this.g = new PointF(0.0f, 0.0f);
                }
                final Matrix matrix = new Matrix();
                PaintDrawView.this.c.getImageMatrix().invert(matrix);
                final float[] array = { motionEvent.getX(), motionEvent.getY() };
                final PointF pointF = new PointF(array[0], array[1]);
                matrix.mapPoints(array);
                PaintDrawView.this.g.x = array[0];
                PaintDrawView.this.g.y = array[1];
                final PaintDrawProcessor processorInstance = PaintDrawView.this.getProcessorInstance();
                switch (motionEvent.getAction()) {
                    case 0: {
                        processorInstance.touchBegan(PaintDrawView.this.g);
                        break;
                    }
                    case 1: {
                        processorInstance.saveCurrentAsHistory();
                        PaintDrawView.this.sendPaintDrawActionChangeNotify();
                        PaintDrawView.this.onPaintDrawEnd();
                        processorInstance.touchEnd();
                        break;
                    }
                    case 2: {
                        PaintDrawView.this.a(pointF);
                        processorInstance.pathMove(PaintDrawView.this.g);
                        final Bitmap canvasImage = processorInstance.getCanvasImage();
                        PaintDrawView.this.d.setImageBitmap(canvasImage);
                        PaintDrawView.this.onPaintDrawChanged(PaintDrawView.this.g, pointF, canvasImage.getWidth(), canvasImage.getHeight());
                        break;
                    }
                }
                return true;
            }
        };
    }
    
    public PaintDrawView(final Context context, final AttributeSet set) {
        super(context, set);
        this.h = BrushSize.SizeType.MediumBrush;
        this.i = 3.0f;
        this.j = 0;
        this.k = Paint.Join.ROUND;
        this.l = Paint.Cap.ROUND;
        this.m = 10.0f;
        this.n = 5;
        this.mOnTouchListener = (View.OnTouchListener)new View.OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                if (PaintDrawView.this.getBrushSizePixel() <= 0) {
                    return false;
                }
                if (PaintDrawView.this.c == null || PaintDrawView.this.getProcessorInstance() == null || motionEvent.getPointerCount() > 1) {
                    return false;
                }
                if (PaintDrawView.this.f == null) {
                    PaintDrawView.this.f = new PointF(0.0f, 0.0f);
                }
                if (PaintDrawView.this.g != null) {
                    PaintDrawView.this.f.x = PaintDrawView.this.g.x;
                    PaintDrawView.this.f.y = PaintDrawView.this.g.y;
                }
                else {
                    PaintDrawView.this.g = new PointF(0.0f, 0.0f);
                }
                final Matrix matrix = new Matrix();
                PaintDrawView.this.c.getImageMatrix().invert(matrix);
                final float[] array = { motionEvent.getX(), motionEvent.getY() };
                final PointF pointF = new PointF(array[0], array[1]);
                matrix.mapPoints(array);
                PaintDrawView.this.g.x = array[0];
                PaintDrawView.this.g.y = array[1];
                final PaintDrawProcessor processorInstance = PaintDrawView.this.getProcessorInstance();
                switch (motionEvent.getAction()) {
                    case 0: {
                        processorInstance.touchBegan(PaintDrawView.this.g);
                        break;
                    }
                    case 1: {
                        processorInstance.saveCurrentAsHistory();
                        PaintDrawView.this.sendPaintDrawActionChangeNotify();
                        PaintDrawView.this.onPaintDrawEnd();
                        processorInstance.touchEnd();
                        break;
                    }
                    case 2: {
                        PaintDrawView.this.a(pointF);
                        processorInstance.pathMove(PaintDrawView.this.g);
                        final Bitmap canvasImage = processorInstance.getCanvasImage();
                        PaintDrawView.this.d.setImageBitmap(canvasImage);
                        PaintDrawView.this.onPaintDrawChanged(PaintDrawView.this.g, pointF, canvasImage.getWidth(), canvasImage.getHeight());
                        break;
                    }
                }
                return true;
            }
        };
    }
    
    public PaintDrawView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.h = BrushSize.SizeType.MediumBrush;
        this.i = 3.0f;
        this.j = 0;
        this.k = Paint.Join.ROUND;
        this.l = Paint.Cap.ROUND;
        this.m = 10.0f;
        this.n = 5;
        this.mOnTouchListener = (View.OnTouchListener)new View.OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                if (PaintDrawView.this.getBrushSizePixel() <= 0) {
                    return false;
                }
                if (PaintDrawView.this.c == null || PaintDrawView.this.getProcessorInstance() == null || motionEvent.getPointerCount() > 1) {
                    return false;
                }
                if (PaintDrawView.this.f == null) {
                    PaintDrawView.this.f = new PointF(0.0f, 0.0f);
                }
                if (PaintDrawView.this.g != null) {
                    PaintDrawView.this.f.x = PaintDrawView.this.g.x;
                    PaintDrawView.this.f.y = PaintDrawView.this.g.y;
                }
                else {
                    PaintDrawView.this.g = new PointF(0.0f, 0.0f);
                }
                final Matrix matrix = new Matrix();
                PaintDrawView.this.c.getImageMatrix().invert(matrix);
                final float[] array = { motionEvent.getX(), motionEvent.getY() };
                final PointF pointF = new PointF(array[0], array[1]);
                matrix.mapPoints(array);
                PaintDrawView.this.g.x = array[0];
                PaintDrawView.this.g.y = array[1];
                final PaintDrawProcessor processorInstance = PaintDrawView.this.getProcessorInstance();
                switch (motionEvent.getAction()) {
                    case 0: {
                        processorInstance.touchBegan(PaintDrawView.this.g);
                        break;
                    }
                    case 1: {
                        processorInstance.saveCurrentAsHistory();
                        PaintDrawView.this.sendPaintDrawActionChangeNotify();
                        PaintDrawView.this.onPaintDrawEnd();
                        processorInstance.touchEnd();
                        break;
                    }
                    case 2: {
                        PaintDrawView.this.a(pointF);
                        processorInstance.pathMove(PaintDrawView.this.g);
                        final Bitmap canvasImage = processorInstance.getCanvasImage();
                        PaintDrawView.this.d.setImageBitmap(canvasImage);
                        PaintDrawView.this.onPaintDrawChanged(PaintDrawView.this.g, pointF, canvasImage.getWidth(), canvasImage.getHeight());
                        break;
                    }
                }
                return true;
            }
        };
    }
    
    protected PaintDrawProcessor getProcessorInstance() {
        if (!SdkValid.shared.paintEnabled()) {
            TLog.e("You are not allowed to use the paint feature, please see http://tusdk.com", new Object[0]);
            return null;
        }
        if (this.mPaintDrawProcessor == null) {
            this.mPaintDrawProcessor = new PaintDrawProcessor();
        }
        return this.mPaintDrawProcessor;
    }
    
    public float getMinDistance() {
        return this.m;
    }
    
    public void setMinDistance(final float m) {
        this.m = m;
        this.updateBrushSettings();
    }
    
    public int getPaintColor() {
        return this.j;
    }
    
    public void setPaintColor(final int j) {
        this.j = j;
        this.updateBrushSettings();
    }
    
    public float getBrushScale() {
        return this.i;
    }
    
    public void setBrushScale(final float i) {
        this.i = i;
        this.updateBrushSettings();
    }
    
    public void setBrushSize(final BrushSize.SizeType h) {
        this.h = h;
        this.updateBrushSettings();
    }
    
    public BrushSize.SizeType getBrushSize() {
        return this.h;
    }
    
    public int getMaxUndoCount() {
        return this.n;
    }
    
    public void setMaxUndoCount(final int n) {
        this.n = n;
    }
    
    public int getBrushSizePixel() {
        final BrushSize.SizeType brushSize = this.getBrushSize();
        int n = 24;
        if (brushSize == BrushSize.SizeType.MediumBrush) {
            n = 48;
        }
        else if (brushSize == BrushSize.SizeType.LargeBrush) {
            n = 72;
        }
        else if (brushSize == BrushSize.SizeType.CustomizeBrush) {
            n = (int)(BrushSize.SizeType.CustomizeBrush.getCustomizeBrushValue() * 72.0f);
        }
        return n;
    }
    
    protected void updateBrushSettings() {
        if (this.getProcessorInstance() == null) {
            return;
        }
        this.getProcessorInstance().setBrushSize(this.h);
        this.getProcessorInstance().setPaintCap(this.l);
        this.getProcessorInstance().setPaintJoin(this.k);
        this.getProcessorInstance().setPaintColor(this.j);
        this.getProcessorInstance().setBrushScale(this.i);
        this.getProcessorInstance().setMinDistance(this.m);
    }
    
    @Override
    public void loadView() {
        super.loadView();
        this.addView((View)(this.c = new ImageView(this.getContext())), (ViewGroup.LayoutParams)new RecyclerView.LayoutParams(-1, -1));
        (this.d = new ImageView(this.getContext())).setOnTouchListener(this.mOnTouchListener);
        this.addView((View)this.d, (ViewGroup.LayoutParams)new RecyclerView.LayoutParams(-1, -1));
        (this.e = new ImageView(this.getContext())).setVisibility(INVISIBLE);
        this.e.setBackgroundColor(16777215);
        this.addView((View)this.e, (ViewGroup.LayoutParams)new RecyclerView.LayoutParams(24, 24));
        this.a();
    }
    
    private void a() {
        final int brushSizePixel = this.getBrushSizePixel();
        final RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)this.e.getLayoutParams();
        layoutParams.width = brushSizePixel;
        layoutParams.height = brushSizePixel;
        layoutParams.leftMargin = 0;
        layoutParams.topMargin = 0;
        this.e.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        if (brushSizePixel > 0) {
            this.e.setImageBitmap(BitmapHelper.createOvalImage(brushSizePixel, brushSizePixel, -1));
        }
    }
    
    protected void sendPaintDrawActionChangeNotify() {
        if (this.getDelegate() != null) {
            this.getDelegate().onRefreshStepStatesWithHistories(this.getUndoCount(), this.getRedoCount());
        }
    }
    
    protected int getUndoCount() {
        if (this.getProcessorInstance() != null) {
            return this.getProcessorInstance().getUndoCount();
        }
        return 0;
    }
    
    protected int getRedoCount() {
        if (this.getProcessorInstance() != null) {
            return this.getProcessorInstance().getRedoCount();
        }
        return 0;
    }
    
    protected void onPaintDrawEnd() {
        if (this.getActionDelegate() != null) {
            this.getActionDelegate().onPaintDrawEnd();
        }
        this.e.setVisibility(INVISIBLE);
    }
    
    private void a(final PointF pointF) {
        final RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)this.e.getLayoutParams();
        layoutParams.setMargins((int)pointF.x - layoutParams.width / 2, (int)pointF.y - layoutParams.height / 2, 0, 0);
        this.e.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        this.e.setVisibility(VISIBLE);
    }
    
    protected void onPaintDrawChanged(final PointF pointF, final PointF pointF2, final int n, final int n2) {
        if (this.getActionDelegate() != null) {
            this.getActionDelegate().onPaintDrawChanged(pointF, pointF2, n, n2);
        }
    }
    
    public void undo() {
        final PaintDrawProcessor processorInstance = this.getProcessorInstance();
        if (processorInstance == null) {
            return;
        }
        if (processorInstance.getUndoCount() > 0) {
            this.d.setImageBitmap(processorInstance.getUndoData());
        }
        this.sendPaintDrawActionChangeNotify();
    }
    
    public void redo() {
        final PaintDrawProcessor processorInstance = this.getProcessorInstance();
        if (processorInstance == null) {
            return;
        }
        if (processorInstance.getRedoCount() > 0) {
            this.d.setImageBitmap(processorInstance.getRedoData());
        }
        this.sendPaintDrawActionChangeNotify();
    }
    
    public void showOriginalImage(final boolean b) {
        if (this.getProcessorInstance() != null) {
            this.d.setVisibility(b ? INVISIBLE : VISIBLE);
        }
    }
    
    public void setImageBitmap(final Bitmap bitmap) {
        if (this.getClass() == PaintDrawView.class && !SdkValid.shared.paintEnabled()) {
            TLog.e("You are not allowed to use the paint feature, please see http://tusdk.com", new Object[0]);
            return;
        }
        if (bitmap == null) {
            return;
        }
        final Bitmap scaleToFill = this.scaleToFill(bitmap, this.getWidth(), this.getHeight());
        (this.mPaintDrawProcessor = this.getProcessorInstance()).init(bitmap, scaleToFill, this.getWidth());
        this.mPaintDrawProcessor.setMaxUndoCount(this.getMaxUndoCount());
        this.d.setImageBitmap(this.mPaintDrawProcessor.getCanvasImage());
        this.c.setImageBitmap(scaleToFill);
        this.updateBrushSettings();
    }
    
    public Bitmap getPrintDrawBitmap() {
        if (this.mPaintDrawProcessor != null) {
            return this.mPaintDrawProcessor.getCanvasImage();
        }
        return null;
    }
    
    public Bitmap getOriginalBitmap() {
        if (this.mPaintDrawProcessor != null) {
            return this.mPaintDrawProcessor.getOriginalImage();
        }
        return null;
    }
    
    protected Bitmap scaleToFill(final Bitmap bitmap, final int n, final int n2) {
        final float n3 = n2 / (float)bitmap.getHeight();
        final float n4 = n / (float)bitmap.getWidth();
        final float n5 = (n3 > n4) ? n4 : n3;
        return Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth() * n5), (int)(bitmap.getHeight() * n5), true);
    }
    
    public Bitmap getCanvasImage(final Bitmap bitmap, final boolean b) {
        if (this.getProcessorInstance() != null) {
            return this.getProcessorInstance().getSmudgeImage(bitmap, b);
        }
        return null;
    }
    
    public void destroy() {
        if (this.getProcessorInstance() != null) {
            this.getProcessorInstance().destroy();
        }
    }
    
    public interface PaintDrawActionDelegate
    {
        void onPaintDrawChanged(final PointF p0, final PointF p1, final int p2, final int p3);
        
        void onPaintDrawEnd();
    }
    
    public interface PaintDrawViewDelagate
    {
        void onRefreshStepStatesWithHistories(final int p0, final int p1);
    }
}
