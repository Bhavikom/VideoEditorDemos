// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ReflectUtils;

import java.util.ArrayList;
//import org.lasque.tusdk.core.utils.ReflectUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class JsonWrapper
{
    protected JSONObject mJson;
    
    public JsonWrapper(final String s) {
        this(JsonHelper.json(s));
    }
    
    public JsonWrapper(final JSONObject mJson) {
        this.mJson = mJson;
    }
    
    public JSONObject getJson() {
        return this.mJson;
    }
    
    public JSONObject getJson(final String s) {
        return JsonHelper.getJSONObject(this.mJson, s);
    }
    
    public JSONArray getJsonArray(final String s) {
        return JsonHelper.getJSONArray(this.mJson, s);
    }
    
    public <T extends JsonBaseBean> T getJsonWithType(final String s, final Class<T> clazz) {
        return this.getJsonWithType(this.getJson(s), clazz);
    }
    
    public <T extends JsonBaseBean> T getJsonWithType(final Class<T> clazz) {
        return this.getJsonWithType(this.getJson(), clazz);
    }
    
    public <T extends JsonBaseBean> T getJsonWithType(final JSONObject json, final Class<T> clazz) {
        if (clazz == null || json == null) {
            return null;
        }
        final JsonBaseBean jsonBaseBean = ReflectUtils.classInstance(clazz);
        if (jsonBaseBean == null) {
            return null;
        }
        jsonBaseBean.setJson(json);
        return (T)jsonBaseBean;
    }
    
    public <T extends JsonBaseBean> T getJsonSubWithType(final String s, final Class<T> clazz) {
        if (clazz == null) {
            return null;
        }
        final JSONObject a = this.a(this.mJson, s);
        if (a == null) {
            return null;
        }
        final JsonBaseBean jsonBaseBean = ReflectUtils.classInstance(clazz);
        if (jsonBaseBean == null) {
            return null;
        }
        jsonBaseBean.setJson(a);
        return (T)jsonBaseBean;
    }
    
    private JSONObject a(final JSONObject jsonObject, final String s) {
        if (jsonObject == null) {
            return null;
        }
        final String[] split = s.split("\\.", 2);
        if (split.length == 2) {
            return this.a(JsonHelper.getJSONObject(jsonObject, split[0]), split[1]);
        }
        return JsonHelper.getJSONObject(jsonObject, s);
    }
    
    public <T extends JsonBaseBean> ArrayList<T> getJsonArrayWithType(final String s, final Class<T> clazz) {
        if (clazz == null) {
            return null;
        }
        final JSONArray jsonArray = this.getJsonArray(s);
        if (jsonArray == null) {
            return null;
        }
        final ArrayList list = new ArrayList<JsonBaseBean>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); ++i) {
            final JSONObject jsonObject = JsonHelper.getJSONObject(jsonArray, i);
            if (jsonObject != null) {
                final JsonBaseBean e = ReflectUtils.classInstance(clazz);
                if (e != null) {
                    e.setJson(jsonObject);
                    list.add(e);
                }
            }
        }
        return (ArrayList<T>)list;
    }
    
    public static <T extends JsonBaseBean> T deserialize(final String s, final Class<T> clazz) {
        return new JsonWrapper(s).getJsonWithType(clazz);
    }
    
    public static <T extends JsonBaseBean> T deserialize(final JSONObject jsonObject, final Class<T> clazz) {
        return new JsonWrapper(jsonObject).getJsonWithType(clazz);
    }
    
    public static <T extends JsonBaseBean> T deserialize(final JSONObject jsonObject, final Class<T> clazz, final String s) {
        return new JsonWrapper(jsonObject).getJsonSubWithType(s, clazz);
    }
    
    public static <T extends JsonBaseBean> ArrayList<T> deserializeArray(final String s, final String s2, final Class<T> clazz) {
        return new JsonWrapper(s2).getJsonArrayWithType(s, clazz);
    }
    
    public static <T extends JsonBaseBean> ArrayList<T> deserializeArray(final String s, final JSONObject jsonObject, final Class<T> clazz) {
        return new JsonWrapper(jsonObject).getJsonArrayWithType(s, clazz);
    }
}
