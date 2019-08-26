package org.lasque.tusdk.core.network;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.lasque.tusdk.core.http.RequestParams;

public class TuSdkHttpParams
  extends RequestParams
{
  public TuSdkHttpParams() {}
  
  public TuSdkHttpParams(Map<String, String> paramMap)
  {
    super(paramMap);
  }
  
  public TuSdkHttpParams(Object... paramVarArgs)
  {
    super(paramVarArgs);
  }
  
  public TuSdkHttpParams(String paramString1, String paramString2)
  {
    super(paramString1, paramString2);
  }
  
  public TuSdkHttpParams append(String paramString, Object paramObject)
  {
    if ((paramString == null) || (paramObject == null)) {
      return this;
    }
    String str = String.valueOf(paramObject);
    put(paramString, str);
    return this;
  }
  
  public TuSdkHttpParams append(Object... paramVarArgs)
  {
    int i = paramVarArgs.length;
    if (i % 2 != 0) {
      throw new IllegalArgumentException("Supplied arguments must be even");
    }
    for (int j = 0; j < i; j += 2) {
      if ((paramVarArgs[j] != null) && (paramVarArgs[(j + 1)] != null))
      {
        String str1 = String.valueOf(paramVarArgs[j]);
        String str2 = String.valueOf(paramVarArgs[(j + 1)]);
        put(str1, str2);
      }
    }
    return this;
  }
  
  public String getUrlParam(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    return (String)this.mUrlParams.get(paramString);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\network\TuSdkHttpParams.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */