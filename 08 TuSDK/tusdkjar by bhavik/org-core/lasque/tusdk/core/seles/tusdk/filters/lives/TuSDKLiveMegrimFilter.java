package org.lasque.tusdk.core.seles.tusdk.filters.lives;

import java.util.HashMap;
import java.util.List;
import org.lasque.tusdk.core.seles.SelesContext.SelesInput;
import org.lasque.tusdk.core.seles.SelesGLProgram;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.filters.SelesFilter;
import org.lasque.tusdk.core.seles.filters.SelesFilterGroup;
import org.lasque.tusdk.core.seles.filters.SelesFrameKeepFilter;
import org.lasque.tusdk.core.seles.filters.SelesTwoInputFilter;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.utils.ThreadHelper;

public class TuSDKLiveMegrimFilter
  extends SelesFilterGroup
{
  private SelesFrameKeepFilter a;
  private _TuSDKLiveMegrimMixedFilter b = new _TuSDKLiveMegrimMixedFilter();
  private SelesFilter c;
  
  public TuSDKLiveMegrimFilter()
  {
    addFilter(this.b);
    this.a = new SelesFrameKeepFilter();
    addFilter(this.a);
    this.a.addTarget(this.b, 1);
    this.b.addTarget(this.a, 0);
    setInitialFilters(new SelesOutInput[] { this.b });
    setTerminalFilter(this.b);
    setAlpha(1.0F);
  }
  
  public TuSDKLiveMegrimFilter(FilterOption paramFilterOption)
  {
    this();
    if ((paramFilterOption != null) && (paramFilterOption.args != null))
    {
      float f;
      if (paramFilterOption.args.containsKey("red"))
      {
        f = Float.parseFloat((String)paramFilterOption.args.get("green"));
        if (f > 0.0F) {
          setRed(f);
        }
      }
      if (paramFilterOption.args.containsKey("green"))
      {
        f = Float.parseFloat((String)paramFilterOption.args.get("green"));
        if (f > 0.0F) {
          setGreen(f);
        }
      }
      if (paramFilterOption.args.containsKey("blue"))
      {
        f = Float.parseFloat((String)paramFilterOption.args.get("blue"));
        if (f > 0.0F) {
          setBlue(f);
        }
      }
    }
  }
  
  public void enableSeprarate()
  {
    if (this.c != null) {
      return;
    }
    this.c = new SelesFilter();
    addFilter(this.c);
    this.b.addTarget(this.c, 0);
    setTerminalFilter(this.c);
  }
  
  public float[] getColor()
  {
    return this.b.getMix();
  }
  
  public void setColor(float[] paramArrayOfFloat)
  {
    this.b.setMix(paramArrayOfFloat);
  }
  
  public float getRed()
  {
    return this.b.getMix()[0];
  }
  
  public void setRed(float paramFloat)
  {
    float[] arrayOfFloat = getColor();
    arrayOfFloat[0] = paramFloat;
    setColor(arrayOfFloat);
  }
  
  public float getGreen()
  {
    return this.b.getMix()[1];
  }
  
  public void setGreen(float paramFloat)
  {
    float[] arrayOfFloat = getColor();
    arrayOfFloat[1] = paramFloat;
    setColor(arrayOfFloat);
  }
  
  public float getBlue()
  {
    return this.b.getMix()[2];
  }
  
  public void setBlue(float paramFloat)
  {
    float[] arrayOfFloat = getColor();
    arrayOfFloat[2] = paramFloat;
    setColor(arrayOfFloat);
  }
  
  public float getAlpha()
  {
    return this.b.getMix()[3];
  }
  
  public void setAlpha(float paramFloat)
  {
    float[] arrayOfFloat = getColor();
    arrayOfFloat[3] = paramFloat;
    setColor(arrayOfFloat);
  }
  
  public void addTarget(SelesContext.SelesInput paramSelesInput, int paramInt)
  {
    getTerminalFilter().addTarget(paramSelesInput, paramInt);
    if ((getTerminalFilter() == this.b) && (!getTerminalFilter().targets().contains(this.a))) {
      getTerminalFilter().addTarget(this.a, 1);
    }
  }
  
  public void newFrameReady(long paramLong, int paramInt)
  {
    a();
    super.newFrameReady(paramLong, paramInt);
  }
  
  private void a()
  {
    if (getRed() > 0.9F)
    {
      getParameter().setFilterArg("red", getRed() - 0.003F);
      submitParameter();
    }
    else if (getRed() != 0.0F)
    {
      getParameter().setFilterArg("red", getRed() - 0.01F);
      submitParameter();
    }
    if (getBlue() < 0.8F)
    {
      getParameter().setFilterArg("blue", getBlue() + 0.003F);
      submitParameter();
    }
  }
  
  protected SelesParameters initParams(SelesParameters paramSelesParameters)
  {
    paramSelesParameters = super.initParams(paramSelesParameters);
    paramSelesParameters.appendFloatArg("strength", getAlpha(), 0.0F, 1.0F);
    paramSelesParameters.appendFloatArg("red", getRed(), 0.0F, 1.0F);
    paramSelesParameters.appendFloatArg("green", getGreen(), 0.0F, 1.0F);
    paramSelesParameters.appendFloatArg("blue", getBlue(), 0.0F, 1.0F);
    return paramSelesParameters;
  }
  
  protected void submitFilterArg(SelesParameters.FilterArg paramFilterArg)
  {
    if (paramFilterArg == null) {
      return;
    }
    if (paramFilterArg.equalsKey("strength")) {
      setAlpha(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("red")) {
      setRed(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("green")) {
      setGreen(paramFilterArg.getValue());
    } else if (paramFilterArg.equalsKey("blue")) {
      setBlue(paramFilterArg.getValue());
    }
  }
  
  private class _TuSDKLiveMegrimMixedFilter
    extends SelesTwoInputFilter
  {
    private int b;
    private float[] c = { 0.0F, 0.75F, 0.8F, 1.0F };
    
    public _TuSDKLiveMegrimMixedFilter()
    {
      super();
      disableSecondFrameCheck();
    }
    
    protected void onInitOnGLThread()
    {
      super.onInitOnGLThread();
      this.b = this.mFilterProgram.uniformIndex("mixturePercent");
      ThreadHelper.postDelayed(new Runnable()
      {
        public void run()
        {
          TuSDKLiveMegrimFilter._TuSDKLiveMegrimMixedFilter.this.setMix(TuSDKLiveMegrimFilter._TuSDKLiveMegrimMixedFilter.a(TuSDKLiveMegrimFilter._TuSDKLiveMegrimMixedFilter.this));
        }
      }, 100L);
    }
    
    public float[] getMix()
    {
      return this.c;
    }
    
    public void setMix(float[] paramArrayOfFloat)
    {
      this.c = paramArrayOfFloat;
      setVec4(this.c, this.b, this.mFilterProgram);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\filters\lives\TuSDKLiveMegrimFilter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */