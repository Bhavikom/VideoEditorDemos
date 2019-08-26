package org.lasque.tusdk.video.editor;

import android.graphics.PointF;
import android.text.TextUtils;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.seles.tusdk.particle.TuSDKParticleFilter;
import org.lasque.tusdk.core.utils.TLog;

public class TuSdkMediaParticleEffectData
  extends TuSdkMediaEffectData
{
  private String a;
  private ConcurrentHashMap<Long, PointF> b = new ConcurrentHashMap(10);
  private float c;
  private int d;
  private DecimalFormat e = new DecimalFormat(".00");
  
  public TuSdkMediaParticleEffectData(String paramString)
  {
    this.a = paramString;
    setMediaEffectType(TuSdkMediaEffectData.TuSdkMediaEffectDataType.TuSdkMediaEffectDataTypeParticle);
    setVaild(!TextUtils.isEmpty(paramString));
    if (!isVaild()) {
      TLog.e("Invalid particle effect code ：%s", new Object[] { paramString });
    }
  }
  
  public TuSdkMediaParticleEffectData(String paramString, TuSdkTimeRange paramTuSdkTimeRange)
  {
    this(paramString);
    setAtTimeRange(paramTuSdkTimeRange);
  }
  
  public void setSize(float paramFloat)
  {
    this.c = paramFloat;
  }
  
  public float getSize()
  {
    return this.c;
  }
  
  public void setColor(int paramInt)
  {
    this.d = paramInt;
  }
  
  public int getColor()
  {
    return this.d;
  }
  
  public String getParticleCode()
  {
    return this.a;
  }
  
  public void putPoint(long paramLong, PointF paramPointF)
  {
    this.b.put(Long.valueOf(paramLong / 1000L), paramPointF);
  }
  
  public PointF getPointF(long paramLong)
  {
    PointF localPointF = (PointF)this.b.get(Long.valueOf(paramLong / 1000L));
    if (localPointF != null) {
      return localPointF;
    }
    Iterator localIterator = this.b.keySet().iterator();
    while (localIterator.hasNext())
    {
      Long localLong = (Long)localIterator.next();
      float f1 = Float.valueOf(this.e.format((float)localLong.longValue() / 1000.0F)).floatValue();
      float f2 = Float.valueOf(this.e.format((float)paramLong / 1000.0F / 1000.0F)).floatValue();
      if (f1 == f2) {
        return (PointF)this.b.get(localLong);
      }
    }
    return null;
  }
  
  public void clearPoints()
  {
    this.b.clear();
  }
  
  public TuSdkMediaEffectData clone()
  {
    TuSdkMediaParticleEffectData localTuSdkMediaParticleEffectData = new TuSdkMediaParticleEffectData(this.a);
    localTuSdkMediaParticleEffectData.setColor(this.d);
    localTuSdkMediaParticleEffectData.setSize(this.c);
    localTuSdkMediaParticleEffectData.b = new ConcurrentHashMap(this.b);
    localTuSdkMediaParticleEffectData.setAtTimeRange(getAtTimeRange());
    localTuSdkMediaParticleEffectData.setVaild(true);
    localTuSdkMediaParticleEffectData.setMediaEffectType(getMediaEffectType());
    localTuSdkMediaParticleEffectData.setIsApplied(false);
    return localTuSdkMediaParticleEffectData;
  }
  
  public void resetParticleFilter()
  {
    if ((this.mFilterWrap == null) || (this.mFilterWrap.getFilter() == null)) {
      return;
    }
    SelesOutInput localSelesOutInput = this.mFilterWrap.getFilter();
    if ((localSelesOutInput instanceof TuSDKParticleFilter)) {
      ((TuSDKParticleFilter)localSelesOutInput).reset();
    }
  }
  
  public synchronized FilterWrap getFilterWrap()
  {
    if (this.mFilterWrap == null)
    {
      FilterOption localFilterOption = FilterLocalPackage.shared().option(this.a);
      this.mFilterWrap = FilterWrap.creat(localFilterOption);
      this.mFilterWrap.setParticleColor(getColor());
      this.mFilterWrap.setParticleSize(getSize());
      this.mFilterWrap.processImage();
    }
    return this.mFilterWrap;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKVideo-3.4.1.jar!\org\lasque\tusdk\video\editor\TuSdkMediaParticleEffectData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */