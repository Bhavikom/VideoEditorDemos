package org.lasque.tusdk.core.utils;

import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.struct.TuSdkSizeF;
import org.lasque.tusdk.core.utils.image.ImageOrientation;

public class RectHelper
{
  public static Rect computerCenter(TuSdkSize paramTuSdkSize1, TuSdkSize paramTuSdkSize2)
  {
    if (paramTuSdkSize2 == null) {
      return null;
    }
    RectF localRectF = computerCenterRectF(paramTuSdkSize1, paramTuSdkSize2.width / paramTuSdkSize2.height, true);
    return fixedRectF(paramTuSdkSize1, localRectF);
  }
  
  public static Rect computerCenter(TuSdkSize paramTuSdkSize, float paramFloat)
  {
    RectF localRectF = computerCenterRectF(paramTuSdkSize, paramFloat, true);
    return fixedRectF(paramTuSdkSize, localRectF);
  }
  
  public static RectF computerCenterRectF(TuSdkSize paramTuSdkSize, float paramFloat)
  {
    return computerCenterRectF(paramTuSdkSize, paramFloat, true);
  }
  
  public static RectF computerCenterRectF(TuSdkSize paramTuSdkSize, float paramFloat, boolean paramBoolean)
  {
    if (paramTuSdkSize == null) {
      return null;
    }
    RectF localRectF = new RectF(0.0F, 0.0F, paramTuSdkSize.width, paramTuSdkSize.height);
    if (paramFloat <= 0.0F) {
      return localRectF;
    }
    if (paramFloat == 1.0F)
    {
      localRectF.right = (localRectF.bottom = Math.min(paramTuSdkSize.width, paramTuSdkSize.height));
    }
    else
    {
      if ((paramFloat > 1.0F) && (!paramBoolean)) {
        paramFloat = 1.0F / paramFloat;
      }
      float f = paramTuSdkSize.minMaxRatio();
      int i = paramFloat < f ? 1 : 0;
      int j = paramTuSdkSize.width < paramTuSdkSize.height ? 1 : 0;
      float[][] arrayOfFloat1 = { { paramTuSdkSize.width, paramTuSdkSize.height / paramFloat }, { paramTuSdkSize.height * paramFloat, paramTuSdkSize.width } };
      float[][] arrayOfFloat2 = { { paramTuSdkSize.height, paramTuSdkSize.width / paramFloat }, { paramTuSdkSize.width * paramFloat, paramTuSdkSize.height } };
      localRectF.right = arrayOfFloat1[i][(1 - j)];
      localRectF.bottom = arrayOfFloat2[i][j];
    }
    localRectF.left = ((paramTuSdkSize.width - localRectF.width()) * 0.5F);
    localRectF.top = ((paramTuSdkSize.height - localRectF.height()) * 0.5F);
    localRectF.right += localRectF.left;
    localRectF.bottom += localRectF.top;
    return localRectF;
  }
  
  public static Rect fixedRectF(TuSdkSize paramTuSdkSize, RectF paramRectF)
  {
    if ((paramTuSdkSize == null) || (paramRectF == null)) {
      return null;
    }
    Rect localRect = new Rect();
    localRect.top = ((int)Math.floor(paramRectF.top));
    localRect.bottom = ((int)Math.floor(paramRectF.bottom));
    localRect.left = ((int)Math.floor(paramRectF.left));
    localRect.right = ((int)Math.floor(paramRectF.right));
    if (localRect.top < 0)
    {
      localRect.bottom -= localRect.top;
      localRect.top = 0;
    }
    if (localRect.left < 0)
    {
      localRect.right -= localRect.left;
      localRect.left = 0;
    }
    if (localRect.height() > paramTuSdkSize.height) {
      localRect.bottom = (localRect.top + paramTuSdkSize.height);
    }
    if (localRect.height() % 2 != 0) {
      localRect.bottom -= 1;
    }
    if (localRect.width() > paramTuSdkSize.width) {
      localRect.right = (localRect.left + paramTuSdkSize.width);
    }
    if (localRect.width() % 2 != 0) {
      localRect.right -= 1;
    }
    return localRect;
  }
  
  public static RectF fixedCorpPrecentRect(RectF paramRectF, ImageOrientation paramImageOrientation)
  {
    if (paramRectF == null) {
      return null;
    }
    if (paramRectF.right > 1.0F) {
      paramRectF.right = 1.0F;
    }
    if (paramRectF.bottom > 1.0F) {
      paramRectF.bottom = 1.0F;
    }
    if (paramRectF.left < 0.0F) {
      paramRectF.left = 0.0F;
    }
    if (paramRectF.top < 0.0F) {
      paramRectF.top = 0.0F;
    }
    if (paramRectF.width() > 1.0F) {
      paramRectF.left = (1.0F - paramRectF.right);
    }
    if (paramRectF.height() > 1.0F) {
      paramRectF.top = (1.0F - paramRectF.bottom);
    }
    if (paramImageOrientation == null) {
      return paramRectF;
    }
    RectF localRectF = new RectF(paramRectF);
    switch (1.a[paramImageOrientation.ordinal()])
    {
    case 1: 
      localRectF.left = (1.0F - paramRectF.right);
      localRectF.right = (localRectF.left + paramRectF.width());
      break;
    case 2: 
      localRectF.left = (1.0F - paramRectF.right);
      localRectF.right = (localRectF.left + paramRectF.width());
      localRectF.bottom = (1.0F - paramRectF.top);
      localRectF.top = (localRectF.bottom - paramRectF.height());
      break;
    case 3: 
      localRectF.bottom = (1.0F - paramRectF.top);
      localRectF.top = (localRectF.bottom - paramRectF.height());
      break;
    case 4: 
      localRectF.left = paramRectF.top;
      localRectF.right = (localRectF.left + paramRectF.height());
      localRectF.top = (1.0F - paramRectF.right);
      localRectF.bottom = (localRectF.top + paramRectF.width());
      break;
    case 5: 
      localRectF.left = paramRectF.top;
      localRectF.right = (localRectF.left + paramRectF.height());
      localRectF.top = paramRectF.left;
      localRectF.bottom = (localRectF.top + paramRectF.width());
      break;
    case 6: 
      localRectF.left = (1.0F - paramRectF.bottom);
      localRectF.right = (localRectF.left + paramRectF.height());
      localRectF.top = paramRectF.left;
      localRectF.bottom = (localRectF.top + paramRectF.width());
      break;
    case 7: 
      localRectF.left = (1.0F - paramRectF.bottom);
      localRectF.right = (localRectF.left + paramRectF.height());
      localRectF.top = (1.0F - paramRectF.right);
      localRectF.bottom = (localRectF.top + paramRectF.width());
      break;
    }
    return localRectF;
  }
  
  public static TuSdkSize computerOutSize(TuSdkSize paramTuSdkSize, float paramFloat, boolean paramBoolean)
  {
    float f = paramTuSdkSize.getRatioFloat();
    if (paramFloat == f) {
      return paramTuSdkSize;
    }
    TuSdkSize localTuSdkSize = TuSdkSize.create(paramTuSdkSize);
    if (paramBoolean)
    {
      if (f > paramFloat)
      {
        localTuSdkSize.height = paramTuSdkSize.height;
        localTuSdkSize.width = ((int)Math.floor(localTuSdkSize.height * paramFloat));
      }
      else
      {
        localTuSdkSize.width = paramTuSdkSize.width;
        localTuSdkSize.height = ((int)Math.floor(localTuSdkSize.width / paramFloat));
      }
    }
    else if (f > paramFloat)
    {
      localTuSdkSize.width = paramTuSdkSize.width;
      localTuSdkSize.height = ((int)Math.floor(localTuSdkSize.width / paramFloat));
    }
    else
    {
      localTuSdkSize.height = paramTuSdkSize.height;
      localTuSdkSize.width = ((int)Math.floor(localTuSdkSize.height * paramFloat));
    }
    return localTuSdkSize;
  }
  
  public static Rect computerOutCenter(Rect paramRect, float paramFloat, boolean paramBoolean)
  {
    Rect localRect = new Rect(paramRect);
    TuSdkSize localTuSdkSize = computerOutSize(TuSdkSize.create(paramRect), paramFloat, paramBoolean);
    localRect.left -= (localTuSdkSize.width - localRect.width()) / 2;
    localRect.right = (localRect.left + localTuSdkSize.width);
    localRect.top -= (localTuSdkSize.height - localRect.height()) / 2;
    localRect.bottom = (localRect.top + localTuSdkSize.height);
    return localRect;
  }
  
  public static float computerOutScale(Rect paramRect, float paramFloat, boolean paramBoolean)
  {
    Rect localRect = computerOutCenter(paramRect, paramFloat, paramBoolean);
    float f1 = localRect.width() / paramRect.width();
    float f2 = localRect.height() / paramRect.height();
    if (paramBoolean) {
      return Math.min(f1, f2);
    }
    return Math.max(f1, f2);
  }
  
  public static float computeAngle(PointF paramPointF1, PointF paramPointF2)
  {
    float f1 = getDistanceOfTwoPoints(paramPointF1, paramPointF2);
    float f2 = (float)Math.asin(Math.abs(paramPointF1.y - paramPointF2.y) / f1);
    float f3 = (float)(f2 * 180.0F / 3.141592653589793D);
    if ((paramPointF2.x - paramPointF1.x <= 0.0F) && (paramPointF2.y - paramPointF1.y >= 0.0F)) {
      f3 = 90.0F - f3;
    } else if ((paramPointF2.x - paramPointF1.x <= 0.0F) && (paramPointF2.y - paramPointF1.y <= 0.0F)) {
      f3 += 90.0F;
    } else if ((paramPointF2.x - paramPointF1.x >= 0.0F) && (paramPointF2.y - paramPointF1.y <= 0.0F)) {
      f3 = 270.0F - f3;
    } else if ((paramPointF2.x - paramPointF1.x >= 0.0F) && (paramPointF2.y - paramPointF1.y >= 0.0F)) {
      f3 += 270.0F;
    }
    f3 -= 235.0F;
    return f3;
  }
  
  public static float getDistanceOfTwoPoints(PointF paramPointF1, PointF paramPointF2)
  {
    return getDistanceOfTwoPoints(paramPointF1.x, paramPointF1.y, paramPointF2.x, paramPointF2.y);
  }
  
  public static float getDistanceOfTwoPoints(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4)
  {
    return (float)Math.sqrt(Math.pow(paramFloat1 - paramFloat3, 2.0D) + Math.pow(paramFloat2 - paramFloat4, 2.0D));
  }
  
  public static Rect computerMinMaxSideInRegionRatio(TuSdkSize paramTuSdkSize, float paramFloat)
  {
    if ((paramTuSdkSize == null) || (!paramTuSdkSize.isSize()) || (paramFloat <= 0.0F) || (paramFloat > 1.0F)) {
      return null;
    }
    TuSdkSize localTuSdkSize = TuSdkSize.create(paramTuSdkSize);
    if (paramTuSdkSize.maxSide() == paramTuSdkSize.height) {
      localTuSdkSize.width = ((int)(paramTuSdkSize.height * paramFloat));
    } else {
      localTuSdkSize.height = ((int)(paramTuSdkSize.width * paramFloat));
    }
    localTuSdkSize = localTuSdkSize.evenSize();
    Rect localRect = makeRectWithAspectRatioInsideRect(localTuSdkSize, new Rect(0, 0, paramTuSdkSize.width, paramTuSdkSize.height));
    return localRect;
  }
  
  public static RectF minEnclosingRectangle(PointF paramPointF, TuSdkSize paramTuSdkSize, float paramFloat)
  {
    PointF localPointF1 = new PointF(paramTuSdkSize.width * 0.5F, paramTuSdkSize.height * 0.5F);
    RectF localRectF = new RectF();
    PointF localPointF2 = new PointF();
    localPointF2.x = (-localPointF1.x);
    localPointF2.y = (-localPointF1.y);
    mergeEnclosingRectangle(localRectF, localPointF2, paramFloat);
    localPointF2.x = localPointF1.x;
    localPointF2.y = (-localPointF1.y);
    mergeEnclosingRectangle(localRectF, localPointF2, paramFloat);
    localPointF2.x = (-localPointF1.x);
    localPointF2.y = localPointF1.y;
    mergeEnclosingRectangle(localRectF, localPointF2, paramFloat);
    localPointF2.x = localPointF1.x;
    localPointF2.y = localPointF1.y;
    mergeEnclosingRectangle(localRectF, localPointF2, paramFloat);
    localRectF.left += paramPointF.x;
    localRectF.top += paramPointF.y;
    localRectF.right += paramPointF.x;
    localRectF.bottom += paramPointF.y;
    return localRectF;
  }
  
  public static void mergeEnclosingRectangle(RectF paramRectF, PointF paramPointF, float paramFloat)
  {
    PointF localPointF = rotationWithOrigin(paramPointF, paramFloat);
    paramRectF.left = Math.min(paramRectF.left, localPointF.x);
    paramRectF.right = Math.max(paramRectF.right, localPointF.x);
    paramRectF.top = Math.min(paramRectF.top, localPointF.y);
    paramRectF.bottom = Math.max(paramRectF.bottom, localPointF.y);
  }
  
  public static PointF rotationWithOrigin(PointF paramPointF, float paramFloat)
  {
    PointF localPointF = new PointF();
    double d = paramFloat * 3.141592653589793D / 180.0D;
    localPointF.x = ((float)(paramPointF.x * Math.cos(d) + paramPointF.y * Math.sin(d)));
    localPointF.y = ((float)(paramPointF.y * Math.cos(d) - paramPointF.x * Math.sin(d)));
    return localPointF;
  }
  
  public static Rect rotationWithRotation(Rect paramRect, TuSdkSize paramTuSdkSize, ImageOrientation paramImageOrientation)
  {
    if ((paramRect == null) || (paramTuSdkSize == null) || (paramImageOrientation == null) || (paramRect.width() <= 0) || (paramRect.height() <= 0) || (paramRect.right > paramTuSdkSize.width) || (paramRect.bottom > paramTuSdkSize.height)) {
      return paramRect;
    }
    Rect localRect = null;
    switch (1.a[paramImageOrientation.ordinal()])
    {
    case 1: 
      localRect = new Rect(paramTuSdkSize.width - paramRect.right, paramRect.top, paramTuSdkSize.width - paramRect.left, paramRect.bottom);
      break;
    case 3: 
      localRect = new Rect(paramRect.left, paramTuSdkSize.height - paramRect.bottom, paramRect.right, paramTuSdkSize.height - paramRect.top);
      break;
    case 6: 
      localRect = new Rect(paramTuSdkSize.height - paramRect.bottom, paramRect.left, paramTuSdkSize.height - paramRect.top, paramRect.right);
      break;
    case 4: 
      localRect = new Rect(paramRect.top, paramTuSdkSize.width - paramRect.right, paramRect.bottom, paramTuSdkSize.width - paramRect.left);
      break;
    case 5: 
      localRect = new Rect(paramRect.top, paramRect.left, paramRect.bottom, paramRect.right);
      break;
    case 7: 
      localRect = new Rect(paramTuSdkSize.height - paramRect.bottom, paramTuSdkSize.width - paramRect.right, paramTuSdkSize.height - paramRect.top, paramTuSdkSize.width - paramRect.left);
      break;
    case 2: 
      localRect = new Rect(paramTuSdkSize.width - paramRect.right, paramTuSdkSize.height - paramRect.bottom, paramTuSdkSize.width - paramRect.left, paramTuSdkSize.height - paramRect.top);
      break;
    case 8: 
    default: 
      localRect = new Rect(paramRect);
    }
    return localRect;
  }
  
  public static RectF rotationWithRotation(RectF paramRectF, ImageOrientation paramImageOrientation)
  {
    if ((paramRectF == null) || (paramImageOrientation == null)) {
      return paramRectF;
    }
    RectF localRectF = null;
    switch (1.a[paramImageOrientation.ordinal()])
    {
    case 1: 
      localRectF = new RectF(1.0F - paramRectF.right, paramRectF.top, 1.0F - paramRectF.left, paramRectF.bottom);
      break;
    case 3: 
      localRectF = new RectF(paramRectF.left, 1.0F - paramRectF.bottom, paramRectF.right, 1.0F - paramRectF.top);
      break;
    case 6: 
      localRectF = new RectF(1.0F - paramRectF.bottom, paramRectF.left, 1.0F - paramRectF.top, paramRectF.right);
      break;
    case 4: 
      localRectF = new RectF(paramRectF.top, 1.0F - paramRectF.right, paramRectF.bottom, 1.0F - paramRectF.left);
      break;
    case 5: 
      localRectF = new RectF(paramRectF.top, paramRectF.left, paramRectF.bottom, paramRectF.right);
      break;
    case 7: 
      localRectF = new RectF(1.0F - paramRectF.bottom, 1.0F - paramRectF.right, 1.0F - paramRectF.top, 1.0F - paramRectF.left);
      break;
    case 2: 
      localRectF = new RectF(1.0F - paramRectF.right, 1.0F - paramRectF.bottom, 1.0F - paramRectF.left, 1.0F - paramRectF.top);
      break;
    case 8: 
    default: 
      localRectF = new RectF(paramRectF);
    }
    return localRectF;
  }
  
  public static RectF getRectInParent(TuSdkSize paramTuSdkSize1, TuSdkSize paramTuSdkSize2, RectF paramRectF)
  {
    RectF localRectF = new RectF(0.0F, 0.0F, paramTuSdkSize1.width, paramTuSdkSize1.height);
    if ((paramTuSdkSize2 == null) || (paramRectF == null) || (paramTuSdkSize1 == null)) {
      return localRectF;
    }
    localRectF.left = (paramTuSdkSize1.width * paramRectF.left - paramTuSdkSize2.width * 0.5F);
    localRectF.top = (paramTuSdkSize1.height * paramRectF.top - paramTuSdkSize2.height * 0.5F);
    localRectF.right = (localRectF.left + paramTuSdkSize1.width * paramRectF.width());
    localRectF.bottom = (localRectF.top + paramTuSdkSize1.height * paramRectF.height());
    return localRectF;
  }
  
  public static RectF getRectInParent(RectF paramRectF1, RectF paramRectF2)
  {
    if (paramRectF1 == null) {
      return null;
    }
    RectF localRectF = new RectF(paramRectF1.left, paramRectF1.top, paramRectF1.right, paramRectF1.bottom);
    if (paramRectF2 == null) {
      return localRectF;
    }
    localRectF.left += paramRectF1.width() * paramRectF2.left;
    localRectF.top += paramRectF1.height() * paramRectF2.top;
    localRectF.right = (localRectF.left + paramRectF1.width() * paramRectF2.width());
    localRectF.bottom = (localRectF.top + paramRectF1.height() * paramRectF2.height());
    return localRectF;
  }
  
  public static Rect makeRectWithAspectRatioInsideRect(TuSdkSize paramTuSdkSize, Rect paramRect)
  {
    if ((paramTuSdkSize == null) || (paramRect == null)) {
      return null;
    }
    TuSdkSize localTuSdkSize = new TuSdkSize();
    localTuSdkSize.width = paramRect.width();
    localTuSdkSize.height = ((int)Math.floor(localTuSdkSize.width / paramTuSdkSize.getRatioFloat()));
    if (localTuSdkSize.height > paramRect.height())
    {
      localTuSdkSize.height = paramRect.height();
      localTuSdkSize.width = ((int)Math.floor(localTuSdkSize.height * paramTuSdkSize.getRatioFloat()));
    }
    Rect localRect = new Rect(paramRect);
    paramRect.left += (paramRect.width() - localTuSdkSize.width) / 2;
    localRect.right = (localRect.left + localTuSdkSize.width);
    paramRect.top += (paramRect.height() - localTuSdkSize.height) / 2;
    localRect.bottom = (localRect.top + localTuSdkSize.height);
    return localRect;
  }
  
  public static RectF makeRectWithAspectRatioOutsideRect(TuSdkSize paramTuSdkSize, RectF paramRectF)
  {
    if ((paramTuSdkSize == null) || (paramRectF == null)) {
      return null;
    }
    TuSdkSizeF localTuSdkSizeF = new TuSdkSizeF();
    localTuSdkSizeF.width = paramRectF.width();
    localTuSdkSizeF.height = ((int)Math.floor(localTuSdkSizeF.width / paramTuSdkSize.getRatioFloat()));
    if (localTuSdkSizeF.height < paramRectF.height())
    {
      localTuSdkSizeF.height = paramRectF.height();
      localTuSdkSizeF.width = ((int)Math.floor(localTuSdkSizeF.height * paramTuSdkSize.getRatioFloat()));
    }
    RectF localRectF = new RectF(paramRectF);
    localRectF.left = ((localTuSdkSizeF.width - paramRectF.width()) / 2.0F - paramRectF.left);
    localRectF.right = (localRectF.left + localTuSdkSizeF.width);
    localRectF.top = ((localTuSdkSizeF.height - paramRectF.height()) / 2.0F - paramRectF.top);
    localRectF.bottom = (localRectF.top + localTuSdkSizeF.height);
    return localRectF;
  }
  
  public static double computerPotintDistance(Point paramPoint1, Point paramPoint2)
  {
    if ((paramPoint1 == null) || (paramPoint2 == null)) {
      return 0.0D;
    }
    float f1 = paramPoint1.x - paramPoint2.x;
    float f2 = paramPoint1.y - paramPoint2.y;
    return Math.sqrt(f1 * f1 + f2 * f2);
  }
  
  public static double computerPotintDistance(PointF paramPointF1, PointF paramPointF2)
  {
    if ((paramPointF1 == null) || (paramPointF2 == null)) {
      return 0.0D;
    }
    float f1 = paramPointF1.x - paramPointF2.x;
    float f2 = paramPointF1.y - paramPointF2.y;
    return Math.sqrt(f1 * f1 + f2 * f2);
  }
  
  public static float[] textureCoordinates(ImageOrientation paramImageOrientation, RectF paramRectF)
  {
    if ((paramRectF == null) || (paramImageOrientation == null)) {
      return new float[] { 0.0F, 0.0F, 1.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F };
    }
    paramRectF = rotationWithRotation(paramRectF, paramImageOrientation);
    float[] arrayOfFloat = new float[8];
    arrayOfFloat[0] = paramRectF.left;
    arrayOfFloat[1] = paramRectF.top;
    arrayOfFloat[2] = paramRectF.right;
    arrayOfFloat[3] = arrayOfFloat[1];
    arrayOfFloat[4] = arrayOfFloat[0];
    arrayOfFloat[5] = paramRectF.bottom;
    arrayOfFloat[6] = arrayOfFloat[2];
    arrayOfFloat[7] = arrayOfFloat[5];
    return a(paramImageOrientation, arrayOfFloat);
  }
  
  public static float[] displayCoordinates(ImageOrientation paramImageOrientation, RectF paramRectF)
  {
    if ((paramRectF == null) || (paramImageOrientation == null)) {
      return new float[] { 0.0F, 1.0F, 1.0F, 1.0F, 0.0F, 0.0F, 1.0F, 0.0F };
    }
    paramRectF = rotationWithRotation(paramRectF, paramImageOrientation);
    float[] arrayOfFloat = new float[8];
    arrayOfFloat[0] = paramRectF.left;
    arrayOfFloat[1] = paramRectF.bottom;
    arrayOfFloat[2] = paramRectF.right;
    arrayOfFloat[3] = arrayOfFloat[1];
    arrayOfFloat[4] = arrayOfFloat[0];
    arrayOfFloat[5] = paramRectF.top;
    arrayOfFloat[6] = arrayOfFloat[2];
    arrayOfFloat[7] = arrayOfFloat[5];
    return a(paramImageOrientation, arrayOfFloat);
  }
  
  public static float[] textureVertices(ImageOrientation paramImageOrientation, RectF paramRectF)
  {
    if ((paramRectF == null) || (paramImageOrientation == null)) {
      return new float[] { -1.0F, -1.0F, 1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F };
    }
    paramRectF = rotationWithRotation(paramRectF, paramImageOrientation);
    float[] arrayOfFloat = new float[8];
    arrayOfFloat[0] = (paramRectF.left * 2.0F - 1.0F);
    arrayOfFloat[1] = (paramRectF.top * 2.0F - 1.0F);
    arrayOfFloat[2] = (paramRectF.right * 2.0F - 1.0F);
    arrayOfFloat[3] = arrayOfFloat[1];
    arrayOfFloat[4] = arrayOfFloat[0];
    arrayOfFloat[5] = (paramRectF.bottom * 2.0F - 1.0F);
    arrayOfFloat[6] = arrayOfFloat[2];
    arrayOfFloat[7] = arrayOfFloat[5];
    return arrayOfFloat;
  }
  
  public static float[] displayVertices(ImageOrientation paramImageOrientation, RectF paramRectF)
  {
    if ((paramRectF == null) || (paramImageOrientation == null)) {
      return new float[] { -1.0F, -1.0F, 1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F };
    }
    paramRectF = rotationWithRotation(paramRectF, paramImageOrientation);
    float[] arrayOfFloat = new float[8];
    arrayOfFloat[0] = (paramRectF.left * 2.0F - 1.0F);
    arrayOfFloat[1] = (1.0F - paramRectF.bottom * 2.0F);
    arrayOfFloat[2] = (paramRectF.right * 2.0F - 1.0F);
    arrayOfFloat[3] = arrayOfFloat[1];
    arrayOfFloat[4] = arrayOfFloat[0];
    arrayOfFloat[5] = (1.0F - paramRectF.top * 2.0F);
    arrayOfFloat[6] = arrayOfFloat[2];
    arrayOfFloat[7] = arrayOfFloat[5];
    return arrayOfFloat;
  }
  
  private static float[] a(ImageOrientation paramImageOrientation, float[] paramArrayOfFloat)
  {
    if ((paramImageOrientation == null) || (paramArrayOfFloat == null)) {
      return paramArrayOfFloat;
    }
    switch (1.a[paramImageOrientation.ordinal()])
    {
    case 1: 
      return new float[] { paramArrayOfFloat[2], paramArrayOfFloat[3], paramArrayOfFloat[0], paramArrayOfFloat[1], paramArrayOfFloat[6], paramArrayOfFloat[7], paramArrayOfFloat[4], paramArrayOfFloat[5] };
    case 3: 
      return new float[] { paramArrayOfFloat[4], paramArrayOfFloat[5], paramArrayOfFloat[6], paramArrayOfFloat[7], paramArrayOfFloat[0], paramArrayOfFloat[1], paramArrayOfFloat[2], paramArrayOfFloat[3] };
    case 6: 
      return new float[] { paramArrayOfFloat[2], paramArrayOfFloat[3], paramArrayOfFloat[6], paramArrayOfFloat[7], paramArrayOfFloat[0], paramArrayOfFloat[1], paramArrayOfFloat[4], paramArrayOfFloat[5] };
    case 4: 
      return new float[] { paramArrayOfFloat[4], paramArrayOfFloat[5], paramArrayOfFloat[0], paramArrayOfFloat[1], paramArrayOfFloat[6], paramArrayOfFloat[7], paramArrayOfFloat[2], paramArrayOfFloat[3] };
    case 5: 
      return new float[] { paramArrayOfFloat[0], paramArrayOfFloat[1], paramArrayOfFloat[4], paramArrayOfFloat[5], paramArrayOfFloat[2], paramArrayOfFloat[3], paramArrayOfFloat[6], paramArrayOfFloat[7] };
    case 7: 
      return new float[] { paramArrayOfFloat[6], paramArrayOfFloat[7], paramArrayOfFloat[2], paramArrayOfFloat[3], paramArrayOfFloat[4], paramArrayOfFloat[5], paramArrayOfFloat[0], paramArrayOfFloat[1] };
    case 2: 
      return new float[] { paramArrayOfFloat[6], paramArrayOfFloat[7], paramArrayOfFloat[4], paramArrayOfFloat[5], paramArrayOfFloat[2], paramArrayOfFloat[3], paramArrayOfFloat[0], paramArrayOfFloat[1] };
    }
    return paramArrayOfFloat;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\RectHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */