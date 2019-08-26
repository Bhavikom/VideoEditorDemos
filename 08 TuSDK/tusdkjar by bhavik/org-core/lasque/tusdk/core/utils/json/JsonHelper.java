package org.lasque.tusdk.core.utils.json;

import java.util.ArrayList;
import java.util.HashMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.TLog;

public class JsonHelper
{
  public static JSONObject json(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    JSONObject localJSONObject = null;
    try
    {
      localJSONObject = new JSONObject(paramString);
    }
    catch (JSONException localJSONException)
    {
      TLog.e(localJSONException, "json decode error: %s", new Object[] { paramString });
    }
    return localJSONObject;
  }
  
  public static JSONArray getJSONArray(JSONObject paramJSONObject, String paramString)
  {
    if ((paramJSONObject == null) || (paramString == null)) {
      return null;
    }
    return paramJSONObject.optJSONArray(paramString);
  }
  
  public static JSONObject getJSONObject(JSONObject paramJSONObject, String paramString)
  {
    if ((paramJSONObject == null) || (paramString == null)) {
      return null;
    }
    return paramJSONObject.optJSONObject(paramString);
  }
  
  public static JSONObject getJSONObject(JSONArray paramJSONArray, int paramInt)
  {
    if ((paramJSONArray == null) || (paramInt >= paramJSONArray.length())) {
      return null;
    }
    return paramJSONArray.optJSONObject(paramInt);
  }
  
  public static JSONArray getJsonArrayForDB(DataBase paramDataBase, JSONObject paramJSONObject)
  {
    if ((paramDataBase == null) || (paramJSONObject == null)) {
      return null;
    }
    JSONArray localJSONArray = null;
    if (paramDataBase.needSub())
    {
      JSONObject localJSONObject = getJSONObject(paramJSONObject, paramDataBase.value());
      if ((localJSONObject == null) || (StringHelper.isEmpty(paramDataBase.sub()))) {
        return null;
      }
      localJSONArray = getJSONArray(localJSONObject, paramDataBase.sub());
    }
    else
    {
      localJSONArray = getJSONArray(paramJSONObject, paramDataBase.value());
    }
    return localJSONArray;
  }
  
  public static JSONObject getJsonObjectForDB(DataBase paramDataBase, JSONObject paramJSONObject)
  {
    if ((paramDataBase == null) || (paramJSONObject == null)) {
      return null;
    }
    JSONObject localJSONObject = getJSONObject(paramJSONObject, paramDataBase.value());
    if ((paramDataBase.needSub()) && (localJSONObject != null) && (StringHelper.isNotEmpty(paramDataBase.sub()))) {
      localJSONObject = getJSONObject(localJSONObject, paramDataBase.sub());
    }
    return localJSONObject;
  }
  
  public static void putLong(JSONObject paramJSONObject, String paramString, long paramLong)
  {
    if ((paramJSONObject == null) || (paramString == null)) {
      return;
    }
    try
    {
      paramJSONObject.put(paramString, paramLong);
    }
    catch (JSONException localJSONException)
    {
      TLog.e(localJSONException, "putLong: %s | %s | %s", new Object[] { paramJSONObject, paramString, Long.valueOf(paramLong) });
    }
  }
  
  public static JSONObject putObject(JSONObject paramJSONObject, String paramString, Object paramObject)
  {
    if ((paramJSONObject == null) || (paramString == null)) {
      return paramJSONObject;
    }
    try
    {
      paramJSONObject.put(paramString, paramObject);
    }
    catch (JSONException localJSONException)
    {
      TLog.e(localJSONException, "putObject: %s | %s | %s", new Object[] { paramJSONObject, paramString, paramObject });
    }
    return paramJSONObject;
  }
  
  public static ArrayList<String> toStringList(JSONArray paramJSONArray)
  {
    if (paramJSONArray == null) {
      return null;
    }
    ArrayList localArrayList = new ArrayList(paramJSONArray.length());
    int i = 0;
    int j = paramJSONArray.length();
    while (i < j)
    {
      localArrayList.add(paramJSONArray.optString(i));
      i++;
    }
    return localArrayList;
  }
  
  public static HashMap<String, String> toHashMap(JSONObject paramJSONObject)
  {
    if (paramJSONObject == null) {
      return null;
    }
    JSONArray localJSONArray = paramJSONObject.names();
    if ((localJSONArray == null) || (localJSONArray.length() < 1)) {
      return null;
    }
    HashMap localHashMap = new HashMap(localJSONArray.length());
    int i = 0;
    int j = localJSONArray.length();
    while (i < j)
    {
      localHashMap.put(localJSONArray.optString(i), paramJSONObject.optString(localJSONArray.optString(i)));
      i++;
    }
    return localHashMap;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\json\JsonHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */