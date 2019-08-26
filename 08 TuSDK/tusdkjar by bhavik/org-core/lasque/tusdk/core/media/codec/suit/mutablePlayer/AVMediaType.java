package org.lasque.tusdk.core.media.codec.suit.mutablePlayer;

public enum AVMediaType
{
  private String a;
  
  private AVMediaType(String paramString)
  {
    this.a = paramString;
  }
  
  public String getMime()
  {
    return this.a;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\media\codec\suit\mutablePlayer\AVMediaType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */