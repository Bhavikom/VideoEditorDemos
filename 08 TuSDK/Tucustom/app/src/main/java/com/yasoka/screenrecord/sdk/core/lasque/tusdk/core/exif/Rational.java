// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.exif;

public class Rational
{
    private final long a;
    private final long b;
    
    public Rational(final long a, final long b) {
        this.a = a;
        this.b = b;
    }
    
    public Rational(final Rational rational) {
        this.a = rational.a;
        this.b = rational.b;
    }
    
    public long getNumerator() {
        return this.a;
    }
    
    public long getDenominator() {
        return this.b;
    }
    
    public double toDouble() {
        return this.a / (double)this.b;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == null) {
            return false;
        }
        if (this == o) {
            return true;
        }
        if (o instanceof Rational) {
            final Rational rational = (Rational)o;
            return this.a == rational.a && this.b == rational.b;
        }
        return false;
    }
    
    @Override
    public String toString() {
        return this.a + "/" + this.b;
    }
}
