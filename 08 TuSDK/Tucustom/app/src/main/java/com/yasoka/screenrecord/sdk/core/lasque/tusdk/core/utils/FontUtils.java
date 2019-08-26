// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils;

import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Paint;

public class FontUtils
{
    public static int computBaseline(final Paint.FontMetricsInt fontMetricsInt, final RectF rectF) {
        if (fontMetricsInt == null) {
            return 0;
        }
        return (int)(rectF.top + (rectF.bottom - rectF.top - fontMetricsInt.bottom + fontMetricsInt.top) / 2.0f - fontMetricsInt.top);
    }
    
    public static Rect getTextBounds(final String s, final float textSize) {
        final Rect rect = new Rect();
        final Paint paint = new Paint(1);
        paint.setTextSize(textSize);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.getTextBounds(s, 0, s.length(), rect);
        final Rect rect2 = rect;
        rect2.right += rect.left;
        final Rect rect3 = rect;
        rect3.bottom += rect.top;
        return rect;
    }
    
    public static Rect getTextBoundsExcludeFontPadding(final String s, final float textSize) {
        final Rect rect = new Rect();
        final Paint paint = new Paint(1);
        paint.setTextSize(textSize);
        paint.setStyle(Paint.Style.FILL);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.getTextBounds(s, 0, s.length(), rect);
        final Rect rect2 = rect;
        rect2.right += rect.left;
        final Rect rect3 = rect;
        rect3.bottom += rect.top;
        final Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        final Rect rect4 = rect;
        rect4.top += (int)(fontMetrics.ascent - fontMetrics.top);
        final Rect rect5 = rect;
        rect5.bottom += (int)(fontMetrics.bottom - fontMetrics.descent);
        return rect;
    }
}
