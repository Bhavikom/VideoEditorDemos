package org.lasque.tusdk.core.http;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import org.lasque.tusdk.core.utils.ReflectUtils;

public class StringEntity
  extends AbstractHttpEntity
  implements Cloneable
{
  public static final String TEXT_PLAIN = "text/plain";
  protected final byte[] content;
  
  public StringEntity(String paramString1, String paramString2, String paramString3)
  {
    ReflectUtils.notNull(paramString1, "Source string");
    String str1 = paramString2 != null ? paramString2 : "text/plain";
    String str2 = paramString3 != null ? paramString3 : "UTF-8";
    this.content = paramString1.getBytes(str2);
    setContentType(str1 + "; " + str2);
  }
  
  public StringEntity(String paramString1, String paramString2)
  {
    this(paramString1, null, paramString2);
  }
  
  public StringEntity(String paramString)
  {
    this(paramString, null);
  }
  
  public boolean isRepeatable()
  {
    return true;
  }
  
  public long getContentLength()
  {
    return this.content.length;
  }
  
  public InputStream getContent()
  {
    return new ByteArrayInputStream(this.content);
  }
  
  public void writeTo(OutputStream paramOutputStream)
  {
    ReflectUtils.notNull(paramOutputStream, "Output stream");
    paramOutputStream.write(this.content);
    paramOutputStream.flush();
  }
  
  public boolean isStreaming()
  {
    return false;
  }
  
  public Object clone()
  {
    return super.clone();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\http\StringEntity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */