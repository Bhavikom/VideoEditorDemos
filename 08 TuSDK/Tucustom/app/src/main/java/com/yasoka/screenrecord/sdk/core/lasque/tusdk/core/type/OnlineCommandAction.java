// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.type;

import java.util.HashMap;
import android.annotation.SuppressLint;
import java.util.Map;

public enum OnlineCommandAction
{
    ActionUnknown(0), 
    ActionDefault(1), 
    ActionCancel(2), 
    ActionSelect(3), 
    ActionDetail(4);
    
    private int a;
    @SuppressLint({ "UseSparseArrays" })
    private static final Map<Integer, OnlineCommandAction> b;
    
    private OnlineCommandAction(final int a) {
        this.a = a;
    }
    
    public int getFlag() {
        return this.a;
    }
    
    public static OnlineCommandAction getType(final int i) {
        OnlineCommandAction actionUnknown = OnlineCommandAction.b.get(i);
        if (actionUnknown == null) {
            actionUnknown = OnlineCommandAction.ActionUnknown;
        }
        return actionUnknown;
    }
    
    static {
        b = new HashMap<Integer, OnlineCommandAction>();
        for (final OnlineCommandAction onlineCommandAction : values()) {
            OnlineCommandAction.b.put(onlineCommandAction.getFlag(), onlineCommandAction);
        }
    }
}
