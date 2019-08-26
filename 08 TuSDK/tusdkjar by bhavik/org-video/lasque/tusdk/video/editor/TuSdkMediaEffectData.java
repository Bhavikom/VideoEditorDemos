package org.lasque.tusdk.video.editor;

import java.util.ArrayList;
import java.util.List;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.seles.SelesParameters.FilterArg;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.utils.TLog;

public abstract class TuSdkMediaEffectData
  implements TuSdkMediaEffectParameterInterface
{
  private boolean a = false;
  private boolean b = false;
  private TuSdkTimeRange c;
  private TuSdkMediaEffectDataType d;
  protected FilterWrap mFilterWrap;
  
  public TuSdkTimeRange getAtTimeRange()
  {
    return this.c;
  }
  
  public void setAtTimeRange(TuSdkTimeRange paramTuSdkTimeRange)
  {
    this.c = paramTuSdkTimeRange;
  }
  
  public boolean validateTimeRange()
  {
    return (getAtTimeRange() != null) && (getAtTimeRange().isValid());
  }
  
  protected final void setVaild(boolean paramBoolean)
  {
    this.b = paramBoolean;
  }
  
  public final boolean isVaild()
  {
    return this.b;
  }
  
  public final boolean isApplied()
  {
    return this.a;
  }
  
  public void setIsApplied(boolean paramBoolean)
  {
    this.a = paramBoolean;
  }
  
  public TuSdkMediaEffectDataType getMediaEffectType()
  {
    return this.d;
  }
  
  protected void setMediaEffectType(TuSdkMediaEffectDataType paramTuSdkMediaEffectDataType)
  {
    this.d = paramTuSdkMediaEffectDataType;
  }
  
  public abstract TuSdkMediaEffectData clone();
  
  public abstract FilterWrap getFilterWrap();
  
  public void destory()
  {
    if (this.mFilterWrap == null) {
      return;
    }
    this.mFilterWrap.destroy();
    this.mFilterWrap = null;
  }
  
  private SelesParameters a()
  {
    if (getFilterWrap() == null) {
      return null;
    }
    return getFilterWrap().getFilterParameter();
  }
  
  public final SelesParameters.FilterArg getFilterArg(String paramString)
  {
    if (a() == null) {
      return null;
    }
    return a().getFilterArg(paramString);
  }
  
  public final List<SelesParameters.FilterArg> getFilterArgs()
  {
    if (a() == null) {
      return new ArrayList();
    }
    return a().getArgs();
  }
  
  public final void submitParameter(String paramString, float paramFloat)
  {
    SelesParameters.FilterArg localFilterArg = getFilterArg(paramString);
    if ((localFilterArg == null) || (getFilterWrap() == null)) {
      return;
    }
    localFilterArg.setPrecentValue(paramFloat);
    submitParameters();
  }
  
  public final void submitParameter(int paramInt, float paramFloat)
  {
    if ((this.mFilterWrap == null) || (paramInt <= 0) || (paramInt > getFilterArgs().size() - 1))
    {
      TLog.i("submitParameter failed", new Object[0]);
      return;
    }
    SelesParameters.FilterArg localFilterArg = (SelesParameters.FilterArg)getFilterArgs().get(paramInt);
    submitParameter(localFilterArg.getKey(), paramFloat);
  }
  
  public final void submitParameters()
  {
    if (getFilterWrap() == null) {
      return;
    }
    this.mFilterWrap.submitFilterParameter();
  }
  
  public void resetParameters()
  {
    if ((this.mFilterWrap == null) || (this.mFilterWrap.getFilterParameter() == null)) {
      return;
    }
    this.mFilterWrap.getFilterParameter().reset();
  }
  
  public static enum TuSdkMediaEffectDataType
  {
    private TuSdkMediaEffectDataType() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\video\editor\TuSdkMediaEffectData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */