package org.lasque.tusdk.core.type;

public enum PermissionType
{
  private String a;
  private int b;
  
  private PermissionType(String paramString, int paramInt)
  {
    this.a = paramString;
    this.b = paramInt;
  }
  
  public String getKey()
  {
    return this.a;
  }
  
  public int getLevel()
  {
    return this.b;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\type\PermissionType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */