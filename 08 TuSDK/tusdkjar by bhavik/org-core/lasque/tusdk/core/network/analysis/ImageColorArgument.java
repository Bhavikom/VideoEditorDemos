package org.lasque.tusdk.core.network.analysis;

import java.io.Serializable;
import org.lasque.tusdk.core.utils.json.DataBase;
import org.lasque.tusdk.core.utils.json.JsonBaseBean;

public class ImageColorArgument
  extends JsonBaseBean
  implements Serializable
{
  @DataBase("maxR")
  public float maxR;
  @DataBase("maxG")
  public float maxG;
  @DataBase("maxB")
  public float maxB;
  @DataBase("maxY")
  public float maxY;
  @DataBase("minR")
  public float minR;
  @DataBase("minG")
  public float minG;
  @DataBase("minB")
  public float minB;
  @DataBase("minY")
  public float minY;
  @DataBase("midR")
  public float midR;
  @DataBase("midG")
  public float midG;
  @DataBase("midB")
  public float midB;
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\network\analysis\ImageColorArgument.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */