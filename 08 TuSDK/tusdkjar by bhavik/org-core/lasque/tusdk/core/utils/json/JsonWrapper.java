package org.lasque.tusdk.core.utils.json;

import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lasque.tusdk.core.utils.ReflectUtils;

public class JsonWrapper
{
  protected JSONObject mJson;
  
  public JsonWrapper(String paramString)
  {
    this(JsonHelper.json(paramString));
  }
  
  public JsonWrapper(JSONObject paramJSONObject)
  {
    this.mJson = paramJSONObject;
  }
  
  public JSONObject getJson()
  {
    return this.mJson;
  }
  
  public JSONObject getJson(String paramString)
  {
    return JsonHelper.getJSONObject(this.mJson, paramString);
  }
  
  public JSONArray getJsonArray(String paramString)
  {
    return JsonHelper.getJSONArray(this.mJson, paramString);
  }
  
  public <T extends JsonBaseBean> T getJsonWithType(String paramString, Class<T> paramClass)
  {
    return getJsonWithType(getJson(paramString), paramClass);
  }
  
  public <T extends JsonBaseBean> T getJsonWithType(Class<T> paramClass)
  {
    return getJsonWithType(getJson(), paramClass);
  }
  
  public <T extends JsonBaseBean> T getJsonWithType(JSONObject paramJSONObject, Class<T> paramClass)
  {
    if ((paramClass == null) || (paramJSONObject == null)) {
      return null;
    }
    JsonBaseBean localJsonBaseBean = (JsonBaseBean)ReflectUtils.classInstance(paramClass);
    if (localJsonBaseBean == null) {
      return null;
    }
    localJsonBaseBean.setJson(paramJSONObject);
    return localJsonBaseBean;
  }
  
  public <T extends JsonBaseBean> T getJsonSubWithType(String paramString, Class<T> paramClass)
  {
    if (paramClass == null) {
      return null;
    }
    JSONObject localJSONObject = a(this.mJson, paramString);
    if (localJSONObject == null) {
      return null;
    }
    JsonBaseBean localJsonBaseBean = (JsonBaseBean)ReflectUtils.classInstance(paramClass);
    if (localJsonBaseBean == null) {
      return null;
    }
    localJsonBaseBean.setJson(localJSONObject);
    return localJsonBaseBean;
  }
  
  private JSONObject a(JSONObject paramJSONObject, String paramString)
  {
    if (paramJSONObject == null) {
      return null;
    }
    String[] arrayOfString = paramString.split("\\.", 2);
    if (arrayOfString.length == 2) {
      return a(JsonHelper.getJSONObject(paramJSONObject, arrayOfString[0]), arrayOfString[1]);
    }
    return JsonHelper.getJSONObject(paramJSONObject, paramString);
  }
  
  public <T extends JsonBaseBean> ArrayList<T> getJsonArrayWithType(String paramString, Class<T> paramClass)
  {
    if (paramClass == null) {
      return null;
    }
    JSONArray localJSONArray = getJsonArray(paramString);
    if (localJSONArray == null) {
      return null;
    }
    ArrayList localArrayList = new ArrayList(localJSONArray.length());
    int i = 0;
    int j = localJSONArray.length();
    while (i < j)
    {
      JSONObject localJSONObject = JsonHelper.getJSONObject(localJSONArray, i);
      if (localJSONObject != null)
      {
        JsonBaseBean localJsonBaseBean = (JsonBaseBean)ReflectUtils.classInstance(paramClass);
        if (localJsonBaseBean != null)
        {
          localJsonBaseBean.setJson(localJSONObject);
          localArrayList.add(localJsonBaseBean);
        }
      }
      i++;
    }
    return localArrayList;
  }
  
  public static <T extends JsonBaseBean> T deserialize(String paramString, Class<T> paramClass)
  {
    JsonWrapper localJsonWrapper = new JsonWrapper(paramString);
    return localJsonWrapper.getJsonWithType(paramClass);
  }
  
  public static <T extends JsonBaseBean> T deserialize(JSONObject paramJSONObject, Class<T> paramClass)
  {
    JsonWrapper localJsonWrapper = new JsonWrapper(paramJSONObject);
    return localJsonWrapper.getJsonWithType(paramClass);
  }
  
  public static <T extends JsonBaseBean> T deserialize(JSONObject paramJSONObject, Class<T> paramClass, String paramString)
  {
    JsonWrapper localJsonWrapper = new JsonWrapper(paramJSONObject);
    return localJsonWrapper.getJsonSubWithType(paramString, paramClass);
  }
  
  public static <T extends JsonBaseBean> ArrayList<T> deserializeArray(String paramString1, String paramString2, Class<T> paramClass)
  {
    JsonWrapper localJsonWrapper = new JsonWrapper(paramString2);
    return localJsonWrapper.getJsonArrayWithType(paramString1, paramClass);
  }
  
  public static <T extends JsonBaseBean> ArrayList<T> deserializeArray(String paramString, JSONObject paramJSONObject, Class<T> paramClass)
  {
    JsonWrapper localJsonWrapper = new JsonWrapper(paramJSONObject);
    return localJsonWrapper.getJsonArrayWithType(paramString, paramClass);
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\json\JsonWrapper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */