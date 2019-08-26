// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.extend;

//import org.lasque.tusdk.core.utils.RectHelper;
import android.graphics.Rect;
//import org.lasque.tusdk.core.seles.output.SelesSurfacePusher;
import java.nio.FloatBuffer;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.seles.output.SelesView;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
import android.graphics.RectF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.output.SelesSurfacePusher;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.output.SelesView;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.RectHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.struct.TuSdkSize;

public class SelesVerticeCoordinateFillModeBuilderImpl implements SelesVerticeCoordinateFillModeBuilder
{
    private TuSdkSize a;
    private TuSdkSize b;
    private RectF c;
    private ImageOrientation d;
    private boolean e;
    private boolean f;
    private OnDisplaySizeChangeListener g;
    private SelesView.SelesFillModeType h;
    
    public SelesVerticeCoordinateFillModeBuilderImpl(final boolean f) {
        this.a = TuSdkSize.create(0);
        this.b = TuSdkSize.create(0);
        this.c = new RectF();
        this.d = ImageOrientation.Up;
        this.e = false;
        this.f = false;
        this.h = SelesView.SelesFillModeType.PreserveAspectRatio;
        this.f = f;
    }
    
    @Override
    public void setOutputSize(final TuSdkSize tuSdkSize) {
        if (tuSdkSize == null || !tuSdkSize.isSize()) {
            TLog.w("%s setOutputSize is Null or side < 1, size: %s", "_SelesFillModeVerticeCoordinateBuilder", tuSdkSize);
            return;
        }
        if (tuSdkSize.equals(this.b)) {
            return;
        }
        this.b = tuSdkSize.copy();
        this.e = true;
    }
    
    @Override
    public void setCanvasRect(final RectF rectF) {
        if (this.c.equals((Object)rectF)) {
            return;
        }
        this.c = new RectF(rectF);
        this.e = true;
    }
    
    @Override
    public void setFillMode(final SelesView.SelesFillModeType h) {
        if (h == null || h == this.h) {
            return;
        }
        this.h = h;
        this.e = true;
    }
    
    @Override
    public void setOnDisplaySizeChangeListener(final OnDisplaySizeChangeListener g) {
        this.g = g;
    }
    
    @Override
    public TuSdkSize outputSize() {
        return this.b;
    }
    
    @Override
    public boolean calculate(final TuSdkSize tuSdkSize, ImageOrientation up, final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        if (tuSdkSize == null || !tuSdkSize.isSize()) {
            TLog.w("%s setOutputSize is Null or side < 1, size: %s", "_SelesFillModeVerticeCoordinateBuilder", tuSdkSize);
            return false;
        }
        if (floatBuffer == null) {
            TLog.w("%s calculate need verticesBuffer", "_SelesFillModeVerticeCoordinateBuilder");
            return false;
        }
        if (floatBuffer2 == null) {
            TLog.w("%s calculate need textureBuffer", "_SelesFillModeVerticeCoordinateBuilder");
            return false;
        }
        if (up == null) {
            up = ImageOrientation.Up;
        }
        if (tuSdkSize.equals(this.a) && up == this.d && !this.e) {
            return true;
        }
        this.e = false;
        this.a = tuSdkSize.copy();
        this.d = up;
        if (!this.b.isSize()) {
            this.b = tuSdkSize.copy();
        }
        floatBuffer2.clear();
        floatBuffer2.put(SelesSurfacePusher.textureCoordinates(up)).position(0);
        if (this.c.isEmpty()) {
            this.a(this.b, this.a, floatBuffer, this.h);
        }
        else {
            this.a(this.b, this.a, floatBuffer, this.c);
        }
        return true;
    }
    
    private void a(final TuSdkSize tuSdkSize, final TuSdkSize tuSdkSize2, final FloatBuffer floatBuffer, final SelesView.SelesFillModeType selesFillModeType) {
        final TuSdkSize copy = tuSdkSize.copy();
        final Rect rectWithAspectRatioInsideRect = RectHelper.makeRectWithAspectRatioInsideRect(tuSdkSize2, new Rect(0, 0, copy.width, copy.height));
        float n = 0.0f;
        float n2 = 0.0f;
        switch (selesFillModeType.ordinal()) {
            case 1: {
                n = rectWithAspectRatioInsideRect.width() / (float)copy.width;
                n2 = rectWithAspectRatioInsideRect.height() / (float)copy.height;
                break;
            }
            case 2: {
                n = copy.height / (float)rectWithAspectRatioInsideRect.height();
                n2 = copy.width / (float)rectWithAspectRatioInsideRect.width();
                break;
            }
            default: {
                n = 1.0f;
                n2 = 1.0f;
                break;
            }
        }
        final float[] src = { -n, -n2, n, -n2, -n, n2, n, n2 };
        if (this.g != null) {
            this.g.onDisplaySizeChanged(TuSdkSize.create(rectWithAspectRatioInsideRect));
        }
        floatBuffer.clear();
        floatBuffer.put(src).position(0);
    }
    
    private void a(final TuSdkSize tuSdkSize, final TuSdkSize tuSdkSize2, final FloatBuffer floatBuffer, final RectF rectF) {
        final TuSdkSize copy = tuSdkSize.copy();
        copy.height = Math.max(copy.width, copy.height);
        final RectF rectWithAspectRatioOutsideRect = RectHelper.makeRectWithAspectRatioOutsideRect(tuSdkSize2, new RectF(0.0f, 0.0f, (float)copy.width, (float)copy.height));
        final float n = rectWithAspectRatioOutsideRect.width() / copy.width;
        final float n2 = rectWithAspectRatioOutsideRect.height() / copy.height;
        final float n3 = n * copy.width / rectWithAspectRatioOutsideRect.width() + 2.0f * n * rectF.left;
        final float n4 = n2 * copy.height / rectWithAspectRatioOutsideRect.height() + 2.0f * n2 * rectF.top;
        float[] src = { -n3, -n2, 2.0f * n - n3, -n2, -n3, n2, 2.0f * n - n3, n2 };
        if (rectWithAspectRatioOutsideRect.width() < rectWithAspectRatioOutsideRect.height()) {
            src = new float[] { -n, -2.0f * n2 + n4, n, -2.0f * n2 + n4, -n, n4, n, n4 };
        }
        floatBuffer.clear();
        floatBuffer.put(src).position(0);
    }
}
