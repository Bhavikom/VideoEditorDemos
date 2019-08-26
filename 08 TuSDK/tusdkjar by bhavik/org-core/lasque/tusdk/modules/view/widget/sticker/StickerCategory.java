package org.lasque.tusdk.modules.view.widget.sticker;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.json.JSONObject;
import org.lasque.tusdk.core.utils.json.DataBase;
import org.lasque.tusdk.core.utils.json.JsonBaseBean;

public class StickerCategory
  extends JsonBaseBean
  implements Serializable
{
  @DataBase("id")
  public long id;
  @DataBase("name")
  public String name;
  public List<StickerGroup> datas;
  public StickerCategoryExtendType extendType;
  
  public StickerCategory() {}
  
  public StickerCategory(JSONObject paramJSONObject)
  {
    deserialize(paramJSONObject);
  }
  
  public StickerCategory(String paramString)
  {
    this.name = paramString;
  }
  
  public StickerCategory(long paramLong, String paramString)
  {
    this(paramString);
    this.id = paramLong;
  }
  
  public void append(StickerGroup paramStickerGroup)
  {
    if (this.datas == null) {
      this.datas = new ArrayList();
    }
    this.datas.add(paramStickerGroup);
  }
  
  public void insertFirst(StickerGroup paramStickerGroup)
  {
    if (this.datas == null) {
      this.datas = new ArrayList();
    }
    this.datas.add(0, paramStickerGroup);
  }
  
  public StickerCategory copy()
  {
    StickerCategory localStickerCategory = new StickerCategory();
    localStickerCategory.id = this.id;
    localStickerCategory.name = this.name;
    if (this.datas == null) {
      return localStickerCategory;
    }
    localStickerCategory.datas = new ArrayList(this.datas.size());
    Iterator localIterator = this.datas.iterator();
    while (localIterator.hasNext())
    {
      StickerGroup localStickerGroup = (StickerGroup)localIterator.next();
      localStickerCategory.datas.add(localStickerGroup.copy());
    }
    return localStickerCategory;
  }
  
  public StickerGroup removeGroup(long paramLong)
  {
    if (this.datas == null) {
      return null;
    }
    ArrayList localArrayList = new ArrayList(this.datas);
    Iterator localIterator = localArrayList.iterator();
    while (localIterator.hasNext())
    {
      StickerGroup localStickerGroup = (StickerGroup)localIterator.next();
      if (localStickerGroup.groupId == paramLong)
      {
        this.datas.remove(localStickerGroup);
        return localStickerGroup;
      }
    }
    return null;
  }
  
  public void deserialize(JSONObject paramJSONObject)
  {
    if (paramJSONObject == null) {
      return;
    }
    this.id = paramJSONObject.optLong("id", 0L);
    this.name = paramJSONObject.optString("name");
  }
  
  public static enum StickerCategoryType
  {
    private long a;
    
    private StickerCategoryType(long paramLong)
    {
      this.a = paramLong;
    }
    
    public long getValue()
    {
      return this.a;
    }
  }
  
  public static enum StickerCategoryExtendType
  {
    private StickerCategoryExtendType() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\view\widget\sticker\StickerCategory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */