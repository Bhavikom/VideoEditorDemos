package org.lasque.tusdk.core.utils;

public class Complex
{
  private double a;
  private double b;
  
  public Complex(double paramDouble1, double paramDouble2)
  {
    this.a = paramDouble1;
    this.b = paramDouble2;
  }
  
  public String toString()
  {
    if (this.b == 0.0D) {
      return this.a + "";
    }
    if (this.a == 0.0D) {
      return this.b + "i";
    }
    if (this.b < 0.0D) {
      return this.a + " - " + -this.b + "i";
    }
    return this.a + " + " + this.b + "i";
  }
  
  public double abs()
  {
    return Math.hypot(this.a, this.b);
  }
  
  public double phase()
  {
    return Math.atan2(this.b, this.a);
  }
  
  public Complex plus(Complex paramComplex)
  {
    Complex localComplex = this;
    double d1 = localComplex.a + paramComplex.a;
    double d2 = localComplex.b + paramComplex.b;
    return new Complex(d1, d2);
  }
  
  public Complex minus(Complex paramComplex)
  {
    Complex localComplex = this;
    double d1 = localComplex.a - paramComplex.a;
    double d2 = localComplex.b - paramComplex.b;
    return new Complex(d1, d2);
  }
  
  public Complex times(Complex paramComplex)
  {
    Complex localComplex = this;
    double d1 = localComplex.a * paramComplex.a - localComplex.b * paramComplex.b;
    double d2 = localComplex.a * paramComplex.b + localComplex.b * paramComplex.a;
    return new Complex(d1, d2);
  }
  
  public Complex scale(double paramDouble)
  {
    return new Complex(paramDouble * this.a, paramDouble * this.b);
  }
  
  public Complex conjugate()
  {
    return new Complex(this.a, -this.b);
  }
  
  public Complex conjugateScale(double paramDouble)
  {
    return new Complex(paramDouble * this.a, paramDouble * -this.b);
  }
  
  public Complex reciprocal()
  {
    double d = this.a * this.a + this.b * this.b;
    return new Complex(this.a / d, -this.b / d);
  }
  
  public double mod()
  {
    double d = this.a * this.a + this.b * this.b;
    return Math.sqrt(d);
  }
  
  public double re()
  {
    return this.a;
  }
  
  public double im()
  {
    return this.b;
  }
  
  public double safeRe()
  {
    return Math.min(Math.max(this.a, -1.0D), 1.0D);
  }
  
  public void setImZero()
  {
    this.b = 0.0D;
  }
  
  public Complex divides(Complex paramComplex)
  {
    Complex localComplex = this;
    return localComplex.times(paramComplex.reciprocal());
  }
  
  public Complex exp()
  {
    return new Complex(Math.exp(this.a) * Math.cos(this.b), Math.exp(this.a) * Math.sin(this.b));
  }
  
  public Complex sin()
  {
    return new Complex(Math.sin(this.a) * Math.cosh(this.b), Math.cos(this.a) * Math.sinh(this.b));
  }
  
  public Complex cos()
  {
    return new Complex(Math.cos(this.a) * Math.cosh(this.b), -Math.sin(this.a) * Math.sinh(this.b));
  }
  
  public Complex tan()
  {
    return sin().divides(cos());
  }
  
  public static Complex plus(Complex paramComplex1, Complex paramComplex2)
  {
    double d1 = paramComplex1.a + paramComplex2.a;
    double d2 = paramComplex1.b + paramComplex2.b;
    Complex localComplex = new Complex(d1, d2);
    return localComplex;
  }
  
  public boolean equals(Object paramObject)
  {
    if (paramObject == null) {
      return false;
    }
    if (getClass() != paramObject.getClass()) {
      return false;
    }
    Complex localComplex = (Complex)paramObject;
    return (this.a == localComplex.a) && (this.b == localComplex.b);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\Complex.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */