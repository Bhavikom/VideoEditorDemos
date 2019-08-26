// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.ColorSpaceConvert;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebuffer;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

//import org.lasque.tusdk.core.secret.ColorSpaceConvert;
//import org.lasque.tusdk.core.seles.SelesFramebuffer;
//import org.lasque.tusdk.core.utils.TLog;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import java.util.LinkedHashMap;
//import org.lasque.tusdk.core.seles.SelesContext;
import java.util.Map;
import java.nio.IntBuffer;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class SelesYuvDataReceiver extends SelesOutput
{
    private boolean a;
    private ImageOrientation b;
    private IntBuffer c;
    private boolean d;
    private final Map<SelesContext.SelesInput, Integer> e;
    
    public SelesYuvDataReceiver() {
        this.b = ImageOrientation.Up;
        this.e = new LinkedHashMap<SelesContext.SelesInput, Integer>();
    }
    
    @Override
    protected void onDestroy() {
        this.a();
    }
    
    private void a() {
        if (this.mOutputFramebuffer != null) {
            this.mOutputFramebuffer.enableReferenceCounting();
            this.mOutputFramebuffer.unlock();
            this.mOutputFramebuffer = null;
        }
    }
    
    public void setInputSize(final TuSdkSize tuSdkSize) {
        if (tuSdkSize == null || !tuSdkSize.isSize() || tuSdkSize.equals(this.mInputTextureSize)) {
            return;
        }
        this.mInputTextureSize = TuSdkSize.create(tuSdkSize);
        this.a = true;
    }
    
    public void setInputRotation(final ImageOrientation b) {
        if (b == null || b == this.b) {
            return;
        }
        this.b = b;
        this.a = true;
    }
    
    public void processFrameData(final byte[] array) {
        if (array == null) {
            TLog.w("%s processFrameNV21Data need data.", "SelesYuvDataReceiver");
            return;
        }
        if (this.d) {
            return;
        }
        this.d = true;
        this.runOnDraw(new Runnable() {
            @Override
            public void run() {
                SelesYuvDataReceiver.this.b();
                if (SelesYuvDataReceiver.this.mOutputFramebuffer == null) {
                    TLog.e("%s newFrameReady need setInputSize[%s] or setInputRotation[%s] first.", "SelesYuvDataReceiver", SelesYuvDataReceiver.this.mInputTextureSize, SelesYuvDataReceiver.this.b);
                    return;
                }
                SelesYuvDataReceiver.this.colorConvert(array, SelesYuvDataReceiver.this.mInputTextureSize.width, SelesYuvDataReceiver.this.mInputTextureSize.height, SelesYuvDataReceiver.this.c.array());
                SelesYuvDataReceiver.this.mOutputFramebuffer.freshTextureRgba(SelesYuvDataReceiver.this.c);
            }
        });
    }
    
    private void b() {
        if (!this.a) {
            return;
        }
        this.c = IntBuffer.allocate(this.mInputTextureSize.width * this.mInputTextureSize.height);
        this.a();
        (this.mOutputFramebuffer = SelesContext.sharedFramebufferCache().fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode.TEXTURE, this.mInputTextureSize)).bindTextureRgbaHolder(false);
    }
    
    protected void colorConvert(final byte[] array, final int n, final int n2, final int[] array2) {
        ColorSpaceConvert.nv21ToRgba(array, n, n2, array2);
    }
    
    public void newFrameReady(final long n) {
        this.runPendingOnDrawTasks();
        this.a(n);
        this.d = false;
    }
    
    private void a(final long n) {
        this.e.clear();
        for (final SelesContext.SelesInput selesInput : this.mTargets) {
            if (!selesInput.isEnabled()) {
                continue;
            }
            if (selesInput == this.getTargetToIgnoreForUpdates()) {
                continue;
            }
            final int intValue = this.mTargetTextureIndices.get(this.mTargets.indexOf(selesInput));
            this.e.put(selesInput, intValue);
            this.setInputFramebufferForTarget(selesInput, intValue);
            selesInput.setInputRotation(this.b, intValue);
            selesInput.setInputSize(this.mInputTextureSize, intValue);
        }
        for (final Map.Entry<SelesContext.SelesInput, Integer> entry : this.e.entrySet()) {
            entry.getKey().newFrameReady(n, entry.getValue());
        }
    }
}
