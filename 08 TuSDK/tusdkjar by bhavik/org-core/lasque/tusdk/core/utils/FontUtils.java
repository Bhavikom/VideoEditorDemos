package org.lasque.tusdk.core.utils;

import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.RectF;

public class FontUtils
{
  public static int computBaseline(Paint.FontMetricsInt paramFontMetricsInt, RectF paramRectF)
  {
    if (paramFontMetricsInt == null) {
      return 0;
    }
    int i = (int)(paramRectF.top + (paramRectF.bottom - paramRectF.top - paramFontMetricsInt.bottom + paramFontMetricsInt.top) / 2.0F - paramFontMetricsInt.top);
    return i;
  }
  
  public static Rect getTextBounds(String paramString, float paramFloat)
  {
    Rect localRect = new Rect();
    Paint localPaint = new Paint(1);
    localPaint.setTextSize(paramFloat);
    localPaint.setStyle(Paint.Style.FILL);
    localPaint.setTextAlign(Paint.Align.LEFT);
    localPaint.getTextBounds(paramString, 0, paramString.length(), localRect);
    localRect.right += localRect.left;
    localRect.bottom += localRect.top;
    return localRect;
  }
  
  public static Rect getTextBoundsExcludeFontPadding(String paramString, float paramFloat)
  {
    Rect localRect = new Rect();
    Paint localPaint = new Paint(1);
    localPaint.setTextSize(paramFloat);
    localPaint.setStyle(Paint.Style.FILL);
    localPaint.setTextAlign(Paint.Align.LEFT);
    localPaint.getTextBounds(paramString, 0, paramString.length(), localRect);
    localRect.right += localRect.left;
    localRect.bottom += localRect.top;
    Paint.FontMetrics localFontMetrics = localPaint.getFontMetrics();
    Rect tmp80_79 = localRect;
    tmp80_79.top = ((int)(tmp80_79.top + (localFontMetrics.ascent - localFontMetrics.top)));
    Rect tmp102_101 = localRect;
    tmp102_101.bottom = ((int)(tmp102_101.bottom + (localFontMetrics.bottom - localFontMetrics.descent)));
    return localRect;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\FontUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */