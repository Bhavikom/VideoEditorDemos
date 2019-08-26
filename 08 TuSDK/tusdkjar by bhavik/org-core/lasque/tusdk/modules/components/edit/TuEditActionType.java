package org.lasque.tusdk.modules.components.edit;

import java.util.ArrayList;
import java.util.List;

public enum TuEditActionType
{
  private TuEditActionType() {}
  
  public static List<TuEditActionType> entryActionTypes()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(TypeCuter);
    localArrayList.add(TypeFilter);
    localArrayList.add(TypeSticker);
    return localArrayList;
  }
  
  public static List<TuEditActionType> multipleActionTypes()
  {
    ArrayList localArrayList = new ArrayList();
    localArrayList.add(TypeSticker);
    localArrayList.add(TypeText);
    localArrayList.add(TypeFilter);
    localArrayList.add(TypeSkin);
    localArrayList.add(TypeCuter);
    localArrayList.add(TypeSmudge);
    localArrayList.add(TypePaint);
    localArrayList.add(TypeAdjust);
    localArrayList.add(TypeWipeFilter);
    localArrayList.add(TypeAperture);
    localArrayList.add(TypeHDR);
    localArrayList.add(TypeHolyLight);
    localArrayList.add(TypeVignette);
    localArrayList.add(TypeSharpness);
    return localArrayList;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\components\edit\TuEditActionType.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */