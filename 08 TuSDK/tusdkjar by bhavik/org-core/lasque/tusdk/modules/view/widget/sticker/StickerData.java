package org.lasque.tusdk.modules.view.widget.sticker;

import android.graphics.Bitmap;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lasque.tusdk.core.sticker.StickerPositionInfo;
import org.lasque.tusdk.core.sticker.StickerPositionInfo.StickerPositionType;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.json.DataBase;
import org.lasque.tusdk.core.utils.json.JsonBaseBean;
import org.lasque.tusdk.core.utils.json.JsonHelper;

public class StickerData
  extends JsonBaseBean
  implements Serializable
{
  @DataBase("id")
  public long stickerId;
  @DataBase("group_id")
  public long groupId;
  @DataBase("category_id")
  public long categoryId;
  @DataBase("name")
  public String name;
  @DataBase("thumb_name")
  public String previewName;
  @DataBase("sticker_name")
  public String stickerImageName;
  @DataBase("width")
  public int width;
  @DataBase("height")
  public int height;
  @DataBase("type_id")
  public int stickerType;
  @DataBase("texts")
  public ArrayList<StickerText> texts;
  @DataBase("sticker_face_info")
  public StickerPositionInfo positionInfo;
  private Bitmap a;
  
  public TuSdkSize size()
  {
    return TuSdkSize.create(this.width, this.height);
  }
  
  public TuSdkSize sizePixies()
  {
    TuSdkSize localTuSdkSize = size();
    return localTuSdkSize.dp2Pix();
  }
  
  public final Bitmap getImage()
  {
    return this.a;
  }
  
  public final void setImage(Bitmap paramBitmap)
  {
    this.a = paramBitmap;
  }
  
  public StickerData() {}
  
  public StickerData(JSONObject paramJSONObject)
  {
    deserialize(paramJSONObject);
  }
  
  public StickerType getType()
  {
    switch (this.stickerType)
    {
    case 3: 
      return StickerType.TypeDynamic;
    case 2: 
      return StickerType.TypeText;
    }
    return StickerType.TypeImage;
  }
  
  public boolean requireFaceFeature()
  {
    if ((getType() == StickerType.TypeDynamic) && (this.positionInfo != null)) {
      return this.positionInfo.getPosType().getValue() < StickerPositionInfo.StickerPositionType.StickerPosFullScreen.getValue();
    }
    return false;
  }
  
  public StickerData copy()
  {
    StickerData localStickerData = new StickerData();
    localStickerData.stickerId = this.stickerId;
    localStickerData.groupId = this.groupId;
    localStickerData.categoryId = this.categoryId;
    localStickerData.width = this.width;
    localStickerData.height = this.height;
    localStickerData.stickerType = this.stickerType;
    localStickerData.a = this.a;
    if ((this.texts != null) && (localStickerData.stickerType == 2))
    {
      localStickerData.texts = new ArrayList(this.texts.size());
      Iterator localIterator = this.texts.iterator();
      while (localIterator.hasNext())
      {
        StickerText localStickerText = (StickerText)localIterator.next();
        localStickerData.texts.add(localStickerText.copy());
      }
    }
    return localStickerData;
  }
  
  public StickerText getStickerText(long paramLong)
  {
    if (this.texts == null) {
      return null;
    }
    Iterator localIterator = this.texts.iterator();
    while (localIterator.hasNext())
    {
      StickerText localStickerText = (StickerText)localIterator.next();
      if (localStickerText.textId == paramLong) {
        return localStickerText;
      }
    }
    return null;
  }
  
  public static StickerData create(long paramLong1, long paramLong2, String paramString1, String paramString2, int paramInt1, int paramInt2, String paramString3)
  {
    StickerData localStickerData = new StickerData();
    localStickerData.stickerId = paramLong1;
    localStickerData.categoryId = paramLong2;
    localStickerData.previewName = paramString1;
    localStickerData.stickerImageName = paramString2;
    localStickerData.width = paramInt1;
    localStickerData.height = paramInt2;
    return localStickerData;
  }
  
  public void deserialize(JSONObject paramJSONObject)
  {
    if (paramJSONObject == null) {
      return;
    }
    this.stickerId = paramJSONObject.optLong("id", 0L);
    this.groupId = paramJSONObject.optLong("group_id", 0L);
    this.categoryId = paramJSONObject.optLong("category_id", 0L);
    this.name = paramJSONObject.optString("name");
    this.previewName = paramJSONObject.optString("thumb_name");
    this.stickerImageName = paramJSONObject.optString("sticker_name");
    this.width = paramJSONObject.optInt("width", 0);
    this.height = paramJSONObject.optInt("height", 0);
    this.stickerType = paramJSONObject.optInt("type_id", 0);
    JSONArray localJSONArray = JsonHelper.getJSONArray(paramJSONObject, "texts");
    if ((localJSONArray != null) && (localJSONArray.length() > 0))
    {
      this.texts = new ArrayList(localJSONArray.length());
      int i = 0;
      int j = localJSONArray.length();
      while (i < j)
      {
        JSONObject localJSONObject2 = localJSONArray.optJSONObject(i);
        this.texts.add(new StickerText(localJSONObject2));
        i++;
      }
    }
    JSONObject localJSONObject1 = JsonHelper.getJSONObject(paramJSONObject, "sticker_face_info");
    if (localJSONObject1 != null) {
      this.positionInfo = new StickerPositionInfo(localJSONObject1);
    }
  }
  
  public static enum StickerType
  {
    private StickerType() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\view\widget\sticker\StickerData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */