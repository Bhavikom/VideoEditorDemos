package org.lasque.tusdk.core.seles.tusdk.liveSticker;

import android.graphics.PointF;
import java.util.ArrayList;
import java.util.List;
import org.lasque.tusdk.core.face.FaceAligment;
import org.lasque.tusdk.core.utils.ArrayHelper;
import org.lasque.tusdk.core.utils.calc.PointCalc;

public class TuSdkPlasticFaceInfo
{
  private boolean b = false;
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
  private static final int[] y = { 0, 4, 8, 12, 16, 20, 24, 28, 32 };
  private static final int[] z = { 33, 34, 64, 35, 65, 37, 67 };
  private static final int[] A = { 42, 41, 71, 40, 70, 38, 68 };
  private static final int[] B = { 72, 73, 52, 55 };
  private static final int[] C = { 75, 76, 58, 61 };
  private static final int[] D = { 49, 82, 83 };
  private static final int[] E = { 84, 90, 86, 87, 88, 97, 98, 99, 94, 93, 92, 103, 102, 101 };
  private static final int[] F = { 2, 3 };
  private static final int[] G = { 5, 6 };
  private static final int[] H = { 1, 7 };
  private static final int[] I = { 0, 4, 8 };
  int[] a = { 0, 1, 12, 0, 9, 12, 1, 2, 13, 1, 12, 13, 2, 3, 14, 2, 13, 14, 3, 4, 16, 3, 14, 15, 3, 15, 16, 4, 5, 16, 5, 6, 18, 5, 16, 17, 5, 17, 18, 6, 7, 19, 6, 18, 19, 7, 8, 20, 7, 19, 20, 8, 10, 20, 9, 11, 24, 9, 12, 21, 9, 21, 22, 9, 22, 24, 10, 11, 31, 10, 20, 28, 10, 28, 29, 10, 29, 31, 11, 24, 26, 11, 26, 35, 11, 31, 33, 11, 33, 35, 12, 13, 38, 12, 21, 36, 12, 36, 38, 13, 14, 47, 13, 37, 38, 13, 37, 45, 13, 45, 47, 14, 15, 47, 15, 16, 55, 15, 47, 55, 16, 17, 57, 16, 55, 56, 16, 56, 57, 17, 18, 48, 17, 48, 57, 18, 19, 48, 19, 46, 48, 19, 20, 43, 19, 41, 43, 19, 41, 46, 20, 28, 40, 20, 40, 43, 21, 22, 23, 21, 23, 36, 22, 23, 25, 22, 24, 25, 23, 25, 36, 24, 25, 26, 25, 26, 27, 25, 27, 36, 26, 27, 35, 27, 35, 36, 28, 29, 30, 28, 30, 40, 29, 30, 32, 29, 31, 32, 30, 32, 40, 31, 32, 33, 32, 33, 34, 32, 34, 40, 33, 34, 35, 34, 35, 40, 35, 36, 39, 35, 39, 45, 35, 40, 42, 35, 42, 46, 35, 44, 45, 35, 44, 46, 36, 37, 38, 36, 37, 39, 37, 39, 45, 40, 41, 42, 40, 41, 43, 41, 42, 46, 44, 45, 49, 44, 46, 51, 44, 49, 50, 44, 50, 51, 45, 47, 49, 46, 48, 51, 47, 49, 52, 47, 52, 58, 47, 55, 58, 48, 51, 54, 48, 54, 60, 48, 57, 60, 49, 50, 52, 50, 51, 54, 50, 52, 53, 50, 53, 54, 52, 53, 58, 53, 54, 60, 53, 58, 59, 53, 59, 60, 55, 56, 58, 56, 57, 60, 56, 58, 59, 56, 59, 60 };
  
  public boolean isEmpty()
  {
    return !this.b;
  }
  
  public TuSdkPlasticFaceInfo(FaceAligment paramFaceAligment)
  {
    if (paramFaceAligment == null) {
      return;
    }
    this.c = paramFaceAligment;
    a(paramFaceAligment.getOrginMarks());
  }
  
  private void a(PointF[] paramArrayOfPointF)
  {
    if ((paramArrayOfPointF == null) || (paramArrayOfPointF.length < 3)) {
      return;
    }
    this.d = PointCalc.crossPoint(paramArrayOfPointF[16], paramArrayOfPointF[43], paramArrayOfPointF[3], paramArrayOfPointF[29]);
    this.e = PointCalc.crossPoint(paramArrayOfPointF[16], paramArrayOfPointF[43], paramArrayOfPointF[48], paramArrayOfPointF[50]);
    this.f = Math.abs(PointCalc.distance(paramArrayOfPointF[16], this.e));
    this.g = Math.abs(PointCalc.distance(paramArrayOfPointF[16], this.d));
    this.h = (this.g * (this.f / this.g * 1.5F));
    this.p = ArrayHelper.toList(paramArrayOfPointF, y);
    this.i = PointCalc.crossPoint(paramArrayOfPointF[52], paramArrayOfPointF[55], paramArrayOfPointF[72], paramArrayOfPointF[73]);
    this.q = ArrayHelper.toList(paramArrayOfPointF, B);
    this.j = PointCalc.crossPoint(paramArrayOfPointF[58], paramArrayOfPointF[61], paramArrayOfPointF[75], paramArrayOfPointF[76]);
    this.r = ArrayHelper.toList(paramArrayOfPointF, C);
    this.m = paramArrayOfPointF[46];
    this.s = ArrayHelper.toList(paramArrayOfPointF, D);
    this.n = PointCalc.crossPoint(paramArrayOfPointF[16], paramArrayOfPointF[43], paramArrayOfPointF[78], paramArrayOfPointF[79]);
    this.o = PointCalc.crossPoint(paramArrayOfPointF[84], paramArrayOfPointF[90], paramArrayOfPointF[87], paramArrayOfPointF[93]);
    this.t = ArrayHelper.toList(paramArrayOfPointF, E);
    this.k = PointCalc.crossPoint(paramArrayOfPointF[33], paramArrayOfPointF[37], paramArrayOfPointF[35], paramArrayOfPointF[65]);
    this.l = PointCalc.crossPoint(paramArrayOfPointF[38], paramArrayOfPointF[42], paramArrayOfPointF[40], paramArrayOfPointF[70]);
    this.u = ArrayHelper.toList(paramArrayOfPointF, z);
    this.v = ArrayHelper.toList(paramArrayOfPointF, A);
    this.w = PointCalc.crossPoint(paramArrayOfPointF[37], paramArrayOfPointF[79], paramArrayOfPointF[38], paramArrayOfPointF[78]);
    this.x = new ArrayList();
    this.x.addAll(ArrayHelper.toList(paramArrayOfPointF, y));
    this.x.add(paramArrayOfPointF[34]);
    this.x.add(paramArrayOfPointF[41]);
    this.x.add(PointCalc.center(paramArrayOfPointF[35], paramArrayOfPointF[40]));
    PointCalc.scalePoint(this.x, this.d, 0.5F);
    this.b = true;
  }
  
  public List<PointF> getPoints()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.addAll(this.x);
    localArrayList.addAll(this.p);
    localArrayList.addAll(this.u);
    localArrayList.addAll(this.v);
    localArrayList.add(this.w);
    localArrayList.addAll(this.q);
    localArrayList.addAll(this.r);
    localArrayList.addAll(this.s);
    localArrayList.addAll(this.t);
    return localArrayList;
  }
  
  public void calcChin(float paramFloat)
  {
    if (paramFloat == 0.0F) {
      return;
    }
    PointCalc.scaleChinPoint2(this.p, F, this.d, paramFloat);
    PointCalc.scaleChinPoint2(this.p, G, this.d, paramFloat);
    PointCalc.scaleChinPoint2(this.p, H, this.d, paramFloat / 2.0F);
    PointCalc.scaleChinPoint2(this.p, I, this.d, paramFloat / 4.0F);
  }
  
  public void calcJaw(float paramFloat)
  {
    if (paramFloat == 0.0F) {
      return;
    }
    PointCalc.scaleJaw(this.p, this.d, paramFloat);
  }
  
  public void calcEyeEnlarge(float paramFloat)
  {
    if (paramFloat == 1.0F) {
      return;
    }
    PointCalc.scaleEyeEnlargePoint(this.q, this.i, paramFloat);
    PointCalc.scaleEyeEnlargePoint(this.r, this.j, paramFloat);
  }
  
  public void calcEyeDis(float paramFloat)
  {
    if (paramFloat == 0.0F) {
      return;
    }
    PointF localPointF = PointCalc.center(this.i, this.j);
    PointCalc.movePoints(this.q, localPointF, paramFloat);
    PointCalc.movePoints(this.r, localPointF, paramFloat);
  }
  
  public void calcEyeAngle(float paramFloat)
  {
    if (paramFloat == 0.0F) {
      return;
    }
    PointCalc.rotatePoints(this.q, this.i, paramFloat);
    PointCalc.rotatePoints(this.r, this.j, paramFloat * -1.0F);
  }
  
  public void calcNose(float paramFloat)
  {
    if (paramFloat == 1.0F) {
      return;
    }
    PointF localPointF1 = PointCalc.pointOfPercentage(this.m, (PointF)this.s.get(1), paramFloat);
    PointF localPointF2 = PointCalc.pointOfPercentage(this.m, (PointF)this.s.get(2), paramFloat);
    this.s.set(1, localPointF1);
    this.s.set(2, localPointF2);
  }
  
  public void calcMouth(float paramFloat)
  {
    if (paramFloat == 1.0F) {
      return;
    }
    PointF localPointF1 = PointCalc.pointOfPercentage(this.o, (PointF)this.t.get(0), paramFloat);
    PointF localPointF2 = PointCalc.pointOfPercentage(this.o, (PointF)this.t.get(1), paramFloat);
    PointF localPointF3 = PointCalc.pointOfPercentage((PointF)this.t.get(3), (PointF)this.t.get(2), paramFloat);
    localPointF3 = PointCalc.pointOfPercentage((PointF)this.t.get(5), localPointF3, paramFloat);
    PointF localPointF4 = PointCalc.pointOfPercentage((PointF)this.t.get(6), (PointF)this.t.get(3), paramFloat);
    PointF localPointF5 = PointCalc.pointOfPercentage((PointF)this.t.get(3), (PointF)this.t.get(4), paramFloat);
    localPointF5 = PointCalc.pointOfPercentage((PointF)this.t.get(7), localPointF5, paramFloat);
    PointF localPointF6 = PointCalc.pointOfPercentage((PointF)this.t.get(6), (PointF)this.t.get(5), paramFloat);
    PointF localPointF7 = PointCalc.pointOfPercentage((PointF)this.t.get(6), (PointF)this.t.get(7), paramFloat);
    PointF localPointF8 = PointCalc.pointOfPercentage((PointF)this.t.get(9), (PointF)this.t.get(8), paramFloat);
    localPointF8 = PointCalc.pointOfPercentage((PointF)this.t.get(11), localPointF8, paramFloat);
    PointF localPointF9 = PointCalc.pointOfPercentage((PointF)this.t.get(12), (PointF)this.t.get(9), paramFloat);
    PointF localPointF10 = PointCalc.pointOfPercentage((PointF)this.t.get(9), (PointF)this.t.get(10), paramFloat);
    localPointF10 = PointCalc.pointOfPercentage((PointF)this.t.get(13), localPointF10, paramFloat);
    PointF localPointF11 = PointCalc.pointOfPercentage((PointF)this.t.get(12), (PointF)this.t.get(11), paramFloat);
    PointF localPointF12 = PointCalc.pointOfPercentage((PointF)this.t.get(12), (PointF)this.t.get(13), paramFloat);
    this.t.set(0, localPointF1);
    this.t.set(1, localPointF2);
    this.t.set(2, localPointF3);
    this.t.set(3, localPointF4);
    this.t.set(4, localPointF5);
    this.t.set(5, localPointF6);
    this.t.set(7, localPointF7);
    this.t.set(8, localPointF8);
    this.t.set(9, localPointF9);
    this.t.set(10, localPointF10);
    this.t.set(11, localPointF11);
    this.t.set(13, localPointF12);
  }
  
  public void calcArchEyebrow(float paramFloat)
  {
    if (paramFloat == 0.0F) {
      return;
    }
    PointCalc.calcArchEyebrow(this.u, this.k, paramFloat);
    PointCalc.calcArchEyebrow(this.v, this.l, paramFloat);
  }
  
  public int[] fillFace()
  {
    return this.a;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\liveSticker\TuSdkPlasticFaceInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */