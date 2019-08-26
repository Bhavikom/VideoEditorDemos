package org.lasque.tusdk.modules.view.widget.smudge;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.lasque.tusdk.core.utils.json.DataBase;
import org.lasque.tusdk.core.utils.json.JsonBaseBean;
import org.lasque.tusdk.core.utils.json.JsonHelper;

public class BrushGroup
  extends JsonBaseBean
  implements Serializable
{
  @DataBase("id")
  public long groupId;
  @DataBase("file")
  public String file;
  @DataBase("valid_type")
  public int validType;
  @DataBase("valid_key")
  public String validKey;
  @DataBase("name")
  public String name;
  @DataBase("brushes")
  public ArrayList<BrushData> brushes;
  public boolean isDownload;
  
  public BrushGroup() {}
  
  public BrushGroup(JSONObject paramJSONObject)
  {
    deserialize(paramJSONObject);
  }
  
  public BrushGroup copy()
  {
    BrushGroup localBrushGroup = new BrushGroup();
    localBrushGroup.groupId = this.groupId;
    localBrushGroup.name = this.name;
    localBrushGroup.isDownload = this.isDownload;
    if (this.brushes == null) {
      return localBrushGroup;
    }
    localBrushGroup.brushes = new ArrayList(this.brushes.size());
    Iterator localIterator = this.brushes.iterator();
    while (localIterator.hasNext())
    {
      BrushData localBrushData = (BrushData)localIterator.next();
      localBrushGroup.brushes.add(localBrushData.copy());
    }
    return localBrushGroup;
  }
  
  public BrushData getBrush(long paramLong)
  {
    if (this.brushes == null) {
      return null;
    }
    Iterator localIterator = this.brushes.iterator();
    while (localIterator.hasNext())
    {
      BrushData localBrushData = (BrushData)localIterator.next();
      if (localBrushData.brushId == paramLong) {
        return localBrushData;
      }
    }
    return null;
  }
  
  public void deserialize(JSONObject paramJSONObject)
  {
    if (paramJSONObject == null) {
      return;
    }
    this.groupId = paramJSONObject.optLong("id", 0L);
    this.file = paramJSONObject.optString("file");
    this.validType = paramJSONObject.optInt("valid_type", 0);
    this.validKey = paramJSONObject.optString("valid_key");
    this.name = paramJSONObject.optString("name");
    JSONArray localJSONArray = JsonHelper.getJSONArray(paramJSONObject, "brushes");
    if ((localJSONArray != null) && (localJSONArray.length() > 0))
    {
      this.brushes = new ArrayList(localJSONArray.length());
      int i = 0;
      int j = localJSONArray.length();
      while (i < j)
      {
        JSONObject localJSONObject = localJSONArray.optJSONObject(i);
        this.brushes.add(new BrushData(localJSONObject));
        i++;
      }
    }
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\modules\view\widget\smudge\BrushGroup.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */