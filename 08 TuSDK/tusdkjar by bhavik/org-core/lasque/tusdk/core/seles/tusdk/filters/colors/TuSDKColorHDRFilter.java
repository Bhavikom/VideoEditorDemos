package org.lasque.tusdk.core.seles.tusdk.filters.colors;

import android.graphics.Bitmap;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.secret.TuSdkImageNative;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.SelesParameters.FilterParameterInterface;
import org.lasque.tusdk.core.seles.SelesParameters.FilterTexturesInterface;
import org.lasque.tusdk.core.seles.filters.SelesThreeInputFilter;
import org.lasque.tusdk.core.seles.sources.SelesPicture;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;

public class TuSDKColorHDRFilter
  extends SelesThreeInputFilter
  implements SelesParameters.FilterParameterInterface, SelesParameters.FilterTexturesInterface
{
  public static final int CLIP_X_NUM = 8;
  public static final int CLIP_Y_NUM = 8;
  private float a = 0.125F;
  private float b = 0.125F;
  private int c;
  private int d;
  private int e;
  private float f = 0.5F;
  
  public static ByteBuffer getClipHistBuffer(Bitmap paramBitmap)
  {
    ByteBuffer localByteBuffer = ByteBuffer.allocate(16384);
    TuSdkImageNative.getClipHistList(paramBitmap, 8, 8, 2.0F, localByteBuffer.array());
    return localByteBuffer;
  }
  
  public TuSDKColorHDRFilter()
  {
    super("-schdr");
    disableSecondFrameCheck();
    disableThirdFrameCheck();
  }
  
  public TuSDKColorHDRFilter(FilterOption paramFilterOption)
  {
    this();
    if ((paramFilterOption != null) && (paramFilterOption.args != null) && (paramFilterOption.args.containsKey("mixied"))) {
      this.f = Float.parseFloat((String)paramFilterOption.args.get("mixied"));
    }
  }
  
  public void appendTextures(List<SelesPicture> paramList)
  {
    if (paramList == null) {
      return;
    }
    int i = 1;
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      SelesPicture localSelesPicture = (SelesPicture)localIterator.next();
      localSelesPicture.processImage();
      localSelesPicture.addTarget(this, i);
      i++;
    }
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.c = this.mFilterProgram.uniformIndex("clipX");
    this.d = this.mFilterProgram.uniformIndex("clipY");
    this.e = this.mFilterProgram.uniformIndex("HDRStrength");
    a(this.a);
    b(this.b);
    setStrength(this.f);
  }
  
  private void a(float paramFloat)
  {
    this.a = paramFloat;
    setFloat(this.a, this.c, this.mFilterProgram);
  }
  
  private void b(float paramFloat)
  {
    this.b = paramFloat;
    setFloat(this.b, this.d, this.mFilterProgram);
  }
  
  public float getStrength()
  {
    return this.f;
  }
  
  public void setStrength(float paramFloat)
  {
    this.f = paramFloat;
    setFloat(this.f, this.e, this.mFilterProgram);
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    paramSelesParameters.appendFloatArg("mixied", getStrength(), 0.0F, 1.0F);
    return paramSelesParameters;
  }
  
  protected void submitFilterArg(SelesParameters.FilterArg paramFilterArg)
  {
    if (paramFilterArg == null) {
      return;
    }
    if (paramFilterArg.equalsKey("mixied")) {
      setStrength(paramFilterArg.getValue());
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\colors\TuSDKColorHDRFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */