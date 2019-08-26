// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.json;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;
import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.TLog;

import java.util.HashMap;
import java.util.ArrayList;
//import org.lasque.tusdk.core.utils.StringHelper;
import org.json.JSONArray;
import org.json.JSONException;
//import org.lasque.tusdk.core.utils.TLog;
import org.json.JSONObject;

public class JsonHelper
{
    public static JSONObject json(final String s) {
        if (s == null) {
            return null;
        }
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(s);
        }
        catch (JSONException ex) {
            TLog.e((Throwable)ex, "json decode error: %s", s);
        }
        return jsonObject;
    }
    
    public static JSONArray getJSONArray(final JSONObject jsonObject, final String s) {
        if (jsonObject == null || s == null) {
            return null;
        }
        return jsonObject.optJSONArray(s);
    }
    
    public static JSONObject getJSONObject(final JSONObject jsonObject, final String s) {
        if (jsonObject == null || s == null) {
            return null;
        }
        return jsonObject.optJSONObject(s);
    }
    
    public static JSONObject getJSONObject(final JSONArray jsonArray, final int n) {
        if (jsonArray == null || n >= jsonArray.length()) {
            return null;
        }
        return jsonArray.optJSONObject(n);
    }
    
    public static JSONArray getJsonArrayForDB(final DataBase dataBase, final JSONObject jsonObject) {
        if (dataBase == null || jsonObject == null) {
            return null;
        }
        JSONArray jsonArray;
        if (dataBase.needSub()) {
            final JSONObject jsonObject2 = getJSONObject(jsonObject, dataBase.value());
            if (jsonObject2 == null || StringHelper.isEmpty(dataBase.sub())) {
                return null;
            }
            jsonArray = getJSONArray(jsonObject2, dataBase.sub());
        }
        else {
            jsonArray = getJSONArray(jsonObject, dataBase.value());
        }
        return jsonArray;
    }
    
    public static JSONObject getJsonObjectForDB(final DataBase dataBase, final JSONObject jsonObject) {
        if (dataBase == null || jsonObject == null) {
            return null;
        }
        JSONObject jsonObject2 = getJSONObject(jsonObject, dataBase.value());
        if (dataBase.needSub() && jsonObject2 != null && StringHelper.isNotEmpty(dataBase.sub())) {
            jsonObject2 = getJSONObject(jsonObject2, dataBase.sub());
        }
        return jsonObject2;
    }
    
    public static void putLong(final JSONObject jsonObject, final String s, final long l) {
        if (jsonObject == null || s == null) {
            return;
        }
        try {
            jsonObject.put(s, l);
        }
        catch (JSONException ex) {
            TLog.e((Throwable)ex, "putLong: %s | %s | %s", jsonObject, s, l);
        }
    }
    
    public static JSONObject putObject(final JSONObject jsonObject, final String s, final Object o) {
        if (jsonObject == null || s == null) {
            return jsonObject;
        }
        try {
            jsonObject.put(s, o);
        }
        catch (JSONException ex) {
            TLog.e((Throwable)ex, "putObject: %s | %s | %s", jsonObject, s, o);
        }
        return jsonObject;
    }
    
    public static ArrayList<String> toStringList(final JSONArray jsonArray) {
        if (jsonArray == null) {
            return null;
        }
        final ArrayList<String> list = new ArrayList<String>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); ++i) {
            list.add(jsonArray.optString(i));
        }
        return list;
    }
    
    public static HashMap<String, String> toHashMap(final JSONObject jsonObject) {
        if (jsonObject == null) {
            return null;
        }
        final JSONArray names = jsonObject.names();
        if (names == null || names.length() < 1) {
            return null;
        }
        final HashMap hashMap = new HashMap<String, String>(names.length());
        for (int i = 0; i < names.length(); ++i) {
            hashMap.put(names.optString(i), jsonObject.optString(names.optString(i)));
        }
        return (HashMap<String, String>)hashMap;
    }
}
