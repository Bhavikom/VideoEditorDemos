package org.lasque.tusdk.core.type;

public enum ResourceType
{
  private String a;
  
  private ResourceType(String paramString)
  {
    this.a = paramString;
  }
  
  public String getKey()
  {
    return this.a;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\type\ResourceType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */