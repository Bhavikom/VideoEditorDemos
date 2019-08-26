// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.impl.components.widget.smudge;

import android.graphics.Matrix;
import android.graphics.Paint;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import java.util.Iterator;
import java.io.File;
//import org.lasque.tusdk.core.TuSdk;
import java.util.concurrent.Executors;
import java.util.ArrayList;
//import org.lasque.tusdk.modules.view.widget.smudge.BrushSize;
//import org.lasque.tusdk.core.utils.image.BitmapHelper;
import android.os.Message;
import java.util.concurrent.ExecutorService;
import android.os.Handler;
import java.util.List;
import android.graphics.PointF;
//import org.lasque.tusdk.modules.view.widget.smudge.BrushData;
import android.graphics.Canvas;
import android.graphics.Bitmap;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdk;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.BitmapHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.smudge.BrushData;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.smudge.BrushSize;

public class SimpleProcessor
{
    protected Bitmap originalImage;
    protected Bitmap originalSnap;
    protected Bitmap currentSnap;
    protected Canvas smudgeCanvas;
    protected Bitmap brushSnap;
    protected BrushData mCurrentBrush;
    protected float mBrushScale;
    private int a;
    private boolean b;
    private PointF c;
    private List<SmudgeLog> d;
    private List<SmudgeLog> e;
    private int f;
    private Handler g;
    private ExecutorService h;
    private boolean i;
    private boolean j;
    
    public SimpleProcessor() {
        this.b = false;
        this.f = 5;
        this.i = false;
        this.j = true;
        this.b = false;
        this.c = new PointF(0.0f, 0.0f);
        this.g = new Handler() {
            public void handleMessage(final Message message) {
                super.handleMessage(message);
                if (message.what == 1 && message.obj != null) {
                    SimpleProcessor.this.a((String)message.obj);
                }
            }
        };
    }
    
    protected void init(final Bitmap originalImage, final Bitmap originalSnap, final int a) {
        this.originalImage = originalImage;
        this.originalSnap = originalSnap;
        this.currentSnap = Bitmap.createBitmap(originalSnap.getWidth(), originalSnap.getHeight(), Bitmap.Config.ARGB_8888);
        this.smudgeCanvas = new Canvas(this.currentSnap);
        this.a = a;
    }
    
    protected int getImageWidth() {
        if (this.originalSnap != null) {
            return this.originalSnap.getWidth();
        }
        return 0;
    }
    
    protected int getImageHeight() {
        if (this.originalSnap != null) {
            return this.originalSnap.getHeight();
        }
        return 0;
    }
    
    protected void setBrush(final BrushData mCurrentBrush) {
        if (mCurrentBrush == null) {
            return;
        }
        this.mCurrentBrush = mCurrentBrush;
        BitmapHelper.recycled(this.brushSnap);
        if (this.mCurrentBrush.getType() != BrushData.BrushType.TypeMosaic) {
            this.brushSnap = this.mCurrentBrush.getImage();
        }
        else {
            this.brushSnap = this.mCurrentBrush.getImage().copy(Bitmap.Config.ARGB_8888, true);
        }
    }
    
    protected BrushData getBrush() {
        return this.mCurrentBrush;
    }
    
    protected void setBrushSize(final BrushSize.SizeType sizeType) {
        if (sizeType == null) {
            return;
        }
        this.mBrushScale = (float)(int)(BrushSize.getBrushValue(sizeType) * this.a);
    }
    
    protected int getMaxUndoCount() {
        return this.f;
    }
    
    protected void setMaxUndoCount(final int f) {
        this.f = f;
    }
    
    protected int getRedoCount() {
        if (this.e != null) {
            return this.e.size();
        }
        return 0;
    }
    
    protected int getUndoCount() {
        if (this.d != null) {
            return this.d.size();
        }
        return 0;
    }
    
    protected void saveCurrentAsHistory() {
        if (this.currentSnap == null) {
            return;
        }
        final Bitmap copy = this.currentSnap.copy(Bitmap.Config.ARGB_8888, true);
        if (this.d == null) {
            this.d = new ArrayList<SmudgeLog>();
        }
        this.d.add(new SmudgeLog(copy));
        this.b();
        this.a();
    }
    
    private void a() {
        if (!this.j || this.i) {
            return;
        }
        SmudgeLog smudgeLog = null;
        for (int i = 0; i < this.d.size(); ++i) {
            if (!this.d.get(i).hasCached()) {
                smudgeLog = this.d.get(i);
                break;
            }
        }
        if (smudgeLog == null) {
            return;
        }
        this.i = true;
        final SmudgeLog smudgeLog2 = smudgeLog;
        if (this.h == null) {
            this.h = Executors.newFixedThreadPool(1);
        }
        this.h.execute(new Runnable() {
            @Override
            public void run() {
                SimpleProcessor.this.a(smudgeLog2);
            }
        });
    }
    
    private void a(final SmudgeLog smudgeLog) {
        BitmapHelper.saveBitmapAsPNG(new File(TuSdk.getAppTempPath(), smudgeLog.getFileName()), smudgeLog.getBitmap(), 100);
        final Message message = new Message();
        message.what = 1;
        message.obj = smudgeLog.getName();
        this.g.sendMessage(message);
    }
    
    private void a(final String anObject) {
        this.i = false;
        if (this.d != null && this.d.size() > 0) {
            for (final SmudgeLog smudgeLog : this.d) {
                if (smudgeLog.getName().equals(anObject)) {
                    smudgeLog.markAsCached();
                    break;
                }
            }
            this.a();
        }
    }
    
    private void b() {
        if (this.d.size() > this.getMaxUndoCount()) {
            final SmudgeLog smudgeLog = this.d.get(0);
            this.d.remove(0);
            smudgeLog.destroy();
        }
        this.a(this.e);
    }
    
    protected Bitmap getRedoData() {
        if (this.e == null || this.e.size() == 0) {
            return null;
        }
        final int n = this.e.size() - 1;
        final SmudgeLog smudgeLog = this.e.get(n);
        final Bitmap bitmap = smudgeLog.getBitmap();
        this.e.remove(n);
        BitmapHelper.recycled(this.currentSnap);
        this.currentSnap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        this.c();
        this.d.add(smudgeLog);
        return this.getCanvasImage();
    }
    
    protected Bitmap getUndoData() {
        if (this.d == null || this.d.size() <= 0) {
            return null;
        }
        final int n = this.d.size() - 1;
        final SmudgeLog smudgeLog = this.d.get(n);
        this.d.remove(n);
        Bitmap bitmap;
        if (this.d.size() == 0) {
            bitmap = Bitmap.createBitmap(this.getImageWidth(), this.getImageHeight(), Bitmap.Config.ARGB_8888);
        }
        else {
            bitmap = this.d.get(this.d.size() - 1).getBitmap();
        }
        this.currentSnap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        this.c();
        if (this.e == null) {
            this.e = new ArrayList<SmudgeLog>();
        }
        this.e.add(smudgeLog);
        return this.getCanvasImage();
    }
    
    private void c() {
        this.smudgeCanvas.setBitmap(this.currentSnap);
    }
    
    protected Bitmap getCanvasImage() {
        return this.currentSnap;
    }
    
    protected Bitmap getOriginalImage() {
        return this.originalSnap;
    }
    
    protected void touchBegan() {
        this.b = false;
    }
    
    protected final void drawBetweenPoints(final PointF pointF, final PointF pointF2) {
        final float n = pointF2.x - pointF.x;
        final float n2 = pointF2.y - pointF.y;
        final float n3 = (float)Math.pow(n * n + n2 * n2, 0.5);
        float n4;
        float n5;
        if (this.b) {
            n4 = this.c.x;
            n5 = this.c.y;
        }
        else {
            n4 = pointF.x;
            n5 = pointF.y;
            this.b = true;
        }
        float n6 = this.mBrushScale / this.brushSnap.getWidth();
        final BrushData.BrushType type = this.mCurrentBrush.getType();
        if (type == BrushData.BrushType.TypeMosaic || type == BrushData.BrushType.TypeEraser) {
            n6 *= 0.5;
        }
        final int n7 = (int)(n3 / this.getMaxTemplateDistance(this.mBrushScale) + 1.0f);
        final float x = pointF2.x;
        final float y = pointF2.y;
        float x2 = pointF.x;
        float y2 = pointF.y;
        for (int i = 1; i <= n7; ++i) {
            final float n8 = i / (float)n7;
            final float n9 = n8 * (1.0f - n8);
            final float n10 = x2 + (x - x2) * n8 + n9 * (x2 - n4);
            final float n11 = y2 + (y - y2) * n8 + n9 * (y2 - n5);
            final float n12 = (float)(Math.atan2(n10 - x2, n11 - y2) * 180.0 / 3.141592653589793);
            x2 = n10;
            y2 = n11;
            this.drawAtPoint(n10, n11, n3, n6, -1.0f * n12);
        }
        this.c.x = pointF.x;
        this.c.y = pointF.y;
    }
    
    protected float getMaxTemplateDistance(final float n) {
        return n * 0.8f;
    }
    
    protected void drawAtPoint(final float n, final float n2, final float n3, final float n4, final float n5) {
    }
    
    protected Bitmap getSmudgeImage(final Bitmap bitmap, final boolean b) {
        final TuSdkSize create = TuSdkSize.create(bitmap);
        final Bitmap bitmap2 = Bitmap.createBitmap(create.width, create.height, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap2);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint)null);
        final Matrix matrix = new Matrix();
        matrix.postScale(bitmap.getWidth() / (float)this.currentSnap.getWidth(), bitmap.getHeight() / (float)this.currentSnap.getHeight());
        canvas.drawBitmap(this.currentSnap, matrix, (Paint)null);
        if (b) {
            BitmapHelper.recycled(bitmap);
        }
        return bitmap2;
    }
    
    protected void destroy() {
        BitmapHelper.recycled(this.originalSnap);
        BitmapHelper.recycled(this.currentSnap);
        BitmapHelper.recycled(this.brushSnap);
        this.a(this.d);
        this.a(this.e);
        if (this.h != null) {
            this.h.shutdown();
            this.h = null;
        }
    }
    
    private void a(final List<SmudgeLog> list) {
        if (list == null || list.size() <= 0) {
            return;
        }
        final Iterator<SmudgeLog> iterator = list.iterator();
        while (iterator.hasNext()) {
            iterator.next().destroy();
        }
        list.clear();
    }
}
