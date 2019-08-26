// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image;

import java.io.OutputStream;
//import org.lasque.tusdk.core.secret.TuSdkImageNative;
import java.io.FileOutputStream;
import java.io.ByteArrayOutputStream;
//import org.lasque.tusdk.core.utils.hardware.TuSdkGPU;
//import org.lasque.tusdk.core.utils.RectHelper;
import java.io.IOException;
import android.media.ExifInterface;
import android.graphics.Matrix;
import android.annotation.TargetApi;
import android.graphics.Xfermode;
import android.graphics.PorterDuffXfermode;
import android.graphics.PorterDuff;
import java.io.ByteArrayInputStream;
//import org.lasque.tusdk.core.utils.FileHelper;
import java.io.FileNotFoundException;
//import org.lasque.tusdk.core.utils.TLog;
import android.graphics.Rect;
import java.io.FileInputStream;
import android.graphics.RectF;
//import org.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;
import android.graphics.drawable.Drawable;
import android.graphics.Paint;
import android.graphics.Canvas;
//import org.lasque.tusdk.core.utils.ContextUtils;
import java.io.InputStream;
//import org.lasque.tusdk.core.utils.AssetsHelper;
import android.content.Context;
//import org.lasque.tusdk.core.struct.TuSdkSize;
import java.io.File;
import android.graphics.Bitmap;
import android.os.Build;
import android.graphics.BitmapFactory;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.secret.TuSdkImageNative;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.AssetsHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ContextUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.FileHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.RectHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware.TuSdkGPU;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;

public class BitmapHelper
{
    public static BitmapFactory.Options getDefaultOptions() {
        return getDefaultOptions(false);
    }
    
    public static BitmapFactory.Options getDefaultOptions(final boolean b) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = false;
        options.inDither = false;
        if (Build.VERSION.SDK_INT < 21) {
            a(options);
        }
        options.inPreferredConfig = (b ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        return options;
    }
    
    private static void a(final BitmapFactory.Options options) {
        options.inPurgeable = true;
        options.inInputShareable = true;
    }
    
    public static TuSdkSize getBitmapSize(final File file) {
        if (file == null || !file.exists() || !file.isFile()) {
            return null;
        }
        final BitmapFactory.Options defaultOptions = getDefaultOptions();
        defaultOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), defaultOptions);
        return new TuSdkSize(defaultOptions.outWidth, defaultOptions.outHeight);
    }
    
    public static Bitmap getAssetsBitmap(final Context context, final String s) {
        final InputStream assetsStream = AssetsHelper.getAssetsStream(context, s);
        if (assetsStream == null) {
            return null;
        }
        return BitmapFactory.decodeStream(assetsStream);
    }
    
    public static Bitmap getRawBitmap(final Context context, final int n) {
        final InputStream rawStream = ContextUtils.getRawStream(context, n);
        if (rawStream == null) {
            return null;
        }
        return BitmapFactory.decodeStream(rawStream);
    }
    
    public static Bitmap getBitmapFormRaw(final Context context, final int n) {
        final InputStream rawStream = ContextUtils.getRawStream(context, n);
        if (rawStream == null) {
            return null;
        }
        final Bitmap decodeStream = BitmapFactory.decodeStream(rawStream);
        final Bitmap bitmap = Bitmap.createBitmap(decodeStream.getWidth(), decodeStream.getHeight(), Bitmap.Config.ARGB_8888);
        new Canvas(bitmap).drawBitmap(decodeStream, 0.0f, 0.0f, new Paint());
        return bitmap;
    }
    
    public static Drawable getResDrawable(final Context context, final int n) {
        if (context == null || n == 0) {
            return null;
        }
        final Drawable drawable = context.getResources().getDrawable(n);
        if (drawable != null) {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        }
        return drawable;
    }
    
    public static Bitmap getDrawableBitmap(final ImageView imageView) {
        if (imageView == null) {
            return null;
        }
        final Drawable drawable = imageView.getDrawable();
        if (drawable == null || !(drawable instanceof BitmapDrawable)) {
            return null;
        }
        return ((BitmapDrawable)drawable).getBitmap();
    }
    
    public static Bitmap changeColor(final Bitmap bitmap, final int color) {
        if (bitmap == null) {
            return null;
        }
        final Bitmap bitmap2 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap2);
        final Paint paint = new Paint();
        paint.setColor(color);
        canvas.drawBitmap(bitmap.extractAlpha(), 0.0f, 0.0f, paint);
        return bitmap2;
    }
    
    public static Bitmap changeAlpha(final Bitmap bitmap, final float n) {
        if (bitmap == null) {
            return null;
        }
        final Bitmap bitmap2 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap2);
        final Paint paint = new Paint();
        paint.setAlpha((int)(255.0f * n));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, paint);
        return bitmap2;
    }
    
    public static Bitmap getBitmap(final File file) {
        return getBitmap(file, true);
    }
    
    public static Bitmap getBitmap(final File file, final boolean b) {
        if (file == null || !file.exists()) {
            return null;
        }
        return decodeFileDescriptor(file, getDefaultOptions(b));
    }
    
    public static Bitmap getBitmap(final File file, final float n) {
        return getBitmap(file, n, false);
    }
    
    public static Bitmap getBitmap(final File file, final float n, final boolean b) {
        if (n >= 1.0f) {
            return getBitmap(file);
        }
        if (file == null || !file.exists() || n <= 0.0f) {
            return null;
        }
        final BitmapFactory.Options defaultOptions = getDefaultOptions(b);
        defaultOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), defaultOptions);
        final TuSdkSize tuSdkSize = new TuSdkSize((int)(defaultOptions.outWidth * n), (int)(defaultOptions.outHeight * n));
        defaultOptions.inSampleSize = a(defaultOptions, tuSdkSize, true);
        defaultOptions.inJustDecodeBounds = false;
        return imageResize(decodeFileDescriptor(file, defaultOptions), tuSdkSize, false);
    }
    
    private static int a(final BitmapFactory.Options options, final TuSdkSize tuSdkSize, final boolean b) {
        if (tuSdkSize == null) {
            return 1;
        }
        final TuSdkSize create = TuSdkSize.create(options.outWidth, options.outHeight);
        float n;
        if (b) {
            n = create.maxSide() / (float)tuSdkSize.maxSide();
        }
        else {
            n = create.minSide() / (float)tuSdkSize.maxSide();
        }
        int n2 = (int)Math.floor(n);
        if (n2 < 1) {
            n2 = 1;
        }
        return n2;
    }
    
    public static Bitmap getBitmap(final File file, final TuSdkSize tuSdkSize) {
        return getBitmap(file, tuSdkSize, false);
    }
    
    public static Bitmap getBitmap(final File file, final TuSdkSize tuSdkSize, final boolean b) {
        return getBitmap(file, tuSdkSize, 0, true, b);
    }
    
    public static Bitmap getBitmap(final ImageSqlInfo imageSqlInfo) {
        return getBitmap(imageSqlInfo, false);
    }
    
    public static Bitmap getBitmap(final ImageSqlInfo imageSqlInfo, final boolean b) {
        if (imageSqlInfo == null) {
            return null;
        }
        return imageRotaing(getBitmap(new File(imageSqlInfo.path), b), ImageOrientation.getValue(imageSqlInfo.orientation, false));
    }
    
    public static Bitmap getBitmap(final ImageSqlInfo imageSqlInfo, final int n) {
        return getBitmap(imageSqlInfo, new TuSdkSize(n, n), true);
    }
    
    public static Bitmap getBitmap(final ImageSqlInfo imageSqlInfo, final TuSdkSize tuSdkSize) {
        return getBitmap(imageSqlInfo, tuSdkSize, true);
    }
    
    public static Bitmap getBitmap(final ImageSqlInfo imageSqlInfo, final int n, final boolean b) {
        return getBitmap(imageSqlInfo, new TuSdkSize(n, n), b);
    }
    
    public static Bitmap getBitmap(final ImageSqlInfo imageSqlInfo, final TuSdkSize tuSdkSize, final boolean b) {
        if (imageSqlInfo == null || imageSqlInfo.path == null) {
            return null;
        }
        return getBitmap(new File(imageSqlInfo.path), tuSdkSize, imageSqlInfo.orientation, b);
    }
    
    public static Bitmap getBitmap(final File file, final TuSdkSize tuSdkSize, final int n, final boolean b) {
        return getBitmap(file, tuSdkSize, n, b, false);
    }
    
    public static Bitmap getBitmap(final ImageSqlInfo imageSqlInfo, final boolean b, final int n) {
        return getBitmap(imageSqlInfo, b, new TuSdkSize(n, n));
    }
    
    public static Bitmap getBitmap(final ImageSqlInfo imageSqlInfo, final boolean b, final TuSdkSize tuSdkSize) {
        return getBitmap(imageSqlInfo, b, tuSdkSize, true);
    }
    
    public static Bitmap getBitmap(final ImageSqlInfo imageSqlInfo, final boolean b, final TuSdkSize tuSdkSize, final boolean b2) {
        if (imageSqlInfo == null || imageSqlInfo.path == null) {
            return null;
        }
        return getBitmap(new File(imageSqlInfo.path), tuSdkSize, imageSqlInfo.orientation, b2, b);
    }
    
    public static Bitmap getBitmap(final File file, final TuSdkSize tuSdkSize, final int n, final boolean b, final boolean b2) {
        if (file == null || !file.exists()) {
            return null;
        }
        final BitmapFactory.Options defaultOptions = getDefaultOptions(b2);
        defaultOptions.inJustDecodeBounds = true;
        defaultOptions.inSampleSize = a(defaultOptions, tuSdkSize, b);
        defaultOptions.inJustDecodeBounds = false;
        return imageResize(decodeFileDescriptor(file, defaultOptions), tuSdkSize, b, ImageOrientation.getValue(n, false));
    }
    
    public static Bitmap createOvalImage(final int n, final int n2, final int color) {
        final Bitmap bitmap = Bitmap.createBitmap(n, n2, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        final RectF rectF = new RectF();
        rectF.left = 0.0f;
        rectF.top = 0.0f;
        rectF.right = (float)n;
        rectF.bottom = (float)n2;
        canvas.drawOval(rectF, paint);
        return bitmap;
    }
    
    public static Bitmap decodeFileDescriptor(final File file, final BitmapFactory.Options options) {
        if (file == null || !file.exists()) {
            return null;
        }
        InputStream inputStream = null;
        Bitmap decodeStream = null;
        try {
            inputStream = new FileInputStream(file);
            decodeStream = BitmapFactory.decodeStream(inputStream, (Rect)null, options);
        }
        catch (OutOfMemoryError outOfMemoryError) {
            TLog.e(outOfMemoryError, "decodeFileDescriptor: %s", outOfMemoryError.getMessage());
        }
        catch (FileNotFoundException ex) {
            TLog.e(ex, "decodeFileDescriptor: %s", ex.getMessage());
        }
        finally {
            FileHelper.safeClose(inputStream);
        }
        return decodeStream;
    }
    
    public static Bitmap imageDecode(final byte[] buf, final boolean b) {
        if (buf == null) {
            return null;
        }
        return BitmapFactory.decodeStream((InputStream)new ByteArrayInputStream(buf), (Rect)null, getDefaultOptions(b));
    }
    
    public static void recycled(final Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) {
            return;
        }
        bitmap.recycle();
    }
    
    public static Bitmap getRoundedCornerBitmap(final Bitmap bitmap, final int n) {
        final Bitmap bitmap2 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap2);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float n2 = (float)n;
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(-12434878);
        canvas.drawRoundRect(rectF, n2, n2, paint);
        paint.setXfermode((Xfermode)new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return bitmap2;
    }
    
    @TargetApi(26)
    public static Bitmap mergeAbove(Bitmap imageScale, final Bitmap bitmap) {
        if (imageScale == null || bitmap == null) {
            return null;
        }
        if (bitmap.getHeight() > imageScale.getHeight() && bitmap.getWidth() > imageScale.getWidth()) {
            imageScale = imageScale(imageScale, bitmap.getWidth(), bitmap.getHeight());
        }
        final int n = (bitmap.getHeight() - imageScale.getHeight()) / 2;
        final int n2 = (bitmap.getWidth() - imageScale.getWidth()) / 2;
        final Bitmap bitmap2 = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap2);
        canvas.drawBitmap(imageScale, (float)n2, (float)n, (Paint)null);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint)null);
        canvas.save();
        canvas.restore();
        return bitmap2;
    }
    
    public static Bitmap imageScale(final Bitmap bitmap, final int n, final int n2) {
        final int width = bitmap.getWidth();
        final int height = bitmap.getHeight();
        final float n3 = n / (float)width;
        final float n4 = n2 / (float)height;
        final Matrix matrix = new Matrix();
        matrix.postScale(n3, n4);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }
    
    public static Bitmap imageScale(final Bitmap bitmap, final float n) {
        if (bitmap == null || n == 1.0f || n <= 0.0f) {
            return bitmap;
        }
        final Matrix matrix = new Matrix();
        matrix.postScale(n, n);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }
    
    public static Bitmap imageLimit(Bitmap imageScale, final int n) {
        final TuSdkSize create = TuSdkSize.create(imageScale);
        if (create == null) {
            return imageScale;
        }
        if (n > 0) {
            imageScale = imageScale(imageScale, n / (float)create.maxSide());
        }
        return imageScale;
    }
    
    public static Bitmap imageRotaing(Bitmap bitmap, final ImageOrientation imageOrientation) {
        if (bitmap == null || imageOrientation == null || imageOrientation == ImageOrientation.Up) {
            return bitmap;
        }
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), a(imageOrientation, 1.0f), true);
        return bitmap;
    }
    
    public static ImageOrientation getImageOrientation(final String s) {
        final ExifInterface exifInterface = getExifInterface(s);
        ImageOrientation imageOrientation = ImageOrientation.Up;
        if (exifInterface != null) {
            imageOrientation = ImageOrientation.getValue(exifInterface.getAttributeInt("Orientation", 1));
        }
        return imageOrientation;
    }
    
    public static ExifInterface getExifInterface(final String s) {
        if (s == null) {
            return null;
        }
        try {
            return new ExifInterface(s);
        }
        catch (IOException ex) {
            TLog.e(ex);
            return null;
        }
    }
    
    public static Bitmap imageCorpResize(final Bitmap bitmap, final TuSdkSize tuSdkSize, final ImageOrientation imageOrientation, final boolean b) {
        return imageCorpResize(bitmap, tuSdkSize, (tuSdkSize != null) ? tuSdkSize.minMaxRatio() : 1.0f, imageOrientation, b);
    }
    
    public static Bitmap imageCorpResize(final Bitmap bitmap, final TuSdkSize tuSdkSize, float minMaxRatio, final ImageOrientation imageOrientation, final boolean b) {
        if (tuSdkSize != null && minMaxRatio == 0.0f) {
            minMaxRatio = tuSdkSize.minMaxRatio();
        }
        return imageResize(bitmap, tuSdkSize, false, minMaxRatio, b, imageOrientation);
    }
    
    public static Bitmap imageResize(final Bitmap bitmap, final TuSdkSize tuSdkSize) {
        return imageResize(bitmap, tuSdkSize, false);
    }
    
    public static Bitmap imageResize(final Bitmap bitmap, final TuSdkSize tuSdkSize, final boolean b) {
        return imageResize(bitmap, tuSdkSize, b, TuSdkSize.create(bitmap).minMaxRatio(), false);
    }
    
    public static Bitmap imageResize(final Bitmap bitmap, final TuSdkSize tuSdkSize, final boolean b, final float n, final boolean b2) {
        return imageResize(bitmap, tuSdkSize, b, n, b2, ImageOrientation.Up);
    }
    
    public static Bitmap imageResize(final Bitmap bitmap, final TuSdkSize tuSdkSize, final boolean b, final ImageOrientation imageOrientation) {
        if (bitmap == null) {
            return bitmap;
        }
        return imageResize(bitmap, tuSdkSize, b, TuSdkSize.create(bitmap).minMaxRatio(), false, imageOrientation);
    }
    
    public static Bitmap imageResize(final Bitmap bitmap, final TuSdkSize tuSdkSize, final boolean b, final float n, final boolean b2, final ImageOrientation imageOrientation) {
        return imageResize(bitmap, tuSdkSize, RectHelper.computerCenterRectF(TuSdkSize.create(bitmap), n), b, b2, imageOrientation);
    }
    
    public static Bitmap imageResize(Bitmap bitmap, final TuSdkSize tuSdkSize, final RectF rectF, final boolean b, final boolean b2, final ImageOrientation imageOrientation) {
        if (bitmap == null || bitmap.getWidth() < 1 || bitmap.getHeight() < 1) {
            return bitmap;
        }
        Rect fixedRectF = null;
        if (rectF != null) {
            fixedRectF = RectHelper.fixedRectF(TuSdkSize.create(bitmap), rectF);
        }
        if (fixedRectF == null || fixedRectF.width() < 1 || fixedRectF.height() < 1) {
            fixedRectF = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        }
        float a = a(bitmap, tuSdkSize, b);
        if (!b2 && a > 1.0f) {
            a = 1.0f;
        }
        final Matrix a2 = a(imageOrientation, a);
        if (fixedRectF.width() <= 0 || fixedRectF.height() <= 0) {
            TLog.e("Image width and height must be > 0", new Object[0]);
            return null;
        }
        try {
            bitmap = Bitmap.createBitmap(bitmap, fixedRectF.left, fixedRectF.top, fixedRectF.width(), fixedRectF.height(), a2, true);
        }
        catch (Exception ex) {
            TLog.e("create Bitmap failed,return orginal Bitmap", new Object[0]);
        }
        return bitmap;
    }
    
    private static float a(final Bitmap bitmap, final TuSdkSize tuSdkSize, final boolean b) {
        if (bitmap == null || tuSdkSize == null) {
            return 1.0f;
        }
        final TuSdkSize create = TuSdkSize.create(bitmap);
        final float n = tuSdkSize.maxSide() / (float)create.maxSide();
        final float n2 = tuSdkSize.minSide() / (float)create.minSide();
        float n3;
        if (b) {
            n3 = Math.min(n, n2);
        }
        else {
            n3 = Math.max(n, n2);
        }
        return n3;
    }
    
    public static TuSdkSize computerScaleSize(final Bitmap bitmap, final TuSdkSize tuSdkSize, final boolean b, final boolean b2) {
        if (bitmap == null || bitmap.getWidth() < 1 || bitmap.getHeight() < 1) {
            return null;
        }
        float a = a(bitmap, tuSdkSize, false);
        if (!b2 && a > 1.0f) {
            a = 1.0f;
        }
        return TuSdkSize.create((int)(bitmap.getWidth() * a), (int)(bitmap.getHeight() * a)).evenSize();
    }
    
    private static Matrix a(ImageOrientation up, final float n) {
        if (up == null) {
            up = ImageOrientation.Up;
        }
        final Matrix matrix = new Matrix();
        matrix.setRotate((float)up.getDegree());
        switch (up.ordinal()) {
            case 1:
            case 2: {
                matrix.preScale(-n, n);
                break;
            }
            case 3:
            case 4: {
                matrix.preScale(n, -n);
                break;
            }
            default: {
                matrix.preScale(n, n);
                break;
            }
        }
        return matrix;
    }
    
    public static Bitmap imageCorp(final Bitmap bitmap, final float n) {
        return imageCorpResize(bitmap, TuSdkSize.create(bitmap), n, ImageOrientation.Up, false);
    }
    
    public static Bitmap imageCorp(final Bitmap bitmap, final RectF rectF, final ImageOrientation imageOrientation) {
        if (rectF == null) {
            return bitmap;
        }
        return imageCorp(bitmap, rectF, null, imageOrientation);
    }
    
    public static Bitmap imageCorp(final Bitmap bitmap, final RectF rectF, TuSdkSize tuSdkSize, final ImageOrientation imageOrientation) {
        if (bitmap == null) {
            return null;
        }
        final RectF fixedCorpPrecentRect = RectHelper.fixedCorpPrecentRect(rectF, imageOrientation);
        if (fixedCorpPrecentRect == null) {
            return bitmap;
        }
        final TuSdkSize create = TuSdkSize.create(bitmap);
        final RectF rectF2 = new RectF(create.width * fixedCorpPrecentRect.left, create.height * fixedCorpPrecentRect.top, create.width * fixedCorpPrecentRect.right, create.height * fixedCorpPrecentRect.bottom);
        if (tuSdkSize != null) {
            final float n = tuSdkSize.width / (create.width * fixedCorpPrecentRect.width());
            tuSdkSize = new TuSdkSize((int)Math.ceil(create.width * n), (int)Math.ceil(create.height * n));
        }
        return imageResize(bitmap, tuSdkSize, rectF2, true, false, imageOrientation);
    }
    
    public static boolean saveBitmap(final File file, final Bitmap bitmap, final int n) {
        if (file == null || bitmap == null) {
            return false;
        }
        if (file.exists()) {
            file.delete();
        }
        if (TuSdkGPU.isSupporTurbo()) {
            return a(file, bitmap, n);
        }
        return b(file, bitmap, n);
    }
    
    public static byte[] bitmap2byteArrayTurbo(final Bitmap bitmap, final int n) {
        if (bitmap == null) {
            return null;
        }
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (!TuSdkGPU.isSupporTurbo()) {
            return bitmap2byteArray(bitmap, n);
        }
        if (a(byteArrayOutputStream, bitmap, n)) {
            return byteArrayOutputStream.toByteArray();
        }
        return null;
    }
    
    private static boolean a(final File file, final Bitmap bitmap, final int n) {
        boolean imageCompress = false;
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            imageCompress = TuSdkImageNative.imageCompress(bitmap, outputStream, n, true);
            outputStream.flush();
        }
        catch (FileNotFoundException ex) {
            TLog.e(ex, "File not found: %s", file.getPath());
        }
        catch (IOException ex2) {
            TLog.e(ex2, "Error accessing file: %s", file.getPath());
        }
        finally {
            FileHelper.safeClose(outputStream);
        }
        return imageCompress;
    }
    
    private static boolean a(final ByteArrayOutputStream byteArrayOutputStream, final Bitmap bitmap, final int n) {
        boolean imageCompress = false;
        try {
            imageCompress = TuSdkImageNative.imageCompress(bitmap, byteArrayOutputStream, n, true);
            byteArrayOutputStream.flush();
        }
        catch (IOException ex) {
            TLog.e(ex, "Error accessing outputStream", new Object[0]);
        }
        return imageCompress;
    }
    
    private static boolean b(final File file, final Bitmap bitmap, final int n) {
        return a(file, bitmap, n, Bitmap.CompressFormat.JPEG);
    }
    
    private static boolean a(final File file, final Bitmap bitmap, final int n, final Bitmap.CompressFormat compressFormat) {
        boolean compress = false;
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            compress = bitmap.compress(compressFormat, n, outputStream);
            outputStream.flush();
        }
        catch (FileNotFoundException ex) {
            TLog.e(ex, "File not found: %s", file.getPath());
        }
        catch (IOException ex2) {
            TLog.e(ex2, "Error accessing file: %s", file.getPath());
        }
        finally {
            FileHelper.safeClose(outputStream);
        }
        return compress;
    }
    
    public static boolean saveBitmapAsWebP(final File file, final Bitmap bitmap, final int n) {
        if (Build.VERSION.SDK_INT < 14) {
            return saveBitmap(file, bitmap, n);
        }
        return c(file, bitmap, n);
    }
    
    @TargetApi(14)
    private static boolean c(final File file, final Bitmap bitmap, final int n) {
        if (file == null || bitmap == null) {
            return false;
        }
        if (file.exists()) {
            file.delete();
        }
        boolean b = false;
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.WEBP, n, outputStream);
            outputStream.flush();
            b = true;
        }
        catch (FileNotFoundException ex) {
            TLog.e(ex, "File not found: %s", file.getPath());
        }
        catch (IOException ex2) {
            TLog.e(ex2, "Error accessing file: %s", file.getPath());
        }
        finally {
            FileHelper.safeClose(outputStream);
        }
        return b;
    }
    
    public static boolean saveBitmapAsPNG(final File file, final Bitmap bitmap, final int n) {
        return a(file, bitmap, n, Bitmap.CompressFormat.PNG);
    }
    
    public static InputStream bitmap2InputStream(final Bitmap bitmap, final int n) {
        return bitmap2InputStream(bitmap, n, null);
    }
    
    public static InputStream bitmap2InputStream(final Bitmap bitmap, final int n, final StringBuilder sb) {
        final byte[] bitmap2byteArray = bitmap2byteArray(bitmap, n);
        if (sb != null) {
            sb.append(FileHelper.toHexString(bitmap2byteArray));
        }
        return new ByteArrayInputStream(bitmap2byteArray);
    }
    
    public static byte[] bitmap2byteArray(final Bitmap bitmap, final int i) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, i, (OutputStream)byteArrayOutputStream);
        final byte[] byteArray = byteArrayOutputStream.toByteArray();
        try {
            byteArrayOutputStream.close();
        }
        catch (IOException ex) {
            TLog.e(ex, "bitmap2byteArray: %s", i);
        }
        return byteArray;
    }
    
    public static byte[] bitmap2byteArrayMaxByte(final Bitmap bitmap, final int n) {
        if (bitmap == null) {
            return null;
        }
        int n2 = 100;
        do {
            final byte[] bitmap2byteArray = bitmap2byteArray(bitmap, n2);
            n2 -= 5;
            if (bitmap2byteArray == null) {
                break;
            }
            if (bitmap2byteArray.length > n) {
                continue;
            }
            return bitmap2byteArray;
        } while (n > 0 && n2 > 0);
        return null;
    }
}
