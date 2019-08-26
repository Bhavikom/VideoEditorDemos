package org.lasque.tusdk.core.seles.tusdk;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.utils.json.DataBase;
import org.lasque.tusdk.core.utils.json.JsonBaseBean;
import org.lasque.tusdk.core.utils.json.JsonHelper;

public class FilterGroup
  extends JsonBaseBean
  implements Serializable
{
  public static final int GROUP_FILTERS_TYPE_GENERAL = 0;
  public static final int GROUP_FILTERS_TYPE_SCENE_EFFECT = 1;
  public static final int GROUP_FILTERS_TYPE_PARTICLE_EFFECT = 2;
  public static final int GROUP_FILTERS_TYPE_CONMIC_EFFECT = 3;
  public static final int ATION_SCEN_IMAGE_SDK = 1;
  public static final int ATION_SCEN_LIVE_SDK = 2;
  public static final int ATION_SCEN_SHORT_VIDEO_SDK = 4;
  @DataBase("id")
  public long groupId;
  @DataBase("category_id")
  public int categoryId;
  @DataBase("type")
  public int groupFiltersType;
  @DataBase("ation_scen")
  public int ation_scen;
  @DataBase("file")
  public String file;
  @DataBase("valid_type")
  public int validType;
  @DataBase("valid_key")
  public String validKey;
  @DataBase("code")
  public String code;
  @DataBase("name")
  public String name;
  @DataBase("thumb")
  public String thumb;
  @DataBase("thumb_key")
  public String thumbKey;
  @DataBase("default_filter_id")
  public long defaultFilterId;
  @DataBase("filters")
  public ArrayList<FilterOption> filters;
  @DataBase("color")
  public String color;
  @DataBase("un_real_time")
  public boolean disableRuntime;
  public boolean isDownload;
  
  public FilterGroup() {}
  
  public FilterGroup(JSONObject paramJSONObject)
  {
    deserialize(paramJSONObject);
  }
  
  public static FilterGroup create(long paramLong1, int paramInt, String paramString1, long paramLong2, String paramString2)
  {
    FilterGroup localFilterGroup = new FilterGroup();
    localFilterGroup.groupId = paramLong1;
    localFilterGroup.validType = paramInt;
    localFilterGroup.code = paramString1;
    localFilterGroup.defaultFilterId = paramLong2;
    localFilterGroup.color = paramString2;
    return localFilterGroup;
  }
  
  public FilterGroup copy()
  {
    FilterGroup localFilterGroup = new FilterGroup();
    localFilterGroup.groupId = this.groupId;
    localFilterGroup.categoryId = this.categoryId;
    localFilterGroup.groupFiltersType = this.groupFiltersType;
    localFilterGroup.ation_scen = this.ation_scen;
    localFilterGroup.code = this.code;
    localFilterGroup.name = this.name;
    localFilterGroup.thumb = this.thumb;
    localFilterGroup.defaultFilterId = this.defaultFilterId;
    localFilterGroup.color = this.color;
    localFilterGroup.disableRuntime = this.disableRuntime;
    localFilterGroup.isDownload = this.isDownload;
    return localFilterGroup;
  }
  
  public FilterOption getFilterOption(long paramLong)
  {
    if (this.filters == null) {
      return null;
    }
    Iterator localIterator = this.filters.iterator();
    while (localIterator.hasNext())
    {
      FilterOption localFilterOption = (FilterOption)localIterator.next();
      if (localFilterOption.id == paramLong) {
        return localFilterOption;
      }
    }
    return null;
  }
  
  public FilterOption getDefaultFilter()
  {
    return getFilterOption(this.defaultFilterId);
  }
  
  public String getName()
  {
    int i = TuSdkContext.getStringResId(this.name);
    if (i <= 0) {
      return getNameKey();
    }
    return TuSdkContext.getString(this.name);
  }
  
  public String getNameKey()
  {
    Locale localLocale = Locale.getDefault();
    String str = localLocale.getLanguage();
    if ((this.name != null) && (str != null) && (str.endsWith("zh"))) {
      return this.name;
    }
    if (this.code != null) {
      return this.code;
    }
    return this.name;
  }
  
  public int getCategoryId()
  {
    if (this.categoryId > 0) {
      return this.categoryId;
    }
    return 0;
  }
  
  public int getGroupFiltersType()
  {
    return this.groupFiltersType;
  }
  
  public String getDefaultFilterThumbKey()
  {
    FilterOption localFilterOption = getDefaultFilter();
    if (localFilterOption == null) {
      return null;
    }
    return localFilterOption.thumbKey;
  }
  
  public boolean canUseForAtionScenType(int paramInt)
  {
    if (this.ation_scen == paramInt) {
      return true;
    }
    return (this.ation_scen == 5) && ((paramInt == 1) || (paramInt == 4));
  }
  
  public void deserialize(JSONObject paramJSONObject)
  {
    if (paramJSONObject == null) {
      return;
    }
    this.groupId = paramJSONObject.optLong("id", 0L);
    this.file = paramJSONObject.optString("file");
    this.categoryId = paramJSONObject.optInt("category_id", 0);
    this.groupFiltersType = paramJSONObject.optInt("type", 0);
    this.ation_scen = paramJSONObject.optInt("ation_scen", 0);
    this.validType = paramJSONObject.optInt("valid_type", 0);
    this.validKey = paramJSONObject.optString("valid_key");
    this.code = paramJSONObject.optString("code");
    this.name = paramJSONObject.optString("name");
    this.thumb = paramJSONObject.optString("thumb");
    this.thumbKey = paramJSONObject.optString("thumb_key");
    this.color = paramJSONObject.optString("color");
    this.disableRuntime = (paramJSONObject.optInt("un_real_time", 0) != 0);
    JSONArray localJSONArray = JsonHelper.getJSONArray(paramJSONObject, "filters");
    if ((localJSONArray != null) && (localJSONArray.length() > 0))
    {
      this.filters = new ArrayList(localJSONArray.length());
      int i = 0;
      int j = localJSONArray.length();
      while (i < j)
      {
        JSONObject localJSONObject = localJSONArray.optJSONObject(i);
        this.filters.add(new FilterOption(localJSONObject));
        i++;
      }
    }
    this.defaultFilterId = paramJSONObject.optLong("default_filter_id", 0L);
    if ((this.defaultFilterId < 1L) && (this.filters != null) && (this.filters.size() > 0))
    {
      FilterOption localFilterOption = (FilterOption)this.filters.get(0);
      this.defaultFilterId = localFilterOption.id;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\FilterGroup.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */