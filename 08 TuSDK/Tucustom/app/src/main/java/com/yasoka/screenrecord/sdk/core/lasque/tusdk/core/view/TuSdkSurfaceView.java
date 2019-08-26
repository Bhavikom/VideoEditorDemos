// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.view;

import android.view.SurfaceHolder;
import android.util.AttributeSet;
import android.content.Context;
import android.opengl.GLSurfaceView;

public class TuSdkSurfaceView extends GLSurfaceView
{
    private CameraSurfaceViewDelegate a;
    private boolean b;
    
    public TuSdkSurfaceView(final Context context) {
        super(context);
    }
    
    public TuSdkSurfaceView(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    public boolean isCreated() {
        return this.b;
    }
    
    public void setDelegate(final CameraSurfaceViewDelegate a) {
        this.a = a;
    }
    
    public void surfaceChanged(final SurfaceHolder surfaceHolder, final int n, final int n2, final int n3) {
        super.surfaceChanged(surfaceHolder, n, n2, n3);
        if (this.a != null) {
            this.a.onSurfaceChanged(surfaceHolder, n, n2, n3);
        }
    }
    
    public void surfaceCreated(final SurfaceHolder surfaceHolder) {
        super.surfaceCreated(surfaceHolder);
        this.b = true;
    }
    
    public void surfaceDestroyed(final SurfaceHolder surfaceHolder) {
        this.b = false;
        super.surfaceDestroyed(surfaceHolder);
    }
    
    public interface CameraSurfaceViewDelegate
    {
        void onSurfaceChanged(final SurfaceHolder p0, final int p1, final int p2, final int p3);
    }
}
