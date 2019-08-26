// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.components.widget.smudge;

//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.utils.image.BitmapHelper;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
//import org.lasque.tusdk.modules.view.widget.smudge.BrushLocalPackage;
//import org.lasque.tusdk.core.secret.SdkValid;
import android.util.AttributeSet;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.view.MotionEvent;
import android.content.Context;
import android.annotation.SuppressLint;
import android.view.View;
//import org.lasque.tusdk.modules.view.widget.smudge.BrushSize;
//import org.lasque.tusdk.modules.view.widget.smudge.BrushData;
import android.graphics.PointF;
import android.widget.ImageView;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.SdkValid;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.BitmapHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view.TuSdkRelativeLayout;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.smudge.BrushData;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.smudge.BrushLocalPackage;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.smudge.BrushSize;
//import org.lasque.tusdk.core.view.TuSdkRelativeLayout;

public class SmudgeView extends TuSdkRelativeLayout
{
    private SmudgeViewDelegate a;
    private SmudgeActionDelegate b;
    protected SimpleProcessor mSmudgeProcessor;
    private ImageView c;
    private ImageView d;
    private ImageView e;
    private PointF f;
    private PointF g;
    private BrushData h;
    private BrushSize.SizeType i;
    private int j;
    @SuppressLint({ "ClickableViewAccessibility" })
    protected View.OnTouchListener mOnTouchListener;
    
    public SmudgeViewDelegate getDelegate() {
        return this.a;
    }
    
    public void setDelegate(final SmudgeViewDelegate a) {
        this.a = a;
    }
    
    public SmudgeActionDelegate getActionDelegate() {
        return this.b;
    }
    
    public void setActionDelegate(final SmudgeActionDelegate b) {
        this.b = b;
    }
    
    public SmudgeView(final Context context) {
        super(context);
        this.j = 5;
        this.mOnTouchListener = (View.OnTouchListener)new View.OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                if (SmudgeView.this.getBrushSizePixel() <= 0) {
                    return false;
                }
                if (SmudgeView.this.c == null || SmudgeView.this.getProcessorInstance() == null || motionEvent.getPointerCount() > 1) {
                    return false;
                }
                if (SmudgeView.this.f == null) {
                    SmudgeView.this.f = new PointF(0.0f, 0.0f);
                }
                if (SmudgeView.this.g != null) {
                    SmudgeView.this.f.x = SmudgeView.this.g.x;
                    SmudgeView.this.f.y = SmudgeView.this.g.y;
                }
                else {
                    SmudgeView.this.g = new PointF(0.0f, 0.0f);
                }
                final Matrix matrix = new Matrix();
                SmudgeView.this.c.getImageMatrix().invert(matrix);
                final float[] array = { motionEvent.getX(), motionEvent.getY() };
                final PointF pointF = new PointF(array[0], array[1]);
                matrix.mapPoints(array);
                SmudgeView.this.g.x = array[0];
                SmudgeView.this.g.y = array[1];
                final SimpleProcessor processorInstance = SmudgeView.this.getProcessorInstance();
                switch (motionEvent.getAction()) {
                    case 0: {
                        processorInstance.touchBegan();
                        break;
                    }
                    case 1: {
                        processorInstance.saveCurrentAsHistory();
                        SmudgeView.this.sendSmudgeActionChangeNotify();
                        SmudgeView.this.onSmudgeEnd();
                        break;
                    }
                    case 2: {
                        SmudgeView.this.a(pointF);
                        processorInstance.drawBetweenPoints(SmudgeView.this.f, SmudgeView.this.g);
                        final Bitmap canvasImage = processorInstance.getCanvasImage();
                        SmudgeView.this.d.setImageBitmap(canvasImage);
                        SmudgeView.this.onSmudgeChanged(SmudgeView.this.g, pointF, canvasImage.getWidth(), canvasImage.getHeight());
                        break;
                    }
                }
                return true;
            }
        };
    }
    
    public SmudgeView(final Context context, final AttributeSet set) {
        super(context, set);
        this.j = 5;
        this.mOnTouchListener = (View.OnTouchListener)new View.OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                if (SmudgeView.this.getBrushSizePixel() <= 0) {
                    return false;
                }
                if (SmudgeView.this.c == null || SmudgeView.this.getProcessorInstance() == null || motionEvent.getPointerCount() > 1) {
                    return false;
                }
                if (SmudgeView.this.f == null) {
                    SmudgeView.this.f = new PointF(0.0f, 0.0f);
                }
                if (SmudgeView.this.g != null) {
                    SmudgeView.this.f.x = SmudgeView.this.g.x;
                    SmudgeView.this.f.y = SmudgeView.this.g.y;
                }
                else {
                    SmudgeView.this.g = new PointF(0.0f, 0.0f);
                }
                final Matrix matrix = new Matrix();
                SmudgeView.this.c.getImageMatrix().invert(matrix);
                final float[] array = { motionEvent.getX(), motionEvent.getY() };
                final PointF pointF = new PointF(array[0], array[1]);
                matrix.mapPoints(array);
                SmudgeView.this.g.x = array[0];
                SmudgeView.this.g.y = array[1];
                final SimpleProcessor processorInstance = SmudgeView.this.getProcessorInstance();
                switch (motionEvent.getAction()) {
                    case 0: {
                        processorInstance.touchBegan();
                        break;
                    }
                    case 1: {
                        processorInstance.saveCurrentAsHistory();
                        SmudgeView.this.sendSmudgeActionChangeNotify();
                        SmudgeView.this.onSmudgeEnd();
                        break;
                    }
                    case 2: {
                        SmudgeView.this.a(pointF);
                        processorInstance.drawBetweenPoints(SmudgeView.this.f, SmudgeView.this.g);
                        final Bitmap canvasImage = processorInstance.getCanvasImage();
                        SmudgeView.this.d.setImageBitmap(canvasImage);
                        SmudgeView.this.onSmudgeChanged(SmudgeView.this.g, pointF, canvasImage.getWidth(), canvasImage.getHeight());
                        break;
                    }
                }
                return true;
            }
        };
    }
    
    public SmudgeView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.j = 5;
        this.mOnTouchListener = (View.OnTouchListener)new View.OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                if (SmudgeView.this.getBrushSizePixel() <= 0) {
                    return false;
                }
                if (SmudgeView.this.c == null || SmudgeView.this.getProcessorInstance() == null || motionEvent.getPointerCount() > 1) {
                    return false;
                }
                if (SmudgeView.this.f == null) {
                    SmudgeView.this.f = new PointF(0.0f, 0.0f);
                }
                if (SmudgeView.this.g != null) {
                    SmudgeView.this.f.x = SmudgeView.this.g.x;
                    SmudgeView.this.f.y = SmudgeView.this.g.y;
                }
                else {
                    SmudgeView.this.g = new PointF(0.0f, 0.0f);
                }
                final Matrix matrix = new Matrix();
                SmudgeView.this.c.getImageMatrix().invert(matrix);
                final float[] array = { motionEvent.getX(), motionEvent.getY() };
                final PointF pointF = new PointF(array[0], array[1]);
                matrix.mapPoints(array);
                SmudgeView.this.g.x = array[0];
                SmudgeView.this.g.y = array[1];
                final SimpleProcessor processorInstance = SmudgeView.this.getProcessorInstance();
                switch (motionEvent.getAction()) {
                    case 0: {
                        processorInstance.touchBegan();
                        break;
                    }
                    case 1: {
                        processorInstance.saveCurrentAsHistory();
                        SmudgeView.this.sendSmudgeActionChangeNotify();
                        SmudgeView.this.onSmudgeEnd();
                        break;
                    }
                    case 2: {
                        SmudgeView.this.a(pointF);
                        processorInstance.drawBetweenPoints(SmudgeView.this.f, SmudgeView.this.g);
                        final Bitmap canvasImage = processorInstance.getCanvasImage();
                        SmudgeView.this.d.setImageBitmap(canvasImage);
                        SmudgeView.this.onSmudgeChanged(SmudgeView.this.g, pointF, canvasImage.getWidth(), canvasImage.getHeight());
                        break;
                    }
                }
                return true;
            }
        };
    }
    
    protected SimpleProcessor getProcessorInstance() {
        if (!SdkValid.shared.smudgeEnabled()) {
            return null;
        }
        if (this.mSmudgeProcessor == null) {
            this.mSmudgeProcessor = new SmudgeProcessor();
        }
        return this.mSmudgeProcessor;
    }
    
    public void setBrush(final BrushData h) {
        this.h = h;
        this.updateBrushSettings();
    }
    
    public void setBrushSize(final BrushSize.SizeType i) {
        this.i = i;
        this.updateBrushSettings();
        this.a();
    }
    
    public BrushSize.SizeType getBrushSize() {
        return this.i;
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
    
    public int getMaxUndoCount() {
        return this.j;
    }
    
    public void setMaxUndoCount(final int j) {
        this.j = j;
    }
    
    protected void updateBrushSettings() {
        if (this.getProcessorInstance() == null) {
            return;
        }
        if (this.h == null) {
            this.h = BrushLocalPackage.shared().getEeaserBrush();
        }
        if (BrushLocalPackage.shared().loadBrushData(this.h)) {
            this.getProcessorInstance().setBrush(this.h);
            this.getProcessorInstance().setBrushSize(this.i);
        }
    }
    
    @Override
    public void loadView() {
        super.loadView();
        this.addView((View)(this.c = new ImageView(this.getContext())), (ViewGroup.LayoutParams)new RelativeLayout.LayoutParams(-1, -1));
        (this.d = new ImageView(this.getContext())).setOnTouchListener(this.mOnTouchListener);
        this.addView((View)this.d, (ViewGroup.LayoutParams)new RelativeLayout.LayoutParams(-1, -1));
        (this.e = new ImageView(this.getContext())).setVisibility(INVISIBLE);
        this.e.setBackgroundColor(16777215);
        this.addView((View)this.e, (ViewGroup.LayoutParams)new RelativeLayout.LayoutParams(24, 24));
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
    
    private void a(final PointF pointF) {
        final RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams)this.e.getLayoutParams();
        layoutParams.setMargins((int)pointF.x - layoutParams.width / 2, (int)pointF.y - layoutParams.height / 2, 0, 0);
        this.e.setLayoutParams((ViewGroup.LayoutParams)layoutParams);
        this.e.setVisibility(VISIBLE);
    }
    
    protected void onSmudgeChanged(final PointF pointF, final PointF pointF2, final int n, final int n2) {
        if (this.getActionDelegate() != null) {
            this.getActionDelegate().onSmudgeChanged(pointF, pointF2, n, n2);
        }
    }
    
    protected void onSmudgeEnd() {
        if (this.getActionDelegate() != null) {
            this.getActionDelegate().onSmudgeEnd();
        }
        this.e.setVisibility(INVISIBLE);
    }
    
    public void undo() {
        final SimpleProcessor processorInstance = this.getProcessorInstance();
        if (processorInstance == null) {
            return;
        }
        if (processorInstance.getUndoCount() > 0) {
            this.d.setImageBitmap(processorInstance.getUndoData());
        }
        this.sendSmudgeActionChangeNotify();
    }
    
    public void redo() {
        final SimpleProcessor processorInstance = this.getProcessorInstance();
        if (processorInstance == null) {
            return;
        }
        if (processorInstance.getRedoCount() > 0) {
            this.d.setImageBitmap(processorInstance.getRedoData());
        }
        this.sendSmudgeActionChangeNotify();
    }
    
    public void showOriginalImage(final boolean b) {
        if (this.getProcessorInstance() != null) {
            this.d.setVisibility(b ? INVISIBLE : VISIBLE);
        }
    }
    
    protected int getRedoCount() {
        if (this.getProcessorInstance() != null) {
            return this.getProcessorInstance().getRedoCount();
        }
        return 0;
    }
    
    protected int getUndoCount() {
        if (this.getProcessorInstance() != null) {
            return this.getProcessorInstance().getUndoCount();
        }
        return 0;
    }
    
    protected void sendSmudgeActionChangeNotify() {
        if (this.getDelegate() != null) {
            this.getDelegate().onRefreshStepStatesWithHistories(this.getUndoCount(), this.getRedoCount());
        }
    }
    
    public void setImageBitmap(final Bitmap bitmap) {
        if (this.getClass() == SmudgeView.class && !SdkValid.shared.smudgeEnabled()) {
            TLog.e("You are not allowed to use the smudge feature, please see http://tusdk.com", new Object[0]);
            return;
        }
        if (bitmap == null) {
            return;
        }
        final Bitmap scaleToFill = this.scaleToFill(bitmap, this.getWidth(), this.getHeight());
        (this.mSmudgeProcessor = this.getProcessorInstance()).init(bitmap, scaleToFill, this.getWidth());
        this.mSmudgeProcessor.setMaxUndoCount(this.getMaxUndoCount());
        this.d.setImageBitmap(this.mSmudgeProcessor.getCanvasImage());
        this.c.setImageBitmap(scaleToFill);
        this.updateBrushSettings();
    }
    
    public Bitmap getSmudgeBitmap() {
        if (this.mSmudgeProcessor != null) {
            return this.mSmudgeProcessor.getCanvasImage();
        }
        return null;
    }
    
    public Bitmap getOriginalBitmap() {
        if (this.mSmudgeProcessor != null) {
            return this.mSmudgeProcessor.getOriginalImage();
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
    
    public interface SmudgeActionDelegate
    {
        void onSmudgeChanged(final PointF p0, final PointF p1, final int p2, final int p3);
        
        void onSmudgeEnd();
    }
    
    public interface SmudgeViewDelegate
    {
        void onRefreshStepStatesWithHistories(final int p0, final int p1);
    }
}
