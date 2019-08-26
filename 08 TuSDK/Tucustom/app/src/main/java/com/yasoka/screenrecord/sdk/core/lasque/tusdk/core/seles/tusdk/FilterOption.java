// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.tusdk;

//import org.lasque.tusdk.core.seles.sources.SelesPicture;
import java.util.List;
//import org.lasque.tusdk.core.utils.json.JsonHelper;
import android.text.TextUtils;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.TuSdkContext;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesOutInput;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.seles.sources.SelesPicture;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.DataBase;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.JsonBaseBean;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json.JsonHelper;

import java.util.Locale;
//import org.lasque.tusdk.core.TuSdkContext;
//import org.lasque.tusdk.core.seles.sources.SelesOutInput;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.ArrayList;
//import org.lasque.tusdk.core.utils.json.DataBase;
import java.io.Serializable;
//import org.lasque.tusdk.core.utils.json.JsonBaseBean;

public class FilterOption extends JsonBaseBean implements Serializable
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
    private RunTimeTextureDelegate a;
    
    public FilterOption() {
        this.a = null;
    }
    
    public FilterOption(final JSONObject jsonObject) {
        this.a = null;
        this.deserialize(jsonObject);
    }
    
    public FilterOption(final RunTimeTextureDelegate a) {
        this.a = null;
        this.a = a;
    }
    
    public RunTimeTextureDelegate getRuntimeTextureDelegate() {
        return this.a;
    }
    
    @Override
    public boolean equals(final Object o) {
        return o != null && o instanceof FilterOption && this.id == ((FilterOption)o).id;
    }
    
    public FilterOption copy() {
        final FilterOption filterOption = new FilterOption(this.getRuntimeTextureDelegate());
        filterOption.id = this.id;
        filterOption.groupId = this.groupId;
        filterOption.code = this.code;
        filterOption.name = this.name;
        filterOption.thumb = this.thumb;
        filterOption.filterType = this.filterType;
        filterOption.texturesKeepInput = this.texturesKeepInput;
        filterOption.canDefinition = this.canDefinition;
        filterOption.encryptType = this.encryptType;
        filterOption.isInternal = this.isInternal;
        filterOption.color = this.color;
        filterOption.disableRuntime = this.disableRuntime;
        if (this.args != null && !this.args.isEmpty()) {
            filterOption.args = new HashMap<String, String>(this.args);
        }
        if (this.internalTextures != null && !this.internalTextures.isEmpty()) {
            filterOption.internalTextures = new ArrayList<String>(this.internalTextures);
        }
        return filterOption;
    }
    
    public void destroy() {
        this.a = null;
        this.textures = null;
        this.internalTextures = null;
    }
    
    public SelesOutInput getFilter() {
        return FilterLocalPackage.shared().createFilter(this);
    }
    
    public String getName() {
        String s = this.name;
        if (TuSdkContext.getStringResId(this.name) <= 0) {
            final String language = Locale.getDefault().getLanguage();
            if (language != null && !language.endsWith("zh")) {
                s = this.code;
            }
        }
        else {
            s = TuSdkContext.getString(this.name);
        }
        return TextUtils.isEmpty((CharSequence)s) ? this.name : s;
    }
    
    public void deserialize(final JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        this.id = jsonObject.optLong("id", 0L);
        this.groupId = jsonObject.optLong("group_id", 0L);
        this.code = jsonObject.optString("code");
        this.name = jsonObject.optString("name");
        this.thumb = jsonObject.optString("thumb");
        this.thumbKey = jsonObject.optString("thumb_key");
        this.filterType = jsonObject.optInt("filter_type", 0);
        this.color = jsonObject.optString("color");
        this.texturesKeepInput = (jsonObject.optInt("textures_keep_input", 0) != 0);
        this.canDefinition = (jsonObject.optInt("can_definition", 0) != 0);
        this.encryptType = jsonObject.optInt("fk_key", 0);
        this.version = jsonObject.optInt("ver", 0);
        this.vertex = jsonObject.optString("vertex");
        this.fragment = jsonObject.optString("fragment");
        this.textures = JsonHelper.toStringList(JsonHelper.getJSONArray(jsonObject, "textures"));
        this.args = JsonHelper.toHashMap(JsonHelper.getJSONObject(jsonObject, "args"));
        this.argList = jsonObject.optString("args_list");
    }
    
    public interface RunTimeTextureDelegate
    {
        List<SelesPicture> getRunTimeTextures();
    }
}
