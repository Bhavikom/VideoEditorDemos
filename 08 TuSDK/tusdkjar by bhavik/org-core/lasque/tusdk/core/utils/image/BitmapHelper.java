package org.lasque.tusdk.core.utils.image;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.os.Build.VERSION;
import android.widget.ImageView;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import org.lasque.tusdk.core.secret.TuSdkImageNative;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.AssetsHelper;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.FileHelper;
import org.lasque.tusdk.core.utils.RectHelper;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.hardware.TuSdkGPU;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;

public class BitmapHelper
{
  public static BitmapFactory.Options getDefaultOptions()
  {
    return getDefaultOptions(false);
  }
  
  public static BitmapFactory.Options getDefaultOptions(boolean paramBoolean)
  {
    BitmapFactory.Options localOptions = new BitmapFactory.Options();
    localOptions.inJustDecodeBounds = false;
    localOptions.inDither = false;
    if (Build.VERSION.SDK_INT < 21) {
      a(localOptions);
    }
    localOptions.inPreferredConfig = (paramBoolean ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
    return localOptions;
  }
  
  private static void a(BitmapFactory.Options paramOptions)
  {
    paramOptions.inPurgeable = true;
    paramOptions.inInputShareable = true;
  }
  
  public static TuSdkSize getBitmapSize(File paramFile)
  {
    if ((paramFile == null) || (!paramFile.exists()) || (!paramFile.isFile())) {
      return null;
    }
    BitmapFactory.Options localOptions = getDefaultOptions();
    localOptions.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(paramFile.getAbsolutePath(), localOptions);
    TuSdkSize localTuSdkSize = new TuSdkSize(localOptions.outWidth, localOptions.outHeight);
    return localTuSdkSize;
  }
  
  public static Bitmap getAssetsBitmap(Context paramContext, String paramString)
  {
    InputStream localInputStream = AssetsHelper.getAssetsStream(paramContext, paramString);
    if (localInputStream == null) {
      return null;
    }
    return BitmapFactory.decodeStream(localInputStream);
  }
  
  public static Bitmap getRawBitmap(Context paramContext, int paramInt)
  {
    InputStream localInputStream = ContextUtils.getRawStream(paramContext, paramInt);
    if (localInputStream == null) {
      return null;
    }
    return BitmapFactory.decodeStream(localInputStream);
  }
  
  public static Bitmap getBitmapFormRaw(Context paramContext, int paramInt)
  {
    InputStream localInputStream = ContextUtils.getRawStream(paramContext, paramInt);
    if (localInputStream == null) {
      return null;
    }
    Bitmap localBitmap1 = BitmapFactory.decodeStream(localInputStream);
    Bitmap localBitmap2 = Bitmap.createBitmap(localBitmap1.getWidth(), localBitmap1.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas localCanvas = new Canvas(localBitmap2);
    Paint localPaint = new Paint();
    localCanvas.drawBitmap(localBitmap1, 0.0F, 0.0F, localPaint);
    return localBitmap2;
  }
  
  public static Drawable getResDrawable(Context paramContext, int paramInt)
  {
    if ((paramContext == null) || (paramInt == 0)) {
      return null;
    }
    Drawable localDrawable = paramContext.getResources().getDrawable(paramInt);
    if (localDrawable != null) {
      localDrawable.setBounds(0, 0, localDrawable.getMinimumWidth(), localDrawable.getMinimumHeight());
    }
    return localDrawable;
  }
  
  public static Bitmap getDrawableBitmap(ImageView paramImageView)
  {
    if (paramImageView == null) {
      return null;
    }
    Drawable localDrawable = paramImageView.getDrawable();
    if ((localDrawable == null) || (!(localDrawable instanceof BitmapDrawable))) {
      return null;
    }
    return ((BitmapDrawable)localDrawable).getBitmap();
  }
  
  public static Bitmap changeColor(Bitmap paramBitmap, int paramInt)
  {
    if (paramBitmap == null) {
      return null;
    }
    Bitmap localBitmap1 = Bitmap.createBitmap(paramBitmap.getWidth(), paramBitmap.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas localCanvas = new Canvas(localBitmap1);
    Paint localPaint = new Paint();
    localPaint.setColor(paramInt);
    Bitmap localBitmap2 = paramBitmap.extractAlpha();
    localCanvas.drawBitmap(localBitmap2, 0.0F, 0.0F, localPaint);
    return localBitmap1;
  }
  
  public static Bitmap changeAlpha(Bitmap paramBitmap, float paramFloat)
  {
    if (paramBitmap == null) {
      return null;
    }
    Bitmap localBitmap = Bitmap.createBitmap(paramBitmap.getWidth(), paramBitmap.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas localCanvas = new Canvas(localBitmap);
    Paint localPaint = new Paint();
    localPaint.setAlpha((int)(255.0F * paramFloat));
    localCanvas.drawBitmap(paramBitmap, 0.0F, 0.0F, localPaint);
    return localBitmap;
  }
  
  public static Bitmap getBitmap(File paramFile)
  {
    return getBitmap(paramFile, true);
  }
  
  public static Bitmap getBitmap(File paramFile, boolean paramBoolean)
  {
    if ((paramFile == null) || (!paramFile.exists())) {
      return null;
    }
    BitmapFactory.Options localOptions = getDefaultOptions(paramBoolean);
    Bitmap localBitmap = decodeFileDescriptor(paramFile, localOptions);
    return localBitmap;
  }
  
  public static Bitmap getBitmap(File paramFile, float paramFloat)
  {
    return getBitmap(paramFile, paramFloat, false);
  }
  
  public static Bitmap getBitmap(File paramFile, float paramFloat, boolean paramBoolean)
  {
    if (paramFloat >= 1.0F) {
      return getBitmap(paramFile);
    }
    if ((paramFile == null) || (!paramFile.exists()) || (paramFloat <= 0.0F)) {
      return null;
    }
    BitmapFactory.Options localOptions = getDefaultOptions(paramBoolean);
    localOptions.inJustDecodeBounds = true;
    Bitmap localBitmap = BitmapFactory.decodeFile(paramFile.getAbsolutePath(), localOptions);
    TuSdkSize localTuSdkSize = new TuSdkSize((int)(localOptions.outWidth * paramFloat), (int)(localOptions.outHeight * paramFloat));
    localOptions.inSampleSize = a(localOptions, localTuSdkSize, true);
    localOptions.inJustDecodeBounds = false;
    localBitmap = decodeFileDescriptor(paramFile, localOptions);
    return imageResize(localBitmap, localTuSdkSize, false);
  }
  
  private static int a(BitmapFactory.Options paramOptions, TuSdkSize paramTuSdkSize, boolean paramBoolean)
  {
    if (paramTuSdkSize == null) {
      return 1;
    }
    TuSdkSize localTuSdkSize = TuSdkSize.create(paramOptions.outWidth, paramOptions.outHeight);
    float f = 1.0F;
    if (paramBoolean) {
      f = localTuSdkSize.maxSide() / paramTuSdkSize.maxSide();
    } else {
      f = localTuSdkSize.minSide() / paramTuSdkSize.maxSide();
    }
    int i = (int)Math.floor(f);
    if (i < 1) {
      i = 1;
    }
    return i;
  }
  
  public static Bitmap getBitmap(File paramFile, TuSdkSize paramTuSdkSize)
  {
    return getBitmap(paramFile, paramTuSdkSize, false);
  }
  
  public static Bitmap getBitmap(File paramFile, TuSdkSize paramTuSdkSize, boolean paramBoolean)
  {
    return getBitmap(paramFile, paramTuSdkSize, 0, true, paramBoolean);
  }
  
  public static Bitmap getBitmap(ImageSqlInfo paramImageSqlInfo)
  {
    return getBitmap(paramImageSqlInfo, false);
  }
  
  public static Bitmap getBitmap(ImageSqlInfo paramImageSqlInfo, boolean paramBoolean)
  {
    if (paramImageSqlInfo == null) {
      return null;
    }
    File localFile = new File(paramImageSqlInfo.path);
    Bitmap localBitmap = getBitmap(localFile, paramBoolean);
    localBitmap = imageRotaing(localBitmap, ImageOrientation.getValue(paramImageSqlInfo.orientation, false));
    return localBitmap;
  }
  
  public static Bitmap getBitmap(ImageSqlInfo paramImageSqlInfo, int paramInt)
  {
    return getBitmap(paramImageSqlInfo, new TuSdkSize(paramInt, paramInt), true);
  }
  
  public static Bitmap getBitmap(ImageSqlInfo paramImageSqlInfo, TuSdkSize paramTuSdkSize)
  {
    return getBitmap(paramImageSqlInfo, paramTuSdkSize, true);
  }
  
  public static Bitmap getBitmap(ImageSqlInfo paramImageSqlInfo, int paramInt, boolean paramBoolean)
  {
    return getBitmap(paramImageSqlInfo, new TuSdkSize(paramInt, paramInt), paramBoolean);
  }
  
  public static Bitmap getBitmap(ImageSqlInfo paramImageSqlInfo, TuSdkSize paramTuSdkSize, boolean paramBoolean)
  {
    if ((paramImageSqlInfo == null) || (paramImageSqlInfo.path == null)) {
      return null;
    }
    File localFile = new File(paramImageSqlInfo.path);
    return getBitmap(localFile, paramTuSdkSize, paramImageSqlInfo.orientation, paramBoolean);
  }
  
  public static Bitmap getBitmap(File paramFile, TuSdkSize paramTuSdkSize, int paramInt, boolean paramBoolean)
  {
    return getBitmap(paramFile, paramTuSdkSize, paramInt, paramBoolean, false);
  }
  
  public static Bitmap getBitmap(ImageSqlInfo paramImageSqlInfo, boolean paramBoolean, int paramInt)
  {
    return getBitmap(paramImageSqlInfo, paramBoolean, new TuSdkSize(paramInt, paramInt));
  }
  
  public static Bitmap getBitmap(ImageSqlInfo paramImageSqlInfo, boolean paramBoolean, TuSdkSize paramTuSdkSize)
  {
    return getBitmap(paramImageSqlInfo, paramBoolean, paramTuSdkSize, true);
  }
  
  public static Bitmap getBitmap(ImageSqlInfo paramImageSqlInfo, boolean paramBoolean1, TuSdkSize paramTuSdkSize, boolean paramBoolean2)
  {
    if ((paramImageSqlInfo == null) || (paramImageSqlInfo.path == null)) {
      return null;
    }
    File localFile = new File(paramImageSqlInfo.path);
    return getBitmap(localFile, paramTuSdkSize, paramImageSqlInfo.orientation, paramBoolean2, paramBoolean1);
  }
  
  public static Bitmap getBitmap(File paramFile, TuSdkSize paramTuSdkSize, int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    if ((paramFile == null) || (!paramFile.exists())) {
      return null;
    }
    BitmapFactory.Options localOptions = getDefaultOptions(paramBoolean2);
    localOptions.inJustDecodeBounds = true;
    localOptions.inSampleSize = a(localOptions, paramTuSdkSize, paramBoolean1);
    localOptions.inJustDecodeBounds = false;
    Bitmap localBitmap = decodeFileDescriptor(paramFile, localOptions);
    return imageResize(localBitmap, paramTuSdkSize, paramBoolean1, ImageOrientation.getValue(paramInt, false));
  }
  
  public static Bitmap createOvalImage(int paramInt1, int paramInt2, int paramInt3)
  {
    Bitmap localBitmap = Bitmap.createBitmap(paramInt1, paramInt2, Bitmap.Config.ARGB_8888);
    Canvas localCanvas = new Canvas(localBitmap);
    Paint localPaint = new Paint();
    localPaint.setAntiAlias(true);
    localCanvas.drawARGB(0, 0, 0, 0);
    localPaint.setColor(paramInt3);
    RectF localRectF = new RectF();
    localRectF.left = 0.0F;
    localRectF.top = 0.0F;
    localRectF.right = paramInt1;
    localRectF.bottom = paramInt2;
    localCanvas.drawOval(localRectF, localPaint);
    return localBitmap;
  }
  
  public static Bitmap decodeFileDescriptor(File paramFile, BitmapFactory.Options paramOptions)
  {
    if ((paramFile == null) || (!paramFile.exists())) {
      return null;
    }
    FileInputStream localFileInputStream = null;
    Bitmap localBitmap = null;
    try
    {
      localFileInputStream = new FileInputStream(paramFile);
      localBitmap = BitmapFactory.decodeStream(localFileInputStream, null, paramOptions);
    }
    catch (OutOfMemoryError localOutOfMemoryError)
    {
      TLog.e(localOutOfMemoryError, "decodeFileDescriptor: %s", new Object[] { localOutOfMemoryError.getMessage() });
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      TLog.e(localFileNotFoundException, "decodeFileDescriptor: %s", new Object[] { localFileNotFoundException.getMessage() });
    }
    finally
    {
      FileHelper.safeClose(localFileInputStream);
    }
    return localBitmap;
  }
  
  public static Bitmap imageDecode(byte[] paramArrayOfByte, boolean paramBoolean)
  {
    if (paramArrayOfByte == null) {
      return null;
    }
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(paramArrayOfByte);
    Bitmap localBitmap = BitmapFactory.decodeStream(localByteArrayInputStream, null, getDefaultOptions(paramBoolean));
    return localBitmap;
  }
  
  public static void recycled(Bitmap paramBitmap)
  {
    if ((paramBitmap == null) || (paramBitmap.isRecycled())) {
      return;
    }
    paramBitmap.recycle();
  }
  
  public static Bitmap getRoundedCornerBitmap(Bitmap paramBitmap, int paramInt)
  {
    Bitmap localBitmap = Bitmap.createBitmap(paramBitmap.getWidth(), paramBitmap.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas localCanvas = new Canvas(localBitmap);
    int i = -12434878;
    Paint localPaint = new Paint();
    Rect localRect = new Rect(0, 0, paramBitmap.getWidth(), paramBitmap.getHeight());
    RectF localRectF = new RectF(localRect);
    float f = paramInt;
    localPaint.setAntiAlias(true);
    localCanvas.drawARGB(0, 0, 0, 0);
    localPaint.setColor(-12434878);
    localCanvas.drawRoundRect(localRectF, f, f, localPaint);
    localPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    localCanvas.drawBitmap(paramBitmap, localRect, localRect, localPaint);
    return localBitmap;
  }
  
  @TargetApi(26)
  public static Bitmap mergeAbove(Bitmap paramBitmap1, Bitmap paramBitmap2)
  {
    if ((paramBitmap1 == null) || (paramBitmap2 == null)) {
      return null;
    }
    if ((paramBitmap2.getHeight() > paramBitmap1.getHeight()) && (paramBitmap2.getWidth() > paramBitmap1.getWidth())) {
      paramBitmap1 = imageScale(paramBitmap1, paramBitmap2.getWidth(), paramBitmap2.getHeight());
    }
    int i = (paramBitmap2.getHeight() - paramBitmap1.getHeight()) / 2;
    int j = (paramBitmap2.getWidth() - paramBitmap1.getWidth()) / 2;
    Bitmap localBitmap = Bitmap.createBitmap(paramBitmap2.getWidth(), paramBitmap2.getHeight(), Bitmap.Config.ARGB_8888);
    Canvas localCanvas = new Canvas(localBitmap);
    localCanvas.drawBitmap(paramBitmap1, j, i, null);
    localCanvas.drawBitmap(paramBitmap2, 0.0F, 0.0F, null);
    localCanvas.save();
    localCanvas.restore();
    return localBitmap;
  }
  
  public static Bitmap imageScale(Bitmap paramBitmap, int paramInt1, int paramInt2)
  {
    int i = paramBitmap.getWidth();
    int j = paramBitmap.getHeight();
    float f1 = paramInt1 / i;
    float f2 = paramInt2 / j;
    Matrix localMatrix = new Matrix();
    localMatrix.postScale(f1, f2);
    Bitmap localBitmap = Bitmap.createBitmap(paramBitmap, 0, 0, i, j, localMatrix, true);
    return localBitmap;
  }
  
  public static Bitmap imageScale(Bitmap paramBitmap, float paramFloat)
  {
    if ((paramBitmap == null) || (paramFloat == 1.0F) || (paramFloat <= 0.0F)) {
      return paramBitmap;
    }
    Matrix localMatrix = new Matrix();
    localMatrix.postScale(paramFloat, paramFloat);
    Bitmap localBitmap = Bitmap.createBitmap(paramBitmap, 0, 0, paramBitmap.getWidth(), paramBitmap.getHeight(), localMatrix, true);
    return localBitmap;
  }
  
  public static Bitmap imageLimit(Bitmap paramBitmap, int paramInt)
  {
    TuSdkSize localTuSdkSize = TuSdkSize.create(paramBitmap);
    if (localTuSdkSize == null) {
      return paramBitmap;
    }
    if (paramInt > 0) {
      paramBitmap = imageScale(paramBitmap, paramInt / localTuSdkSize.maxSide());
    }
    return paramBitmap;
  }
  
  public static Bitmap imageRotaing(Bitmap paramBitmap, ImageOrientation paramImageOrientation)
  {
    if ((paramBitmap == null) || (paramImageOrientation == null) || (paramImageOrientation == ImageOrientation.Up)) {
      return paramBitmap;
    }
    Matrix localMatrix = a(paramImageOrientation, 1.0F);
    paramBitmap = Bitmap.createBitmap(paramBitmap, 0, 0, paramBitmap.getWidth(), paramBitmap.getHeight(), localMatrix, true);
    return paramBitmap;
  }
  
  public static ImageOrientation getImageOrientation(String paramString)
  {
    ExifInterface localExifInterface = getExifInterface(paramString);
    ImageOrientation localImageOrientation = ImageOrientation.Up;
    if (localExifInterface != null)
    {
      int i = localExifInterface.getAttributeInt("Orientation", 1);
      localImageOrientation = ImageOrientation.getValue(i);
    }
    return localImageOrientation;
  }
  
  public static ExifInterface getExifInterface(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    try
    {
      ExifInterface localExifInterface = new ExifInterface(paramString);
      return localExifInterface;
    }
    catch (IOException localIOException)
    {
      TLog.e(localIOException);
    }
    return null;
  }
  
  public static Bitmap imageCorpResize(Bitmap paramBitmap, TuSdkSize paramTuSdkSize, ImageOrientation paramImageOrientation, boolean paramBoolean)
  {
    return imageCorpResize(paramBitmap, paramTuSdkSize, paramTuSdkSize != null ? paramTuSdkSize.minMaxRatio() : 1.0F, paramImageOrientation, paramBoolean);
  }
  
  public static Bitmap imageCorpResize(Bitmap paramBitmap, TuSdkSize paramTuSdkSize, float paramFloat, ImageOrientation paramImageOrientation, boolean paramBoolean)
  {
    if ((paramTuSdkSize != null) && (paramFloat == 0.0F)) {
      paramFloat = paramTuSdkSize.minMaxRatio();
    }
    return imageResize(paramBitmap, paramTuSdkSize, false, paramFloat, paramBoolean, paramImageOrientation);
  }
  
  public static Bitmap imageResize(Bitmap paramBitmap, TuSdkSize paramTuSdkSize)
  {
    return imageResize(paramBitmap, paramTuSdkSize, false);
  }
  
  public static Bitmap imageResize(Bitmap paramBitmap, TuSdkSize paramTuSdkSize, boolean paramBoolean)
  {
    return imageResize(paramBitmap, paramTuSdkSize, paramBoolean, TuSdkSize.create(paramBitmap).minMaxRatio(), false);
  }
  
  public static Bitmap imageResize(Bitmap paramBitmap, TuSdkSize paramTuSdkSize, boolean paramBoolean1, float paramFloat, boolean paramBoolean2)
  {
    return imageResize(paramBitmap, paramTuSdkSize, paramBoolean1, paramFloat, paramBoolean2, ImageOrientation.Up);
  }
  
  public static Bitmap imageResize(Bitmap paramBitmap, TuSdkSize paramTuSdkSize, boolean paramBoolean, ImageOrientation paramImageOrientation)
  {
    if (paramBitmap == null) {
      return paramBitmap;
    }
    return imageResize(paramBitmap, paramTuSdkSize, paramBoolean, TuSdkSize.create(paramBitmap).minMaxRatio(), false, paramImageOrientation);
  }
  
  public static Bitmap imageResize(Bitmap paramBitmap, TuSdkSize paramTuSdkSize, boolean paramBoolean1, float paramFloat, boolean paramBoolean2, ImageOrientation paramImageOrientation)
  {
    RectF localRectF = RectHelper.computerCenterRectF(TuSdkSize.create(paramBitmap), paramFloat);
    return imageResize(paramBitmap, paramTuSdkSize, localRectF, paramBoolean1, paramBoolean2, paramImageOrientation);
  }
  
  public static Bitmap imageResize(Bitmap paramBitmap, TuSdkSize paramTuSdkSize, RectF paramRectF, boolean paramBoolean1, boolean paramBoolean2, ImageOrientation paramImageOrientation)
  {
    if ((paramBitmap == null) || (paramBitmap.getWidth() < 1) || (paramBitmap.getHeight() < 1)) {
      return paramBitmap;
    }
    Rect localRect = null;
    if (paramRectF != null) {
      localRect = RectHelper.fixedRectF(TuSdkSize.create(paramBitmap), paramRectF);
    }
    if ((localRect == null) || (localRect.width() < 1) || (localRect.height() < 1)) {
      localRect = new Rect(0, 0, paramBitmap.getWidth(), paramBitmap.getHeight());
    }
    float f = a(paramBitmap, paramTuSdkSize, paramBoolean1);
    if ((!paramBoolean2) && (f > 1.0F)) {
      f = 1.0F;
    }
    Matrix localMatrix = a(paramImageOrientation, f);
    if ((localRect.width() <= 0) || (localRect.height() <= 0))
    {
      TLog.e("Image width and height must be > 0", new Object[0]);
      return null;
    }
    try
    {
      paramBitmap = Bitmap.createBitmap(paramBitmap, localRect.left, localRect.top, localRect.width(), localRect.height(), localMatrix, true);
    }
    catch (Exception localException)
    {
      TLog.e("create Bitmap failed,return orginal Bitmap", new Object[0]);
    }
    return paramBitmap;
  }
  
  private static float a(Bitmap paramBitmap, TuSdkSize paramTuSdkSize, boolean paramBoolean)
  {
    if ((paramBitmap == null) || (paramTuSdkSize == null)) {
      return 1.0F;
    }
    TuSdkSize localTuSdkSize = TuSdkSize.create(paramBitmap);
    float f1 = paramTuSdkSize.maxSide() / localTuSdkSize.maxSide();
    float f2 = paramTuSdkSize.minSide() / localTuSdkSize.minSide();
    float f3;
    if (paramBoolean) {
      f3 = Math.min(f1, f2);
    } else {
      f3 = Math.max(f1, f2);
    }
    return f3;
  }
  
  public static TuSdkSize computerScaleSize(Bitmap paramBitmap, TuSdkSize paramTuSdkSize, boolean paramBoolean1, boolean paramBoolean2)
  {
    if ((paramBitmap == null) || (paramBitmap.getWidth() < 1) || (paramBitmap.getHeight() < 1)) {
      return null;
    }
    float f = a(paramBitmap, paramTuSdkSize, false);
    if ((!paramBoolean2) && (f > 1.0F)) {
      f = 1.0F;
    }
    TuSdkSize localTuSdkSize = TuSdkSize.create((int)(paramBitmap.getWidth() * f), (int)(paramBitmap.getHeight() * f));
    localTuSdkSize = localTuSdkSize.evenSize();
    return localTuSdkSize;
  }
  
  private static Matrix a(ImageOrientation paramImageOrientation, float paramFloat)
  {
    if (paramImageOrientation == null) {
      paramImageOrientation = ImageOrientation.Up;
    }
    Matrix localMatrix = new Matrix();
    localMatrix.setRotate(paramImageOrientation.getDegree());
    switch (1.a[paramImageOrientation.ordinal()])
    {
    case 1: 
    case 2: 
      localMatrix.preScale(-paramFloat, paramFloat);
      break;
    case 3: 
    case 4: 
      localMatrix.preScale(paramFloat, -paramFloat);
      break;
    default: 
      localMatrix.preScale(paramFloat, paramFloat);
    }
    return localMatrix;
  }
  
  public static Bitmap imageCorp(Bitmap paramBitmap, float paramFloat)
  {
    return imageCorpResize(paramBitmap, TuSdkSize.create(paramBitmap), paramFloat, ImageOrientation.Up, false);
  }
  
  public static Bitmap imageCorp(Bitmap paramBitmap, RectF paramRectF, ImageOrientation paramImageOrientation)
  {
    if (paramRectF == null) {
      return paramBitmap;
    }
    return imageCorp(paramBitmap, paramRectF, null, paramImageOrientation);
  }
  
  public static Bitmap imageCorp(Bitmap paramBitmap, RectF paramRectF, TuSdkSize paramTuSdkSize, ImageOrientation paramImageOrientation)
  {
    if (paramBitmap == null) {
      return null;
    }
    RectF localRectF1 = RectHelper.fixedCorpPrecentRect(paramRectF, paramImageOrientation);
    if (localRectF1 == null) {
      return paramBitmap;
    }
    TuSdkSize localTuSdkSize = TuSdkSize.create(paramBitmap);
    RectF localRectF2 = new RectF(localTuSdkSize.width * localRectF1.left, localTuSdkSize.height * localRectF1.top, localTuSdkSize.width * localRectF1.right, localTuSdkSize.height * localRectF1.bottom);
    if (paramTuSdkSize != null)
    {
      float f = paramTuSdkSize.width / (localTuSdkSize.width * localRectF1.width());
      paramTuSdkSize = new TuSdkSize((int)Math.ceil(localTuSdkSize.width * f), (int)Math.ceil(localTuSdkSize.height * f));
    }
    return imageResize(paramBitmap, paramTuSdkSize, localRectF2, true, false, paramImageOrientation);
  }
  
  public static boolean saveBitmap(File paramFile, Bitmap paramBitmap, int paramInt)
  {
    if ((paramFile == null) || (paramBitmap == null)) {
      return false;
    }
    if (paramFile.exists()) {
      paramFile.delete();
    }
    if (TuSdkGPU.isSupporTurbo()) {
      return a(paramFile, paramBitmap, paramInt);
    }
    return b(paramFile, paramBitmap, paramInt);
  }
  
  public static byte[] bitmap2byteArrayTurbo(Bitmap paramBitmap, int paramInt)
  {
    if (paramBitmap == null) {
      return null;
    }
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    if (TuSdkGPU.isSupporTurbo())
    {
      boolean bool = a(localByteArrayOutputStream, paramBitmap, paramInt);
      if (bool) {
        return localByteArrayOutputStream.toByteArray();
      }
      return null;
    }
    return bitmap2byteArray(paramBitmap, paramInt);
  }
  
  private static boolean a(File paramFile, Bitmap paramBitmap, int paramInt)
  {
    boolean bool = false;
    FileOutputStream localFileOutputStream = null;
    try
    {
      localFileOutputStream = new FileOutputStream(paramFile);
      bool = TuSdkImageNative.imageCompress(paramBitmap, localFileOutputStream, paramInt, true);
      localFileOutputStream.flush();
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      TLog.e(localFileNotFoundException, "File not found: %s", new Object[] { paramFile.getPath() });
    }
    catch (IOException localIOException)
    {
      TLog.e(localIOException, "Error accessing file: %s", new Object[] { paramFile.getPath() });
    }
    finally
    {
      FileHelper.safeClose(localFileOutputStream);
    }
    return bool;
  }
  
  private static boolean a(ByteArrayOutputStream paramByteArrayOutputStream, Bitmap paramBitmap, int paramInt)
  {
    boolean bool = false;
    try
    {
      bool = TuSdkImageNative.imageCompress(paramBitmap, paramByteArrayOutputStream, paramInt, true);
      paramByteArrayOutputStream.flush();
    }
    catch (IOException localIOException)
    {
      TLog.e(localIOException, "Error accessing outputStream", new Object[0]);
    }
    return bool;
  }
  
  private static boolean b(File paramFile, Bitmap paramBitmap, int paramInt)
  {
    return a(paramFile, paramBitmap, paramInt, Bitmap.CompressFormat.JPEG);
  }
  
  private static boolean a(File paramFile, Bitmap paramBitmap, int paramInt, Bitmap.CompressFormat paramCompressFormat)
  {
    boolean bool = false;
    FileOutputStream localFileOutputStream = null;
    try
    {
      localFileOutputStream = new FileOutputStream(paramFile);
      bool = paramBitmap.compress(paramCompressFormat, paramInt, localFileOutputStream);
      localFileOutputStream.flush();
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      TLog.e(localFileNotFoundException, "File not found: %s", new Object[] { paramFile.getPath() });
    }
    catch (IOException localIOException)
    {
      TLog.e(localIOException, "Error accessing file: %s", new Object[] { paramFile.getPath() });
    }
    finally
    {
      FileHelper.safeClose(localFileOutputStream);
    }
    return bool;
  }
  
  public static boolean saveBitmapAsWebP(File paramFile, Bitmap paramBitmap, int paramInt)
  {
    if (Build.VERSION.SDK_INT < 14) {
      return saveBitmap(paramFile, paramBitmap, paramInt);
    }
    return c(paramFile, paramBitmap, paramInt);
  }
  
  @TargetApi(14)
  private static boolean c(File paramFile, Bitmap paramBitmap, int paramInt)
  {
    if ((paramFile == null) || (paramBitmap == null)) {
      return false;
    }
    if (paramFile.exists()) {
      paramFile.delete();
    }
    boolean bool = false;
    FileOutputStream localFileOutputStream = null;
    try
    {
      localFileOutputStream = new FileOutputStream(paramFile);
      paramBitmap.compress(Bitmap.CompressFormat.WEBP, paramInt, localFileOutputStream);
      localFileOutputStream.flush();
      bool = true;
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
      TLog.e(localFileNotFoundException, "File not found: %s", new Object[] { paramFile.getPath() });
    }
    catch (IOException localIOException)
    {
      TLog.e(localIOException, "Error accessing file: %s", new Object[] { paramFile.getPath() });
    }
    finally
    {
      FileHelper.safeClose(localFileOutputStream);
    }
    return bool;
  }
  
  public static boolean saveBitmapAsPNG(File paramFile, Bitmap paramBitmap, int paramInt)
  {
    return a(paramFile, paramBitmap, paramInt, Bitmap.CompressFormat.PNG);
  }
  
  public static InputStream bitmap2InputStream(Bitmap paramBitmap, int paramInt)
  {
    return bitmap2InputStream(paramBitmap, paramInt, null);
  }
  
  public static InputStream bitmap2InputStream(Bitmap paramBitmap, int paramInt, StringBuilder paramStringBuilder)
  {
    byte[] arrayOfByte = bitmap2byteArray(paramBitmap, paramInt);
    if (paramStringBuilder != null) {
      paramStringBuilder.append(FileHelper.toHexString(arrayOfByte));
    }
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte);
    return localByteArrayInputStream;
  }
  
  public static byte[] bitmap2byteArray(Bitmap paramBitmap, int paramInt)
  {
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    paramBitmap.compress(Bitmap.CompressFormat.JPEG, paramInt, localByteArrayOutputStream);
    byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
    try
    {
      localByteArrayOutputStream.close();
    }
    catch (IOException localIOException)
    {
      TLog.e(localIOException, "bitmap2byteArray: %s", new Object[] { Integer.valueOf(paramInt) });
    }
    return arrayOfByte;
  }
  
  public static byte[] bitmap2byteArrayMaxByte(Bitmap paramBitmap, int paramInt)
  {
    if (paramBitmap == null) {
      return null;
    }
    int i = 100;
    do
    {
      byte[] arrayOfByte = bitmap2byteArray(paramBitmap, i);
      i -= 5;
      if (arrayOfByte == null) {
        break;
      }
      if (arrayOfByte.length <= paramInt) {
        return arrayOfByte;
      }
    } while ((paramInt > 0) && (i > 0));
    return null;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\image\BitmapHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */