// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources;

import java.nio.FloatBuffer;
import android.graphics.RectF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters.SelesFilter;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.seles.filters.SelesFilter;

public class SelesTextureReceiver extends SelesFilter
{
    private TuSdkSize a;
    private SelesVerticeCoordinateCorpBuilder b;
    private RectF c;
    private FloatBuffer d;
    private FloatBuffer e;
    
    public SelesTextureReceiver() {
        this.d = SelesFilter.buildBuffer(SelesFilter.imageVertices);
        this.e = SelesFilter.buildBuffer(SelesFilter.noRotationTextureCoordinates);
    }
    
    @Override
    protected void onInitOnGLThread() {
        super.onInitOnGLThread();
        this.checkGLError(this.getClass().getSimpleName() + " onInitOnGLThread");
    }
    
    public void setTextureCoordinateBuilder(final SelesVerticeCoordinateCorpBuilder b) {
        this.b = b;
        if (this.b != null && this.c != null) {
            this.b.setPreCropRect(this.c);
        }
    }
    
    public void setPreCropRect(final RectF c) {
        this.c = c;
        if (this.b != null) {
            this.b.setPreCropRect(this.c);
        }
    }
    
    public void newFrameReadyInGLThread(final long n) {
        if (this.b != null && this.b.calculate(this.mInputTextureSize, this.mInputRotation, this.d, this.e)) {
            this.a = this.b.outputSize();
        }
        else {
            this.e.clear();
            this.e.put(SelesFilter.textureCoordinates(this.mInputRotation)).position(0);
            this.a = this.mInputTextureSize;
        }
        this.renderToTexture(this.d, this.e);
        this.informTargetsAboutNewFrame(n);
    }
    
    @Override
    protected void renderToTexture(final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
        super.renderToTexture(floatBuffer, floatBuffer2);
        this.checkGLError(this.getClass().getSimpleName());
        this.captureFilterImage(this.getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
    }
    
    @Override
    public TuSdkSize outputFrameSize() {
        return (this.a == null) ? this.mInputTextureSize : this.a;
    }
}
