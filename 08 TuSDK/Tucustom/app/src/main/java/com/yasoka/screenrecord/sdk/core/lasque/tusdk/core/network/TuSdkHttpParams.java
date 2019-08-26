// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.network;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.http.RequestParams;

import java.util.Map;
//import org.lasque.tusdk.core.http.RequestParams;

public class TuSdkHttpParams extends RequestParams
{
    public TuSdkHttpParams() {
    }
    
    public TuSdkHttpParams(final Map<String, String> map) {
        super(map);
    }
    
    public TuSdkHttpParams(final Object... array) {
        super(array);
    }
    
    public TuSdkHttpParams(final String s, final String s2) {
        super(s, s2);
    }
    
    public TuSdkHttpParams append(final String s, final Object obj) {
        if (s == null || obj == null) {
            return this;
        }
        this.put(s, String.valueOf(obj));
        return this;
    }
    
    public TuSdkHttpParams append(final Object... array) {
        final int length = array.length;
        if (length % 2 != 0) {
            throw new IllegalArgumentException("Supplied arguments must be even");
        }
        for (int i = 0; i < length; i += 2) {
            if (array[i] != null) {
                if (array[i + 1] != null) {
                    this.put(String.valueOf(array[i]), String.valueOf(array[i + 1]));
                }
            }
        }
        return this;
    }
    
    public String getUrlParam(final String key) {
        if (key == null) {
            return null;
        }
        return this.mUrlParams.get(key);
    }
}
