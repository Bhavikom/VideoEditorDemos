// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.engine;

//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.seles.SelesContext;
import android.graphics.RectF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;

public class TuSdkEngineImpl implements TuSdkEngine
{
    private boolean a;
    private TuSdkEngineOrientation b;
    private TuSdkEngineInputImage c;
    private TuSdkEngineOutputImage d;
    private TuSdkEngineProcessor e;
    private boolean f;
    private boolean g;
    private SelesVerticeCoordinateCorpBuilder h;
    private RectF i;
    
    @Override
    public void setEngineInputImage(final TuSdkEngineInputImage c) {
        if (c == null) {
            return;
        }
        if (this.c != null) {
            this.c.release();
        }
        (this.c = c).setTextureCoordinateBuilder(this.h);
        this.c.setPreCropRect(this.i);
        this.c.setEngineRotation(this.b);
    }
    
    @Override
    public void setEngineOutputImage(final TuSdkEngineOutputImage d) {
        if (d == null) {
            return;
        }
        if (this.d != null) {
            this.d.release();
        }
        (this.d = d).setEngineRotation(this.b);
    }
    
    @Override
    public void setEngineOrientation(final TuSdkEngineOrientation b) {
        if (b == null) {
            return;
        }
        if (this.b != null) {
            this.b.release();
        }
        this.b = b;
    }
    
    @Override
    public void setEngineProcessor(final TuSdkEngineProcessor e) {
        if (e == null) {
            return;
        }
        if (this.e != null) {
            this.e.release();
        }
        (this.e = e).setEngineRotation(this.b);
    }
    
    @Override
    public void setInputTextureCoordinateBuilder(final SelesVerticeCoordinateCorpBuilder h) {
        this.h = h;
        if (this.c != null) {
            this.c.setTextureCoordinateBuilder(this.h);
        }
    }
    
    @Override
    public void setInputPreCropRect(final RectF i) {
        this.i = i;
        if (this.c != null) {
            this.c.setPreCropRect(this.i);
        }
    }
    
    public TuSdkEngineImpl(final boolean f) {
        this.a = false;
        this.g = false;
        this.f = f;
    }
    
    @Override
    public void release() {
        if (this.a) {
            return;
        }
        this.a = true;
        if (this.c != null) {
            this.c.release();
            this.c = null;
        }
        if (this.d != null) {
            this.d.release();
            this.d = null;
        }
        if (this.b != null) {
            this.b.release();
            this.b = null;
        }
        if (this.e != null) {
            this.e.release();
            this.e = null;
        }
        if (this.f) {
            SelesContext.destroyContext(SelesContext.currentEGLContext());
        }
    }
    
    @Override
    protected void finalize() {
        this.release();
        try {
            super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    
    @Override
    public boolean prepareInGlThread() {
        if (!this.a("prepare")) {
            return false;
        }
        if (this.g) {
            return true;
        }
        this.g = true;
        if (this.f) {
            SelesContext.createEGLContext(SelesContext.currentEGLContext());
        }
        this.c.setEngineRotation(this.b);
        this.e.setEngineRotation(this.b);
        this.d.setEngineRotation(this.b);
        this.c.bindEngineProcessor(this.e);
        this.e.bindEngineOutput(this.d);
        return true;
    }
    
    private boolean a(final String s) {
        if (this.a) {
            TLog.w("%s %s has released.", "TuSdkEngineImpl", s);
            return false;
        }
        if (this.b == null) {
            TLog.w("%s %s need setEngineOrientation first.", "TuSdkEngineImpl", s);
            return false;
        }
        if (this.c == null) {
            TLog.w("%s %s need setEngineInputImage first.", "TuSdkEngineImpl", s);
            return false;
        }
        if (this.e == null) {
            TLog.w("%s %s need setEngineProcessor first.", "TuSdkEngineImpl", s);
            return false;
        }
        if (this.d == null) {
            TLog.w("%s %s need setEngineOutputImage first.", "TuSdkEngineImpl", s);
            return false;
        }
        return true;
    }
    
    @Override
    public void processFrame(final byte[] array, final int n, final int n2, final long n3) {
        this.processFrame(-1, n, n2, array, n3);
    }
    
    @Override
    public void processFrame(final int n, final int n2, final int n3, final long n4) {
        this.processFrame(n, n2, n3, null, n4);
    }
    
    @Override
    public void processFrame(final int n, final int n2, final int n3, final byte[] array, final long n4) {
        if (!this.a("processFrame")) {
            return;
        }
        this.b.setInputSize(n2, n3);
        this.c.processFrame(n, n2, n3, array, n4);
    }
}
