package org.lasque.tusdk.core.http;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.lasque.tusdk.core.utils.TLog;

public class RequestParams
  implements Serializable
{
  public static final String APPLICATION_OCTET_STREAM = "application/octet-stream";
  public static final String APPLICATION_JSON = "application/json";
  protected final ConcurrentHashMap<String, String> mUrlParams = new ConcurrentHashMap();
  protected final ConcurrentHashMap<String, StreamWrapper> mStreamParams = new ConcurrentHashMap();
  protected final ConcurrentHashMap<String, FileWrapper> mFileParams = new ConcurrentHashMap();
  protected final ConcurrentHashMap<String, List<FileWrapper>> mFileArrayParams = new ConcurrentHashMap();
  protected final ConcurrentHashMap<String, Object> mUrlParamsWithObjects = new ConcurrentHashMap();
  protected boolean mIsRepeatable;
  protected boolean mForceMultipartEntity = false;
  protected boolean mUseJsonStreamer;
  protected String mElapsedFieldInJsonStreamer = "_elapsed";
  protected boolean mAutoCloseInputStreams;
  protected String mContentEncoding = "UTF-8";
  
  public RequestParams()
  {
    this((Map)null);
  }
  
  public RequestParams(Map<String, String> paramMap)
  {
    if (paramMap != null)
    {
      Iterator localIterator = paramMap.entrySet().iterator();
      while (localIterator.hasNext())
      {
        Map.Entry localEntry = (Map.Entry)localIterator.next();
        put((String)localEntry.getKey(), (String)localEntry.getValue());
      }
    }
  }
  
  public RequestParams(String paramString1, final String paramString2)
  {
    this(new HashMap() {});
  }
  
  public RequestParams(Object... paramVarArgs)
  {
    int i = paramVarArgs.length;
    if (i % 2 != 0) {
      throw new IllegalArgumentException("Supplied arguments must be even");
    }
    for (int j = 0; j < i; j += 2)
    {
      String str1 = String.valueOf(paramVarArgs[j]);
      String str2 = String.valueOf(paramVarArgs[(j + 1)]);
      put(str1, str2);
    }
  }
  
  public void setContentEncoding(String paramString)
  {
    if (paramString != null) {
      this.mContentEncoding = paramString;
    } else {
      TLog.d("setContentEncoding called with null attribute", new Object[0]);
    }
  }
  
  public void setForceMultipartEntityContentType(boolean paramBoolean)
  {
    this.mForceMultipartEntity = paramBoolean;
  }
  
  public void put(String paramString1, String paramString2)
  {
    if ((paramString1 != null) && (paramString2 != null)) {
      this.mUrlParams.put(paramString1, paramString2);
    }
  }
  
  public void put(String paramString, File[] paramArrayOfFile)
  {
    put(paramString, paramArrayOfFile, null, null);
  }
  
  public void put(String paramString1, File[] paramArrayOfFile, String paramString2, String paramString3)
  {
    if (paramString1 != null)
    {
      ArrayList localArrayList = new ArrayList();
      for (File localFile : paramArrayOfFile)
      {
        if ((localFile == null) || (!localFile.exists())) {
          throw new FileNotFoundException();
        }
        localArrayList.add(new FileWrapper(localFile, paramString2, paramString3));
      }
      this.mFileArrayParams.put(paramString1, localArrayList);
    }
  }
  
  public void put(String paramString, File paramFile)
  {
    put(paramString, paramFile, null, null);
  }
  
  public void put(String paramString1, String paramString2, File paramFile)
  {
    put(paramString1, paramFile, null, paramString2);
  }
  
  public void put(String paramString1, File paramFile, String paramString2)
  {
    put(paramString1, paramFile, paramString2, null);
  }
  
  public void put(String paramString1, File paramFile, String paramString2, String paramString3)
  {
    if ((paramFile == null) || (!paramFile.exists())) {
      throw new FileNotFoundException();
    }
    if (paramString1 != null) {
      this.mFileParams.put(paramString1, new FileWrapper(paramFile, paramString2, paramString3));
    }
  }
  
  public void put(String paramString, InputStream paramInputStream)
  {
    put(paramString, paramInputStream, null);
  }
  
  public void put(String paramString1, InputStream paramInputStream, String paramString2)
  {
    put(paramString1, paramInputStream, paramString2, null);
  }
  
  public void put(String paramString1, InputStream paramInputStream, String paramString2, String paramString3)
  {
    put(paramString1, paramInputStream, paramString2, paramString3, this.mAutoCloseInputStreams);
  }
  
  public void put(String paramString1, InputStream paramInputStream, String paramString2, String paramString3, boolean paramBoolean)
  {
    if ((paramString1 != null) && (paramInputStream != null)) {
      this.mStreamParams.put(paramString1, StreamWrapper.a(paramInputStream, paramString2, paramString3, paramBoolean));
    }
  }
  
  public void put(String paramString, Object paramObject)
  {
    if ((paramString != null) && (paramObject != null)) {
      this.mUrlParamsWithObjects.put(paramString, paramObject);
    }
  }
  
  public void put(String paramString, int paramInt)
  {
    if (paramString != null) {
      this.mUrlParams.put(paramString, String.valueOf(paramInt));
    }
  }
  
  public void put(String paramString, long paramLong)
  {
    if (paramString != null) {
      this.mUrlParams.put(paramString, String.valueOf(paramLong));
    }
  }
  
  public void add(String paramString1, String paramString2)
  {
    if ((paramString1 != null) && (paramString2 != null))
    {
      Object localObject = this.mUrlParamsWithObjects.get(paramString1);
      if (localObject == null)
      {
        localObject = new HashSet();
        put(paramString1, localObject);
      }
      if ((localObject instanceof List)) {
        ((List)localObject).add(paramString2);
      } else if ((localObject instanceof Set)) {
        ((Set)localObject).add(paramString2);
      }
    }
  }
  
  public void remove(String paramString)
  {
    this.mUrlParams.remove(paramString);
    this.mStreamParams.remove(paramString);
    this.mFileParams.remove(paramString);
    this.mUrlParamsWithObjects.remove(paramString);
    this.mFileArrayParams.remove(paramString);
  }
  
  public boolean has(String paramString)
  {
    return (this.mUrlParams.get(paramString) != null) || (this.mStreamParams.get(paramString) != null) || (this.mFileParams.get(paramString) != null) || (this.mUrlParamsWithObjects.get(paramString) != null) || (this.mFileArrayParams.get(paramString) != null);
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = new StringBuilder();
    Object localObject1 = this.mUrlParams.entrySet().iterator();
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = (Map.Entry)((Iterator)localObject1).next();
      if (localStringBuilder.length() > 0) {
        localStringBuilder.append("&");
      }
      localStringBuilder.append((String)((Map.Entry)localObject2).getKey());
      localStringBuilder.append("=");
      localStringBuilder.append((String)((Map.Entry)localObject2).getValue());
    }
    localObject1 = this.mStreamParams.entrySet().iterator();
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = (Map.Entry)((Iterator)localObject1).next();
      if (localStringBuilder.length() > 0) {
        localStringBuilder.append("&");
      }
      localStringBuilder.append((String)((Map.Entry)localObject2).getKey());
      localStringBuilder.append("=");
      localStringBuilder.append("STREAM");
    }
    localObject1 = this.mFileParams.entrySet().iterator();
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = (Map.Entry)((Iterator)localObject1).next();
      if (localStringBuilder.length() > 0) {
        localStringBuilder.append("&");
      }
      localStringBuilder.append((String)((Map.Entry)localObject2).getKey());
      localStringBuilder.append("=");
      localStringBuilder.append("FILE");
    }
    localObject1 = this.mFileArrayParams.entrySet().iterator();
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = (Map.Entry)((Iterator)localObject1).next();
      if (localStringBuilder.length() > 0) {
        localStringBuilder.append("&");
      }
      localStringBuilder.append((String)((Map.Entry)localObject2).getKey());
      localStringBuilder.append("=");
      localStringBuilder.append("FILES(SIZE=").append(((List)((Map.Entry)localObject2).getValue()).size()).append(")");
    }
    localObject1 = a(null, this.mUrlParamsWithObjects);
    Object localObject2 = ((List)localObject1).iterator();
    while (((Iterator)localObject2).hasNext())
    {
      URLEncodedUtils.BasicNameValuePair localBasicNameValuePair = (URLEncodedUtils.BasicNameValuePair)((Iterator)localObject2).next();
      if (localStringBuilder.length() > 0) {
        localStringBuilder.append("&");
      }
      localStringBuilder.append(localBasicNameValuePair.getName());
      localStringBuilder.append("=");
      localStringBuilder.append(localBasicNameValuePair.getValue());
    }
    return localStringBuilder.toString();
  }
  
  public String toPairString()
  {
    ConcurrentHashMap localConcurrentHashMap = new ConcurrentHashMap();
    Object localObject1 = this.mUrlParams.entrySet().iterator();
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = (Map.Entry)((Iterator)localObject1).next();
      localConcurrentHashMap.put(((Map.Entry)localObject2).getKey(), ((Map.Entry)localObject2).getValue());
    }
    localObject1 = a(null, this.mUrlParamsWithObjects);
    Object localObject2 = ((List)localObject1).iterator();
    while (((Iterator)localObject2).hasNext())
    {
      localObject3 = (URLEncodedUtils.BasicNameValuePair)((Iterator)localObject2).next();
      localConcurrentHashMap.put(((URLEncodedUtils.BasicNameValuePair)localObject3).getName(), ((URLEncodedUtils.BasicNameValuePair)localObject3).getValue());
    }
    localObject2 = new ArrayList(localConcurrentHashMap.entrySet());
    Collections.sort((List)localObject2, new Comparator()
    {
      public int compare(Map.Entry<String, Object> paramAnonymousEntry1, Map.Entry<String, Object> paramAnonymousEntry2)
      {
        return ((String)paramAnonymousEntry1.getKey()).toString().compareTo((String)paramAnonymousEntry2.getKey());
      }
    });
    Object localObject3 = new StringBuilder();
    for (int i = 0; i < ((List)localObject2).size(); i++)
    {
      Map.Entry localEntry = (Map.Entry)((List)localObject2).get(i);
      ((StringBuilder)localObject3).append((String)localEntry.getKey()).append(localEntry.getValue());
    }
    return ((StringBuilder)localObject3).toString();
  }
  
  public void setHttpEntityIsRepeatable(boolean paramBoolean)
  {
    this.mIsRepeatable = paramBoolean;
  }
  
  public void setUseJsonStreamer(boolean paramBoolean)
  {
    this.mUseJsonStreamer = paramBoolean;
  }
  
  public void setElapsedFieldInJsonStreamer(String paramString)
  {
    this.mElapsedFieldInJsonStreamer = paramString;
  }
  
  public void setAutoCloseInputStreams(boolean paramBoolean)
  {
    this.mAutoCloseInputStreams = paramBoolean;
  }
  
  public HttpEntity getEntity(ResponseHandlerInterface paramResponseHandlerInterface)
  {
    if (this.mUseJsonStreamer) {
      return a(paramResponseHandlerInterface);
    }
    if ((!this.mForceMultipartEntity) && (this.mStreamParams.isEmpty()) && (this.mFileParams.isEmpty()) && (this.mFileArrayParams.isEmpty())) {
      return a();
    }
    return b(paramResponseHandlerInterface);
  }
  
  private HttpEntity a(ResponseHandlerInterface paramResponseHandlerInterface)
  {
    JsonStreamerEntity localJsonStreamerEntity = new JsonStreamerEntity(paramResponseHandlerInterface, (!this.mFileParams.isEmpty()) || (!this.mStreamParams.isEmpty()), this.mElapsedFieldInJsonStreamer);
    Iterator localIterator = this.mUrlParams.entrySet().iterator();
    Map.Entry localEntry;
    while (localIterator.hasNext())
    {
      localEntry = (Map.Entry)localIterator.next();
      localJsonStreamerEntity.addPart((String)localEntry.getKey(), localEntry.getValue());
    }
    localIterator = this.mUrlParamsWithObjects.entrySet().iterator();
    while (localIterator.hasNext())
    {
      localEntry = (Map.Entry)localIterator.next();
      localJsonStreamerEntity.addPart((String)localEntry.getKey(), localEntry.getValue());
    }
    localIterator = this.mFileParams.entrySet().iterator();
    while (localIterator.hasNext())
    {
      localEntry = (Map.Entry)localIterator.next();
      localJsonStreamerEntity.addPart((String)localEntry.getKey(), localEntry.getValue());
    }
    localIterator = this.mStreamParams.entrySet().iterator();
    while (localIterator.hasNext())
    {
      localEntry = (Map.Entry)localIterator.next();
      StreamWrapper localStreamWrapper = (StreamWrapper)localEntry.getValue();
      if (localStreamWrapper.inputStream != null) {
        localJsonStreamerEntity.addPart((String)localEntry.getKey(), StreamWrapper.a(localStreamWrapper.inputStream, localStreamWrapper.name, localStreamWrapper.contentType, localStreamWrapper.autoClose));
      }
    }
    return localJsonStreamerEntity;
  }
  
  private HttpEntity a()
  {
    try
    {
      return new UrlEncodedFormEntity(getParamsList(), this.mContentEncoding);
    }
    catch (UnsupportedEncodingException localUnsupportedEncodingException)
    {
      TLog.e(localUnsupportedEncodingException, "createFormEntity failed", new Object[0]);
    }
    return null;
  }
  
  private HttpEntity b(ResponseHandlerInterface paramResponseHandlerInterface)
  {
    SimpleMultipartEntity localSimpleMultipartEntity = new SimpleMultipartEntity(paramResponseHandlerInterface);
    localSimpleMultipartEntity.setIsRepeatable(this.mIsRepeatable);
    Object localObject1 = this.mUrlParams.entrySet().iterator();
    while (((Iterator)localObject1).hasNext())
    {
      localObject2 = (Map.Entry)((Iterator)localObject1).next();
      localSimpleMultipartEntity.addPartWithCharset((String)((Map.Entry)localObject2).getKey(), (String)((Map.Entry)localObject2).getValue(), this.mContentEncoding);
    }
    localObject1 = a(null, this.mUrlParamsWithObjects);
    Object localObject2 = ((List)localObject1).iterator();
    Object localObject3;
    while (((Iterator)localObject2).hasNext())
    {
      localObject3 = (URLEncodedUtils.BasicNameValuePair)((Iterator)localObject2).next();
      localSimpleMultipartEntity.addPartWithCharset(((URLEncodedUtils.BasicNameValuePair)localObject3).getName(), ((URLEncodedUtils.BasicNameValuePair)localObject3).getValue(), this.mContentEncoding);
    }
    localObject2 = this.mStreamParams.entrySet().iterator();
    Object localObject4;
    while (((Iterator)localObject2).hasNext())
    {
      localObject3 = (Map.Entry)((Iterator)localObject2).next();
      localObject4 = (StreamWrapper)((Map.Entry)localObject3).getValue();
      if (((StreamWrapper)localObject4).inputStream != null) {
        localSimpleMultipartEntity.addPart((String)((Map.Entry)localObject3).getKey(), ((StreamWrapper)localObject4).name, ((StreamWrapper)localObject4).inputStream, ((StreamWrapper)localObject4).contentType);
      }
    }
    localObject2 = this.mFileParams.entrySet().iterator();
    while (((Iterator)localObject2).hasNext())
    {
      localObject3 = (Map.Entry)((Iterator)localObject2).next();
      localObject4 = (FileWrapper)((Map.Entry)localObject3).getValue();
      localSimpleMultipartEntity.addPart((String)((Map.Entry)localObject3).getKey(), ((FileWrapper)localObject4).file, ((FileWrapper)localObject4).contentType, ((FileWrapper)localObject4).customFileName);
    }
    localObject2 = this.mFileArrayParams.entrySet().iterator();
    while (((Iterator)localObject2).hasNext())
    {
      localObject3 = (Map.Entry)((Iterator)localObject2).next();
      localObject4 = (List)((Map.Entry)localObject3).getValue();
      Iterator localIterator = ((List)localObject4).iterator();
      while (localIterator.hasNext())
      {
        FileWrapper localFileWrapper = (FileWrapper)localIterator.next();
        localSimpleMultipartEntity.addPart((String)((Map.Entry)localObject3).getKey(), localFileWrapper.file, localFileWrapper.contentType, localFileWrapper.customFileName);
      }
    }
    return localSimpleMultipartEntity;
  }
  
  protected List<URLEncodedUtils.BasicNameValuePair> getParamsList()
  {
    LinkedList localLinkedList = new LinkedList();
    Iterator localIterator = this.mUrlParams.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      localLinkedList.add(new URLEncodedUtils.BasicNameValuePair((String)localEntry.getKey(), (String)localEntry.getValue()));
    }
    localLinkedList.addAll(a(null, this.mUrlParamsWithObjects));
    return localLinkedList;
  }
  
  private List<URLEncodedUtils.BasicNameValuePair> a(String paramString, Object paramObject)
  {
    LinkedList localLinkedList = new LinkedList();
    Object localObject1;
    if ((paramObject instanceof Map))
    {
      localObject1 = (Map)paramObject;
      ArrayList localArrayList = new ArrayList(((Map)localObject1).keySet());
      if ((localArrayList.size() > 0) && ((localArrayList.get(0) instanceof Comparable))) {
        Collections.sort(localArrayList);
      }
      Iterator localIterator2 = localArrayList.iterator();
      while (localIterator2.hasNext())
      {
        Object localObject3 = localIterator2.next();
        if ((localObject3 instanceof String))
        {
          Object localObject4 = ((Map)localObject1).get(localObject3);
          if (localObject4 != null) {
            localLinkedList.addAll(a(paramString == null ? (String)localObject3 : String.format(Locale.US, "%s[%s]", new Object[] { paramString, localObject3 }), localObject4));
          }
        }
      }
    }
    else
    {
      int i;
      int j;
      if ((paramObject instanceof List))
      {
        localObject1 = (List)paramObject;
        i = ((List)localObject1).size();
        for (j = 0; j < i; j++) {
          localLinkedList.addAll(a(String.format(Locale.US, "%s[%d]", new Object[] { paramString, Integer.valueOf(j) }), ((List)localObject1).get(j)));
        }
      }
      else if ((paramObject instanceof Object[]))
      {
        localObject1 = (Object[])paramObject;
        i = localObject1.length;
        for (j = 0; j < i; j++) {
          localLinkedList.addAll(a(String.format(Locale.US, "%s[%d]", new Object[] { paramString, Integer.valueOf(j) }), localObject1[j]));
        }
      }
      else if ((paramObject instanceof Set))
      {
        localObject1 = (Set)paramObject;
        Iterator localIterator1 = ((Set)localObject1).iterator();
        while (localIterator1.hasNext())
        {
          Object localObject2 = localIterator1.next();
          localLinkedList.addAll(a(paramString, localObject2));
        }
      }
      else
      {
        localLinkedList.add(new URLEncodedUtils.BasicNameValuePair(paramString, paramObject.toString()));
      }
    }
    return localLinkedList;
  }
  
  protected String getParamString()
  {
    return URLEncodedUtils.format(getParamsList(), this.mContentEncoding);
  }
  
  public static class StreamWrapper
  {
    public final InputStream inputStream;
    public final String name;
    public final String contentType;
    public final boolean autoClose;
    
    public StreamWrapper(InputStream paramInputStream, String paramString1, String paramString2, boolean paramBoolean)
    {
      this.inputStream = paramInputStream;
      this.name = paramString1;
      this.contentType = paramString2;
      this.autoClose = paramBoolean;
    }
    
    static StreamWrapper a(InputStream paramInputStream, String paramString1, String paramString2, boolean paramBoolean)
    {
      return new StreamWrapper(paramInputStream, paramString1, paramString2 == null ? "application/octet-stream" : paramString2, paramBoolean);
    }
  }
  
  public static class FileWrapper
    implements Serializable
  {
    public final File file;
    public final String contentType;
    public final String customFileName;
    
    public FileWrapper(File paramFile, String paramString1, String paramString2)
    {
      this.file = paramFile;
      this.contentType = paramString1;
      this.customFileName = paramString2;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\http\RequestParams.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */