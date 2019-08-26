package org.lasque.tusdk.modules.view.widget.smudge;

import java.util.ArrayList;
import java.util.List;

public class BrushSize
{
  public static List<SizeType> getAllBrushSizes()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(SizeType.SmallBrush);
    localArrayList.add(SizeType.MediumBrush);
    localArrayList.add(SizeType.LargeBrush);
    return localArrayList;
  }
  
  public static SizeType nextBrushSize(SizeType paramSizeType)
  {
    List localList = getAllBrushSizes();
    int i = -1;
    for (i = 0; (i < localList.size()) && (paramSizeType != localList.get(i)); i++) {}
    i++;
    if (i >= localList.size()) {
      i = 0;
    }
    return (SizeType)localList.get(i);
  }
  
  public static String nameForSize(SizeType paramSizeType)
  {
    List localList = getAllBrushSizes();
    String[] arrayOfString = { "small", "medium", "large" };
    int i = 0;
    for (i = 0; i < localList.size(); i++) {
      if (paramSizeType == localList.get(i)) {
        return arrayOfString[i];
      }
    }
    return null;
  }
  
  public static double getBrushValue(SizeType paramSizeType)
  {
    switch (1.a[paramSizeType.ordinal()])
    {
    case 1: 
      return 0.2D;
    case 2: 
      return 0.15D;
    case 3: 
      return 0.1D;
    case 4: 
      return paramSizeType.getCustomizeBrushValue() * 0.2D;
    }
    return 0.15D;
  }
  
  public static enum SizeType
  {
    private int a;
    private float b = 0.1F;
    
    private SizeType(int paramInt)
    {
      this.a = paramInt;
    }
    
    public int getValue()
    {
      return this.a;
    }
    
    public SizeType setCustomizeBrushValue(float paramFloat)
    {
      if ((paramFloat > 1.0F) || (paramFloat < 0.0F)) {
        return this;
      }
      this.b = paramFloat;
      return this;
    }
    
    public float getCustomizeBrushValue()
    {
      return this.b;
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\view\widget\smudge\BrushSize.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */