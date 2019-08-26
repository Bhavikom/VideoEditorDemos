package org.lasque.tusdk.core.seles;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLES20;
import java.nio.FloatBuffer;
import java.util.HashMap;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL10;
import org.lasque.tusdk.core.seles.egl.SelesEGL10Core;
import org.lasque.tusdk.core.seles.egl.SelesEGLContext;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.hardware.TuSdkGPU;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

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
  private final HashMap<String, SelesEGLContextCache> o = new HashMap();
  private boolean p;
  private boolean q;
  private boolean r;
  
  public static SelesContext shared()
  {
    return a;
  }
  
  public static synchronized SelesContext init(Context paramContext)
  {
    if ((paramContext != null) && (a == null))
    {
      a = new SelesContext();
      a.a(paramContext);
    }
    return a;
  }
  
  public static boolean isSupportGL2()
  {
    if (shared() == null) {
      return false;
    }
    return shared().b;
  }
  
  public static int getMaxTextureSize()
  {
    if (shared() == null) {
      return 0;
    }
    return shared().c;
  }
  
  public static int getMaxTextureOptimizedSize()
  {
    if (shared() == null) {
      return 0;
    }
    return shared().d;
  }
  
  public static int getMaxTextureImageUnits()
  {
    if (shared() == null) {
      return 0;
    }
    return shared().e;
  }
  
  public static int getMaxVertexAttribs()
  {
    if (shared() == null) {
      return 0;
    }
    return shared().f;
  }
  
  public static int getMaxVertexUniformVertors()
  {
    if (shared() == null) {
      return 0;
    }
    return shared().g;
  }
  
  public static int getMaxFragmentUniformVertors()
  {
    if (shared() == null) {
      return 0;
    }
    return shared().h;
  }
  
  public static int getMaxVertexTextureImageUnits()
  {
    if (shared() == null) {
      return 0;
    }
    return shared().i;
  }
  
  public static int getMaxVaryingVectors()
  {
    if (shared() == null) {
      return 0;
    }
    return shared().j;
  }
  
  public float[] getVertexPointSize()
  {
    if (shared() == null) {
      return new float[4];
    }
    return shared().k;
  }
  
  public static String getGpuInfo()
  {
    if (shared() == null) {
      return null;
    }
    return shared().l;
  }
  
  public static String getCpuType()
  {
    if (shared() == null) {
      return null;
    }
    return shared().m;
  }
  
  public static boolean isSupportRedTextures()
  {
    if (shared() == null) {
      return false;
    }
    return shared().p;
  }
  
  public static boolean isSupportFrameBufferReads()
  {
    if (shared() == null) {
      return false;
    }
    return shared().q;
  }
  
  public static boolean isSupportOESImageExternal()
  {
    if (shared() == null) {
      return false;
    }
    return shared().r;
  }
  
  private void a(Context paramContext)
  {
    this.b = b(paramContext);
    if (!this.b)
    {
      TLog.e("OpenGL ES 2.0 is not supported on this device.", new Object[0]);
      return;
    }
    SelesEGL10Core localSelesEGL10Core = SelesEGL10Core.create(TuSdkSize.create(1, 1));
    this.c = a(3379);
    this.e = a(34930);
    this.f = a(34921);
    this.g = a(36347);
    this.h = a(36349);
    this.i = a(35660);
    this.j = a(36348);
    this.l = GLES20.glGetString(7937);
    this.m = GLES20.glGetString(7936);
    this.n = GLES20.glGetString(7939);
    this.k = new float[4];
    GLES20.glGetFloatv(33901, this.k, 0);
    localSelesEGL10Core.destroy();
    this.p = supportsOpenGLESExtension("GL_EXT_texture_rg");
    this.q = supportsOpenGLESExtension("GL_EXT_shader_framebuffer_fetch");
    this.r = supportsOpenGLESExtension("GL_OES_EGL_image_external");
    TuSdkGPU.init(this.c, this.l);
    this.d = TuSdkGPU.getMaxTextureOptimizedSize();
  }
  
  private boolean b(Context paramContext)
  {
    ActivityManager localActivityManager = (ActivityManager)paramContext.getSystemService("activity");
    ConfigurationInfo localConfigurationInfo = localActivityManager.getDeviceConfigurationInfo();
    if (localConfigurationInfo == null) {
      return false;
    }
    return localConfigurationInfo.reqGlEsVersion >= 131072;
  }
  
  private int a(int paramInt)
  {
    int[] arrayOfInt = new int[1];
    GLES20.glGetIntegerv(paramInt, arrayOfInt, 0);
    return arrayOfInt[0];
  }
  
  public boolean supportsOpenGLESExtension(String paramString)
  {
    if ((StringHelper.isBlank(this.n)) || (StringHelper.isBlank(paramString))) {
      return false;
    }
    return this.n.contains(paramString);
  }
  
  public static TuSdkSize sizeThatFitsWithinATexture(TuSdkSize paramTuSdkSize)
  {
    if (paramTuSdkSize == null) {
      return null;
    }
    return paramTuSdkSize.limitSize();
  }
  
  public static SelesGLProgram program(String paramString1, String paramString2)
  {
    if ((shared() == null) || (paramString1 == null) || (paramString2 == null)) {
      return null;
    }
    SelesEGLContextCache localSelesEGLContextCache = a(SelesEGLContext.currentHashKey());
    if (localSelesEGLContextCache == null)
    {
      TLog.e("Can not find GLProgram: %s", new Object[] { SelesEGLContext.currentEGLContext() });
      return null;
    }
    return localSelesEGLContextCache.getProgram(paramString1, paramString2);
  }
  
  public static void setActiveShaderProgram(SelesGLProgram paramSelesGLProgram)
  {
    if ((shared() == null) || (paramSelesGLProgram == null)) {
      return;
    }
    paramSelesGLProgram.use();
  }
  
  public static SelesFramebufferCache sharedFramebufferCache()
  {
    if (shared() == null) {
      return null;
    }
    SelesEGLContextCache localSelesEGLContextCache = a(SelesEGLContext.currentHashKey());
    if (localSelesEGLContextCache == null) {
      return null;
    }
    return localSelesEGLContextCache.sharedFramebufferCache();
  }
  
  public static void returnFramebufferToCache(SelesFramebuffer paramSelesFramebuffer)
  {
    if ((shared() == null) || (paramSelesFramebuffer == null) || (paramSelesFramebuffer.isDestory())) {
      return;
    }
    SelesEGLContextCache localSelesEGLContextCache = a(paramSelesFramebuffer.getEglContext().getHashKey());
    if (localSelesEGLContextCache == null) {
      return;
    }
    localSelesEGLContextCache.returnFramebufferToCache(paramSelesFramebuffer);
  }
  
  public static void recycleFramebuffer(SelesFramebuffer paramSelesFramebuffer)
  {
    if ((shared() == null) || (paramSelesFramebuffer == null) || (paramSelesFramebuffer.isDestory())) {
      return;
    }
    SelesEGLContextCache localSelesEGLContextCache = (SelesEGLContextCache)shared().o.get(paramSelesFramebuffer.getEglContext().getHashKey());
    if (localSelesEGLContextCache == null) {
      return;
    }
    localSelesEGLContextCache.recycleFramebuffer(paramSelesFramebuffer);
  }
  
  public static SelesVertexbuffer fetchVertexbuffer(FloatBuffer paramFloatBuffer)
  {
    if ((shared() == null) || (paramFloatBuffer == null)) {
      return null;
    }
    SelesEGLContextCache localSelesEGLContextCache = a(SelesEGLContext.currentHashKey());
    if (localSelesEGLContextCache == null) {
      return null;
    }
    return localSelesEGLContextCache.sharedEGLBufferCache().fetchVertexbuffer(paramFloatBuffer);
  }
  
  public static void recycleVertexbuffer(SelesVertexbuffer paramSelesVertexbuffer)
  {
    if ((shared() == null) || (paramSelesVertexbuffer == null)) {
      return;
    }
    SelesEGLContextCache localSelesEGLContextCache = (SelesEGLContextCache)shared().o.get(paramSelesVertexbuffer.getEglContext().getHashKey());
    if (localSelesEGLContextCache == null) {
      return;
    }
    localSelesEGLContextCache.sharedEGLBufferCache().recycleVertexbuffer(paramSelesVertexbuffer);
  }
  
  public static SelesPixelBuffer fetchPixelBuffer(TuSdkSize paramTuSdkSize, int paramInt)
  {
    if ((shared() == null) || (paramTuSdkSize == null) || (!paramTuSdkSize.isSize()) || (paramInt < 1)) {
      return null;
    }
    SelesEGLContextCache localSelesEGLContextCache = a(SelesEGLContext.currentHashKey());
    if (localSelesEGLContextCache == null) {
      return null;
    }
    return localSelesEGLContextCache.sharedEGLBufferCache().fetchPixelBuffer(paramTuSdkSize, paramInt);
  }
  
  public static void recyclePixelbuffer(SelesPixelBuffer paramSelesPixelBuffer)
  {
    if ((shared() == null) || (paramSelesPixelBuffer == null)) {
      return;
    }
    SelesEGLContextCache localSelesEGLContextCache = (SelesEGLContextCache)shared().o.get(paramSelesPixelBuffer.getEglContext().getHashKey());
    if (localSelesEGLContextCache == null) {
      return;
    }
    localSelesEGLContextCache.sharedEGLBufferCache().recyclePixelbuffer(paramSelesPixelBuffer);
  }
  
  private static SelesEGLContextCache a(String paramString)
  {
    if ((shared() == null) || (paramString == null) || (SelesEGLContext.equalsCurrent(EGL10.EGL_NO_CONTEXT))) {
      return null;
    }
    SelesEGLContextCache localSelesEGLContextCache = (SelesEGLContextCache)shared().o.get(paramString);
    if (localSelesEGLContextCache == null)
    {
      createEGLContext(SelesEGLContext.currentEGLContext());
      localSelesEGLContextCache = (SelesEGLContextCache)shared().o.get(paramString);
    }
    return localSelesEGLContextCache;
  }
  
  public static EGLContext currentEGLContext()
  {
    return SelesEGLContext.currentEGLContext();
  }
  
  public static GL10 currentGL()
  {
    return SelesEGLContext.currentGL();
  }
  
  public static synchronized void createEGLContext(EGLContext paramEGLContext)
  {
    if ((shared() == null) || (paramEGLContext == null) || (paramEGLContext.equals(EGL10.EGL_NO_CONTEXT))) {
      return;
    }
    String str = SelesEGLContext.currentHashKey();
    if ((!SelesEGLContext.equalsCurrent(paramEGLContext)) || (shared().o.containsKey(str))) {
      return;
    }
    shared().o.put(str, new SelesEGLContextCache());
  }
  
  public static synchronized void destroyContext(EGLContext paramEGLContext)
  {
    if ((shared() == null) || (paramEGLContext == null) || (paramEGLContext.equals(EGL10.EGL_NO_CONTEXT))) {
      return;
    }
    String str = SelesEGLContext.currentHashKey(paramEGLContext);
    SelesEGLContextCache localSelesEGLContextCache = (SelesEGLContextCache)shared().o.remove(str);
    if (localSelesEGLContextCache == null) {
      return;
    }
    localSelesEGLContextCache.destory();
  }
  
  public static boolean checkGlError(String paramString)
  {
    int i1 = GLES20.glGetError();
    if (i1 != 0)
    {
      TLog.e("%s glError: 0x%s", new Object[] { paramString, Integer.toHexString(i1) });
      return true;
    }
    return false;
  }
  
  public void dumpGPU()
  {
    TLog.d("-------- GPU info --------", new Object[0]);
    TLog.d("mSupportGL2: %s", new Object[] { Boolean.valueOf(this.b) });
    TLog.d("mSupportRedTextures: %s", new Object[] { Boolean.valueOf(this.p) });
    TLog.d("mSupportFrameBufferReads: %s", new Object[] { Boolean.valueOf(this.q) });
    TLog.d("mSupportOESImageExternal: %s", new Object[] { Boolean.valueOf(this.r) });
    TLog.d("mMaxTextureSize: %s", new Object[] { Integer.valueOf(this.c) });
    TLog.d("mMaxTextureOptimizedSize: %s", new Object[] { Integer.valueOf(this.d) });
    TLog.d("mMaxTextureImageUnits: %s", new Object[] { Integer.valueOf(this.e) });
    TLog.d("mMaxVertexAttribs: %s", new Object[] { Integer.valueOf(this.f) });
    TLog.d("mMaxVertexUniformVertors: %s", new Object[] { Integer.valueOf(this.g) });
    TLog.d("mMaxFragmentUniformVertors: %s", new Object[] { Integer.valueOf(this.h) });
    TLog.d("mMaxVertexTextureImageUnits: %s", new Object[] { Integer.valueOf(this.i) });
    TLog.d("mMaxVaryingVectors: %s", new Object[] { Integer.valueOf(this.j) });
    TLog.d("mVertexPointSizeRange: [%f, %f]", new Object[] { Float.valueOf(this.k[0]), Float.valueOf(this.k[1]) });
    TLog.d("mGpuInfo: %s", new Object[] { this.l });
    TLog.d("mCpuType: %s", new Object[] { this.m });
    TLog.d("mExtensionNames: %s", new Object[] { this.n });
  }
  
  public static abstract interface ResponseListener<T>
  {
    public abstract void response(T paramT);
  }
  
  public static abstract interface SelesInput
  {
    public abstract void mountAtGLThread(Runnable paramRunnable);
    
    public abstract void newFrameReady(long paramLong, int paramInt);
    
    public abstract void setInputFramebuffer(SelesFramebuffer paramSelesFramebuffer, int paramInt);
    
    public abstract int nextAvailableTextureIndex();
    
    public abstract void setInputSize(TuSdkSize paramTuSdkSize, int paramInt);
    
    public abstract void setInputRotation(ImageOrientation paramImageOrientation, int paramInt);
    
    public abstract TuSdkSize maximumOutputSize();
    
    public abstract void endProcessing();
    
    public abstract boolean isShouldIgnoreUpdatesToThisTarget();
    
    public abstract boolean isEnabled();
    
    public abstract boolean wantsMonochromeInput();
    
    public abstract void setCurrentlyReceivingMonochromeInput(boolean paramBoolean);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\SelesContext.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */