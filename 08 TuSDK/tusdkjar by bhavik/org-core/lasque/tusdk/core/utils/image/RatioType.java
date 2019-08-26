package org.lasque.tusdk.core.utils.image;

import java.util.ArrayList;
import org.lasque.tusdk.modules.components.ComponentActType;

public class RatioType
{
  public static final int ratio_orgin = 1;
  public static final int ratio_1_1 = 2;
  public static final int ratio_2_3 = 4;
  public static final int ratio_3_4 = 8;
  public static final int ratio_9_16 = 16;
  public static final int ratio_3_2 = 32;
  public static final int ratio_4_3 = 64;
  public static final int ratio_16_9 = 128;
  public static final int ratio_all = 255;
  public static final int ratio_default = 31;
  public static final int[] ratioTypes = { 1, 2, 4, 8, 16, 32, 64, 128 };
  public static final int[] defaultRatioTypes = { 1, 2, 4, 8, 16 };
  
  public static float ratio(int paramInt)
  {
    switch (paramInt)
    {
    case 2: 
      return 1.0F;
    case 4: 
      return 0.6666667F;
    case 8: 
      return 0.75F;
    case 16: 
      return 0.5625F;
    case 32: 
      return 1.5F;
    case 64: 
      return 1.3333334F;
    case 128: 
      return 1.7777778F;
    }
    return 0.0F;
  }
  
  public static int radioType(float paramFloat)
  {
    int i = (int)Math.floor(paramFloat * 100.0F);
    for (int m : ratioTypes)
    {
      int n = (int)Math.floor(ratio(m) * 100.0F);
      if (n == i) {
        return m;
      }
    }
    return 1;
  }
  
  public static int[] getRatioTypesByValue(int paramInt)
  {
    ArrayList localArrayList = new ArrayList();
    for (int k : ratioTypes) {
      if (k == (k & paramInt)) {
        localArrayList.add(Integer.valueOf(k));
      }
    }
    return a(localArrayList);
  }
  
  public static int[] validRatioTypes(int[] paramArrayOfInt)
  {
    ArrayList localArrayList = new ArrayList();
    int i = 0;
    for (i = 0; i < paramArrayOfInt.length; i++)
    {
      int j = paramArrayOfInt[i];
      if ((j == 1) || (ratio(j) != 0.0F)) {
        localArrayList.add(Integer.valueOf(j));
      }
    }
    int[] arrayOfInt = null;
    if (localArrayList.size() == 0) {
      arrayOfInt = ratioTypes;
    } else {
      arrayOfInt = a(localArrayList);
    }
    return arrayOfInt;
  }
  
  private static int[] a(ArrayList<Integer> paramArrayList)
  {
    int[] arrayOfInt = null;
    if ((paramArrayList != null) && (paramArrayList.size() > 0))
    {
      arrayOfInt = new int[paramArrayList.size()];
      for (int i = 0; i < paramArrayList.size(); i++) {
        arrayOfInt[i] = ((Integer)paramArrayList.get(i)).intValue();
      }
    }
    return arrayOfInt;
  }
  
  public static int firstRatioType(int paramInt)
  {
    if ((paramInt <= 0) || (paramInt == 255)) {
      return 1;
    }
    int i = 1;
    for (int m : ratioTypes) {
      if (m == (m & paramInt))
      {
        i = m;
        break;
      }
    }
    return i;
  }
  
  public static float firstRatio(int paramInt)
  {
    int i = firstRatioType(paramInt);
    return ratio(i);
  }
  
  public static int ratioCount(int paramInt)
  {
    if (paramInt <= 0) {
      return 1;
    }
    int i = 0;
    for (int m : ratioTypes) {
      if (m == (m & paramInt)) {
        i++;
      }
    }
    return i;
  }
  
  public static int nextRatioType(int paramInt1, int paramInt2, int paramInt3)
  {
    int i = nextRatioType(paramInt1, paramInt2);
    if ((paramInt3 > 0) && (i == paramInt3)) {
      return nextRatioType(paramInt1, i);
    }
    return i;
  }
  
  public static int nextRatioType(int paramInt1, int paramInt2)
  {
    int i = ratioCount(paramInt1);
    if (i < 2) {
      return paramInt2;
    }
    int[] arrayOfInt1 = new int[i];
    int j = 0;
    int k = 0;
    for (int i1 : ratioTypes) {
      if (i1 == (i1 & paramInt1))
      {
        arrayOfInt1[j] = i1;
        if (paramInt2 == i1) {
          k = j;
        }
        j++;
      }
    }
    if (k + 1 < i) {
      return arrayOfInt1[(k + 1)];
    }
    return arrayOfInt1[0];
  }
  
  public static long ratioActionType(int paramInt)
  {
    switch (paramInt)
    {
    case 2: 
      return ComponentActType.editCuter_action_ratio_1_1;
    case 4: 
      return ComponentActType.editCuter_action_ratio_2_3;
    case 8: 
      return ComponentActType.editCuter_action_ratio_3_4;
    case 16: 
      return ComponentActType.editCuter_action_ratio_9_16;
    case 32: 
      return ComponentActType.editCuter_action_ratio_3_2;
    case 64: 
      return ComponentActType.editCuter_action_ratio_4_3;
    case 128: 
      return ComponentActType.editCuter_action_ratio_16_9;
    }
    return ComponentActType.editCuter_action_ratio_orgin;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\utils\image\RatioType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */