// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.filters;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.SelesFramebuffer;

import java.nio.FloatBuffer;
//import org.lasque.tusdk.core.seles.SelesContext;
import java.util.Iterator;
//import org.lasque.tusdk.core.seles.SelesFramebuffer;
import java.util.ArrayList;

public class SelesFrameKeepFilter extends SelesFilter
{
    private final ArrayList<SelesFramebuffer> a;
    private int b;
    
    public SelesFrameKeepFilter() {
        super("varying highp vec2 textureCoordinate;uniform sampler2D inputImageTexture;void main(){     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);}");
        this.a = new ArrayList<SelesFramebuffer>();
        this.b = 1;
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        final Iterator<SelesFramebuffer> iterator = this.a.iterator();
        while (iterator.hasNext()) {
            iterator.next().unlock();
        }
        this.a.clear();
    }
    
    @Override
    public void newFrameReady(final long n, final int n2) {
        if (this.a.size() < this.b) {
            this.mOutputFramebuffer = this.mFirstInputFramebuffer;
            this.mFirstInputFramebuffer.lock();
        }
        else {
            this.mOutputFramebuffer = this.a.get(0);
            this.a.remove(0);
        }
        this.a.add(this.mFirstInputFramebuffer);
        this.runPendingOnDrawTasks();
        for (final SelesContext.SelesInput selesInput : this.mTargets) {
            if (selesInput != this.getTargetToIgnoreForUpdates()) {
                selesInput.setInputRotation(this.mInputRotation, this.mTargetTextureIndices.get(this.mTargets.indexOf(selesInput)));
            }
        }
        this.informTargetsAboutNewFrame(n);
    }
    
    @Override
    protected void renderToTexture(final FloatBuffer floatBuffer, final FloatBuffer floatBuffer2) {
    }
    
    public int getKeepSize() {
        return this.b;
    }
    
    public void setKeepSize(final int b) {
        if (b == this.b || b < 1) {
            return;
        }
        if (b > this.b) {
            for (int n = b - this.b, i = 0; i < n; ++i) {}
        }
        else {
            for (int n2 = this.b - b, j = 0; j < n2; ++j) {
                final SelesFramebuffer selesFramebuffer = this.a.get(this.a.size() - 1);
                this.a.remove(this.a.size() - 1);
                selesFramebuffer.unlock();
            }
        }
        this.b = b;
    }
}
