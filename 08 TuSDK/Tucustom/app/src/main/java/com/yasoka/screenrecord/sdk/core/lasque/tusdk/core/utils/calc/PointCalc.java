// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.calc;

import java.util.List;
//import org.lasque.tusdk.core.utils.RectHelper;
import android.graphics.PointF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.RectHelper;

public class PointCalc
{
    public static float distance(final PointF pointF, final PointF pointF2) {
        return RectHelper.getDistanceOfTwoPoints(pointF2, pointF);
    }
    
    public static PointF center(final PointF pointF, final PointF pointF2) {
        final PointF pointF3 = new PointF();
        pointF3.x = (pointF.x + pointF2.x) / 2.0f;
        pointF3.y = (pointF.y + pointF2.y) / 2.0f;
        return pointF3;
    }
    
    public static PointF crossPoint(final PointF pointF, final PointF pointF2, final PointF pointF3, final PointF pointF4) {
        final float a = a(pointF3, pointF4, pointF, pointF2);
        final float a2 = a(pointF, pointF2, pointF, pointF3);
        return new PointF(pointF3.x + (pointF4.x - pointF3.x) * a2 / a, pointF3.y + (pointF4.y - pointF3.y) * a2 / a);
    }
    
    private static float a(final PointF pointF, final PointF pointF2, final PointF pointF3, final PointF pointF4) {
        return (pointF2.x - pointF.x) * (pointF4.y - pointF3.y) - (pointF2.y - pointF.y) * (pointF4.x - pointF3.x);
    }
    
    public static PointF increase(final PointF pointF, final PointF pointF2, final float n) {
        if (n == 0.0f) {
            return new PointF(pointF2.x, pointF2.y);
        }
        final float n2 = pointF2.x - pointF.x;
        final float n3 = pointF2.y - pointF.y;
        float n4 = (float)Math.sqrt(n * n / (n3 / n2 * (n3 / n2) + 1.0f));
        if (n2 <= 0.0f) {
            n4 = -n4;
        }
        if (n < 0.0f) {
            n4 = -n4;
        }
        final PointF pointF3 = new PointF();
        pointF3.x = pointF2.x + n4;
        pointF3.y = pointF2.y + n3 / n2 * n4;
        return pointF3;
    }
    
    public static PointF increasePercentage(final PointF pointF, final PointF pointF2, final float n) {
        return increase(pointF, pointF2, Math.abs(distance(pointF, pointF2)) * n);
    }
    
    public static PointF pointOf(final PointF pointF, final PointF pointF2, final float n) {
        return increase(pointF2, pointF, -n);
    }
    
    public static PointF pointOfPercentage(final PointF pointF, final PointF pointF2, final float n) {
        return increase(pointF2, pointF, -n * Math.abs(distance(pointF, pointF2)));
    }
    
    public static PointF crossPoint(final PointF pointF, final PointF pointF2, final PointF pointF3, final float n, final float n2) {
        final PointF point = pointOf(pointF, pointF2, Math.abs(distance(pointF, pointF2)) * n);
        return increase(pointF3, point, Math.abs(distance(pointF3, pointF)) * n + n2 * (1.0f - n) - Math.abs(distance(pointF3, point)));
    }
    
    public static PointF crossPoint(final PointF pointF, final PointF pointF2, final PointF pointF3) {
        final float n = (pointF.y - pointF2.y) / (pointF2.x - pointF2.x);
        final float n2 = pointF.y - n * pointF.y;
        final float n3 = pointF3.x + n * pointF3.y;
        final PointF pointF4 = new PointF();
        pointF4.x = (n3 - n * n2) / (n * n + 1.0f);
        pointF4.y = n * pointF4.x + n2;
        return pointF4;
    }
    
    public static void scalePoint(final List<PointF> list, final PointF pointF, final float n) {
        if (list == null || pointF == null || n == 0.0f) {
            return;
        }
        for (int i = 0; i < list.size(); ++i) {
            list.set(i, increasePercentage(pointF, list.get(i), n));
        }
    }
    
    public static void scalePoint(final PointF[] array, final PointF pointF, final float n) {
        if (array == null || pointF == null || n == 0.0f) {
            return;
        }
        for (int i = 0; i < array.length; ++i) {
            array[i] = increasePercentage(pointF, array[i], n);
        }
    }
    
    public static void disPoints(final List<PointF> list, final PointF pointF, final float n) {
        for (int i = 1; i < list.size(); ++i) {
            list.set(i, increase(pointF, list.get(i), n));
        }
    }
    
    public static void scaleChinPoint(final List<PointF> list, final int[] array, final PointF pointF, final float n) {
        final float abs = Math.abs(distance(list.get(array[0]), list.get(array[2])));
        final PointF pointF2 = list.get(array[1]);
        final PointF crossPoint = crossPoint(pointF, pointF2, list.get(array[0]), list.get(array[2]));
        final float abs2 = Math.abs(distance(pointF, crossPoint));
        final float abs3 = Math.abs(distance(pointF, pointF2));
        if (abs2 >= abs3) {
            return;
        }
        final float n2 = abs3 - abs2;
        list.set(array[1], increase(pointF, crossPoint, n2 - n2 * (n2 / abs) * 1.2f * (1.0f - n)));
    }
    
    public static void scaleChinPoint2(final List<PointF> list, final int[] array, final PointF pointF, final float n) {
        if (list == null || pointF == null || n == 0.0f) {
            return;
        }
        for (final int n2 : array) {
            list.set(n2, pointOfPercentage(list.get(n2), pointF, n));
        }
    }
    
    public static void scaleChinPoint3(final List<PointF> list, final int n, final PointF pointF, final float n2) {
        if (list == null || pointF == null || n2 == 0.0f) {
            return;
        }
        list.set(n, pointOfPercentage(list.get(n), pointF, (n2 - 1.0f) * 0.8f));
    }
    
    public static void scaleEyeEnlargePoint(final List<PointF> list, final PointF pointF, final float n) {
        final PointF increasePercentage = increasePercentage(pointF, list.get(0), (n - 1.0f) * 1.6f);
        final PointF increasePercentage2 = increasePercentage(pointF, list.get(1), (n - 1.0f) * 1.6f);
        final PointF increasePercentage3 = increasePercentage(pointF, list.get(2), (n - 1.0f) / 2.0f);
        final PointF increasePercentage4 = increasePercentage(pointF, list.get(3), (n - 1.0f) / 2.0f);
        list.set(0, increasePercentage);
        list.set(1, increasePercentage2);
        list.set(2, increasePercentage3);
        list.set(3, increasePercentage4);
    }
    
    public static void movePoints(final List<PointF> list, final PointF pointF, final float n) {
        for (int i = 0; i < list.size(); ++i) {
            list.set(i, increasePercentage(pointF, list.get(i), n));
        }
    }
    
    public static void moveEyeBrow(final List<PointF> list, final PointF pointF, final float n) {
        for (int i = 0; i < list.size() / 2; ++i) {
            final PointF increasePercentage = increasePercentage(pointF, list.get(i + 3), n);
            list.set(i, increasePercentage(pointF, list.get(i), n));
            list.set(i + 3, increasePercentage);
        }
    }
    
    public static void calcArchEyebrow(final List<PointF> list, final PointF pointF, final float n) {
        final PointF increasePercentage = increasePercentage(pointF, list.get(1), n * 0.5f);
        final PointF increasePercentage2 = increasePercentage(pointF, list.get(3), n * 0.5f);
        final PointF increasePercentage3 = increasePercentage(pointF, list.get(5), n * 0.5f);
        list.set(1, increasePercentage);
        list.set(3, increasePercentage2);
        list.set(5, increasePercentage3);
    }
    
    public static float smoothstep(final float n, final float n2, final float n3) {
        if (n3 < n) {
            return 0.0f;
        }
        if (n3 >= n2) {
            return 1.0f;
        }
        final float n4 = (n3 - n) / (n2 - n);
        return n4 * n4 * (3.0f - 2.0f * n4);
    }
    
    public static void rotatePoints(final List<PointF> list, final PointF pointF, final float n) {
        final float n2 = (float)(n / 180.0f * 3.141592653589793);
        for (int i = 0; i < list.size(); ++i) {
            final float distance = distance(pointF, list.get(i));
            final float n3 = (float)(Math.sin(n2 / 2.0f) * distance);
            final float n4 = (float)(Math.cos(n2 / 2.0f) * distance);
            list.set(i, new PointF((pointF.x * n3 / n4 + list.get(i).x * n4 / n3 + list.get(i).y - pointF.y) / (n3 / n4 + n4 / n3), (pointF.y * n3 / n4 + list.get(i).y * n4 / n3 + pointF.x - list.get(i).x) / (n3 / n4 + n4 / n3)));
        }
    }
    
    public static void scaleJaw(final List<PointF> list, final PointF pointF, final float n) {
        if (list == null || pointF == null || n == 0.0f) {
            return;
        }
        final PointF pointF2 = list.get(3);
        final PointF pointF3 = list.get(4);
        final PointF pointF4 = list.get(5);
        list.set(3, pointOfPercentage(pointF2, pointF, n * 0.5f));
        list.set(4, pointOfPercentage(pointF3, pointF, n));
        list.set(5, pointOfPercentage(pointF4, pointF, n * 0.5f));
    }
}
