package org.lasque.tusdk.core.network;

import java.io.Serializable;
import java.util.Calendar;
import org.lasque.tusdk.core.utils.json.DataBase;
import org.lasque.tusdk.core.utils.json.JsonBaseBean;

public class TuSdkConfigResult
  extends JsonBaseBean
  implements Serializable
{
  @DataBase("last_updated")
  public Calendar lastUpdatedDate;
  @DataBase("next_request")
  public Calendar nextCheckDate;
  @DataBase("master_key")
  public String masterKey;
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\network\TuSdkConfigResult.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */