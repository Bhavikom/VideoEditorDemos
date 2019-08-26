package org.lasque.tusdk.core;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lasque.tusdk.core.seles.tusdk.FilterGroup;
import org.lasque.tusdk.core.utils.json.DataBase;
import org.lasque.tusdk.core.utils.json.JsonBaseBean;
import org.lasque.tusdk.core.utils.json.JsonHelper;
import org.lasque.tusdk.modules.view.widget.smudge.BrushGroup;
import org.lasque.tusdk.modules.view.widget.sticker.StickerCategory;
import org.lasque.tusdk.modules.view.widget.sticker.StickerGroup;

public class TuSdkConfigs
  extends JsonBaseBean
  implements Serializable
{
  @DataBase("app_type")
  public int appType;
  @DataBase("filterGroups")
  public ArrayList<FilterGroup> filterGroups;
  @DataBase("stickerCategories")
  public ArrayList<StickerCategory> stickerCategories;
  @DataBase("stickerGroups")
  public ArrayList<StickerGroup> stickerGroups;
  @DataBase("brushGroups")
  public ArrayList<BrushGroup> brushGroups;
  @DataBase("master")
  public String master;
  @DataBase("masters")
  public HashMap<String, String> masters;
  
  public TuSdkConfigs() {}
  
  public TuSdkConfigs(JSONObject paramJSONObject)
  {
    deserialize(paramJSONObject);
  }
  
  public void deserialize(JSONObject paramJSONObject)
  {
    if (paramJSONObject == null) {
      return;
    }
    this.master = paramJSONObject.optString("master", null);
    this.masters = JsonHelper.toHashMap(JsonHelper.getJSONObject(paramJSONObject, "masters"));
    this.appType = paramJSONObject.optInt("app_type");
    JSONArray localJSONArray = JsonHelper.getJSONArray(paramJSONObject, "filterGroups");
    int i;
    int j;
    JSONObject localJSONObject;
    if ((localJSONArray != null) && (localJSONArray.length() > 0))
    {
      this.filterGroups = new ArrayList(localJSONArray.length());
      i = 0;
      j = localJSONArray.length();
      while (i < j)
      {
        localJSONObject = localJSONArray.optJSONObject(i);
        this.filterGroups.add(new FilterGroup(localJSONObject));
        i++;
      }
    }
    localJSONArray = JsonHelper.getJSONArray(paramJSONObject, "stickerCategories");
    if ((localJSONArray != null) && (localJSONArray.length() > 0))
    {
      this.stickerCategories = new ArrayList(localJSONArray.length());
      i = 0;
      j = localJSONArray.length();
      while (i < j)
      {
        localJSONObject = localJSONArray.optJSONObject(i);
        this.stickerCategories.add(new StickerCategory(localJSONObject));
        i++;
      }
    }
    localJSONArray = JsonHelper.getJSONArray(paramJSONObject, "stickerGroups");
    if ((localJSONArray != null) && (localJSONArray.length() > 0))
    {
      this.stickerGroups = new ArrayList(localJSONArray.length());
      i = 0;
      j = localJSONArray.length();
      while (i < j)
      {
        localJSONObject = localJSONArray.optJSONObject(i);
        this.stickerGroups.add(new StickerGroup(localJSONObject));
        i++;
      }
    }
    localJSONArray = JsonHelper.getJSONArray(paramJSONObject, "brushGroups");
    if ((localJSONArray != null) && (localJSONArray.length() > 0))
    {
      this.brushGroups = new ArrayList(localJSONArray.length());
      i = 0;
      j = localJSONArray.length();
      while (i < j)
      {
        localJSONObject = localJSONArray.optJSONObject(i);
        this.brushGroups.add(new BrushGroup(localJSONObject));
        i++;
      }
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\TuSdkConfigs.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */