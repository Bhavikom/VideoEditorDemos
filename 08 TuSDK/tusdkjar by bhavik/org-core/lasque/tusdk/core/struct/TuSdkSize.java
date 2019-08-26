package org.lasque.tusdk.core.struct;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import java.io.Serializable;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.secret.SdkValid;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.hardware.CameraHelper;
import org.lasque.tusdk.core.utils.hardware.TuSdkGPU;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class TuSdkSize
  implements Serializable
{
  public int width;
  public int height;
  
  public static TuSdkSize create(Bitmap paramBitmap)
  {
    if (paramBitmap != null) {
      return new TuSdkSize(paramBitmap.getWidth(), paramBitmap.getHeight());
    }
    return null;
  }
  
  public static TuSdkSize create(TuSdkSize paramTuSdkSize)
  {
    if (paramTuSdkSize != null) {
      return new TuSdkSize(paramTuSdkSize.width, paramTuSdkSize.height);
    }
    return null;
  }
  
  public static TuSdkSize create(Rect paramRect)
  {
    if (paramRect != null) {
      return new TuSdkSize(paramRect.width(), paramRect.height());
    }
    return null;
  }
  
  public static TuSdkSize create(int paramInt)
  {
    return create(paramInt, paramInt);
  }
  
  public static TuSdkSize create(int paramInt1, int paramInt2)
  {
    return new TuSdkSize(paramInt1, paramInt2);
  }
  
  public static TuSdkSize createDP(Context paramContext, int paramInt1, int paramInt2)
  {
    return create(ContextUtils.dip2px(paramContext, paramInt1), ContextUtils.dip2px(paramContext, paramInt2));
  }
  
  public TuSdkSize() {}
  
  public TuSdkSize(int paramInt1, int paramInt2)
  {
    this.width = paramInt1;
    this.height = paramInt2;
  }
  
  public void set(TuSdkSize paramTuSdkSize)
  {
    if (paramTuSdkSize == null) {
      return;
    }
    set(paramTuSdkSize.width, paramTuSdkSize.height);
  }
  
  public void set(int paramInt1, int paramInt2)
  {
    this.width = paramInt1;
    this.height = paramInt2;
  }
  
  public int getRatio()
  {
    return CameraHelper.getSizeRatio(this.width, this.height);
  }
  
  public float getRatioFloat()
  {
    return this.width / this.height;
  }
  
  public TuSdkSize maxSize()
  {
    int i = maxSide();
    return new TuSdkSize(i, i);
  }
  
  public int maxSide()
  {
    return Math.max(this.width, this.height);
  }
  
  public TuSdkSize minSize()
  {
    int i = minSide();
    return new TuSdkSize(i, i);
  }
  
  public int minSide()
  {
    return Math.min(this.width, this.height);
  }
  
  public boolean isSize()
  {
    return minSide() > 0;
  }
  
  public float maxMinRatio()
  {
    return maxSide() / minSide();
  }
  
  public float minMaxRatio()
  {
    return minSide() / maxSide();
  }
  
  public float diagonal()
  {
    float f = (float)Math.sqrt(this.width * this.width + this.height * this.height);
    return f;
  }
  
  public boolean equals(Object paramObject)
  {
    if (!(paramObject instanceof TuSdkSize)) {
      return false;
    }
    TuSdkSize localTuSdkSize = (TuSdkSize)paramObject;
    return (this.width == localTuSdkSize.width) && (this.height == localTuSdkSize.height);
  }
  
  public int hashCode()
  {
    return this.width * 32713 + this.height;
  }
  
  public String toString()
  {
    String str = String.format("{width: %s, height:%s };", new Object[] { Integer.valueOf(this.width), Integer.valueOf(this.height) });
    return str;
  }
  
  public TuSdkSize copy()
  {
    return new TuSdkSize(this.width, this.height);
  }
  
  public TuSdkSize dp2Pix()
  {
    if ((TuSdkContext.ins() == null) || (TuSdkContext.ins().getContext() == null)) {
      return null;
    }
    Context localContext = TuSdkContext.ins().getContext();
    TuSdkSize localTuSdkSize = new TuSdkSize();
    localTuSdkSize.width = ContextUtils.dip2px(localContext, this.width);
    localTuSdkSize.height = ContextUtils.dip2px(localContext, this.height);
    return localTuSdkSize;
  }
  
  public final float limitScale()
  {
    TuSdkSize localTuSdkSize = limitSize();
    if (localTuSdkSize.maxSide() >= maxSide()) {
      return 1.0F;
    }
    float f = localTuSdkSize.maxSide() / maxSide();
    return f;
  }
  
  public final TuSdkSize limitSize()
  {
    return a(TuSdkGPU.getMaxTextureOptimizedSize());
  }
  
  private TuSdkSize a(int paramInt)
  {
    if (SdkValid.shared.maxImageSide() > 0) {
      paramInt = Math.min(Math.max(paramInt, 0), SdkValid.shared.maxImageSide());
    }
    if ((paramInt <= 0) || (!isSize()) || (paramInt >= maxSide())) {
      return evenSize();
    }
    TuSdkSize localTuSdkSize = create(this);
    if (this.width > this.height)
    {
      localTuSdkSize.width = paramInt;
      localTuSdkSize.height = ((int)Math.floor(paramInt / this.width * this.height));
    }
    else
    {
      localTuSdkSize.height = paramInt;
      localTuSdkSize.width = ((int)Math.floor(paramInt / this.height * this.width));
    }
    return localTuSdkSize.evenSize();
  }
  
  public TuSdkSize evenSize()
  {
    TuSdkSize localTuSdkSize = create(this);
    if (localTuSdkSize.width % 2 != 0) {
      localTuSdkSize.width -= 1;
    }
    if (localTuSdkSize.height % 2 != 0) {
      localTuSdkSize.height -= 1;
    }
    return localTuSdkSize;
  }
  
  public TuSdkSize transforOrientation(ImageOrientation paramImageOrientation)
  {
    TuSdkSize localTuSdkSize = create(this);
    if ((paramImageOrientation == null) || (!paramImageOrientation.isTransposed())) {
      return localTuSdkSize;
    }
    localTuSdkSize.width = this.height;
    localTuSdkSize.height = this.width;
    return localTuSdkSize;
  }
  
  public Point center()
  {
    Point localPoint = new Point();
    localPoint.x = ((int)(this.width * 0.5F));
    localPoint.y = ((int)(this.height * 0.5F));
    return localPoint;
  }
  
  public TuSdkSize scale(float paramFloat)
  {
    if ((paramFloat == 0.0F) || (paramFloat == 1.0F)) {
      return this;
    }
    return new TuSdkSize((int)(this.width * paramFloat), (int)(this.height * paramFloat)).evenSize();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\struct\TuSdkSize.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */