package org.lasque.tusdk.core.seles;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.opengles.GL10;
import org.lasque.tusdk.core.seles.egl.SelesEGL10Core;
import org.lasque.tusdk.core.seles.egl.SelesEGLContext;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.BitmapHelper;

public class SelesFramebuffer
{
  private int a;
  private int b;
  private int c;
  private SelesFramebufferMode d = SelesFramebufferMode.HOLDER;
  private int e;
  private boolean f;
  private TuSdkSize g;
  private SelesTextureOptions h;
  private boolean i;
  private SelesEGLContext j;
  private boolean k = false;
  private IntBuffer l;
  
  public int getTexture()
  {
    return this.c;
  }
  
  public int getFramebuffer()
  {
    return this.a;
  }
  
  public int getRenderbuffer()
  {
    return this.b;
  }
  
  public SelesFramebufferMode getModel()
  {
    return this.d;
  }
  
  public TuSdkSize getSize()
  {
    return this.g;
  }
  
  public SelesTextureOptions getTextureOptions()
  {
    return this.h;
  }
  
  public boolean isMissingFramebuffer()
  {
    return this.i;
  }
  
  public SelesEGLContext getEglContext()
  {
    return this.j;
  }
  
  public void flagDestory()
  {
    this.k = true;
  }
  
  public boolean isDestory()
  {
    return this.k;
  }
  
  public SelesFramebuffer(SelesFramebufferMode paramSelesFramebufferMode, TuSdkSize paramTuSdkSize, int paramInt, SelesTextureOptions paramSelesTextureOptions)
  {
    if (paramSelesFramebufferMode == null) {
      paramSelesFramebufferMode = SelesFramebufferMode.HOLDER;
    }
    this.d = paramSelesFramebufferMode;
    this.h = (paramSelesTextureOptions != null ? paramSelesTextureOptions : new SelesTextureOptions());
    this.g = paramTuSdkSize;
    this.e = 0;
    this.j = new SelesEGLContext();
    this.c = paramInt;
    this.k = (this.d == SelesFramebufferMode.HOLDER);
    this.i = (this.d.getTypeId() <= SelesFramebufferMode.TEXTURE_OES.getTypeId());
    this.f = this.i;
    if (this.d.getTypeId() <= SelesFramebufferMode.PACKAGE.getTypeId()) {
      return;
    }
    if (this.d.getTypeId() <= SelesFramebufferMode.TEXTURE_ACTIVE.getTypeId()) {
      a(this.d == SelesFramebufferMode.TEXTURE_ACTIVE ? 33985 : 0);
    } else if (this.d == SelesFramebufferMode.TEXTURE_OES) {
      b(0);
    } else if (this.d == SelesFramebufferMode.FBO_AND_TEXTURE) {
      a();
    } else if (this.d == SelesFramebufferMode.FBO_AND_TEXTURE_AND_RENDER) {
      b();
    }
    TLog.dump("%s create()  fId:%s texId:%s  %s|%s", new Object[] { "SelesFramebuffer", Integer.valueOf(this.a), Integer.valueOf(this.c), this, SelesContext.currentEGLContext() });
  }
  
  public void bindTexture(Bitmap paramBitmap)
  {
    bindTexture(paramBitmap, false);
  }
  
  public void bindTexture(Bitmap paramBitmap, boolean paramBoolean)
  {
    bindTexture(paramBitmap, false, paramBoolean);
  }
  
  public void bindTexture(Bitmap paramBitmap, boolean paramBoolean1, boolean paramBoolean2)
  {
    if ((paramBitmap == null) || (paramBitmap.isRecycled()))
    {
      TLog.w("%s bindTexture image is Null or Recycled, %s", new Object[] { "SelesFramebuffer", paramBitmap });
      return;
    }
    GLES20.glBindTexture(3553, this.c);
    if (paramBoolean1) {
      GLES20.glTexParameteri(3553, 10241, 9987);
    }
    GLUtils.texImage2D(3553, 0, paramBitmap, 0);
    if (paramBoolean1) {
      GLES20.glGenerateMipmap(3553);
    }
    GLES20.glBindTexture(3553, 0);
    if (paramBoolean2) {
      BitmapHelper.recycled(paramBitmap);
    }
  }
  
  public void bindTextureLuminance(ByteBuffer paramByteBuffer, int paramInt1, int paramInt2, boolean paramBoolean)
  {
    if (paramByteBuffer == null)
    {
      TLog.w("%s bindTextureLuminance imageData is Null", new Object[] { "SelesFramebuffer" });
      return;
    }
    GLES20.glBindTexture(3553, this.c);
    if (paramBoolean) {
      GLES20.glTexParameteri(3553, 10241, 9987);
    }
    GLES20.glTexImage2D(3553, 0, 6409, paramInt1, paramInt2, 0, 6409, 5121, paramByteBuffer);
    if (paramBoolean) {
      GLES20.glGenerateMipmap(3553);
    }
    GLES20.glBindTexture(3553, 0);
  }
  
  public void bindTextureRgbaHolder(boolean paramBoolean)
  {
    GLES20.glBindTexture(3553, this.c);
    if (paramBoolean) {
      GLES20.glTexParameteri(3553, 10241, 9987);
    }
    GLES20.glTexImage2D(3553, 0, 6408, this.g.width, this.g.height, 0, 6408, 5121, null);
    if (paramBoolean) {
      GLES20.glGenerateMipmap(3553);
    }
    GLES20.glBindTexture(3553, 0);
  }
  
  public void freshTextureRgba(Buffer paramBuffer)
  {
    if (paramBuffer == null) {
      return;
    }
    GLES20.glBindTexture(3553, this.c);
    GLES20.glTexSubImage2D(3553, 0, 0, 0, this.g.width, this.g.height, 6408, 5121, paramBuffer);
    GLES20.glBindTexture(3553, 0);
  }
  
  private void a(int paramInt)
  {
    int[] arrayOfInt = new int[1];
    if (paramInt != 0) {
      GLES20.glActiveTexture(paramInt);
    }
    GLES20.glGenTextures(1, arrayOfInt, 0);
    GLES20.glBindTexture(3553, arrayOfInt[0]);
    GLES20.glTexParameteri(3553, 10241, this.h.minFilter);
    GLES20.glTexParameteri(3553, 10240, this.h.magFilter);
    GLES20.glTexParameteri(3553, 10242, this.h.wrapS);
    GLES20.glTexParameteri(3553, 10243, this.h.wrapT);
    this.c = arrayOfInt[0];
  }
  
  @TargetApi(15)
  private void b(int paramInt)
  {
    int[] arrayOfInt = new int[1];
    if (paramInt != 0) {
      GLES20.glActiveTexture(paramInt);
    }
    GLES20.glGenTextures(1, arrayOfInt, 0);
    GLES20.glBindTexture(36197, arrayOfInt[0]);
    GLES20.glTexParameteri(36197, 10241, 9728);
    GLES20.glTexParameteri(36197, 10240, this.h.magFilter);
    GLES20.glTexParameteri(36197, 10242, this.h.wrapS);
    GLES20.glTexParameteri(36197, 10243, this.h.wrapT);
    this.c = arrayOfInt[0];
  }
  
  private void a()
  {
    int[] arrayOfInt = new int[1];
    GLES20.glGenFramebuffers(1, arrayOfInt, 0);
    GLES20.glBindFramebuffer(36160, arrayOfInt[0]);
    a(33985);
    GLES20.glBindTexture(3553, this.c);
    GLES20.glTexImage2D(3553, 0, this.h.internalFormat, this.g.width, this.g.height, 0, this.h.format, this.h.type, null);
    GLES20.glFramebufferTexture2D(36160, 36064, 3553, this.c, 0);
    this.a = arrayOfInt[0];
    a("generateFramebuffer");
    GLES20.glBindTexture(3553, 0);
  }
  
  private void b()
  {
    a();
    int[] arrayOfInt = new int[1];
    GLES20.glGenRenderbuffers(1, arrayOfInt, 0);
    this.b = arrayOfInt[0];
    GLES20.glBindRenderbuffer(36161, this.b);
    GLES20.glRenderbufferStorage(36161, 33189, this.g.width, this.g.height);
    GLES20.glBindFramebuffer(36160, this.a);
    GLES20.glFramebufferRenderbuffer(36160, 36096, 36161, this.b);
    GLES20.glBindRenderbuffer(36161, 0);
    a("generateRenderbuffer");
  }
  
  private void a(String paramString)
  {
    int m = GLES20.glCheckFramebufferStatus(36160);
    if (m == 36053) {
      return;
    }
    String str = String.format("%s %s framebuffer error:[0x%s], fbo: %d, texture: %d, rbo: %d, context: %s, %s", new Object[] { "SelesFramebuffer", paramString, Integer.toHexString(m), Integer.valueOf(this.a), Integer.valueOf(this.c), Integer.valueOf(this.b), this.j, this });
    TLog.e(str, new Object[0]);
  }
  
  public void activateFramebuffer()
  {
    GLES20.glBindFramebuffer(36160, this.a);
    GLES20.glViewport(0, 0, this.g.width, this.g.height);
    a("activateFramebuffer");
  }
  
  public void destroy()
  {
    if (this.k) {
      return;
    }
    SelesContext.recycleFramebuffer(this);
    TLog.dump("%s destroy()   fId:%s texId:%s  %s | %s", new Object[] { "SelesFramebuffer", Integer.valueOf(this.a), Integer.valueOf(this.c), this, SelesContext.currentEGLContext() });
  }
  
  protected void finalize()
  {
    TLog.dump("%s finalize()  fId:%s texId:%s  %s | %s", new Object[] { "SelesFramebuffer", Integer.valueOf(this.a), Integer.valueOf(this.c), this, SelesContext.currentEGLContext() });
    destroy();
    super.finalize();
  }
  
  public synchronized void lock()
  {
    if (this.f) {
      return;
    }
    this.e += 1;
  }
  
  public synchronized void unlock()
  {
    if (this.f) {
      return;
    }
    if (this.e <= 0)
    {
      TLog.w("Tried to overrelease a framebuffer, did you forget to call useNextFrameForImageCapture before using imageFromCurrentFramebuffer?", new Object[0]);
      return;
    }
    this.e -= 1;
    if (this.e < 1) {
      SelesContext.returnFramebufferToCache(this);
    }
  }
  
  public void clearAllLocks()
  {
    this.e = 0;
  }
  
  public void disableReferenceCounting()
  {
    this.f = true;
  }
  
  public void enableReferenceCounting()
  {
    this.f = false;
  }
  
  public IntBuffer captureImageBufferFromFramebufferContents()
  {
    if (this.i)
    {
      TLog.w("%s captureImageBufferFromFramebufferContents Missing Framebuffer", new Object[] { "SelesFramebuffer" });
      return null;
    }
    EGLContext localEGLContext = SelesContext.currentEGLContext();
    if ((localEGLContext == null) || (localEGLContext == EGL10.EGL_NO_CONTEXT))
    {
      TLog.w("%s captureImageBufferFromFramebufferContents need EGLContext", new Object[] { "SelesFramebuffer" });
      return null;
    }
    GL10 localGL10 = (GL10)localEGLContext.getGL();
    activateFramebuffer();
    this.l = IntBuffer.allocate(this.g.width * this.g.height);
    localGL10.glReadPixels(0, 0, this.g.width, this.g.height, 6408, 5121, this.l);
    SelesEGL10Core.checkGLError(String.format("captureImageBufferFromFramebufferContents", new Object[0]));
    return this.l;
  }
  
  public IntBuffer imageBufferFromFramebufferContents()
  {
    IntBuffer localIntBuffer = this.l;
    this.l = null;
    return localIntBuffer;
  }
  
  public Bitmap imageFromFramebufferContents()
  {
    IntBuffer localIntBuffer = imageBufferFromFramebufferContents();
    if (localIntBuffer == null)
    {
      TLog.w("%s imageFromFramebufferContents can not get image buffer", new Object[] { "SelesFramebuffer" });
      return null;
    }
    Bitmap localBitmap = Bitmap.createBitmap(this.g.width, this.g.height, Bitmap.Config.ARGB_8888);
    localBitmap.copyPixelsFromBuffer(localIntBuffer);
    return localBitmap;
  }
  
  public static class SelesTextureOptions
  {
    public int minFilter = 9729;
    public int magFilter = 9729;
    public int wrapS = 33071;
    public int wrapT = 33071;
    public int internalFormat = 6408;
    public int format = 6408;
    public int type = 5121;
  }
  
  public static enum SelesFramebufferMode
  {
    private int a = 0;
    
    public int getTypeId()
    {
      return this.a;
    }
    
    private SelesFramebufferMode(int paramInt)
    {
      this.a = paramInt;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\SelesFramebuffer.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */