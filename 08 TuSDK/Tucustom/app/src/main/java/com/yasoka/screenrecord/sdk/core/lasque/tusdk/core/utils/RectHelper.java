// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils;

import android.graphics.Point;
//import org.lasque.tusdk.core.struct.TuSdkSizeF;
import android.graphics.PointF;
//import org.lasque.tusdk.core.utils.image.ImageOrientation;
import android.graphics.RectF;
import android.graphics.Rect;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSize;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.struct.TuSdkSizeF;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.image.ImageOrientation;
//import org.lasque.tusdk.core.struct.TuSdkSize;

public class RectHelper
{
    public static Rect computerCenter(final TuSdkSize tuSdkSize, final TuSdkSize tuSdkSize2) {
        if (tuSdkSize2 == null) {
            return null;
        }
        return fixedRectF(tuSdkSize, computerCenterRectF(tuSdkSize, tuSdkSize2.width / (float)tuSdkSize2.height, true));
    }
    
    public static Rect computerCenter(final TuSdkSize tuSdkSize, final float n) {
        return fixedRectF(tuSdkSize, computerCenterRectF(tuSdkSize, n, true));
    }
    
    public static RectF computerCenterRectF(final TuSdkSize tuSdkSize, final float n) {
        return computerCenterRectF(tuSdkSize, n, true);
    }
    
    public static RectF computerCenterRectF(final TuSdkSize tuSdkSize, float n, final boolean b) {
        if (tuSdkSize == null) {
            return null;
        }
        final RectF rectF = new RectF(0.0f, 0.0f, (float)tuSdkSize.width, (float)tuSdkSize.height);
        if (n <= 0.0f) {
            return rectF;
        }
        if (n == 1.0f) {
            final RectF rectF2 = rectF;
            final RectF rectF3 = rectF;
            final float n2 = (float)Math.min(tuSdkSize.width, tuSdkSize.height);
            rectF3.bottom = n2;
            rectF2.right = n2;
        }
        else {
            if (n > 1.0f && !b) {
                n = 1.0f / n;
            }
            final int n3 = (n < tuSdkSize.minMaxRatio()) ? 1 : 0;
            final int n4 = (tuSdkSize.width < tuSdkSize.height) ? 1 : 0;
            final float[][] array = { { (float)tuSdkSize.width, tuSdkSize.height / n }, { tuSdkSize.height * n, (float)tuSdkSize.width } };
            final float[][] array2 = { { (float)tuSdkSize.height, tuSdkSize.width / n }, { tuSdkSize.width * n, (float)tuSdkSize.height } };
            rectF.right = array[n3][1 - n4];
            rectF.bottom = array2[n3][n4];
        }
        rectF.left = (tuSdkSize.width - rectF.width()) * 0.5f;
        rectF.top = (tuSdkSize.height - rectF.height()) * 0.5f;
        final RectF rectF4 = rectF;
        rectF4.right += rectF.left;
        final RectF rectF5 = rectF;
        rectF5.bottom += rectF.top;
        return rectF;
    }
    
    public static Rect fixedRectF(final TuSdkSize tuSdkSize, final RectF rectF) {
        if (tuSdkSize == null || rectF == null) {
            return null;
        }
        final Rect rect = new Rect();
        rect.top = (int)Math.floor(rectF.top);
        rect.bottom = (int)Math.floor(rectF.bottom);
        rect.left = (int)Math.floor(rectF.left);
        rect.right = (int)Math.floor(rectF.right);
        if (rect.top < 0) {
            final Rect rect2 = rect;
            rect2.bottom -= rect.top;
            rect.top = 0;
        }
        if (rect.left < 0) {
            final Rect rect3 = rect;
            rect3.right -= rect.left;
            rect.left = 0;
        }
        if (rect.height() > tuSdkSize.height) {
            rect.bottom = rect.top + tuSdkSize.height;
        }
        if (rect.height() % 2 != 0) {
            final Rect rect4 = rect;
            --rect4.bottom;
        }
        if (rect.width() > tuSdkSize.width) {
            rect.right = rect.left + tuSdkSize.width;
        }
        if (rect.width() % 2 != 0) {
            final Rect rect5 = rect;
            --rect5.right;
        }
        return rect;
    }
    
    public static RectF fixedCorpPrecentRect(final RectF rectF, final ImageOrientation imageOrientation) {
        if (rectF == null) {
            return null;
        }
        if (rectF.right > 1.0f) {
            rectF.right = 1.0f;
        }
        if (rectF.bottom > 1.0f) {
            rectF.bottom = 1.0f;
        }
        if (rectF.left < 0.0f) {
            rectF.left = 0.0f;
        }
        if (rectF.top < 0.0f) {
            rectF.top = 0.0f;
        }
        if (rectF.width() > 1.0f) {
            rectF.left = 1.0f - rectF.right;
        }
        if (rectF.height() > 1.0f) {
            rectF.top = 1.0f - rectF.bottom;
        }
        if (imageOrientation == null) {
            return rectF;
        }
        final RectF rectF2 = new RectF(rectF);
        switch (imageOrientation.ordinal()) {
            case 1: {
                rectF2.left = 1.0f - rectF.right;
                rectF2.right = rectF2.left + rectF.width();
                break;
            }
            case 2: {
                rectF2.left = 1.0f - rectF.right;
                rectF2.right = rectF2.left + rectF.width();
                rectF2.bottom = 1.0f - rectF.top;
                rectF2.top = rectF2.bottom - rectF.height();
                break;
            }
            case 3: {
                rectF2.bottom = 1.0f - rectF.top;
                rectF2.top = rectF2.bottom - rectF.height();
                break;
            }
            case 4: {
                rectF2.left = rectF.top;
                rectF2.right = rectF2.left + rectF.height();
                rectF2.top = 1.0f - rectF.right;
                rectF2.bottom = rectF2.top + rectF.width();
                break;
            }
            case 5: {
                rectF2.left = rectF.top;
                rectF2.right = rectF2.left + rectF.height();
                rectF2.top = rectF.left;
                rectF2.bottom = rectF2.top + rectF.width();
                break;
            }
            case 6: {
                rectF2.left = 1.0f - rectF.bottom;
                rectF2.right = rectF2.left + rectF.height();
                rectF2.top = rectF.left;
                rectF2.bottom = rectF2.top + rectF.width();
                break;
            }
            case 7: {
                rectF2.left = 1.0f - rectF.bottom;
                rectF2.right = rectF2.left + rectF.height();
                rectF2.top = 1.0f - rectF.right;
                rectF2.bottom = rectF2.top + rectF.width();
                break;
            }
        }
        return rectF2;
    }
    
    public static TuSdkSize computerOutSize(final TuSdkSize tuSdkSize, final float n, final boolean b) {
        final float ratioFloat = tuSdkSize.getRatioFloat();
        if (n == ratioFloat) {
            return tuSdkSize;
        }
        final TuSdkSize create = TuSdkSize.create(tuSdkSize);
        if (b) {
            if (ratioFloat > n) {
                create.height = tuSdkSize.height;
                create.width = (int)Math.floor(create.height * n);
            }
            else {
                create.width = tuSdkSize.width;
                create.height = (int)Math.floor(create.width / n);
            }
        }
        else if (ratioFloat > n) {
            create.width = tuSdkSize.width;
            create.height = (int)Math.floor(create.width / n);
        }
        else {
            create.height = tuSdkSize.height;
            create.width = (int)Math.floor(create.height * n);
        }
        return create;
    }
    
    public static Rect computerOutCenter(final Rect rect, final float n, final boolean b) {
        final Rect rect2 = new Rect(rect);
        final TuSdkSize computerOutSize = computerOutSize(TuSdkSize.create(rect), n, b);
        final Rect rect3 = rect2;
        rect3.left -= (computerOutSize.width - rect2.width()) / 2;
        rect2.right = rect2.left + computerOutSize.width;
        final Rect rect4 = rect2;
        rect4.top -= (computerOutSize.height - rect2.height()) / 2;
        rect2.bottom = rect2.top + computerOutSize.height;
        return rect2;
    }
    
    public static float computerOutScale(final Rect rect, final float n, final boolean b) {
        final Rect computerOutCenter = computerOutCenter(rect, n, b);
        final float n2 = computerOutCenter.width() / (float)rect.width();
        final float n3 = computerOutCenter.height() / (float)rect.height();
        if (b) {
            return Math.min(n2, n3);
        }
        return Math.max(n2, n3);
    }
    
    public static float computeAngle(final PointF pointF, final PointF pointF2) {
        float n = (float)((float)Math.asin(Math.abs(pointF.y - pointF2.y) / getDistanceOfTwoPoints(pointF, pointF2)) * 180.0f / 3.141592653589793);
        if (pointF2.x - pointF.x <= 0.0f && pointF2.y - pointF.y >= 0.0f) {
            n = 90.0f - n;
        }
        else if (pointF2.x - pointF.x <= 0.0f && pointF2.y - pointF.y <= 0.0f) {
            n += 90.0f;
        }
        else if (pointF2.x - pointF.x >= 0.0f && pointF2.y - pointF.y <= 0.0f) {
            n = 270.0f - n;
        }
        else if (pointF2.x - pointF.x >= 0.0f && pointF2.y - pointF.y >= 0.0f) {
            n += 270.0f;
        }
        return n - 235.0f;
    }
    
    public static float getDistanceOfTwoPoints(final PointF pointF, final PointF pointF2) {
        return getDistanceOfTwoPoints(pointF.x, pointF.y, pointF2.x, pointF2.y);
    }
    
    public static float getDistanceOfTwoPoints(final float n, final float n2, final float n3, final float n4) {
        return (float)Math.sqrt(Math.pow(n - n3, 2.0) + Math.pow(n2 - n4, 2.0));
    }
    
    public static Rect computerMinMaxSideInRegionRatio(final TuSdkSize tuSdkSize, final float n) {
        if (tuSdkSize == null || !tuSdkSize.isSize() || n <= 0.0f || n > 1.0f) {
            return null;
        }
        final TuSdkSize create = TuSdkSize.create(tuSdkSize);
        if (tuSdkSize.maxSide() == tuSdkSize.height) {
            create.width = (int)(tuSdkSize.height * n);
        }
        else {
            create.height = (int)(tuSdkSize.width * n);
        }
        return makeRectWithAspectRatioInsideRect(create.evenSize(), new Rect(0, 0, tuSdkSize.width, tuSdkSize.height));
    }
    
    public static RectF minEnclosingRectangle(final PointF pointF, final TuSdkSize tuSdkSize, final float n) {
        final PointF pointF2 = new PointF(tuSdkSize.width * 0.5f, tuSdkSize.height * 0.5f);
        final RectF rectF = new RectF();
        final PointF pointF3 = new PointF();
        pointF3.x = -pointF2.x;
        pointF3.y = -pointF2.y;
        mergeEnclosingRectangle(rectF, pointF3, n);
        pointF3.x = pointF2.x;
        pointF3.y = -pointF2.y;
        mergeEnclosingRectangle(rectF, pointF3, n);
        pointF3.x = -pointF2.x;
        pointF3.y = pointF2.y;
        mergeEnclosingRectangle(rectF, pointF3, n);
        pointF3.x = pointF2.x;
        pointF3.y = pointF2.y;
        mergeEnclosingRectangle(rectF, pointF3, n);
        final RectF rectF2 = rectF;
        rectF2.left += pointF.x;
        final RectF rectF3 = rectF;
        rectF3.top += pointF.y;
        final RectF rectF4 = rectF;
        rectF4.right += pointF.x;
        final RectF rectF5 = rectF;
        rectF5.bottom += pointF.y;
        return rectF;
    }
    
    public static void mergeEnclosingRectangle(final RectF rectF, final PointF pointF, final float n) {
        final PointF rotationWithOrigin = rotationWithOrigin(pointF, n);
        rectF.left = Math.min(rectF.left, rotationWithOrigin.x);
        rectF.right = Math.max(rectF.right, rotationWithOrigin.x);
        rectF.top = Math.min(rectF.top, rotationWithOrigin.y);
        rectF.bottom = Math.max(rectF.bottom, rotationWithOrigin.y);
    }
    
    public static PointF rotationWithOrigin(final PointF pointF, final float n) {
        final PointF pointF2 = new PointF();
        final double n2 = n * 3.141592653589793 / 180.0;
        pointF2.x = (float)(pointF.x * Math.cos(n2) + pointF.y * Math.sin(n2));
        pointF2.y = (float)(pointF.y * Math.cos(n2) - pointF.x * Math.sin(n2));
        return pointF2;
    }
    
    public static Rect rotationWithRotation(final Rect rect, final TuSdkSize tuSdkSize, final ImageOrientation imageOrientation) {
        if (rect == null || tuSdkSize == null || imageOrientation == null || rect.width() <= 0 || rect.height() <= 0 || rect.right > tuSdkSize.width || rect.bottom > tuSdkSize.height) {
            return rect;
        }
        Rect rect2 = null;
        switch (imageOrientation.ordinal()) {
            case 1: {
                rect2 = new Rect(tuSdkSize.width - rect.right, rect.top, tuSdkSize.width - rect.left, rect.bottom);
                break;
            }
            case 3: {
                rect2 = new Rect(rect.left, tuSdkSize.height - rect.bottom, rect.right, tuSdkSize.height - rect.top);
                break;
            }
            case 6: {
                rect2 = new Rect(tuSdkSize.height - rect.bottom, rect.left, tuSdkSize.height - rect.top, rect.right);
                break;
            }
            case 4: {
                rect2 = new Rect(rect.top, tuSdkSize.width - rect.right, rect.bottom, tuSdkSize.width - rect.left);
                break;
            }
            case 5: {
                rect2 = new Rect(rect.top, rect.left, rect.bottom, rect.right);
                break;
            }
            case 7: {
                rect2 = new Rect(tuSdkSize.height - rect.bottom, tuSdkSize.width - rect.right, tuSdkSize.height - rect.top, tuSdkSize.width - rect.left);
                break;
            }
            case 2: {
                rect2 = new Rect(tuSdkSize.width - rect.right, tuSdkSize.height - rect.bottom, tuSdkSize.width - rect.left, tuSdkSize.height - rect.top);
                break;
            }
            default: {
                rect2 = new Rect(rect);
                break;
            }
        }
        return rect2;
    }
    
    public static RectF rotationWithRotation(final RectF rectF, final ImageOrientation imageOrientation) {
        if (rectF == null || imageOrientation == null) {
            return rectF;
        }
        RectF rectF2 = null;
        switch (imageOrientation.ordinal()) {
            case 1: {
                rectF2 = new RectF(1.0f - rectF.right, rectF.top, 1.0f - rectF.left, rectF.bottom);
                break;
            }
            case 3: {
                rectF2 = new RectF(rectF.left, 1.0f - rectF.bottom, rectF.right, 1.0f - rectF.top);
                break;
            }
            case 6: {
                rectF2 = new RectF(1.0f - rectF.bottom, rectF.left, 1.0f - rectF.top, rectF.right);
                break;
            }
            case 4: {
                rectF2 = new RectF(rectF.top, 1.0f - rectF.right, rectF.bottom, 1.0f - rectF.left);
                break;
            }
            case 5: {
                rectF2 = new RectF(rectF.top, rectF.left, rectF.bottom, rectF.right);
                break;
            }
            case 7: {
                rectF2 = new RectF(1.0f - rectF.bottom, 1.0f - rectF.right, 1.0f - rectF.top, 1.0f - rectF.left);
                break;
            }
            case 2: {
                rectF2 = new RectF(1.0f - rectF.right, 1.0f - rectF.bottom, 1.0f - rectF.left, 1.0f - rectF.top);
                break;
            }
            default: {
                rectF2 = new RectF(rectF);
                break;
            }
        }
        return rectF2;
    }
    
    public static RectF getRectInParent(final TuSdkSize tuSdkSize, final TuSdkSize tuSdkSize2, final RectF rectF) {
        final RectF rectF2 = new RectF(0.0f, 0.0f, (float)tuSdkSize.width, (float)tuSdkSize.height);
        if (tuSdkSize2 == null || rectF == null || tuSdkSize == null) {
            return rectF2;
        }
        rectF2.left = tuSdkSize.width * rectF.left - tuSdkSize2.width * 0.5f;
        rectF2.top = tuSdkSize.height * rectF.top - tuSdkSize2.height * 0.5f;
        rectF2.right = rectF2.left + tuSdkSize.width * rectF.width();
        rectF2.bottom = rectF2.top + tuSdkSize.height * rectF.height();
        return rectF2;
    }
    
    public static RectF getRectInParent(final RectF rectF, final RectF rectF2) {
        if (rectF == null) {
            return null;
        }
        final RectF rectF3 = new RectF(rectF.left, rectF.top, rectF.right, rectF.bottom);
        if (rectF2 == null) {
            return rectF3;
        }
        final RectF rectF4 = rectF3;
        rectF4.left += rectF.width() * rectF2.left;
        final RectF rectF5 = rectF3;
        rectF5.top += rectF.height() * rectF2.top;
        rectF3.right = rectF3.left + rectF.width() * rectF2.width();
        rectF3.bottom = rectF3.top + rectF.height() * rectF2.height();
        return rectF3;
    }
    
    public static Rect makeRectWithAspectRatioInsideRect(final TuSdkSize tuSdkSize, final Rect rect) {
        if (tuSdkSize == null || rect == null) {
            return null;
        }
        final TuSdkSize tuSdkSize2 = new TuSdkSize();
        tuSdkSize2.width = rect.width();
        tuSdkSize2.height = (int)Math.floor(tuSdkSize2.width / tuSdkSize.getRatioFloat());
        if (tuSdkSize2.height > rect.height()) {
            tuSdkSize2.height = rect.height();
            tuSdkSize2.width = (int)Math.floor(tuSdkSize2.height * tuSdkSize.getRatioFloat());
        }
        final Rect rect2 = new Rect(rect);
        rect2.left = rect.left + (rect.width() - tuSdkSize2.width) / 2;
        rect2.right = rect2.left + tuSdkSize2.width;
        rect2.top = rect.top + (rect.height() - tuSdkSize2.height) / 2;
        rect2.bottom = rect2.top + tuSdkSize2.height;
        return rect2;
    }
    
    public static RectF makeRectWithAspectRatioOutsideRect(final TuSdkSize tuSdkSize, final RectF rectF) {
        if (tuSdkSize == null || rectF == null) {
            return null;
        }
        final TuSdkSizeF tuSdkSizeF = new TuSdkSizeF();
        tuSdkSizeF.width = rectF.width();
        tuSdkSizeF.height = (float)(int)Math.floor(tuSdkSizeF.width / tuSdkSize.getRatioFloat());
        if (tuSdkSizeF.height < rectF.height()) {
            tuSdkSizeF.height = rectF.height();
            tuSdkSizeF.width = (float)(int)Math.floor(tuSdkSizeF.height * tuSdkSize.getRatioFloat());
        }
        final RectF rectF2 = new RectF(rectF);
        rectF2.left = (tuSdkSizeF.width - rectF.width()) / 2.0f - rectF.left;
        rectF2.right = rectF2.left + tuSdkSizeF.width;
        rectF2.top = (tuSdkSizeF.height - rectF.height()) / 2.0f - rectF.top;
        rectF2.bottom = rectF2.top + tuSdkSizeF.height;
        return rectF2;
    }
    
    public static double computerPotintDistance(final Point point, final Point point2) {
        if (point == null || point2 == null) {
            return 0.0;
        }
        final float n = (float)(point.x - point2.x);
        final float n2 = (float)(point.y - point2.y);
        return Math.sqrt(n * n + n2 * n2);
    }
    
    public static double computerPotintDistance(final PointF pointF, final PointF pointF2) {
        if (pointF == null || pointF2 == null) {
            return 0.0;
        }
        final float n = pointF.x - pointF2.x;
        final float n2 = pointF.y - pointF2.y;
        return Math.sqrt(n * n + n2 * n2);
    }
    
    public static float[] textureCoordinates(final ImageOrientation imageOrientation, RectF rotationWithRotation) {
        if (rotationWithRotation == null || imageOrientation == null) {
            return new float[] { 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f };
        }
        rotationWithRotation = rotationWithRotation(rotationWithRotation, imageOrientation);
        final float[] array = new float[8];
        array[0] = rotationWithRotation.left;
        array[1] = rotationWithRotation.top;
        array[2] = rotationWithRotation.right;
        array[3] = array[1];
        array[4] = array[0];
        array[5] = rotationWithRotation.bottom;
        array[6] = array[2];
        array[7] = array[5];
        return a(imageOrientation, array);
    }
    
    public static float[] displayCoordinates(final ImageOrientation imageOrientation, RectF rotationWithRotation) {
        if (rotationWithRotation == null || imageOrientation == null) {
            return new float[] { 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f };
        }
        rotationWithRotation = rotationWithRotation(rotationWithRotation, imageOrientation);
        final float[] array = new float[8];
        array[0] = rotationWithRotation.left;
        array[1] = rotationWithRotation.bottom;
        array[2] = rotationWithRotation.right;
        array[3] = array[1];
        array[4] = array[0];
        array[5] = rotationWithRotation.top;
        array[6] = array[2];
        array[7] = array[5];
        return a(imageOrientation, array);
    }
    
    public static float[] textureVertices(final ImageOrientation imageOrientation, RectF rotationWithRotation) {
        if (rotationWithRotation == null || imageOrientation == null) {
            return new float[] { -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f };
        }
        rotationWithRotation = rotationWithRotation(rotationWithRotation, imageOrientation);
        final float[] array = new float[8];
        array[0] = rotationWithRotation.left * 2.0f - 1.0f;
        array[1] = rotationWithRotation.top * 2.0f - 1.0f;
        array[2] = rotationWithRotation.right * 2.0f - 1.0f;
        array[3] = array[1];
        array[4] = array[0];
        array[5] = rotationWithRotation.bottom * 2.0f - 1.0f;
        array[6] = array[2];
        array[7] = array[5];
        return array;
    }
    
    public static float[] displayVertices(final ImageOrientation imageOrientation, RectF rotationWithRotation) {
        if (rotationWithRotation == null || imageOrientation == null) {
            return new float[] { -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f };
        }
        rotationWithRotation = rotationWithRotation(rotationWithRotation, imageOrientation);
        final float[] array = new float[8];
        array[0] = rotationWithRotation.left * 2.0f - 1.0f;
        array[1] = 1.0f - rotationWithRotation.bottom * 2.0f;
        array[2] = rotationWithRotation.right * 2.0f - 1.0f;
        array[3] = array[1];
        array[4] = array[0];
        array[5] = 1.0f - rotationWithRotation.top * 2.0f;
        array[6] = array[2];
        array[7] = array[5];
        return array;
    }
    
    private static float[] a(final ImageOrientation imageOrientation, final float[] array) {
        if (imageOrientation == null || array == null) {
            return array;
        }
        switch (imageOrientation.ordinal()) {
            case 1: {
                return new float[] { array[2], array[3], array[0], array[1], array[6], array[7], array[4], array[5] };
            }
            case 3: {
                return new float[] { array[4], array[5], array[6], array[7], array[0], array[1], array[2], array[3] };
            }
            case 6: {
                return new float[] { array[2], array[3], array[6], array[7], array[0], array[1], array[4], array[5] };
            }
            case 4: {
                return new float[] { array[4], array[5], array[0], array[1], array[6], array[7], array[2], array[3] };
            }
            case 5: {
                return new float[] { array[0], array[1], array[4], array[5], array[2], array[3], array[6], array[7] };
            }
            case 7: {
                return new float[] { array[6], array[7], array[2], array[3], array[4], array[5], array[0], array[1] };
            }
            case 2: {
                return new float[] { array[6], array[7], array[4], array[5], array[2], array[3], array[0], array[1] };
            }
            default: {
                return array;
            }
        }
    }
}
