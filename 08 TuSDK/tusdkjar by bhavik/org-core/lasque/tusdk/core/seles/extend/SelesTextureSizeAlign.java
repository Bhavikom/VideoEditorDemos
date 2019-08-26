package org.lasque.tusdk.core.seles.extend;

import org.lasque.tusdk.core.struct.TuSdkSize;

public enum SelesTextureSizeAlign
{
  private int a;
  private boolean b;
  private boolean c;
  
  private SelesTextureSizeAlign(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    this.a = paramInt;
    this.b = paramBoolean1;
    this.c = paramBoolean2;
  }
  
  public int getMultiple()
  {
    return this.a;
  }
  
  public TuSdkSize align(TuSdkSize paramTuSdkSize)
  {
    if ((paramTuSdkSize == null) || (paramTuSdkSize.minSide() < getMultiple())) {
      return paramTuSdkSize;
    }
    TuSdkSize localTuSdkSize = TuSdkSize.create(align(paramTuSdkSize.width), align(paramTuSdkSize.height));
    return localTuSdkSize;
  }
  
  public int align(int paramInt)
  {
    if (paramInt < getMultiple()) {
      return paramInt;
    }
    int i = paramInt - paramInt % getMultiple();
    if (paramInt == i) {
      return paramInt;
    }
    int j = i + getMultiple();
    int k = this.b ? j : i;
    if (!this.c) {
      return k;
    }
    int m = Math.abs(paramInt - i);
    int n = Math.abs(paramInt - j);
    if (m == n) {
      return k;
    }
    if (m > n) {
      return j;
    }
    return i;
  }
  
  public static SelesTextureSizeAlign getValue(int paramInt, boolean paramBoolean1, boolean paramBoolean2)
  {
    for (SelesTextureSizeAlign localSelesTextureSizeAlign : ) {
      if ((paramInt == localSelesTextureSizeAlign.a) && (paramBoolean1 == localSelesTextureSizeAlign.b) && (paramBoolean2 == localSelesTextureSizeAlign.c)) {
        return localSelesTextureSizeAlign;
      }
    }
    return Align2MultipleMin;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\extend\SelesTextureSizeAlign.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */