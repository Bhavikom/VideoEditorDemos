// 
// Decompiled by Procyon v0.5.36
// 

package com.yasoka.screenrecord.sdk.core.lasque.tusdk.core.type;

import java.util.HashMap;
import android.annotation.SuppressLint;
import java.util.Map;

public enum DownloadTaskStatus
{
    StatusInit(0), 
    StatusDowning(10), 
    StatusDowned(20), 
    StatusDownFailed(30), 
    StatusCancel(40), 
    StatusRemoved(100);
    
    private int a;
    @SuppressLint({ "UseSparseArrays" })
    private static final Map<Integer, DownloadTaskStatus> b;
    
    private DownloadTaskStatus(final int a) {
        this.a = a;
    }
    
    public int getFlag() {
        return this.a;
    }
    
    public static DownloadTaskStatus getType(final int i) {
        DownloadTaskStatus statusInit = DownloadTaskStatus.b.get(i);
        if (statusInit == null) {
            statusInit = DownloadTaskStatus.StatusInit;
        }
        return statusInit;
    }
    
    static {
        b = new HashMap<Integer, DownloadTaskStatus>();
        for (final DownloadTaskStatus downloadTaskStatus : values()) {
            DownloadTaskStatus.b.put(downloadTaskStatus.getFlag(), downloadTaskStatus);
        }
    }
}
