package org.lasque.tusdk.core.seles.tusdk.textSticker;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.TLog;

public class TuSdkImage2DSticker
{
  private Image2DStickerData a;
  private boolean b;
  private int c;
  private TuSdkSize d;
  private TuSdkSize e;
  
  public boolean isEnabled()
  {
    return this.b;
  }
  
  public void setEnabled(boolean paramBoolean)
  {
    this.b = paramBoolean;
  }
  
  public int getCurrentTextureId()
  {
    if ((this.a == null) || (this.a.getBitmap() == null))
    {
      TLog.e("Bitmap is null !!!", new Object[0]);
      return -1;
    }
    if (this.c <= 0) {
      a(this.a.getBitmap());
    }
    return this.c;
  }
  
  public void reset()
  {
    this.c = -1;
  }
  
  private void a(Bitmap paramBitmap)
  {
    if ((paramBitmap == null) || (paramBitmap.isRecycled())) {
      return;
    }
    TuSdkSize localTuSdkSize = TuSdkSize.create(paramBitmap);
    if (localTuSdkSize.minSide() <= 0)
    {
      TLog.e("Passed image must not be empty - it should be at least 1px tall and wide", new Object[0]);
      return;
    }
    int[] arrayOfInt = new int[1];
    GLES20.glGenTextures(1, arrayOfInt, 0);
    int i = arrayOfInt[0];
    GLES20.glBindTexture(3553, i);
    GLUtils.texImage2D(3553, 0, paramBitmap, 0);
    GLES20.glTexParameterf(3553, 10241, 9729.0F);
    GLES20.glTexParameterf(3553, 10240, 9729.0F);
    GLES20.glTexParameterf(3553, 10242, 33071.0F);
    GLES20.glTexParameterf(3553, 10243, 33071.0F);
    GLES20.glBindTexture(3553, 0);
    this.d = localTuSdkSize;
    this.c = i;
  }
  
  public TuSdkSize getCurrentSize()
  {
    if (this.d == null) {
      a(this.a.getBitmap());
    }
    return this.d;
  }
  
  public Image2DStickerData getCurrentSticker()
  {
    return this.a;
  }
  
  public void setCurrentSticker(Image2DStickerData paramImage2DStickerData)
  {
    this.a = paramImage2DStickerData;
  }
  
  public TuSdkSize getDesignScreenSize()
  {
    return this.e;
  }
  
  public void setDesignScreenSize(TuSdkSize paramTuSdkSize)
  {
    this.e = paramTuSdkSize;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\textSticker\TuSdkImage2DSticker.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */