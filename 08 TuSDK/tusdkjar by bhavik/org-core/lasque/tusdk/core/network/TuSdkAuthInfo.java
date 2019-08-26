package org.lasque.tusdk.core.network;

import java.io.Serializable;
import java.util.Calendar;
import org.lasque.tusdk.core.utils.StringHelper;
import org.lasque.tusdk.core.utils.json.DataBase;
import org.lasque.tusdk.core.utils.json.JsonBaseBean;

public class TuSdkAuthInfo
  extends JsonBaseBean
  implements Serializable
{
  @DataBase("last_updated")
  public Calendar lastUpdatedDate;
  @DataBase("next_request")
  public Calendar nextCheckDate;
  @DataBase("service_expire")
  public Calendar service_expire;
  @DataBase("master_key")
  public String masterKey;
  
  public boolean isValid()
  {
    return (StringHelper.isNotBlank(this.masterKey)) && (this.masterKey.trim().length() > 11);
  }
  
  public String toString()
  {
    return "masterKey : " + this.masterKey + " nextCheckDate :" + this.nextCheckDate + " lastUpdatedDate :" + this.lastUpdatedDate;
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\network\TuSdkAuthInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */