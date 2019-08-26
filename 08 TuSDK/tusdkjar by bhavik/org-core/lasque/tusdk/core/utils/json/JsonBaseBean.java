package org.lasque.tusdk.core.utils.json;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import org.lasque.tusdk.core.utils.ReflectUtils;
import org.lasque.tusdk.core.utils.StringHelper;

public abstract class JsonBaseBean
{
  private static final Map<String, DataBaseNexus> a = new HashMap();
  
  private static DataBaseNexus a(Class<?> paramClass)
  {
    DataBaseNexus localDataBaseNexus = (DataBaseNexus)a.get(paramClass.getName());
    if (localDataBaseNexus == null)
    {
      localDataBaseNexus = new DataBaseNexus(paramClass);
      a.put(paramClass.getName(), localDataBaseNexus);
    }
    return localDataBaseNexus;
  }
  
  public void setJson(JSONObject paramJSONObject)
  {
    if (paramJSONObject == null) {
      return;
    }
    DataBaseNexus localDataBaseNexus = a(getClass());
    localDataBaseNexus.bindJson(this, paramJSONObject);
  }
  
  public JSONObject buildJson()
  {
    DataBaseNexus localDataBaseNexus = a(getClass());
    return localDataBaseNexus.buildJson(this);
  }
  
  public JSONObject buildJson(String paramString)
  {
    JSONObject localJSONObject = buildJson();
    if (StringHelper.isNotEmpty(paramString)) {
      localJSONObject = JsonHelper.putObject(new JSONObject(), paramString, localJSONObject);
    }
    return localJSONObject;
  }
  
  public String toString()
  {
    StringBuilder localStringBuilder = ReflectUtils.trace(this);
    if (localStringBuilder != null) {
      return localStringBuilder.toString();
    }
    return super.toString();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\json\JsonBaseBean.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */