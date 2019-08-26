// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils;

public class Complex
{
    private double a;
    private double b;
    
    public Complex(final double a, final double b) {
        this.a = a;
        this.b = b;
    }
    
    @Override
    public String toString() {
        if (this.b == 0.0) {
            return this.a + "";
        }
        if (this.a == 0.0) {
            return this.b + "i";
        }
        if (this.b < 0.0) {
            return this.a + " - " + -this.b + "i";
        }
        return this.a + " + " + this.b + "i";
    }
    
    public double abs() {
        return Math.hypot(this.a, this.b);
    }
    
    public double phase() {
        return Math.atan2(this.b, this.a);
    }
    
    public Complex plus(final Complex complex) {
        return new Complex(this.a + complex.a, this.b + complex.b);
    }
    
    public Complex minus(final Complex complex) {
        return new Complex(this.a - complex.a, this.b - complex.b);
    }
    
    public Complex times(final Complex complex) {
        return new Complex(this.a * complex.a - this.b * complex.b, this.a * complex.b + this.b * complex.a);
    }
    
    public Complex scale(final double n) {
        return new Complex(n * this.a, n * this.b);
    }
    
    public Complex conjugate() {
        return new Complex(this.a, -this.b);
    }
    
    public Complex conjugateScale(final double n) {
        return new Complex(n * this.a, n * -this.b);
    }
    
    public Complex reciprocal() {
        final double n = this.a * this.a + this.b * this.b;
        return new Complex(this.a / n, -this.b / n);
    }
    
    public double mod() {
        return Math.sqrt(this.a * this.a + this.b * this.b);
    }
    
    public double re() {
        return this.a;
    }
    
    public double im() {
        return this.b;
    }
    
    public double safeRe() {
        return Math.min(Math.max(this.a, -1.0), 1.0);
    }
    
    public void setImZero() {
        this.b = 0.0;
    }
    
    public Complex divides(final Complex complex) {
        return this.times(complex.reciprocal());
    }
    
    public Complex exp() {
        return new Complex(Math.exp(this.a) * Math.cos(this.b), Math.exp(this.a) * Math.sin(this.b));
    }
    
    public Complex sin() {
        return new Complex(Math.sin(this.a) * Math.cosh(this.b), Math.cos(this.a) * Math.sinh(this.b));
    }
    
    public Complex cos() {
        return new Complex(Math.cos(this.a) * Math.cosh(this.b), -Math.sin(this.a) * Math.sinh(this.b));
    }
    
    public Complex tan() {
        return this.sin().divides(this.cos());
    }
    
    public static Complex plus(final Complex complex, final Complex complex2) {
        return new Complex(complex.a + complex2.a, complex.b + complex2.b);
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (this.getClass() != o.getClass()) {
            return false;
        }
        final Complex complex = (Complex)o;
        return this.a == complex.a && this.b == complex.b;
    }
}
