// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.type;

import com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.utils.StringHelper;

import java.util.HashMap;
//import org.lasque.tusdk.core.utils.StringHelper;
import java.util.Map;

public enum SoundType
{
    TypeUnknown("unknown", 0), 
    TypeDefault("default", 1), 
    TypeShock("shock", 2), 
    TypeVoice("voice", 3);
    
    private String a;
    private int b;
    private static final Map<String, SoundType> c;
    
    private SoundType(final String a, final int b) {
        this.a = a;
        this.b = b;
    }
    
    public String getFlag() {
        return this.a;
    }
    
    public int getNum() {
        return this.b;
    }
    
    public static SoundType getType(final String s) {
        SoundType typeUnknown = null;
        if (StringHelper.isNotEmpty(s)) {
            typeUnknown = SoundType.c.get(s);
        }
        if (typeUnknown == null) {
            typeUnknown = SoundType.TypeUnknown;
        }
        return typeUnknown;
    }
    
    static {
        c = new HashMap<String, SoundType>();
        for (final SoundType soundType : values()) {
            SoundType.c.put(soundType.getFlag(), soundType);
        }
    }
}
