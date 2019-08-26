package org.lasque.tusdk.core.api.engine;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import org.lasque.tusdk.core.secret.ColorSpaceConvert;
import org.lasque.tusdk.core.seles.SelesContext.SelesInput;
import org.lasque.tusdk.core.seles.SelesFramebuffer;
import org.lasque.tusdk.core.seles.output.SelesOffscreen;
import org.lasque.tusdk.core.seles.output.SelesOffscreen.SelesOffscreenDelegate;
import org.lasque.tusdk.core.seles.output.SelesTerminalFilter;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.type.ColorFormatType;
import org.lasque.tusdk.core.utils.TLog;

public class TuSdkEngineOutputImageImpl
  implements TuSdkEngineOutputImage
{
  private TuSdkEngineOrientation a;
  private SelesTerminalFilter b = new SelesTerminalFilter();
  private SelesOffscreen c;
  private boolean d;
  private TuSdkSize e;
  private ByteBuffer f;
  private List<ByteBuffer> g;
  private Object h = new Object();
  private ColorFormatType i = ColorFormatType.NV21;
  private SelesOffscreen.SelesOffscreenDelegate j = new SelesOffscreen.SelesOffscreenDelegate()
  {
    public boolean onFrameRendered(SelesOffscreen paramAnonymousSelesOffscreen)
    {
      if (!TuSdkEngineOutputImageImpl.a(TuSdkEngineOutputImageImpl.this)) {
        return false;
      }
      IntBuffer localIntBuffer = paramAnonymousSelesOffscreen.renderBuffer();
      List localList;
      ByteBuffer localByteBuffer;
      synchronized (TuSdkEngineOutputImageImpl.b(TuSdkEngineOutputImageImpl.this))
      {
        localList = TuSdkEngineOutputImageImpl.c(TuSdkEngineOutputImageImpl.this);
        localByteBuffer = TuSdkEngineOutputImageImpl.d(TuSdkEngineOutputImageImpl.this);
      }
      if ((localIntBuffer == null) || (localList == null) || (localList.size() < 1)) {
        return true;
      }
      ??? = (ByteBuffer)localList.remove(0);
      ((ByteBuffer)???).position(0);
      byte[] arrayOfByte = ((ByteBuffer)???).array();
      TuSdkEngineOutputImageImpl.a(TuSdkEngineOutputImageImpl.this, localIntBuffer.array(), arrayOfByte, TuSdkEngineOutputImageImpl.e(TuSdkEngineOutputImageImpl.this));
      synchronized (TuSdkEngineOutputImageImpl.b(TuSdkEngineOutputImageImpl.this))
      {
        TuSdkEngineOutputImageImpl.a(TuSdkEngineOutputImageImpl.this, (ByteBuffer)???);
        if (localByteBuffer != null) {
          localList.add(localByteBuffer);
        }
      }
      return true;
    }
  };
  
  public void setEngineRotation(TuSdkEngineOrientation paramTuSdkEngineOrientation)
  {
    this.a = paramTuSdkEngineOrientation;
  }
  
  public List<SelesContext.SelesInput> getInputs()
  {
    ArrayList localArrayList = new ArrayList(2);
    if (this.c != null) {
      localArrayList.add(this.c);
    }
    if (this.b != null) {
      localArrayList.add(this.b);
    }
    return localArrayList;
  }
  
  public void release()
  {
    if (this.b != null)
    {
      this.b.destroy();
      this.b = null;
    }
    if (this.c != null)
    {
      this.c.setDelegate(null);
      this.c.setEnabled(false);
      this.c.destroy();
      this.c = null;
    }
  }
  
  protected void finalize()
  {
    release();
    super.finalize();
  }
  
  public void willProcessFrame(long paramLong)
  {
    if ((this.a == null) || (this.b == null)) {
      return;
    }
    this.b.setInputRotation(this.a.getOutputOrientation(), 0);
  }
  
  public int getTerminalTexture()
  {
    if ((this.b == null) || (this.b.framebufferForOutput() == null)) {
      return -1;
    }
    return this.b.framebufferForOutput().getTexture();
  }
  
  public void setYuvOutputImageFormat(ColorFormatType paramColorFormatType)
  {
    if (paramColorFormatType == null) {
      return;
    }
    this.i = paramColorFormatType;
  }
  
  public void setEnableOutputYUVData(boolean paramBoolean)
  {
    this.d = paramBoolean;
    if ((this.d) && (this.c == null))
    {
      this.c = new SelesOffscreen();
      this.c.setDelegate(this.j);
    }
    if (this.d) {
      this.c.startWork();
    } else {
      this.c.setEnabled(false);
    }
  }
  
  public void snatchFrame(byte[] paramArrayOfByte)
  {
    if (!a()) {
      return;
    }
    ByteBuffer localByteBuffer;
    synchronized (this.h)
    {
      localByteBuffer = this.f;
    }
    if ((localByteBuffer == null) || (paramArrayOfByte.length != localByteBuffer.capacity())) {
      return;
    }
    this.c.setInputRotation(this.a.getYuvOutputOrienation(), 0);
    localByteBuffer.position(0);
    localByteBuffer.get(paramArrayOfByte);
  }
  
  private boolean a()
  {
    if ((!this.d) || (this.c == null)) {
      return false;
    }
    if (this.a == null)
    {
      TLog.w("%s checkBuffer need setEngineRotation first.", new Object[] { "TuSdkEngineOutputImageImpl" });
      return false;
    }
    if ((this.a.getInputSize().equals(this.e)) && (this.g != null)) {
      return true;
    }
    synchronized (this.h)
    {
      this.g = null;
      this.f = null;
      this.e = this.a.getInputSize();
      ArrayList localArrayList = new ArrayList(3);
      int k = this.e.width * this.e.height * 3 / 2;
      for (int m = 0; m < 3; m++) {
        localArrayList.add(ByteBuffer.allocate(k));
      }
      this.g = localArrayList;
    }
    return true;
  }
  
  private void a(int[] paramArrayOfInt, byte[] paramArrayOfByte, ColorFormatType paramColorFormatType)
  {
    if (this.c == null) {
      return;
    }
    TuSdkSize localTuSdkSize = this.c.sizeOfFBO();
    if (paramColorFormatType == ColorFormatType.NV21) {
      ColorSpaceConvert.rgbaToNv21(paramArrayOfInt, localTuSdkSize.width, localTuSdkSize.height, paramArrayOfByte);
    } else if (paramColorFormatType == ColorFormatType.I420) {
      ColorSpaceConvert.rgbaToI420(paramArrayOfInt, localTuSdkSize.width, localTuSdkSize.height, paramArrayOfByte);
    } else if (paramColorFormatType == ColorFormatType.YV12) {
      ColorSpaceConvert.rgbaToYv12(paramArrayOfInt, localTuSdkSize.width, localTuSdkSize.height, paramArrayOfByte);
    } else {
      TLog.e("%s Unsupported image format", new Object[] { "TuSdkEngineOutputImageImpl" });
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\api\engine\TuSdkEngineOutputImageImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */