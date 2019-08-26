package org.lasque.tusdk.core.seles.tusdk;

import android.widget.ImageView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONObject;
import org.lasque.tusdk.core.TuSdkConfigs;
import org.lasque.tusdk.core.network.TuSdkDownloadItem;
import org.lasque.tusdk.core.network.TuSdkDownloadManger;
import org.lasque.tusdk.core.network.TuSdkDownloadManger.TuSdkDownloadMangerDelegate;
import org.lasque.tusdk.core.secret.FilterAdapter;
import org.lasque.tusdk.core.secret.FilterAdapter.FiltersConfigDelegate;
import org.lasque.tusdk.core.secret.StatisticsManger;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.sources.SelesPicture;
import org.lasque.tusdk.core.type.DownloadTaskStatus;

public class FilterLocalPackage
  implements TuSdkDownloadManger.TuSdkDownloadMangerDelegate
{
  public static final String NormalFilterCode = "Normal";
  private static FilterLocalPackage a;
  private FilterAdapter b;
  private List<FilterLocalPackageDelegate> c = new ArrayList();
  
  public static FilterLocalPackage shared()
  {
    return a;
  }
  
  public static FilterLocalPackage init(TuSdkConfigs paramTuSdkConfigs)
  {
    if ((a == null) && (paramTuSdkConfigs != null)) {
      a = new FilterLocalPackage(paramTuSdkConfigs);
    }
    return a;
  }
  
  public void appenDelegate(FilterLocalPackageDelegate paramFilterLocalPackageDelegate)
  {
    if ((paramFilterLocalPackageDelegate == null) || (this.c.contains(paramFilterLocalPackageDelegate))) {
      return;
    }
    this.c.add(paramFilterLocalPackageDelegate);
  }
  
  public void removeDelegate(FilterLocalPackageDelegate paramFilterLocalPackageDelegate)
  {
    if (paramFilterLocalPackageDelegate == null) {
      return;
    }
    this.c.remove(paramFilterLocalPackageDelegate);
  }
  
  private FilterLocalPackage(TuSdkConfigs paramTuSdkConfigs)
  {
    this.b = new FilterAdapter(paramTuSdkConfigs);
    this.b.setDownloadDelegate(this);
  }
  
  public List<String> getCodes()
  {
    return this.b.getCodes();
  }
  
  public void setInitDelegate(FilterAdapter.FiltersConfigDelegate paramFiltersConfigDelegate)
  {
    this.b.setInitDelegate(paramFiltersConfigDelegate);
  }
  
  public boolean isInited()
  {
    return this.b.isInited();
  }
  
  public List<String> verifyCodes(List<String> paramList)
  {
    return this.b.verifyCodes(paramList);
  }
  
  public List<FilterOption> getFilters(List<String> paramList)
  {
    return this.b.getFilters(paramList);
  }
  
  public List<FilterOption> getGroupFilters(FilterGroup paramFilterGroup)
  {
    return this.b.getGroupFilters(paramFilterGroup);
  }
  
  public FilterGroup getFilterGroup(long paramLong)
  {
    return this.b.getFilterGroup(paramLong);
  }
  
  public String getGroupNameKey(long paramLong)
  {
    return this.b.getGroupNameKey(paramLong);
  }
  
  public int getGroupType(long paramLong)
  {
    return this.b.getGroupType(paramLong);
  }
  
  public int getGroupFiltersType(long paramLong)
  {
    return this.b.getGroupFiltersType(paramLong);
  }
  
  public List<FilterGroup> getGroups()
  {
    return this.b.getGroups();
  }
  
  public List<FilterGroup> getGroupsByAtionScen(int paramInt)
  {
    return this.b.getGroupsByAtionScen(paramInt);
  }
  
  public String getGroupDefaultFilterCode(FilterGroup paramFilterGroup)
  {
    return this.b.getGroupDefaultFilterCode(paramFilterGroup);
  }
  
  public FilterOption option(String paramString)
  {
    return this.b.option(paramString);
  }
  
  public FilterWrap getFilterWrap(String paramString)
  {
    FilterOption localFilterOption = option(paramString);
    FilterWrap localFilterWrap = FilterWrap.creat(localFilterOption);
    StatisticsManger.appendFilter(localFilterOption);
    return localFilterWrap;
  }
  
  public List<SelesPicture> loadTextures(String paramString)
  {
    return this.b.loadTextures(paramString);
  }
  
  public List<SelesPicture> loadInternalTextures(List<String> paramList)
  {
    return this.b.loadInternalTextures(paramList);
  }
  
  public SelesOutInput createFilter(FilterOption paramFilterOption)
  {
    return this.b.createFilter(paramFilterOption);
  }
  
  public void loadGroupThumb(ImageView paramImageView, FilterGroup paramFilterGroup)
  {
    this.b.loadGroupThumb(paramImageView, paramFilterGroup);
  }
  
  public void loadGroupDefaultFilterThumb(ImageView paramImageView, FilterGroup paramFilterGroup)
  {
    this.b.loadGroupDefaultFilterThumb(paramImageView, paramFilterGroup);
  }
  
  public void loadFilterThumb(ImageView paramImageView, FilterOption paramFilterOption)
  {
    this.b.loadFilterThumb(paramImageView, paramFilterOption);
  }
  
  public void cancelLoadImage(ImageView paramImageView)
  {
    this.b.cancelLoadImage(paramImageView);
  }
  
  public void download(long paramLong, String paramString1, String paramString2)
  {
    this.b.download(paramLong, paramString1, paramString2);
  }
  
  public void cancelDownload(long paramLong)
  {
    this.b.cancelDownload(paramLong);
  }
  
  public void removeDownloadWithIdt(long paramLong)
  {
    this.b.removeDownloadWithIdt(paramLong);
  }
  
  public JSONObject getAllDatas()
  {
    return this.b.getAllDatas();
  }
  
  public void onDownloadMangerStatusChanged(TuSdkDownloadManger paramTuSdkDownloadManger, TuSdkDownloadItem paramTuSdkDownloadItem, DownloadTaskStatus paramDownloadTaskStatus)
  {
    ArrayList localArrayList = new ArrayList(this.c);
    Iterator localIterator = localArrayList.iterator();
    while (localIterator.hasNext())
    {
      FilterLocalPackageDelegate localFilterLocalPackageDelegate = (FilterLocalPackageDelegate)localIterator.next();
      localFilterLocalPackageDelegate.onFilterPackageStatusChanged(this, paramTuSdkDownloadItem, paramDownloadTaskStatus);
    }
  }
  
  public static abstract interface FilterLocalPackageDelegate
  {
    public abstract void onFilterPackageStatusChanged(FilterLocalPackage paramFilterLocalPackage, TuSdkDownloadItem paramTuSdkDownloadItem, DownloadTaskStatus paramDownloadTaskStatus);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\FilterLocalPackage.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */