package org.lasque.tusdk.core.http;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.lasque.tusdk.core.utils.FileHelper;

public class HttpResponse
{
  private final HttpURLConnection a;
  private OutputStream b;
  private InputStream c;
  private HttpEntity d;
  private List<HttpHeader> e = new ArrayList();
  private int f = -1;
  private String g;
  private long h = -1L;
  private String i;
  private String j;
  
  public HttpResponse(HttpURLConnection paramHttpURLConnection)
  {
    this.a = paramHttpURLConnection;
  }
  
  public int getResponseCode()
  {
    return this.f;
  }
  
  public String getResponseMessage()
  {
    return this.g;
  }
  
  public long getContentLength()
  {
    return this.h;
  }
  
  public String getContentType()
  {
    return this.i;
  }
  
  public String getContentEncoding()
  {
    return this.j;
  }
  
  public List<HttpHeader> getAllHeaders()
  {
    return this.e;
  }
  
  public List<HttpHeader> getHeaders(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    List localList = getAllHeaders();
    if (localList == null) {
      return null;
    }
    ArrayList localArrayList = new ArrayList();
    Iterator localIterator = localList.iterator();
    while (localIterator.hasNext())
    {
      HttpHeader localHttpHeader = (HttpHeader)localIterator.next();
      if (localHttpHeader.equalsName(paramString)) {
        localArrayList.add(localHttpHeader);
      }
    }
    return localArrayList;
  }
  
  public HttpHeader getFirstHeader(String paramString)
  {
    List localList = getHeaders(paramString);
    if ((localList == null) || (localList.isEmpty())) {
      return null;
    }
    return (HttpHeader)localList.get(0);
  }
  
  public HttpEntity getEntity()
  {
    return this.d;
  }
  
  public void setEntity(HttpEntity paramHttpEntity)
  {
    if (paramHttpEntity == null) {
      return;
    }
    this.d = paramHttpEntity;
  }
  
  public OutputStream getOutputStream()
  {
    return this.b;
  }
  
  public void openOutputStream()
  {
    if (this.b != null) {
      return;
    }
    this.b = this.a.getOutputStream();
  }
  
  public InputStream getInputStream()
  {
    return this.c;
  }
  
  public void openInputStream()
  {
    if (this.b != null)
    {
      this.b.flush();
      FileHelper.safeClose(this.b);
      this.b = null;
    }
    a();
    if (this.c != null) {
      return;
    }
    this.c = this.a.getInputStream();
  }
  
  private void a()
  {
    this.f = this.a.getResponseCode();
    this.g = this.a.getResponseMessage();
    this.h = this.a.getContentLength();
    this.i = this.a.getContentType();
    this.j = this.a.getContentEncoding();
    this.d = new HttpResponseEntity(null);
    Map localMap = this.a.getHeaderFields();
    if (localMap == null) {
      return;
    }
    Iterator localIterator1 = localMap.entrySet().iterator();
    while (localIterator1.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator1.next();
      if ((localEntry.getValue() != null) && (!((List)localEntry.getValue()).isEmpty()))
      {
        Iterator localIterator2 = ((List)localEntry.getValue()).iterator();
        while (localIterator2.hasNext())
        {
          String str = (String)localIterator2.next();
          this.e.add(new HttpHeader((String)localEntry.getKey(), str));
        }
      }
    }
  }
  
  public void disconnect()
  {
    if (this.c != null)
    {
      FileHelper.safeClose(this.c);
      this.c = null;
    }
    if (getEntity() != null) {
      getEntity().consumeContent();
    }
    this.a.disconnect();
  }
  
  private class HttpResponseEntity
    implements HttpEntity
  {
    private HttpResponseEntity() {}
    
    public boolean isRepeatable()
    {
      return false;
    }
    
    public boolean isChunked()
    {
      return false;
    }
    
    public long getContentLength()
    {
      return HttpResponse.a(HttpResponse.this);
    }
    
    public HttpHeader getContentType()
    {
      if (HttpResponse.b(HttpResponse.this) == null) {
        return null;
      }
      return new HttpHeader("Content-Type", HttpResponse.b(HttpResponse.this));
    }
    
    public HttpHeader getContentEncoding()
    {
      if (HttpResponse.c(HttpResponse.this) == null) {
        return null;
      }
      return new HttpHeader("Content-Encoding", HttpResponse.c(HttpResponse.this));
    }
    
    public InputStream getContent()
    {
      return HttpResponse.this.getInputStream();
    }
    
    public void writeTo(OutputStream paramOutputStream) {}
    
    public boolean isStreaming()
    {
      return true;
    }
    
    public void consumeContent() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\http\HttpResponse.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */