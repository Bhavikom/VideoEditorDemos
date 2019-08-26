// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.hardware;

import java.util.Iterator;
import java.util.List;
//import org.lasque.tusdk.core.utils.ReflectUtils;
import android.content.SharedPreferences;
import android.content.Context;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.ReflectUtils;

public class TuSdkSharedPreferences
{
    private Context a;
    private String b;
    
    public TuSdkSharedPreferences(final Context a, final String b) {
        this.a = a;
        this.b = b;
    }
    
    public SharedPreferences getSharedPreferences() {
        return this.a.getSharedPreferences(this.b, 0);
    }
    
    public SharedPreferences.Editor getSharedEditor() {
        return this.getSharedPreferences().edit();
    }
    
    public String loadSharedCache(final String s) {
        if (s == null) {
            return null;
        }
        return this.getSharedPreferences().getString(s, (String)null);
    }
    
    public <T> T loadSharedCacheObject(final String s) {
        final String loadSharedCache = this.loadSharedCache(s);
        if (loadSharedCache == null) {
            return null;
        }
        return (T)ReflectUtils.deserialize(loadSharedCache);
    }
    
    public void saveSharedCache(final String s, final String s2) {
        if (s == null) {
            return;
        }
        final SharedPreferences.Editor sharedEditor = this.getSharedEditor();
        if (s2 == null) {
            sharedEditor.remove(s);
        }
        else {
            sharedEditor.putString(s, s2);
        }
        sharedEditor.commit();
    }
    
    public void removeSharedCache(final List<String> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        final SharedPreferences.Editor sharedEditor = this.getSharedEditor();
        final Iterator<String> iterator = list.iterator();
        while (iterator.hasNext()) {
            sharedEditor.remove((String)iterator.next());
        }
        sharedEditor.commit();
    }
    
    public void saveSharedCacheObject(final String s, final Object o) {
        if (s == null) {
            return;
        }
        final SharedPreferences.Editor sharedEditor = this.getSharedEditor();
        if (o == null) {
            sharedEditor.remove(s);
        }
        else {
            sharedEditor.putString(s, ReflectUtils.serialize(o));
        }
        sharedEditor.commit();
    }
    
    public boolean loadSharedCacheBool(final String s) {
        return s != null && this.getSharedPreferences().getBoolean(s, false);
    }
    
    public void saveSharedCache(final String s, final boolean b) {
        if (s == null) {
            return;
        }
        final SharedPreferences.Editor sharedEditor = this.getSharedEditor();
        sharedEditor.putBoolean(s, b);
        sharedEditor.commit();
    }
    
    public long loadSharedCacheLong(final String s) {
        if (s == null) {
            return 0L;
        }
        return this.getSharedPreferences().getLong(s, 0L);
    }
    
    public void saveSharedCache(final String s, final long n) {
        if (s == null) {
            return;
        }
        final SharedPreferences.Editor sharedEditor = this.getSharedEditor();
        sharedEditor.putLong(s, n);
        sharedEditor.commit();
    }
}
