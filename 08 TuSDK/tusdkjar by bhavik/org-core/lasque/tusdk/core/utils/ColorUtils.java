package org.lasque.tusdk.core.utils;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;

public class ColorUtils
{
  public static void setColorFilter(ColorDrawable paramColorDrawable, float[] paramArrayOfFloat)
  {
    if (paramColorDrawable == null) {
      return;
    }
    int i = getColor(paramColorDrawable);
    int j = filterColor(paramArrayOfFloat, i);
    setColor(paramColorDrawable, j);
  }
  
  @TargetApi(11)
  public static void setColor(ColorDrawable paramColorDrawable, int paramInt)
  {
    if (paramColorDrawable == null) {
      return;
    }
    paramColorDrawable.setColor(paramInt);
  }
  
  @TargetApi(11)
  public static int getColor(ColorDrawable paramColorDrawable)
  {
    if (paramColorDrawable == null) {
      return 0;
    }
    return paramColorDrawable.getColor();
  }
  
  public static int filterColor(float[] paramArrayOfFloat, int paramInt)
  {
    if (paramArrayOfFloat.length != 20) {
      return paramInt;
    }
    String str1 = String.format("%#010x", new Object[] { Integer.valueOf(paramInt) });
    int i = mergerColor(Integer.parseInt(str1.substring(4, 6), 16), (int)paramArrayOfFloat[4]);
    int j = mergerColor(Integer.parseInt(str1.substring(6, 8), 16), (int)paramArrayOfFloat[9]);
    int k = mergerColor(Integer.parseInt(str1.substring(8, 10), 16), (int)paramArrayOfFloat[14]);
    String str2 = String.format("#%s%02x%02x%02x", new Object[] { str1.substring(2, 4), Integer.valueOf(i), Integer.valueOf(j), Integer.valueOf(k) });
    return Color.parseColor(str2);
  }
  
  public static int mergerColor(int paramInt1, int paramInt2)
  {
    paramInt1 += paramInt2;
    if (paramInt1 < 0) {
      paramInt1 = 0;
    } else if (paramInt1 > 255) {
      paramInt1 = 255;
    }
    return paramInt1;
  }
  
  public static void setBackgroudImageColor(View paramView, int paramInt)
  {
    if ((paramView == null) || (paramView.getBackground() == null)) {
      return;
    }
    Drawable localDrawable = paramView.getBackground();
    localDrawable.clearColorFilter();
    localDrawable.setColorFilter(paramInt, PorterDuff.Mode.SRC_IN);
  }
  
  public static int alphaEvaluator(float paramFloat, int paramInt)
  {
    int i = paramInt >> 16 & 0xFF;
    int j = paramInt >> 8 & 0xFF;
    int k = paramInt & 0xFF;
    return (int)(255.0F * paramFloat) << 24 | i << 16 | j << 8 | k;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\ColorUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */