package org.lasque.tusdk.modules.view.widget.sticker;

import android.text.TextUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lasque.tusdk.core.network.TuSdkHttpEngine;
import org.lasque.tusdk.core.utils.json.DataBase;
import org.lasque.tusdk.core.utils.json.JsonBaseBean;
import org.lasque.tusdk.core.utils.json.JsonHelper;

public class StickerGroup
  extends JsonBaseBean
  implements Serializable
{
  @DataBase("id")
  public long groupId;
  @DataBase("category_id")
  public long categoryId;
  @DataBase("file")
  public String file;
  @DataBase("valid_type")
  public int validType;
  @DataBase("valid_key")
  public String validKey;
  @DataBase("name")
  public String name;
  @DataBase("thumb")
  public String previewName;
  @DataBase("name_en")
  public String name_en;
  @DataBase("stickers")
  public ArrayList<StickerData> stickers;
  public boolean isDownload;
  
  public StickerGroup() {}
  
  public StickerGroup(JSONObject paramJSONObject)
  {
    deserialize(paramJSONObject);
  }
  
  public StickerGroup copy()
  {
    StickerGroup localStickerGroup = new StickerGroup();
    localStickerGroup.groupId = this.groupId;
    localStickerGroup.categoryId = this.categoryId;
    localStickerGroup.name = this.name;
    localStickerGroup.name_en = this.name_en;
    localStickerGroup.isDownload = this.isDownload;
    if (this.stickers == null) {
      return localStickerGroup;
    }
    localStickerGroup.stickers = new ArrayList(this.stickers.size());
    Iterator localIterator = this.stickers.iterator();
    while (localIterator.hasNext())
    {
      StickerData localStickerData = (StickerData)localIterator.next();
      localStickerGroup.stickers.add(localStickerData.copy());
    }
    return localStickerGroup;
  }
  
  public StickerData getSticker(long paramLong)
  {
    if (this.stickers == null) {
      return null;
    }
    Iterator localIterator = this.stickers.iterator();
    while (localIterator.hasNext())
    {
      StickerData localStickerData = (StickerData)localIterator.next();
      if (localStickerData.stickerId == paramLong) {
        return localStickerData;
      }
    }
    return null;
  }
  
  public boolean requireFaceFeature()
  {
    if (this.stickers == null) {
      return false;
    }
    Iterator localIterator = this.stickers.iterator();
    while (localIterator.hasNext())
    {
      StickerData localStickerData = (StickerData)localIterator.next();
      if (localStickerData.requireFaceFeature()) {
        return true;
      }
    }
    return false;
  }
  
  public String getNameOfLanguage()
  {
    Locale localLocale = Locale.getDefault();
    String str = localLocale.getLanguage();
    if ((!TextUtils.isEmpty(this.name_en)) && (str != null) && (!str.endsWith("zh"))) {
      return this.name_en;
    }
    return this.name;
  }
  
  public String getPreviewNamePath()
  {
    if (TextUtils.isEmpty(this.previewName)) {
      return null;
    }
    return TuSdkHttpEngine.WEB_PIC_DOMAIN + "/" + this.previewName;
  }
  
  public void deserialize(JSONObject paramJSONObject)
  {
    if (paramJSONObject == null) {
      return;
    }
    this.groupId = paramJSONObject.optLong("id", 0L);
    this.categoryId = paramJSONObject.optLong("category_id", 0L);
    this.file = paramJSONObject.optString("file");
    this.validType = paramJSONObject.optInt("valid_type", 0);
    this.validKey = paramJSONObject.optString("valid_key");
    this.name = paramJSONObject.optString("name");
    this.name_en = paramJSONObject.optString("name_en");
    this.previewName = paramJSONObject.optString("thumb");
    JSONArray localJSONArray = JsonHelper.getJSONArray(paramJSONObject, "stickers");
    if ((localJSONArray != null) && (localJSONArray.length() > 0))
    {
      this.stickers = new ArrayList(localJSONArray.length());
      int i = 0;
      int j = localJSONArray.length();
      while (i < j)
      {
        JSONObject localJSONObject = localJSONArray.optJSONObject(i);
        this.stickers.add(new StickerData(localJSONObject));
        i++;
      }
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\view\widget\sticker\StickerGroup.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */