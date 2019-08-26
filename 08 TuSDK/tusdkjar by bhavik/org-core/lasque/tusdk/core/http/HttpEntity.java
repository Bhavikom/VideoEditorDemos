package org.lasque.tusdk.core.http;

import java.io.InputStream;
import java.io.OutputStream;

public abstract interface HttpEntity
{
  public abstract boolean isRepeatable();
  
  public abstract boolean isChunked();
  
  public abstract long getContentLength();
  
  public abstract HttpHeader getContentType();
  
  public abstract HttpHeader getContentEncoding();
  
  public abstract InputStream getContent();
  
  public abstract void writeTo(OutputStream paramOutputStream);
  
  public abstract boolean isStreaming();
  
  public abstract void consumeContent();
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\http\HttpEntity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */