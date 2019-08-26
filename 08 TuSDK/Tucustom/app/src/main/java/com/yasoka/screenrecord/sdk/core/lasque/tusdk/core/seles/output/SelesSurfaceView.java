// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.output;

//import org.lasque.tusdk.core.secret.SdkValid;
import android.graphics.Canvas;
//import org.lasque.tusdk.core.utils.ThreadHelper;
import android.view.SurfaceHolder;
import android.util.AttributeSet;
import android.content.Context;
import android.opengl.GLSurfaceView;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.SdkValid;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ThreadHelper;

public class SelesSurfaceView extends GLSurfaceView
{
    public static final int Renderer_Max_FPS = 50;
    private int a;
    private int b;
    private int c;
    private boolean d;
    private boolean e;
    private SelesSurfaceViewDelegate f;
    private boolean g;
    private Runnable h;
    
    public SelesSurfaceView(final Context context, final AttributeSet set) {
        super(context, set);
        this.a = 50;
        this.b = 20;
        this.c = 0;
        this.d = true;
        this.h = new Runnable() {
            @Override
            public void run() {
                SelesSurfaceView.this.b();
            }
        };
        this.a();
    }
    
    public SelesSurfaceView(final Context context) {
        super(context);
        this.a = 50;
        this.b = 20;
        this.c = 0;
        this.d = true;
        this.h = new Runnable() {
            @Override
            public void run() {
                SelesSurfaceView.this.b();
            }
        };
        this.a();
    }
    
    private void a() {
        this.setWillNotDraw(false);
    }
    
    public boolean isCreated() {
        return this.e;
    }
    
    public boolean isPaused() {
        return this.g;
    }
    
    public void setDelegate(final SelesSurfaceViewDelegate f) {
        this.f = f;
    }
    
    public void surfaceChanged(final SurfaceHolder surfaceHolder, final int n, final int n2, final int n3) {
        super.surfaceChanged(surfaceHolder, n, n2, n3);
        this.postInvalidate();
        if (this.f != null) {
            this.f.onSurfaceChanged(surfaceHolder, n, n2, n3);
        }
    }
    
    public void surfaceCreated(final SurfaceHolder surfaceHolder) {
        super.surfaceCreated(surfaceHolder);
        this.g = false;
        this.e = true;
    }
    
    public void surfaceDestroyed(final SurfaceHolder surfaceHolder) {
        this.g = false;
        this.e = false;
        super.surfaceDestroyed(surfaceHolder);
    }
    
    public void onPause() {
        this.g = true;
        super.onPause();
    }
    
    public void onResume() {
        this.g = false;
        super.onResume();
        this.b();
    }
    
    public void setEnableFixedFrameRate(final boolean d) {
        this.d = d;
    }
    
    public boolean getEnableFixedFrameRate() {
        return this.d;
    }
    
    public void setRenderMode(final int c) {
        this.g = false;
        this.c = c;
        super.setRenderMode(0);
        this.b();
    }
    
    public int getRenderMode() {
        return this.c;
    }
    
    public int getRendererFPS() {
        return this.a;
    }
    
    public void setRendererFPS(final int a) {
        this.a = a;
        if (a < 1 || a > 50) {
            this.a = 50;
        }
        this.b = 1000 / this.a;
    }
    
    private void b() {
        if (this.c == 0 || this.g || this.b < 16) {
            return;
        }
        this.requestRender();
        if (this.getEnableFixedFrameRate()) {
            ThreadHelper.postDelayed(this.h, this.b);
        }
    }
    
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);
        SdkValid.shared.vaildAndDraw(canvas);
    }
    
    public interface SelesSurfaceViewDelegate
    {
        void onSurfaceChanged(final SurfaceHolder p0, final int p1, final int p2, final int p3);
    }
}
