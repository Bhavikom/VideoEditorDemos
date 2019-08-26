package org.lasque.tusdk.core.http;

import android.text.TextUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPOutputStream;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lasque.tusdk.core.utils.FileHelper;
import org.lasque.tusdk.core.utils.TLog;

public class JsonStreamerEntity
  implements HttpEntity
{
  private static final UnsupportedOperationException a = new UnsupportedOperationException("Unsupported operation in this implementation.");
  private static final byte[] b = "true".getBytes();
  private static final byte[] c = "false".getBytes();
  private static final byte[] d = "null".getBytes();
  private static final byte[] e = a("name");
  private static final byte[] f = a("type");
  private static final byte[] g = a("contents");
  private static final HttpHeader h = new HttpHeader("Content-Type", "application/json");
  private static final HttpHeader i = new HttpHeader("Content-Encoding", "gzip");
  private final byte[] j = new byte['က'];
  private final Map<String, Object> k = new HashMap();
  private final HttpHeader l;
  private final byte[] m;
  private final ResponseHandlerInterface n;
  
  public JsonStreamerEntity(ResponseHandlerInterface paramResponseHandlerInterface, boolean paramBoolean, String paramString)
  {
    this.n = paramResponseHandlerInterface;
    this.l = (paramBoolean ? i : null);
    this.m = (TextUtils.isEmpty(paramString) ? null : a(paramString));
  }
  
  static byte[] a(String paramString)
  {
    if (paramString == null) {
      return d;
    }
    StringBuilder localStringBuilder = new StringBuilder(128);
    localStringBuilder.append('"');
    int i1 = paramString.length();
    int i2 = -1;
    for (;;)
    {
      i2++;
      if (i2 >= i1) {
        break;
      }
      char c1 = paramString.charAt(i2);
      switch (c1)
      {
      case '"': 
        localStringBuilder.append("\\\"");
        break;
      case '\\': 
        localStringBuilder.append("\\\\");
        break;
      case '\b': 
        localStringBuilder.append("\\b");
        break;
      case '\f': 
        localStringBuilder.append("\\f");
        break;
      case '\n': 
        localStringBuilder.append("\\n");
        break;
      case '\r': 
        localStringBuilder.append("\\r");
        break;
      case '\t': 
        localStringBuilder.append("\\t");
        break;
      default: 
        if ((c1 <= '\037') || ((c1 >= '') && (c1 <= '')) || ((c1 >= ' ') && (c1 <= '⃿')))
        {
          String str = Integer.toHexString(c1);
          localStringBuilder.append("\\u");
          int i3 = 4 - str.length();
          for (int i4 = 0; i4 < i3; i4++) {
            localStringBuilder.append('0');
          }
          localStringBuilder.append(str.toUpperCase(Locale.US));
        }
        else
        {
          localStringBuilder.append(c1);
        }
        break;
      }
    }
    localStringBuilder.append('"');
    return localStringBuilder.toString().getBytes();
  }
  
  public void addPart(String paramString, Object paramObject)
  {
    this.k.put(paramString, paramObject);
  }
  
  public boolean isRepeatable()
  {
    return false;
  }
  
  public boolean isChunked()
  {
    return false;
  }
  
  public boolean isStreaming()
  {
    return false;
  }
  
  public long getContentLength()
  {
    return -1L;
  }
  
  public HttpHeader getContentEncoding()
  {
    return this.l;
  }
  
  public HttpHeader getContentType()
  {
    return h;
  }
  
  public void consumeContent() {}
  
  public InputStream getContent()
  {
    throw a;
  }
  
  public void writeTo(OutputStream paramOutputStream)
  {
    if (paramOutputStream == null) {
      throw new IllegalStateException("Output stream cannot be null.");
    }
    long l1 = System.currentTimeMillis();
    OutputStream localOutputStream = this.l != null ? new GZIPOutputStream(paramOutputStream, 4096) : paramOutputStream;
    localOutputStream.write(123);
    Set localSet = this.k.keySet();
    int i1 = localSet.size();
    if (0 < i1)
    {
      int i2 = 0;
      Iterator localIterator = localSet.iterator();
      while (localIterator.hasNext())
      {
        String str = (String)localIterator.next();
        i2++;
        try
        {
          Object localObject1 = this.k.get(str);
          localOutputStream.write(a(str));
          localOutputStream.write(58);
          if (localObject1 == null)
          {
            localOutputStream.write(d);
          }
          else
          {
            boolean bool = localObject1 instanceof RequestParams.FileWrapper;
            if ((bool) || ((localObject1 instanceof RequestParams.StreamWrapper)))
            {
              localOutputStream.write(123);
              if (bool) {
                a(localOutputStream, (RequestParams.FileWrapper)localObject1);
              } else {
                a(localOutputStream, (RequestParams.StreamWrapper)localObject1);
              }
              localOutputStream.write(125);
            }
            else if ((localObject1 instanceof JsonValueInterface))
            {
              localOutputStream.write(((JsonValueInterface)localObject1).getEscapedJsonValue());
            }
            else if ((localObject1 instanceof JSONObject))
            {
              localOutputStream.write(localObject1.toString().getBytes());
            }
            else if ((localObject1 instanceof JSONArray))
            {
              localOutputStream.write(localObject1.toString().getBytes());
            }
            else if ((localObject1 instanceof Boolean))
            {
              localOutputStream.write(((Boolean)localObject1).booleanValue() ? b : c);
            }
            else if ((localObject1 instanceof Long))
            {
              localOutputStream.write((((Number)localObject1).longValue() + "").getBytes());
            }
            else if ((localObject1 instanceof Double))
            {
              localOutputStream.write((((Number)localObject1).doubleValue() + "").getBytes());
            }
            else if ((localObject1 instanceof Float))
            {
              localOutputStream.write((((Number)localObject1).floatValue() + "").getBytes());
            }
            else if ((localObject1 instanceof Integer))
            {
              localOutputStream.write((((Number)localObject1).intValue() + "").getBytes());
            }
            else
            {
              localOutputStream.write(a(localObject1.toString()));
            }
          }
        }
        finally
        {
          if ((this.m != null) || (i2 < i1)) {
            localOutputStream.write(44);
          }
        }
      }
      long l2 = System.currentTimeMillis() - l1;
      if (this.m != null)
      {
        localOutputStream.write(this.m);
        localOutputStream.write(58);
        localOutputStream.write((l2 + "").getBytes());
      }
      TLog.i("Uploaded JSON in %s seconds", new Object[] { Double.valueOf(Math.floor(l2 / 1000L)) });
    }
    localOutputStream.write(125);
    localOutputStream.flush();
    FileHelper.safeClose(localOutputStream);
  }
  
  private void a(OutputStream paramOutputStream, RequestParams.StreamWrapper paramStreamWrapper)
  {
    a(paramOutputStream, paramStreamWrapper.name, paramStreamWrapper.contentType);
    Base64OutputStream localBase64OutputStream = new Base64OutputStream(paramOutputStream, 18);
    int i1;
    while ((i1 = paramStreamWrapper.inputStream.read(this.j)) != -1) {
      localBase64OutputStream.write(this.j, 0, i1);
    }
    FileHelper.safeClose(localBase64OutputStream);
    a(paramOutputStream);
    if (paramStreamWrapper.autoClose) {
      FileHelper.safeClose(paramStreamWrapper.inputStream);
    }
  }
  
  private void a(OutputStream paramOutputStream, RequestParams.FileWrapper paramFileWrapper)
  {
    a(paramOutputStream, paramFileWrapper.file.getName(), paramFileWrapper.contentType);
    long l1 = 0L;
    long l2 = paramFileWrapper.file.length();
    FileInputStream localFileInputStream = new FileInputStream(paramFileWrapper.file);
    Base64OutputStream localBase64OutputStream = new Base64OutputStream(paramOutputStream, 18);
    int i1;
    while ((i1 = localFileInputStream.read(this.j)) != -1)
    {
      localBase64OutputStream.write(this.j, 0, i1);
      l1 += i1;
      this.n.sendProgressMessage(l1, l2);
    }
    FileHelper.safeClose(localBase64OutputStream);
    a(paramOutputStream);
    FileHelper.safeClose(localFileInputStream);
  }
  
  private void a(OutputStream paramOutputStream, String paramString1, String paramString2)
  {
    paramOutputStream.write(e);
    paramOutputStream.write(58);
    paramOutputStream.write(a(paramString1));
    paramOutputStream.write(44);
    paramOutputStream.write(f);
    paramOutputStream.write(58);
    paramOutputStream.write(a(paramString2));
    paramOutputStream.write(44);
    paramOutputStream.write(g);
    paramOutputStream.write(58);
    paramOutputStream.write(34);
  }
  
  private void a(OutputStream paramOutputStream)
  {
    paramOutputStream.write(34);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\http\JsonStreamerEntity.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */