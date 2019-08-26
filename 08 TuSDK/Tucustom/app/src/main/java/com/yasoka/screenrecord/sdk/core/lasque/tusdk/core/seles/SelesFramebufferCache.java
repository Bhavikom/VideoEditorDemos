// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles;

import android.opengl.GLES20;
//import org.lasque.tusdk.core.utils.ArrayHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ArrayHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.util.concurrent.LinkedBlockingQueue;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.utils.TLog;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.Map;

public class SelesFramebufferCache
{
    private final Map<String, BlockingQueue<SelesFramebuffer>> a;
    private final List<Integer> b;
    private final List<Integer> c;
    private final List<Integer> d;
    private final List<Integer> e;
    private final List<Integer> f;
    private final List<Integer> g;
    private int h;
    
    public SelesFramebufferCache() {
        this.a = Collections.synchronizedMap(new HashMap<String, BlockingQueue<SelesFramebuffer>>());
        this.b = new ArrayList<Integer>();
        this.c = new ArrayList<Integer>();
        this.d = new ArrayList<Integer>();
        this.e = new ArrayList<Integer>();
        this.f = new ArrayList<Integer>();
        this.g = new ArrayList<Integer>();
        TLog.dump("%s create() %s|%s", "SelesFramebufferCache", this, SelesContext.currentEGLContext());
    }
    
    private String a(TuSdkSize create, final SelesFramebuffer.SelesTextureOptions selesTextureOptions, final SelesFramebuffer.SelesFramebufferMode selesFramebufferMode) {
        if (create == null) {
            create = TuSdkSize.create(0);
        }
        return String.format("%sx%s-%s:%s:%s:%s:%s:%s:%s-%s", create.width, create.height, selesTextureOptions.minFilter, selesTextureOptions.magFilter, selesTextureOptions.wrapS, selesTextureOptions.wrapT, selesTextureOptions.internalFormat, selesTextureOptions.format, selesTextureOptions.type, selesFramebufferMode);
    }
    
    public SelesFramebuffer fetchFramebuffer(final TuSdkSize tuSdkSize, final boolean b) {
        return this.fetchFramebuffer(tuSdkSize, new SelesFramebuffer.SelesTextureOptions(), b);
    }
    
    public SelesFramebuffer fetchFramebuffer(final TuSdkSize tuSdkSize, final SelesFramebuffer.SelesTextureOptions selesTextureOptions, final boolean b) {
        SelesFramebuffer.SelesFramebufferMode selesFramebufferMode = SelesFramebuffer.SelesFramebufferMode.FBO_AND_TEXTURE;
        if (b) {
            selesFramebufferMode = SelesFramebuffer.SelesFramebufferMode.TEXTURE_ACTIVE;
        }
        return this.fetchFramebuffer(selesFramebufferMode, tuSdkSize, 0, selesTextureOptions);
    }
    
    public SelesFramebuffer fetchTextureOES() {
        return this.fetchTexture(TuSdkSize.create(0), false, true);
    }
    
    public SelesFramebuffer fetchTexture(final TuSdkSize tuSdkSize, final boolean b) {
        return this.fetchTexture(tuSdkSize, b, false);
    }
    
    public SelesFramebuffer fetchTexture(final TuSdkSize tuSdkSize, final boolean b, final boolean b2) {
        return this.fetchTexture(tuSdkSize, new SelesFramebuffer.SelesTextureOptions(), b, b2);
    }
    
    public SelesFramebuffer fetchTexture(final TuSdkSize tuSdkSize, final SelesFramebuffer.SelesTextureOptions selesTextureOptions, final boolean b, final boolean b2) {
        SelesFramebuffer.SelesFramebufferMode selesFramebufferMode = SelesFramebuffer.SelesFramebufferMode.TEXTURE;
        if (b2) {
            selesFramebufferMode = SelesFramebuffer.SelesFramebufferMode.TEXTURE_OES;
        }
        else if (b) {
            selesFramebufferMode = SelesFramebuffer.SelesFramebufferMode.TEXTURE_ACTIVE;
        }
        return this.fetchFramebuffer(selesFramebufferMode, tuSdkSize, 0, selesTextureOptions);
    }
    
