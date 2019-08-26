// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles;

import javax.microedition.khronos.egl.EGLContext;
//import org.lasque.tusdk.core.seles.egl.SelesEGL10Core;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.egl.EGL10;
import android.annotation.TargetApi;
import java.nio.Buffer;
import java.nio.ByteBuffer;
//import org.lasque.tusdk.core.utils.image.BitmapHelper;
import android.opengl.GLUtils;
import android.opengl.GLES20;
import android.graphics.Bitmap;
//import org.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.egl.SelesEGL10Core;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.egl.SelesEGLContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.BitmapHelper;

import java.nio.IntBuffer;
//import org.lasque.tusdk.core.seles.egl.SelesEGLContext;
//import org.lasque.tusdk.core.struct.TuSdkSize;

public class SelesFramebuffer
{
    private int a;
    private int b;
    private int c;
    private SelesFramebufferMode d;
    private int e;
    private boolean f;
    private TuSdkSize g;
    private SelesTextureOptions h;
    private boolean i;
    private SelesEGLContext j;
    private boolean k;
    private IntBuffer l;
    
    public int getTexture() {
        return this.c;
    }
    
    public int getFramebuffer() {
        return this.a;
    }
    
    public int getRenderbuffer() {
        return this.b;
    }
    
    public SelesFramebufferMode getModel() {
        return this.d;
    }
    
    public TuSdkSize getSize() {
        return this.g;
    }
    
    public SelesTextureOptions getTextureOptions() {
        return this.h;
    }
    
    public boolean isMissingFramebuffer() {
        return this.i;
    }
    
    public SelesEGLContext getEglContext() {
        return this.j;
    }
    
    public void flagDestory() {
        this.k = true;
    }
    
    public boolean isDestory() {
        return this.k;
    }
    
    public SelesFramebuffer(SelesFramebufferMode holder, final TuSdkSize g, final int c, final SelesTextureOptions selesTextureOptions) {
        this.d = SelesFramebufferMode.HOLDER;
        this.k = false;
        if (holder == null) {
            holder = SelesFramebufferMode.HOLDER;
        }
        this.d = holder;
        this.h = ((selesTextureOptions != null) ? selesTextureOptions : new SelesTextureOptions());
        this.g = g;
        this.e = 0;
        this.j = new SelesEGLContext();
        this.c = c;
        this.k = (this.d == SelesFramebufferMode.HOLDER);
        this.i = (this.d.getTypeId() <= SelesFramebufferMode.TEXTURE_OES.getTypeId());
        this.f = this.i;
        if (this.d.getTypeId() <= SelesFramebufferMode.PACKAGE.getTypeId()) {
            return;
        }
        if (this.d.getTypeId() <= SelesFramebufferMode.TEXTURE_ACTIVE.getTypeId()) {
            this.a((this.d == SelesFramebufferMode.TEXTURE_ACTIVE) ? 33985 : 0);
        }
        else if (this.d == SelesFramebufferMode.TEXTURE_OES) {
            this.b(0);
        }
        else if (this.d == SelesFramebufferMode.FBO_AND_TEXTURE) {
            this.a();
        }
        else if (this.d == SelesFramebufferMode.FBO_AND_TEXTURE_AND_RENDER) {
            this.b();
        }
        TLog.dump("%s create()  fId:%s texId:%s  %s|%s", "SelesFramebuffer", this.a, this.c, this, SelesContext.currentEGLContext());
    }
    
    public void bindTexture(final Bitmap bitmap) {
        this.bindTexture(bitmap, false);
    }
    
    public void bindTexture(final Bitmap bitmap, final boolean b) {
        this.bindTexture(bitmap, false, b);
    }
    
    public void bindTexture(final Bitmap bitmap, final boolean b, final boolean b2) {
        if (bitmap == null || bitmap.isRecycled()) {
            TLog.w("%s bindTexture image is Null or Recycled, %s", "SelesFramebuffer", bitmap);
            return;
        }
        GLES20.glBindTexture(3553, this.c);
        if (b) {
            GLES20.glTexParameteri(3553, 10241, 9987);
        }
        GLUtils.texImage2D(3553, 0, bitmap, 0);
        if (b) {
            GLES20.glGenerateMipmap(3553);
        }
        GLES20.glBindTexture(3553, 0);
        if (b2) {
            BitmapHelper.recycled(bitmap);
        }
    }
    
    public void bindTextureLuminance(final ByteBuffer byteBuffer, final int n, final int n2, final boolean b) {
        if (byteBuffer == null) {
            TLog.w("%s bindTextureLuminance imageData is Null", "SelesFramebuffer");
            return;
        }
        GLES20.glBindTexture(3553, this.c);
        if (b) {
            GLES20.glTexParameteri(3553, 10241, 9987);
        }
        GLES20.glTexImage2D(3553, 0, 6409, n, n2, 0, 6409, 5121, (Buffer)byteBuffer);
        if (b) {
            GLES20.glGenerateMipmap(3553);
        }
        GLES20.glBindTexture(3553, 0);
    }
    
    public void bindTextureRgbaHolder(final boolean b) {
        GLES20.glBindTexture(3553, this.c);
        if (b) {
            GLES20.glTexParameteri(3553, 10241, 9987);
        }
        GLES20.glTexImage2D(3553, 0, 6408, this.g.width, this.g.height, 0, 6408, 5121, (Buffer)null);
        if (b) {
            GLES20.glGenerateMipmap(3553);
        }
        GLES20.glBindTexture(3553, 0);
    }
    
    public void freshTextureRgba(final Buffer buffer) {
        if (buffer == null) {
            return;
        }
        GLES20.glBindTexture(3553, this.c);
        GLES20.glTexSubImage2D(3553, 0, 0, 0, this.g.width, this.g.height, 6408, 5121, buffer);
        GLES20.glBindTexture(3553, 0);
    }
    
    private void a(final int n) {
        final int[] array = { 0 };
        if (n != 0) {
            GLES20.glActiveTexture(n);
        }
        GLES20.glGenTextures(1, array, 0);
        GLES20.glBindTexture(3553, array[0]);
        GLES20.glTexParameteri(3553, 10241, this.h.minFilter);
        GLES20.glTexParameteri(3553, 10240, this.h.magFilter);
        GLES20.glTexParameteri(3553, 10242, this.h.wrapS);
        GLES20.glTexParameteri(3553, 10243, this.h.wrapT);
        this.c = array[0];
    }
    
    @TargetApi(15)
    private void b(final int n) {
        final int[] array = { 0 };
        if (n != 0) {
            GLES20.glActiveTexture(n);
        }
        GLES20.glGenTextures(1, array, 0);
        GLES20.glBindTexture(36197, array[0]);
        GLES20.glTexParameteri(36197, 10241, 9728);
        GLES20.glTexParameteri(36197, 10240, this.h.magFilter);
        GLES20.glTexParameteri(36197, 10242, this.h.wrapS);
        GLES20.glTexParameteri(36197, 10243, this.h.wrapT);
        this.c = array[0];
    }
    
    private void a() {
        final int[] array = { 0 };
        GLES20.glGenFramebuffers(1, array, 0);
        GLES20.glBindFramebuffer(36160, array[0]);
        this.a(33985);
        GLES20.glBindTexture(3553, this.c);
        GLES20.glTexImage2D(3553, 0, this.h.internalFormat, this.g.width, this.g.height, 0, this.h.format, this.h.type, (Buffer)null);
        GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.c, 0);
        this.a = array[0];
        this.a("generateFramebuffer");
        GLES20.glBindTexture(3553, 0);
    }
    
    private void b() {
        this.a();
        final int[] array = { 0 };
        GLES20.glGenRenderbuffers(1, array, 0);
        GLES20.glBindRenderbuffer(36161, this.b = array[0]);
        GLES20.glRenderbufferStorage(36161, 33189, this.g.width, this.g.height);
        GLES20.glBindFramebuffer(36160, this.a);
        GLES20.glFramebufferRenderbuffer(36160, 36096, 36161, this.b);
        GLES20.glBindRenderbuffer(36161, 0);
        this.a("generateRenderbuffer");
    }
    
    private void a(final String s) {
        final int glCheckFramebufferStatus = GLES20.glCheckFramebufferStatus(36160);
        if (glCheckFramebufferStatus == 36053) {
            return;
        }
        TLog.e(String.format("%s %s framebuffer error:[0x%s], fbo: %d, texture: %d, rbo: %d, context: %s, %s", "SelesFramebuffer", s, Integer.toHexString(glCheckFramebufferStatus), this.a, this.c, this.b, this.j, this), new Object[0]);
    }
    
    public void activateFramebuffer() {
        GLES20.glBindFramebuffer(36160, this.a);
        GLES20.glViewport(0, 0, this.g.width, this.g.height);
        this.a("activateFramebuffer");
    }
    
    public void destroy() {
        if (this.k) {
            return;
        }
        SelesContext.recycleFramebuffer(this);
        TLog.dump("%s destroy()   fId:%s texId:%s  %s | %s", "SelesFramebuffer", this.a, this.c, this, SelesContext.currentEGLContext());
    }
    
    @Override
    protected void finalize() {
        TLog.dump("%s finalize()  fId:%s texId:%s  %s | %s", "SelesFramebuffer", this.a, this.c, this, SelesContext.currentEGLContext());
        this.destroy();
        try {
            super.finalize();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }
    
    public synchronized void lock() {
        if (this.f) {
            return;
        }
        ++this.e;
    }
    
    public synchronized void unlock() {
        if (this.f) {
            return;
        }
        if (this.e <= 0) {
            TLog.w("Tried to overrelease a framebuffer, did you forget to call useNextFrameForImageCapture before using imageFromCurrentFramebuffer?", new Object[0]);
            return;
        }
        --this.e;
        if (this.e < 1) {
            SelesContext.returnFramebufferToCache(this);
        }
    }
    
    public void clearAllLocks() {
        this.e = 0;
    }
    
    public void disableReferenceCounting() {
        this.f = true;
    }
    
    public void enableReferenceCounting() {
        this.f = false;
    }
    
    public IntBuffer captureImageBufferFromFramebufferContents() {
        if (this.i) {
            TLog.w("%s captureImageBufferFromFramebufferContents Missing Framebuffer", "SelesFramebuffer");
            return null;
        }
        final EGLContext currentEGLContext = SelesContext.currentEGLContext();
        if (currentEGLContext == null || currentEGLContext == EGL10.EGL_NO_CONTEXT) {
            TLog.w("%s captureImageBufferFromFramebufferContents need EGLContext", "SelesFramebuffer");
            return null;
        }
        final GL10 gl10 = (GL10)currentEGLContext.getGL();
        this.activateFramebuffer();
        this.l = IntBuffer.allocate(this.g.width * this.g.height);
        gl10.glReadPixels(0, 0, this.g.width, this.g.height, 6408, 5121, (Buffer)this.l);
        SelesEGL10Core.checkGLError(String.format("captureImageBufferFromFramebufferContents", new Object[0]));
        return this.l;
    }
    
    public IntBuffer imageBufferFromFramebufferContents() {
        final IntBuffer l = this.l;
        this.l = null;
        return l;
    }
    
    public Bitmap imageFromFramebufferContents() {
        final IntBuffer imageBufferFromFramebufferContents = this.imageBufferFromFramebufferContents();
        if (imageBufferFromFramebufferContents == null) {
            TLog.w("%s imageFromFramebufferContents can not get image buffer", "SelesFramebuffer");
            return null;
        }
        final Bitmap bitmap = Bitmap.createBitmap(this.g.width, this.g.height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer((Buffer)imageBufferFromFramebufferContents);
        return bitmap;
    }
    
    public static class SelesTextureOptions
    {
        public int minFilter;
        public int magFilter;
        public int wrapS;
        public int wrapT;
        public int internalFormat;
        public int format;
        public int type;
        
        public SelesTextureOptions() {
            this.minFilter = 9729;
            this.magFilter = 9729;
            this.wrapS = 33071;
            this.wrapT = 33071;
            this.internalFormat = 6408;
            this.format = 6408;
            this.type = 5121;
        }
    }
    
    public enum SelesFramebufferMode
    {
        HOLDER(0), 
        PACKAGE(16), 
        TEXTURE(4096), 
        TEXTURE_ACTIVE(4112), 
        TEXTURE_OES(4128), 
        FBO_AND_TEXTURE(8192), 
        FBO_AND_TEXTURE_AND_RENDER(12288);
        
        private int a;
        
        public int getTypeId() {
            return this.a;
        }
        
        private SelesFramebufferMode(final int a) {
            this.a = 0;
            this.a = a;
        }
    }
}
