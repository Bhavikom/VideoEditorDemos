// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.modules.view.widget.sticker;

import android.os.Build;
import android.view.View;
import android.graphics.Color;
//import org.lasque.tusdk.core.utils.FontUtils;
import android.text.TextUtils;
import java.util.ArrayList;
import android.text.TextPaint;
//import org.lasque.tusdk.core.TuSdkContext;
import android.graphics.Matrix;
import android.graphics.RectF;
//import org.lasque.tusdk.core.utils.RectHelper;
//import org.lasque.tusdk.core.struct.TuSdkSizeF;
import java.util.Iterator;
//import org.lasque.tusdk.core.utils.image.BitmapHelper;
import android.graphics.Paint;
import android.graphics.Canvas;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import java.util.List;
import android.graphics.Bitmap;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSizeF;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.FontUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.RectHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.BitmapHelper;

public class StickerFactory
{
    public static Bitmap megerStickers(final Bitmap bitmap, final List<StickerResult> list) {
        return megerStickers(bitmap, list, null, true);
    }
    
    public static Bitmap megerStickers(final Bitmap bitmap, final List<StickerResult> list, Bitmap.Config argb_8888, final boolean b) {
        if (bitmap == null || bitmap.isRecycled() || list == null) {
            return bitmap;
        }
        if (argb_8888 == null) {
            argb_8888 = Bitmap.Config.ARGB_8888;
        }
        final TuSdkSize create = TuSdkSize.create(bitmap);
        final Bitmap bitmap2 = Bitmap.createBitmap(create.width, create.height, argb_8888);
        final Canvas canvas = new Canvas(bitmap2);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint)null);
        if (b) {
            BitmapHelper.recycled(bitmap);
        }
        final Iterator<StickerResult> iterator = list.iterator();
        while (iterator.hasNext()) {
            a(canvas, create, iterator.next());
        }
        canvas.save();
        canvas.restore();
        return bitmap2;
    }
    
    private static void a(final Canvas canvas, final TuSdkSize tuSdkSize, final StickerResult stickerResult) {
        if (canvas == null || stickerResult == null || stickerResult.item == null) {
            return;
        }
        final TuSdkSizeF create = TuSdkSizeF.create(tuSdkSize.width * stickerResult.center.width(), tuSdkSize.height * stickerResult.center.height());
        a(canvas, tuSdkSize, stickerResult, create);
        b(canvas, tuSdkSize, stickerResult, create);
    }
    
    private static void a(final Canvas canvas, final TuSdkSize tuSdkSize, final StickerResult stickerResult, final TuSdkSizeF tuSdkSizeF) {
        if (canvas == null || stickerResult == null || stickerResult.item == null) {
            return;
        }
        if (stickerResult.item.stickerId > 0L || stickerResult.item.getImage() == null) {
            StickerLocalPackage.shared().loadStickerItem(stickerResult.item);
        }
        if (stickerResult.item.getImage() == null) {
            return;
        }
        final Bitmap a = a(stickerResult.item.getImage(), tuSdkSize, tuSdkSizeF, stickerResult.degree);
        stickerResult.item.setImage(null);
        final RectF rectInParent = RectHelper.getRectInParent(tuSdkSize, TuSdkSize.create(a), stickerResult.center);
        if (rectInParent == null || a.isRecycled()) {
            return;
        }
        canvas.drawBitmap(a, rectInParent.left, rectInParent.top, (Paint)null);
        BitmapHelper.recycled(a);
    }
    
    private static Bitmap a(Bitmap bitmap, final TuSdkSize tuSdkSize, final TuSdkSizeF tuSdkSizeF, final float rotate) {
        if (bitmap == null || bitmap.isRecycled() || tuSdkSize == null || tuSdkSizeF == null) {
            return null;
        }
        final float n = tuSdkSizeF.width / bitmap.getWidth();
        final float n2 = tuSdkSizeF.height / bitmap.getHeight();
        final Matrix matrix = new Matrix();
        matrix.setRotate(rotate);
        matrix.preScale(n, n2);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return bitmap;
    }
    
    private static void b(final Canvas canvas, final TuSdkSize tuSdkSize, final StickerResult stickerResult, final TuSdkSizeF tuSdkSizeF) {
        if (stickerResult.item.getType() != StickerData.StickerType.TypeText || stickerResult.item.texts == null) {
            return;
        }
        final RectF rectInParent = RectHelper.getRectInParent(tuSdkSize, tuSdkSizeF.toSizeCeil(), stickerResult.center);
        final float max = Math.max(tuSdkSizeF.width / stickerResult.item.sizePixies().width, tuSdkSizeF.height / stickerResult.item.sizePixies().height);
        final Iterator<StickerText> iterator = stickerResult.item.texts.iterator();
        while (iterator.hasNext()) {
            a(canvas, rectInParent, iterator.next(), max, stickerResult);
        }
    }
    
    private static void a(final Canvas canvas, final RectF rectF, final StickerText stickerText, float n, final StickerResult stickerResult) {
        if (canvas == null || rectF == null || stickerText == null || stickerText.content == null) {
            return;
        }
        if (n <= 0.0f) {
            n = 1.0f;
        }
        final float n2 = TuSdkContext.sp2pxFloat(stickerText.textSize) * n;
        final RectF rectInParent = RectHelper.getRectInParent(rectF, stickerText.getRect());
        final TextPaint textPaint = new TextPaint(1);
        final ArrayList<String> list = new ArrayList<String>();
        float n3 = rectInParent.left;
        if (stickerText.getAlignment() == Paint.Align.CENTER) {
            n3 = rectInParent.centerX();
        }
        else if (stickerText.getAlignment() == Paint.Align.RIGHT) {
            n3 = rectInParent.right;
        }
        textPaint.setTextAlign(stickerText.getAlignment());
        if (stickerResult.text == null && stickerText.content != null) {
            canvas.save();
            final CharSequence ellipsize = TextUtils.ellipsize((CharSequence)stickerText.content, textPaint, rectInParent.width(), TextUtils.TruncateAt.END);
            textPaint.setTextSize(n2);
            textPaint.setColor(stickerText.getColor());
            textPaint.setShadowLayer(1.0f, 1.0f, 1.0f, stickerText.getShadowColor());
            canvas.drawText(ellipsize.toString(), n3, (float) FontUtils.computBaseline(textPaint.getFontMetricsInt(), rectInParent), (Paint)textPaint);
            canvas.restore();
            return;
        }
        if (stickerResult.text == null) {
            return;
        }
        final Iterator<String> iterator = stickerResult.text.iterator();
        while (iterator.hasNext()) {
            list.add(TextUtils.ellipsize((CharSequence)iterator.next(), textPaint, rectInParent.width(), TextUtils.TruncateAt.END).toString().trim().replaceAll("\\s+\\n", "\n"));
        }
        textPaint.setUnderlineText(stickerText.underline);
        canvas.save();
        canvas.rotate(stickerResult.degree, rectInParent.left + rectInParent.width() / 2.0f, rectInParent.top + rectInParent.height() / 2.0f);
        if (stickerText.backgroundColor != null) {
            textPaint.setColor(Color.parseColor(stickerText.backgroundColor));
            canvas.drawRect(new RectF(rectInParent.left - TuSdkContext.dip2px((float)stickerText.paddings), rectInParent.top - TuSdkContext.dip2px((float)stickerText.paddings), rectInParent.right + TuSdkContext.dip2px((float)stickerText.paddings), rectInParent.bottom + TuSdkContext.dip2px((float)stickerText.paddings)), (Paint)textPaint);
        }
        textPaint.setTextSize(n2);
        textPaint.setColor(stickerText.getColor());
        if (stickerText.shadowColor != null) {
            textPaint.setShadowLayer(2.0f, 3.0f, 2.0f, stickerText.getShadowColor());
        }
        final Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        final int size = list.size();
        final float n4 = size * (-fontMetrics.ascent + fontMetrics.descent);
        final float n5 = n4 / 2.0f - fontMetrics.descent;
        for (int i = 0; i < size; ++i) {
            canvas.drawText((String)list.get(i), n3, rectInParent.top + n4 / 2.0f + (-(size - i - 1) * (-fontMetrics.ascent + fontMetrics.descent) + n5), (Paint)textPaint);
        }
        canvas.restore();
    }
    
    public static Bitmap getBitmapForText(final View view) {
        view.buildDrawingCache(false);
        return BitmapHelper.changeAlpha(view.getDrawingCache(), view.getAlpha());
    }
    
    public static Bitmap createBitmapFromView(final View view, final int n) {
        final Bitmap bitmapForText = getBitmapForText(view);
        final Bitmap bitmap = Bitmap.createBitmap(bitmapForText.getWidth() + n * 2, bitmapForText.getHeight() + n * 2, Bitmap.Config.ARGB_8888);
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        final Matrix matrix = new Matrix();
        matrix.postTranslate((float)n, (float)n);
        new Canvas(bitmap).drawBitmap(bitmapForText, matrix, paint);
        BitmapHelper.recycled(bitmapForText);
        return bitmap;
    }
    
    public static int getBitmapSize(final Bitmap bitmap) {
        if (Build.VERSION.SDK_INT >= 19) {
            return bitmap.getAllocationByteCount();
        }
        if (Build.VERSION.SDK_INT >= 12) {
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
}
