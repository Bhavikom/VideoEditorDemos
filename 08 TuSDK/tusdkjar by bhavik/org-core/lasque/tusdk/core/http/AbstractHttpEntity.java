package org.lasque.tusdk.core.http;

public abstract class AbstractHttpEntity
  implements HttpEntity
{
  protected static final int OUTPUT_BUFFER_SIZE = 4096;
  protected HttpHeader contentType;
  protected HttpHeader contentEncoding;
  protected boolean chunked;
  
  public HttpHeader getContentType()
  {
    return this.contentType;
  }
  
  public HttpHeader getContentEncoding()
  {
    return this.contentEncoding;
  }
  
  public boolean isChunked()
  {
    return this.chunked;
  }
  
  public void setContentType(HttpHeader paramHttpHeader)
  {
    this.contentType = paramHttpHeader;
  }
  
  public void setContentType(String paramString)
  {
    HttpHeader localHttpHeader = null;
    if (paramString != null) {
      localHttpHeader = new HttpHeader("Content-Type", paramString);
    }
    setContentType(localHttpHeader);
  }
  
  public void setContentEncoding(HttpHeader paramHttpHeader)
  {
    this.contentEncoding = paramHttpHeader;
  }
  
  public void setContentEncoding(String paramString)
  {
    HttpHeader localHttpHeader = null;
    if (paramString != null) {
      localHttpHeader = new HttpHeader("Content-Encoding", paramString);
    }
    setContentEncoding(localHttpHeader);
  }
  
  public void setChunked(boolean paramBoolean)
  {
    this.chunked = paramBoolean;
  }
  
  public void consumeContent() {}
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append('[');
    if (this.contentType != null)
    {
      localStringBuilder.append("Content-Type: ");
      localStringBuilder.append(this.contentType.getValue());
      localStringBuilder.append(',');
    }
    if (this.contentEncoding != null)
    {
      localStringBuilder.append("Content-Encoding: ");
      localStringBuilder.append(this.contentEncoding.getValue());
      localStringBuilder.append(',');
    }
    long l = getContentLength();
    if (l >= 0L)
    {
      localStringBuilder.append("Content-Length: ");
      localStringBuilder.append(l);
      localStringBuilder.append(',');
    }
    localStringBuilder.append("Chunked: ");
    localStringBuilder.append(this.chunked);
    localStringBuilder.append(']');
    return localStringBuilder.toString();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\http\AbstractHttpEntity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */