package org.lasque.tusdk.core.exif;

public class Rational
{
  private final long a;
  private final long b;
  
  public Rational(long paramLong1, long paramLong2)
  {
    this.a = paramLong1;
    this.b = paramLong2;
  }
  
  public Rational(Rational paramRational)
  {
    this.a = paramRational.a;
    this.b = paramRational.b;
  }
  
  public long getNumerator()
  {
    return this.a;
  }
  
  public long getDenominator()
  {
    return this.b;
  }
  
  public double toDouble()
  {
    return this.a / this.b;
  }
  
  public boolean equals(Object paramObject)
  {
    if (paramObject == null) {
      return false;
    }
    if (this == paramObject) {
      return true;
    }
    if ((paramObject instanceof Rational))
    {
      Rational localRational = (Rational)paramObject;
      return (this.a == localRational.a) && (this.b == localRational.b);
    }
    return false;
  }
  
  public String toString()
  {
    return this.a + "/" + this.b;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\exif\Rational.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */