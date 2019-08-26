// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.engine;

//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.seles.sources.SelesOutput;
import android.graphics.RectF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebuffer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesTextureReceiver;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.seles.SelesFramebuffer;
//import org.lasque.tusdk.core.seles.sources.SelesTextureReceiver;

public class TuSdkEngineInputTextureImpl implements TuSdkEngineInputImage
{
    private TuSdkEngineOrientation a;
    private TuSdkEngineProcessor b;
    private SelesTextureReceiver c;
    private SelesFramebuffer d;
    
    @Override
    public void setEngineRotation(final TuSdkEngineOrientation a) {
        if (a == null) {
            return;
        }
        this.a = a;
    }
    
    @Override
    public void bindEngineProcessor(final TuSdkEngineProcessor b) {
        if (b == null) {
            return;
        }
        this.b = b;
        if (this.c == null) {
            TLog.w("%s bindEngineProcessor has released.", "TuSdkEngineInputTextureImpl");
            return;
        }
        this.c.addTarget(this.b.getInput(), 0);
    }
    
    @Override
    public void setTextureCoordinateBuilder(final SelesVerticeCoordinateCorpBuilder textureCoordinateBuilder) {
        if (this.c == null) {
            return;
        }
        this.c.setTextureCoordinateBuilder(textureCoordinateBuilder);
    }
    
    @Override
    public void setPreCropRect(final RectF preCropRect) {
        if (this.c == null) {
            return;
        }
        this.c.setPreCropRect(preCropRect);
    }
    
    @Override
    public SelesOutput getOutput() {
        return this.c;
    }
    
    public TuSdkEngineInputTextureImpl() {
        this.c = new SelesTextureReceiver();
    }
    
    @Override
    public void release() {
        if (this.c != null) {
            this.c.destroy();
            this.c = null;
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
    public void processFrame(final int n, final int n2, final int n3, final byte[] array, final long n4) {
        if (this.c == null) {
            TLog.w("%s processFrame has released.", "TuSdkEngineInputTextureImpl");
            return;
        }
        if (this.b == null) {
            TLog.w("%s processFrame need bindEngineProcessor first.", "TuSdkEngineInputTextureImpl");
            return;
        }
        this.b.willProcessFrame(n4);
        if (this.d == null || this.d.getTexture() != n) {
            this.d = SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.HOLDER, null, n);
        }
        this.c.setInputFramebuffer(this.d, 0);
        if (this.a != null) {
            this.c.setInputRotation(this.a.getInputRotation(), 0);
            this.c.setInputSize(this.a.getInputSize(), 0);
        }
        else {
            this.c.setInputSize(TuSdkSize.create(n2, n3), 0);
        }
        this.c.newFrameReadyInGLThread(n4);
    }
}
