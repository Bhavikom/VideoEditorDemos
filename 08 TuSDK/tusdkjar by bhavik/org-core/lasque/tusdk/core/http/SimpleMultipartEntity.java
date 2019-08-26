package org.lasque.tusdk.core.http;

import android.text.TextUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.lasque.tusdk.core.utils.FileHelper;
import org.lasque.tusdk.core.utils.TLog;

class SimpleMultipartEntity
  implements HttpEntity
{
  private static final byte[] a = "\r\n".getBytes();
  private static final byte[] b = "Content-Transfer-Encoding: binary\r\n".getBytes();
  private static final char[] c = "-_1234567890abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
  private final String d;
  private final byte[] e;
  private final byte[] f;
  private final List<FilePart> g = new ArrayList();
  private final ByteArrayOutputStream h = new ByteArrayOutputStream();
  private final ResponseHandlerInterface i;
  private boolean j;
  private long k;
  private long l;
  
  public SimpleMultipartEntity(ResponseHandlerInterface paramResponseHandlerInterface)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    Random localRandom = new Random();
    for (int m = 0; m < 30; m++) {
      localStringBuilder.append(c[localRandom.nextInt(c.length)]);
    }
    this.d = localStringBuilder.toString();
    this.e = ("--" + this.d + "\r\n").getBytes();
    this.f = ("--" + this.d + "--" + "\r\n").getBytes();
    this.i = paramResponseHandlerInterface;
  }
  
  public void addPart(String paramString1, String paramString2, String paramString3)
  {
    try
    {
      this.h.write(this.e);
      this.h.write(c(paramString1));
      this.h.write(b(paramString3));
      this.h.write(a);
      this.h.write(paramString2.getBytes());
      this.h.write(a);
    }
    catch (IOException localIOException)
    {
      TLog.e(localIOException, "addPart ByteArrayOutputStream exception", new Object[0]);
    }
  }
  
  public void addPartWithCharset(String paramString1, String paramString2, String paramString3)
  {
    if (paramString3 == null) {
      paramString3 = "UTF-8";
    }
    addPart(paramString1, paramString2, "text/plain; charset=" + paramString3);
  }
  
  public void addPart(String paramString1, String paramString2)
  {
    addPartWithCharset(paramString1, paramString2, null);
  }
  
  public void addPart(String paramString, File paramFile)
  {
    addPart(paramString, paramFile, null);
  }
  
  public void addPart(String paramString1, File paramFile, String paramString2)
  {
    this.g.add(new FilePart(paramString1, paramFile, a(paramString2)));
  }
  
  public void addPart(String paramString1, File paramFile, String paramString2, String paramString3)
  {
    this.g.add(new FilePart(paramString1, paramFile, a(paramString2), paramString3));
  }
  
  public void addPart(String paramString1, String paramString2, InputStream paramInputStream, String paramString3)
  {
    this.h.write(this.e);
    this.h.write(a(paramString1, paramString2));
    this.h.write(b(paramString3));
    this.h.write(b);
    this.h.write(a);
    byte[] arrayOfByte = new byte['က'];
    int m;
    while ((m = paramInputStream.read(arrayOfByte)) != -1) {
      this.h.write(arrayOfByte, 0, m);
    }
    this.h.write(a);
    this.h.flush();
  }
  
  private String a(String paramString)
  {
    return paramString == null ? "application/octet-stream" : paramString;
  }
  
  private byte[] b(String paramString)
  {
    String str = "Content-Type: " + a(paramString) + "\r\n";
    return str.getBytes();
  }
  
  private byte[] c(String paramString)
  {
    return ("Content-Disposition: form-data; name=\"" + paramString + "\"" + "\r\n").getBytes();
  }
  
  private byte[] a(String paramString1, String paramString2)
  {
    return ("Content-Disposition: form-data; name=\"" + paramString1 + "\"; filename=\"" + paramString2 + "\"" + "\r\n").getBytes();
  }
  
  private void a(long paramLong)
  {
    this.k += paramLong;
    this.i.sendProgressMessage(this.k, this.l);
  }
  
  public long getContentLength()
  {
    long l1 = this.h.size();
    Iterator localIterator = this.g.iterator();
    while (localIterator.hasNext())
    {
      FilePart localFilePart = (FilePart)localIterator.next();
      long l2 = localFilePart.getTotalLength();
      if (l2 < 0L) {
        return -1L;
      }
      l1 += l2;
    }
    l1 += this.f.length;
    return l1;
  }
  
  public HttpHeader getContentType()
  {
    return new HttpHeader("Content-Type", "multipart/form-data; boundary=" + this.d);
  }
  
  public boolean isChunked()
  {
    return false;
  }
  
  public void setIsRepeatable(boolean paramBoolean)
  {
    this.j = paramBoolean;
  }
  
  public boolean isRepeatable()
  {
    return this.j;
  }
  
  public boolean isStreaming()
  {
    return false;
  }
  
  public void writeTo(OutputStream paramOutputStream)
  {
    this.k = 0L;
    this.l = ((int)getContentLength());
    this.h.writeTo(paramOutputStream);
    a(this.h.size());
    Iterator localIterator = this.g.iterator();
    while (localIterator.hasNext())
    {
      FilePart localFilePart = (FilePart)localIterator.next();
      localFilePart.writeTo(paramOutputStream);
    }
    paramOutputStream.write(this.f);
    a(this.f.length);
  }
  
  public HttpHeader getContentEncoding()
  {
    return null;
  }
  
  public void consumeContent()
  {
    if (isStreaming()) {
      throw new UnsupportedOperationException("Streaming entity does not implement #consumeContent()");
    }
  }
  
  public InputStream getContent()
  {
    throw new UnsupportedOperationException("getContent() is not supported. Use writeTo() instead.");
  }
  
  private class FilePart
  {
    public final File file;
    public final byte[] header;
    
    public FilePart(String paramString1, File paramFile, String paramString2, String paramString3)
    {
      this.header = a(paramString1, TextUtils.isEmpty(paramString3) ? paramFile.getName() : paramString3, paramString2);
      this.file = paramFile;
    }
    
    public FilePart(String paramString1, File paramFile, String paramString2)
    {
      this.header = a(paramString1, paramFile.getName(), paramString2);
      this.file = paramFile;
    }
    
    private byte[] a(String paramString1, String paramString2, String paramString3)
    {
      ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
      try
      {
        localByteArrayOutputStream.write(SimpleMultipartEntity.a(SimpleMultipartEntity.this));
        localByteArrayOutputStream.write(SimpleMultipartEntity.a(SimpleMultipartEntity.this, paramString1, paramString2));
        localByteArrayOutputStream.write(SimpleMultipartEntity.a(SimpleMultipartEntity.this, paramString3));
        localByteArrayOutputStream.write(SimpleMultipartEntity.a());
        localByteArrayOutputStream.write(SimpleMultipartEntity.b());
      }
      catch (IOException localIOException)
      {
        TLog.e(localIOException, "createHeader ByteArrayOutputStream exception", new Object[0]);
      }
      return localByteArrayOutputStream.toByteArray();
    }
    
    public long getTotalLength()
    {
      long l = this.file.length() + SimpleMultipartEntity.b().length;
      return this.header.length + l;
    }
    
    public void writeTo(OutputStream paramOutputStream)
    {
      paramOutputStream.write(this.header);
      SimpleMultipartEntity.a(SimpleMultipartEntity.this, this.header.length);
      FileInputStream localFileInputStream = new FileInputStream(this.file);
      byte[] arrayOfByte = new byte['က'];
      int i;
      while ((i = localFileInputStream.read(arrayOfByte)) != -1)
      {
        paramOutputStream.write(arrayOfByte, 0, i);
        SimpleMultipartEntity.a(SimpleMultipartEntity.this, i);
      }
      paramOutputStream.write(SimpleMultipartEntity.b());
      SimpleMultipartEntity.a(SimpleMultipartEntity.this, SimpleMultipartEntity.b().length);
      paramOutputStream.flush();
      FileHelper.safeClose(localFileInputStream);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\http\SimpleMultipartEntity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */