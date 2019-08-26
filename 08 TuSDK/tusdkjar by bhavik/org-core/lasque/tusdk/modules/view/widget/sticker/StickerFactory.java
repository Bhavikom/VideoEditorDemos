package org.lasque.tusdk.modules.view.widget.sticker;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.RectF;
import android.os.Build.VERSION;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.struct.TuSdkSizeF;
import org.lasque.tusdk.core.utils.FontUtils;
import org.lasque.tusdk.core.utils.RectHelper;
import org.lasque.tusdk.core.utils.image.BitmapHelper;

public class StickerFactory
{
  public static Bitmap megerStickers(Bitmap paramBitmap, List<StickerResult> paramList)
  {
    return megerStickers(paramBitmap, paramList, null, true);
  }
  
  public static Bitmap megerStickers(Bitmap paramBitmap, List<StickerResult> paramList, Bitmap.Config paramConfig, boolean paramBoolean)
  {
    if ((paramBitmap == null) || (paramBitmap.isRecycled()) || (paramList == null)) {
      return paramBitmap;
    }
    if (paramConfig == null) {
      paramConfig = Bitmap.Config.ARGB_8888;
    }
    TuSdkSize localTuSdkSize = TuSdkSize.create(paramBitmap);
    Bitmap localBitmap = Bitmap.createBitmap(localTuSdkSize.width, localTuSdkSize.height, paramConfig);
    Canvas localCanvas = new Canvas(localBitmap);
    localCanvas.drawBitmap(paramBitmap, 0.0F, 0.0F, null);
    if (paramBoolean) {
      BitmapHelper.recycled(paramBitmap);
    }
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      StickerResult localStickerResult = (StickerResult)localIterator.next();
      a(localCanvas, localTuSdkSize, localStickerResult);
    }
    localCanvas.save();
    localCanvas.restore();
    return localBitmap;
  }
  
  private static void a(Canvas paramCanvas, TuSdkSize paramTuSdkSize, StickerResult paramStickerResult)
  {
    if ((paramCanvas == null) || (paramStickerResult == null) || (paramStickerResult.item == null)) {
      return;
    }
    TuSdkSizeF localTuSdkSizeF = TuSdkSizeF.create(paramTuSdkSize.width * paramStickerResult.center.width(), paramTuSdkSize.height * paramStickerResult.center.height());
    a(paramCanvas, paramTuSdkSize, paramStickerResult, localTuSdkSizeF);
    b(paramCanvas, paramTuSdkSize, paramStickerResult, localTuSdkSizeF);
  }
  
  private static void a(Canvas paramCanvas, TuSdkSize paramTuSdkSize, StickerResult paramStickerResult, TuSdkSizeF paramTuSdkSizeF)
  {
    if ((paramCanvas == null) || (paramStickerResult == null) || (paramStickerResult.item == null)) {
      return;
    }
    if ((paramStickerResult.item.stickerId > 0L) || (paramStickerResult.item.getImage() == null)) {
      StickerLocalPackage.shared().loadStickerItem(paramStickerResult.item);
    }
    if (paramStickerResult.item.getImage() == null) {
      return;
    }
    Bitmap localBitmap = a(paramStickerResult.item.getImage(), paramTuSdkSize, paramTuSdkSizeF, paramStickerResult.degree);
    paramStickerResult.item.setImage(null);
    RectF localRectF = RectHelper.getRectInParent(paramTuSdkSize, TuSdkSize.create(localBitmap), paramStickerResult.center);
    if ((localRectF == null) || (localBitmap.isRecycled())) {
      return;
    }
    paramCanvas.drawBitmap(localBitmap, localRectF.left, localRectF.top, null);
    BitmapHelper.recycled(localBitmap);
    localBitmap = null;
  }
  
  private static Bitmap a(Bitmap paramBitmap, TuSdkSize paramTuSdkSize, TuSdkSizeF paramTuSdkSizeF, float paramFloat)
  {
    if ((paramBitmap == null) || (paramBitmap.isRecycled()) || (paramTuSdkSize == null) || (paramTuSdkSizeF == null)) {
      return null;
    }
    float f1 = paramTuSdkSizeF.width / paramBitmap.getWidth();
    float f2 = paramTuSdkSizeF.height / paramBitmap.getHeight();
    Matrix localMatrix = new Matrix();
    localMatrix.setRotate(paramFloat);
    localMatrix.preScale(f1, f2);
    paramBitmap = Bitmap.createBitmap(paramBitmap, 0, 0, paramBitmap.getWidth(), paramBitmap.getHeight(), localMatrix, true);
    return paramBitmap;
  }
  
  private static void b(Canvas paramCanvas, TuSdkSize paramTuSdkSize, StickerResult paramStickerResult, TuSdkSizeF paramTuSdkSizeF)
  {
    if ((paramStickerResult.item.getType() != StickerData.StickerType.TypeText) || (paramStickerResult.item.texts == null)) {
      return;
    }
    RectF localRectF = RectHelper.getRectInParent(paramTuSdkSize, paramTuSdkSizeF.toSizeCeil(), paramStickerResult.center);
    float f1 = paramTuSdkSizeF.width / paramStickerResult.item.sizePixies().width;
    float f2 = paramTuSdkSizeF.height / paramStickerResult.item.sizePixies().height;
    float f3 = Math.max(f1, f2);
    Iterator localIterator = paramStickerResult.item.texts.iterator();
    while (localIterator.hasNext())
    {
      StickerText localStickerText = (StickerText)localIterator.next();
      a(paramCanvas, localRectF, localStickerText, f3, paramStickerResult);
    }
  }
  
  private static void a(Canvas paramCanvas, RectF paramRectF, StickerText paramStickerText, float paramFloat, StickerResult paramStickerResult)
  {
    if ((paramCanvas == null) || (paramRectF == null) || (paramStickerText == null) || (paramStickerText.content == null)) {
      return;
    }
    if (paramFloat <= 0.0F) {
      paramFloat = 1.0F;
    }
    float f1 = TuSdkContext.sp2pxFloat(paramStickerText.textSize) * paramFloat;
    RectF localRectF = RectHelper.getRectInParent(paramRectF, paramStickerText.getRect());
    TextPaint localTextPaint = new TextPaint(1);
    ArrayList localArrayList = new ArrayList();
    float f2 = localRectF.left;
    if (paramStickerText.getAlignment() == Paint.Align.CENTER) {
      f2 = localRectF.centerX();
    } else if (paramStickerText.getAlignment() == Paint.Align.RIGHT) {
      f2 = localRectF.right;
    }
    localTextPaint.setTextAlign(paramStickerText.getAlignment());
    if ((paramStickerResult.text == null) && (paramStickerText.content != null))
    {
      paramCanvas.save();
      localObject = TextUtils.ellipsize(paramStickerText.content, localTextPaint, localRectF.width(), TextUtils.TruncateAt.END);
      localTextPaint.setTextSize(f1);
      localTextPaint.setColor(paramStickerText.getColor());
      localTextPaint.setShadowLayer(1.0F, 1.0F, 1.0F, paramStickerText.getShadowColor());
      int i = FontUtils.computBaseline(localTextPaint.getFontMetricsInt(), localRectF);
      paramCanvas.drawText(((CharSequence)localObject).toString(), f2, i, localTextPaint);
      paramCanvas.restore();
      return;
    }
    if (paramStickerResult.text == null) {
      return;
    }
    Object localObject = paramStickerResult.text.iterator();
    while (((Iterator)localObject).hasNext())
    {
      String str = (String)((Iterator)localObject).next();
      CharSequence localCharSequence = TextUtils.ellipsize(str, localTextPaint, localRectF.width(), TextUtils.TruncateAt.END);
      localArrayList.add(localCharSequence.toString().trim().replaceAll("\\s+\\n", "\n"));
    }
    localTextPaint.setUnderlineText(paramStickerText.underline);
    paramCanvas.save();
    paramCanvas.rotate(paramStickerResult.degree, localRectF.left + localRectF.width() / 2.0F, localRectF.top + localRectF.height() / 2.0F);
    if (paramStickerText.backgroundColor != null)
    {
      localTextPaint.setColor(Color.parseColor(paramStickerText.backgroundColor));
      localObject = new RectF(localRectF.left - TuSdkContext.dip2px(paramStickerText.paddings), localRectF.top - TuSdkContext.dip2px(paramStickerText.paddings), localRectF.right + TuSdkContext.dip2px(paramStickerText.paddings), localRectF.bottom + TuSdkContext.dip2px(paramStickerText.paddings));
      paramCanvas.drawRect((RectF)localObject, localTextPaint);
    }
    localTextPaint.setTextSize(f1);
    localTextPaint.setColor(paramStickerText.getColor());
    if (paramStickerText.shadowColor != null) {
      localTextPaint.setShadowLayer(2.0F, 3.0F, 2.0F, paramStickerText.getShadowColor());
    }
    localObject = localTextPaint.getFontMetrics();
    int j = localArrayList.size();
    float f3 = j * (-((Paint.FontMetrics)localObject).ascent + ((Paint.FontMetrics)localObject).descent);
    float f4 = f3 / 2.0F - ((Paint.FontMetrics)localObject).descent;
    for (int k = 0; k < j; k++)
    {
      float f5 = -(j - k - 1) * (-((Paint.FontMetrics)localObject).ascent + ((Paint.FontMetrics)localObject).descent) + f4;
      float f6 = localRectF.top + f3 / 2.0F + f5;
      paramCanvas.drawText((String)localArrayList.get(k), f2, f6, localTextPaint);
    }
    paramCanvas.restore();
  }
  
  public static Bitmap getBitmapForText(View paramView)
  {
    paramView.buildDrawingCache(false);
    Bitmap localBitmap1 = paramView.getDrawingCache();
    Bitmap localBitmap2 = BitmapHelper.changeAlpha(localBitmap1, paramView.getAlpha());
    return localBitmap2;
  }
  
  public static Bitmap createBitmapFromView(View paramView, int paramInt)
  {
    Bitmap localBitmap1 = getBitmapForText(paramView);
    int i = localBitmap1.getWidth();
    int j = localBitmap1.getHeight();
    Bitmap localBitmap2 = Bitmap.createBitmap(i + paramInt * 2, j + paramInt * 2, Bitmap.Config.ARGB_8888);
    Paint localPaint = new Paint();
    localPaint.setAntiAlias(true);
    Matrix localMatrix = new Matrix();
    localMatrix.postTranslate(paramInt, paramInt);
    Canvas localCanvas = new Canvas(localBitmap2);
    localCanvas.drawBitmap(localBitmap1, localMatrix, localPaint);
    BitmapHelper.recycled(localBitmap1);
    return localBitmap2;
  }
  
  public static int getBitmapSize(Bitmap paramBitmap)
  {
    if (Build.VERSION.SDK_INT >= 19) {
      return paramBitmap.getAllocationByteCount();
    }
    if (Build.VERSION.SDK_INT >= 12) {
      return paramBitmap.getByteCount();
    }
    return paramBitmap.getRowBytes() * paramBitmap.getHeight();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\view\widget\sticker\StickerFactory.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */