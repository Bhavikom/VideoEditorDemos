package org.lasque.tusdk.core.seles.tusdk.textSticker;

import android.graphics.Bitmap;
import org.lasque.tusdk.core.struct.TuSdkSize;

public class Image2DStickerData
{
  private Bitmap a;
  private int b;
  private int c;
  private float d;
  private float e;
  private float f;
  private float g;
  
  public TuSdkSize size()
  {
    return TuSdkSize.create(this.b, this.c);
  }
  
  public Image2DStickerData() {}
  
  public Image2DStickerData(Bitmap paramBitmap, int paramInt1, int paramInt2, float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    this.a = paramBitmap;
    this.b = paramInt1;
    this.c = paramInt2;
    this.d = paramFloat1;
    this.e = paramFloat2;
    this.f = paramFloat3;
    this.g = paramFloat4;
  }
  
  public Bitmap getBitmap()
  {
    return this.a;
  }
  
  public void setBitmap(Bitmap paramBitmap)
  {
    this.a = paramBitmap;
  }
  
  public int getWidth()
  {
    return this.b;
  }
  
  public void setWidth(int paramInt)
  {
    this.b = paramInt;
  }
  
  public int getHeight()
  {
    return this.c;
  }
  
  public void setHeight(int paramInt)
  {
    this.c = paramInt;
  }
  
  public float getRatio()
  {
    return this.d;
  }
  
  public void setRatio(float paramFloat)
  {
    this.d = paramFloat;
  }
  
  public float getOffsetX()
  {
    return this.e;
  }
  
  public void setOffsetX(float paramFloat)
  {
    this.e = paramFloat;
  }
  
  public float getOffsetY()
  {
    return this.f;
  }
  
  public void setOffsetY(float paramFloat)
  {
    this.f = paramFloat;
  }
  
  public float getRotation()
  {
    return this.g;
  }
  
  public void setRotation(float paramFloat)
  {
    this.g = paramFloat;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\textSticker\Image2DStickerData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */