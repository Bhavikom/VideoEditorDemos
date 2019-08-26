package org.lasque.tusdk.core.seles.tusdk.filters.lives;

import java.nio.FloatBuffer;
import java.util.HashMap;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.struct.TuSdkSizeF;

public class TuSDKLiveEdgeMagicFilter
  extends SelesFilter
{
  private int a;
  private int b;
  private float c = 1.6F;
  
  public TuSDKLiveEdgeMagicFilter()
  {
    super("-ssev1", "-slive02f");
  }
  
  public TuSDKLiveEdgeMagicFilter(FilterOption paramFilterOption)
  {
    this();
    if ((paramFilterOption != null) && (paramFilterOption.args != null) && (paramFilterOption.args.containsKey("edgeStrength")))
    {
      float f = Float.parseFloat((String)paramFilterOption.args.get("edgeStrength"));
      if (f > 0.0F) {
        setEdgeStrength(f);
      }
    }
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.a = this.mFilterProgram.uniformIndex("stepOffset");
    this.b = this.mFilterProgram.uniformIndex("edgeStrength");
    setEdgeStrength(this.c);
    checkGLError(getClass().getSimpleName() + " onInitOnGLThread");
  }
  
  public void setupFilterForSize(final TuSdkSize paramTuSdkSize)
  {
    super.setupFilterForSize(paramTuSdkSize);
    if ((paramTuSdkSize == null) || (!paramTuSdkSize.isSize())) {
      return;
    }
    runOnDraw(new Runnable()
    {
      public void run()
      {
        TuSdkSizeF localTuSdkSizeF = TuSdkSizeF.create(1.0F / paramTuSdkSize.width, 1.0F / paramTuSdkSize.height);
        TuSDKLiveEdgeMagicFilter.this.setSize(localTuSdkSizeF, TuSDKLiveEdgeMagicFilter.a(TuSDKLiveEdgeMagicFilter.this), TuSDKLiveEdgeMagicFilter.b(TuSDKLiveEdgeMagicFilter.this));
      }
    });
  }
  
  protected void renderToTexture(FloatBuffer paramFloatBuffer1, FloatBuffer paramFloatBuffer2)
  {
    super.renderToTexture(paramFloatBuffer1, paramFloatBuffer2);
    checkGLError(getClass().getSimpleName());
    captureFilterImage(getClass().getSimpleName(), this.mInputTextureSize.width, this.mInputTextureSize.height);
  }
  
  public float getEdgeStrength()
  {
    return this.c;
  }
  
  public void setEdgeStrength(float paramFloat)
  {
    this.c = paramFloat;
    setFloat(this.c, this.b, this.mFilterProgram);
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    paramSelesParameters.appendFloatArg("edgeStrength", getEdgeStrength(), 0.0F, 4.0F);
    return paramSelesParameters;
  }
  
  protected void submitFilterArg(SelesParameters.FilterArg paramFilterArg)
  {
    if (paramFilterArg == null) {
      return;
    }
    if (paramFilterArg.equalsKey("edgeStrength")) {
      setEdgeStrength(paramFilterArg.getValue());
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\lives\TuSDKLiveEdgeMagicFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */