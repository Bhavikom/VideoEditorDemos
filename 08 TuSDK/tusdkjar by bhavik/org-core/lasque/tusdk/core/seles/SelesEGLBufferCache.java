package org.lasque.tusdk.core.seles;

import android.opengl.GLES20;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.seles.egl.SelesEGLContext;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.ArrayHelper;
import org.lasque.tusdk.core.utils.TLog;

public class SelesEGLBufferCache
{
  private final List<SelesVertexbuffer> a = new ArrayList();
  private final List<SelesPixelBuffer> b = new ArrayList();
  private final List<Integer> c = new ArrayList();
  
  public SelesVertexbuffer fetchVertexbuffer(FloatBuffer paramFloatBuffer)
  {
    clearRecycle();
    if (paramFloatBuffer == null) {
      return null;
    }
    SelesVertexbuffer localSelesVertexbuffer = new SelesVertexbuffer(paramFloatBuffer);
    this.a.add(localSelesVertexbuffer);
    return localSelesVertexbuffer;
  }
  
  public void recycleVertexbuffer(SelesVertexbuffer paramSelesVertexbuffer)
  {
    if (paramSelesVertexbuffer == null) {
      return;
    }
    this.a.remove(paramSelesVertexbuffer);
    a(paramSelesVertexbuffer);
    if (paramSelesVertexbuffer.getEglContext().equalsCurrent()) {
      clearRecycle();
    }
  }
  
  public SelesPixelBuffer fetchPixelBuffer(TuSdkSize paramTuSdkSize, int paramInt)
  {
    clearRecycle();
    if ((paramTuSdkSize == null) || (!paramTuSdkSize.isSize()) || (paramInt < 1)) {
      return null;
    }
    SelesPixelBuffer localSelesPixelBuffer = new SelesPixelBuffer(paramTuSdkSize, paramInt);
    this.b.add(localSelesPixelBuffer);
    return localSelesPixelBuffer;
  }
  
  public void recyclePixelbuffer(SelesPixelBuffer paramSelesPixelBuffer)
  {
    if (paramSelesPixelBuffer == null) {
      return;
    }
    this.b.remove(paramSelesPixelBuffer);
    a(paramSelesPixelBuffer);
    if (paramSelesPixelBuffer.getEglContext().equalsCurrent()) {
      clearRecycle();
    }
  }
  
  private void a(SelesVertexbuffer paramSelesVertexbuffer)
  {
    if (paramSelesVertexbuffer == null) {
      return;
    }
    paramSelesVertexbuffer.flagDestory();
    if ((paramSelesVertexbuffer.getVertexbuffer() == 0) || (this.c.contains(Integer.valueOf(paramSelesVertexbuffer.getVertexbuffer())))) {
      return;
    }
    this.c.add(Integer.valueOf(paramSelesVertexbuffer.getVertexbuffer()));
  }
  
  private void a(SelesPixelBuffer paramSelesPixelBuffer)
  {
    if (paramSelesPixelBuffer == null) {
      return;
    }
    paramSelesPixelBuffer.flagDestory();
    if (paramSelesPixelBuffer.getPixelbuffers() == null) {
      return;
    }
    int i = 0;
    int j = paramSelesPixelBuffer.getPixelbuffers().length;
    while (i < j)
    {
      if (this.c.contains(Integer.valueOf(paramSelesPixelBuffer.getPixelbuffers()[i]))) {
        this.c.add(Integer.valueOf(paramSelesPixelBuffer.getPixelbuffers()[i]));
      }
      i++;
    }
  }
  
  public void clearRecycle()
  {
    if (this.c.size() < 1) {
      return;
    }
    int[] arrayOfInt = ArrayHelper.toIntArray(this.c);
    this.c.clear();
    GLES20.glDeleteBuffers(arrayOfInt.length, arrayOfInt, 0);
  }
  
  public void destory()
  {
    Iterator localIterator = this.a.iterator();
    Object localObject;
    while (localIterator.hasNext())
    {
      localObject = (SelesVertexbuffer)localIterator.next();
      a((SelesVertexbuffer)localObject);
    }
    this.a.clear();
    localIterator = this.b.iterator();
    while (localIterator.hasNext())
    {
      localObject = (SelesPixelBuffer)localIterator.next();
      a((SelesPixelBuffer)localObject);
    }
    this.b.clear();
    clearRecycle();
    TLog.dump("%s destory() %s|%s", new Object[] { getClass().getSimpleName(), this, SelesContext.currentEGLContext() });
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\SelesEGLBufferCache.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */