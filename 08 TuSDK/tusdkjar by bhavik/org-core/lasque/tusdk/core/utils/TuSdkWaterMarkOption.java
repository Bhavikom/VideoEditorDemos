package org.lasque.tusdk.core.utils;

import android.graphics.Bitmap;
import org.lasque.tusdk.core.utils.image.BitmapHelper;

public class TuSdkWaterMarkOption
{
  private WaterMarkPosition a = WaterMarkPosition.BottomRight;
  private String b;
  private Bitmap c;
  private float d = 6.0F;
  private TextPosition e = TextPosition.Right;
  private int f = 2;
  private int g = 24;
  private String h = "#FFFFFF";
  private String i = "#000000";
  
  public WaterMarkPosition getMarkPosition()
  {
    return this.a;
  }
  
  public void setMarkPosition(WaterMarkPosition paramWaterMarkPosition)
  {
    this.a = paramWaterMarkPosition;
  }
  
  public String getMarkText()
  {
    return this.b;
  }
  
  public void setMarkText(String paramString)
  {
    this.b = paramString;
  }
  
  public Bitmap getMarkImage()
  {
    return this.c;
  }
  
  public void setMarkImage(Bitmap paramBitmap)
  {
    this.c = paramBitmap;
  }
  
  public float getMarkMargin()
  {
    return this.d;
  }
  
  public void setMarkMargin(float paramFloat)
  {
    this.d = paramFloat;
  }
  
  public TextPosition getMarkTextPosition()
  {
    return this.e;
  }
  
  public void setMarkTextPosition(TextPosition paramTextPosition)
  {
    this.e = paramTextPosition;
  }
  
  public int getMarkTextPadding()
  {
    return this.f;
  }
  
  public void setMarkTextPadding(int paramInt)
  {
    this.f = paramInt;
  }
  
  public int getMarkTextSize()
  {
    return this.g;
  }
  
  public void setMarkTextSize(int paramInt)
  {
    this.g = paramInt;
  }
  
  public String getMarkTextColor()
  {
    return this.h;
  }
  
  public void setMarkTextColor(String paramString)
  {
    this.h = paramString;
  }
  
  public String getMarkTextShadowColor()
  {
    return this.i;
  }
  
  public void setMarkTextShadowColor(String paramString)
  {
    this.i = paramString;
  }
  
  public boolean isValid()
  {
    return ((StringHelper.isNotEmpty(this.b)) && (StringHelper.isNotBlank(this.b))) || (this.c != null);
  }
  
  public void destroy()
  {
    if (this.c != null) {
      BitmapHelper.recycled(this.c);
    }
  }
  
  public static enum TextPosition
  {
    private TextPosition() {}
  }
  
  public static enum WaterMarkPosition
  {
    private WaterMarkPosition() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\TuSdkWaterMarkOption.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */