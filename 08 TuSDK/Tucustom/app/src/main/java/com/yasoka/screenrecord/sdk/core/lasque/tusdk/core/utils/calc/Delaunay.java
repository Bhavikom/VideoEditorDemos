// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.calc;

import java.util.ArrayList;
import java.util.Arrays;
import android.graphics.PointF;
import java.util.List;

public class Delaunay
{
    final int a = 1;
    final int b = 0;
    final int c = -1;
    final int d = -1;
    final int e = 0;
    final int f = 1;
    final double g = 0.0;
    final double h = 1.0;
    final double i = 2.0;
    final double j = 4.0;
    tri_delaunay2d k;
    int l;
    static final /* synthetic */ boolean m;
    
    private double a(final mat3 mat3) {
        return mat3.a[0][0] * (mat3.a[1][1] * mat3.a[2][2] - mat3.a[1][2] * mat3.a[2][1]) - mat3.a[0][1] * (mat3.a[1][0] * mat3.a[2][2] - mat3.a[1][2] * mat3.a[2][0]) + mat3.a[0][2] * (mat3.a[1][0] * mat3.a[2][1] - mat3.a[1][1] * mat3.a[2][0]);
    }
    
    public Delaunay(final List<PointF> list) {
        this.k = null;
        this.l = 0;
        final PointF[] array = new PointF[list.size()];
        list.toArray(array);
        this.a(array);
    }
    
    public Delaunay(final PointF[] array) {
        this.k = null;
        this.l = 0;
        this.a(array);
    }
    
    private void a(final PointF[] array) {
        this.k = null;
        this.l = 0;
        final Del_Point2d[] a = new Del_Point2d[array.length];
        for (int i = 0; i < array.length; ++i) {
            a[i] = new Del_Point2d();
            final PointF pointF = array[i];
            a[i].a = pointF.x;
            a[i].b = pointF.y;
            a[i].c = i;
        }
        Arrays.sort(a);
        int length;
        int n;
        int j;
        for (length = array.length, n = 0, j = 0; j < length - 1 - n; ++j) {
            if (n > 0) {
                a[j] = a[j + n];
            }
            while (j < length - 1 - n && a[j].compareTo(a[j + 1 + n]) == 0) {
                ++n;
            }
            if (j >= length - 1 - n) {
                break;
            }
        }
        if (n > 0) {
            a[j] = a[j + n];
            a[j + 1] = null;
        }
        final int n2 = length - n;
        Delaunay2d a2 = null;
        try {
            a2 = this.a(a, n2);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        if (n2 >= 3 && a2 != null) {
            this.k = this.a(a2);
        }
    }
    
    private int a(final Point2d point2d, final Point2d point2d2, final Point2d point2d3) {
        final double n = (point2d2.a - point2d.a) * (point2d3.b - point2d.b) - (point2d2.b - point2d.b) * (point2d3.a - point2d.a);
        if (n < 0.0) {
            return 1;
        }
        if (n > 0.0) {
            return -1;
        }
        return 0;
    }
    
    private int a(final Halfedge halfedge, final Point2d point2d) {
        return this.a(halfedge.a, halfedge.b.a, point2d);
    }
    
    private int a(final Point2d point2d, final Point2d point2d2, final Point2d point2d3, final Point2d point2d4) {
        if (point2d == null || point2d2 == null || point2d3 == null || point2d4 == null) {
            return 0;
        }
        final mat3 mat3 = new mat3();
        final double n = point2d.a - point2d4.a;
        final double n2 = point2d.b - point2d4.b;
        final double n3 = point2d2.a - point2d4.a;
        final double n4 = point2d2.b - point2d4.b;
        final double n5 = point2d3.a - point2d4.a;
        final double n6 = point2d3.b - point2d4.b;
        final double n7 = n * n + n2 * n2;
        final double n8 = n3 * n3 + n4 * n4;
        final double n9 = n5 * n5 + n6 * n6;
        mat3.a[0][0] = n;
        mat3.a[0][1] = n2;
        mat3.a[0][2] = n7;
        mat3.a[1][0] = n3;
        mat3.a[1][1] = n4;
        mat3.a[1][2] = n8;
        mat3.a[2][0] = n5;
        mat3.a[2][1] = n6;
        mat3.a[2][2] = n9;
        final double n10 = -this.a(mat3);
        if (n10 < 0.0) {
            return 1;
        }
        if (n10 > 0.0) {
            return -1;
        }
        return 0;
    }
    
    private int a(final DelaunaySegment delaunaySegment, final int f) {
        delaunaySegment.f = f;
        delaunaySegment.g = f + 1;
        final Point2d a = delaunaySegment.c[f];
        final Point2d a2 = delaunaySegment.c[f + 1];
        final Halfedge b = new Halfedge();
        final Halfedge a3 = new Halfedge();
        b.a = a;
        a3.a = a2;
        final Halfedge halfedge = b;
        final Halfedge halfedge2 = b;
        final Halfedge halfedge3 = b;
        halfedge2.d = halfedge3;
        halfedge.c = halfedge3;
        final Halfedge halfedge4 = a3;
        final Halfedge halfedge5 = a3;
        final Halfedge halfedge6 = a3;
        halfedge5.d = halfedge6;
        halfedge4.c = halfedge6;
        b.b = a3;
        a3.b = b;
        a.c = b;
        a2.c = a3;
        delaunaySegment.a = a3;
        delaunaySegment.b = b;
        return 0;
    }
    
    private int b(final DelaunaySegment delaunaySegment, final int f) {
        delaunaySegment.f = f;
        delaunaySegment.g = f + 2;
        final Point2d point2d = delaunaySegment.c[f];
        final Point2d point2d2 = delaunaySegment.c[f + 1];
        final Point2d point2d3 = delaunaySegment.c[f + 2];
        final Halfedge halfedge = new Halfedge();
        final Halfedge b = new Halfedge();
        final Halfedge a = new Halfedge();
        final Halfedge halfedge2 = new Halfedge();
        final Halfedge halfedge3 = new Halfedge();
        final Halfedge halfedge4 = new Halfedge();
        if (this.a(point2d, point2d3, point2d2) == -1) {
            halfedge.a = point2d;
            b.a = point2d3;
            a.a = point2d2;
            halfedge2.a = point2d3;
            halfedge3.a = point2d2;
            halfedge4.a = point2d;
            point2d.c = halfedge;
            point2d2.c = a;
            point2d3.c = b;
            halfedge.c = halfedge4;
            halfedge.d = halfedge4;
            b.c = halfedge2;
            b.d = halfedge2;
            a.c = halfedge3;
            a.d = halfedge3;
            halfedge2.c = b;
            halfedge2.d = b;
            halfedge3.c = a;
            halfedge3.d = a;
            halfedge4.c = halfedge;
            halfedge4.d = halfedge;
            halfedge.b = halfedge2;
            halfedge2.b = halfedge;
            b.b = halfedge3;
            halfedge3.b = b;
            a.b = halfedge4;
            halfedge4.b = a;
            delaunaySegment.a = b;
            delaunaySegment.b = halfedge;
        }
        else {
            halfedge.a = point2d;
            b.a = point2d2;
            a.a = point2d3;
            halfedge2.a = point2d2;
            halfedge3.a = point2d3;
            halfedge4.a = point2d;
            point2d.c = halfedge;
            point2d2.c = b;
            point2d3.c = a;
            halfedge.c = halfedge4;
            halfedge.d = halfedge4;
            b.c = halfedge2;
            b.d = halfedge2;
            a.c = halfedge3;
            a.d = halfedge3;
            halfedge2.c = b;
            halfedge2.d = b;
            halfedge3.c = a;
            halfedge3.d = a;
            halfedge4.c = halfedge;
            halfedge4.d = halfedge;
            halfedge.b = halfedge2;
            halfedge2.b = halfedge;
            b.b = halfedge3;
            halfedge3.b = b;
            a.b = halfedge4;
            halfedge4.b = a;
            delaunaySegment.a = a;
            delaunaySegment.b = halfedge;
        }
        return 0;
    }
    
    private void a(final Halfedge halfedge) {
        final Halfedge b = halfedge.b;
        final Halfedge c = halfedge.c;
        final Halfedge d = halfedge.d;
        final Halfedge b2 = halfedge.b;
        if (!Delaunay.m && c == null) {
            throw new AssertionError();
        }
        if (!Delaunay.m && d == null) {
            throw new AssertionError();
        }
        c.d = d;
        d.c = c;
        if (b2 != null) {
            b2.b = null;
        }
        if (halfedge.a.c == halfedge) {
            halfedge.a.c = c;
        }
        halfedge.a = null;
        halfedge.c = null;
        halfedge.d = null;
        halfedge.b = null;
        final Halfedge c2 = b.c;
        final Halfedge d2 = b.d;
        final Halfedge b3 = b.b;
        if (!Delaunay.m && c2 == null) {
            throw new AssertionError();
        }
        if (!Delaunay.m && d2 == null) {
            throw new AssertionError();
        }
        c2.d = d2;
        d2.c = c2;
        if (b3 != null) {
            b3.b = null;
        }
        if (b.a.c == b) {
            b.a.c = c2;
        }
        b.a = null;
        b.c = null;
        b.d = null;
        b.b = null;
    }
    
    private Halfedge b(Halfedge c) {
        final Point2d a = c.a;
        final Halfedge halfedge = c;
        final Point2d a2 = c.b.a;
        c = c.c;
        Point2d point2d = c.b.a;
        Halfedge halfedge2 = c.b;
        Point2d point2d2 = c.c.b.a;
        if (this.a(a, a2, point2d) == -1) {
            if (!Delaunay.m && point2d2 == point2d) {
                throw new AssertionError();
            }
            while (point2d2 != a2 && point2d2 != a && this.a(a, a2, point2d, point2d2) == 1) {
                final Halfedge c2 = c.c;
                halfedge2 = c.c.b;
                this.a(c);
                c = c2;
                point2d = halfedge2.a;
                point2d2 = c.c.b.a;
            }
            if (!Delaunay.m && point2d2 == point2d) {
                throw new AssertionError();
            }
            if (point2d2 != a2 && point2d2 != a && this.a(a, a2, point2d, point2d2) == 0) {
                halfedge2 = halfedge2.d;
                this.a(c);
            }
        }
        else {
            halfedge2 = halfedge;
        }
        if (!Delaunay.m && halfedge2.b == null) {
            throw new AssertionError();
        }
        return halfedge2;
    }
    
    private Halfedge c(Halfedge halfedge) {
        halfedge = halfedge.b;
        final Point2d a = halfedge.a;
        final Halfedge halfedge2 = halfedge;
        final Point2d a2 = halfedge.b.a;
        halfedge = halfedge.d;
        Point2d point2d = halfedge.b.a;
        Halfedge halfedge3 = halfedge.b;
        Point2d point2d2 = halfedge.d.b.a;
        if (this.a(a2, a, point2d) == -1) {
            if (!Delaunay.m && point2d2 == point2d) {
                throw new AssertionError();
            }
            while (point2d2 != a2 && point2d2 != a && this.a(a2, a, point2d, point2d2) == 1) {
                final Halfedge d = halfedge.d;
                halfedge3 = d.b;
                this.a(halfedge);
                halfedge = d;
                point2d = halfedge3.a;
                point2d2 = halfedge.d.b.a;
            }
            if (!Delaunay.m && point2d2 == point2d) {
                throw new AssertionError();
            }
            if (point2d2 != a2 && point2d2 != a && this.a(a2, a, point2d, point2d2) == 0) {
                halfedge3 = halfedge3.c;
                this.a(halfedge);
            }
        }
        else {
            halfedge3 = halfedge2;
        }
        if (!Delaunay.m && halfedge3.b == null) {
            throw new AssertionError();
        }
        return halfedge3;
    }
    
    private Halfedge d(final Halfedge halfedge) {
        final Point2d a = halfedge.a;
        Halfedge b = this.b(halfedge);
        final Point2d a2 = b.a;
        if (!Delaunay.m && halfedge.b == null) {
            throw new AssertionError();
        }
        final Point2d a3 = halfedge.b.a;
        Halfedge c = this.c(halfedge);
        final Point2d a4 = c.a;
        if (!Delaunay.m && halfedge.b == null) {
            throw new AssertionError();
        }
        if (a != a2 && a3 != a4) {
            final int a5 = this.a(a, a3, a2, a4);
            if (a5 != 0) {
                if (a5 == 1) {
                    b = halfedge;
                }
                else {
                    c = halfedge.b;
                }
            }
        }
        final Halfedge b2 = new Halfedge();
        final Halfedge d = new Halfedge();
        b2.a = b.a;
        b2.b = d;
        b2.d = b;
        b2.c = b.c;
        b.c.d = b2;
        b.c = b2;
        d.a = c.a;
        d.b = b2;
        d.d = c.d;
        c.d.c = d;
        d.c = c;
        c.d = d;
        return b2;
    }
    
    private Halfedge a(final DelaunaySegment delaunaySegment, final DelaunaySegment delaunaySegment2) {
        Halfedge c = delaunaySegment.a;
        Halfedge c2 = delaunaySegment2.b;
        int a;
        int a2;
        do {
            final Point2d a3 = c.d.b.a;
            final Point2d a4 = c2.b.a;
            if ((a = this.a(c.a, c2.a, a3)) == 1) {
                c = c.d.b;
            }
            if ((a2 = this.a(c.a, c2.a, a4)) == 1) {
                c2 = c2.b.c;
            }
        } while (a == 1 || a2 == 1);
        final Halfedge b = new Halfedge();
        final Halfedge d = new Halfedge();
        b.a = c.a;
        b.b = d;
        b.d = c.d;
        c.d.c = b;
        b.c = c;
        c.d = b;
        d.a = c2.a;
        d.b = b;
        d.d = c2.d;
        c2.d.c = d;
        d.c = c2;
        c2.d = d;
        return b;
    }
    
    private void a(final DelaunaySegment delaunaySegment, final DelaunaySegment delaunaySegment2, final DelaunaySegment delaunaySegment3) {
        if (!Delaunay.m && delaunaySegment2.c != delaunaySegment3.c) {
            throw new AssertionError();
        }
        final Point2d a = delaunaySegment2.b.a;
        final Point2d a2 = delaunaySegment3.a.a;
        Halfedge halfedge = this.a(delaunaySegment2, delaunaySegment3);
        for (Point2d point2d = halfedge.c.b.a, point2d2 = halfedge.b.d.b.a; this.a(halfedge, point2d) == -1 || this.a(halfedge, point2d2) == -1; halfedge = this.d(halfedge), point2d = halfedge.c.b.a, point2d2 = halfedge.b.d.b.a) {}
        delaunaySegment3.a = a2.c;
        delaunaySegment2.b = a.c;
        while (this.a(delaunaySegment3.a, delaunaySegment3.a.d.b.a) == 1) {
            delaunaySegment3.a = delaunaySegment3.a.d;
        }
        while (this.a(delaunaySegment2.b, delaunaySegment2.b.d.b.a) == 1) {
            delaunaySegment2.b = delaunaySegment2.b.d;
        }
        delaunaySegment.b = delaunaySegment2.b;
        delaunaySegment.a = delaunaySegment3.a;
        delaunaySegment.c = delaunaySegment2.c;
        delaunaySegment.f = delaunaySegment2.f;
        delaunaySegment.g = delaunaySegment3.g;
    }
    
    private void a(final DelaunaySegment delaunaySegment, final int n, final int n2) {
        final DelaunaySegment delaunaySegment2 = new DelaunaySegment();
        final DelaunaySegment delaunaySegment3 = new DelaunaySegment();
        final int n3 = n2 - n + 1;
        if (n3 > 3) {
            final int n4 = n3 / 2 + (n3 & 0x1);
            delaunaySegment2.c = delaunaySegment.c;
            delaunaySegment3.c = delaunaySegment.c;
            this.a(delaunaySegment2, n, n + n4 - 1);
            this.a(delaunaySegment3, n + n4, n2);
            this.a(delaunaySegment, delaunaySegment2, delaunaySegment3);
        }
        else if (n3 == 3) {
            this.b(delaunaySegment, n);
        }
        else if (n3 == 2) {
            this.a(delaunaySegment, n);
        }
    }
    
    private void a(final DelaunaySegment delaunaySegment, final Halfedge a) {
        if (a.e != null) {
            return;
        }
        final Face[] d = new Face[delaunaySegment.e + 1];
        if (!Delaunay.m && (null == d || (delaunaySegment.e != 0 && null == delaunaySegment.d))) {
            throw new AssertionError();
        }
        for (int i = 0; i < delaunaySegment.e; ++i) {
            d[i] = delaunaySegment.d[i];
        }
        d[delaunaySegment.e] = new Face();
        delaunaySegment.d = d;
        final Face e = delaunaySegment.d[delaunaySegment.e];
        Halfedge d2 = a;
        e.a = a;
        e.b = 0;
        do {
            d2.e = e;
            final Face face = e;
            ++face.b;
            d2 = d2.b.d;
        } while (d2 != a);
        ++delaunaySegment.e;
    }
    
    private void a(final DelaunaySegment delaunaySegment) {
        delaunaySegment.e = 0;
        delaunaySegment.d = null;
        this.a(delaunaySegment, delaunaySegment.a.b);
        for (int i = delaunaySegment.f; i <= delaunaySegment.g; ++i) {
            Halfedge halfedge = delaunaySegment.c[i].c;
            do {
                this.a(delaunaySegment, halfedge);
                halfedge = halfedge.c;
            } while (halfedge != delaunaySegment.c[i].c);
        }
    }
    
    private Delaunay2d a(final Del_Point2d[] array, final int a) {
        final DelaunaySegment delaunaySegment = new DelaunaySegment();
        int[] d = null;
        delaunaySegment.c = new Point2d[a];
        if (!Delaunay.m && null == delaunaySegment.c) {
            throw new AssertionError();
        }
        for (int i = 0; i < a; ++i) {
            delaunaySegment.c[i] = new Point2d();
            delaunaySegment.c[i].d = array[i].c;
            delaunaySegment.c[i].a = array[i].a;
            delaunaySegment.c[i].b = array[i].b;
        }
        Arrays.sort(delaunaySegment.c);
        if (a >= 3) {
            this.a(delaunaySegment, 0, a - 1);
            this.a(delaunaySegment);
            int n = 0;
            for (int j = 0; j < delaunaySegment.e; ++j) {
                n += delaunaySegment.d[j].b + 1;
            }
            d = new int[n];
            if (!Delaunay.m && null == d) {
                throw new AssertionError();
            }
            int n2 = 0;
            for (int k = 0; k < delaunaySegment.e; ++k) {
                d[n2] = delaunaySegment.d[k].b;
                ++n2;
                Halfedge halfedge = delaunaySegment.d[k].a;
                do {
                    d[n2] = halfedge.a.d;
                    ++n2;
                    halfedge = halfedge.b.d;
                } while (halfedge != delaunaySegment.d[k].a);
            }
            delaunaySegment.d = null;
            delaunaySegment.c = null;
        }
        final Delaunay2d delaunay2d = new Delaunay2d();
        if (!Delaunay.m && null == delaunay2d) {
            throw new AssertionError();
        }
        delaunay2d.a = a;
        delaunay2d.b = new Del_Point2d[a];
        if (!Delaunay.m && null == delaunay2d.b) {
            throw new AssertionError();
        }
        for (int l = 0; l < a; ++l) {
            delaunay2d.b[l] = array[l].clone();
        }
        delaunay2d.c = delaunaySegment.e;
        delaunay2d.d = d;
        return delaunay2d;
    }
    
    private tri_delaunay2d a(final Delaunay2d delaunay2d) {
        int n = delaunay2d.d[0] + 1;
        int n2 = 0;
        final tri_delaunay2d tri_delaunay2d = new tri_delaunay2d();
        tri_delaunay2d.c = 0;
        if (1 == delaunay2d.c) {
            final int n3 = delaunay2d.d[0];
            final tri_delaunay2d tri_delaunay2d2 = tri_delaunay2d;
            tri_delaunay2d2.c += n3 - 2;
        }
        else {
            for (int i = 1; i < delaunay2d.c; ++i) {
                final int n4 = delaunay2d.d[n];
                final tri_delaunay2d tri_delaunay2d3 = tri_delaunay2d;
                tri_delaunay2d3.c += n4 - 2;
                n += n4 + 1;
            }
        }
        tri_delaunay2d.a = delaunay2d.a;
        tri_delaunay2d.b = new Del_Point2d[delaunay2d.a];
        if (!Delaunay.m && null == tri_delaunay2d.b) {
            throw new AssertionError();
        }
        for (int j = 0; j < delaunay2d.a; ++j) {
            tri_delaunay2d.b[j] = delaunay2d.b[j].clone();
        }
        tri_delaunay2d.d = new int[3 * tri_delaunay2d.c];
        if (!Delaunay.m && null == tri_delaunay2d.d) {
            throw new AssertionError();
        }
        int n5 = delaunay2d.d[0] + 1;
        if (1 == delaunay2d.c) {
            final int n6 = delaunay2d.d[0];
            int k = 0;
            final int n7 = 1;
            while (k < n6 - 2) {
                tri_delaunay2d.d[n2] = delaunay2d.d[n7 + k];
                tri_delaunay2d.d[n2 + 1] = delaunay2d.d[(n7 + k + 1) % n6];
                tri_delaunay2d.d[n2 + 2] = delaunay2d.d[n7 + k];
                n2 += 3;
                ++k;
            }
        }
        else {
            for (int l = 1; l < delaunay2d.c; ++l) {
                final int n8 = delaunay2d.d[n5];
                int n9 = 0;
                final int n10 = delaunay2d.d[n5 + 1];
                while (n9 < n8 - 2) {
                    tri_delaunay2d.d[n2] = n10;
                    tri_delaunay2d.d[n2 + 1] = delaunay2d.d[n5 + n9 + 2];
                    tri_delaunay2d.d[n2 + 2] = delaunay2d.d[n5 + n9 + 3];
                    n2 += 3;
                    ++n9;
                }
                n5 += n8 + 1;
            }
        }
        return tri_delaunay2d;
    }
    
    public boolean ready() {
        return this.k != null && this.l < this.k.c;
    }
    
    public void next() {
        ++this.l;
    }
    
    public int getPointsTotal() {
        if (this.k.b != null) {
            return this.k.b.length;
        }
        return 0;
    }
    
    public int getNumTriangles() {
        return this.k.c;
    }
    
    public List<PointF> getAllPoints() {
        final ArrayList<PointF> list = new ArrayList<PointF>();
        for (int i = 0; i < this.k.b.length; ++i) {
            final PointF pointF = new PointF();
            pointF.x = (float)this.k.b[i].a;
            pointF.y = (float)this.k.b[i].b;
            list.add(pointF);
        }
        return list;
    }
    
    public int[] getTrianglesTris() {
        if (this.k == null) {
            return null;
        }
        return this.k.d;
    }
    
    public void retrieve_triangle_points(final PointF[] array) {
        if (array.length < 3) {
            throw new RuntimeException("retrieve_triangle_points() method must be passed an empty Point[] array of length 3.");
        }
        if (!this.ready()) {
            throw new RuntimeException("retrieve_triangle_points() called when triangle points are unavailable.");
        }
        for (int i = 0; i < 3; ++i) {
            final int n = this.k.d[this.l * 3 + i];
            array[i] = new PointF();
            array[i].x = (float)this.k.b[n].a;
            array[i].y = (float)this.k.b[n].b;
        }
    }
    
    static {
        m = !Delaunay.class.desiredAssertionStatus();
    }
    
    private class Del_Point2d implements Cloneable, Comparable
    {
        double a;
        double b;
        int c;
        
        public Del_Point2d clone() {
            final Del_Point2d del_Point2d = new Del_Point2d();
            del_Point2d.a = this.a;
            del_Point2d.b = this.b;
            del_Point2d.c = this.c;
            return del_Point2d;
        }
        
        @Override
        public int compareTo(final Object o) {
            final Del_Point2d del_Point2d = (Del_Point2d)o;
            if (this.a < del_Point2d.a) {
                return -1;
            }
            if (this.a > del_Point2d.a) {
                return 1;
            }
            if (this.b < del_Point2d.b) {
                return -1;
            }
            if (this.b > del_Point2d.b) {
                return 1;
            }
            return 0;
        }
    }
    
    private class tri_delaunay2d
    {
        int a;
        Del_Point2d[] b;
        int c;
        int[] d;
    }
    
    private class Delaunay2d
    {
        int a;
        Del_Point2d[] b;
        int c;
        int[] d;
    }
    
    private class working_set_s
    {
    }
    
    private class Halfedge
    {
        Point2d a;
        Halfedge b;
        Halfedge c;
        Halfedge d;
        Face e;
    }
    
    private class Point2d implements Comparable
    {
        double a;
        double b;
        Halfedge c;
        int d;
        
        @Override
        public int compareTo(final Object o) {
            final Point2d point2d = (Point2d)o;
            if (this.a < point2d.a) {
                return -1;
            }
            if (this.a > point2d.a) {
                return 1;
            }
            if (this.b < point2d.b) {
                return -1;
            }
            if (this.b > point2d.b) {
                return 1;
            }
            return 0;
        }
    }
    
    private class Face
    {
        Halfedge a;
        int b;
    }
    
    private class DelaunaySegment
    {
        Halfedge a;
        Halfedge b;
        Point2d[] c;
        Face[] d;
        int e;
        int f;
        int g;
    }
    
    private class mat3
    {
        double[][] a;
        
        private mat3() {
            this.a = new double[3][3];
        }
    }
}
