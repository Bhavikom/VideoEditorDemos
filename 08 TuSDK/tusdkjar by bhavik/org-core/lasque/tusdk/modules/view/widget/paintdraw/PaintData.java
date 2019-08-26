package org.lasque.tusdk.modules.view.widget.paintdraw;

import java.io.Serializable;
import org.lasque.tusdk.core.utils.json.JsonBaseBean;

public class PaintData
  extends JsonBaseBean
  implements Serializable
{
  private Object a;
  private PaintType b;
  
  public PaintData(int paramInt, PaintType paramPaintType)
  {
    this.b = paramPaintType;
    this.a = Integer.valueOf(paramInt);
  }
  
  public Object getData()
  {
    return this.a;
  }
  
  public void setData(Object paramObject)
  {
    this.a = paramObject;
  }
  
  public PaintType getPaintType()
  {
    return this.b;
  }
  
  public void setPaintType(PaintType paramPaintType)
  {
    this.b = paramPaintType;
  }
  
  public static enum PaintType
  {
    private PaintType() {}
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\view\widget\paintdraw\PaintData.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */