// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.extend;

//import org.lasque.tusdk.core.seles.output.SelesSurfacePusher;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;
import android.graphics.Rect;
//import org.lasque.tusdk.core.utils.RectHelper;
import java.nio.FloatBuffer;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
import android.graphics.RectF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.output.SelesSurfacePusher;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.RectHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.struct.TuSdkSize;

public class SelesVerticeCoordinateCropBuilderImpl implements SelesVerticeCoordinateCorpBuilder
{
    private TuSdkSize a;
    private TuSdkSize b;
    private RectF c;
    private RectF d;
    private RectF e;
    private ImageOrientation f;
    private boolean g;
    private SelesTextureSizeAlign h;
    private boolean i;
    private boolean j;
    private boolean k;
    private float l;
    
    @Override
    public TuSdkSize outputSize() {
        return this.b;
    }
    
    public void setTextureSizeAlign(final SelesTextureSizeAlign h) {
        if (h == null || this.h == h) {
            return;
        }
        this.h = h;
        this.b = this.h.align(this.b);
        this.g = true;
    }
    
    @Override
    public void setOutputSize(final TuSdkSize tuSdkSize) {
        if (tuSdkSize == null || tuSdkSize.minSide() < this.h.getMultiple()) {
            TLog.w("%s setOutputSize is Null or side < %d, size: %s", "SelesVerticeCoordinateCropBuilderImpl", this.h.getMultiple(), tuSdkSize);
            return;
        }
        if (tuSdkSize.equals(this.b) && this.k) {
            return;
        }
        this.b = this.h.align(tuSdkSize.copy());
        this.g = true;
        this.j = true;
    }
    
    @Override
    public void setCanvasRect(final RectF rectF) {
        if (rectF == null || rectF.width() == 0.0f || rectF.height() == 0.0f || rectF.equals((Object)this.c)) {
            return;
        }
        this.c = new RectF(rectF);
        this.g = true;
    }
    
    @Override
    public void setCropRect(final RectF rectF) {
        if (rectF == null || rectF.width() == 0.0f || rectF.height() == 0.0f || rectF.equals((Object)this.c)) {
            return;
        }
        this.d = new RectF(rectF);
        this.g = true;
    }
    
    @Override
    public void setPreCropRect(RectF e) {
        if (e != null && !e.equals((Object)e)) {
            e = new RectF(e);
        }
        this.e = e;
        this.g = true;
    }
    
    @Override
    public void setEnableClip(final boolean k) {
        this.k = k;
    }
    
    @Override
    public TuSdkSize setOutputRatio(final float l) {
        this.l = l;
        if (l == 0.0f) {
            this.c.left = 0.0f;
            this.c.top = 0.0f;
            this.c.right = 1.0f;
            this.c.bottom = 1.0f;
            this.setOutputSize(this.a);
            return this.a;
        }
        final int n = (this.a.width > this.a.height) ? this.a.height : this.a.width;
        final TuSdkSize create = TuSdkSize.create(n, (int)(n / l));
        this.setOutputSize(create);
        return create;
    }
    
    @Override
    public float getOutputRatio(final float n) {
        return this.l;
    }
    
    public SelesVerticeCoordinateCropBuilderImpl(final boolean i) {
        this.a = TuSdkSize.create(0);
        this.b = TuSdkSize.create(0);
        this.c = new RectF(0.0f, 0.0f, 1.0f, 1.0f);
        this.d = new RectF(0.0f, 0.0f, 1.0f, 1.0f);
        this.f = ImageOrientation.Up;
        this.g = false;
        this.h = SelesTextureSizeAlign.Align2MultipleMax;
        this.i = false;
        this.j = false;
        this.k = true;
        this.i = i;
    }
    
    @Override
    public boolean calculate(final TuSdkSize tuSdkSize, ImageOrientation up, final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        if (tuSdkSize == null || tuSdkSize.minSide() < this.h.getMultiple()) {
            TLog.w("%s calculate need min side >= %d, Input: %s", "SelesVerticeCoordinateCropBuilderImpl", this.h.getMultiple(), tuSdkSize);
            return false;
        }
        if (floatBuffer == null) {
            TLog.w("%s calculate need verticesBuffer", "SelesVerticeCoordinateCropBuilderImpl");
            return false;
        }
        if (floatBuffer2 == null) {
            TLog.w("%s calculate need textureBuffer", "SelesVerticeCoordinateCropBuilderImpl");
            return false;
        }
        if (up == null) {
            up = ImageOrientation.Up;
        }
        if (tuSdkSize.equals(this.a) && up == this.f && !this.g) {
            return true;
        }
        this.g = false;
        this.a = tuSdkSize.copy();
        this.f = up;
        final RectF rectF = (this.e == null) ? null : RectHelper.rotationWithRotation(this.e, up);
        final RectF rectF2 = this.d.contains(0.0f, 0.0f, 1.0f, 1.0f) ? null : this.d;
        if (!this.j) {
            final TuSdkSize copy = tuSdkSize.copy();
            if (rectF != null) {
                final TuSdkSize tuSdkSize2 = copy;
                tuSdkSize2.width *= (int)rectF.width();
                final TuSdkSize tuSdkSize3 = copy;
                tuSdkSize3.height *= (int)rectF.height();
            }
            if (rectF2 != null) {
                final TuSdkSize tuSdkSize4 = copy;
                tuSdkSize4.width *= (int)rectF2.width();
                final TuSdkSize tuSdkSize5 = copy;
                tuSdkSize5.height *= (int)rectF2.height();
            }
            this.b = this.h.align(copy);
        }
        TLog.d("%s Input: %s | Output: %s | Orientation: %s | PreCropRect: %s | CropRect: %s | CanvasRect: %s", "SelesVerticeCoordinateCropBuilderImpl", this.a, this.b, up, this.e, this.d, this.c);
        final TuSdkSize b = this.b;
        if (!this.k && (!this.a.equals(this.b) || this.l != 0.0f)) {
            final Rect rectWithAspectRatioInsideRect = RectHelper.makeRectWithAspectRatioInsideRect(this.a, new Rect(0, 0, this.b.width, this.b.height));
            this.c.left = rectWithAspectRatioInsideRect.left / (float)this.b.width;
            this.c.right = rectWithAspectRatioInsideRect.right / (float)this.b.width;
            this.c.top = rectWithAspectRatioInsideRect.top / (float)this.b.height;
            this.c.bottom = rectWithAspectRatioInsideRect.bottom / (float)this.b.height;
        }
        final RectF rectF3 = new RectF(this.c);
        final TuSdkSize create = TuSdkSize.create((int)(rectF3.width() * b.width), (int)(rectF3.height() * b.height));
        this.a(floatBuffer, rectF3, create.equals(b));
        if (!this.k && this.l != 0.0f) {
            return true;
        }
        this.a(tuSdkSize, up, floatBuffer2, create, rectF, rectF2);
        return true;
    }
    
    private void a(final FloatBuffer floatBuffer, final RectF rectF, final boolean b) {
        floatBuffer.clear();
        if (b) {
            floatBuffer.put(SelesFilter.imageVertices).position(0);
            return;
        }
        final float[] src = new float[8];
        if (this.i) {
            src[0] = rectF.left * 2.0f - 1.0f;
            src[1] = 1.0f - rectF.bottom * 2.0f;
            src[2] = rectF.right * 2.0f - 1.0f;
            src[3] = src[1];
            src[4] = src[0];
            src[5] = 1.0f - rectF.top * 2.0f;
            src[6] = src[2];
            src[7] = src[5];
        }
        else {
            src[0] = rectF.left * 2.0f - 1.0f;
            src[1] = rectF.top * 2.0f - 1.0f;
            src[2] = rectF.right * 2.0f - 1.0f;
            src[3] = src[1];
            src[4] = src[0];
            src[5] = rectF.bottom * 2.0f - 1.0f;
            src[6] = src[2];
            src[7] = src[5];
        }
        floatBuffer.put(src).position(0);
    }
    
    private void a(final TuSdkSize tuSdkSize, final ImageOrientation imageOrientation, final FloatBuffer floatBuffer, final TuSdkSize tuSdkSize2, final RectF rectF, final RectF rectF2) {
        if (tuSdkSize.equals(tuSdkSize2) && rectF == null && rectF2 == null) {
            floatBuffer.clear();
            if (this.i) {
                floatBuffer.put(SelesSurfacePusher.textureCoordinates(imageOrientation)).position(0);
            }
            else {
                floatBuffer.put(SelesFilter.textureCoordinates(imageOrientation)).position(0);
            }
            return;
        }
        final Rect rect = new Rect(0, 0, tuSdkSize.width, tuSdkSize.height);
        if (rectF != null) {
            rect.left = (int)(rectF.left * tuSdkSize.width);
            rect.right = (int)(rectF.right * tuSdkSize.width);
            rect.top = (int)(rectF.top * tuSdkSize.height);
            rect.bottom = (int)(rectF.bottom * tuSdkSize.height);
        }
        if (rectF2 != null) {
            final int width = rect.width();
            final int height = rect.height();
            final Rect rect2 = rect;
            rect2.left += (int)(rectF2.left * width);
            rect.right = rect.left + (int)(rectF2.width() * width);
            final Rect rect3 = rect;
            rect3.top += (int)(rectF2.top * height);
            rect.bottom = rect.top + (int)(rectF2.height() * height);
        }
        final Rect rectWithAspectRatioInsideRect = RectHelper.makeRectWithAspectRatioInsideRect(tuSdkSize2, rect);
        TLog.d("%s size: %s, displaySize: %s, textureRect: %s, displayRect: %s", "SelesVerticeCoordinateCropBuilderImpl", tuSdkSize, tuSdkSize2, rect, rectWithAspectRatioInsideRect);
        final RectF rectF3 = new RectF();
        rectF3.left = rectWithAspectRatioInsideRect.left / (float)tuSdkSize.width;
        rectF3.top = rectWithAspectRatioInsideRect.top / (float)tuSdkSize.height;
        rectF3.right = rectWithAspectRatioInsideRect.right / (float)tuSdkSize.width;
        rectF3.bottom = rectWithAspectRatioInsideRect.bottom / (float)tuSdkSize.height;
        float[] src;
        if (this.i) {
            src = RectHelper.displayCoordinates(imageOrientation, rectF3);
        }
        else {
            src = RectHelper.textureCoordinates(imageOrientation, rectF3);
        }
        floatBuffer.clear();
        floatBuffer.put(src).position(0);
    }
}
