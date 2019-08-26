package org.lasque.tusdk.core.utils;

import android.util.Base64;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import org.lasque.tusdk.core.type.ResourceType;

public class ReflectUtils
{
  public static int getResource(Class<?> paramClass, ResourceType paramResourceType, String paramString)
  {
    if ((paramClass == null) || (paramResourceType == null) || (paramString == null)) {
      return 0;
    }
    Class localClass = subClass(paramClass, paramResourceType.getKey());
    if (localClass == null) {
      return 0;
    }
    int i = getResourceFieldValue(localClass, paramString);
    return i;
  }
  
  public static int getResourceFieldValue(Class<?> paramClass, String paramString)
  {
    Field localField = getField(paramClass, paramString);
    if (localField == null) {
      return 0;
    }
    try
    {
      Object localObject = localField.get(paramClass);
      int i = ((Integer)localObject).intValue();
      return i;
    }
    catch (Exception localException)
    {
      TLog.e(localException, "getResourceFieldValue: %s | %s", new Object[] { paramClass, paramString });
    }
    return 0;
  }
  
  public static Field getField(Class<?> paramClass, String paramString)
  {
    if ((paramClass == null) || (paramString == null)) {
      return null;
    }
    Field[] arrayOfField1 = paramClass.getFields();
    if (arrayOfField1 == null) {
      return null;
    }
    for (Field localField : arrayOfField1) {
      if (localField.getName().equalsIgnoreCase(paramString)) {
        return localField;
      }
    }
    return null;
  }
  
  public static Class<?> subClass(Class<?> paramClass, String paramString)
  {
    Class[] arrayOfClass1 = paramClass.getDeclaredClasses();
    if ((paramString == null) || (arrayOfClass1 == null) || (arrayOfClass1.length == 0)) {
      return null;
    }
    for (Class localClass : arrayOfClass1) {
      if (localClass.getSimpleName().equalsIgnoreCase(paramString)) {
        return localClass;
      }
    }
    return null;
  }
  
  public static Class<?> reflectClass(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    try
    {
      Class localClass = Class.forName(paramString);
      return localClass;
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      TLog.e(localClassNotFoundException);
    }
    return null;
  }
  
  public static <T> T classInstance(String paramString)
  {
    Class localClass = reflectClass(paramString);
    return (T)classInstance(localClass);
  }
  
  public static <T> T classInstance(Class<?> paramClass)
  {
    if (paramClass == null) {
      return null;
    }
    try
    {
      Object localObject = paramClass.newInstance();
      return (T)localObject;
    }
    catch (InstantiationException localInstantiationException)
    {
      TLog.e(localInstantiationException, "classInstance", new Object[0]);
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      TLog.e(localIllegalAccessException, "classInstance", new Object[0]);
    }
    return null;
  }
  
  public static <T> T[] arrayInstance(Class<?> paramClass, int paramInt)
  {
    if ((paramClass == null) || (paramInt < 0)) {
      return null;
    }
    try
    {
      Object[] arrayOfObject = (Object[])Array.newInstance(paramClass, paramInt);
      return arrayOfObject;
    }
    catch (Exception localException)
    {
      TLog.e(localException, "arrayInstance", new Object[0]);
    }
    return null;
  }
  
  public static StringBuilder trace(Object paramObject)
  {
    if (paramObject == null) {
      return null;
    }
    StringBuilder localStringBuilder = new StringBuilder(paramObject.getClass().getName()).append(":{\n");
    Field[] arrayOfField1 = paramObject.getClass().getFields();
    for (Field localField : arrayOfField1) {
      if (!Modifier.isStatic(localField.getModifiers()))
      {
        localStringBuilder.append(localField.getName()).append(" : ");
        Object localObject = getFieldValue(localField, paramObject);
        if (localObject == null) {
          localStringBuilder.append("null");
        } else {
          localStringBuilder.append(localObject.toString());
        }
        localStringBuilder.append(", \n");
      }
    }
    localStringBuilder.append("};");
    return localStringBuilder;
  }
  
  public static Object getFieldValue(Field paramField, Object paramObject)
  {
    Object localObject = null;
    try
    {
      localObject = paramField.get(paramObject);
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      TLog.e(localIllegalArgumentException);
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      TLog.e(localIllegalAccessException);
    }
    return localObject;
  }
  
  public static Field reflectField(Class<?> paramClass, String paramString)
  {
    if ((paramClass == null) || (paramString == null)) {
      return null;
    }
    Field localField = null;
    try
    {
      localField = paramClass.getField(paramString);
    }
    catch (NoSuchFieldException localNoSuchFieldException)
    {
      TLog.e(localNoSuchFieldException);
    }
    return localField;
  }
  
  public static void setFieldValue(Field paramField, Object paramObject1, Object paramObject2)
  {
    if ((paramField == null) || (paramObject1 == null) || (paramObject2 == null)) {
      return;
    }
    try
    {
      paramField.set(paramObject1, paramObject2);
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
  
  public static Method getMethod(Class<?> paramClass, String paramString, Class<?>... paramVarArgs)
  {
    if ((paramClass == null) || (paramString == null)) {
      return null;
    }
    Method localMethod = null;
    try
    {
      localMethod = paramClass.getMethod(paramString, paramVarArgs);
    }
    catch (NoSuchMethodException localNoSuchMethodException)
    {
      TLog.e(localNoSuchMethodException);
    }
    return localMethod;
  }
  
  public static Object reflectMethod(Method paramMethod, Object paramObject, Object... paramVarArgs)
  {
    if ((paramMethod == null) || (paramObject == null)) {
      return null;
    }
    try
    {
      return paramMethod.invoke(paramObject, paramVarArgs);
    }
    catch (IllegalArgumentException localIllegalArgumentException)
    {
      TLog.e(localIllegalArgumentException);
    }
    catch (IllegalAccessException localIllegalAccessException)
    {
      TLog.e(localIllegalAccessException);
    }
    catch (InvocationTargetException localInvocationTargetException)
    {
      TLog.e(localInvocationTargetException);
    }
    return null;
  }
  
  public static Class<?> genericCollectionType(Type paramType)
  {
    List localList = genericCollectionTypes(paramType);
    if ((localList == null) || (localList.isEmpty())) {
      return null;
    }
    return (Class)localList.get(0);
  }
  
  public static List<Class<?>> genericCollectionTypes(Type paramType)
  {
    if ((paramType == null) || (!(paramType instanceof ParameterizedType))) {
      return null;
    }
    Type[] arrayOfType1 = ((ParameterizedType)paramType).getActualTypeArguments();
    if ((arrayOfType1 == null) || (arrayOfType1.length < 1)) {
      return null;
    }
    ArrayList localArrayList = new ArrayList(arrayOfType1.length);
    for (Type localType : arrayOfType1) {
      localArrayList.add((Class)localType);
    }
    return localArrayList;
  }
  
  public static String serialize(Object paramObject)
  {
    if (paramObject == null) {
      return null;
    }
    String str = null;
    ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
    try
    {
      ObjectOutputStream localObjectOutputStream = new ObjectOutputStream(localByteArrayOutputStream);
      localObjectOutputStream.writeObject(paramObject);
      str = Base64.encodeToString(localByteArrayOutputStream.toByteArray(), 0);
    }
    catch (IOException localIOException)
    {
      TLog.e(localIOException, "serialize: %s", new Object[] { paramObject });
    }
    return str;
  }
  
  public static <T> T deserialize(String paramString)
  {
    if (paramString == null) {
      return null;
    }
    byte[] arrayOfByte = Base64.decode(paramString, 0);
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(arrayOfByte);
    Object localObject = null;
    try
    {
      ObjectInputStream localObjectInputStream = new ObjectInputStream(localByteArrayInputStream);
      localObject = localObjectInputStream.readObject();
    }
    catch (StreamCorruptedException localStreamCorruptedException)
    {
      TLog.e(localStreamCorruptedException, "deserialize error", new Object[0]);
    }
    catch (IOException localIOException)
    {
      TLog.e(localIOException, "deserialize error", new Object[0]);
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      TLog.e(localClassNotFoundException, "deserialize error", new Object[0]);
    }
    return (T)localObject;
  }
  
  public static void asserts(boolean paramBoolean, String paramString)
  {
    if (!paramBoolean) {
      throw new AssertionError(paramString);
    }
  }
  
  public static <T> T notNull(T paramT, String paramString)
  {
    if (paramT == null) {
      throw new IllegalArgumentException(paramString + " should not be null!");
    }
    return paramT;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\ReflectUtils.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */