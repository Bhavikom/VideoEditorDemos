package org.lasque.tusdk.core.network.analysis;

import java.io.Serializable;
import org.lasque.tusdk.core.utils.json.DataBase;
import org.lasque.tusdk.core.utils.json.JsonBaseBean;

public class ImageMarkFaceResult
  extends JsonBaseBean
  implements Serializable
{
  @DataBase("count")
  public int count;
  @DataBase("items")
  public ImageMark5FaceArgument<ImageMark5FaceArgument.ImageItems> items;
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\network\analysis\ImageMarkFaceResult.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */