// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.extend;

//import org.lasque.tusdk.core.utils.TLog;
import android.os.Handler;
import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

public class SelesSurfaceTexture extends SurfaceTexture
{
    private long a;
    private boolean b;
    private OnFrameAvailableListener c;
    private int d;
    private Object e;
    private OnFrameAvailableListener f;
    
    public long getDefindTimestamp() {
        return this.a;
    }
    
    public void setDefindTimestamp(final long a) {
        this.a = a;
        this.b = true;
    }
    
    public boolean hasNewFrameNeedUpdate() {
        final int d;
        synchronized (this.e) {
            d = this.d;
        }
        return d > 0;
    }
    
    public SelesSurfaceTexture(final int n) {
        super(n);
        this.a = -1L;
        this.b = false;
        this.d = 0;
        this.e = new Object();
        this.f = (OnFrameAvailableListener)new OnFrameAvailableListener() {
            public void onFrameAvailable(final SurfaceTexture surfaceTexture) {
                SelesSurfaceTexture.this.a();
                if (SelesSurfaceTexture.this.c != null) {
                    SelesSurfaceTexture.this.c.onFrameAvailable((SurfaceTexture)SelesSurfaceTexture.this);
                }
            }
        };
    }
    
    @TargetApi(21)
    public SelesSurfaceTexture(final int n, final boolean b) {
        super(n, b);
        this.a = -1L;
        this.b = false;
        this.d = 0;
        this.e = new Object();
        this.f = (OnFrameAvailableListener)new OnFrameAvailableListener() {
            public void onFrameAvailable(final SurfaceTexture surfaceTexture) {
                SelesSurfaceTexture.this.a();
                if (SelesSurfaceTexture.this.c != null) {
                    SelesSurfaceTexture.this.c.onFrameAvailable((SurfaceTexture)SelesSurfaceTexture.this);
                }
            }
        };
    }
    
    @TargetApi(26)
    public SelesSurfaceTexture(final boolean b) {
        super(b);
        this.a = -1L;
        this.b = false;
        this.d = 0;
        this.e = new Object();
        this.f = (OnFrameAvailableListener)new OnFrameAvailableListener() {
            public void onFrameAvailable(final SurfaceTexture surfaceTexture) {
                SelesSurfaceTexture.this.a();
                if (SelesSurfaceTexture.this.c != null) {
                    SelesSurfaceTexture.this.c.onFrameAvailable((SurfaceTexture)SelesSurfaceTexture.this);
                }
            }
        };
    }
    
    public long getTimestamp() {
        if (this.b) {
            return this.getDefindTimestamp();
        }
        return super.getTimestamp();
    }
    
    public void updateTexImage() {
        if (!this.hasNewFrameNeedUpdate()) {
            return;
        }
        this.b();
        try {
            super.updateTexImage();
        }
        catch (Exception ex) {}
    }
    
    public void forceUpdateTexImage() {
        if (this.hasNewFrameNeedUpdate()) {
            this.b();
        }
        try {
            super.updateTexImage();
        }
        catch (Exception ex) {}
    }
    
    private void a() {
        synchronized (this.e) {
            ++this.d;
        }
    }
    
    private void b() {
        synchronized (this.e) {
            --this.d;
        }
    }
    
    public void setOnFrameAvailableListener(final OnFrameAvailableListener c, final Handler handler) {
        this.c = c;
        OnFrameAvailableListener f = null;
        if (this.c != null) {
            f = this.f;
        }
        super.setOnFrameAvailableListener(f, handler);
    }
    
    public void release() {
        try {
            super.release();
        }
        catch (Exception ex) {
            TLog.e("%s release error.", "SelesSurfaceTexture");
        }
    }
}
