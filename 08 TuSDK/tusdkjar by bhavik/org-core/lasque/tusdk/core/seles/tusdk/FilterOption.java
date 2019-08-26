package org.lasque.tusdk.core.seles.tusdk;

import android.text.TextUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import org.json.JSONObject;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.lasque.tusdk.core.seles.sources.SelesPicture;
import org.lasque.tusdk.core.utils.json.DataBase;
import org.lasque.tusdk.core.utils.json.JsonBaseBean;
import org.lasque.tusdk.core.utils.json.JsonHelper;

public class FilterOption
  extends JsonBaseBean
  implements Serializable
{
  @DataBase("id")
  public long id;
  @DataBase("group_id")
  public long groupId;
  @DataBase("code")
  public String code;
  @DataBase("name")
  public String name;
  @DataBase("thumb")
  public String thumb;
  @DataBase("thumb_key")
  public String thumbKey;
  @DataBase("filter_type")
  public int filterType;
  @DataBase("textures")
  public ArrayList<String> textures;
  @DataBase("textures_keep_input")
  public boolean texturesKeepInput;
  @DataBase("can_definition")
  public boolean canDefinition;
  @DataBase("fk_key")
  public int encryptType;
  @DataBase("color")
  public String color;
  @DataBase("args")
  public HashMap<String, String> args;
  @DataBase("args_list")
  public String argList;
  @DataBase("vertex")
  public String vertex;
  @DataBase("fragment")
  public String fragment;
  @DataBase("ver")
  public int version;
  @DataBase("un_real_time")
  public boolean disableRuntime;
  public boolean isInternal;
  public ArrayList<String> internalTextures;
  private RunTimeTextureDelegate a = null;
  
  public FilterOption() {}
  
  public FilterOption(JSONObject paramJSONObject)
  {
    deserialize(paramJSONObject);
  }
  
  public FilterOption(RunTimeTextureDelegate paramRunTimeTextureDelegate)
  {
    this.a = paramRunTimeTextureDelegate;
  }
  
  public RunTimeTextureDelegate getRuntimeTextureDelegate()
  {
    return this.a;
  }
  
  public boolean equals(Object paramObject)
  {
    if ((paramObject == null) || (!(paramObject instanceof FilterOption))) {
      return false;
    }
    return this.id == ((FilterOption)paramObject).id;
  }
  
  public FilterOption copy()
  {
    FilterOption localFilterOption = new FilterOption(getRuntimeTextureDelegate());
    localFilterOption.id = this.id;
    localFilterOption.groupId = this.groupId;
    localFilterOption.code = this.code;
    localFilterOption.name = this.name;
    localFilterOption.thumb = this.thumb;
    localFilterOption.filterType = this.filterType;
    localFilterOption.texturesKeepInput = this.texturesKeepInput;
    localFilterOption.canDefinition = this.canDefinition;
    localFilterOption.encryptType = this.encryptType;
    localFilterOption.isInternal = this.isInternal;
    localFilterOption.color = this.color;
    localFilterOption.disableRuntime = this.disableRuntime;
    if ((this.args != null) && (!this.args.isEmpty())) {
      localFilterOption.args = new HashMap(this.args);
    }
    if ((this.internalTextures != null) && (!this.internalTextures.isEmpty())) {
      localFilterOption.internalTextures = new ArrayList(this.internalTextures);
    }
    return localFilterOption;
  }
  
  public void destroy()
  {
    this.a = null;
    this.textures = null;
    this.internalTextures = null;
  }
  
  public SelesOutInput getFilter()
  {
    return FilterLocalPackage.shared().createFilter(this);
  }
  
  public String getName()
  {
    String str1 = this.name;
    int i = TuSdkContext.getStringResId(this.name);
    if (i <= 0)
    {
      Locale localLocale = Locale.getDefault();
      String str2 = localLocale.getLanguage();
      if ((str2 != null) && (!str2.endsWith("zh"))) {
        str1 = this.code;
      }
    }
    else
    {
      str1 = TuSdkContext.getString(this.name);
    }
    return TextUtils.isEmpty(str1) ? this.name : str1;
  }
  
  public void deserialize(JSONObject paramJSONObject)
  {
    if (paramJSONObject == null) {
      return;
    }
    this.id = paramJSONObject.optLong("id", 0L);
    this.groupId = paramJSONObject.optLong("group_id", 0L);
    this.code = paramJSONObject.optString("code");
    this.name = paramJSONObject.optString("name");
    this.thumb = paramJSONObject.optString("thumb");
    this.thumbKey = paramJSONObject.optString("thumb_key");
    this.filterType = paramJSONObject.optInt("filter_type", 0);
    this.color = paramJSONObject.optString("color");
    this.texturesKeepInput = (paramJSONObject.optInt("textures_keep_input", 0) != 0);
    this.canDefinition = (paramJSONObject.optInt("can_definition", 0) != 0);
    this.encryptType = paramJSONObject.optInt("fk_key", 0);
    this.version = paramJSONObject.optInt("ver", 0);
    this.vertex = paramJSONObject.optString("vertex");
    this.fragment = paramJSONObject.optString("fragment");
    this.textures = JsonHelper.toStringList(JsonHelper.getJSONArray(paramJSONObject, "textures"));
    this.args = JsonHelper.toHashMap(JsonHelper.getJSONObject(paramJSONObject, "args"));
    this.argList = paramJSONObject.optString("args_list");
  }
  
  public static abstract interface RunTimeTextureDelegate
  {
    public abstract List<SelesPicture> getRunTimeTextures();
  }
}


/* Location:              C:\Users\OM\Desktop\tusdkjar\TuSDKCore-3.1.0.jar!\org\lasque\tusdk\core\seles\tusdk\FilterOption.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       0.7.1
 */