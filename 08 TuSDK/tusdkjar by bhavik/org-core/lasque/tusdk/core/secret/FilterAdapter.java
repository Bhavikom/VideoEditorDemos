package org.lasque.tusdk.core.secret;

import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.ImageView;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import org.lasque.tusdk.core.TuSdkBundle;
import org.lasque.tusdk.core.TuSdkConfigs;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.network.TuSdkDownloadAdapter;
import org.lasque.tusdk.core.network.TuSdkDownloadItem;
import org.lasque.tusdk.core.network.TuSdkDownloadTask.DownloadTaskType;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.sources.SelesPicture;
import org.lasque.tusdk.core.seles.tusdk.FilterGroup;
import org.lasque.tusdk.core.seles.tusdk.FilterOption;
import org.lasque.tusdk.core.seles.tusdk.filters.TuSDKNormalFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.arts.TuSDKArtBrushFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.base.TuSDKProgramFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.colors.TuSDKColorHDRFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.colors.TuSDKColorLomoFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.colors.TuSDKColorMixCoverFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.colors.TuSDKColorMixedFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.colors.TuSDKColorNoirFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.colors.TuSDKColorSelectiveFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.flowabs.TuSDKTfmFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.flowabs.TuSDKTfmInkFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.lights.TuSDKLightGlareFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.lights.TuSDKSobelEdgeFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveEdgeMagicFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveFancy01Filter;
import org.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveFaultFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveHeartbeatFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveLightningFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveMegrimFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveMirrorImageFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveOldTVFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveRadialBlurFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveScanningLineFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveShakeFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveSignalFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveSloshFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveSoulOutFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.lives.TuSDKLiveXRayFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.skins.TuSDKLiveSkinColor2Filter;
import org.lasque.tusdk.core.seles.tusdk.filters.skins.TuSDKLiveSkinColorFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.skins.TuSDKSkinColor2Filter;
import org.lasque.tusdk.core.seles.tusdk.filters.skins.TuSDKSkinColorFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.skins.TuSDKSkinMoistFilter;
import org.lasque.tusdk.core.seles.tusdk.filters.skins.TuSDKSkinNaturalFilter;
import org.lasque.tusdk.core.seles.tusdk.particle.TuSDKParticleFilter;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.json.JsonHelper;
import org.lasque.tusdk.core.utils.json.JsonWrapper;

public class FilterAdapter
  extends TuSdkDownloadAdapter<FilterThumbTaskImageWare>
{
  public static final String NormalFilterCode = "Normal";
  private ArrayList<FilterGroup> a;
  private Hashtable<String, FilterOption> b;
  private ArrayList<String> c;
  private FiltersConfigDelegate d;
  private boolean e;
  private TuSdkConfigs f;
  
  public FilterAdapter(TuSdkConfigs paramTuSdkConfigs)
  {
    this.f = paramTuSdkConfigs;
    setDownloadTaskType(TuSdkDownloadTask.DownloadTaskType.TypeFilter);
    b();
  }
  
  public List<String> getCodes()
  {
    return verifyCodes(this.c);
  }
  
  public void setInitDelegate(FiltersConfigDelegate paramFiltersConfigDelegate)
  {
    this.d = paramFiltersConfigDelegate;
    a();
  }
  
  private void a()
  {
    if ((this.e) && (this.d != null))
    {
      this.d.onFiltersConfigInited();
      this.d = null;
      TLog.d("FiltersConfig inited: %s", new Object[] { Integer.valueOf(this.c.size()) });
    }
  }
  
  public boolean isInited()
  {
    return this.e;
  }
  
  public List<String> verifyCodes(List<String> paramList)
  {
    if ((!this.e) || (paramList == null) || (this.c == null) || (!SdkValid.shared.sdkValid())) {
      return null;
    }
    ArrayList localArrayList = new ArrayList(paramList.size());
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      if (this.c.contains(str)) {
        localArrayList.add(str);
      }
    }
    return localArrayList;
  }
  
  public List<FilterOption> getFilters(List<String> paramList)
  {
    if ((paramList == null) || (paramList.isEmpty())) {
      return null;
    }
    ArrayList localArrayList = new ArrayList(paramList.size());
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      FilterOption localFilterOption = (FilterOption)this.b.get(str);
      if (localFilterOption != null) {
        localArrayList.add(localFilterOption.copy());
      }
    }
    return localArrayList;
  }
  
  public List<FilterOption> getGroupFilters(FilterGroup paramFilterGroup)
  {
    if (paramFilterGroup == null) {
      return null;
    }
    FilterGroup localFilterGroup = getFilterGroup(paramFilterGroup.groupId);
    if ((localFilterGroup == null) || (localFilterGroup.filters == null) || (localFilterGroup.filters.isEmpty())) {
      return null;
    }
    ArrayList localArrayList = new ArrayList(localFilterGroup.filters.size());
    Iterator localIterator = localFilterGroup.filters.iterator();
    while (localIterator.hasNext())
    {
      FilterOption localFilterOption = (FilterOption)localIterator.next();
      localArrayList.add(localFilterOption.copy());
    }
    return localArrayList;
  }
  
  public String getGroupNameKey(long paramLong)
  {
    FilterGroup localFilterGroup = getFilterGroup(paramLong);
    if (localFilterGroup == null) {
      return null;
    }
    return localFilterGroup.getNameKey();
  }
  
  public int getGroupType(long paramLong)
  {
    FilterGroup localFilterGroup = getFilterGroup(paramLong);
    if (localFilterGroup == null) {
      return 0;
    }
    return localFilterGroup.getCategoryId();
  }
  
  public int getGroupFiltersType(long paramLong)
  {
    FilterGroup localFilterGroup = getFilterGroup(paramLong);
    if (localFilterGroup == null) {
      return 0;
    }
    return localFilterGroup.getGroupFiltersType();
  }
  
  public List<FilterGroup> getGroups()
  {
    ArrayList localArrayList = new ArrayList(this.a.size());
    Iterator localIterator = this.a.iterator();
    while (localIterator.hasNext())
    {
      FilterGroup localFilterGroup = (FilterGroup)localIterator.next();
      if (localFilterGroup.categoryId <= 0) {
        localArrayList.add(localFilterGroup.copy());
      }
    }
    return localArrayList;
  }
  
  public List<FilterGroup> getGroupsByAtionScen(int paramInt)
  {
    ArrayList localArrayList = new ArrayList(this.a.size());
    List localList = getGroups();
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      FilterGroup localFilterGroup = (FilterGroup)localIterator.next();
      if (localFilterGroup.canUseForAtionScenType(paramInt)) {
        localArrayList.add(localFilterGroup.copy());
      }
    }
    return localArrayList;
  }
  
  public String getGroupDefaultFilterCode(FilterGroup paramFilterGroup)
  {
    if (paramFilterGroup == null) {
      return null;
    }
    FilterGroup localFilterGroup = getFilterGroup(paramFilterGroup.groupId);
    if (localFilterGroup == null) {
      return null;
    }
    FilterOption localFilterOption = localFilterGroup.getDefaultFilter();
    if (localFilterOption == null) {
      return null;
    }
    return localFilterOption.code;
  }
  
  private void b()
  {
    this.a = new ArrayList();
    this.b = new Hashtable();
    this.c = new ArrayList();
    a("Normal", 0, new String[0]);
    tryLoadTaskDataWithCache();
    new Thread(new Runnable()
    {
      public void run()
      {
        FilterAdapter.a(FilterAdapter.this);
      }
    }).start();
  }
  
  private void c()
  {
    String str = TuSdkBundle.sdkBundleTexture("lsq_internal_filters.filter");
    SdkValid.shared.loadFilterConfig(str);
    if ((this.f != null) && (this.f.filterGroups != null))
    {
      ArrayList localArrayList = new ArrayList(this.f.filterGroups);
      Iterator localIterator = localArrayList.iterator();
      while (localIterator.hasNext())
      {
        FilterGroup localFilterGroup = (FilterGroup)localIterator.next();
        a(localFilterGroup);
      }
    }
    asyncLoadDownloadDatas();
    new Handler(Looper.getMainLooper()).post(new Runnable()
    {
      public void run()
      {
        FilterAdapter.a(FilterAdapter.this, true);
        FilterAdapter.b(FilterAdapter.this);
      }
    });
  }
  
  private void a(FilterGroup paramFilterGroup)
  {
    if ((paramFilterGroup == null) || (paramFilterGroup.file == null)) {
      return;
    }
    String str1 = TuSdkBundle.sdkBundleTexture(paramFilterGroup.file);
    String str2 = SdkValid.shared.loadFilterGroup(str1, null);
    if (str2 == null) {
      return;
    }
    FilterGroup localFilterGroup = (FilterGroup)JsonWrapper.deserialize(str2, FilterGroup.class);
    if ((localFilterGroup == null) || (containsGroupId(localFilterGroup.groupId))) {
      return;
    }
    if ((localFilterGroup.filters == null) || (localFilterGroup.filters.isEmpty())) {
      return;
    }
    if ((paramFilterGroup.filters == null) || (paramFilterGroup.filters.isEmpty())) {
      b(localFilterGroup);
    } else {
      a(paramFilterGroup, localFilterGroup);
    }
    if (paramFilterGroup.name != null) {
      localFilterGroup.name = paramFilterGroup.name;
    }
    if (paramFilterGroup.thumb != null) {
      localFilterGroup.thumb = paramFilterGroup.thumb;
    }
    if (paramFilterGroup.color != null) {
      localFilterGroup.color = paramFilterGroup.color;
    }
    localFilterGroup.groupFiltersType = paramFilterGroup.groupFiltersType;
    if ((paramFilterGroup.defaultFilterId > 0L) && (localFilterGroup.getFilterOption(paramFilterGroup.defaultFilterId) != null)) {
      localFilterGroup.defaultFilterId = paramFilterGroup.defaultFilterId;
    }
    if ((localFilterGroup.filters != null) && (!localFilterGroup.filters.isEmpty())) {
      this.a.add(localFilterGroup);
    }
  }
  
  private boolean b(FilterGroup paramFilterGroup)
  {
    ArrayList localArrayList = new ArrayList(paramFilterGroup.filters.size());
    Iterator localIterator = paramFilterGroup.filters.iterator();
    while (localIterator.hasNext())
    {
      FilterOption localFilterOption = (FilterOption)localIterator.next();
      localFilterOption.disableRuntime = paramFilterGroup.disableRuntime;
      if (a(localFilterOption)) {
        localArrayList.add(localFilterOption);
      }
    }
    paramFilterGroup.filters = localArrayList;
    return !localArrayList.isEmpty();
  }
  
  private boolean a(FilterOption paramFilterOption)
  {
    if (paramFilterOption.version > 11) {
      return false;
    }
    if (!this.c.contains(paramFilterOption.code))
    {
      this.c.add(paramFilterOption.code);
      this.b.put(paramFilterOption.code, paramFilterOption);
    }
    return true;
  }
  
  private void a(FilterGroup paramFilterGroup1, FilterGroup paramFilterGroup2)
  {
    ArrayList localArrayList = new ArrayList(paramFilterGroup1.filters.size());
    Iterator localIterator = paramFilterGroup1.filters.iterator();
    while (localIterator.hasNext())
    {
      FilterOption localFilterOption1 = (FilterOption)localIterator.next();
      FilterOption localFilterOption2 = paramFilterGroup2.getFilterOption(localFilterOption1.id);
      if ((localFilterOption2 != null) && (localFilterOption2.version <= 11))
      {
        if (localFilterOption1.name != null) {
          localFilterOption2.name = localFilterOption1.name;
        }
        if (localFilterOption1.thumb != null) {
          localFilterOption2.thumb = localFilterOption1.thumb;
        }
        if (localFilterOption1.color != null) {
          localFilterOption2.color = localFilterOption1.color;
        }
        paramFilterGroup2.groupFiltersType = paramFilterGroup1.groupFiltersType;
        localFilterOption2.disableRuntime = paramFilterGroup2.disableRuntime;
        if (localFilterOption1.args != null) {
          a(localFilterOption1, localFilterOption2);
        }
        if (a(localFilterOption2)) {
          localArrayList.add(localFilterOption2);
        }
      }
    }
    paramFilterGroup2.filters = localArrayList;
  }
  
  private void a(FilterOption paramFilterOption1, FilterOption paramFilterOption2)
  {
    if (paramFilterOption2.args == null)
    {
      paramFilterOption2.args = paramFilterOption1.args;
      return;
    }
    Iterator localIterator = paramFilterOption1.args.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      paramFilterOption2.args.put(localEntry.getKey(), localEntry.getValue());
    }
  }
  
  private FilterOption d()
  {
    return (FilterOption)this.b.get("Normal");
  }
  
  public FilterOption option(String paramString)
  {
    FilterOption localFilterOption = a(paramString);
    if (localFilterOption != null) {
      return localFilterOption.copy();
    }
    return d().copy();
  }
  
  private FilterOption a(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    FilterOption localFilterOption = (FilterOption)this.b.get(paramString);
    return localFilterOption;
  }
  
  public List<SelesPicture> loadTextures(String paramString)
  {
    FilterOption localFilterOption = a(paramString);
    if ((localFilterOption == null) || (localFilterOption.textures == null)) {
      return null;
    }
    List localList = null;
    if (localFilterOption.encryptType == 0) {
      localList = b(localFilterOption);
    } else {
      localList = SdkValid.shared.readTextures(localFilterOption.groupId, localFilterOption.textures);
    }
    return localList;
  }
  
  private List<SelesPicture> b(FilterOption paramFilterOption)
  {
    if ((paramFilterOption == null) || (paramFilterOption.textures == null)) {
      return null;
    }
    FilterGroup localFilterGroup = getFilterGroup(paramFilterOption.groupId);
    if (localFilterGroup == null) {
      return null;
    }
    ArrayList localArrayList = new ArrayList(paramFilterOption.textures.size());
    Iterator localIterator = paramFilterOption.textures.iterator();
    while (localIterator.hasNext())
    {
      String str1 = (String)localIterator.next();
      String str2 = TuSdkBundle.sdkBundleTexture(str1);
      Bitmap localBitmap = TuSdkContext.getAssetsBitmap(str2);
      if (localBitmap != null)
      {
        SelesPicture localSelesPicture = new SelesPicture(localBitmap, false, true);
        localArrayList.add(localSelesPicture);
      }
    }
    return localArrayList;
  }
  
  public List<SelesPicture> loadInternalTextures(List<String> paramList)
  {
    return SdkValid.shared.readInternalTextures(paramList);
  }
  
  public FilterGroup getFilterGroup(long paramLong)
  {
    Iterator localIterator = this.a.iterator();
    while (localIterator.hasNext())
    {
      FilterGroup localFilterGroup = (FilterGroup)localIterator.next();
      if (localFilterGroup.groupId == paramLong) {
        return localFilterGroup;
      }
    }
    return null;
  }
  
  public SelesOutInput createFilter(FilterOption paramFilterOption)
  {
    if (paramFilterOption == null) {
      return null;
    }
    paramFilterOption = a(paramFilterOption.code);
    if (paramFilterOption == null) {
      return null;
    }
    switch (paramFilterOption.filterType)
    {
    case 0: 
      return new TuSDKNormalFilter();
    case 16: 
      return new TuSDKColorMixedFilter(paramFilterOption);
    case 32: 
      return new TuSDKColorLomoFilter(paramFilterOption);
    case 48: 
      return new TuSDKColorMixCoverFilter(paramFilterOption);
    case 49: 
      return new TuSDKColorHDRFilter();
    case 64: 
      return new TuSDKLightGlareFilter(paramFilterOption);
    case 65: 
      return new TuSDKSobelEdgeFilter(paramFilterOption);
    case 66: 
      return new TuSDKTfmFilter();
    case 67: 
      return new TuSDKTfmInkFilter();
    case 80: 
      return new TuSDKArtBrushFilter(paramFilterOption);
    case 96: 
      return new TuSDKSkinColor2Filter(paramFilterOption);
    case 97: 
      return new TuSDKSkinColorFilter(paramFilterOption);
    case 98: 
      return new TuSDKColorSelectiveFilter(paramFilterOption);
    case 99: 
      return new TuSDKLiveSkinColorFilter(paramFilterOption);
    case 100: 
      return new TuSDKLiveSkinColor2Filter(paramFilterOption);
    case 101: 
      return new TuSDKSkinMoistFilter();
    case 102: 
      return new TuSDKSkinNaturalFilter();
    case 112: 
      return c(paramFilterOption);
    case 113: 
      return new TuSDKLiveShakeFilter(paramFilterOption);
    case 114: 
      return new TuSDKLiveEdgeMagicFilter(paramFilterOption);
    case 115: 
      return new TuSDKLiveSoulOutFilter(paramFilterOption);
    case 116: 
      return new TuSDKLiveMegrimFilter(paramFilterOption);
    case 117: 
      return new TuSDKLiveFancy01Filter(paramFilterOption);
    case 118: 
      return new TuSDKLiveSignalFilter(paramFilterOption);
    case 119: 
      return new TuSDKLiveLightningFilter(paramFilterOption);
    case 120: 
      return new TuSDKLiveXRayFilter(paramFilterOption);
    case 121: 
      return new TuSDKLiveHeartbeatFilter(paramFilterOption);
    case 128: 
      return new TuSDKLiveMirrorImageFilter();
    case 129: 
      return new TuSDKLiveSloshFilter(paramFilterOption);
    case 130: 
      return new TuSDKLiveOldTVFilter(paramFilterOption);
    case 131: 
      return new TuSDKLiveRadialBlurFilter(paramFilterOption);
    case 132: 
      return new TuSDKLiveFaultFilter();
    case 133: 
      return new TuSDKLiveScanningLineFilter();
    case 240: 
      return new TuSDKColorNoirFilter();
    case 241: 
      if ((paramFilterOption.argList != null) && (!paramFilterOption.argList.isEmpty())) {
        return new TuSDKParticleFilter(JsonHelper.json(paramFilterOption.argList));
      }
      return null;
    }
    return null;
  }
  
  private SelesOutInput c(FilterOption paramFilterOption)
  {
    if ((StringHelper.isNotBlank(paramFilterOption.vertex)) && (StringHelper.isNotBlank(paramFilterOption.fragment))) {
      return new TuSDKProgramFilter(paramFilterOption.vertex, paramFilterOption.fragment);
    }
    if (StringHelper.isNotBlank(paramFilterOption.fragment)) {
      return new TuSDKProgramFilter(paramFilterOption.fragment);
    }
    return null;
  }
  
  private void a(String paramString, int paramInt, String... paramVarArgs)
  {
    a(paramString, paramInt, false, paramVarArgs);
  }
  
  private void a(String paramString, int paramInt, boolean paramBoolean, String... paramVarArgs)
  {
    a(paramString, paramInt, paramBoolean, false, paramVarArgs);
  }
  
  private void a(String paramString, int paramInt, boolean paramBoolean1, boolean paramBoolean2, String... paramVarArgs)
  {
    FilterOption localFilterOption = new FilterOption();
    localFilterOption.id = 0L;
    localFilterOption.code = paramString;
    localFilterOption.name = String.format("lsq_filter_%s", new Object[] { paramString });
    localFilterOption.filterType = paramInt;
    if (paramVarArgs != null) {
      localFilterOption.textures = new ArrayList(Arrays.asList(paramVarArgs));
    }
    localFilterOption.texturesKeepInput = paramBoolean1;
    localFilterOption.canDefinition = paramBoolean2;
    localFilterOption.encryptType = 1;
    localFilterOption.isInternal = true;
    this.c.add(paramString);
    this.b.put(paramString, localFilterOption);
  }
  
  public void loadGroupThumb(ImageView paramImageView, FilterGroup paramFilterGroup)
  {
    if ((paramImageView == null) || (paramFilterGroup == null)) {
      return;
    }
    FilterGroup localFilterGroup = getFilterGroup(paramFilterGroup.groupId);
    if ((localFilterGroup == null) || (a(paramImageView, localFilterGroup.thumb))) {
      return;
    }
    FilterThumbTaskImageWare localFilterThumbTaskImageWare = new FilterThumbTaskImageWare(paramImageView, FilterThumbTaskImageWare.FilterThumbTaskType.TypeGroupThumb, null, localFilterGroup);
    loadImage(localFilterThumbTaskImageWare);
  }
  
  public void loadGroupDefaultFilterThumb(ImageView paramImageView, FilterGroup paramFilterGroup)
  {
    if ((paramImageView == null) || (paramFilterGroup == null)) {
      return;
    }
    FilterGroup localFilterGroup = getFilterGroup(paramFilterGroup.groupId);
    if (localFilterGroup == null) {
      return;
    }
    FilterOption localFilterOption = localFilterGroup.getDefaultFilter();
    loadFilterThumb(paramImageView, localFilterOption);
  }
  
  public void loadFilterThumb(ImageView paramImageView, FilterOption paramFilterOption)
  {
    if ((paramImageView == null) || (paramFilterOption == null)) {
      return;
    }
    FilterOption localFilterOption = a(paramFilterOption.code);
    if ((localFilterOption == null) || (a(paramImageView, localFilterOption.thumb))) {
      return;
    }
    FilterThumbTaskImageWare localFilterThumbTaskImageWare = new FilterThumbTaskImageWare(paramImageView, FilterThumbTaskImageWare.FilterThumbTaskType.TypeFilterThumb, localFilterOption, null);
    loadImage(localFilterThumbTaskImageWare);
  }
  
  private boolean a(ImageView paramImageView, String paramString)
  {
    if (paramString == null) {
      return false;
    }
    int i = TuSdkContext.getDrawableResId(paramString);
    if (i != 0)
    {
      paramImageView.setImageResource(i);
      return true;
    }
    return false;
  }
  
  protected String getCacheKey(FilterThumbTaskImageWare paramFilterThumbTaskImageWare)
  {
    String str = null;
    switch (3.a[paramFilterThumbTaskImageWare.taskType.ordinal()])
    {
    case 1: 
      if (paramFilterThumbTaskImageWare.group != null) {
        str = TextUtils.isEmpty(paramFilterThumbTaskImageWare.group.thumbKey) ? paramFilterThumbTaskImageWare.group.code : paramFilterThumbTaskImageWare.group.thumbKey;
      }
      break;
    case 2: 
      if (paramFilterThumbTaskImageWare.option != null) {
        str = TextUtils.isEmpty(paramFilterThumbTaskImageWare.option.thumbKey) ? paramFilterThumbTaskImageWare.option.code : paramFilterThumbTaskImageWare.option.thumbKey;
      }
      break;
    }
    return str;
  }
  
  protected Bitmap asyncTaskLoadImage(FilterThumbTaskImageWare paramFilterThumbTaskImageWare)
  {
    Bitmap localBitmap = null;
    FilterGroup localFilterGroup;
    if ((paramFilterThumbTaskImageWare.taskType == FilterThumbTaskImageWare.FilterThumbTaskType.TypeGroupThumb) && (paramFilterThumbTaskImageWare.group != null))
    {
      localBitmap = b(paramFilterThumbTaskImageWare.group.thumb);
      if (localBitmap != null) {
        return localBitmap;
      }
      localFilterGroup = getFilterGroup(paramFilterThumbTaskImageWare.group.groupId);
      if (localFilterGroup != null) {
        localBitmap = SdkValid.shared.readFilterThumb(localFilterGroup.groupId, 0L);
      }
    }
    else if ((paramFilterThumbTaskImageWare.taskType == FilterThumbTaskImageWare.FilterThumbTaskType.TypeFilterThumb) && (paramFilterThumbTaskImageWare.option != null))
    {
      localBitmap = b(paramFilterThumbTaskImageWare.option.thumb);
      if (localBitmap != null) {
        return localBitmap;
      }
      localFilterGroup = getFilterGroup(paramFilterThumbTaskImageWare.option.groupId);
      if (localFilterGroup != null) {
        localBitmap = SdkValid.shared.readFilterThumb(localFilterGroup.groupId, paramFilterThumbTaskImageWare.option.id);
      }
    }
    return localBitmap;
  }
  
  private Bitmap b(String paramString)
  {
    if (StringHelper.isEmpty(paramString)) {
      return null;
    }
    String str = TuSdkBundle.sdkBundleTexture(paramString);
    return TuSdkContext.getAssetsBitmap(str);
  }
  
  public boolean containsGroupId(long paramLong)
  {
    return getFilterGroup(paramLong) != null;
  }
  
  protected void removeDownloadData(long paramLong)
  {
    FilterGroup localFilterGroup = getFilterGroup(paramLong);
    if (localFilterGroup == null) {
      return;
    }
    if (localFilterGroup.filters != null)
    {
      Iterator localIterator = localFilterGroup.filters.iterator();
      while (localIterator.hasNext())
      {
        FilterOption localFilterOption = (FilterOption)localIterator.next();
        this.b.remove(localFilterOption.code);
        this.c.remove(localFilterOption.code);
      }
    }
    this.a.remove(localFilterGroup);
    SdkValid.shared.removeFilterGroup(localFilterGroup.groupId);
    TLog.d("remove download filter [%s]: %s | %s", new Object[] { Long.valueOf(paramLong), Integer.valueOf(this.c.size()), Integer.valueOf(this.a.size()) });
  }
  
  protected boolean appendDownload(TuSdkDownloadItem paramTuSdkDownloadItem)
  {
    if (!super.appendDownload(paramTuSdkDownloadItem)) {
      return false;
    }
    String str = SdkValid.shared.loadFilterGroup(paramTuSdkDownloadItem.localDownloadPath().getAbsolutePath(), paramTuSdkDownloadItem.key);
    if (str == null) {
      return false;
    }
    FilterGroup localFilterGroup = (FilterGroup)JsonWrapper.deserialize(str, FilterGroup.class);
    if (localFilterGroup == null) {
      return false;
    }
    if ((localFilterGroup.filters == null) || (localFilterGroup.filters.isEmpty())) {
      return false;
    }
    localFilterGroup.isDownload = true;
    if (b(localFilterGroup)) {
      this.a.add(localFilterGroup);
    }
    return true;
  }
  
  protected Collection<?> getAllGroupID()
  {
    ArrayList localArrayList = new ArrayList(this.a.size());
    Iterator localIterator = this.a.iterator();
    while (localIterator.hasNext())
    {
      FilterGroup localFilterGroup = (FilterGroup)localIterator.next();
      localArrayList.add(Long.valueOf(localFilterGroup.groupId));
    }
    return localArrayList;
  }
  
  public static abstract interface FiltersConfigDelegate
  {
    public abstract void onFiltersConfigInited();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\secret\FilterAdapter.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */