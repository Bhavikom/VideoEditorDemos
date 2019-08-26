package org.lasque.tusdk.core.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class ArrayHelper
{
  public static <T> ArrayGroup<T> splitForGroupsize(List<T> paramList, int paramInt)
  {
    if (paramList == null) {
      return null;
    }
    int i = (int)Math.ceil(paramList.size() / paramInt);
    ArrayGroup localArrayGroup = new ArrayGroup(i);
    int j = paramInt;
    int k = 0;
    int m = 0;
    int n = paramList.size();
    while (k < i)
    {
      int i1 = m + j;
      if (i1 > n) {
        j = n - m;
      }
      List localList = paramList.subList(m, m + j);
      localArrayGroup.add(new ArrayList(localList));
      m = i1;
      k++;
    }
    return localArrayGroup;
  }
  
  public static String join(List<String> paramList, String paramString)
  {
    return join(paramList, paramString, null, null);
  }
  
  public static String join(List<String> paramList, String paramString1, String paramString2, String paramString3)
  {
    if ((paramList == null) || (paramList.size() < 1)) {
      return "";
    }
    StringBuilder localStringBuilder = new StringBuilder();
    Iterator localIterator = paramList.iterator();
    while (localIterator.hasNext())
    {
      String str = (String)localIterator.next();
      if (paramString2 != null) {
        localStringBuilder.append(paramString2);
      }
      localStringBuilder.append(str);
      if (paramString3 != null) {
        localStringBuilder.append(paramString3);
      }
      localStringBuilder.append(paramString1);
    }
    localStringBuilder.deleteCharAt(localStringBuilder.length() - 1);
    return localStringBuilder.toString();
  }
  
  public static boolean contains(int[] paramArrayOfInt, int paramInt)
  {
    for (int k : paramArrayOfInt) {
      if (k == paramInt) {
        return true;
      }
    }
    return false;
  }
  
  public static int[] toIntArray(List<Integer> paramList)
  {
    if (paramList == null) {
      return null;
    }
    int[] arrayOfInt = new int[paramList.size()];
    int i = 0;
    int j = arrayOfInt.length;
    while (i < j)
    {
      if ((paramList != null) && (paramList.get(i) != null)) {
        arrayOfInt[i] = ((Integer)paramList.get(i)).intValue();
      }
      i++;
    }
    return arrayOfInt;
  }
  
  public static <T> T[] toArray(Collection<T> paramCollection, Class<?> paramClass)
  {
    if (paramCollection == null) {
      return null;
    }
    Object[] arrayOfObject = ReflectUtils.arrayInstance(paramClass, paramCollection.size());
    if (arrayOfObject == null) {
      return null;
    }
    return paramCollection.toArray(arrayOfObject);
  }
  
  public static <T> List<T> toList(T[] paramArrayOfT, int[] paramArrayOfInt)
  {
    if ((paramArrayOfT == null) || (paramArrayOfInt == null)) {
      return null;
    }
    ArrayList localArrayList = new ArrayList(paramArrayOfInt.length);
    for (int i = 0; i < paramArrayOfInt.length; i++) {
      localArrayList.add(paramArrayOfT[paramArrayOfInt[i]]);
    }
    return localArrayList;
  }
  
  public static class ArrayGroup<T>
    extends ArrayList<List<T>>
  {
    public ArrayGroup() {}
    
    public ArrayGroup(Collection<? extends List<T>> paramCollection)
    {
      super();
    }
    
    public ArrayGroup(int paramInt)
    {
      super();
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\ArrayHelper.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */