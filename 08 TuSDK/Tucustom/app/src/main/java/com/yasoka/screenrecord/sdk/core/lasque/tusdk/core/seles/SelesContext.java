// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles;

//import org.lasque.tusdk.core.utils.image.ImageOrientation;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGL10;
import java.nio.FloatBuffer;
//import org.lasque.tusdk.core.seles.egl.SelesEGLContext;
//import org.lasque.tusdk.core.utils.StringHelper;
import android.content.pm.ConfigurationInfo;
import android.app.ActivityManager;
//import org.lasque.tusdk.core.utils.hardware.TuSdkGPU;
import android.opengl.GLES20;
//import org.lasque.tusdk.core.seles.egl.SelesEGL10Core;
//import org.lasque.tusdk.core.struct.TuSdkSize;
//import org.lasque.tusdk.core.utils.TLog;
import android.content.Context;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.egl.SelesEGL10Core;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.egl.SelesEGLContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkGPU;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;

import java.util.HashMap;

public class SelesContext
{
    private static SelesContext a;
    private boolean b;
    private int c;
    private int d;
    private int e;
    private int f;
    private int g;
    private int h;
    private int i;
    private int j;
    private float[] k;
    private String l;
    private String m;
    private String n;
    private final HashMap<String, SelesEGLContextCache> o;
    private boolean p;
    private boolean q;
    private boolean r;
    
    public static SelesContext shared() {
        return SelesContext.a;
    }
    
    public static synchronized SelesContext init(final Context context) {
        if (context != null && SelesContext.a == null) {
            (SelesContext.a = new SelesContext()).a(context);
        }
        return SelesContext.a;
    }
    
    public static boolean isSupportGL2() {
        return shared() != null && shared().b;
    }
    
    public static int getMaxTextureSize() {
        if (shared() == null) {
            return 0;
        }
        return shared().c;
    }
    
    public static int getMaxTextureOptimizedSize() {
        if (shared() == null) {
            return 0;
        }
        return shared().d;
    }
    
    public static int getMaxTextureImageUnits() {
        if (shared() == null) {
            return 0;
        }
        return shared().e;
    }
    
    public static int getMaxVertexAttribs() {
        if (shared() == null) {
            return 0;
        }
        return shared().f;
    }
    
    public static int getMaxVertexUniformVertors() {
        if (shared() == null) {
            return 0;
        }
        return shared().g;
    }
    
    public static int getMaxFragmentUniformVertors() {
        if (shared() == null) {
            return 0;
        }
        return shared().h;
    }
    
    public static int getMaxVertexTextureImageUnits() {
        if (shared() == null) {
            return 0;
        }
        return shared().i;
    }
    
    public static int getMaxVaryingVectors() {
        if (shared() == null) {
            return 0;
        }
        return shared().j;
    }
    
    public float[] getVertexPointSize() {
        if (shared() == null) {
            return new float[4];
        }
        return shared().k;
    }
    
    public static String getGpuInfo() {
        if (shared() == null) {
            return null;
        }
        return shared().l;
    }
    
    public static String getCpuType() {
        if (shared() == null) {
            return null;
        }
        return shared().m;
    }
    
    public static boolean isSupportRedTextures() {
        return shared() != null && shared().p;
    }
    
    public static boolean isSupportFrameBufferReads() {
        return shared() != null && shared().q;
    }
    
    public static boolean isSupportOESImageExternal() {
        return shared() != null && shared().r;
    }
    
    private SelesContext() {
        this.o = new HashMap<String, SelesEGLContextCache>();
    }
    
    private void a(final Context context) {
        if (!(this.b = this.b(context))) {
            TLog.e("OpenGL ES 2.0 is not supported on this device.", new Object[0]);
            return;
        }
        final SelesEGL10Core create = SelesEGL10Core.create(TuSdkSize.create(1, 1));
        this.c = this.a(3379);
        this.e = this.a(34930);
        this.f = this.a(34921);
        this.g = this.a(36347);
        this.h = this.a(36349);
        this.i = this.a(35660);
        this.j = this.a(36348);
        this.l = GLES20.glGetString(7937);
        this.m = GLES20.glGetString(7936);
        this.n = GLES20.glGetString(7939);
        GLES20.glGetFloatv(33901, this.k = new float[4], 0);
        create.destroy();
        this.p = this.supportsOpenGLESExtension("GL_EXT_texture_rg");
        this.q = this.supportsOpenGLESExtension("GL_EXT_shader_framebuffer_fetch");
        this.r = this.supportsOpenGLESExtension("GL_OES_EGL_image_external");
        TuSdkGPU.init(this.c, this.l);
        this.d = TuSdkGPU.getMaxTextureOptimizedSize();
    }
    
    private boolean b(final Context context) {
        final ConfigurationInfo deviceConfigurationInfo = ((ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE)).getDeviceConfigurationInfo();
        return deviceConfigurationInfo != null && deviceConfigurationInfo.reqGlEsVersion >= 131072;
    }
    
    private int a(final int n) {
        final int[] array = { 0 };
        GLES20.glGetIntegerv(n, array, 0);
        return array[0];
    }
    
    public boolean supportsOpenGLESExtension(final String s) {
        return !StringHelper.isBlank(this.n) && !StringHelper.isBlank(s) && this.n.contains(s);
    }
    
    public static TuSdkSize sizeThatFitsWithinATexture(final TuSdkSize tuSdkSize) {
        if (tuSdkSize == null) {
            return null;
        }
        return tuSdkSize.limitSize();
    }
    
    public static SelesGLProgram program(final String s, final String s2) {
        if (shared() == null || s == null || s2 == null) {
            return null;
        }
        final SelesEGLContextCache a = a(SelesEGLContext.currentHashKey());
        if (a == null) {
            TLog.e("Can not find GLProgram: %s", SelesEGLContext.currentEGLContext());
            return null;
        }
        return a.getProgram(s, s2);
    }
    
    public static void setActiveShaderProgram(final SelesGLProgram selesGLProgram) {
        if (shared() == null || selesGLProgram == null) {
            return;
        }
        selesGLProgram.use();
    }
    
    public static SelesFramebufferCache sharedFramebufferCache() {
        if (shared() == null) {
            return null;
        }
        final SelesEGLContextCache a = a(SelesEGLContext.currentHashKey());
        if (a == null) {
            return null;
        }
        return a.sharedFramebufferCache();
    }
    
    public static void returnFramebufferToCache(final SelesFramebuffer selesFramebuffer) {
        if (shared() == null || selesFramebuffer == null || selesFramebuffer.isDestory()) {
            return;
        }
        final SelesEGLContextCache a = a(selesFramebuffer.getEglContext().getHashKey());
        if (a == null) {
            return;
        }
        a.returnFramebufferToCache(selesFramebuffer);
    }
    
    public static void recycleFramebuffer(final SelesFramebuffer selesFramebuffer) {
        if (shared() == null || selesFramebuffer == null || selesFramebuffer.isDestory()) {
            return;
        }
        final SelesEGLContextCache selesEGLContextCache = shared().o.get(selesFramebuffer.getEglContext().getHashKey());
        if (selesEGLContextCache == null) {
            return;
        }
        selesEGLContextCache.recycleFramebuffer(selesFramebuffer);
    }
    
    public static SelesVertexbuffer fetchVertexbuffer(final FloatBuffer floatBuffer) {
        if (shared() == null || floatBuffer == null) {
            return null;
        }
        final SelesEGLContextCache a = a(SelesEGLContext.currentHashKey());
        if (a == null) {
            return null;
        }
        return a.sharedEGLBufferCache().fetchVertexbuffer(floatBuffer);
    }
    
    public static void recycleVertexbuffer(final SelesVertexbuffer selesVertexbuffer) {
        if (shared() == null || selesVertexbuffer == null) {
            return;
        }
        final SelesEGLContextCache selesEGLContextCache = shared().o.get(selesVertexbuffer.getEglContext().getHashKey());
        if (selesEGLContextCache == null) {
            return;
        }
        selesEGLContextCache.sharedEGLBufferCache().recycleVertexbuffer(selesVertexbuffer);
    }
    
    public static SelesPixelBuffer fetchPixelBuffer(final TuSdkSize tuSdkSize, final int n) {
        if (shared() == null || tuSdkSize == null || !tuSdkSize.isSize() || n < 1) {
            return null;
        }
        final SelesEGLContextCache a = a(SelesEGLContext.currentHashKey());
        if (a == null) {
            return null;
        }
        return a.sharedEGLBufferCache().fetchPixelBuffer(tuSdkSize, n);
    }
    
    public static void recyclePixelbuffer(final SelesPixelBuffer selesPixelBuffer) {
        if (shared() == null || selesPixelBuffer == null) {
            return;
        }
        final SelesEGLContextCache selesEGLContextCache = shared().o.get(selesPixelBuffer.getEglContext().getHashKey());
        if (selesEGLContextCache == null) {
            return;
        }
        selesEGLContextCache.sharedEGLBufferCache().recyclePixelbuffer(selesPixelBuffer);
    }
    
    private static SelesEGLContextCache a(final String s) {
        if (shared() == null || s == null || SelesEGLContext.equalsCurrent(EGL10.EGL_NO_CONTEXT)) {
            return null;
        }
        SelesEGLContextCache selesEGLContextCache = shared().o.get(s);
        if (selesEGLContextCache == null) {
            createEGLContext(SelesEGLContext.currentEGLContext());
            selesEGLContextCache = shared().o.get(s);
        }
        return selesEGLContextCache;
    }
    
    public static EGLContext currentEGLContext() {
        return SelesEGLContext.currentEGLContext();
    }
    
    public static GL10 currentGL() {
        return SelesEGLContext.currentGL();
    }
    
    public static synchronized void createEGLContext(final EGLContext eglContext) {
        if (shared() == null || eglContext == null || eglContext.equals(EGL10.EGL_NO_CONTEXT)) {
            return;
        }
        final String currentHashKey = SelesEGLContext.currentHashKey();
        if (!SelesEGLContext.equalsCurrent(eglContext) || shared().o.containsKey(currentHashKey)) {
            return;
        }
        shared().o.put(currentHashKey, new SelesEGLContextCache());
    }
    
    public static synchronized void destroyContext(final EGLContext eglContext) {
        if (shared() == null || eglContext == null || eglContext.equals(EGL10.EGL_NO_CONTEXT)) {
            return;
        }
        final SelesEGLContextCache selesEGLContextCache = shared().o.remove(SelesEGLContext.currentHashKey(eglContext));
        if (selesEGLContextCache == null) {
            return;
        }
        selesEGLContextCache.destory();
    }
    
    public static boolean checkGlError(final String s) {
        final int glGetError = GLES20.glGetError();
        if (glGetError != 0) {
            TLog.e("%s glError: 0x%s", s, Integer.toHexString(glGetError));
            return true;
        }
        return false;
    }
    
    public void dumpGPU() {
        TLog.d("-------- GPU info --------", new Object[0]);
        TLog.d("mSupportGL2: %s", this.b);
        TLog.d("mSupportRedTextures: %s", this.p);
        TLog.d("mSupportFrameBufferReads: %s", this.q);
        TLog.d("mSupportOESImageExternal: %s", this.r);
        TLog.d("mMaxTextureSize: %s", this.c);
        TLog.d("mMaxTextureOptimizedSize: %s", this.d);
        TLog.d("mMaxTextureImageUnits: %s", this.e);
        TLog.d("mMaxVertexAttribs: %s", this.f);
        TLog.d("mMaxVertexUniformVertors: %s", this.g);
        TLog.d("mMaxFragmentUniformVertors: %s", this.h);
        TLog.d("mMaxVertexTextureImageUnits: %s", this.i);
        TLog.d("mMaxVaryingVectors: %s", this.j);
        TLog.d("mVertexPointSizeRange: [%f, %f]", this.k[0], this.k[1]);
        TLog.d("mGpuInfo: %s", this.l);
        TLog.d("mCpuType: %s", this.m);
        TLog.d("mExtensionNames: %s", this.n);
    }
    
    public interface ResponseListener<T>
    {
        void response(final T p0);
    }
    
    public interface SelesInput
    {
        void mountAtGLThread(final Runnable p0);
        
        void newFrameReady(final long p0, final int p1);
        
        void setInputFramebuffer(final SelesFramebuffer p0, final int p1);
        
        int nextAvailableTextureIndex();
        
        void setInputSize(final TuSdkSize p0, final int p1);
        
        void setInputRotation(final ImageOrientation p0, final int p1);
        
        TuSdkSize maximumOutputSize();
        
        void endProcessing();
        
        boolean isShouldIgnoreUpdatesToThisTarget();
        
        boolean isEnabled();
        
        boolean wantsMonochromeInput();
        
        void setCurrentlyReceivingMonochromeInput(final boolean p0);
    }
}