    public SelesFramebuffer fetchFramebuffer(final SelesFramebuffer.SelesFramebufferMode selesFramebufferMode, final TuSdkSize tuSdkSize) {
        return this.fetchFramebuffer(selesFramebufferMode, tuSdkSize, new SelesFramebuffer.SelesTextureOptions());
    }
    
    public SelesFramebuffer fetchFramebuffer(final SelesFramebuffer.SelesFramebufferMode selesFramebufferMode, final TuSdkSize tuSdkSize, final int n) {
        return this.fetchFramebuffer(selesFramebufferMode, tuSdkSize, n, new SelesFramebuffer.SelesTextureOptions());
    }
    
    public SelesFramebuffer fetchFramebuffer(final SelesFramebuffer.SelesFramebufferMode selesFramebufferMode, final TuSdkSize tuSdkSize, final SelesFramebuffer.SelesTextureOptions selesTextureOptions) {
        return this.fetchFramebuffer(selesFramebufferMode, tuSdkSize, 0, selesTextureOptions);
    }
    
    public SelesFramebuffer fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode holder, final TuSdkSize tuSdkSize, final int n, final SelesFramebuffer.SelesTextureOptions selesTextureOptions) {
        if (holder == null) {
            holder = SelesFramebuffer.SelesFramebufferMode.HOLDER;
        }
        this.clearRecycle();
        SelesFramebuffer a = this.a(this.a(tuSdkSize, selesTextureOptions, holder));
        if (a == null) {
            a = new SelesFramebuffer(holder, tuSdkSize, n, selesTextureOptions);
            if (holder != SelesFramebuffer.SelesFramebufferMode.HOLDER) {
                ++this.h;
            }
        }
        a.lock();
        this.a(a);
        return a;
    }
    
    private SelesFramebuffer a(final String s) {
        SelesFramebuffer selesFramebuffer = null;
        final BlockingQueue<SelesFramebuffer> b = this.b(s);
        while (selesFramebuffer == null && !b.isEmpty()) {
            try {
                selesFramebuffer = b.take();
            }
            catch (InterruptedException ex) {
                TLog.e(ex, "%s fetchFramebuffer: %s", this.getClass(), s);
            }
        }
        return selesFramebuffer;
    }
    
    private BlockingQueue<SelesFramebuffer> b(final String s) {
        BlockingQueue<SelesFramebuffer> blockingQueue = this.a.get(s);
        if (blockingQueue == null) {
            blockingQueue = new LinkedBlockingQueue<SelesFramebuffer>();
            this.a.put(s, blockingQueue);
        }
        return blockingQueue;
    }
    
    public void returnFramebufferToCache(final SelesFramebuffer selesFramebuffer) {
        if (selesFramebuffer == null || selesFramebuffer.getModel() == SelesFramebuffer.SelesFramebufferMode.HOLDER) {
            return;
        }
        selesFramebuffer.clearAllLocks();
        this.b(selesFramebuffer);
        this.b(this.a(selesFramebuffer.getSize(), selesFramebuffer.getTextureOptions(), selesFramebuffer.getModel())).add(selesFramebuffer);
    }
    
    private void a(final SelesFramebuffer selesFramebuffer) {
        if (selesFramebuffer.getModel() == SelesFramebuffer.SelesFramebufferMode.HOLDER) {
            return;
        }
        if (selesFramebuffer.getTexture() != 0 && !this.e.contains(selesFramebuffer.getTexture())) {
            this.e.add(selesFramebuffer.getTexture());
        }
        if (selesFramebuffer.getFramebuffer() != 0 && !this.f.contains(selesFramebuffer.getFramebuffer())) {
            this.f.add(selesFramebuffer.getFramebuffer());
        }
        if (selesFramebuffer.getRenderbuffer() != 0 && !this.f.contains(selesFramebuffer.getRenderbuffer())) {
            this.g.add(selesFramebuffer.getRenderbuffer());
        }
    }
    
    private void b(final SelesFramebuffer selesFramebuffer) {
        this.e.remove((Object)selesFramebuffer.getTexture());
        this.f.remove((Object)selesFramebuffer.getFramebuffer());
        this.g.remove((Object)selesFramebuffer.getRenderbuffer());
    }
    
    public void recycleFramebuffer(final SelesFramebuffer selesFramebuffer) {
        if (selesFramebuffer == null) {
            return;
        }
        this.b(selesFramebuffer);
        this.c(selesFramebuffer);
        if (selesFramebuffer.getEglContext().equalsCurrentThread()) {
            this.clearRecycle();
        }
    }
    
    private void c(final SelesFramebuffer selesFramebuffer) {
        if (selesFramebuffer == null) {
            return;
        }
        selesFramebuffer.flagDestory();
        this.d(selesFramebuffer);
        this.e(selesFramebuffer);
        this.f(selesFramebuffer);
    }
    
    private void d(final SelesFramebuffer selesFramebuffer) {
        if (selesFramebuffer.getFramebuffer() == 0 || this.c.contains(selesFramebuffer.getFramebuffer())) {
            return;
        }
        this.c.add(selesFramebuffer.getFramebuffer());
    }
    
    private void e(final SelesFramebuffer selesFramebuffer) {
        if (selesFramebuffer.getTexture() == 0 || this.b.contains(selesFramebuffer.getTexture())) {
            return;
        }
        this.b.add(selesFramebuffer.getTexture());
    }
    
    private void f(final SelesFramebuffer selesFramebuffer) {
        if (selesFramebuffer.getRenderbuffer() == 0 || this.d.contains(selesFramebuffer.getRenderbuffer())) {
            return;
        }
        this.d.add(selesFramebuffer.getRenderbuffer());
    }
    
    public void clearRecycle() {
        if (this.b.size() > 0) {
            this.a(ArrayHelper.toIntArray(this.b));
            this.b.clear();
        }
        if (this.c.size() > 0) {
            this.b(ArrayHelper.toIntArray(this.c));
            this.c.clear();
        }
        if (this.d.size() > 0) {
            this.c(ArrayHelper.toIntArray(this.d));
            this.d.clear();
        }
    }
    
    private void a(final int[] array) {
        if (array == null) {
            return;
        }
        GLES20.glDeleteTextures(array.length, array, 0);
    }
    
    private void b(final int[] array) {
        if (array == null) {
            return;
        }
        GLES20.glDeleteFramebuffers(array.length, array, 0);
    }
    
    private void c(final int[] array) {
        if (array == null) {
            return;
        }
        GLES20.glDeleteRenderbuffers(array.length, array, 0);
    }
    
    public void destory() {
        for (final BlockingQueue<SelesFramebuffer> blockingQueue : this.a.values()) {
            while (!blockingQueue.isEmpty()) {
                try {
                    this.c(blockingQueue.take());
                }
                catch (InterruptedException ex) {
                    TLog.e(ex);
                }
            }
        }
        this.a.clear();
        this.clearRecycle();
        if (this.e.size() > 0) {
            this.a(ArrayHelper.toIntArray(this.e));
            this.e.clear();
        }
        if (this.f.size() > 0) {
            this.b(ArrayHelper.toIntArray(this.f));
            this.f.clear();
        }
        if (this.g.size() > 0) {
            this.c(ArrayHelper.toIntArray(this.g));
            this.g.clear();
        }
        TLog.dump("%s destory() %s|%s", "SelesFramebufferCache", this, SelesContext.currentEGLContext());
    }
}
