// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.api.engine;

//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.seles.SelesContext;
//import org.lasque.tusdk.core.seles.sources.SelesOutput;
import android.graphics.RectF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesTextureReceiver;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesYuvDataReceiver;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.seles.sources.SelesTextureReceiver;
//import org.lasque.tusdk.core.seles.sources.SelesYuvDataReceiver;

public class TuSdkEngineInputYUVDataImpl implements TuSdkEngineInputImage
{
    private TuSdkEngineOrientation a;
    private TuSdkEngineProcessor b;
    private SelesYuvDataReceiver c;
    private SelesTextureReceiver d;
    
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
        if (this.d == null) {
            TLog.w("%s bindEngineProcessor has released.", "TuSdkEngineInputYUVDataImpl");
            return;
        }
        this.d.addTarget(this.b.getInput(), 0);
    }
    
    @Override
    public void setTextureCoordinateBuilder(final SelesVerticeCoordinateCorpBuilder textureCoordinateBuilder) {
        if (this.d == null) {
            return;
        }
        this.d.setTextureCoordinateBuilder(textureCoordinateBuilder);
    }
    
    @Override
    public void setPreCropRect(final RectF preCropRect) {
        if (this.d == null) {
            return;
        }
        this.d.setPreCropRect(preCropRect);
    }
    
    @Override
    public SelesOutput getOutput() {
        return this.d;
    }
    
    public TuSdkEngineInputYUVDataImpl() {
        this.c = new SelesYuvDataReceiver();
        this.d = new SelesTextureReceiver();
        this.c.addTarget(this.d, 0);
    }
    
    @Override
    public void release() {
        if (this.c != null) {
            this.c.destroy();
            this.c = null;
        }
        if (this.d != null) {
            this.d.destroy();
            this.d = null;
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
        if (array == null) {
            TLog.w("%s processFrame need yuv data.", "TuSdkEngineInputYUVDataImpl");
            return;
        }
        if (this.c == null) {
            TLog.w("%s processFrame has released.", "TuSdkEngineInputYUVDataImpl");
            return;
        }
        if (this.b == null) {
            TLog.w("%s processFrame need bindEngineProcessor first.", "TuSdkEngineInputYUVDataImpl");
            return;
        }
        this.b.willProcessFrame(n4);
        if (this.a != null) {
            this.c.setInputRotation(this.a.getInputRotation());
            this.c.setInputSize(this.a.getInputSize());
        }
        else {
            this.c.setInputSize(TuSdkSize.create(n2, n3));
        }
        this.c.processFrameData(array);
        this.c.newFrameReady(n4);
    }
}
