package org.lasque.tusdk.core.media.codec.video;

import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.hardware.TuSdkGPU;
import org.lasque.tusdk.core.utils.hardware.TuSdkGPU.GpuType;

public enum TuSdkVideoQuality
{
  private int a;
  private int b;
  private int c;
  
  private TuSdkVideoQuality(int paramInt1, int paramInt2, int paramInt3)
  {
    this.a = paramInt1;
    this.b = paramInt2;
    this.c = Math.max(paramInt3, 1);
  }
  
  public int getFrameRates()
  {
    return this.a;
  }
  
  public int getBitrate()
  {
    return this.b;
  }
  
  public int getRefer()
  {
    return this.c;
  }
  
  public int dynamicBitrate(TuSdkSize paramTuSdkSize)
  {
    if (paramTuSdkSize == null) {
      return this.b;
    }
    return dynamicBitrate(paramTuSdkSize.width, paramTuSdkSize.height);
  }
  
  public int dynamicBitrate(int paramInt1, int paramInt2)
  {
    return dynamicBitrate(paramInt1, paramInt2, this.c);
  }
  
  public TuSdkVideoQuality upgrade()
  {
    TuSdkVideoQuality[] arrayOfTuSdkVideoQuality = values();
    int i = ordinal();
    if (i + 1 > LIVE_HIGH3.ordinal()) {
      return LIVE_HIGH3;
    }
    if (i + 1 > arrayOfTuSdkVideoQuality.length - 1) {
      return arrayOfTuSdkVideoQuality[(arrayOfTuSdkVideoQuality.length - 1)];
    }
    return arrayOfTuSdkVideoQuality[(i + 1)];
  }
  
  public TuSdkVideoQuality degrade()
  {
    TuSdkVideoQuality[] arrayOfTuSdkVideoQuality = values();
    int i = ordinal();
    if (i - 1 < 0) {
      return arrayOfTuSdkVideoQuality[0];
    }
    return arrayOfTuSdkVideoQuality[(i - 1)];
  }
  
  public static TuSdkVideoQuality safeQuality()
  {
    int i = TuSdkGPU.getGpuType().getPerformance();
    if (i < 3) {
      return RECORD_LOW2;
    }
    if (i == 3) {
      return RECORD_MEDIUM1;
    }
    if (i == 4) {
      return RECORD_MEDIUM3;
    }
    return RECORD_HIGH1;
  }
  
  public static int dynamicBitrate(int paramInt1, int paramInt2, int paramInt3)
  {
    if ((paramInt1 < 2) || (paramInt2 < 2) || (paramInt3 < 1)) {
      return 0;
    }
    int i = paramInt1 * paramInt2 * 3 * 4 / paramInt3;
    return i;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\video\TuSdkVideoQuality.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */