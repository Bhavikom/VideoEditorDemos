package org.lasque.tusdk.core.utils.hardware;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.util.Iterator;
import java.util.List;
import org.lasque.tusdk.core.utils.ReflectUtils;

public class TuSdkSharedPreferences
{
  private Context a;
  private String b;
  
  public TuSdkSharedPreferences(Context paramContext, String paramString)
  {
    this.a = paramContext;
    this.b = paramString;
  }
  
  public SharedPreferences getSharedPreferences()
  {
    SharedPreferences localSharedPreferences = this.a.getSharedPreferences(this.b, 0);
    return localSharedPreferences;
  }
  
  public SharedPreferences.Editor getSharedEditor()
  {
    return getSharedPreferences().edit();
  }
  
  public String loadSharedCache(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    SharedPreferences localSharedPreferences = getSharedPreferences();
    String str = localSharedPreferences.getString(paramString, null);
    return str;
  }
  
  public <T> T loadSharedCacheObject(String paramString)
  {
    String str = loadSharedCache(paramString);
    if (str == null) {
      return null;
    }
    Object localObject = ReflectUtils.deserialize(str);
    return (T)localObject;
  }
  
  public void saveSharedCache(String paramString1, String paramString2)
  {
    if (paramString1 == null) {
      return;
    }
    SharedPreferences.Editor localEditor = getSharedEditor();
    if (paramString2 == null) {
      localEditor.remove(paramString1);
    } else {
      localEditor.putString(paramString1, paramString2);
    }
    localEditor.commit();
  }
  
  public void removeSharedCache(List<String> paramList)
  {
    if ((paramList == null) || (paramList.isEmpty())) {
      return;
    }
    SharedPreferences.Editor localEditor = getSharedEditor();
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      localEditor.remove(str);
    }
    localEditor.commit();
  }
  
  public void saveSharedCacheObject(String paramString, Object paramObject)
  {
    if (paramString == null) {
      return;
    }
    SharedPreferences.Editor localEditor = getSharedEditor();
    if (paramObject == null)
    {
      localEditor.remove(paramString);
    }
    else
    {
      String str = ReflectUtils.serialize(paramObject);
      localEditor.putString(paramString, str);
    }
    localEditor.commit();
  }
  
  public boolean loadSharedCacheBool(String paramString)
  {
    if (paramString == null) {
      return false;
    }
    SharedPreferences localSharedPreferences = getSharedPreferences();
    boolean bool = localSharedPreferences.getBoolean(paramString, false);
    return bool;
  }
  
  public void saveSharedCache(String paramString, boolean paramBoolean)
  {
    if (paramString == null) {
      return;
    }
    SharedPreferences.Editor localEditor = getSharedEditor();
    localEditor.putBoolean(paramString, paramBoolean);
    localEditor.commit();
  }
  
  public long loadSharedCacheLong(String paramString)
  {
    if (paramString == null) {
      return 0L;
    }
    SharedPreferences localSharedPreferences = getSharedPreferences();
    long l = localSharedPreferences.getLong(paramString, 0L);
    return l;
  }
  
  public void saveSharedCache(String paramString, long paramLong)
  {
    if (paramString == null) {
      return;
    }
    SharedPreferences.Editor localEditor = getSharedEditor();
    localEditor.putLong(paramString, paramLong);
    localEditor.commit();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\hardware\TuSdkSharedPreferences.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */