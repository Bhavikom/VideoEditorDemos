// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ReflectUtils;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;

import java.util.HashMap;
//import org.lasque.tusdk.core.utils.ReflectUtils;
//import org.lasque.tusdk.core.utils.StringHelper;
import org.json.JSONObject;
import java.util.Map;

public abstract class JsonBaseBean
{
    private static final Map<String, DataBaseNexus> a;
    
    private static DataBaseNexus a(final Class<?> clazz) {
        DataBaseNexus dataBaseNexus = JsonBaseBean.a.get(clazz.getName());
        if (dataBaseNexus == null) {
            dataBaseNexus = new DataBaseNexus(clazz);
            JsonBaseBean.a.put(clazz.getName(), dataBaseNexus);
        }
        return dataBaseNexus;
    }
    
    public void setJson(final JSONObject jsonObject) {
        if (jsonObject == null) {
            return;
        }
        a(this.getClass()).bindJson(this, jsonObject);
    }
    
    public JSONObject buildJson() {
        return a(this.getClass()).buildJson(this);
    }
    
    public JSONObject buildJson(final String s) {
        JSONObject jsonObject = this.buildJson();
        if (StringHelper.isNotEmpty(s)) {
            jsonObject = JsonHelper.putObject(new JSONObject(), s, jsonObject);
        }
        return jsonObject;
    }
    
    @Override
    public String toString() {
        final StringBuilder trace = ReflectUtils.trace(this);
        if (trace != null) {
            return trace.toString();
        }
        return super.toString();
    }
    
    static {
        a = new HashMap<String, DataBaseNexus>();
    }
}
