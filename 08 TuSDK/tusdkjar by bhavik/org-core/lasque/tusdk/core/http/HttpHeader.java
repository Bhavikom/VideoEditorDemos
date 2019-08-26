package org.lasque.tusdk.core.http;

public class HttpHeader
{
  private String a;
  private String b;
  
  public String getName()
  {
    return this.a;
  }
  
  public void setName(String paramString)
  {
    this.a = paramString;
  }
  
  public String getValue()
  {
    return this.b;
  }
  
  public void setValue(String paramString)
  {
    this.b = paramString;
  }
  
  public HttpHeader() {}
  
  public HttpHeader(String paramString1, String paramString2)
  {
    this.a = paramString1;
    this.b = paramString2;
  }
  
  public boolean equalsName(String paramString)
  {
    if ((this.a == null) || (paramString == null)) {
      return false;
    }
    return this.a.equalsIgnoreCase(paramString);
  }
  
  public boolean equalsValue(String paramString)
  {
    if ((this.b == null) || (paramString == null)) {
      return false;
    }
    return this.b.equalsIgnoreCase(paramString);
  }
  
  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof HttpHeader))) {
      return false;
    }
    HttpHeader localHttpHeader = (HttpHeader)paramObject;
    return (equalsName(localHttpHeader.getName())) && (equalsValue(localHttpHeader.getValue()));
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\http\HttpHeader.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */