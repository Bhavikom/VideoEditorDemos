package org.lasque.tusdk.core.api.engine;

import android.graphics.RectF;
import org.lasque.tusdk.core.seles.SelesContext;
import org.lasque.tusdk.core.seles.extend.SelesVerticeCoordinateCorpBuilder;
import org.lasque.tusdk.core.utils.TLog;

public class TuSdkEngineImpl
  implements TuSdkEngine
{
  private boolean a = false;
  private TuSdkEngineOrientation b;
  private TuSdkEngineInputImage c;
  private TuSdkEngineOutputImage d;
  private TuSdkEngineProcessor e;
  private boolean f;
  private boolean g = false;
  private SelesVerticeCoordinateCorpBuilder h;
  private RectF i;
  
  public void setEngineInputImage(TuSdkEngineInputImage paramTuSdkEngineInputImage)
  {
    if (paramTuSdkEngineInputImage == null) {
      return;
    }
    if (this.c != null) {
      this.c.release();
    }
    this.c = paramTuSdkEngineInputImage;
    this.c.setTextureCoordinateBuilder(this.h);
    this.c.setPreCropRect(this.i);
    this.c.setEngineRotation(this.b);
  }
  
  public void setEngineOutputImage(TuSdkEngineOutputImage paramTuSdkEngineOutputImage)
  {
    if (paramTuSdkEngineOutputImage == null) {
      return;
    }
    if (this.d != null) {
      this.d.release();
    }
    this.d = paramTuSdkEngineOutputImage;
    this.d.setEngineRotation(this.b);
  }
  
  public void setEngineOrientation(TuSdkEngineOrientation paramTuSdkEngineOrientation)
  {
    if (paramTuSdkEngineOrientation == null) {
      return;
    }
    if (this.b != null) {
      this.b.release();
    }
    this.b = paramTuSdkEngineOrientation;
  }
  
  public void setEngineProcessor(TuSdkEngineProcessor paramTuSdkEngineProcessor)
  {
    if (paramTuSdkEngineProcessor == null) {
      return;
    }
    if (this.e != null) {
      this.e.release();
    }
    this.e = paramTuSdkEngineProcessor;
    this.e.setEngineRotation(this.b);
  }
  
  public void setInputTextureCoordinateBuilder(SelesVerticeCoordinateCorpBuilder paramSelesVerticeCoordinateCorpBuilder)
  {
    this.h = paramSelesVerticeCoordinateCorpBuilder;
    if (this.c != null) {
      this.c.setTextureCoordinateBuilder(this.h);
    }
  }
  
  public void setInputPreCropRect(RectF paramRectF)
  {
    this.i = paramRectF;
    if (this.c != null) {
      this.c.setPreCropRect(this.i);
    }
  }
  
  public TuSdkEngineImpl(boolean paramBoolean)
  {
    this.f = paramBoolean;
  }
  
  public void release()
  {
    if (this.a) {
      return;
    }
    this.a = true;
    if (this.c != null)
    {
      this.c.release();
      this.c = null;
    }
    if (this.d != null)
    {
      this.d.release();
      this.d = null;
    }
    if (this.b != null)
    {
      this.b.release();
      this.b = null;
    }
    if (this.e != null)
    {
      this.e.release();
      this.e = null;
    }
    if (this.f) {
      SelesContext.destroyContext(SelesContext.currentEGLContext());
    }
  }
  
  protected void finalize()
  {
    release();
    super.finalize();
  }
  
  public boolean prepareInGlThread()
  {
    if (!a("prepare")) {
      return false;
    }
    if (this.g) {
      return true;
    }
    this.g = true;
    if (this.f) {
      SelesContext.createEGLContext(SelesContext.currentEGLContext());
    }
    this.c.setEngineRotation(this.b);
    this.e.setEngineRotation(this.b);
    this.d.setEngineRotation(this.b);
    this.c.bindEngineProcessor(this.e);
    this.e.bindEngineOutput(this.d);
    return true;
  }
  
  private boolean a(String paramString)
  {
    if (this.a)
    {
      TLog.w("%s %s has released.", new Object[] { "TuSdkEngineImpl", paramString });
      return false;
    }
    if (this.b == null)
    {
      TLog.w("%s %s need setEngineOrientation first.", new Object[] { "TuSdkEngineImpl", paramString });
      return false;
    }
    if (this.c == null)
    {
      TLog.w("%s %s need setEngineInputImage first.", new Object[] { "TuSdkEngineImpl", paramString });
      return false;
    }
    if (this.e == null)
    {
      TLog.w("%s %s need setEngineProcessor first.", new Object[] { "TuSdkEngineImpl", paramString });
      return false;
    }
    if (this.d == null)
    {
      TLog.w("%s %s need setEngineOutputImage first.", new Object[] { "TuSdkEngineImpl", paramString });
      return false;
    }
    return true;
  }
  
  public void processFrame(byte[] paramArrayOfByte, int paramInt1, int paramInt2, long paramLong)
  {
    processFrame(-1, paramInt1, paramInt2, paramArrayOfByte, paramLong);
  }
  
  public void processFrame(int paramInt1, int paramInt2, int paramInt3, long paramLong)
  {
    processFrame(paramInt1, paramInt2, paramInt3, null, paramLong);
  }
  
  public void processFrame(int paramInt1, int paramInt2, int paramInt3, byte[] paramArrayOfByte, long paramLong)
  {
    if (!a("processFrame")) {
      return;
    }
    this.b.setInputSize(paramInt2, paramInt3);
    this.c.processFrame(paramInt1, paramInt2, paramInt3, paramArrayOfByte, paramLong);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\api\engine\TuSdkEngineImpl.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */