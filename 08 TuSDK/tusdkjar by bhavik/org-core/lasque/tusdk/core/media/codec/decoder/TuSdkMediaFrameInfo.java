package org.lasque.tusdk.core.media.codec.decoder;

public class TuSdkMediaFrameInfo
{
  public long startTimeUs;
  public long endTimeUs;
  public long intervalUs;
  public long keyFrameIntervalUs = -1L;
  public int keyFrameRate = -1;
  public long skipMinUs = -1L;
  public long skipPreviousMinUs = -1L;
  
  public boolean supportAllKeys()
  {
    return this.keyFrameRate == 0;
  }
  
  public String toString()
  {
    StringBuffer localStringBuffer = new StringBuffer("TuSdkVideoFileFrame").append("{ \n");
    localStringBuffer.append("startTimeUs: ").append(this.startTimeUs).append(", \n");
    localStringBuffer.append("endTimeUs: ").append(this.endTimeUs).append(", \n");
    localStringBuffer.append("intervalUs: ").append(this.intervalUs).append(", \n");
    localStringBuffer.append("keyFrameIntervalUs: ").append(this.keyFrameIntervalUs).append(", \n");
    localStringBuffer.append("keyFrameRate: ").append(this.keyFrameRate).append(", \n");
    localStringBuffer.append("skipMinUs: ").append(this.skipMinUs).append(", \n");
    localStringBuffer.append("skipPreviousMinUs: ").append(this.skipPreviousMinUs).append(", \n");
    localStringBuffer.append("}");
    return localStringBuffer.toString();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\decoder\TuSdkMediaFrameInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */