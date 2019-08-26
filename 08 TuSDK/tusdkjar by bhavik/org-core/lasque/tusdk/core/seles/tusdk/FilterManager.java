package org.lasque.tusdk.core.seles.tusdk;

import android.graphics.Bitmap;
import java.util.List;
import org.lasque.tusdk.core.TuSdkConfigs;
import org.lasque.tusdk.core.secret.FilterAdapter.FiltersConfigDelegate;
import org.lasque.tusdk.core.secret.SdkValid;
import org.lasque.tusdk.core.seles.SelesParameters;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class FilterManager
  implements FilterAdapter.FiltersConfigDelegate
{
  private static FilterManager a;
  private FilterLocalPackage b;
  private FilterManagerDelegate c;
  
  public static FilterManager shared()
  {
    return a;
  }
  
  public static FilterManager init(TuSdkConfigs paramTuSdkConfigs)
  {
    if ((a == null) && (paramTuSdkConfigs != null)) {
      a = new FilterManager(paramTuSdkConfigs);
    }
    return a;
  }
  
  public List<String> getFilterNames()
  {
    return this.b.getCodes();
  }
  
  public boolean isInited()
  {
    return this.b.isInited();
  }
  
  private FilterManager(TuSdkConfigs paramTuSdkConfigs)
  {
    this.b = FilterLocalPackage.init(paramTuSdkConfigs);
    this.b.setInitDelegate(this);
  }
  
  public void checkFilterManager(FilterManagerDelegate paramFilterManagerDelegate)
  {
    this.c = paramFilterManagerDelegate;
    a();
  }
  
  private void a()
  {
    if ((this.b.isInited()) && (this.c != null))
    {
      this.c.onFilterManagerInited(this);
      this.c = null;
    }
  }
  
  public boolean isNormalFilter(String paramString)
  {
    return (paramString == null) || (StringHelper.isEmpty(paramString)) || (paramString.equals("Normal"));
  }
  
  public boolean isFilterEffect(String paramString)
  {
    if (isNormalFilter(paramString)) {
      return true;
    }
    FilterOption localFilterOption = this.b.option(paramString);
    return this.b.getGroupFiltersType(localFilterOption.groupId) == 0;
  }
  
  public boolean isSceneEffectFilter(String paramString)
  {
    if (paramString == null) {
      return false;
    }
    FilterOption localFilterOption = this.b.option(paramString);
    return this.b.getGroupFiltersType(localFilterOption.groupId) == 1;
  }
  
  public boolean isParticleEffectFilter(String paramString)
  {
    if (paramString == null) {
      return false;
    }
    FilterOption localFilterOption = this.b.option(paramString);
    return this.b.getGroupFiltersType(localFilterOption.groupId) == 2;
  }
  
  public boolean isConmicEffectFilter(String paramString)
  {
    if (paramString == null) {
      return false;
    }
    FilterOption localFilterOption = this.b.option(paramString);
    return this.b.getGroupFiltersType(localFilterOption.groupId) == 3;
  }
  
  public int getGroupTypeByCode(String paramString)
  {
    FilterOption localFilterOption = this.b.option(paramString);
    return this.b.getGroupType(localFilterOption.groupId);
  }
  
  public void onFiltersConfigInited()
  {
    a();
  }
  
  public Bitmap process(Bitmap paramBitmap, String paramString)
  {
    return process(paramBitmap, paramString, ImageOrientation.Up);
  }
  
  public Bitmap process(Bitmap paramBitmap, String paramString, float paramFloat)
  {
    return process(paramBitmap, paramString, ImageOrientation.Up, paramFloat);
  }
  
  public Bitmap process(Bitmap paramBitmap, String paramString, SelesParameters paramSelesParameters, float paramFloat)
  {
    return process(paramBitmap, paramString, paramSelesParameters, ImageOrientation.Up, paramFloat);
  }
  
  public Bitmap process(Bitmap paramBitmap, String paramString, ImageOrientation paramImageOrientation)
  {
    return process(paramBitmap, paramString, paramImageOrientation, 0.0F);
  }
  
  public Bitmap process(Bitmap paramBitmap, String paramString, ImageOrientation paramImageOrientation, float paramFloat)
  {
    return process(paramBitmap, paramString, null, paramImageOrientation, paramFloat);
  }
  
  public Bitmap process(Bitmap paramBitmap, String paramString, SelesParameters paramSelesParameters, ImageOrientation paramImageOrientation, float paramFloat)
  {
    if (!SdkValid.shared.filterAPIEnabled())
    {
      TLog.e("You are not allowed to use the filterAPI feature, please see http://tusdk.com", new Object[0]);
      return null;
    }
    if (isNormalFilter(paramString)) {
      return paramBitmap;
    }
    FilterWrap localFilterWrap = SdkValid.shared.getFilterWrapWithCode(paramString);
    if (localFilterWrap == null)
    {
      TLog.e("You are not allowed to use the filter [%s] in Filter API, please see http://tusdk.com", new Object[] { paramString });
      return null;
    }
    localFilterWrap.setFilterParameter(paramSelesParameters);
    return localFilterWrap.process(paramBitmap, paramImageOrientation, paramFloat);
  }
  
  public static abstract interface FilterManagerDelegate
  {
    public abstract void onFilterManagerInited(FilterManager paramFilterManager);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\FilterManager.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */