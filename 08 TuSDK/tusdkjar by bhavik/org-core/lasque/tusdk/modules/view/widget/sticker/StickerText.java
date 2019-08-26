package org.lasque.tusdk.modules.view.widget.sticker;

import android.graphics.Color;
import android.graphics.Paint.Align;
import android.graphics.RectF;
import java.io.Serializable;
import org.json.JSONObject;
import org.lasque.tusdk.core.utils.json.DataBase;
import org.lasque.tusdk.core.utils.json.JsonBaseBean;

public class StickerText
  extends JsonBaseBean
  implements Serializable
{
  @DataBase("id")
  public long textId;
  @DataBase("sticker_id")
  public long stickerId;
  @DataBase("group_id")
  public long groupId;
  @DataBase("category_id")
  public long categoryId;
  @DataBase("type")
  public int textType;
  @DataBase("content")
  public String content;
  @DataBase("color")
  public String color;
  @DataBase("shadowColor")
  public String shadowColor;
  @DataBase("backgroundColor")
  public String backgroundColor;
  @DataBase("underline")
  public boolean underline;
  @DataBase("size")
  public float textSize;
  @DataBase("alignment")
  public int alignment;
  @DataBase("paddings")
  public int paddings;
  @DataBase("rect_left")
  public float rectLeft;
  @DataBase("rect_top")
  public float rectTop;
  @DataBase("rect_width")
  public float rectWidth;
  @DataBase("rect_height")
  public float rectHeight;
  
  public StickerText() {}
  
  public StickerText(JSONObject paramJSONObject)
  {
    deserialize(paramJSONObject);
  }
  
  public StickerText copy()
  {
    StickerText localStickerText = new StickerText();
    localStickerText.textId = this.textId;
    localStickerText.stickerId = this.stickerId;
    localStickerText.groupId = this.groupId;
    localStickerText.categoryId = this.categoryId;
    localStickerText.textType = this.textType;
    localStickerText.content = this.content;
    localStickerText.color = this.color;
    localStickerText.shadowColor = this.shadowColor;
    localStickerText.textSize = this.textSize;
    localStickerText.alignment = this.alignment;
    localStickerText.rectLeft = this.rectLeft;
    localStickerText.rectTop = this.rectTop;
    localStickerText.rectWidth = this.rectWidth;
    localStickerText.rectHeight = this.rectHeight;
    localStickerText.paddings = this.paddings;
    localStickerText.underline = this.underline;
    localStickerText.backgroundColor = this.backgroundColor;
    return localStickerText;
  }
  
  public StickerTextType getType()
  {
    switch (this.textType)
    {
    case 2: 
      return StickerTextType.TypeTime;
    case 3: 
      return StickerTextType.TypeDate;
    case 4: 
      return StickerTextType.TypeDateTime;
    case 5: 
      return StickerTextType.TypeLocal;
    case 6: 
      return StickerTextType.TypeWeather;
    }
    return StickerTextType.TypeDefault;
  }
  
  public RectF getRect()
  {
    return new RectF(this.rectLeft, this.rectTop, this.rectLeft + this.rectWidth, this.rectTop + this.rectHeight);
  }
  
  public int getColor()
  {
    if (this.color == null) {
      return -1;
    }
    return Color.parseColor(this.color);
  }
  
  public int getShadowColor()
  {
    if (this.shadowColor == null) {
      return -16777216;
    }
    return Color.parseColor(this.shadowColor);
  }
  
  public Paint.Align getAlignment()
  {
    switch (this.alignment)
    {
    case 1: 
      return Paint.Align.CENTER;
    case 2: 
      return Paint.Align.RIGHT;
    }
    return Paint.Align.LEFT;
  }
  
  public void deserialize(JSONObject paramJSONObject)
  {
    if (paramJSONObject == null) {
      return;
    }
    this.textId = paramJSONObject.optLong("id", 0L);
    this.stickerId = paramJSONObject.optLong("sticker_id", 0L);
    this.groupId = paramJSONObject.optLong("group_id", 0L);
    this.categoryId = paramJSONObject.optLong("category_id", 0L);
    this.textType = paramJSONObject.optInt("type", 0);
    this.content = paramJSONObject.optString("content");
    this.color = paramJSONObject.optString("color");
    this.shadowColor = paramJSONObject.optString("shadowColor");
    this.textSize = ((float)paramJSONObject.optDouble("size", 0.0D));
    this.alignment = paramJSONObject.optInt("alignment", 0);
    this.rectLeft = ((float)paramJSONObject.optDouble("rect_left", 0.0D));
    this.rectTop = ((float)paramJSONObject.optDouble("rect_top", 0.0D));
    this.rectWidth = ((float)paramJSONObject.optDouble("rect_width", 0.0D));
    this.rectHeight = ((float)paramJSONObject.optDouble("rect_height", 0.0D));
  }
  
  public static enum StickerTextType
  {
    private StickerTextType() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\view\widget\sticker\StickerText.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */