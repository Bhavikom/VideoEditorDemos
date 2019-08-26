package org.lasque.tusdk.core.http;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;

public abstract class HttpUriRequest
{
  private HttpEntity a;
  private final List<HttpHeader> b = new ArrayList();
  private URL c;
  private HttpURLConnection d;
  
  public abstract String getMethod();
  
  public abstract boolean canOutput();
  
  public abstract boolean canUseCache();
  
  public HttpUriRequest() {}
  
  public HttpUriRequest(URL paramURL)
  {
    setURL(paramURL);
  }
  
  public HttpUriRequest(String paramString)
  {
    setURL(URLEncodedUtils.getURL(paramString));
  }
  
  public List<HttpHeader> getAllHeaders()
  {
    return this.b;
  }
  
  public boolean containsHeader(String paramString)
  {
    if (paramString == null) {
      return false;
    }
    return getFirstHeader(paramString) != null;
  }
  
  public HttpHeader getFirstHeader(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    Iterator localIterator = this.b.iterator();
    while (localIterator.hasNext())
    {
      HttpHeader localHttpHeader = (HttpHeader)localIterator.next();
      if (localHttpHeader.equalsName(paramString)) {
        return localHttpHeader;
      }
    }
    return null;
  }
  
  public void setHeader(String paramString1, String paramString2)
  {
    if ((paramString1 == null) || (paramString2 == null)) {
      return;
    }
    HttpHeader localHttpHeader = getFirstHeader(paramString1);
    if (localHttpHeader != null) {
      localHttpHeader.setValue(paramString2);
    } else {
      this.b.add(new HttpHeader(paramString1, paramString2));
    }
  }
  
  public void addHeader(String paramString1, String paramString2)
  {
    if ((paramString1 == null) || (paramString2 == null)) {
      return;
    }
    this.b.add(new HttpHeader(paramString1, paramString2));
  }
  
  public void setHeaders(List<HttpHeader> paramList)
  {
    if (paramList == null) {
      return;
    }
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      HttpHeader localHttpHeader = (HttpHeader)localIterator.next();
      setHeader(localHttpHeader.getName(), localHttpHeader.getValue());
    }
  }
  
  public void addHeaders(List<HttpHeader> paramList)
  {
    if (paramList == null) {
      return;
    }
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      HttpHeader localHttpHeader = (HttpHeader)localIterator.next();
      addHeader(localHttpHeader.getName(), localHttpHeader.getValue());
    }
  }
  
  public void removeHeader(String paramString)
  {
    ArrayList localArrayList = new ArrayList(this.b);
    Iterator localIterator = localArrayList.iterator();
    while (localIterator.hasNext())
    {
      HttpHeader localHttpHeader = (HttpHeader)localIterator.next();
      if (localHttpHeader.equalsName(paramString)) {
        this.b.remove(localHttpHeader);
      }
    }
  }
  
  public void removeHeader(HttpHeader paramHttpHeader)
  {
    if (paramHttpHeader == null) {
      return;
    }
    this.b.remove(paramHttpHeader);
  }
  
  public HttpEntity getEntity()
  {
    return this.a;
  }
  
  public void setEntity(HttpEntity paramHttpEntity)
  {
    this.a = paramHttpEntity;
  }
  
  public URL getURL()
  {
    return this.c;
  }
  
  public void setURL(URL paramURL)
  {
    this.c = paramURL;
  }
  
  public void abort()
  {
    if (this.d == null) {
      return;
    }
    this.d.disconnect();
    this.d = null;
  }
  
  public HttpURLConnection openConnection()
  {
    abort();
    if (this.c.getProtocol().toLowerCase().equals("https")) {
      this.d = ((HttpsURLConnection)this.c.openConnection());
    } else {
      this.d = ((HttpURLConnection)this.c.openConnection());
    }
    this.d.setDoOutput(canOutput());
    this.d.setDoInput(true);
    this.d.setRequestMethod(getMethod());
    this.d.setUseCaches(canUseCache());
    if ((!canUseCache()) && (canOutput()) && (getEntity() != null)) {
      this.d.setFixedLengthStreamingMode((int)getEntity().getContentLength());
    }
    a(this.d);
    return this.d;
  }
  
  private void a(HttpURLConnection paramHttpURLConnection)
  {
    if (paramHttpURLConnection == null) {
      return;
    }
    Iterator localIterator = this.b.iterator();
    while (localIterator.hasNext())
    {
      HttpHeader localHttpHeader = (HttpHeader)localIterator.next();
      paramHttpURLConnection.setRequestProperty(localHttpHeader.getName(), localHttpHeader.getValue());
    }
    if (getEntity() == null) {
      return;
    }
    if (getEntity().getContentEncoding() != null) {
      paramHttpURLConnection.setRequestProperty(getEntity().getContentEncoding().getName(), getEntity().getContentEncoding().getValue());
    }
    if (getEntity().getContentType() != null) {
      paramHttpURLConnection.setRequestProperty(getEntity().getContentType().getName(), getEntity().getContentType().getValue());
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\http\HttpUriRequest.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */