package org.lasque.tusdk.core.seles.tusdk.filters.lights;

import java.util.HashMap;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.SelesParameters.FilterParameterInterface;
import org.lasque.tusdk.core.seles.filters.SelesTwoInputFilter;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.struct.TuSdkSizeF;

public class TuSDKSobelEdgeFilter
  extends SelesTwoInputFilter
  implements SelesParameters.FilterParameterInterface
{
  private int a;
  private int b;
  private int c;
  private int d;
  private float e = 1.0F;
  private float f = 1.0F;
  private float g = 0.0F;
  private float h = 0.03F;
  
  public TuSDKSobelEdgeFilter()
  {
    super("-ssev1", "-ssef1");
    disableSecondFrameCheck();
  }
  
  public TuSDKSobelEdgeFilter(FilterOption paramFilterOption)
  {
    this();
    if ((paramFilterOption != null) && (paramFilterOption.args != null))
    {
      float f1;
      if (paramFilterOption.args.containsKey("edgeStrength"))
      {
        f1 = Float.parseFloat((String)paramFilterOption.args.get("edgeStrength"));
        if (f1 > 0.0F) {
          a(f1);
        }
      }
      if (paramFilterOption.args.containsKey("thresholdLevel"))
      {
        f1 = Float.parseFloat((String)paramFilterOption.args.get("thresholdLevel"));
        if (f1 > 0.0F) {
          b(f1);
        }
      }
      if (paramFilterOption.args.containsKey("speed"))
      {
        f1 = Float.parseFloat((String)paramFilterOption.args.get("speed"));
        if (f1 > 0.0F) {
          d(f1);
        }
      }
      if (paramFilterOption.args.containsKey("showType"))
      {
        f1 = Float.parseFloat((String)paramFilterOption.args.get("showType"));
        if (f1 > 0.0F) {
          c(f1);
        }
      }
    }
  }
  
  protected void onInitOnGLThread()
  {
    super.onInitOnGLThread();
    this.a = this.mFilterProgram.uniformIndex("stepOffset");
    this.b = this.mFilterProgram.uniformIndex("edgeStrength");
    this.c = this.mFilterProgram.uniformIndex("thresholdLevel");
    this.d = this.mFilterProgram.uniformIndex("showType");
    a(this.e);
    b(this.f);
    c(this.g);
    d(this.h);
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
        TuSDKSobelEdgeFilter.this.setSize(localTuSdkSizeF, TuSDKSobelEdgeFilter.a(TuSDKSobelEdgeFilter.this), TuSDKSobelEdgeFilter.b(TuSDKSobelEdgeFilter.this));
        if (TuSDKSobelEdgeFilter.c(TuSDKSobelEdgeFilter.this) < 0.0F) {
          TuSDKSobelEdgeFilter.a(TuSDKSobelEdgeFilter.this, 1.0F);
        }
        TuSDKSobelEdgeFilter.b(TuSDKSobelEdgeFilter.this, TuSDKSobelEdgeFilter.d(TuSDKSobelEdgeFilter.this) - TuSDKSobelEdgeFilter.e(TuSDKSobelEdgeFilter.this));
      }
    });
  }
  
  private float a()
  {
    return this.e;
  }
  
  private void a(float paramFloat)
  {
    this.e = paramFloat;
    setFloat(this.e, this.b, this.mFilterProgram);
  }
  
  private float b()
  {
    return this.f;
  }
  
  private void b(float paramFloat)
  {
    this.f = paramFloat;
    setFloat(this.f, this.c, this.mFilterProgram);
  }
  
  private float c()
  {
    return this.g;
  }
  
  private void c(float paramFloat)
  {
    this.g = paramFloat;
    setFloat(this.g, this.d, this.mFilterProgram);
  }
  
  private float d()
  {
    return this.h;
  }
  
  private void d(float paramFloat)
  {
    if (paramFloat > 0.0F) {
      this.h = paramFloat;
    }
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    paramSelesParameters.appendFloatArg("edgeStrength", a(), 0.0F, 4.0F);
    paramSelesParameters.appendFloatArg("thresholdLevel", b(), 0.0F, 1.0F);
    paramSelesParameters.appendFloatArg("speed", d(), 0.0F, 0.1F);
    paramSelesParameters.appendFloatArg("showType", c(), 0.0F, 4.0F);
    return paramSelesParameters;
  }
  
  protected void submitFilterArg(SelesParameters.FilterArg paramFilterArg)
  {
    if (paramFilterArg == null) {
      return;
    }
    if (paramFilterArg.equalsKey("edgeStrength")) {
      a(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("thresholdLevel")) {
      b(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("speed")) {
      d(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("showType")) {
      c(paramFilterArg.getValue());
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\lights\TuSDKSobelEdgeFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */