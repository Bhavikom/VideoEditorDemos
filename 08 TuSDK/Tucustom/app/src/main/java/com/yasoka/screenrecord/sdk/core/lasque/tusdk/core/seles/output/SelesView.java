// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.output;

//import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateFillModeBuilderImpl;
//import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateBuilder;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.RectF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateBuilder;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateFillModeBuilder;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateFillModeBuilderImpl;
//import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateFillModeBuilder;

public class SelesView extends SelesBaseView
{
    private SelesFillModeType a;
    private SelesVerticeCoordinateFillModeBuilder b;
    private SelesSurfacePusher c;
    private RectF d;
    
    public SelesFillModeType getFillMode() {
        return this.a;
    }
    
    public void setFillMode(final SelesFillModeType selesFillModeType) {
        if (selesFillModeType == null) {
            return;
        }
        this.a = selesFillModeType;
        if (this.b != null) {
            this.b.setFillMode(selesFillModeType);
        }
    }
    
    public RectF getDisplayRect() {
        return this.d;
    }
    
    public void setDisplayRect(final RectF rectF) {
        if (rectF == null) {
            return;
        }
        this.d = rectF;
        if (this.b != null) {
            this.b.setCanvasRect(rectF);
        }
    }
    
    public SelesView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    public SelesView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public SelesView(final Context context) {
        super(context);
    }
    
    @Override
    protected void initView(final Context context, final AttributeSet set) {
        this.a = SelesFillModeType.PreserveAspectRatio;
        super.initView(context, set);
    }
    
    @Override
    protected SelesSurfacePusher buildWindowDisplay() {
        if (this.c == null) {
            this.c = new SelesSurfacePusher();
        }
        return this.c;
    }
    
    @Override
    protected SelesVerticeCoordinateBuilder buildVerticeCoordinateBuilder() {
        if (this.b == null) {
            this.b = new SelesVerticeCoordinateFillModeBuilderImpl(true);
        }
        this.b.setFillMode(this.a);
        return this.b;
    }
    
    public void setOnDisplayChangeListener(final SelesVerticeCoordinateFillModeBuilder.OnDisplaySizeChangeListener onDisplaySizeChangeListener) {
        if (this.b == null) {
            return;
        }
        this.b.setOnDisplaySizeChangeListener(onDisplaySizeChangeListener);
    }
    
    public enum SelesFillModeType
    {
        Stretch, 
        PreserveAspectRatio, 
        PreserveAspectRatioAndFill;
    }
}
