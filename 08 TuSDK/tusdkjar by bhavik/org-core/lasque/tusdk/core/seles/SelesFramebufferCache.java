package org.lasque.tusdk.core.seles;

import android.opengl.GLES20;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.lasque.tusdk.core.seles.egl.SelesEGLContext;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.ArrayHelper;
import org.lasque.tusdk.core.utils.TLog;

public class SelesFramebufferCache
{
  private final Map<String, BlockingQueue<SelesFramebuffer>> a = Collections.synchronizedMap(new HashMap());
  private final List<Integer> b = new ArrayList();
  private final List<Integer> c = new ArrayList();
  private final List<Integer> d = new ArrayList();
  private final List<Integer> e = new ArrayList();
  private final List<Integer> f = new ArrayList();
  private final List<Integer> g = new ArrayList();
  private int h;
  
  public SelesFramebufferCache()
  {
    TLog.dump("%s create() %s|%s", new Object[] { "SelesFramebufferCache", this, SelesContext.currentEGLContext() });
  }
  
  private String a(TuSdkSize paramTuSdkSize, SelesFramebuffer.SelesTextureOptions paramSelesTextureOptions, SelesFramebuffer.SelesFramebufferMode paramSelesFramebufferMode)
  {
    if (paramTuSdkSize == null) {
      paramTuSdkSize = TuSdkSize.create(0);
    }
    String str = String.format("%sx%s-%s:%s:%s:%s:%s:%s:%s-%s", new Object[] { Integer.valueOf(paramTuSdkSize.width), Integer.valueOf(paramTuSdkSize.height), Integer.valueOf(paramSelesTextureOptions.minFilter), Integer.valueOf(paramSelesTextureOptions.magFilter), Integer.valueOf(paramSelesTextureOptions.wrapS), Integer.valueOf(paramSelesTextureOptions.wrapT), Integer.valueOf(paramSelesTextureOptions.internalFormat), Integer.valueOf(paramSelesTextureOptions.format), Integer.valueOf(paramSelesTextureOptions.type), paramSelesFramebufferMode });
    return str;
  }
  
  public SelesFramebuffer fetchFramebuffer(TuSdkSize paramTuSdkSize, boolean paramBoolean)
  {
    return fetchFramebuffer(paramTuSdkSize, new SelesFramebuffer.SelesTextureOptions(), paramBoolean);
  }
  
  public SelesFramebuffer fetchFramebuffer(TuSdkSize paramTuSdkSize, SelesFramebuffer.SelesTextureOptions paramSelesTextureOptions, boolean paramBoolean)
  {
    SelesFramebuffer.SelesFramebufferMode localSelesFramebufferMode = SelesFramebuffer.SelesFramebufferMode.FBO_AND_TEXTURE;
    if (paramBoolean) {
      localSelesFramebufferMode = SelesFramebuffer.SelesFramebufferMode.TEXTURE_ACTIVE;
    }
    return fetchFramebuffer(localSelesFramebufferMode, paramTuSdkSize, 0, paramSelesTextureOptions);
  }
  
  public SelesFramebuffer fetchTextureOES()
  {
    return fetchTexture(TuSdkSize.create(0), false, true);
  }
  
  public SelesFramebuffer fetchTexture(TuSdkSize paramTuSdkSize, boolean paramBoolean)
  {
    return fetchTexture(paramTuSdkSize, paramBoolean, false);
  }
  
  public SelesFramebuffer fetchTexture(TuSdkSize paramTuSdkSize, boolean paramBoolean1, boolean paramBoolean2)
  {
    return fetchTexture(paramTuSdkSize, new SelesFramebuffer.SelesTextureOptions(), paramBoolean1, paramBoolean2);
  }
  
  public SelesFramebuffer fetchTexture(TuSdkSize paramTuSdkSize, SelesFramebuffer.SelesTextureOptions paramSelesTextureOptions, boolean paramBoolean1, boolean paramBoolean2)
  {
    SelesFramebuffer.SelesFramebufferMode localSelesFramebufferMode = SelesFramebuffer.SelesFramebufferMode.TEXTURE;
    if (paramBoolean2) {
      localSelesFramebufferMode = SelesFramebuffer.SelesFramebufferMode.TEXTURE_OES;
    } else if (paramBoolean1) {
      localSelesFramebufferMode = SelesFramebuffer.SelesFramebufferMode.TEXTURE_ACTIVE;
    }
    return fetchFramebuffer(localSelesFramebufferMode, paramTuSdkSize, 0, paramSelesTextureOptions);
  }
  
  public SelesFramebuffer fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode paramSelesFramebufferMode, TuSdkSize paramTuSdkSize)
  {
    return fetchFramebuffer(paramSelesFramebufferMode, paramTuSdkSize, new SelesFramebuffer.SelesTextureOptions());
  }
  
  public SelesFramebuffer fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode paramSelesFramebufferMode, TuSdkSize paramTuSdkSize, int paramInt)
  {
    return fetchFramebuffer(paramSelesFramebufferMode, paramTuSdkSize, paramInt, new SelesFramebuffer.SelesTextureOptions());
  }
  
  public SelesFramebuffer fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode paramSelesFramebufferMode, TuSdkSize paramTuSdkSize, SelesFramebuffer.SelesTextureOptions paramSelesTextureOptions)
  {
    return fetchFramebuffer(paramSelesFramebufferMode, paramTuSdkSize, 0, paramSelesTextureOptions);
  }
  
  public SelesFramebuffer fetchFramebuffer(SelesFramebuffer.SelesFramebufferMode paramSelesFramebufferMode, TuSdkSize paramTuSdkSize, int paramInt, SelesFramebuffer.SelesTextureOptions paramSelesTextureOptions)
  {
    if (paramSelesFramebufferMode == null) {
      paramSelesFramebufferMode = SelesFramebuffer.SelesFramebufferMode.HOLDER;
    }
    clearRecycle();
    String str = a(paramTuSdkSize, paramSelesTextureOptions, paramSelesFramebufferMode);
    SelesFramebuffer localSelesFramebuffer = a(str);
    if (localSelesFramebuffer == null)
    {
      localSelesFramebuffer = new SelesFramebuffer(paramSelesFramebufferMode, paramTuSdkSize, paramInt, paramSelesTextureOptions);
      if (paramSelesFramebufferMode != SelesFramebuffer.SelesFramebufferMode.HOLDER) {
        this.h += 1;
      }
    }
    localSelesFramebuffer.lock();
    a(localSelesFramebuffer);
    return localSelesFramebuffer;
  }
  
  private SelesFramebuffer a(String paramString)
  {
    SelesFramebuffer localSelesFramebuffer = null;
    BlockingQueue localBlockingQueue = b(paramString);
    while ((localSelesFramebuffer == null) && (!localBlockingQueue.isEmpty())) {
      try
      {
        localSelesFramebuffer = (SelesFramebuffer)localBlockingQueue.take();
      }
      catch (InterruptedException localInterruptedException)
      {
        TLog.e(localInterruptedException, "%s fetchFramebuffer: %s", new Object[] { getClass(), paramString });
      }
    }
    return localSelesFramebuffer;
  }
  
  private BlockingQueue<SelesFramebuffer> b(String paramString)
  {
    Object localObject = (BlockingQueue)this.a.get(paramString);
    if (localObject == null)
    {
      localObject = new LinkedBlockingQueue();
      this.a.put(paramString, localObject);
    }
    return (BlockingQueue<SelesFramebuffer>)localObject;
  }
  
  public void returnFramebufferToCache(SelesFramebuffer paramSelesFramebuffer)
  {
    if ((paramSelesFramebuffer == null) || (paramSelesFramebuffer.getModel() == SelesFramebuffer.SelesFramebufferMode.HOLDER)) {
      return;
    }
    paramSelesFramebuffer.clearAllLocks();
    b(paramSelesFramebuffer);
    TuSdkSize localTuSdkSize = paramSelesFramebuffer.getSize();
    SelesFramebuffer.SelesTextureOptions localSelesTextureOptions = paramSelesFramebuffer.getTextureOptions();
    String str = a(localTuSdkSize, localSelesTextureOptions, paramSelesFramebuffer.getModel());
    BlockingQueue localBlockingQueue = b(str);
    localBlockingQueue.add(paramSelesFramebuffer);
  }
  
  private void a(SelesFramebuffer paramSelesFramebuffer)
  {
    if (paramSelesFramebuffer.getModel() == SelesFramebuffer.SelesFramebufferMode.HOLDER) {
      return;
    }
    if ((paramSelesFramebuffer.getTexture() != 0) && (!this.e.contains(Integer.valueOf(paramSelesFramebuffer.getTexture())))) {
      this.e.add(Integer.valueOf(paramSelesFramebuffer.getTexture()));
    }
    if ((paramSelesFramebuffer.getFramebuffer() != 0) && (!this.f.contains(Integer.valueOf(paramSelesFramebuffer.getFramebuffer())))) {
      this.f.add(Integer.valueOf(paramSelesFramebuffer.getFramebuffer()));
    }
    if ((paramSelesFramebuffer.getRenderbuffer() != 0) && (!this.f.contains(Integer.valueOf(paramSelesFramebuffer.getRenderbuffer())))) {
      this.g.add(Integer.valueOf(paramSelesFramebuffer.getRenderbuffer()));
    }
  }
  
  private void b(SelesFramebuffer paramSelesFramebuffer)
  {
    this.e.remove(Integer.valueOf(paramSelesFramebuffer.getTexture()));
    this.f.remove(Integer.valueOf(paramSelesFramebuffer.getFramebuffer()));
    this.g.remove(Integer.valueOf(paramSelesFramebuffer.getRenderbuffer()));
  }
  
  public void recycleFramebuffer(SelesFramebuffer paramSelesFramebuffer)
  {
    if (paramSelesFramebuffer == null) {
      return;
    }
    b(paramSelesFramebuffer);
    c(paramSelesFramebuffer);
    if (paramSelesFramebuffer.getEglContext().equalsCurrentThread()) {
      clearRecycle();
    }
  }
  
  private void c(SelesFramebuffer paramSelesFramebuffer)
  {
    if (paramSelesFramebuffer == null) {
      return;
    }
    paramSelesFramebuffer.flagDestory();
    d(paramSelesFramebuffer);
    e(paramSelesFramebuffer);
    f(paramSelesFramebuffer);
  }
  
  private void d(SelesFramebuffer paramSelesFramebuffer)
  {
    if ((paramSelesFramebuffer.getFramebuffer() == 0) || (this.c.contains(Integer.valueOf(paramSelesFramebuffer.getFramebuffer())))) {
      return;
    }
    this.c.add(Integer.valueOf(paramSelesFramebuffer.getFramebuffer()));
  }
  
  private void e(SelesFramebuffer paramSelesFramebuffer)
  {
    if ((paramSelesFramebuffer.getTexture() == 0) || (this.b.contains(Integer.valueOf(paramSelesFramebuffer.getTexture())))) {
      return;
    }
    this.b.add(Integer.valueOf(paramSelesFramebuffer.getTexture()));
  }
  
  private void f(SelesFramebuffer paramSelesFramebuffer)
  {
    if ((paramSelesFramebuffer.getRenderbuffer() == 0) || (this.d.contains(Integer.valueOf(paramSelesFramebuffer.getRenderbuffer())))) {
      return;
    }
    this.d.add(Integer.valueOf(paramSelesFramebuffer.getRenderbuffer()));
  }
  
  public void clearRecycle()
  {
    if (this.b.size() > 0)
    {
      a(ArrayHelper.toIntArray(this.b));
      this.b.clear();
    }
    if (this.c.size() > 0)
    {
      b(ArrayHelper.toIntArray(this.c));
      this.c.clear();
    }
    if (this.d.size() > 0)
    {
      c(ArrayHelper.toIntArray(this.d));
      this.d.clear();
    }
  }
  
  private void a(int[] paramArrayOfInt)
  {
    if (paramArrayOfInt == null) {
      return;
    }
    GLES20.glDeleteTextures(paramArrayOfInt.length, paramArrayOfInt, 0);
  }
  
  private void b(int[] paramArrayOfInt)
  {
    if (paramArrayOfInt == null) {
      return;
    }
    GLES20.glDeleteFramebuffers(paramArrayOfInt.length, paramArrayOfInt, 0);
  }
  
  private void c(int[] paramArrayOfInt)
  {
    if (paramArrayOfInt == null) {
      return;
    }
    GLES20.glDeleteRenderbuffers(paramArrayOfInt.length, paramArrayOfInt, 0);
  }
  
  public void destory()
  {
    Iterator localIterator = this.a.values().iterator();
    while (localIterator.hasNext())
    {
      BlockingQueue localBlockingQueue = (BlockingQueue)localIterator.next();
      while (!localBlockingQueue.isEmpty()) {
        try
        {
          c((SelesFramebuffer)localBlockingQueue.take());
        }
        catch (InterruptedException localInterruptedException)
        {
          TLog.e(localInterruptedException);
        }
      }
    }
    this.a.clear();
    clearRecycle();
    if (this.e.size() > 0)
    {
      a(ArrayHelper.toIntArray(this.e));
      this.e.clear();
    }
    if (this.f.size() > 0)
    {
      b(ArrayHelper.toIntArray(this.f));
      this.f.clear();
    }
    if (this.g.size() > 0)
    {
      c(ArrayHelper.toIntArray(this.g));
      this.g.clear();
    }
    TLog.dump("%s destory() %s|%s", new Object[] { "SelesFramebufferCache", this, SelesContext.currentEGLContext() });
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\SelesFramebufferCache.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */