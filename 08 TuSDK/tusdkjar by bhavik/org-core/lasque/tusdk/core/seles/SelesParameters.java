package org.lasque.tusdk.core.seles;

import android.graphics.Bitmap;
import android.graphics.RectF;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.lasque.tusdk.core.face.FaceAligment;
import org.lasque.tusdk.core.seles.sources.SelesPicture;
import org.lasque.tusdk.core.seles.tusdk.liveSticker.TuSDKLiveStickerImage;
import org.lasque.tusdk.core.seles.tusdk.textSticker.TuSdkImage2DSticker;
import org.lasque.tusdk.core.utils.StringHelper;

public class SelesParameters
{
  private List<FilterArg> a = new ArrayList();
  private Map<String, String> b;
  private boolean c;
  
  public boolean isInited()
  {
    return this.c;
  }
  
  public List<FilterArg> getArgs()
  {
    return this.a;
  }
  
  public List<String> getArgKeys()
  {
    ArrayList localArrayList = new ArrayList(this.a.size());
    Iterator localIterator = this.a.iterator();
    while (localIterator.hasNext())
    {
      FilterArg localFilterArg = (FilterArg)localIterator.next();
      localArrayList.add(localFilterArg.getKey());
    }
    return localArrayList;
  }
  
  public int size()
  {
    if (this.a == null) {
      return 0;
    }
    return this.a.size();
  }
  
  public SelesParameters() {}
  
  public SelesParameters(Map<String, String> paramMap)
  {
    this.b = paramMap;
  }
  
  public void appendFloatArg(String paramString, float paramFloat)
  {
    appendFloatArg(paramString, paramFloat, 0.0F, 1.0F);
  }
  
  public void appendFloatArg(String paramString, float paramFloat1, float paramFloat2, float paramFloat3)
  {
    if (paramString == null) {
      return;
    }
    this.c = true;
    FilterArg localFilterArg = new FilterArg();
    FilterArg.a(localFilterArg, paramString);
    FilterArg.a(localFilterArg, FilterArg.b(localFilterArg, paramFloat1));
    FilterArg.c(localFilterArg, paramFloat2);
    FilterArg.d(localFilterArg, paramFloat3);
    if ((this.b != null) && (this.b.containsKey(paramString)))
    {
      float f = StringHelper.parseFloat((String)this.b.get(paramString));
      if ((0.0F <= f) && (f <= 1.0F))
      {
        localFilterArg.setPrecentValue(f);
        FilterArg.a(localFilterArg, false);
        FilterArg.a(localFilterArg, localFilterArg.getValue());
      }
    }
    this.a.add(localFilterArg);
  }
  
  public ArrayList<FilterArg> changedArgs()
  {
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = this.a.iterator();
    while (localIterator.hasNext())
    {
      FilterArg localFilterArg = (FilterArg)localIterator.next();
      if (FilterArg.a(localFilterArg))
      {
        FilterArg.a(localFilterArg, false);
        localArrayList.add(localFilterArg);
      }
    }
    return localArrayList;
  }
  
  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof SelesParameters)) || (this.a.size() != ((SelesParameters)paramObject).getArgs().size())) {
      return false;
    }
    List localList = ((SelesParameters)paramObject).getArgs();
    int i = 0;
    int j = localList.size();
    while (i < j)
    {
      if (!FilterArg.b((FilterArg)localList.get(i)).equalsIgnoreCase(FilterArg.b((FilterArg)this.a.get(i)))) {
        return false;
      }
      i++;
    }
    return true;
  }
  
  public void reset()
  {
    Iterator localIterator = this.a.iterator();
    while (localIterator.hasNext())
    {
      FilterArg localFilterArg = (FilterArg)localIterator.next();
      localFilterArg.reset();
    }
  }
  
  public void reset(String paramString)
  {
    FilterArg localFilterArg = getFilterArg(paramString);
    if (localFilterArg == null) {
      return;
    }
    localFilterArg.reset();
  }
  
  public FilterArg getFilterArg(String paramString)
  {
    if (StringHelper.isBlank(paramString)) {
      return null;
    }
    Iterator localIterator = this.a.iterator();
    while (localIterator.hasNext())
    {
      FilterArg localFilterArg = (FilterArg)localIterator.next();
      if (localFilterArg.equalsKey(paramString)) {
        return localFilterArg;
      }
    }
    return null;
  }
  
  public float getDefaultArg(String paramString)
  {
    if ((this.b != null) && (this.b.containsKey(paramString))) {
      return StringHelper.parseFloat((String)this.b.get(paramString));
    }
    return 0.0F;
  }
  
  public void setFilterArg(String paramString, float paramFloat)
  {
    FilterArg localFilterArg = getFilterArg(paramString);
    if (localFilterArg == null) {
      return;
    }
    localFilterArg.setPrecentValue(paramFloat);
  }
  
  public void stepFilterArg(String paramString, float paramFloat)
  {
    FilterArg localFilterArg = getFilterArg(paramString);
    if (localFilterArg == null) {
      return;
    }
    localFilterArg.setPrecentValue(localFilterArg.getPrecentValue() + paramFloat);
  }
  
  public void merge(SelesParameters paramSelesParameters)
  {
    if (paramSelesParameters == null) {
      return;
    }
    this.a.addAll(paramSelesParameters.a);
  }
  
  public void syncArgs(SelesParameters paramSelesParameters)
  {
    if (paramSelesParameters == null) {
      return;
    }
    Iterator localIterator = paramSelesParameters.a.iterator();
    while (localIterator.hasNext())
    {
      FilterArg localFilterArg1 = (FilterArg)localIterator.next();
      FilterArg localFilterArg2 = getFilterArg(localFilterArg1.getKey());
      if (localFilterArg2 != null)
      {
        localFilterArg2.setPrecentValue(localFilterArg1.getPrecentValue());
        FilterArg.a(localFilterArg2, false);
      }
    }
  }
  
  public class FilterArg
  {
    private String b;
    private float c;
    private float d;
    private float e;
    private float f;
    private boolean g;
    
    public FilterArg() {}
    
    public void setPrecentValue(float paramFloat)
    {
      if (paramFloat < 0.0F) {
        paramFloat = 0.0F;
      } else if (paramFloat > 1.0F) {
        paramFloat = 1.0F;
      }
      if (getPrecentValue() != paramFloat)
      {
        this.g = true;
        this.c = ((this.f - this.e) * paramFloat + this.e);
      }
    }
    
    public float getPrecentValue()
    {
      return (this.c - this.e) / (this.f - this.e);
    }
    
    public float getValue()
    {
      return this.c;
    }
    
    public void setValue(float paramFloat)
    {
      this.c = paramFloat;
      this.g = true;
    }
    
    public void reset()
    {
      if (this.c != this.d)
      {
        this.c = this.d;
        this.g = true;
      }
    }
    
    public String getKey()
    {
      return this.b;
    }
    
    public boolean equalsKey(String paramString)
    {
      return getKey().equalsIgnoreCase(paramString);
    }
    
    public void setMaxValueFactor(float paramFloat)
    {
      if (paramFloat < 0.0F) {
        paramFloat = 0.0F;
      }
      if (paramFloat > 1.0F) {
        paramFloat = 1.0F;
      }
      float f1 = this.c / (this.f - this.e);
      String str = String.format("%.2f", new Object[] { Float.valueOf((this.f - this.e) * paramFloat) });
      this.f = (this.e + Float.valueOf(str).floatValue());
      this.c = ((this.f - this.e) * f1);
    }
  }
  
  public static abstract interface TileStickerInterface
  {
    public abstract void updateTileStickers(List<TuSdkImage2DSticker> paramList);
  }
  
  public static abstract interface FilterStickerInterface
    extends SelesParameters.FilterFacePositionInterface
  {
    public abstract void updateStickers(List<TuSDKLiveStickerImage> paramList);
    
    public abstract void setDisplayRect(RectF paramRectF, float paramFloat);
    
    public abstract void setEnableAutoplayMode(boolean paramBoolean);
    
    public abstract void seekStickerToFrameTime(long paramLong);
    
    public abstract void setBenchmarkTime(long paramLong);
    
    public abstract void setStickerVisibility(boolean paramBoolean);
  }
  
  public static abstract interface FilterFacePositionInterface
  {
    public abstract void updateFaceFeatures(FaceAligment[] paramArrayOfFaceAligment, float paramFloat);
  }
  
  public static abstract interface FilterTexturesInterface
  {
    public abstract void appendTextures(List<SelesPicture> paramList);
  }
  
  public static abstract interface FilterTexturesInterface2
  {
    public abstract void appendTextures(List<Bitmap> paramList);
  }
  
  public static abstract interface FilterParameterInterface
  {
    public abstract SelesParameters getParameter();
    
    public abstract void setParameter(SelesParameters paramSelesParameters);
    
    public abstract void submitParameter();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\SelesParameters.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */