package org.lasque.tusdk.core.utils.json;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lasque.tusdk.core.type.ClazzType;
import org.lasque.tusdk.core.utils.DateHelper;
import org.lasque.tusdk.core.utils.ReflectUtils;
import org.lasque.tusdk.core.utils.TLog;

public class DataBaseNexus
{
  private Map<String, DataBase> a = new HashMap();
  
  public DataBaseNexus(Class<?> paramClass)
  {
    a(paramClass);
  }
  
  private void a(Class<?> paramClass)
  {
    if (paramClass == null) {
      return;
    }
    Field[] arrayOfField1 = paramClass.getFields();
    for (Field localField : arrayOfField1) {
      if (!Modifier.isStatic(localField.getModifiers())) {
        a(localField);
      }
    }
  }
  
  private void a(Field paramField)
  {
    DataBase localDataBase = (DataBase)paramField.getAnnotation(DataBase.class);
    if (localDataBase == null) {
      return;
    }
    this.a.put(paramField.getName(), localDataBase);
  }
  
  public void bindJson(JsonBaseBean paramJsonBaseBean, JSONObject paramJSONObject)
  {
    if ((paramJsonBaseBean == null) || (paramJSONObject == null)) {
      return;
    }
    Iterator localIterator = this.a.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      Field localField = ReflectUtils.reflectField(paramJsonBaseBean.getClass(), (String)localEntry.getKey());
      a(localField, (DataBase)localEntry.getValue(), paramJsonBaseBean, paramJSONObject);
    }
  }
  
  private void a(Field paramField, DataBase paramDataBase, JsonBaseBean paramJsonBaseBean, JSONObject paramJSONObject)
  {
    if ((paramField == null) || (!paramJSONObject.has(paramDataBase.value()))) {
      return;
    }
    ClazzType localClazzType = ClazzType.getType(paramField.getType().hashCode());
    if (localClazzType == null)
    {
      b(paramField, paramDataBase, paramJsonBaseBean, paramJSONObject);
      return;
    }
    try
    {
      a(localClazzType, paramField, paramDataBase, paramJsonBaseBean, paramJSONObject);
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      TLog.e(localIllegalArgumentException);
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      TLog.e(localIllegalAccessException);
    }
  }
  
  private void a(ClazzType paramClazzType, Field paramField, DataBase paramDataBase, JsonBaseBean paramJsonBaseBean, JSONObject paramJSONObject)
  {
    switch (1.a[paramClazzType.ordinal()])
    {
    case 1: 
    case 2: 
      paramField.setInt(paramJsonBaseBean, paramJSONObject.optInt(paramDataBase.value(), 0));
      break;
    case 3: 
    case 4: 
      paramField.setLong(paramJsonBaseBean, paramJSONObject.optLong(paramDataBase.value(), 0L));
      break;
    case 5: 
    case 6: 
      paramField.setFloat(paramJsonBaseBean, (float)paramJSONObject.optDouble(paramDataBase.value(), 0.0D));
      break;
    case 7: 
    case 8: 
      paramField.setDouble(paramJsonBaseBean, paramJSONObject.optDouble(paramDataBase.value(), 0.0D));
      break;
    case 9: 
    case 10: 
      paramField.setBoolean(paramJsonBaseBean, paramJSONObject.optInt(paramDataBase.value(), 0) > 0);
      break;
    case 11: 
      paramField.set(paramJsonBaseBean, paramJSONObject.optString(paramDataBase.value()));
      break;
    case 12: 
      paramField.set(paramJsonBaseBean, DateHelper.parseDate(paramJSONObject.optLong(paramDataBase.value(), 0L)));
      break;
    case 13: 
      paramField.set(paramJsonBaseBean, DateHelper.parseGregorianCalendar(paramJSONObject.optLong(paramDataBase.value(), 0L)));
      break;
    case 14: 
      paramField.set(paramJsonBaseBean, DateHelper.parseCalendar(paramJSONObject.optLong(paramDataBase.value(), 0L)));
      break;
    }
  }
  
  private void b(Field paramField, DataBase paramDataBase, JsonBaseBean paramJsonBaseBean, JSONObject paramJSONObject)
  {
    if (Collection.class.isAssignableFrom(paramField.getType()))
    {
      localObject = JsonHelper.getJsonArrayForDB(paramDataBase, paramJSONObject);
      a(paramField, paramJsonBaseBean, (JSONArray)localObject);
      return;
    }
    Object localObject = JsonHelper.getJsonObjectForDB(paramDataBase, paramJSONObject);
    if (Map.class.isAssignableFrom(paramField.getType()))
    {
      a(paramField, paramJsonBaseBean, (JSONObject)localObject);
      return;
    }
    JsonBaseBean localJsonBaseBean = a(paramField.getType(), (JSONObject)localObject);
    ReflectUtils.setFieldValue(paramField, paramJsonBaseBean, localJsonBaseBean);
  }
  
  private void a(Field paramField, JsonBaseBean paramJsonBaseBean, JSONArray paramJSONArray)
  {
    if ((paramField == null) || (paramJsonBaseBean == null) || (paramJSONArray == null)) {
      return;
    }
    Class localClass = ReflectUtils.genericCollectionType(paramField.getGenericType());
    if (localClass == null) {
      return;
    }
    Method localMethod = ReflectUtils.getMethod(paramField.getType(), "add", new Class[] { Object.class });
    if (localMethod == null) {
      return;
    }
    Object localObject = ReflectUtils.classInstance(paramField.getType());
    int i;
    int j;
    if (JsonBaseBean.class.isAssignableFrom(localClass))
    {
      i = 0;
      j = paramJSONArray.length();
      while (i < j)
      {
        JSONObject localJSONObject = JsonHelper.getJSONObject(paramJSONArray, i);
        JsonBaseBean localJsonBaseBean = a(localClass, localJSONObject);
        if (localJsonBaseBean != null) {
          ReflectUtils.reflectMethod(localMethod, localObject, new Object[] { localJsonBaseBean });
        }
        i++;
      }
    }
    else
    {
      i = 0;
      j = paramJSONArray.length();
      while (i < j)
      {
        ReflectUtils.reflectMethod(localMethod, localObject, new Object[] { paramJSONArray.opt(i) });
        i++;
      }
    }
    ReflectUtils.setFieldValue(paramField, paramJsonBaseBean, localObject);
  }
  
  private void a(Field paramField, JsonBaseBean paramJsonBaseBean, JSONObject paramJSONObject)
  {
    if ((paramField == null) || (paramJsonBaseBean == null) || (paramJSONObject == null) || (paramJSONObject.length() == 0)) {
      return;
    }
    List localList = ReflectUtils.genericCollectionTypes(paramField.getGenericType());
    if ((localList == null) || (localList.size() != 2)) {
      return;
    }
    Method localMethod = ReflectUtils.getMethod(paramField.getType(), "put", new Class[] { Object.class, Object.class });
    if (localMethod == null) {
      return;
    }
    Object localObject = ReflectUtils.classInstance(paramField.getType());
    if (localObject == null) {
      return;
    }
    JSONArray localJSONArray = paramJSONObject.names();
    int i = 0;
    int j = localJSONArray.length();
    while (i < j)
    {
      ReflectUtils.reflectMethod(localMethod, localObject, new Object[] { localJSONArray.optString(i), paramJSONObject.optString(localJSONArray.optString(i)) });
      i++;
    }
    ReflectUtils.setFieldValue(paramField, paramJsonBaseBean, localObject);
  }
  
  private JsonBaseBean a(Class<?> paramClass, JSONObject paramJSONObject)
  {
    if ((paramClass == null) || (paramJSONObject == null)) {
      return null;
    }
    if (!JsonBaseBean.class.isAssignableFrom(paramClass)) {
      return null;
    }
    JsonBaseBean localJsonBaseBean = (JsonBaseBean)ReflectUtils.classInstance(paramClass);
    if (localJsonBaseBean != null) {
      localJsonBaseBean.setJson(paramJSONObject);
    }
    return localJsonBaseBean;
  }
  
  public JSONObject buildJson(JsonBaseBean paramJsonBaseBean)
  {
    if (paramJsonBaseBean == null) {
      return null;
    }
    JSONObject localJSONObject = new JSONObject();
    Iterator localIterator = this.a.entrySet().iterator();
    while (localIterator.hasNext())
    {
      Map.Entry localEntry = (Map.Entry)localIterator.next();
      Field localField = ReflectUtils.reflectField(paramJsonBaseBean.getClass(), (String)localEntry.getKey());
      c(localField, (DataBase)localEntry.getValue(), paramJsonBaseBean, localJSONObject);
    }
    return localJSONObject;
  }
  
  private void c(Field paramField, DataBase paramDataBase, JsonBaseBean paramJsonBaseBean, JSONObject paramJSONObject)
  {
    if ((paramField == null) || (paramJsonBaseBean == null)) {
      return;
    }
    Object localObject = ReflectUtils.getFieldValue(paramField, paramJsonBaseBean);
    if (localObject == null) {
      return;
    }
    ClazzType localClazzType = ClazzType.getType(paramField.getType().hashCode());
    if (localClazzType == null)
    {
      a(paramField, paramDataBase, localObject, paramJSONObject);
      return;
    }
    a(localClazzType, paramDataBase, localObject, paramJSONObject);
  }
  
  private void a(Field paramField, DataBase paramDataBase, Object paramObject, JSONObject paramJSONObject)
  {
    if ((paramObject instanceof Collection))
    {
      b(paramField, paramDataBase, paramObject, paramJSONObject);
      return;
    }
    if ((paramObject instanceof Map))
    {
      c(paramField, paramDataBase, paramObject, paramJSONObject);
      return;
    }
    d(paramField, paramDataBase, paramObject, paramJSONObject);
  }
  
  private void b(Field paramField, DataBase paramDataBase, Object paramObject, JSONObject paramJSONObject)
  {
    if (paramField == null) {
      return;
    }
    Class localClass = ReflectUtils.genericCollectionType(paramField.getGenericType());
    if (localClass == null) {
      return;
    }
    JSONArray localJSONArray = new JSONArray();
    Collection localCollection;
    Iterator localIterator;
    Object localObject;
    if (JsonBaseBean.class.isAssignableFrom(localClass))
    {
      localCollection = (Collection)paramObject;
      localIterator = localCollection.iterator();
      while (localIterator.hasNext())
      {
        localObject = (JsonBaseBean)localIterator.next();
        localJSONArray.put(((JsonBaseBean)localObject).buildJson());
      }
    }
    else
    {
      localCollection = (Collection)paramObject;
      localIterator = localCollection.iterator();
      while (localIterator.hasNext())
      {
        localObject = localIterator.next();
        localJSONArray.put(localObject);
      }
    }
    JsonHelper.putObject(paramJSONObject, paramDataBase.value(), localJSONArray);
  }
  
  private void c(Field paramField, DataBase paramDataBase, Object paramObject, JSONObject paramJSONObject)
  {
    if (paramField == null) {
      return;
    }
    JSONObject localJSONObject = new JSONObject((Map)paramObject);
    JsonHelper.putObject(paramJSONObject, paramDataBase.value(), localJSONObject);
  }
  
  private void d(Field paramField, DataBase paramDataBase, Object paramObject, JSONObject paramJSONObject)
  {
    if (!(paramObject instanceof JsonBaseBean)) {
      return;
    }
    JsonBaseBean localJsonBaseBean = (JsonBaseBean)paramObject;
    JsonHelper.putObject(paramJSONObject, paramDataBase.value(), localJsonBaseBean.buildJson());
  }
  
  private void a(ClazzType paramClazzType, DataBase paramDataBase, Object paramObject, JSONObject paramJSONObject)
  {
    switch (1.a[paramClazzType.ordinal()])
    {
    case 9: 
    case 10: 
      JsonHelper.putObject(paramJSONObject, paramDataBase.value(), ((Boolean)paramObject).booleanValue() ? "1" : "0");
      break;
    case 12: 
      JsonHelper.putObject(paramJSONObject, paramDataBase.value(), Long.valueOf(((Date)paramObject).getTime() / 1000L));
      break;
    case 13: 
      JsonHelper.putObject(paramJSONObject, paramDataBase.value(), Long.valueOf(((GregorianCalendar)paramObject).getTimeInMillis() / 1000L));
      break;
    case 14: 
      JsonHelper.putObject(paramJSONObject, paramDataBase.value(), Long.valueOf(((Calendar)paramObject).getTimeInMillis() / 1000L));
      break;
    case 11: 
    default: 
      JsonHelper.putObject(paramJSONObject, paramDataBase.value(), paramObject);
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\json\DataBaseNexus.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */