package org.lasque.tusdk.core.sticker;

import java.io.Serializable;
import java.util.ArrayList;
import org.json.JSONObject;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.json.DataBase;
import org.lasque.tusdk.core.utils.json.JsonBaseBean;
import org.lasque.tusdk.core.utils.json.JsonHelper;

public class StickerPositionInfo
  extends JsonBaseBean
  implements Serializable
{
  @DataBase("model_width")
  public int modelWidth;
  @DataBase("model_height")
  public int modelHeight;
  @DataBase("screen_width")
  public int screenWidth;
  @DataBase("screem_height")
  public int screenHeight;
  @DataBase("model_type")
  public int categoryId;
  @DataBase("pos_type")
  public int posType;
  @DataBase("render_type")
  public int renderType;
  @DataBase("ratio")
  public float ratio;
  @DataBase("scale")
  public float scale;
  @DataBase("offset_x")
  public float offsetX;
  @DataBase("offset_y")
  public float offsetY;
  @DataBase("rotation")
  public float rotation;
  @DataBase("animation_interval")
  public int frameInterval;
  @DataBase("animation_files")
  public ArrayList<String> resourceList;
  @DataBase("animation_loop")
  public int loopMode;
  @DataBase("animation_loop_start")
  public int loopStartIndex;
  
  public StickerPositionInfo() {}
  
  public StickerPositionInfo(JSONObject paramJSONObject)
  {
    deserialize(paramJSONObject);
  }
  
  public int getFrameInterval()
  {
    if (this.frameInterval <= 0) {
      this.frameInterval = 100;
    }
    return this.frameInterval;
  }
  
  public StickerPositionType getPosType()
  {
    switch (this.posType)
    {
    case 1: 
      return StickerPositionType.StickerPosEyeBrow;
    case 2: 
      return StickerPositionType.StickerPosEye;
    case 3: 
      return StickerPositionType.StickerPosNose;
    case 4: 
      return StickerPositionType.StickerPosMouth;
    case 5: 
      return StickerPositionType.StickerPosCheek;
    case 6: 
      return StickerPositionType.StickerPosHead;
    case 7: 
      return StickerPositionType.StickerPosJaw;
    case 8: 
      return StickerPositionType.StickerPosEyeShadow;
    case 9: 
      return StickerPositionType.StickerPosLip;
    case 100: 
      return StickerPositionType.StickerPosFullScreen;
    case 101: 
      return StickerPositionType.StickerPosScreenLeftTop;
    case 102: 
      return StickerPositionType.StickerPosScreenRightTop;
    case 103: 
      return StickerPositionType.StickerPosScreenLeftBottom;
    case 104: 
      return StickerPositionType.StickerPosScreenRightBottom;
    case 105: 
      return StickerPositionType.StickerPosScreenCenter;
    case 106: 
      return StickerPositionType.StickerPosScreenRightCenter;
    case 107: 
      return StickerPositionType.StickerPosScreenLeftCenter;
    case 108: 
      return StickerPositionType.StickerPosScreenTopCenter;
    case 109: 
      return StickerPositionType.StickerPosScreenBottomCenter;
    }
    return StickerPositionType.StickerPosHead;
  }
  
  public StickerRenderType getRenderType()
  {
    switch (this.renderType)
    {
    case 1: 
      return StickerRenderType.lsqRenderAlphaBlend;
    case 2: 
      return StickerRenderType.lsqrenderBlendMultipy;
    case 3: 
      return StickerRenderType.lsqRenderLightGlare;
    }
    return StickerRenderType.lsqRenderAlphaBlend;
  }
  
  public TuSdkSize getDesignScreenSize()
  {
    if ((this.screenWidth > 0) && (this.screenHeight > 0)) {
      return TuSdkSize.create(this.screenWidth, this.screenHeight);
    }
    return TuSdkSize.create(800, 1416);
  }
  
  public boolean hasAnimationSupported()
  {
    return (this.resourceList != null) && (this.resourceList.size() > 0);
  }
  
  public void deserialize(JSONObject paramJSONObject)
  {
    if (paramJSONObject == null) {
      return;
    }
    this.modelWidth = paramJSONObject.optInt("model_width", 0);
    this.modelHeight = paramJSONObject.optInt("model_height", 0);
    this.screenWidth = paramJSONObject.optInt("screen_width", 0);
    this.screenHeight = paramJSONObject.optInt("screen_height", 0);
    this.categoryId = paramJSONObject.optInt("model_type", 0);
    this.posType = paramJSONObject.optInt("pos_type", 0);
    this.renderType = paramJSONObject.optInt("render_type", 0);
    this.ratio = ((float)paramJSONObject.optDouble("ratio", 0.0D));
    this.scale = ((float)paramJSONObject.optDouble("scale", 0.0D));
    this.offsetX = ((float)paramJSONObject.optDouble("offset_x", 0.0D));
    this.offsetY = ((float)paramJSONObject.optDouble("offset_y", 0.0D));
    this.rotation = ((float)paramJSONObject.optDouble("rotation", 0.0D));
    this.frameInterval = paramJSONObject.optInt("animation_interval", 0);
    this.resourceList = JsonHelper.toStringList(JsonHelper.getJSONArray(paramJSONObject, "animation_files"));
    this.loopMode = paramJSONObject.optInt("animation_loop", 0);
    this.loopStartIndex = paramJSONObject.optInt("animation_loop_start", 0);
  }
  
  public static enum StickerRenderType
  {
    private StickerRenderType() {}
  }
  
  public static enum StickerLoopMode
  {
    private StickerLoopMode() {}
  }
  
  public static enum StickerPositionType
  {
    private int a;
    
    private StickerPositionType(int paramInt)
    {
      this.a = paramInt;
    }
    
    public int getValue()
    {
      return this.a;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\sticker\StickerPositionInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */