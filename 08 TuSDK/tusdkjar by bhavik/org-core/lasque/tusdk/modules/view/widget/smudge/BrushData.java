package org.lasque.tusdk.modules.view.widget.smudge;

import android.graphics.Bitmap;
import java.io.Serializable;
import java.util.HashMap;
import org.json.JSONObject;
import org.lasque.tusdk.core.utils.json.DataBase;
import org.lasque.tusdk.core.utils.json.JsonBaseBean;
import org.lasque.tusdk.core.utils.json.JsonHelper;

public final class BrushData
  extends JsonBaseBean
  implements Serializable
{
  @DataBase("id")
  public long brushId;
  @DataBase("group_id")
  public long groupId;
  @DataBase("code")
  public String code;
  @DataBase("name")
  public String name;
  public String thumb;
  @DataBase("thumb_name")
  public String thumbKey;
  @DataBase("brush_name")
  public String brushImageKey;
  @DataBase("brush_type")
  public int brushType;
  @DataBase("rotate_type")
  public int rotateType;
  @DataBase("position_type")
  public int positionType;
  @DataBase("size_type")
  public int sizeType;
  @DataBase("args")
  public HashMap<String, String> args;
  public boolean isInternal;
  private Bitmap a;
  
  public final Bitmap getImage()
  {
    return this.a;
  }
  
  public final void setImage(Bitmap paramBitmap)
  {
    this.a = paramBitmap;
  }
  
  public BrushData() {}
  
  public BrushData(JSONObject paramJSONObject)
  {
    deserialize(paramJSONObject);
  }
  
  public BrushType getType()
  {
    switch (this.brushType)
    {
    case 4: 
      return BrushType.TypeOnline;
    case 3: 
      return BrushType.TypeStamp;
    case 2: 
      return BrushType.TypeMosaic;
    }
    return BrushType.TypeEraser;
  }
  
  public void setType(BrushType paramBrushType)
  {
    switch (1.a[paramBrushType.ordinal()])
    {
    case 1: 
      this.brushType = 4;
      break;
    case 2: 
      this.brushType = 3;
      break;
    case 3: 
      this.brushType = 2;
      break;
    default: 
      this.brushType = 1;
    }
  }
  
  public RotateType getRotateType()
  {
    switch (this.rotateType)
    {
    case 4: 
      return RotateType.RotateLimitRandom;
    case 3: 
      return RotateType.RotateRandom;
    case 2: 
      return RotateType.RotateAuto;
    }
    return RotateType.RotateNone;
  }
  
  public PositionType getPositionType()
  {
    switch (this.positionType)
    {
    case 2: 
      return PositionType.PositionRandom;
    }
    return PositionType.PositionAuto;
  }
  
  public SizeType getSizeType()
  {
    switch (this.sizeType)
    {
    case 2: 
      return SizeType.SizeRandom;
    }
    return SizeType.SizeAuto;
  }
  
  public String getNameKey()
  {
    if (this.name == null) {
      return String.format("lsq_brush_%s", new Object[] { this.code });
    }
    return this.name;
  }
  
  public BrushData copy()
  {
    BrushData localBrushData = new BrushData();
    localBrushData.brushId = this.brushId;
    localBrushData.groupId = this.groupId;
    localBrushData.code = this.code;
    localBrushData.brushType = this.brushType;
    localBrushData.name = this.name;
    localBrushData.thumb = this.thumb;
    localBrushData.thumbKey = this.thumbKey;
    localBrushData.brushImageKey = this.brushImageKey;
    localBrushData.rotateType = this.rotateType;
    localBrushData.positionType = this.positionType;
    localBrushData.sizeType = this.sizeType;
    return localBrushData;
  }
  
  public static BrushData create(long paramLong, String paramString1, String paramString2)
  {
    BrushData localBrushData = new BrushData();
    localBrushData.brushId = paramLong;
    localBrushData.thumbKey = paramString1;
    localBrushData.brushImageKey = paramString2;
    return localBrushData;
  }
  
  public void deserialize(JSONObject paramJSONObject)
  {
    if (paramJSONObject == null) {
      return;
    }
    this.brushId = paramJSONObject.optLong("id", 0L);
    this.code = paramJSONObject.optString("code");
    this.groupId = paramJSONObject.optLong("group_id", 0L);
    this.name = paramJSONObject.optString("name");
    this.thumb = paramJSONObject.optString("thumb");
    this.thumbKey = paramJSONObject.optString("thumb_name");
    this.brushImageKey = paramJSONObject.optString("brush_name");
    this.brushType = paramJSONObject.optInt("brush_type", 0);
    this.rotateType = paramJSONObject.optInt("rotate_type", 0);
    this.positionType = paramJSONObject.optInt("position_type", 0);
    this.sizeType = paramJSONObject.optInt("size_type", 0);
    this.args = JsonHelper.toHashMap(JsonHelper.getJSONObject(paramJSONObject, "args"));
  }
  
  public static enum SizeType
  {
    private SizeType() {}
  }
  
  public static enum PositionType
  {
    private PositionType() {}
  }
  
  public static enum RotateType
  {
    private RotateType() {}
  }
  
  public static enum BrushType
  {
    private BrushType() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\view\widget\smudge\BrushData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */