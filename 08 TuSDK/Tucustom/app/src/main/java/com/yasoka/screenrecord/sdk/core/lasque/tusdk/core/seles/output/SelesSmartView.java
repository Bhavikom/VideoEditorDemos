// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.output;

//import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateBuilder;
import android.util.AttributeSet;
import android.content.Context;
import android.graphics.RectF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateBuilder;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCropBuilderImpl;
//import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCropBuilderImpl;

public class SelesSmartView extends SelesBaseView
{
    private SelesVerticeCoordinateCropBuilderImpl a;
    private SelesSurfacePusher b;
    private RectF c;
    
    public RectF getDisplayRect() {
        return this.c;
    }
    
    public void setDisplayRect(final RectF rectF) {
        if (rectF == null) {
            return;
        }
        this.c = rectF;
        if (this.a != null) {
            this.a.setCanvasRect(rectF);
        }
    }
    
    public SelesSmartView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
    }
    
    public SelesSmartView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public SelesSmartView(final Context context) {
        super(context);
    }
    
    @Override
    protected void initView(final Context context, final AttributeSet set) {
        this.c = new RectF(0.0f, 0.0f, 1.0f, 1.0f);
        super.initView(context, set);
    }
    
    @Override
    protected SelesSurfacePusher buildWindowDisplay() {
        if (this.b == null) {
            this.b = new SelesSurfacePusher();
        }
        return this.b;
    }
    
    @Override
    protected SelesVerticeCoordinateBuilder buildVerticeCoordinateBuilder() {
        if (this.a == null) {
            this.a = new SelesVerticeCoordinateCropBuilderImpl(true);
        }
        this.a.setCanvasRect(this.c);
        return this.a;
    }
}
