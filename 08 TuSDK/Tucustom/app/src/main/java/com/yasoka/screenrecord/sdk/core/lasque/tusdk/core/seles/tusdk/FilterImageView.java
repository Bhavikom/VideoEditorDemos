// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk;

import android.opengl.GLES20;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.graphics.Color;
import android.graphics.Bitmap;
//import org.lasque.tusdk.core.seles.sources.SelesOutput;
//import org.lasque.tusdk.core.seles.SelesContext;
import android.view.MotionEvent;
import android.util.AttributeSet;
import android.content.Context;
import android.annotation.SuppressLint;
import android.view.View;
//import org.lasque.tusdk.core.seles.sources.SelesPicture;
import android.opengl.GLSurfaceView;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.output.SelesView;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesPicture;
//import org.lasque.tusdk.core.seles.output.SelesView;

public class FilterImageView extends SelesView implements GLSurfaceView.Renderer, FilterImageViewInterface
{
    private SelesPicture a;
    private FilterWrap b;
    @SuppressLint({ "ClickableViewAccessibility" })
    private View.OnTouchListener c;
    
    public FilterImageView(final Context context, final AttributeSet set, final int n) {
        super(context, set, n);
        this.c = (View.OnTouchListener)new View.OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                if (FilterImageView.this.a == null || FilterImageView.this.b == null || motionEvent.getPointerCount() > 1) {
                    return false;
                }
                switch (motionEvent.getAction()) {
                    case 0: {
                        FilterImageView.this.a();
                        break;
                    }
                    case 2: {
                        break;
                    }
                    default: {
                        FilterImageView.this.b();
                        break;
                    }
                }
                return true;
            }
        };
    }
    
    public FilterImageView(final Context context, final AttributeSet set) {
        super(context, set);
        this.c = (View.OnTouchListener)new View.OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                if (FilterImageView.this.a == null || FilterImageView.this.b == null || motionEvent.getPointerCount() > 1) {
                    return false;
                }
                switch (motionEvent.getAction()) {
                    case 0: {
                        FilterImageView.this.a();
                        break;
                    }
                    case 2: {
                        break;
                    }
                    default: {
                        FilterImageView.this.b();
                        break;
                    }
                }
                return true;
            }
        };
    }
    
    public FilterImageView(final Context context) {
        super(context);
        this.c = (View.OnTouchListener)new View.OnTouchListener() {
            public boolean onTouch(final View view, final MotionEvent motionEvent) {
                if (FilterImageView.this.a == null || FilterImageView.this.b == null || motionEvent.getPointerCount() > 1) {
                    return false;
                }
                switch (motionEvent.getAction()) {
                    case 0: {
                        FilterImageView.this.a();
                        break;
                    }
                    case 2: {
                        break;
                    }
                    default: {
                        FilterImageView.this.b();
                        break;
                    }
                }
                return true;
            }
        };
    }
    
    @Override
    protected void initView(final Context context, final AttributeSet set) {
        super.initView(context, set);
        this.setRenderer((GLSurfaceView.Renderer)this);
    }
    
    public void requestRender() {
        if (this.b != null) {
            this.b.submitFilterParameter();
        }
        super.requestRender();
    }
    
    @SuppressLint({ "ClickableViewAccessibility" })
    public void enableTouchForOrigin() {
        this.setOnTouchListener(this.c);
    }
    
    @SuppressLint({ "ClickableViewAccessibility" })
    public void disableTouchForOrigin() {
        this.setOnTouchListener((View.OnTouchListener)null);
    }
    
    public FilterWrap getFilterWrap() {
        return this.b;
    }
    
    public void setFilterWrap(final FilterWrap b) {
        if (this.b == null && b == null) {
            return;
        }
        if (b == null) {
            if (this.b.equalsCode("Normal")) {
                return;
            }
        }
        else if (b.equals(this.b)) {
            return;
        }
        this.b = b;
        if (this.b == null) {
            this.b = FilterLocalPackage.shared().getFilterWrap(null);
        }
        this.b.bindWithCameraView(this);
        this.b.processImage();
        if (this.a == null) {
            return;
        }
        this.a.removeAllTargets();
        this.b.addOrgin(this.a);
        this.a.processImage();
        this.requestRender();
    }
    
    public void setImage(final Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        this.a = new SelesPicture(bitmap);
        if (this.b == null) {
            (this.b = FilterLocalPackage.shared().getFilterWrap(null)).bindWithCameraView(this);
            this.b.processImage();
        }
        this.b.addOrgin(this.a);
        this.a.processImage();
        this.requestRender();
    }
    
    public void setImageBackgroundColor(final int n) {
        this.setBackgroundColor(Color.red(n) / 255.0f, Color.green(n) / 255.0f, Color.blue(n) / 255.0f, Color.alpha(n) / 255.0f);
    }
    
    private void a() {
        this.a.removeAllTargets();
        this.a.addTarget(this, 0);
        this.requestRender();
    }
    
    private void b() {
        this.a.removeAllTargets();
        this.b.addOrgin(this.a);
        this.requestRender();
    }
    
    public void onSurfaceCreated(final GL10 gl10, final EGLConfig eglConfig) {
        GLES20.glDisable(2929);
    }
    
    public void onSurfaceChanged(final GL10 gl10, final int n, final int n2) {
        GLES20.glViewport(0, 0, n, n2);
    }
    
    public void onDrawFrame(final GL10 gl10) {
        if (this.a == null) {
            return;
        }
        this.a.processImage();
        this.a.onDrawFrame(gl10);
    }
}
