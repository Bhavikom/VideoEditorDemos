// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.util.Iterator;
//import org.lasque.tusdk.core.utils.TLog;
import java.util.HashMap;

public class SelesEGLContextCache
{
    private final HashMap<String, SelesGLProgram> a;
    private final SelesFramebufferCache b;
    private final SelesEGLBufferCache c;
    
    public SelesEGLContextCache() {
        this.a = new HashMap<String, SelesGLProgram>();
        this.b = new SelesFramebufferCache();
        this.c = new SelesEGLBufferCache();
    }
    
    public SelesGLProgram getProgram(final String s, final String s2) {
        if (s == null || s2 == null) {
            return null;
        }
        final String format = String.format("V: %s - F: %s", s, s2);
        SelesGLProgram create = this.a.get(format);
        if (create == null) {
            create = SelesGLProgram.create(s, s2);
            this.a.put(format, create);
        }
        return create;
    }
    
    public SelesFramebufferCache sharedFramebufferCache() {
        return this.b;
    }
    
    public void returnFramebufferToCache(final SelesFramebuffer selesFramebuffer) {
        this.b.returnFramebufferToCache(selesFramebuffer);
    }
    
    public void recycleFramebuffer(final SelesFramebuffer selesFramebuffer) {
        this.b.recycleFramebuffer(selesFramebuffer);
    }
    
    public SelesEGLBufferCache sharedEGLBufferCache() {
        return this.c;
    }
    
    public void destory() {
        TLog.dump("%s destory() %s|%s", "SelesEGLContextCache", this, SelesContext.currentEGLContext());
        this.b.destory();
        this.c.destory();
        final Iterator<SelesGLProgram> iterator = this.a.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().destory();
        }
        this.a.clear();
    }
}
