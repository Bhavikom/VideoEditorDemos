// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk.liveSticker;

import java.util.Collection;
import java.util.ArrayList;
//import org.lasque.tusdk.core.utils.ArrayHelper;
//import org.lasque.tusdk.core.utils.calc.PointCalc;
import java.util.List;
import android.graphics.PointF;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.face.FaceAligment;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ArrayHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.calc.PointCalc;
//import org.lasque.tusdk.core.face.FaceAligment;

public class TuSdkPlasticFaceInfo
{
    private boolean b;
    private FaceAligment c;
    private PointF d;
    private PointF e;
    private float f;
    private float g;
    private float h;
    private PointF i;
    private PointF j;
    private PointF k;
    private PointF l;
    private PointF m;
    private PointF n;
    private PointF o;
    private List<PointF> p;
    private List<PointF> q;
    private List<PointF> r;
    private List<PointF> s;
    private List<PointF> t;
    private List<PointF> u;
    private List<PointF> v;
    private PointF w;
    private List<PointF> x;
    private static final int[] y;
    private static final int[] z;
    private static final int[] A;
    private static final int[] B;
    private static final int[] C;
    private static final int[] D;
    private static final int[] E;
    private static final int[] F;
    private static final int[] G;
    private static final int[] H;
    private static final int[] I;
    int[] a;
    
    public boolean isEmpty() {
        return !this.b;
    }
    
    public TuSdkPlasticFaceInfo(final FaceAligment c) {
        this.b = false;
        this.a = new int[] { 0, 1, 12, 0, 9, 12, 1, 2, 13, 1, 12, 13, 2, 3, 14, 2, 13, 14, 3, 4, 16, 3, 14, 15, 3, 15, 16, 4, 5, 16, 5, 6, 18, 5, 16, 17, 5, 17, 18, 6, 7, 19, 6, 18, 19, 7, 8, 20, 7, 19, 20, 8, 10, 20, 9, 11, 24, 9, 12, 21, 9, 21, 22, 9, 22, 24, 10, 11, 31, 10, 20, 28, 10, 28, 29, 10, 29, 31, 11, 24, 26, 11, 26, 35, 11, 31, 33, 11, 33, 35, 12, 13, 38, 12, 21, 36, 12, 36, 38, 13, 14, 47, 13, 37, 38, 13, 37, 45, 13, 45, 47, 14, 15, 47, 15, 16, 55, 15, 47, 55, 16, 17, 57, 16, 55, 56, 16, 56, 57, 17, 18, 48, 17, 48, 57, 18, 19, 48, 19, 46, 48, 19, 20, 43, 19, 41, 43, 19, 41, 46, 20, 28, 40, 20, 40, 43, 21, 22, 23, 21, 23, 36, 22, 23, 25, 22, 24, 25, 23, 25, 36, 24, 25, 26, 25, 26, 27, 25, 27, 36, 26, 27, 35, 27, 35, 36, 28, 29, 30, 28, 30, 40, 29, 30, 32, 29, 31, 32, 30, 32, 40, 31, 32, 33, 32, 33, 34, 32, 34, 40, 33, 34, 35, 34, 35, 40, 35, 36, 39, 35, 39, 45, 35, 40, 42, 35, 42, 46, 35, 44, 45, 35, 44, 46, 36, 37, 38, 36, 37, 39, 37, 39, 45, 40, 41, 42, 40, 41, 43, 41, 42, 46, 44, 45, 49, 44, 46, 51, 44, 49, 50, 44, 50, 51, 45, 47, 49, 46, 48, 51, 47, 49, 52, 47, 52, 58, 47, 55, 58, 48, 51, 54, 48, 54, 60, 48, 57, 60, 49, 50, 52, 50, 51, 54, 50, 52, 53, 50, 53, 54, 52, 53, 58, 53, 54, 60, 53, 58, 59, 53, 59, 60, 55, 56, 58, 56, 57, 60, 56, 58, 59, 56, 59, 60 };
        if (c == null) {
            return;
        }
        this.c = c;
        this.a(c.getOrginMarks());
    }
    
    private void a(final PointF[] array) {
        if (array == null || array.length < 3) {
            return;
        }
        this.d = PointCalc.crossPoint(array[16], array[43], array[3], array[29]);
        this.e = PointCalc.crossPoint(array[16], array[43], array[48], array[50]);
        this.f = Math.abs(PointCalc.distance(array[16], this.e));
        this.g = Math.abs(PointCalc.distance(array[16], this.d));
        this.h = this.g * (this.f / this.g * 1.5f);
        this.p = ArrayHelper.toList(array, TuSdkPlasticFaceInfo.y);
        this.i = PointCalc.crossPoint(array[52], array[55], array[72], array[73]);
        this.q = ArrayHelper.toList(array, TuSdkPlasticFaceInfo.B);
        this.j = PointCalc.crossPoint(array[58], array[61], array[75], array[76]);
        this.r = ArrayHelper.toList(array, TuSdkPlasticFaceInfo.C);
        this.m = array[46];
        this.s = ArrayHelper.toList(array, TuSdkPlasticFaceInfo.D);
        this.n = PointCalc.crossPoint(array[16], array[43], array[78], array[79]);
        this.o = PointCalc.crossPoint(array[84], array[90], array[87], array[93]);
        this.t = ArrayHelper.toList(array, TuSdkPlasticFaceInfo.E);
        this.k = PointCalc.crossPoint(array[33], array[37], array[35], array[65]);
        this.l = PointCalc.crossPoint(array[38], array[42], array[40], array[70]);
        this.u = ArrayHelper.toList(array, TuSdkPlasticFaceInfo.z);
        this.v = ArrayHelper.toList(array, TuSdkPlasticFaceInfo.A);
        this.w = PointCalc.crossPoint(array[37], array[79], array[38], array[78]);
        (this.x = new ArrayList<PointF>()).addAll(ArrayHelper.toList(array, TuSdkPlasticFaceInfo.y));
        this.x.add(array[34]);
        this.x.add(array[41]);
        this.x.add(PointCalc.center(array[35], array[40]));
        PointCalc.scalePoint(this.x, this.d, 0.5f);
        this.b = true;
    }
    
    public List<PointF> getPoints() {
        final ArrayList<PointF> list = new ArrayList<PointF>();
        list.addAll(this.x);
        list.addAll(this.p);
        list.addAll(this.u);
        list.addAll(this.v);
        list.add(this.w);
        list.addAll(this.q);
        list.addAll(this.r);
        list.addAll(this.s);
        list.addAll(this.t);
        return list;
    }
    
    public void calcChin(final float n) {
        if (n == 0.0f) {
            return;
        }
        PointCalc.scaleChinPoint2(this.p, TuSdkPlasticFaceInfo.F, this.d, n);
        PointCalc.scaleChinPoint2(this.p, TuSdkPlasticFaceInfo.G, this.d, n);
        PointCalc.scaleChinPoint2(this.p, TuSdkPlasticFaceInfo.H, this.d, n / 2.0f);
        PointCalc.scaleChinPoint2(this.p, TuSdkPlasticFaceInfo.I, this.d, n / 4.0f);
    }
    
    public void calcJaw(final float n) {
        if (n == 0.0f) {
            return;
        }
        PointCalc.scaleJaw(this.p, this.d, n);
    }
    
    public void calcEyeEnlarge(final float n) {
        if (n == 1.0f) {
            return;
        }
        PointCalc.scaleEyeEnlargePoint(this.q, this.i, n);
        PointCalc.scaleEyeEnlargePoint(this.r, this.j, n);
    }
    
    public void calcEyeDis(final float n) {
        if (n == 0.0f) {
            return;
        }
        final PointF center = PointCalc.center(this.i, this.j);
        PointCalc.movePoints(this.q, center, n);
        PointCalc.movePoints(this.r, center, n);
    }
    
    public void calcEyeAngle(final float n) {
        if (n == 0.0f) {
            return;
        }
        PointCalc.rotatePoints(this.q, this.i, n);
        PointCalc.rotatePoints(this.r, this.j, n * -1.0f);
    }
    
    public void calcNose(final float n) {
        if (n == 1.0f) {
            return;
        }
        final PointF pointOfPercentage = PointCalc.pointOfPercentage(this.m, this.s.get(1), n);
        final PointF pointOfPercentage2 = PointCalc.pointOfPercentage(this.m, this.s.get(2), n);
        this.s.set(1, pointOfPercentage);
        this.s.set(2, pointOfPercentage2);
    }
    
    public void calcMouth(final float n) {
        if (n == 1.0f) {
            return;
        }
        final PointF pointOfPercentage = PointCalc.pointOfPercentage(this.o, this.t.get(0), n);
        final PointF pointOfPercentage2 = PointCalc.pointOfPercentage(this.o, this.t.get(1), n);
        final PointF pointOfPercentage3 = PointCalc.pointOfPercentage(this.t.get(5), PointCalc.pointOfPercentage(this.t.get(3), this.t.get(2), n), n);
        final PointF pointOfPercentage4 = PointCalc.pointOfPercentage(this.t.get(6), this.t.get(3), n);
        final PointF pointOfPercentage5 = PointCalc.pointOfPercentage(this.t.get(7), PointCalc.pointOfPercentage(this.t.get(3), this.t.get(4), n), n);
        final PointF pointOfPercentage6 = PointCalc.pointOfPercentage(this.t.get(6), this.t.get(5), n);
        final PointF pointOfPercentage7 = PointCalc.pointOfPercentage(this.t.get(6), this.t.get(7), n);
        final PointF pointOfPercentage8 = PointCalc.pointOfPercentage(this.t.get(11), PointCalc.pointOfPercentage(this.t.get(9), this.t.get(8), n), n);
        final PointF pointOfPercentage9 = PointCalc.pointOfPercentage(this.t.get(12), this.t.get(9), n);
        final PointF pointOfPercentage10 = PointCalc.pointOfPercentage(this.t.get(13), PointCalc.pointOfPercentage(this.t.get(9), this.t.get(10), n), n);
        final PointF pointOfPercentage11 = PointCalc.pointOfPercentage(this.t.get(12), this.t.get(11), n);
        final PointF pointOfPercentage12 = PointCalc.pointOfPercentage(this.t.get(12), this.t.get(13), n);
        this.t.set(0, pointOfPercentage);
        this.t.set(1, pointOfPercentage2);
        this.t.set(2, pointOfPercentage3);
        this.t.set(3, pointOfPercentage4);
        this.t.set(4, pointOfPercentage5);
        this.t.set(5, pointOfPercentage6);
        this.t.set(7, pointOfPercentage7);
        this.t.set(8, pointOfPercentage8);
        this.t.set(9, pointOfPercentage9);
        this.t.set(10, pointOfPercentage10);
        this.t.set(11, pointOfPercentage11);
        this.t.set(13, pointOfPercentage12);
    }
    
    public void calcArchEyebrow(final float n) {
        if (n == 0.0f) {
            return;
        }
        PointCalc.calcArchEyebrow(this.u, this.k, n);
        PointCalc.calcArchEyebrow(this.v, this.l, n);
    }
    
    public int[] fillFace() {
        return this.a;
    }
    
    static {
        y = new int[] { 0, 4, 8, 12, 16, 20, 24, 28, 32 };
        z = new int[] { 33, 34, 64, 35, 65, 37, 67 };
        A = new int[] { 42, 41, 71, 40, 70, 38, 68 };
        B = new int[] { 72, 73, 52, 55 };
        C = new int[] { 75, 76, 58, 61 };
        D = new int[] { 49, 82, 83 };
        E = new int[] { 84, 90, 86, 87, 88, 97, 98, 99, 94, 93, 92, 103, 102, 101 };
        F = new int[] { 2, 3 };
        G = new int[] { 5, 6 };
        H = new int[] { 1, 7 };
        I = new int[] { 0, 4, 8 };
    }
}
