package com.meishe.sdkdemo.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.media.ExifInterface;
import android.util.Log;

import com.meicam.sdk.NvsVideoResolution;
import com.meishe.sdkdemo.utils.dataInfo.TimelineData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;


public class ImageConverter {
    private static final String TAG = "Image Converter";
    private static int m_videoEditWidth = 1280;
    private static int m_videoEditHeight = 720;

    public static Bitmap convertImage(Context context, String srcImageFilePath) {
        if (srcImageFilePath == null)
            return null;
        BitmapFactory.Options opt = new BitmapFactory.Options();
        Bitmap bitmap = null;
        Matrix matrix = new Matrix();
        if (srcImageFilePath.contains("assets:/watermark/")) {
            bitmap = getBitmapFromAssetFile(context,srcImageFilePath);
        } else {
            opt.inJustDecodeBounds = true;
            // Determine source image size
            BitmapFactory.decodeFile(srcImageFilePath, opt);
            if (opt.outWidth == 0 || opt.outHeight == 0) {
                return null;
            }
            // Calculate image size to be converted
            Point convertedImageSize = calcConvertedImageSize(opt.outWidth, opt.outHeight);

            // Calculate in sample size for decoding
            opt.inSampleSize = calculateInSampleSize(opt, convertedImageSize.x, convertedImageSize.y);
            opt.inJustDecodeBounds = false;

            // Decode image file
            bitmap = BitmapFactory.decodeFile(srcImageFilePath, opt);
            if (bitmap == null) {
                return null;
            }
            int rotation = 0;
            if (opt.outMimeType.equals("image/jpeg")) {
                rotation = detectImageRotation(srcImageFilePath);
            }
            if (convertedImageSize.x != bitmap.getWidth() || convertedImageSize.y != bitmap.getHeight()) {
                final float scaleW = (float) convertedImageSize.x / bitmap.getWidth();
                final float scaleH = (float) convertedImageSize.y / bitmap.getHeight();
                matrix.postScale(scaleW, scaleH);
            }
            if (rotation != 0) {
                matrix.postRotate(rotation);
            }
        }
        if (bitmap == null) {
            return null;
        }
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    public static boolean convertImage(String srcImageFilePath, String dstImageFilePath, Context context) {
        Bitmap targetBitmap = convertImage(context, srcImageFilePath);
        if (targetBitmap == null)
            return false;
        final boolean succeeded = saveBitmap(targetBitmap, dstImageFilePath);
        if (!targetBitmap.isRecycled())
            targetBitmap.recycle();
        return succeeded;
    }

    private static Point calcConvertedImageSize(int orgImageWidth, int orgImageHeight) {
        int convertedImageWidth = orgImageWidth;
        int convertedImageHeight = orgImageHeight;
        int refWidth = m_videoEditWidth;
        int refHeight = m_videoEditHeight;
        NvsVideoResolution nvsVideoResolution = TimelineData.instance().getVideoResolution();
        if (nvsVideoResolution != null && nvsVideoResolution.imageWidth > 0 && nvsVideoResolution.imageHeight > 0) {
            refWidth = nvsVideoResolution.imageWidth;
            refHeight = nvsVideoResolution.imageHeight;
        }
        if (orgImageWidth >= orgImageHeight) {
            if (refWidth < refHeight) {
                int tmp = refWidth;
                refWidth = refHeight;
                refHeight = tmp;
            }
        } else {
            if (refWidth > refHeight) {
                int tmp = refWidth;
                refWidth = refHeight;
                refHeight = tmp;
            }
        }

        double scaleW = 1, scaleH = 1;
        if (orgImageWidth > refWidth)
            scaleW = (double) refWidth / orgImageWidth;
        if (orgImageHeight > refHeight)
            scaleH = (double) refHeight / orgImageHeight;
        if (scaleW < 1 || scaleH < 1) {
            final double scale = Math.min(scaleW, scaleH);
            convertedImageWidth = (int) (orgImageWidth * scale + 0.5);
            convertedImageHeight = (int) (orgImageHeight * scale + 0.5);
        }

        return new Point(convertedImageWidth, convertedImageHeight);
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth,
                                             int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth)
                inSampleSize *= 2;
        }

        return inSampleSize;
    }

    private static int detectImageRotation(String imageFilePath) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imageFilePath);
        } catch (Exception e) {
            e.printStackTrace();
            exif = null;
        }

        if (exif == null)
            return 0;

        final int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return 90;
            case ExifInterface.ORIENTATION_ROTATE_180:
                return 180;
            case ExifInterface.ORIENTATION_ROTATE_270:
                return 270;
            default:
                return 0;
        }
    }

    private static boolean saveBitmap(Bitmap bitmap, String targetFilePath) {
        File file = new File(targetFilePath);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
            fos.flush();
            fos.close();
            return true;
        } catch (Exception e) {
            Log.d(TAG, "" + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static Point getPicturePoint(String filePath, Context context) {
        if (filePath != null) {
            if (filePath.contains("assets:/watermark/")) {
                Bitmap bitmap = getBitmapFromAssetFile(context,filePath);
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                bitmap.recycle();
                return new Point(width, height);
            } else {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inJustDecodeBounds = true;
                // Determine source image size
                BitmapFactory.decodeFile(filePath, opt);
                if (opt.outWidth == 0 || opt.outHeight == 0)
                    return null;
                Point point = new Point(opt.outWidth, opt.outHeight);
                opt.inJustDecodeBounds = false;
                return point;
            }
        }
        return null;
    }

    private static Bitmap getBitmapFromAssetFile(Context context, String filePath) {
        if (filePath != null) {
            if (filePath.contains("assets:/watermark/")) {
                int lastIndex = filePath.lastIndexOf("/");
                String fileName = filePath.substring(lastIndex + 1, filePath.length());
                filePath = "watermark/" + fileName;
                InputStream open = null;
                AssetManager manager = context.getResources().getAssets();
                try {
                    open = manager.open(filePath); //得到输出流
                    return BitmapFactory.decodeStream(open); //得到每个图片
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "convertImage: !!!!!!!!!!!!!!!   " + e.getMessage());
                }
            }
        }
        return null;
    }
}

