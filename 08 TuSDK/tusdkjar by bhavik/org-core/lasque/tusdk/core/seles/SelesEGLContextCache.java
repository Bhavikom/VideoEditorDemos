package org.lasque.tusdk.core.seles;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import org.lasque.tusdk.core.utils.TLog;

public class SelesEGLContextCache
{
  private final HashMap<String, SelesGLProgram> a = new HashMap();
  private final SelesFramebufferCache b = new SelesFramebufferCache();
  private final SelesEGLBufferCache c = new SelesEGLBufferCache();
  
  public SelesGLProgram getProgram(String paramString1, String paramString2)
  {
    if ((paramString1 == null) || (paramString2 == null)) {
      return null;
    }
    String str = String.format("V: %s - F: %s", new Object[] { paramString1, paramString2 });
    SelesGLProgram localSelesGLProgram = (SelesGLProgram)this.a.get(str);
    if (localSelesGLProgram == null)
    {
      localSelesGLProgram = SelesGLProgram.create(paramString1, paramString2);
      this.a.put(str, localSelesGLProgram);
    }
    return localSelesGLProgram;
  }
  
  public SelesFramebufferCache sharedFramebufferCache()
  {
    return this.b;
  }
  
  public void returnFramebufferToCache(SelesFramebuffer paramSelesFramebuffer)
  {
    this.b.returnFramebufferToCache(paramSelesFramebuffer);
  }
  
  public void recycleFramebuffer(SelesFramebuffer paramSelesFramebuffer)
  {
    this.b.recycleFramebuffer(paramSelesFramebuffer);
  }
  
  public SelesEGLBufferCache sharedEGLBufferCache()
  {
    return this.c;
  }
  
  public void destory()
  {
    TLog.dump("%s destory() %s|%s", new Object[] { "SelesEGLContextCache", this, SelesContext.currentEGLContext() });
    this.b.destory();
    this.c.destory();
    Iterator localIterator = this.a.values().iterator();
    while (localIterator.hasNext())
    {
      SelesGLProgram localSelesGLProgram = (SelesGLProgram)localIterator.next();
      localSelesGLProgram.destory();
    }
    this.a.clear();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\SelesEGLContextCache.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */