package org.lasque.tusdk.core.http;

import java.io.InputStream;
import java.io.OutputStream;

public class HttpEntityWrapper
  implements HttpEntity
{
  protected HttpEntity mWrappedEntity;
  
  public HttpEntityWrapper(HttpEntity paramHttpEntity)
  {
    if (paramHttpEntity == null) {
      throw new IllegalArgumentException("wrapped entity must not be null");
    }
    this.mWrappedEntity = paramHttpEntity;
  }
  
  public boolean isRepeatable()
  {
    return this.mWrappedEntity.isRepeatable();
  }
  
  public boolean isChunked()
  {
    return this.mWrappedEntity.isChunked();
  }
  
  public long getContentLength()
  {
    return this.mWrappedEntity.getContentLength();
  }
  
  public HttpHeader getContentType()
  {
    return this.mWrappedEntity.getContentType();
  }
  
  public HttpHeader getContentEncoding()
  {
    return this.mWrappedEntity.getContentEncoding();
  }
  
  public InputStream getContent()
  {
    return this.mWrappedEntity.getContent();
  }
  
  public void writeTo(OutputStream paramOutputStream)
  {
    this.mWrappedEntity.writeTo(paramOutputStream);
  }
  
  public boolean isStreaming()
  {
    return this.mWrappedEntity.isStreaming();
  }
  
  public void consumeContent()
  {
    consumeWrappedEntity();
  }
  
  public void consumeWrappedEntity()
  {
    this.mWrappedEntity.consumeContent();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\http\HttpEntityWrapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */