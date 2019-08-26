package org.lasque.tusdk.core.utils.calc;

import android.graphics.PointF;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Delaunay
{
  final int a = 1;
  final int b = 0;
  final int c = -1;
  final int d = -1;
  final int e = 0;
  final int f = 1;
  final double g = 0.0D;
  final double h = 1.0D;
  final double i = 2.0D;
  final double j = 4.0D;
  tri_delaunay2d k = null;
  int l = 0;
  
  private double a(mat3 parammat3)
  {
    double d1 = parammat3.a[0][0] * (parammat3.a[1][1] * parammat3.a[2][2] - parammat3.a[1][2] * parammat3.a[2][1]) - parammat3.a[0][1] * (parammat3.a[1][0] * parammat3.a[2][2] - parammat3.a[1][2] * parammat3.a[2][0]) + parammat3.a[0][2] * (parammat3.a[1][0] * parammat3.a[2][1] - parammat3.a[1][1] * parammat3.a[2][0]);
    return d1;
  }
  
  public Delaunay(List<PointF> paramList)
  {
    PointF[] arrayOfPointF = new PointF[paramList.size()];
    paramList.toArray(arrayOfPointF);
    a(arrayOfPointF);
  }
  
  public Delaunay(PointF[] paramArrayOfPointF)
  {
    a(paramArrayOfPointF);
  }
  
  private void a(PointF[] paramArrayOfPointF)
  {
    this.k = null;
    this.l = 0;
    Del_Point2d[] arrayOfDel_Point2d = new Del_Point2d[paramArrayOfPointF.length];
    for (int n = 0; n < paramArrayOfPointF.length; n++)
    {
      arrayOfDel_Point2d[n] = new Del_Point2d(null);
      PointF localPointF = paramArrayOfPointF[n];
      arrayOfDel_Point2d[n].a = localPointF.x;
      arrayOfDel_Point2d[n].b = localPointF.y;
      arrayOfDel_Point2d[n].c = n;
    }
    Arrays.sort(arrayOfDel_Point2d);
    n = paramArrayOfPointF.length;
    int i1 = 0;
    for (int i2 = 0; i2 < n - 1 - i1; i2++)
    {
      if (i1 > 0) {
        arrayOfDel_Point2d[i2] = arrayOfDel_Point2d[(i2 + i1)];
      }
      while ((i2 < n - 1 - i1) && (arrayOfDel_Point2d[i2].compareTo(arrayOfDel_Point2d[(i2 + 1 + i1)]) == 0)) {
        i1++;
      }
      if (i2 >= n - 1 - i1) {
        break;
      }
    }
    if (i1 > 0)
    {
      arrayOfDel_Point2d[i2] = arrayOfDel_Point2d[(i2 + i1)];
      arrayOfDel_Point2d[(i2 + 1)] = null;
    }
    n -= i1;
    Delaunay2d localDelaunay2d = null;
    try
    {
      localDelaunay2d = a(arrayOfDel_Point2d, n);
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
    }
    if ((n >= 3) && (localDelaunay2d != null)) {
      this.k = a(localDelaunay2d);
    }
  }
  
  private int a(Point2d paramPoint2d1, Point2d paramPoint2d2, Point2d paramPoint2d3)
  {
    double d1 = paramPoint2d2.a - paramPoint2d1.a;
    double d2 = paramPoint2d2.b - paramPoint2d1.b;
    double d3 = paramPoint2d3.a - paramPoint2d1.a;
    double d4 = paramPoint2d3.b - paramPoint2d1.b;
    double d5 = d1 * d4 - d2 * d3;
    if (d5 < 0.0D) {
      return 1;
    }
    if (d5 > 0.0D) {
      return -1;
    }
    return 0;
  }
  
  private int a(Halfedge paramHalfedge, Point2d paramPoint2d)
  {
    Point2d localPoint2d1 = paramHalfedge.a;
    Point2d localPoint2d2 = paramHalfedge.b.a;
    return a(localPoint2d1, localPoint2d2, paramPoint2d);
  }
  
  private int a(Point2d paramPoint2d1, Point2d paramPoint2d2, Point2d paramPoint2d3, Point2d paramPoint2d4)
  {
    if ((paramPoint2d1 == null) || (paramPoint2d2 == null) || (paramPoint2d3 == null) || (paramPoint2d4 == null)) {
      return 0;
    }
    mat3 localmat3 = new mat3(null);
    double d1 = paramPoint2d1.a - paramPoint2d4.a;
    double d2 = paramPoint2d1.b - paramPoint2d4.b;
    double d3 = paramPoint2d2.a - paramPoint2d4.a;
    double d4 = paramPoint2d2.b - paramPoint2d4.b;
    double d5 = paramPoint2d3.a - paramPoint2d4.a;
    double d6 = paramPoint2d3.b - paramPoint2d4.b;
    double d7 = d1 * d1 + d2 * d2;
    double d8 = d3 * d3 + d4 * d4;
    double d9 = d5 * d5 + d6 * d6;
    localmat3.a[0][0] = d1;
    localmat3.a[0][1] = d2;
    localmat3.a[0][2] = d7;
    localmat3.a[1][0] = d3;
    localmat3.a[1][1] = d4;
    localmat3.a[1][2] = d8;
    localmat3.a[2][0] = d5;
    localmat3.a[2][1] = d6;
    localmat3.a[2][2] = d9;
    double d10 = -a(localmat3);
    if (d10 < 0.0D) {
      return 1;
    }
    if (d10 > 0.0D) {
      return -1;
    }
    return 0;
  }
  
  private int a(DelaunaySegment paramDelaunaySegment, int paramInt)
  {
    paramDelaunaySegment.f = paramInt;
    paramDelaunaySegment.g = (paramInt + 1);
    Point2d localPoint2d1 = paramDelaunaySegment.c[paramInt];
    Point2d localPoint2d2 = paramDelaunaySegment.c[(paramInt + 1)];
    Halfedge localHalfedge1 = new Halfedge(null);
    Halfedge localHalfedge2 = new Halfedge(null);
    localHalfedge1.a = localPoint2d1;
    localHalfedge2.a = localPoint2d2;
    localHalfedge1.c = (localHalfedge1.d = localHalfedge1);
    localHalfedge2.c = (localHalfedge2.d = localHalfedge2);
    localHalfedge1.b = localHalfedge2;
    localHalfedge2.b = localHalfedge1;
    localPoint2d1.c = localHalfedge1;
    localPoint2d2.c = localHalfedge2;
    paramDelaunaySegment.a = localHalfedge2;
    paramDelaunaySegment.b = localHalfedge1;
    return 0;
  }
  
  private int b(DelaunaySegment paramDelaunaySegment, int paramInt)
  {
    paramDelaunaySegment.f = paramInt;
    paramDelaunaySegment.g = (paramInt + 2);
    Point2d localPoint2d1 = paramDelaunaySegment.c[paramInt];
    Point2d localPoint2d2 = paramDelaunaySegment.c[(paramInt + 1)];
    Point2d localPoint2d3 = paramDelaunaySegment.c[(paramInt + 2)];
    Halfedge localHalfedge1 = new Halfedge(null);
    Halfedge localHalfedge2 = new Halfedge(null);
    Halfedge localHalfedge3 = new Halfedge(null);
    Halfedge localHalfedge4 = new Halfedge(null);
    Halfedge localHalfedge5 = new Halfedge(null);
    Halfedge localHalfedge6 = new Halfedge(null);
    if (a(localPoint2d1, localPoint2d3, localPoint2d2) == -1)
    {
      localHalfedge1.a = localPoint2d1;
      localHalfedge2.a = localPoint2d3;
      localHalfedge3.a = localPoint2d2;
      localHalfedge4.a = localPoint2d3;
      localHalfedge5.a = localPoint2d2;
      localHalfedge6.a = localPoint2d1;
      localPoint2d1.c = localHalfedge1;
      localPoint2d2.c = localHalfedge3;
      localPoint2d3.c = localHalfedge2;
      localHalfedge1.c = localHalfedge6;
      localHalfedge1.d = localHalfedge6;
      localHalfedge2.c = localHalfedge4;
      localHalfedge2.d = localHalfedge4;
      localHalfedge3.c = localHalfedge5;
      localHalfedge3.d = localHalfedge5;
      localHalfedge4.c = localHalfedge2;
      localHalfedge4.d = localHalfedge2;
      localHalfedge5.c = localHalfedge3;
      localHalfedge5.d = localHalfedge3;
      localHalfedge6.c = localHalfedge1;
      localHalfedge6.d = localHalfedge1;
      localHalfedge1.b = localHalfedge4;
      localHalfedge4.b = localHalfedge1;
      localHalfedge2.b = localHalfedge5;
      localHalfedge5.b = localHalfedge2;
      localHalfedge3.b = localHalfedge6;
      localHalfedge6.b = localHalfedge3;
      paramDelaunaySegment.a = localHalfedge2;
      paramDelaunaySegment.b = localHalfedge1;
    }
    else
    {
      localHalfedge1.a = localPoint2d1;
      localHalfedge2.a = localPoint2d2;
      localHalfedge3.a = localPoint2d3;
      localHalfedge4.a = localPoint2d2;
      localHalfedge5.a = localPoint2d3;
      localHalfedge6.a = localPoint2d1;
      localPoint2d1.c = localHalfedge1;
      localPoint2d2.c = localHalfedge2;
      localPoint2d3.c = localHalfedge3;
      localHalfedge1.c = localHalfedge6;
      localHalfedge1.d = localHalfedge6;
      localHalfedge2.c = localHalfedge4;
      localHalfedge2.d = localHalfedge4;
      localHalfedge3.c = localHalfedge5;
      localHalfedge3.d = localHalfedge5;
      localHalfedge4.c = localHalfedge2;
      localHalfedge4.d = localHalfedge2;
      localHalfedge5.c = localHalfedge3;
      localHalfedge5.d = localHalfedge3;
      localHalfedge6.c = localHalfedge1;
      localHalfedge6.d = localHalfedge1;
      localHalfedge1.b = localHalfedge4;
      localHalfedge4.b = localHalfedge1;
      localHalfedge2.b = localHalfedge5;
      localHalfedge5.b = localHalfedge2;
      localHalfedge3.b = localHalfedge6;
      localHalfedge6.b = localHalfedge3;
      paramDelaunaySegment.a = localHalfedge3;
      paramDelaunaySegment.b = localHalfedge1;
    }
    return 0;
  }
  
  private void a(Halfedge paramHalfedge)
  {
    Halfedge localHalfedge4 = paramHalfedge.b;
    Halfedge localHalfedge1 = paramHalfedge.c;
    Halfedge localHalfedge2 = paramHalfedge.d;
    Halfedge localHalfedge3 = paramHalfedge.b;
    if ((!m) && (localHalfedge1 == null)) {
      throw new AssertionError();
    }
    if ((!m) && (localHalfedge2 == null)) {
      throw new AssertionError();
    }
    localHalfedge1.d = localHalfedge2;
    localHalfedge2.c = localHalfedge1;
    if (localHalfedge3 != null) {
      localHalfedge3.b = null;
    }
    if (paramHalfedge.a.c == paramHalfedge) {
      paramHalfedge.a.c = localHalfedge1;
    }
    paramHalfedge.a = null;
    paramHalfedge.c = null;
    paramHalfedge.d = null;
    paramHalfedge.b = null;
    localHalfedge1 = localHalfedge4.c;
    localHalfedge2 = localHalfedge4.d;
    localHalfedge3 = localHalfedge4.b;
    if ((!m) && (localHalfedge1 == null)) {
      throw new AssertionError();
    }
    if ((!m) && (localHalfedge2 == null)) {
      throw new AssertionError();
    }
    localHalfedge1.d = localHalfedge2;
    localHalfedge2.c = localHalfedge1;
    if (localHalfedge3 != null) {
      localHalfedge3.b = null;
    }
    if (localHalfedge4.a.c == localHalfedge4) {
      localHalfedge4.a.c = localHalfedge1;
    }
    localHalfedge4.a = null;
    localHalfedge4.c = null;
    localHalfedge4.d = null;
    localHalfedge4.b = null;
  }
  
  private Halfedge b(Halfedge paramHalfedge)
  {
    Point2d localPoint2d1 = paramHalfedge.a;
    Halfedge localHalfedge3 = paramHalfedge;
    Point2d localPoint2d2 = paramHalfedge.b.a;
    paramHalfedge = paramHalfedge.c;
    Point2d localPoint2d3 = paramHalfedge.b.a;
    Halfedge localHalfedge2 = paramHalfedge.b;
    Point2d localPoint2d4 = paramHalfedge.c.b.a;
    if (a(localPoint2d1, localPoint2d2, localPoint2d3) == -1)
    {
      if ((!m) && (localPoint2d4 == localPoint2d3)) {
        throw new AssertionError();
      }
      while ((localPoint2d4 != localPoint2d2) && (localPoint2d4 != localPoint2d1) && (a(localPoint2d1, localPoint2d2, localPoint2d3, localPoint2d4) == 1))
      {
        Halfedge localHalfedge1 = paramHalfedge.c;
        localHalfedge2 = paramHalfedge.c.b;
        a(paramHalfedge);
        paramHalfedge = localHalfedge1;
        localPoint2d3 = localHalfedge2.a;
        localPoint2d4 = paramHalfedge.c.b.a;
      }
      if ((!m) && (localPoint2d4 == localPoint2d3)) {
        throw new AssertionError();
      }
      if ((localPoint2d4 != localPoint2d2) && (localPoint2d4 != localPoint2d1) && (a(localPoint2d1, localPoint2d2, localPoint2d3, localPoint2d4) == 0))
      {
        localHalfedge2 = localHalfedge2.d;
        a(paramHalfedge);
      }
    }
    else
    {
      localHalfedge2 = localHalfedge3;
    }
    if ((!m) && (localHalfedge2.b == null)) {
      throw new AssertionError();
    }
    return localHalfedge2;
  }
  
  private Halfedge c(Halfedge paramHalfedge)
  {
    paramHalfedge = paramHalfedge.b;
    Point2d localPoint2d1 = paramHalfedge.a;
    Halfedge localHalfedge2 = paramHalfedge;
    Point2d localPoint2d2 = paramHalfedge.b.a;
    paramHalfedge = paramHalfedge.d;
    Point2d localPoint2d3 = paramHalfedge.b.a;
    Halfedge localHalfedge3 = paramHalfedge.b;
    Point2d localPoint2d4 = paramHalfedge.d.b.a;
    if (a(localPoint2d2, localPoint2d1, localPoint2d3) == -1)
    {
      if ((!m) && (localPoint2d4 == localPoint2d3)) {
        throw new AssertionError();
      }
      while ((localPoint2d4 != localPoint2d2) && (localPoint2d4 != localPoint2d1) && (a(localPoint2d2, localPoint2d1, localPoint2d3, localPoint2d4) == 1))
      {
        Halfedge localHalfedge1 = paramHalfedge.d;
        localHalfedge3 = localHalfedge1.b;
        a(paramHalfedge);
        paramHalfedge = localHalfedge1;
        localPoint2d3 = localHalfedge3.a;
        localPoint2d4 = paramHalfedge.d.b.a;
      }
      if ((!m) && (localPoint2d4 == localPoint2d3)) {
        throw new AssertionError();
      }
      if ((localPoint2d4 != localPoint2d2) && (localPoint2d4 != localPoint2d1) && (a(localPoint2d2, localPoint2d1, localPoint2d3, localPoint2d4) == 0))
      {
        localHalfedge3 = localHalfedge3.c;
        a(paramHalfedge);
      }
    }
    else
    {
      localHalfedge3 = localHalfedge2;
    }
    if ((!m) && (localHalfedge3.b == null)) {
      throw new AssertionError();
    }
    return localHalfedge3;
  }
  
  private Halfedge d(Halfedge paramHalfedge)
  {
    Point2d localPoint2d1 = paramHalfedge.a;
    Halfedge localHalfedge1 = b(paramHalfedge);
    Point2d localPoint2d2 = localHalfedge1.a;
    if ((!m) && (paramHalfedge.b == null)) {
      throw new AssertionError();
    }
    Point2d localPoint2d3 = paramHalfedge.b.a;
    Halfedge localHalfedge2 = c(paramHalfedge);
    Point2d localPoint2d4 = localHalfedge2.a;
    if ((!m) && (paramHalfedge.b == null)) {
      throw new AssertionError();
    }
    if ((localPoint2d1 != localPoint2d2) && (localPoint2d3 != localPoint2d4))
    {
      int n = a(localPoint2d1, localPoint2d3, localPoint2d2, localPoint2d4);
      if (n != 0) {
        if (n == 1) {
          localHalfedge1 = paramHalfedge;
        } else {
          localHalfedge2 = paramHalfedge.b;
        }
      }
    }
    Halfedge localHalfedge3 = new Halfedge(null);
    Halfedge localHalfedge4 = new Halfedge(null);
    localHalfedge3.a = localHalfedge1.a;
    localHalfedge3.b = localHalfedge4;
    localHalfedge3.d = localHalfedge1;
    localHalfedge3.c = localHalfedge1.c;
    localHalfedge1.c.d = localHalfedge3;
    localHalfedge1.c = localHalfedge3;
    localHalfedge4.a = localHalfedge2.a;
    localHalfedge4.b = localHalfedge3;
    localHalfedge4.d = localHalfedge2.d;
    localHalfedge2.d.c = localHalfedge4;
    localHalfedge4.c = localHalfedge2;
    localHalfedge2.d = localHalfedge4;
    return localHalfedge3;
  }
  
  private Halfedge a(DelaunaySegment paramDelaunaySegment1, DelaunaySegment paramDelaunaySegment2)
  {
    Halfedge localHalfedge2 = paramDelaunaySegment1.a;
    Halfedge localHalfedge1 = paramDelaunaySegment2.b;
    int n;
    int i1;
    do
    {
      Point2d localPoint2d1 = localHalfedge2.d.b.a;
      Point2d localPoint2d2 = localHalfedge1.b.a;
      if ((n = a(localHalfedge2.a, localHalfedge1.a, localPoint2d1)) == 1) {
        localHalfedge2 = localHalfedge2.d.b;
      }
      if ((i1 = a(localHalfedge2.a, localHalfedge1.a, localPoint2d2)) == 1) {
        localHalfedge1 = localHalfedge1.b.c;
      }
    } while ((n == 1) || (i1 == 1));
    Halfedge localHalfedge3 = new Halfedge(null);
    Halfedge localHalfedge4 = new Halfedge(null);
    localHalfedge3.a = localHalfedge2.a;
    localHalfedge3.b = localHalfedge4;
    localHalfedge3.d = localHalfedge2.d;
    localHalfedge2.d.c = localHalfedge3;
    localHalfedge3.c = localHalfedge2;
    localHalfedge2.d = localHalfedge3;
    localHalfedge4.a = localHalfedge1.a;
    localHalfedge4.b = localHalfedge3;
    localHalfedge4.d = localHalfedge1.d;
    localHalfedge1.d.c = localHalfedge4;
    localHalfedge4.c = localHalfedge1;
    localHalfedge1.d = localHalfedge4;
    return localHalfedge3;
  }
  
  private void a(DelaunaySegment paramDelaunaySegment1, DelaunaySegment paramDelaunaySegment2, DelaunaySegment paramDelaunaySegment3)
  {
    if ((!m) && (paramDelaunaySegment2.c != paramDelaunaySegment3.c)) {
      throw new AssertionError();
    }
    Point2d localPoint2d3 = paramDelaunaySegment2.b.a;
    Point2d localPoint2d4 = paramDelaunaySegment3.a.a;
    Halfedge localHalfedge = a(paramDelaunaySegment2, paramDelaunaySegment3);
    Point2d localPoint2d1 = localHalfedge.c.b.a;
    for (Point2d localPoint2d2 = localHalfedge.b.d.b.a; (a(localHalfedge, localPoint2d1) == -1) || (a(localHalfedge, localPoint2d2) == -1); localPoint2d2 = localHalfedge.b.d.b.a)
    {
      localHalfedge = d(localHalfedge);
      localPoint2d1 = localHalfedge.c.b.a;
    }
    paramDelaunaySegment3.a = localPoint2d4.c;
    paramDelaunaySegment2.b = localPoint2d3.c;
    while (a(paramDelaunaySegment3.a, paramDelaunaySegment3.a.d.b.a) == 1) {
      paramDelaunaySegment3.a = paramDelaunaySegment3.a.d;
    }
    while (a(paramDelaunaySegment2.b, paramDelaunaySegment2.b.d.b.a) == 1) {
      paramDelaunaySegment2.b = paramDelaunaySegment2.b.d;
    }
    paramDelaunaySegment1.b = paramDelaunaySegment2.b;
    paramDelaunaySegment1.a = paramDelaunaySegment3.a;
    paramDelaunaySegment1.c = paramDelaunaySegment2.c;
    paramDelaunaySegment1.f = paramDelaunaySegment2.f;
    paramDelaunaySegment1.g = paramDelaunaySegment3.g;
  }
  
  private void a(DelaunaySegment paramDelaunaySegment, int paramInt1, int paramInt2)
  {
    DelaunaySegment localDelaunaySegment1 = new DelaunaySegment(null);
    DelaunaySegment localDelaunaySegment2 = new DelaunaySegment(null);
    int i1 = paramInt2 - paramInt1 + 1;
    if (i1 > 3)
    {
      int n = i1 / 2 + (i1 & 0x1);
      localDelaunaySegment1.c = paramDelaunaySegment.c;
      localDelaunaySegment2.c = paramDelaunaySegment.c;
      a(localDelaunaySegment1, paramInt1, paramInt1 + n - 1);
      a(localDelaunaySegment2, paramInt1 + n, paramInt2);
      a(paramDelaunaySegment, localDelaunaySegment1, localDelaunaySegment2);
    }
    else if (i1 == 3)
    {
      b(paramDelaunaySegment, paramInt1);
    }
    else if (i1 == 2)
    {
      a(paramDelaunaySegment, paramInt1);
    }
  }
  
  private void a(DelaunaySegment paramDelaunaySegment, Halfedge paramHalfedge)
  {
    if (paramHalfedge.e != null) {
      return;
    }
    Face[] arrayOfFace = new Face[paramDelaunaySegment.e + 1];
    if ((!m) && ((null == arrayOfFace) || ((paramDelaunaySegment.e != 0) && (null == paramDelaunaySegment.d)))) {
      throw new AssertionError();
    }
    for (int n = 0; n < paramDelaunaySegment.e; n++) {
      arrayOfFace[n] = paramDelaunaySegment.d[n];
    }
    arrayOfFace[paramDelaunaySegment.e] = new Face(null);
    paramDelaunaySegment.d = arrayOfFace;
    Face localFace = paramDelaunaySegment.d[paramDelaunaySegment.e];
    Halfedge localHalfedge = paramHalfedge;
    localFace.a = paramHalfedge;
    localFace.b = 0;
    do
    {
      localHalfedge.e = localFace;
      localFace.b += 1;
      localHalfedge = localHalfedge.b.d;
    } while (localHalfedge != paramHalfedge);
    paramDelaunaySegment.e += 1;
  }
  
  private void a(DelaunaySegment paramDelaunaySegment)
  {
    paramDelaunaySegment.e = 0;
    paramDelaunaySegment.d = null;
    a(paramDelaunaySegment, paramDelaunaySegment.a.b);
    for (int n = paramDelaunaySegment.f; n <= paramDelaunaySegment.g; n++)
    {
      Halfedge localHalfedge = paramDelaunaySegment.c[n].c;
      do
      {
        a(paramDelaunaySegment, localHalfedge);
        localHalfedge = localHalfedge.c;
      } while (localHalfedge != paramDelaunaySegment.c[n].c);
    }
  }
  
  private Delaunay2d a(Del_Point2d[] paramArrayOfDel_Point2d, int paramInt)
  {
    Delaunay2d localDelaunay2d = null;
    DelaunaySegment localDelaunaySegment = new DelaunaySegment(null);
    int i2 = 0;
    int[] arrayOfInt = null;
    localDelaunaySegment.c = new Point2d[paramInt];
    if ((!m) && (null == localDelaunaySegment.c)) {
      throw new AssertionError();
    }
    for (int n = 0; n < paramInt; n++)
    {
      localDelaunaySegment.c[n] = new Point2d(null);
      localDelaunaySegment.c[n].d = paramArrayOfDel_Point2d[n].c;
      localDelaunaySegment.c[n].a = paramArrayOfDel_Point2d[n].a;
      localDelaunaySegment.c[n].b = paramArrayOfDel_Point2d[n].b;
    }
    Arrays.sort(localDelaunaySegment.c);
    if (paramInt >= 3)
    {
      a(localDelaunaySegment, 0, paramInt - 1);
      a(localDelaunaySegment);
      i2 = 0;
      for (n = 0; n < localDelaunaySegment.e; n++) {
        i2 += localDelaunaySegment.d[n].b + 1;
      }
      arrayOfInt = new int[i2];
      if ((!m) && (null == arrayOfInt)) {
        throw new AssertionError();
      }
      int i1 = 0;
      for (n = 0; n < localDelaunaySegment.e; n++)
      {
        arrayOfInt[i1] = localDelaunaySegment.d[n].b;
        i1++;
        Halfedge localHalfedge = localDelaunaySegment.d[n].a;
        do
        {
          arrayOfInt[i1] = localHalfedge.a.d;
          i1++;
          localHalfedge = localHalfedge.b.d;
        } while (localHalfedge != localDelaunaySegment.d[n].a);
      }
      localDelaunaySegment.d = null;
      localDelaunaySegment.c = null;
    }
    localDelaunay2d = new Delaunay2d(null);
    if ((!m) && (null == localDelaunay2d)) {
      throw new AssertionError();
    }
    localDelaunay2d.a = paramInt;
    localDelaunay2d.b = new Del_Point2d[paramInt];
    if ((!m) && (null == localDelaunay2d.b)) {
      throw new AssertionError();
    }
    for (int i3 = 0; i3 < paramInt; i3++) {
      localDelaunay2d.b[i3] = paramArrayOfDel_Point2d[i3].clone();
    }
    localDelaunay2d.c = localDelaunaySegment.e;
    localDelaunay2d.d = arrayOfInt;
    return localDelaunay2d;
  }
  
  private tri_delaunay2d a(Delaunay2d paramDelaunay2d)
  {
    int n = paramDelaunay2d.d[0] + 1;
    int i1 = 0;
    tri_delaunay2d localtri_delaunay2d = new tri_delaunay2d(null);
    localtri_delaunay2d.c = 0;
    int i2;
    if (1 == paramDelaunay2d.c)
    {
      i3 = paramDelaunay2d.d[0];
      localtri_delaunay2d.c += i3 - 2;
    }
    else
    {
      for (i2 = 1; i2 < paramDelaunay2d.c; i2++)
      {
        i3 = paramDelaunay2d.d[n];
        localtri_delaunay2d.c += i3 - 2;
        n += i3 + 1;
      }
    }
    localtri_delaunay2d.a = paramDelaunay2d.a;
    localtri_delaunay2d.b = new Del_Point2d[paramDelaunay2d.a];
    if ((!m) && (null == localtri_delaunay2d.b)) {
      throw new AssertionError();
    }
    for (int i3 = 0; i3 < paramDelaunay2d.a; i3++) {
      localtri_delaunay2d.b[i3] = paramDelaunay2d.b[i3].clone();
    }
    localtri_delaunay2d.d = new int[3 * localtri_delaunay2d.c];
    if ((!m) && (null == localtri_delaunay2d.d)) {
      throw new AssertionError();
    }
    n = paramDelaunay2d.d[0] + 1;
    int i4;
    if (1 == paramDelaunay2d.c)
    {
      i3 = paramDelaunay2d.d[0];
      i4 = 0;
      n = 1;
      while (i4 < i3 - 2)
      {
        localtri_delaunay2d.d[i1] = paramDelaunay2d.d[(n + i4)];
        localtri_delaunay2d.d[(i1 + 1)] = paramDelaunay2d.d[((n + i4 + 1) % i3)];
        localtri_delaunay2d.d[(i1 + 2)] = paramDelaunay2d.d[(n + i4)];
        i1 += 3;
        i4++;
      }
    }
    else
    {
      for (i2 = 1; i2 < paramDelaunay2d.c; i2++)
      {
        i3 = paramDelaunay2d.d[n];
        i4 = 0;
        int i5 = paramDelaunay2d.d[(n + 1)];
        while (i4 < i3 - 2)
        {
          localtri_delaunay2d.d[i1] = i5;
          localtri_delaunay2d.d[(i1 + 1)] = paramDelaunay2d.d[(n + i4 + 2)];
          localtri_delaunay2d.d[(i1 + 2)] = paramDelaunay2d.d[(n + i4 + 3)];
          i1 += 3;
          i4++;
        }
        n += i3 + 1;
      }
    }
    return localtri_delaunay2d;
  }
  
  public boolean ready()
  {
    if (this.k == null) {
      return false;
    }
    return this.l < this.k.c;
  }
  
  public void next()
  {
    this.l += 1;
  }
  
  public int getPointsTotal()
  {
    if (this.k.b != null) {
      return this.k.b.length;
    }
    return 0;
  }
  
  public int getNumTriangles()
  {
    return this.k.c;
  }
  
  public List<PointF> getAllPoints()
  {
    ArrayList localArrayList = new ArrayList();
    for (int n = 0; n < this.k.b.length; n++)
    {
      PointF localPointF = new PointF();
      localPointF.x = ((float)this.k.b[n].a);
      localPointF.y = ((float)this.k.b[n].b);
      localArrayList.add(localPointF);
    }
    return localArrayList;
  }
  
  public int[] getTrianglesTris()
  {
    if (this.k == null) {
      return null;
    }
    return this.k.d;
  }
  
  public void retrieve_triangle_points(PointF[] paramArrayOfPointF)
  {
    if (paramArrayOfPointF.length < 3) {
      throw new RuntimeException("retrieve_triangle_points() method must be passed an empty Point[] array of length 3.");
    }
    if (!ready()) {
      throw new RuntimeException("retrieve_triangle_points() called when triangle points are unavailable.");
    }
    for (int n = 0; n < 3; n++)
    {
      int i1 = this.k.d[(this.l * 3 + n)];
      paramArrayOfPointF[n] = new PointF();
      paramArrayOfPointF[n].x = ((float)this.k.b[i1].a);
      paramArrayOfPointF[n].y = ((float)this.k.b[i1].b);
    }
  }
  
  private class Del_Point2d
    implements Cloneable, Comparable
  {
    double a;
    double b;
    int c;
    
    private Del_Point2d() {}
    
    public Del_Point2d clone()
    {
      Del_Point2d localDel_Point2d = new Del_Point2d(Delaunay.this);
      localDel_Point2d.a = this.a;
      localDel_Point2d.b = this.b;
      localDel_Point2d.c = this.c;
      return localDel_Point2d;
    }
    
    public int compareTo(Object paramObject)
    {
      Del_Point2d localDel_Point2d = (Del_Point2d)paramObject;
      if (this.a < localDel_Point2d.a) {
        return -1;
      }
      if (this.a > localDel_Point2d.a) {
        return 1;
      }
      if (this.b < localDel_Point2d.b) {
        return -1;
      }
      if (this.b > localDel_Point2d.b) {
        return 1;
      }
      return 0;
    }
  }
  
  private class tri_delaunay2d
  {
    int a;
    Delaunay.Del_Point2d[] b;
    int c;
    int[] d;
    
    private tri_delaunay2d() {}
  }
  
  private class Delaunay2d
  {
    int a;
    Delaunay.Del_Point2d[] b;
    int c;
    int[] d;
    
    private Delaunay2d() {}
  }
  
  private class working_set_s {}
  
  private class DelaunaySegment
  {
    Delaunay.Halfedge a;
    Delaunay.Halfedge b;
    Delaunay.Point2d[] c;
    Delaunay.Face[] d;
    int e;
    int f;
    int g;
    
    private DelaunaySegment() {}
  }
  
  private class Halfedge
  {
    Delaunay.Point2d a;
    Halfedge b;
    Halfedge c;
    Halfedge d;
    Delaunay.Face e;
    
    private Halfedge() {}
  }
  
  private class Face
  {
    Delaunay.Halfedge a;
    int b;
    
    private Face() {}
  }
  
  private class Point2d
    implements Comparable
  {
    double a;
    double b;
    Delaunay.Halfedge c;
    int d;
    
    private Point2d() {}
    
    public int compareTo(Object paramObject)
    {
      Point2d localPoint2d = (Point2d)paramObject;
      if (this.a < localPoint2d.a) {
        return -1;
      }
      if (this.a > localPoint2d.a) {
        return 1;
      }
      if (this.b < localPoint2d.b) {
        return -1;
      }
      if (this.b > localPoint2d.b) {
        return 1;
      }
      return 0;
    }
  }
  
  private class mat3
  {
    double[][] a = new double[3][3];
    
    private mat3() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\calc\Delaunay.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */