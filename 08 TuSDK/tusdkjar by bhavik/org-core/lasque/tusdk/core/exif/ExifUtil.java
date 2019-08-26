package org.lasque.tusdk.core.exif;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class ExifUtil
{
  static final NumberFormat a = ;
  
  public static String processLensSpecifications(Rational[] paramArrayOfRational)
  {
    Rational localRational1 = paramArrayOfRational[0];
    Rational localRational2 = paramArrayOfRational[1];
    Rational localRational3 = paramArrayOfRational[2];
    Rational localRational4 = paramArrayOfRational[3];
    a.setMaximumFractionDigits(1);
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(a.format(localRational1.toDouble()));
    localStringBuilder.append("-");
    localStringBuilder.append(a.format(localRational2.toDouble()));
    localStringBuilder.append("mm f/");
    localStringBuilder.append(a.format(localRational3.toDouble()));
    localStringBuilder.append("-");
    localStringBuilder.append(a.format(localRational4.toDouble()));
    return localStringBuilder.toString();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\exif\ExifUtil.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */